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

import androidx.annotation.VisibleForTesting
import com.itsaky.androidide.levelhash.DataExternalizer
import com.itsaky.androidide.levelhash.LevelHash.ResizeFailure
import com.itsaky.androidide.levelhash.seekInt
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_INT
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_LONG
import com.itsaky.androidide.levelhash.util.FileUtils
import com.itsaky.androidide.levelhash.util.MappedRandomAccessIO
import org.slf4j.LoggerFactory
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import kotlin.math.pow

private val logger = LoggerFactory.getLogger(PersistentLevelHashIO::class.java)

// TODO: Update valuesFirstEntry and valuesNxtEntry whenever an entry is added or deleted
// TODO: Entries in the values file are always appended at the end. When an entry
//    is to be deleted, we simply use fallocate with FALLOC_FL_PUNCH_HOLE to deallocate
//    the disk space occupied by that entry. However, this may result in the region
//    at the start of the values file become empty. In that case, we might consider
//    moving the entries at the start of the file. This may (or may not) result in
//    improved performance when seeking through the file.
// TODO: The above is also applicable for the keymap file when the level hash is
//    expanded and new regions for the levels are allocated.

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
 * Structure of the keymap file:
 *
 * ```
 * keymap {
 *   u64 magic_number;
 *   level levels[2];
 *   level interim_level?;
 * }
 *
 * level {
 *    bucket buckets[2^(level_size-level_idx)];
 * }
 *
 * bucket {
 *    u64 slots[bucket_size];
 * }
 * ```
 *
 * The `keymap` struct contains fields :
 * - `magic_number` - Magic number for uniquely identifying the keymap file.
 * - `levels` - The levels of the keymap. There are 2 levels, the top level (index 0)
 *    and the bottom level (index 1).
 * - `interim_level` - The temporary level that is used to move the slots from
 *    the bottom level to the yet-to-be top level during expansion process.
 *
 * The `level` struct contains fields :
 * - `buckets` - The buckets of the level. The number of buckets depends on the
 *    index of the level and can be calculated using `2^(level_size-level_idx)`
 *    where `level_size` is the size of the level hash and `level_idx` is the
 *    index of the current level.
 *
 * The `bucket` struct contains fields :
 * - `slots` - An array of size `bucket_size` that contains the 1-based, 64-bit
 *    address of the the value entry in the values file. Note that the offset is
 *    1-based because 0 is considered as an invalid value for the address (hence,
 *    if the value is 0, then that slot is considered as an empty slot). This
 *    helps in recognizing if an entry in the keymap points to a valid address
 *    or not. We can't really fill up the keymap entries with a negative offset
 *    as that would just take up unnecessary space.
 *
 * ## Values
 *
 * Structure of the values file :
 *
 * ```
 * values {
 *    u64 magic_number;
 *    value values[];
 *
 *    value {
 *      u32 entry_size;
 *      u32 key_size;
 *      u8 key[key_size];
 *      u32 value_size;
 *      u8 value[value_size];
 *    }
 * }
 * ```
 *
 * The `values` structure constains fields :
 * - `magic_number` - A magic number that is used to identify the values file.
 * - `values` - The value entries.
 *
 * Each `value` entry contains fields :
 * - `entry_size` - The size of the entry in bytes (excluding the size of this
 *    `entry_size` field).
 * - `key_size` - The size of the key in bytes.
 * - `key` - The key of `key_size` bytes.
 * - `value_size` - The size of the value in bytes.
 * - `value` - The value of `value_size` bytes.
 *
 * ## Metadata
 *
 * Structure of the metadata file :
 *
 * ```
 * meta {
 *    u32 values_version;
 *    u32 keymap_version;
 *    u64 values_first_entry;
 *    u64 values_next_entry;
 *    u64 values_file_size_bytes;
 *    u32 km_level_size;
 *    u32 km_bucket_size;
 *    u64 km_l0_addr;
 *    u64 km_l1_addr;
 * }
 * ```
 *
 * The `meta` structure contains the fields :
 * - `values_version` - The version of the values file.
 * - `keymap_version` - The version of the keymap file.
 * - `values_first_entry` - The address of the first entry in the values file.
 * - `values_next_entry` - The address of the next entry in the values file.
 * - `values_file_size_bytes` - The (occupied) size of the values file in bytes.
 * - `km_level_size` - The level size of the level hash.
 * - `km_bucket_size` - The bucket size of the level hash.
 * - `km_l0_addr` - Address of the level 0 (top level) in the keymap.
 * - `km_l1_addr` - Address of the level 1 (bottom level) in the keymap.
 *
 * @author Akash Yadav
 */
