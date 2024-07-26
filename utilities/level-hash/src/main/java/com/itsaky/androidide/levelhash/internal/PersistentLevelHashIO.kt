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

import com.itsaky.androidide.jna.Falloc
import com.itsaky.androidide.jna.LibC
import com.itsaky.androidide.levelhash.DataExternalizer
import com.itsaky.androidide.levelhash.HashT
import com.itsaky.androidide.levelhash.RandomAccessIO
import com.itsaky.androidide.levelhash.seekBoolean
import com.itsaky.androidide.levelhash.seekInt
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_BOOLEAN
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_INT
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_LONG
import com.itsaky.androidide.levelhash.util.FileUtils
import com.itsaky.androidide.levelhash.util.MappedRandomAccessIO
import com.itsaky.androidide.utils.FDUtils
import com.sun.jna.Native
import org.slf4j.LoggerFactory
import java.io.DataInput
import java.io.DataOutput
import java.io.File
import java.io.RandomAccessFile
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


private val logger = LoggerFactory.getLogger(PersistentLevelHashIO::class.java)

/**
 * This class is responsible for performing the IO operations for the level hash
 * backed by the disk.
 *
 * The peristent level hash is backed by three files :
 *
 * - `{name}._meta` - metadata for the level hash.
 * - `{name}._i` - maps the hash of the slot position
 *    (`hash(level_num, bucket_idx, slot_idx`) to the address of entry in the
 *    `${name}` file. This is known as the keymap.
 * - `{name}` - Contains all the variable-length entry values of the level hash.
 *
 * ## Keymap
 *
 * - The keymap file header occupies [PersistentLevelHashIO.KEYMAP_HEADER_SIZE_BYTES]
 *    bytes.
 * - Each segment is occupies [PersistentLevelHashIO.KEYMAP_SEGMENT_SIZE_BYTES]
 *    bytes.
 * - There can be at most [PersistentLevelHashIO.KEYMAP_MAX_SEGMENTS] segments in
 *    the keymap file.
 * - Each segment can store at most [PersistentLevelHashIO.KEYMAP_ENTRIES_PER_SEGMENT]
 *    key-value pairs.
 *
 * Considering the default values, a keymap file can:
 * - be `8 + 4 + 1024 * 1024 * 100 = ~100 MB` in size.
 * - hold `(1024 * 1024 * 100) / 16 = 6553600 entries`
 *
 * Structure of the keymap file:
 *
 * ```
 * keymap {
 *   u32 segment_count;
 *   u64 segment_size;
 *   segment segments[segment_count];
 *
 *   segment {
 *       u64 value_address[];
 *   }
 * }
 * ```
 *
 * - `segment_count` - The number of segments in the file.
 * - `segment_size` - The size of each segment.
 * - `segments` - The segments of the file. There are `segment_count` entries,
 *    each of exactly `segment_size` bytes.
 * - `value_address` - A 1-based 64-bit offset pointing the value entry in the values
 *    file. Note that the offset is 1-based because 0 is considered as an invalid
 *    value for the address. This helps in recognizing if an entry in the keymap
 *    points to a valid address or not. We can't really fill up the keymap entries
 *    with a negative offset as that would just take up unnecessary space.
 *
 * ## Values
 *
 * Structure of the values file :
 *
 * ```
 * values {
 *    u64 magic_number;
 *    u64 first_entry;
 *    u64 next_entry;
 *    value values[];
 *
 *    value {
 *      u8 is_occupied;
 *      u32 key_size;
 *      u8 key[key_size];
 *      u32 value_size;
 *      u8 value[value_size];
 *    }
 * }
 * ```
 * @author Akash Yadav
 */
