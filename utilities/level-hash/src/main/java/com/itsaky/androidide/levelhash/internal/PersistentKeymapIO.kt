/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.levelhash.internal

import com.itsaky.androidide.levelhash.HashT
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.MAGIC_NUMBER_SIZE_BYTES
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_INT
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_LONG
import com.itsaky.androidide.levelhash.util.FileUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.io.RandomAccessFile

/**
 * @author Akash Yadav
 */
internal class PersistentKeymapIO(indexFile: File
) : AutoCloseable {
  // TODO(itsaky): use LFU cache to limit the number of segments mapped in memory at once.
  /**
   * The segments for the keymap file.
   */
  private val keymapSegments = arrayOfNulls<KeymapSegment>(KEYMAP_MAX_SEGMENTS)
  private val keymapFile = File(indexFile.parentFile, "${indexFile.name}._i")
  private val raKeymapFile by lazy { RandomAccessFile(keymapFile, "rw") }

  private val segmentCount by lazy {
    if (!keymapFile.exists() || !keymapFile.isFile || keymapFile.length() <= KEYMAP_HEADER_SIZE_BYTES) {
      // there should be 1 segment initially
      return@lazy 1
    }

    raKeymapFile.seek(KEYMAP_HEADER_SIZE_BYTES)
    val count = raKeymapFile.readLong()
    raKeymapFile.seek(0)
    return@lazy count
  }

  init {
    BinaryFileUtils.initSparseFile(
      file = keymapFile,
      magicNumber = KEYMAP_MAGIC_NUMBER,
      maxLength = KEYMAP_HEADER_SIZE_BYTES + KEYMAP_SEGMENT_SIZE_BYTES * KEYMAP_MAX_SEGMENTS
    ) { raf ->
      var position = MAGIC_NUMBER_SIZE_BYTES
      raf.seek(position)
      raf.writeInt(0)
      position += SIZE_INT

      raf.seek(position)
      raf.writeLong(KEYMAP_SEGMENT_SIZE_BYTES)
    }
  }

  fun getSegmentOffset(index: Int): Long {
    return KEYMAP_HEADER_SIZE_BYTES + index * KEYMAP_SEGMENT_SIZE_BYTES
  }

  /**
   * Get or create a segment for the keymap file.
   */
  fun getOrCreateSegment(index: Int): KeymapSegment {
    var segment = keymapSegments[index]
    if (segment == null) {
      segment = KeymapSegment.create(getSegmentOffset(index), raKeymapFile,
        keymapFile.canonicalPath)
      keymapSegments[index] = segment
    }
    return segment
  }

  fun segmentAndPosForPos(
    levelIdx: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): Pair<KeymapSegment, Long> {
    val hash = hash(levelIdx, bucketIdx, slotIdx)
    val segmentIdx = segmentIndex(hash)
    val positionInSegment = positionInSegment(hash)
    val segment = getOrCreateSegment(segmentIdx)
    return segment to positionInSegment
  }

  fun deallocate(offset: Long, len: Long
  ) {
    FileUtils.deallocate(raKeymapFile, offset, len)
  }

  fun deallocate(segment: KeymapSegment, positionInSegment: Long, len: Long
  ) {
    FileUtils.deallocate(raKeymapFile,
      segment.segmentOffset + positionInSegment, len)
  }

  override fun close() {
    keymapSegments.forEach { segment ->
      try {
        segment?.unmap()
      } catch (err: Throwable) {
        log.error("Failed to unmap segment", err)
      }
    }

    try {
      raKeymapFile.close()
    } catch (e: Throwable) {
      log.error("Failed to close keymap file", e)
    }
  }

  companion object {

    private val log = LoggerFactory.getLogger(PersistentKeymapIO::class.java)

    /**
     * The number of bytes in the header of the keymap file that is used to store
     * the segment count.
     */
    internal const val KEYMAP_HEADER_SEGCOUNT_BYTES = SIZE_INT

    /**
     * The number of bytes in the header of the keymap file that is used to store
     * the size of the segment structures.
     */
    internal const val KEYMAP_HEADER_SEGSIZE_BYTES = SIZE_LONG

    /**
     * The number of bytes used to store the header of the keymap file.
     */
    internal const val KEYMAP_HEADER_SIZE_BYTES: Long =
      MAGIC_NUMBER_SIZE_BYTES + KEYMAP_HEADER_SEGCOUNT_BYTES + KEYMAP_HEADER_SEGSIZE_BYTES

    /**
     * The size of one segment in the key map file.
     */
    internal const val KEYMAP_SEGMENT_SIZE_BYTES: Long = 1024 * 1024 // 1MB

    /**
     * The number of bytes that are used to store an entry in a key map file.
     */
    internal const val KEYMAP_ENTRY_BYTES: Long = SIZE_LONG

    /**
     * The number of entries that can be stored in a segment in the key map file.
     */
    internal const val KEYMAP_ENTRIES_PER_SEGMENT =
      KEYMAP_SEGMENT_SIZE_BYTES / KEYMAP_ENTRY_BYTES

    /**
     * The maximum number of segments that can be stored for an index.
     */
    internal const val KEYMAP_MAX_SEGMENTS = 100

    /**
     * The maximum number of entries that can be stored in all of the keymap segments combined.
     */
    const val KEYMAP_MAX_ENTRIES =
      KEYMAP_MAX_SEGMENTS * KEYMAP_ENTRIES_PER_SEGMENT

    /**
     * Magic number that is used as the file signature to identify the keymap file.
     */
    internal const val KEYMAP_MAGIC_NUMBER = 0x414944584B

    /**
     * Get the ID of the segment that should be used to store the store the
     * given position hash.
     */
    private fun segmentIndex(positionHash: HashT): Int {
      return (positionHash % KEYMAP_MAX_SEGMENTS.toULong()).toInt()
    }

    /**
     * Get the position of the entry in the segment where the entry should be
     * written. The returned position is in bytes from the beginning of the
     * segment.
     */
    private fun positionInSegment(positionHash: HashT): Long {
      return ((positionHash % KEYMAP_ENTRIES_PER_SEGMENT.toULong()).toLong() * KEYMAP_ENTRY_BYTES)
    }

    /**
     * Get the hash of the position coordinates.
     */
    private fun hash(levelNum: Int, bucketIdx: Int, slotIdx: Int
    ): HashT {
      var hash = 17L
      hash = 31L * hash + levelNum
      hash = 31L * hash + bucketIdx
      hash = 31L * hash + slotIdx
      return hash.toULong()
    }
  }
}