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

import com.itsaky.androidide.levelhash.DataExternalizer
import com.itsaky.androidide.levelhash.internal.PersistentKeymapIO.Companion.KEYMAP_HEADER_SIZE_BYTES
import com.itsaky.androidide.levelhash.internal.PersistentKeymapIO.Companion.KEYMAP_MAX_SEGMENTS
import com.itsaky.androidide.levelhash.internal.PersistentKeymapIO.Companion.KEYMAP_SEGMENT_SIZE_BYTES
import com.itsaky.androidide.levelhash.seekBoolean
import com.itsaky.androidide.levelhash.seekInt
import com.itsaky.androidide.levelhash.seekShort
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_BOOLEAN
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_INT
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_LONG
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_SHORT
import com.itsaky.androidide.levelhash.util.FileUtils
import com.itsaky.androidide.levelhash.util.MappedRandomAccessIO
import org.slf4j.LoggerFactory
import java.io.File
import java.io.RandomAccessFile
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
 * - The keymap file header occupies [PersistentKeymapIO.KEYMAP_HEADER_SIZE_BYTES]
 *    bytes.
 * - Each segment is occupies [PersistentKeymapIO.KEYMAP_SEGMENT_SIZE_BYTES]
 *    bytes.
 * - There can be at most [PersistentKeymapIO.KEYMAP_MAX_SEGMENTS] segments in
 *    the keymap file.
 * - Each segment can store at most [PersistentKeymapIO.KEYMAP_ENTRIES_PER_SEGMENT]
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
 *   u64 magic_number;
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
 *      u16 entry_size;
 *      u32 key_size;
 *      u8 key[key_size];
 *      u32 value_size;
 *      u8 value[value_size];
 *    }
 * }
 * ```
 * @author Akash Yadav
 */
internal class PersistentLevelHashIO<K : Any, V : Any?>(
  indexFile: File,
  private val keyExternalizer: DataExternalizer<K>,
  private val valueExternalizer: DataExternalizer<V>,
) : AutoCloseable {

  // IMPLEMENTATION NOTE
  // Whenever you access the valuesIo property, you must ensure that the
  // position is set to the desired location in the values file.
  // as the values file is randomly accessed, and by multiple functions, or
  // even multiple threads, the current position cannot be determined

  private val keymapIo = PersistentKeymapIO(indexFile)

  private val metaFile = File(indexFile.parentFile, "${indexFile.name}._meta")
  private val raMetaFile by lazy { RandomAccessFile(metaFile, "rw") }
  private val raIndexFile by lazy { RandomAccessFile(indexFile, "rw") }

  private var valFirstEntryPos = 0L
  private var valNxtEntryPos = 0L
  private var valIdxSize = 0L
  private var valIo = MappedRandomAccessIO()

  init {
    BinaryFileUtils.initSparseFile(
      file = indexFile,
      magicNumber = VALUES_MAGIC_NUMBER,
      maxLength = VALUES_HEADER_SIZE_BYTES + valIdxSize
    ) { raf ->
      var position = MAGIC_NUMBER_SIZE_BYTES
      raf.seek(position)
      raf.writeLong(0) // first entry
      position += SIZE_LONG

      raf.seek(position)
      raf.writeLong(0) // next entry
    }

    // TODO: Load the below values from file when the file already exists

    valNxtEntryPos = 0
    valFirstEntryPos = VALUES_HEADER_SIZE_BYTES
    valIdxSize = VALUES_MAX_MAP_SIZE_BYTES

    // the header region is not memory mapped
    log.debug("mmap values file: offset={}, size={}", VALUES_HEADER_SIZE_BYTES,
      valIdxSize)
    val buffer = raIndexFile.channel.map(FileChannel.MapMode.READ_WRITE,
      VALUES_HEADER_SIZE_BYTES, valIdxSize)
    valIo.reset(buffer, 0L, valIdxSize)
  }

  private fun getValuesRealOffset(offset: Long) =
    VALUES_HEADER_SIZE_BYTES + offset

  private fun valuesDeallocate(offset: Long, len: Long) {
    FileUtils.deallocate(raIndexFile, getValuesRealOffset(offset), len)
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
    val (segment, positionInSegment) = keymapIo.segmentAndPosForPos(levelIdx,
      bucketIdx, slotIdx)
    segment.position(positionInSegment)
    return segment.readLong().also { address ->
      check(
        address >= 0) { "Invalid value address in keymap at $positionInSegment: $address" }
    }
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

    valIo.position(valueAddr - 1)
    return true
  }

  fun isOccupied(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): Boolean {
    if (!moveToSlot(levelNum, bucketIdx, slotIdx)) {
      return false
    }

    // token must be set and key size must be > 0
    return valIo.readBoolean() && valIo.readInt() > 0
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

    val isOccupied = valIo.readBoolean()

    if (!isOccupied) {
      return null
    }

    if (valIo.readUnsignedShort() <= 0L) {
      // entry size is 0, so the slot is empty
      return null
    }

    val keySize = valIo.readInt()
    if (keySize == 0) {
      if (readKey) {
        log.info(
          "Cannot read 'key' at slot (key size is 0): level={}, bucket={}, slot={}",
          levelNum, bucketIdx, slotIdx)
      }
      return null
    }

    if (readKey) {
      val keyAddr = valIo.position()
      val key = keyExternalizer.read(valIo)
      val readBytes = (valIo.position() - keyAddr).toInt()
      check(readBytes == keySize) {
        "Key size mismatch. Expected to read $keySize bytes, but $readBytes bytes were read."
      }

      return key to keySize
    }

    if (keySize > 0) {
      valIo.skipBytes(keySize)
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

    val valueSize = valIo.readInt()
    if (valueSize == 0) {
      return null
    }

    val valAddr = valIo.position()
    val value = valueExternalizer.read(valIo)
    val readBytes = (valIo.position() - valAddr).toInt()
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
    val (segment, positionInSegment) = keymapIo.segmentAndPosForPos(levelNum,
      bucketIdx, slotIdx)

    if (key == null) {
      // delete entry
      deleteEntry(segment, positionInSegment)
      return
    }

    segment.position(positionInSegment)
    val existingValueAddr = segment.readLong()
    val (isUpdate, existingEntrySize) = run {
      if (existingValueAddr > 0L) {
        valIo.position(existingValueAddr - 1)
        valIo.readBoolean() to valIo.readUnsignedShort().toLong()
      } else {
        false to 0L
      }
    }

    val tokenAddr = valNxtEntryPos
    valIo.position(tokenAddr)

    // leave space for isOccupied token and entry size
    valIo.seekBoolean()
    valIo.seekShort()

    val keyValPos = valIo.position()

    // write key and value
    writeSlotKeyOrVal(key, keyExternalizer)
    writeSlotKeyOrVal(value, valueExternalizer)

    val finalAddr = valIo.position()
    val entrySize = finalAddr - keyValPos
    check(entrySize <= Short.MAX_VALUE) {
      "Entry size is too large: $entrySize"
    }

    // then set the token
    valIo.position(tokenAddr)
    valIo.writeBoolean(true)
    valIo.writeShort((entrySize and 0xFFFF).toInt())

    // reset to the final position
    valIo.position(finalAddr)

    // then update the address in the keymap
    segment.position(positionInSegment)
    segment.writeLong(tokenAddr + 1)

    valNxtEntryPos = finalAddr

    if (isUpdate) {
      var size: Long = SIZE_BOOLEAN + SIZE_SHORT
      if (existingEntrySize > 0L) {
        size += existingEntrySize
      }

      // deallocate the region which contains the existing entry
      valuesDeallocate(existingValueAddr - 1, size)
    }
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

    // reading this region again will return 0, which is considered
    // a null pointer
    keymapIo.deallocate(segment, positionInSegment, SIZE_LONG)

    valIo.position(valueAddr - 1)

    var entrySize = 0L

    if (!valIo.readBoolean()) {
      // slot is not occupied
      return null
    }

    entrySize += SIZE_BOOLEAN // isOccupied
    entrySize += SIZE_SHORT // entrySize

    val keySize = valIo.readInt()
    entrySize += SIZE_INT
    entrySize += keySize

    if (keySize > 0) {
      valIo.seekRelative(keySize)
    }

    val valueSize = valIo.readInt()
    entrySize += SIZE_INT
    entrySize += valueSize

    var value: V? = null
    if (readValue && valueSize > 0) {
      val valAddr = valIo.position()
      value = valueExternalizer.read(valIo)

      // ensure that the externalizer read exactly the expected bytes
      check((valIo.position() - valAddr).toInt() == valueSize)
    } else if (valueSize > 0) {
      valIo.seekRelative(valueSize)
    }

    // punch holes in the file
    valuesDeallocate(valueAddr - 1, entrySize)

    // TODO(itsaky): When an entry is deleted, update valueFirstEntryAt and valueLastEntryAt accordingly
    return value
  }

  fun clear() {
    keymapIo.deallocate(KEYMAP_HEADER_SIZE_BYTES,
      KEYMAP_SEGMENT_SIZE_BYTES * KEYMAP_MAX_SEGMENTS)
    valuesDeallocate(VALUES_HEADER_SIZE_BYTES, valIdxSize)
  }

  @Suppress("UNUSED", "UNUSED_PARAMETER")
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
      val sizeAddr = valIo.position()
      valIo.seekInt()

      val valueAddr = valIo.position()
      externalizer.write(valIo, obj)

      val finalAddr = valIo.position()
      valIo.position(sizeAddr)
      valIo.writeInt((finalAddr - valueAddr).toInt().also {
        check(it > 0) { "Key/Value must be at least 1 byte in size" }
      })
      valIo.position(finalAddr)
    } else {
      // object is null, write the size of the obj as 0
      // since obj size is 0, we don't need to write the obj
      valIo.writeInt(0)
    }
  }

  override fun close() {

    try {
      keymapIo.close()
    } catch (e: Throwable) {
      log.error("Failed to close keymap file", e)
    }

    try {
      raIndexFile.close()
    } catch (e: Throwable) {
      logger.error("Failed to close index file", e)
    }

    try {
      raMetaFile.close()
    } catch (e: Throwable) {
      logger.error("Failed to close meta file", e)
    }
  }


  companion object {

    private val log = LoggerFactory.getLogger(PersistentLevelHashIO::class.java)

    /**
     * The number of bytes it takes to store the magic number of the keymap/values
     * file.
     */
    internal const val MAGIC_NUMBER_SIZE_BYTES = SIZE_LONG

    /**
     * Magic number that is used as the file signature to identify the values file.
     */
    internal const val VALUES_MAGIC_NUMBER = 0x4149445856

    /**
     * The size of the header (bytes) in the values file.
     */
    internal const val VALUES_HEADER_SIZE_BYTES: Long =
      MAGIC_NUMBER_SIZE_BYTES + SIZE_LONG + // first entry address
        SIZE_LONG   // next entry address

    /**
     * The maximum size of the region of the values file that can be mapped into
     * memory at a given time.
     */
    internal const val VALUES_MAX_MAP_SIZE_BYTES = 512L * 1024L;
  }

}