class PersistentLevelHashIO<K : Any, V : Any?>(
  private val indexFile: File,
  private val keyExternalizer: DataExternalizer<K>,
  private val valueExternalizer: DataExternalizer<V>,
) : AutoCloseable {

  // IMPLEMENTATION NOTE
  // Whenever you access the valuesIo property, you must ensure that the
  // position is set to the desired location in the values file.
  // as the values file is randomly accessed, and by multiple functions, or
  // even multiple threads, the current position cannot be determined

  private val raIndexFile by lazy {
    RandomAccessFile(indexFile, "rw")
  }

  private val keymapFile by lazy {
    File(indexFile.parentFile, "${indexFile.name}._i")
  }

  private val raKeymapFile by lazy {
    RandomAccessFile(keymapFile, "rw")
  }

  private val metaFile by lazy {
    File(indexFile.parentFile, "${indexFile.name}._meta")
  }

  private val raMetaFile by lazy {
    RandomAccessFile(metaFile, "rw")
  }

  private val segmentCount by lazy {
    if (!keymapFile.exists() || !keymapFile.isFile || keymapFile.length() <= KEYMAP_HEADER_SIZE_BYTES) {
      // there should be 1 segment initially
      return@lazy 1
    }

    raKeymapFile.seek(KEYMAP_HEADER_SIZE_BYTES.toLong())
    val count = raKeymapFile.readLong()
    raKeymapFile.seek(0)
    return@lazy count
  }

  // TODO(itsaky): use LFU cache to limit the number of segments mapped in memory at once.
  /**
   * The segments for the keymap file.
   */
  private val keymapSegments = arrayOfNulls<KeymapSegment>(KEYMAP_MAX_SEGMENTS)

  private var valuesFirstEntryAt = 0L
  private var valuesLastEntryAt = 0L
  private var valuesWriteAt = 0L
  private var valuesMapSize = 0L
  private var valuesIo = MappedRandomAccessIO()

  init {
    if (indexFile.exists()) {
      if (indexFile.isDirectory) {
        indexFile.deleteRecursively()
      } else if (indexFile.isFile && indexFile.length() < VALUES_HEADER_SIZE_BYTES) {
        indexFile.delete()
      }
    } else {
      indexFile.createNewFile()
    }

    // TODO(itsaky): Load the below values from file when the file already exists

    valuesLastEntryAt = -1
    valuesWriteAt = 0
    valuesFirstEntryAt = VALUES_HEADER_SIZE_BYTES
    valuesMapSize = VALUES_MAX_MAP_SIZE_BYTES

    // the header region is not memory mapped
    log.debug("mmap values file: offset={}, size={}", VALUES_HEADER_SIZE_BYTES,
      valuesMapSize)
    val buffer = raIndexFile.channel.map(FileChannel.MapMode.READ_WRITE,
      VALUES_HEADER_SIZE_BYTES, valuesMapSize)
    valuesIo.reset(buffer, 0L, valuesMapSize)
  }

  private fun getValuesRealOffset(offset: Long) =
    VALUES_HEADER_SIZE_BYTES + offset

  private fun getSegmentOffset(index: Int): Long {
    return KEYMAP_HEADER_SIZE_BYTES + index * KEYMAP_SEGMENT_SIZE_BYTES
  }

  /**
   * Get or create a segment for the keymap file.
   */
  private fun getOrCreateSegment(index: Int): KeymapSegment {
    var segment = keymapSegments[index]
    if (segment == null) {
      segment = KeymapSegment.create(getSegmentOffset(index), raKeymapFile,
        keymapFile.canonicalPath)
      keymapSegments[index] = segment
    }
    return segment
  }

  private fun segmentAndPosForPos(
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

  /**
   * Get the address of the value entry in the values for the given slot
   * position.
   *
   * @return An address in the values file. If the address is 0, then value
   * does not exist in the values file. The returned value is guaranteed to be
   * in the range [[0, valuesMapSize]].
   */
  private fun valueAddressForPos(
    levelIdx: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): Long {
    val (segment, positionInSegment) = segmentAndPosForPos(levelIdx, bucketIdx,
      slotIdx)
    segment.position(positionInSegment)
    return segment.readLong().also { address ->
      check(
        address >= 0) { "Invalid value address in keymap at $positionInSegment: $address" }
    }
  }

  fun isOccupied(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): Boolean {
    val valueAddr = valueAddressForPos(levelNum, bucketIdx, slotIdx)
    if (valueAddr == 0L) {
      // 0 == no value entry
      return false
    }

    valuesIo.position(valueAddr - 1)

    // token must be set and key size must be > 0
    return valuesIo.readBoolean() && valuesIo.readInt() > 0
  }

  private fun moveToSlot(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): Boolean {
    val valueAddr = valueAddressForPos(levelNum, bucketIdx, slotIdx)
    if (valueAddr == 0L) {
      // 0 == no value entry
      return false
    }

    valuesIo.position(valueAddr - 1)
    return true
  }

  /**
   * Visits the key region for the given slot position.
   *
   * @return - `null` - If the slot at the given position is not occupied.
   *         - `null to <keySize>` - If [readKey] is `false`.
   *         - `key to keySize` - If [readKey] is `true`.
   */
  private fun visitKeyRegion(levelNum: Int, bucketIdx: Int, slotIdx: Int,
                             readKey: Boolean = false
  ): Pair<K?, Int>? {
    if (!moveToSlot(levelNum, bucketIdx, slotIdx)) {
      return null
    }

    val isOccupied = valuesIo.readBoolean()

    if (!isOccupied) {
      return null
    }

    val keySize = valuesIo.readInt()
    if (keySize == 0) {
      if (readKey) {
        logReadFailure("key", "key size is 0", levelNum, bucketIdx, slotIdx)
      }
      return null
    }

    if (readKey) {
      val keyAddr = valuesIo.position()
      val key = keyExternalizer.read(valuesIo)
      val readBytes = (valuesIo.position() - keyAddr).toInt()
      check(readBytes == keySize) {
        "Key size mismatch. Expected to read $keySize bytes, but $readBytes bytes were read."
      }

      return key to keySize
    }

    if (keySize > 0) {
      valuesIo.skipBytes(keySize)
    }

    return null to keySize
  }

  /**
   * Read the key at the given slot position.
   */
  fun readKey(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): K? {
    return visitKeyRegion(levelNum = levelNum, bucketIdx = bucketIdx,
      slotIdx = slotIdx, readKey = true)?.first
  }

  /**
   * Read the value at the given slot position.
   */
  fun readValue(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): V? {
    if (visitKeyRegion(levelNum = levelNum, bucketIdx = bucketIdx,
        slotIdx = slotIdx, readKey = false)?.second == null
    ) {
      // if the key is empty (specifically, keySize == 0), then this slot is empty
      return null
    }

    val valueSize = valuesIo.readInt()
    if (valueSize == 0) {
      logReadFailure("value", "value size is 0", levelNum, bucketIdx, slotIdx)
      return null
    }

    val valAddr = valuesIo.position()
    val value = valueExternalizer.read(valuesIo)
    val readBytes = (valuesIo.position() - valAddr).toInt()
    check(readBytes == valueSize) {
      "Value size mismatch. Expected to read $valueSize bytes, but $readBytes bytes were read."
    }

    return value
  }

  fun writeEntry(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
    key: K?,
    value: V?,
  ) {
    val (segment, positionInSegment) = segmentAndPosForPos(levelNum, bucketIdx,
      slotIdx)

    if (key == null) {
      // delete entry
      deleteEntry(segment, positionInSegment)
      return
    }

    // TODO(itsaky): Punch holes in the region of the existing entry on update
    //   to ensure the disk space is released

    // leave space for the 'is_occupied' token
    val tokenAddr = valuesWriteAt
    valuesIo.position(tokenAddr)
    valuesIo.seekBoolean()

    // write key and value
    writeSlotKeyOrVal(key, keyExternalizer)
    writeSlotKeyOrVal(value, valueExternalizer)

    val finalAddr = valuesIo.position()

    // then set the token
    valuesIo.position(tokenAddr)
    valuesIo.writeBoolean(true)

    // reset to the final position
    valuesIo.position(finalAddr)

    // then update the address in the keymap
    segment.position(positionInSegment)
    segment.writeLong(tokenAddr + 1)

    valuesWriteAt = finalAddr
  }

  private fun deleteEntry(
    segment: KeymapSegment,
    positionInSegment: Long,
    readValue: Boolean = false,
  ): V? {
    segment.position(positionInSegment)
    val valueAddr = segment.readLong()

    if (valueAddr == 0L) {
      return null
    }

    FileUtils.deallocate(raKeymapFile, positionInSegment, SIZE_LONG)

    valuesIo.position(valueAddr - 1)

    var entrySize = 0L

    if (!valuesIo.readBoolean()) {
      // slot is not occupied
      return null
    }

    entrySize += SIZE_BOOLEAN // isOccupied

    val keySize = valuesIo.readInt()
    entrySize += SIZE_INT
    entrySize += keySize

    if (keySize > 0) {
      valuesIo.seekRelative(keySize)
    }

    val valueSize = valuesIo.readInt()
    entrySize += SIZE_INT
    entrySize += valueSize

    var value: V? = null
    if (readValue && valueSize > 0) {
      val valAddr = valuesIo.position()
      value = valueExternalizer.read(valuesIo)

      // ensure that the externalizer read exactly the expected bytes
      check((valuesIo.position() - valAddr).toInt() == valueSize)
    } else if (valueSize > 0) {
      valuesIo.seekRelative(valueSize)
    }

    // punch holes in the file
    FileUtils.deallocate(raIndexFile, getValuesRealOffset(valueAddr - 1),
      entrySize)

    // TODO(itsaky): When deleting an entry, update the corresponding keymap entry with -1
    // TODO(itsaky): When an entry is deleted, update valueFirstEntryAt and valueLastEntryAt accordingly
    return value
  }

  fun clear() {
    // TODO: This should not clear the header data in the files
    FileUtils.deallocate(raKeymapFile, 0, KEYMAP_SEGMENT_SIZE_BYTES * KEYMAP_MAX_SEGMENTS)
    FileUtils.deallocate(raIndexFile, 0, valuesMapSize + VALUES_HEADER_SIZE_BYTES)
//    FileUtils.deallocate(raMetaFile)
  }

  fun tryMoveToInterim(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
    key: K?,
    value: V?,
  ): V? {
    TODO()
  }

  private fun <T> writeSlotKeyOrVal(obj: T?, externalizer: DataExternalizer<T>
  ) {
    if (obj != null) {
      // obj is not null
      // leave space for the obj size and write the key
      // then write the number of bytes written for obj to the sizeAddr
      val sizeAddr = valuesIo.position()
      valuesIo.seekInt()

      val valueAddr = valuesIo.position()
      externalizer.write(valuesIo, obj)

      val finalAddr = valuesIo.position()
      valuesIo.position(sizeAddr)
      valuesIo.writeInt((finalAddr - valueAddr).toInt().also {
        check(it > 0) { "Key/Value must be at least 1 byte in size" }
      })
      valuesIo.position(finalAddr)
    } else {
      // object is null, write the size of the obj as 0
      // since obj size is 0, we don't need to write the obj
      valuesIo.writeInt(0)
    }
  }

  override fun close() {
    keymapSegments.forEach { segment ->
      try {
        segment?.unmap()
      } catch (err: Throwable) {
        logger.error("Failed to unmap segment", err)
      }
    }

    try {
      raIndexFile.close()
    } catch (e: Throwable) {
      logger.error("Failed to close index file", e)
    }

    try {
      raKeymapFile.close()
    } catch (e: Throwable) {
      logger.error("Failed to close keymap file", e)
    }

    try {
      raMetaFile.close()
    } catch (e: Throwable) {
      logger.error("Failed to close meta file", e)
    }
  }

  private fun logReadFailure(
    what: String,
    reason: String,
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ) {
    log.info(
      "Cannot read '$what' at slot ($reason): level={}, bucket={}, slot={}",
      levelNum, bucketIdx, slotIdx)
  }


  companion object {

    private val log = LoggerFactory.getLogger(PersistentLevelHashIO::class.java)

    /**
     * The number of bytes it takes to store the magic number of the keymap/values
     * file.
     */
    private const val MAGIC_NUMBER_SIZE_BYTES = SIZE_LONG

    /**
     * The number of bytes in the header of the keymap file that is used to store
     * the segment count.
     */
    private const val KEYMAP_HEADER_SEGCOUNT_BYTES = SIZE_INT

    /**
     * The number of bytes in the header of the keymap file that is used to store
     * the size of the segment structures.
     */
    private const val KEYMAP_HEADER_SEGSIZE_BYTES = SIZE_LONG

    /**
     * The number of bytes used to store the header of the keymap file.
     */
    private const val KEYMAP_HEADER_SIZE_BYTES: Long =
      MAGIC_NUMBER_SIZE_BYTES + KEYMAP_HEADER_SEGCOUNT_BYTES + KEYMAP_HEADER_SEGSIZE_BYTES

    /**
     * The size of one segment in the key map file.
     */
    private const val KEYMAP_SEGMENT_SIZE_BYTES: Long = 1024 * 1024 // 1MB

    /**
     * The number of bytes that are used to store an entry in a key map file.
     */
    private const val KEYMAP_ENTRY_BYTES: Long = SIZE_LONG

    /**
     * The number of entries that can be stored in a segment in the key map file.
     */
    private const val KEYMAP_ENTRIES_PER_SEGMENT =
      KEYMAP_SEGMENT_SIZE_BYTES / KEYMAP_ENTRY_BYTES

    /**
     * The maximum number of segments that can be stored for an index.
     */
    private const val KEYMAP_MAX_SEGMENTS = 100

    /**
     * The maximum number of entries that can be stored in all of the keymap segments combined.
     */
    const val KEYMAP_MAX_ENTRIES =
      KEYMAP_MAX_SEGMENTS * KEYMAP_ENTRIES_PER_SEGMENT

    /**
     * Magic number that is used as the file signature to identify the keymap file.
     */
    private const val KEYMAP_MAGIC_NUMBER = 0x414944584B

    /**
     * Magic number that is used as the file signature to identify the values file.
     */
    private const val VALUES_MAGIC_NUMBER = 0x4149445856

    /**
     * The size of the header (bytes) in the values file.
     */
    private const val VALUES_HEADER_SIZE_BYTES: Long =
      MAGIC_NUMBER_SIZE_BYTES + SIZE_LONG + // first entry address
        SIZE_LONG   // next entry address

    /**
     * The maximum size of the region of the values file that can be mapped into
     * memory at a given time.
     */
    private const val VALUES_MAX_MAP_SIZE_BYTES = 512L * 1024L;

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

  /**
   * A segment of the key map file.
   */
  private class KeymapSegment(
    val file: RandomAccessFile,
    val path: String,
    private val segmentOffset: Long,
    private val segmentLength: Long,
    private val io: MappedRandomAccessIO = MappedRandomAccessIO(),
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
        logger.error(
          "Failed to unmap keymap segment. file={}, offset={}, size={}", path,
          segmentOffset, segmentLength, err)
        return false
      }
    }

    companion object {

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
          segmentLength = KEYMAP_SEGMENT_SIZE_BYTES.toLong())
      }
    }
  }
}