internal class PersistentLevelHashIO<K : Any, V : Any?>(
  indexFile: File,
  levelSize: Int,
  bucketSize: Int,
  private val keyExternalizer: DataExternalizer<K>,
  private val valueExternalizer: DataExternalizer<V>,
) : AutoCloseable {

  // IMPLEMENTATION NOTE
  // Whenever you access the valuesIo property, you must ensure that the
  // position is set to the desired location in the values file.
  // as the values file is randomly accessed, and by multiple functions, or
  // even multiple threads, the current position cannot be determined

  @VisibleForTesting
  internal val metaIo =
    PersistentMetaIO(File(indexFile.parentFile, "${indexFile.name}._meta"),
      levelSize, bucketSize)

  private val keymapFile = File(indexFile.parentFile, "${indexFile.name}._i")
  private val raKeymapFile by lazy { RandomAccessFile(keymapFile, "rw") }
  private val raValuesFile by lazy { RandomAccessFile(indexFile, "rw") }

  private val keymapIo = MappedRandomAccessIO()
  private var valIo = MappedRandomAccessIO()

  private var interimLvlAddr: Long? = null

  private val topLevelBucketCount: Long
    get() = 2.0.pow(metaIo.levelSize)
      .toLong()

  private val topLevelSizeBytes: Long
    get() = topLevelBucketCount * metaIo.bucketSize * KEYMAP_ENTRY_SIZE_BYTES

  internal var levelSize: Int
    get() = metaIo.levelSize
    set(value) { metaIo.levelSize = value }

  init {
    BinaryFileUtils.initSparseFile(file = keymapFile,
      magicNumber = KEYMAP_MAGIC_NUMBER)
    BinaryFileUtils.initSparseFile(file = indexFile,
      magicNumber = VALUES_MAGIC_NUMBER)

    // the header region is not memory mapped
    valuesRemap(size = metaIo.valuesFileSize)
    keymapResize(newSize = metaIo.keymapSize)
  }

  private fun realValueOffset(offset: Long) = VALUES_HEADER_SIZE_BYTES + offset
  private fun realKeymapOffset(offset: Long) = KEYMAP_HEADER_SIZE_BYTES + offset
  private fun keymapBufOffset(offset: Long) = offset - KEYMAP_HEADER_SIZE_BYTES

  private fun valuesDeallocate(offset: Long, len: Long) =
    FileUtils.deallocate(raValuesFile, realValueOffset(offset), len)

  private fun keymapDeallocate(offset: Long, len: Long) =
    FileUtils.deallocate(raKeymapFile, realKeymapOffset(offset), len)

  /**
   * Resize the values file so that its size becomes [newSize] in bytes. This is
   * risky and should be used carefully. Resizing the file incorrectly may lead
   * to data loss. The [valIo] is reset to position 0 after this operation.
   *
   * @param newSize The new size of the values file, in bytes.
   */
  private fun valuesResize(newSize: Long) {
    metaIo.valuesFileSize = newSize
    raValuesFile.setLength(newSize)
    valuesRemap(newSize)
  }

  private fun keymapResize(newSize: Long) {
    if (raKeymapFile.length() != newSize) {
      raKeymapFile.setLength(newSize)
    }
    keymapRemap(newSize)
  }

  private fun valuesRemap(size: Long, offset: Long = VALUES_HEADER_SIZE_BYTES) =
    fileRemap(raValuesFile, valIo, size, offset)

  private fun keymapRemap(size: Long, offset: Long = KEYMAP_HEADER_SIZE_BYTES) =
    fileRemap(raKeymapFile, keymapIo, size, offset)

  private fun fileRemap(file: RandomAccessFile, io: MappedRandomAccessIO,
                        size: Long, offset: Long = 0L
  ) {
    val buffer = file.channel.map(FileChannel.MapMode.READ_WRITE, offset, size)
    io.close()
    io.reset(buffer)
  }

  private fun slotAddress(
    levelIdx: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): Long {
    val levelAddr = when (levelIdx) {
      0 -> metaIo.l0Addr
      1 -> metaIo.l1Addr
      else -> throw IllegalArgumentException("Invalid level index: $levelIdx")
    }

    return slotAddrForLvlAddr(levelAddr, bucketIdx, slotIdx)
  }

  private fun slotAddrForLvlAddr(
    lvlAddr: Long,
    bucketIdx: Int,
    slotIdx: Int,
  ): Long {
    return lvlAddr + // start position of level
      (KEYMAP_ENTRY_SIZE_BYTES * metaIo.bucketSize * bucketIdx) + // bucket position
      (KEYMAP_ENTRY_SIZE_BYTES * slotIdx) // slot position in bucket
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
    val slotAddr = slotAddress(levelIdx, bucketIdx, slotIdx)
    keymapIo.position(slotAddr)
    return keymapIo.readLong().also { address ->
      check(
        address >= 0) { "Invalid value address in keymap at $slotAddr: $address" }
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
    // 0 == no value entry
    return valueAddressForPos(levelNum, bucketIdx, slotIdx).let { valAddr ->
      valAddr > 0L && valIo.tryPosition(valAddr - 1) && valIo.readInt() > 0
    }
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

    if (valIo.readInt() <= 0L) {
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
    val slotAddr = slotAddress(levelNum, bucketIdx, slotIdx)

    if (key == null) {
      // delete entry
      deleteEntry(slotAddr)
      return
    }

    keymapIo.position(slotAddr)
    val existingValueAddr = keymapIo.readLong()
    val (isUpdate, existingEntrySize) = run {
      if (existingValueAddr > 0L) {
        valIo.position(existingValueAddr - 1)
        true to valIo.readInt().toLong()
      } else {
        false to 0L
      }
    }

    val thisValueAddr = metaIo.valuesNextEntry
    if (thisValueAddr + 4 * KB_1 > metaIo.valuesFileSize) {
      valuesResize(thisValueAddr + VALUES_SEGMENT_SIZE_BYTES)
    }

    // valuesResize will reset the position to 0
    valIo.position(thisValueAddr)

    if (valIo.readInt() > 0) {
      return
    }

    val keyValPos = valIo.position()

    // write key and value
    writeSlotKeyOrVal(key, keyExternalizer)
    writeSlotKeyOrVal(value, valueExternalizer)

    val finalAddr = valIo.position()
    val entrySize = finalAddr - keyValPos
    check(entrySize <= Int.MAX_VALUE) {
      "Entry size is too large: $entrySize"
    }

    // then set the token
    valIo.position(thisValueAddr)
    valIo.writeInt(entrySize.toInt())

    // reset to the final position
    valIo.position(finalAddr)

    // then update the address in the keymap
    keymapIo.position(slotAddr)
    keymapIo.writeLong(thisValueAddr + 1)

    metaIo.valuesNextEntry = finalAddr

    if (isUpdate) {
      var size = SIZE_INT
      if (existingEntrySize > 0L) {
        size += existingEntrySize
      }

      // deallocate the region which contains the existing entry
      valuesDeallocate(existingValueAddr - 1, size)
    }
  }

  private fun deleteEntry(
    slotAddr: Long,
    readValue: Boolean = false,
  ): V? {
    keymapIo.position(slotAddr)
    val valueAddr = keymapIo.readLong()

    // reading this region again will return 0, which is considered
    // a null pointer
    keymapDeallocate(slotAddr, SIZE_LONG)

    if (valueAddr == 0L) {
      return null
    }

    valIo.position(valueAddr - 1)
    valIo.seekInt() // seek over entrySize

    var entrySize = SIZE_INT

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
    metaIo.valuesFirstEntry = 0L
    metaIo.valuesNextEntry = 0L
    metaIo.l0Addr = 0
    metaIo.l1Addr = 2.0.pow(metaIo.levelSize).toLong() * metaIo.bucketSize * KEYMAP_ENTRY_SIZE_BYTES
    val kmSize = metaIo.l1Addr + (metaIo.l1Addr shr 1)

    keymapResize(realKeymapOffset(kmSize))
    keymapDeallocate(0, kmSize)

    valuesResize(realValueOffset(VALUES_SEGMENT_SIZE_BYTES))
    valuesDeallocate(0, VALUES_SEGMENT_SIZE_BYTES)
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

  internal fun prepareInterimLevel(bucketCount: Int) {
    check(interimLvlAddr == null) {
      "Interim level address must be null. Is the level hash being resized on multiple threads?"
    }

    val interimSize = bucketCount * metaIo.bucketSize * KEYMAP_ENTRY_SIZE_BYTES
    val start = metaIo.kmStartAddr
    if (realKeymapOffset(interimSize) < start) {
      // we can reuse the empty space at the beginning of the file
      interimLvlAddr = 0L
    } else {
      // ensure the keymap can accommodate the interim level
      val length = raKeymapFile.length()
      keymapResize(length + interimSize)
      interimLvlAddr = keymapBufOffset(length)
    }
  }

  internal fun moveToInterim(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
    interimBucketIdx: Int,
    interimSlotIdx: Int,
  ): Boolean {
    // current (source) slot
    val sSlotAddr = slotAddress(levelNum, bucketIdx, slotIdx)

    // destination slot
    val dSlotAddr =
      slotAddrForLvlAddr(interimLvlAddr!!, interimBucketIdx, interimSlotIdx)

    keymapIo.position(dSlotAddr)
    val dValAddr = keymapIo.readLong()
    if (dValAddr > 0) {
      // interim (destination) slot is occupied
      return false
    }

    // interim slot is not occupied
    // as the values in the keymap are just pointers to the actual value entries
    // in the values file, we just need to update destination slot to point
    // where the source slot points

    // 1. read the address where the source slot points
    keymapIo.position(sSlotAddr)
    val eValAddr = keymapIo.readLong()

    // 2. move the destination slot and write the address of the source slot's value
    keymapIo.position(dSlotAddr)
    keymapIo.writeLong(eValAddr)

    // 3. deallocate the space occupied by the source slot
    keymapDeallocate(sSlotAddr, KEYMAP_HEADER_SIZE_BYTES)

    return true
  }

  internal fun finalizeExpansion() {
    // current top level becomes the bottom level
    // and interim level becomes the new top level
    metaIo.l1Addr = metaIo.l0Addr
    metaIo.l0Addr = interimLvlAddr!!
    interimLvlAddr = null
  }

  override fun close() {
    closeAndLogErrs(metaIo, keymapIo, valIo, raKeymapFile, raValuesFile)
  }

  internal fun dmpSlotAddrs(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ) {
    val sb = StringBuilder()
    sb.append("level: ")
    sb.appendLine(levelNum)
    sb.append("bucket: ")
    sb.appendLine(bucketIdx)
    sb.append("slot: ")
    sb.appendLine(slotIdx)

    val slotAddr = slotAddress(levelNum, bucketIdx, slotIdx)
    sb.append("keymapAddr: ")
    appendAddress(sb, slotAddr)

    keymapIo.position(slotAddr)

    val valueAddr = keymapIo.readLong()
    sb.append("valueAddr: ")
    appendAddress(sb, valueAddr)

    println(sb.toString())
  }

  @OptIn(ExperimentalStdlibApi::class)
  private fun appendAddress(sb: StringBuilder, value: Long) {
    sb.append(value)
    sb.append(" [")
    sb.append(value.toHexString(HexFormat.UpperCase))
    sb.appendLine("]")
  }

  companion object {

    private fun closeAndLogErrs(vararg closeable: AutoCloseable) {
      closeable.forEachIndexed { index, autoCloseable ->
        try {
          autoCloseable.close()
        } catch (e: Throwable) {
          logger.error("Failed to close [{}]", index, e)
        }
      }
    }

    private val log = LoggerFactory.getLogger(PersistentLevelHashIO::class.java)

    /**
     * 1 Kilobyte.
     */
    internal const val KB_1 = 1024

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
    internal const val VALUES_HEADER_SIZE_BYTES: Long = MAGIC_NUMBER_SIZE_BYTES

    /**
     * The size of one segment region in the values file.
     */
    internal const val VALUES_SEGMENT_SIZE_BYTES = 512L * 1024L

    /**
     * The number of bytes used to store the header of the keymap file.
     */
    internal const val KEYMAP_HEADER_SIZE_BYTES: Long = MAGIC_NUMBER_SIZE_BYTES

    /**
     * The number of bytes that are used to store an entry in a key map file.
     */
    internal const val KEYMAP_ENTRY_SIZE_BYTES: Long = SIZE_LONG

    /**
     * Magic number that is used as the file signature to identify the keymap file.
     */
    internal const val KEYMAP_MAGIC_NUMBER = 0x414944584B

    internal const val LEVEL_VALUES_VERSION = 1
    internal const val LEVEL_KEYMAP_VERSION = 1
  }

  /**
   * Thrown during a move operation when the destination slot is already occupied.
   */
  class DestinationOccupiedException(
    levelIdx: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ) : ResizeFailure(
    "Destination slot is already occupied: levelIdx=$levelIdx, bucketIdx=$bucketIdx, slotIdx=$slotIdx")
}