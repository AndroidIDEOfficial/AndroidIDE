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
import com.itsaky.androidide.levelhash.internal.PersistentValueEntry.Companion.useAndRecycle
import com.itsaky.androidide.levelhash.seekInt
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_INT
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_LONG
import com.itsaky.androidide.levelhash.util.FileUtils
import com.itsaky.androidide.levelhash.util.MappedRandomAccessIO
import com.itsaky.androidide.utils.uncheckedCast
import org.slf4j.LoggerFactory
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import kotlin.math.pow

private val logger = LoggerFactory.getLogger(PersistentLevelHashIO::class.java)

// TODO: Now that the entries in the values file are stored in a bi-directional
//  linked list, we can determine if there is enough space available in the
//  previously deallocated regions. We can use this information to store
//  new entries in these regions, thus reusing the space and avoid resizing.
// TODO: When the level hash is expanded, region for the new level in keymap file
//  is allocated at the end of the file and the region at the beginning is
//  deallocated. When there is enough space at the beginning of the file for the
//  new level, we can reuse the space at the beginning of the file. Or, maybe we
//  can move the levels towards the beginning of the file when idle.
// TODO: Allow concurrent access to the level hash.

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
 *      u64 prev_entry;
 *      u64 next_entry;
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
 * - `prev_entry` - The address of the previous entry in the values file.
 * - `next_entry` - The address of the next entry in the values file.
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
 *    u64 values_head_entry;
 *    u64 values_tail_entry;
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
 * - `values_head_entry` - The address of the first entry in the values file.
 * - `values_tail_entry` - The address of the next entry in the values file.
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

  @VisibleForTesting
  internal val metaIo =
    PersistentMetaIO(File(indexFile.parentFile, "${indexFile.name}._meta"),
      levelSize, bucketSize)

  private val keymapFile = File(indexFile.parentFile, "${indexFile.name}._i")
  private val raKeymapFile by lazy { RandomAccessFile(keymapFile, "rw") }
  private val keymapIo = MappedRandomAccessIO()
  private val raValuesFile by lazy { RandomAccessFile(indexFile, "rw") }
  private var valIo = MappedRandomAccessIO()

  private var interimLvlAddr: Long? = null

  internal var levelSize: Int
    get() = metaIo.levelSize
    set(value) {
      metaIo.levelSize = value
    }

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

  private fun valuesEntryAt(addr: Long): PersistentValueEntry<K, V> {
    return uncheckedCast<PersistentValueEntry<K, V>>(
      PersistentValueEntry.OBJECT_POOL.obtain()).also {
      it._addr = addr
      it._io = valIo
      it._keyExternalizer = keyExternalizer
      it._valueExternalizer = valueExternalizer
    }
  }

  private fun valuesEntryForSlot(
    levelIdx: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): PersistentValueEntry<K, V>? {
    val addr = valueAddressForPos(levelIdx, bucketIdx, slotIdx)
    if (addr == POSITION_INVALID) {
      return null
    }

    return valuesEntryAt(addr - 1)
  }

  fun isOccupied(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): Boolean {
    return valuesEntryForSlot(levelNum, bucketIdx, slotIdx)?.useAndRecycle {
      entrySize > 0
    } ?: false
  }

  /**
   * Read the key at the given slot position.
   */
  fun readKey(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): K? {
    return valuesEntryForSlot(levelNum, bucketIdx, slotIdx)?.useAndRecycle {
      key
    }
  }

  /**
   * Read the value at the given slot position.
   */
  fun readValue(
    levelNum: Int,
    bucketIdx: Int,
    slotIdx: Int,
  ): V? {
    return valuesEntryForSlot(levelNum, bucketIdx, slotIdx)?.useAndRecycle {
      value
    }
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
      deleteEntryAtSlot(slotAddr)
      return
    }

    keymapIo.position(slotAddr)
    val existingValueAddr = keymapIo.readLong()
    val isUpdate = existingValueAddr > POSITION_INVALID

    val tailAddr = metaIo.valuesTailAddr
    val tail = if (tailAddr > POSITION_INVALID) {
      valuesEntryAt(tailAddr - 1)
    } else {
      // this may be the first entry in the values file
      null
    }

    val thisValueAddr = tail?.nextEntry ?: 1 // nextEntry is 1-based
    if (thisValueAddr + 4 * KB_1 > metaIo.valuesFileSize) {
      valuesResize(thisValueAddr - 1 + VALUES_SEGMENT_SIZE_BYTES)
    }

    // valuesResize call above will reset the position to 0
    // so we need to call position(...) here
    valIo.position(thisValueAddr - 1)

    if (valIo.readInt() > 0) {
      // entrySize > 0
      // entry is occupied
      tail?.also { PersistentValueEntry.recycle(it) }
      return
    }

    val entryStart = valIo.position()

    // seek over prevEntry and nextEntry
    valIo.seekRelative(SIZE_ADDR + SIZE_ADDR)

    // write key and value
    writeSlotKeyOrVal(key, keyExternalizer)
    writeSlotKeyOrVal(value, valueExternalizer)

    val entryEnd = valIo.position()
    val entrySize = entryEnd - entryStart
    check(entrySize <= Int.MAX_VALUE) {
      "Entry size is too large: $entrySize"
    }

    if (tail != null) {
      // make current_tail.next -> this_entry
      valIo.position(tail.addr)
      valIo.seekRelative(PersistentValueEntry.OFF_NEXT_ENTRY)
      valIo.writeLong(thisValueAddr)
    }

    // then this_entry.prev -> current_tail
    valIo.position(thisValueAddr - 1)
    valIo.writeInt(entrySize.toInt())
    valIo.writeLong(tailAddr)
    valIo.writeLong(entryEnd + 1)

    // finally, current_tail = this_entry
    metaIo.valuesTailAddr = thisValueAddr

    if (metaIo.valuesHeadAddr == POSITION_INVALID) {
      // this is the first entry to be added in the value file
      metaIo.valuesHeadAddr = metaIo.valuesTailAddr
    }

    // update the address in the keymap
    keymapIo.position(slotAddr)
    keymapIo.writeLong(thisValueAddr)

    if (isUpdate) {
      deleteEntryAt(valueAddr = existingValueAddr, readValue = false)
    }

    tail?.let { PersistentValueEntry.recycle(it) }
  }

  /**
   * Delete the entry at the values file address pointed to by given slot address.
   *
   * @param slotAddr The 0-based address of the slot which points to the value entry in the values file.
   * @param readValue Whether the value of the entry being deleted must be read
   * and returned.
   * @return The value of the deleted entry, if the entry exists and [readValue] is `true`.
   */
  private fun deleteEntryAtSlot(
    slotAddr: Long,
    readValue: Boolean = false,
  ): V? {
    keymapIo.position(slotAddr)
    val valueAddr = keymapIo.readLong()

    // reading this region again will return 0, which is considered
    // a null pointer
    keymapDeallocate(slotAddr, SIZE_ADDR)
    return deleteEntryAt(valueAddr, readValue)
  }

  /**
   * Delete the entry at the given address in the values file.
   *
   * @param valueAddr The 1-based address of the value entry.
   * @param readValue Whether the value of the entry being deleted must be read
   * and returned.
   * @return The value of the deleted entry, if the entry exists and [readValue] is `true`.
   */
  private fun deleteEntryAt(valueAddr: Long, readValue: Boolean
  ): V? {
    if (valueAddr == POSITION_INVALID) {
      return null
    }

    val current = valuesEntryAt(valueAddr - 1)
    if (current.prevEntry == 4486112473) {
      print("break")
    }
    val prev = current.prevEntry // 1-based
    val next = current.nextEntry // 1-based

    // if this entry has a previous entry, then,
    // update the 'next' of previous entry to point to the 'next' of this entry
    if (prev > POSITION_INVALID) {
      valIo.position(prev - 1 + PersistentValueEntry.OFF_NEXT_ENTRY)
      valIo.writeLong(next) // 1-based
    }

    // if this entry has a next entry, then,
    // update the 'prev' of next entry to point to the 'prev' of this entry
    if (next > POSITION_INVALID) {
      valIo.position(next - 1 + PersistentValueEntry.OFF_PREV_ENTRY)
      valIo.writeLong(prev) // 1-based
    }

    if (metaIo.valuesHeadAddr == valueAddr) {
      metaIo.valuesHeadAddr =
        if (next > POSITION_INVALID) next else POSITION_INVALID
    }
    if (metaIo.valuesTailAddr == valueAddr) {
      metaIo.valuesTailAddr =
        if (prev > POSITION_INVALID) prev else POSITION_INVALID
    }

    valIo.position(current.addr)

    var entrySize = SIZE_INT + SIZE_ADDR + SIZE_ADDR

    // seek entrySize
    valIo.seekRelative(entrySize)

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

    // punch holes in the file in order to deallocate space occupied by current entry
    valuesDeallocate(current.addr, entrySize)

    PersistentValueEntry.recycle(current)

    return value
  }

  fun clear() {
    metaIo.valuesHeadAddr = 0
    metaIo.valuesTailAddr = 0
    metaIo.l0Addr = 0
    metaIo.l1Addr = 2.0.pow(metaIo.levelSize)
      .toLong() * metaIo.bucketSize * KEYMAP_ENTRY_SIZE_BYTES
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

    internal const val POSITION_INVALID = 0L

    /**
     * The size of the address values in bytes.
     */
    internal const val SIZE_ADDR = SIZE_LONG

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