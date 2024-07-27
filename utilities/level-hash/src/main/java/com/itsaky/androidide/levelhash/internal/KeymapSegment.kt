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

import com.itsaky.androidide.levelhash.RandomAccessIO
import com.itsaky.androidide.levelhash.internal.PersistentKeymapIO.Companion.KEYMAP_SEGMENT_SIZE_BYTES
import com.itsaky.androidide.levelhash.util.MappedRandomAccessIO
import org.slf4j.LoggerFactory
import java.io.DataInput
import java.io.DataOutput
import java.io.RandomAccessFile
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * A segment of the key map file.
 */
internal class KeymapSegment(
  val file: RandomAccessFile,
  val path: String,
  val segmentOffset: Long,
  val segmentLength: Long,
  val io: MappedRandomAccessIO = MappedRandomAccessIO(),
) : DataInput by io, DataOutput by io, RandomAccessIO by io, AutoCloseable {

  lateinit var mappedBuffer: MappedByteBuffer

  init {
    remap(segmentOffset, segmentLength, true)
  }

  /**
   * Map a region of the backing file into memory, unmapping previous mappings,
   * if any.
   */
  @Suppress("SameParameterValue")
  private fun remap(position: Long, size: Long, forceRemap: Boolean = false
  ) {
    if (!forceRemap && (segmentOffset == position && segmentLength == size)) {
      // the given region is already mapped into memory
      return
    }

    unmap()

    this.mappedBuffer =
      file.channel.map(FileChannel.MapMode.READ_WRITE, segmentOffset,
        segmentLength)
    this.io.reset(mappedBuffer, segmentOffset, segmentLength)
  }

  /**
   * Unmap the memory mapped region, if mapped.
   */

  override fun close() {
    unmap()
  }

  /**
   * Unmap the memory mapped buffer. This [KeymapSegment] becomes invalid after
   * this operation and must not be used further unless remapped.
   *
   * @return `true` if the unmapping request was successful, `false` otherwise.
   */
  fun unmap(): Boolean {
    if (!::mappedBuffer.isInitialized) {
      return false
    }

    try {
      val cleanerF = mappedBuffer.javaClass.getDeclaredField("cleaner")
      cleanerF.isAccessible = true
      val cleaner = cleanerF.get(mappedBuffer)

      val cleanM = cleaner.javaClass.getDeclaredMethod("clean")
      cleanM.isAccessible = true

      cleanM.invoke(cleaner)
      return true
    } catch (err: Throwable) {
      log.error("Failed to unmap keymap segment. file={}, offset={}, size={}",
        path, segmentOffset, segmentLength, err)
      return false
    }
  }

  companion object {

    private val log = LoggerFactory.getLogger(KeymapSegment::class.java)

    /**
     * Create a new segment.
     *
     * @param offset The offset of the segment in the keymap file.
     * @param file The file to create the segment in.
     * @param path The path of the file (used for debugging).
     */
    fun create(offset: Long, file: RandomAccessFile, path: String
    ): KeymapSegment {
      return KeymapSegment(file = file, path = path, segmentOffset = offset,
        segmentLength = KEYMAP_SEGMENT_SIZE_BYTES)
    }
  }
}
