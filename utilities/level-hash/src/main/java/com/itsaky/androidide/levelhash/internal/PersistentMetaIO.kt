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
import androidx.collection.MutableLongLongMap
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.KEYMAP_ENTRY_SIZE_BYTES
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.LEVEL_KEYMAP_VERSION
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.LEVEL_VALUES_VERSION
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.VALUES_SEGMENT_SIZE_BYTES
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_INT
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_LONG
import org.slf4j.LoggerFactory
import java.io.EOFException
import java.io.File
import java.io.RandomAccessFile
import kotlin.math.min
import kotlin.math.pow

/**
 * @author Akash Yadav
 */
internal class PersistentMetaIO(private val metaFile: File,
                                levelSize: Int,
                                bucketSize: Int,
) : AutoCloseable {

  private val raFile by lazy { RandomAccessFile(metaFile, "rw") }
  private val cache = MutableLongLongMap(META__FIELD_COUNT)

  var valuesVersion: Int
    get() = readInt(VAL__VERSION__OFFSET)
    set(value) = writeInt(VAL__VERSION__OFFSET, value)

  var keymapVersion: Int
    get() = readInt(KM__VERSION__OFFSET)
    set(value) = writeInt(KM__VERSION__OFFSET, value)

  var valuesFirstEntry: Long
    get() = readLong(VAL__FIRST_ENTRY__OFFSET)
    set(value) = writeLong(VAL__FIRST_ENTRY__OFFSET, value)

  var valuesNextEntry: Long
    get() = readLong(VAL__NEXT_ENTRY__OFFSET)
    set(value) = writeLong(VAL__NEXT_ENTRY__OFFSET, value)

  var valuesFileSize: Long
    get() = readLong(VAL__VAL_SIZE__OFFSET)
    set(value) = writeLong(VAL__VAL_SIZE__OFFSET, value)

  var levelSize: Int
    get() = readInt(KM__LEVEL_SIZE__OFFSET)
    set(value) = writeInt(KM__LEVEL_SIZE__OFFSET, value)

  var bucketSize: Int
    get() = readInt(KM__BUCKET_SIZE__OFFSET)
    set(value) = writeInt(KM__BUCKET_SIZE__OFFSET, value)

  var l0Addr: Long
    get() = readLong(KM__L0_ADDR__OFFSET)
    set(value) { writeLong(KM__L0_ADDR__OFFSET, value) }

  var l1Addr: Long
    get() = readLong(KM__L1_ADDR__OFFSET)
    set(value) { writeLong(KM__L1_ADDR__OFFSET, value) }

  val kmStartAddr: Long
    get() = min(l0Addr, l1Addr)

  val keymapSize: Long
    get() {
      // for this to work, l0 and l1 must be in a contiguosly allocated space
      // in the keymap file
      val l0SizeBytes = 2.0.pow(levelSize).toLong() * bucketSize * KEYMAP_ENTRY_SIZE_BYTES
      var size = kmStartAddr
      size += l0SizeBytes
      size += l0SizeBytes shr 1
      return size
    }

  init {
    BinaryFileUtils.initSparseFile(metaFile)
    raFile.setLength(META__SIZE_BYTES)

    // initialize default state
    if (valuesVersion == 0) {
      valuesVersion = LEVEL_VALUES_VERSION
    }
    if (keymapVersion == 0) {
      keymapVersion = LEVEL_KEYMAP_VERSION
    }
    if (valuesFileSize == 0L) {
      valuesFileSize = VALUES_SEGMENT_SIZE_BYTES
    }
    if (this.levelSize != levelSize) {
      this.levelSize = levelSize
    }
    if (this.bucketSize != bucketSize) {
      this.bucketSize = bucketSize
    }
    // default value of l0Addr is 0
    // only the value of l1Addr should be updated
    if (this.l1Addr == 0L) {
      this.l1Addr = 2.0.pow(levelSize).toLong() * bucketSize * KEYMAP_ENTRY_SIZE_BYTES
    }
  }

  private fun readInt(position: Long): Int = fromCacheOrRead(position) {
   readOrDefault(0) { raFile.readInt() }
  }

  private fun writeInt(position: Long, value: Int) =
    writeAndCache(position, value.toLong()) {
      raFile.writeInt(value)
    }

  private fun readLong(position: Long): Long = fromCacheOrRead(position) {
    readOrDefault(0L) { raFile.readLong() }
  }

  private fun writeLong(position: Long, value: Long) =
    writeAndCache(position, value) {
      raFile.writeLong(value)
    }

  private inline fun <reified T : Number> fromCacheOrRead(position: Long,
                                                          read: () -> T
  ): T {
    val cached = cache.getOrDefault(position, 0L)
    if (cached != 0L) {
      return cast(cached)
    }

    raFile.seek(position)
    return read().also { value -> cache(position, value.toLong()) }
  }

  private inline fun writeAndCache(position: Long, value: Long,
                                   writer: () -> Unit
  ) {
    raFile.seek(position)
    writer()
    cache(position, value)
  }

  private fun cache(position: Long, value: Long) {
    cache[position] = value
  }

  override fun close() {
    try {
      clearCache()
      raFile.close()
    } catch (e: Exception) {
      log.error("Failed to close meta file", e)
    }
  }

  @VisibleForTesting
  internal fun clearCache() {
    this.cache.clear()
  }

  private inline fun <reified T : Number> readOrDefault(default: T,
                                                        read: () -> T
  ): T {
    return try {
      read()
    } catch (e: EOFException) {
      default
    }
  }

  private inline fun <reified T : Number> cast(v: Long): T {
    if (T::class == Long::class) {
      return v as T
    }

    return when {
      T::class == Byte::class -> v.toByte() as T
      T::class == Short::class -> v.toShort() as T
      T::class == Int::class -> v.toInt() as T
      T::class == Float::class -> v.toFloat() as T
      T::class == Double::class -> v.toDouble() as T
      else -> throw UnsupportedOperationException("Unsupported data type")
    }
  }

  companion object {

    private val log = LoggerFactory.getLogger(PersistentMetaIO::class.java)

    private const val VAL__VERSION__OFFSET = 0L
    private const val VAL__VERSION__SIZE_BYTES = SIZE_INT

    private const val KM__VERSION__OFFSET =
      VAL__VERSION__OFFSET + VAL__VERSION__SIZE_BYTES
    private const val KM__VERSION__SIZE_BYTES = SIZE_INT

    private const val VAL__FIRST_ENTRY__OFFSET =
      KM__VERSION__OFFSET + KM__VERSION__SIZE_BYTES
    private const val VAL__FIRST_ENTRY__SIZE_BYTES = SIZE_LONG

    private const val VAL__NEXT_ENTRY__OFFSET =
      VAL__FIRST_ENTRY__OFFSET + VAL__FIRST_ENTRY__SIZE_BYTES
    private const val VAL__NEXT_ENTRY__SIZE_BYTES = SIZE_LONG

    private const val VAL__VAL_SIZE__OFFSET =
      VAL__NEXT_ENTRY__OFFSET + VAL__NEXT_ENTRY__SIZE_BYTES
    private const val VAL__VAL_SIZE__SIZE_BYTES = SIZE_LONG

    private const val KM__LEVEL_SIZE__OFFSET =
      VAL__VAL_SIZE__OFFSET + VAL__VAL_SIZE__SIZE_BYTES
    private const val KM__LEVEL_SIZE__SIZE_BYTES = SIZE_INT

    private const val KM__BUCKET_SIZE__OFFSET =
      KM__LEVEL_SIZE__OFFSET + KM__LEVEL_SIZE__SIZE_BYTES
    private const val KM__BUCKET_SIZE__SIZE_BYTES = SIZE_INT

    private const val KM__L0_ADDR__OFFSET =
      KM__BUCKET_SIZE__OFFSET + KM__BUCKET_SIZE__SIZE_BYTES
    private const val KM__L0_ADDR__SIZE_BYTES = SIZE_LONG

    private const val KM__L1_ADDR__OFFSET =
      KM__L0_ADDR__OFFSET + KM__L0_ADDR__SIZE_BYTES
    private const val KM__L1_ADDR__SIZE_BYTES = SIZE_LONG

    // TODO: The properties below must be updated when a new field is added to the meta file

    /**
     * The number of fields in the meta file.
     */
    private const val META__FIELD_COUNT = 9

    /**
     * The size of the meta file in bytes.
     */
    // Offset of the last field + its size
    private const val META__SIZE_BYTES =
      KM__L1_ADDR__OFFSET + KM__L1_ADDR__SIZE_BYTES
  }
}