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
import com.itsaky.androidide.levelhash.seekInt
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_INT
import com.itsaky.androidide.levelhash.util.DataExternalizers.SIZE_LONG
import com.itsaky.androidide.levelhash.util.MappedRandomAccessIO
import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.RecyclableObjectPool
import com.itsaky.androidide.utils.RecyclableObjectPool.Recyclable
import com.itsaky.androidide.utils.newRecyclableObjectPool
import com.itsaky.androidide.utils.uncheckedCast

/**
 * @author Akash Yadav
 */
@Suppress("PropertyName")
internal class PersistentValueEntry<K : Any, V : Any?> private constructor(
  internal var _addr: Long? = null,
  internal var _io: MappedRandomAccessIO? = null,
  internal var _keyExternalizer: DataExternalizer<K>? = null,
  internal var _valueExternalizer: DataExternalizer<V>? = null,
) : DefaultRecyclable() {

  override fun recycle() {
    this._addr = null
    this._io = null
    this._keyExternalizer = null
    this._valueExternalizer = null
  }

  companion object {

    internal val OBJECT_POOL = newRecyclableObjectPool(capacity = 16, factory = { PersistentValueEntry<Any, Any>() })

    internal fun <K: Any, V: Any?, T> PersistentValueEntry<K, V>.useAndRecycle(action: PersistentValueEntry<K, V>.() -> T) : T {
      return try {
        action()
      } finally {
        recycle(this)
      }
    }

    internal fun recycle(entry: PersistentValueEntry<*, *>) = OBJECT_POOL.recycle(
      uncheckedCast(entry))

    internal const val OFF_ENTRY_SIZE = 0L
    internal const val OFF_PREV_ENTRY = OFF_ENTRY_SIZE + SIZE_INT
    internal const val OFF_NEXT_ENTRY = OFF_PREV_ENTRY + SIZE_LONG
    internal const val OFF_KEY_SIZE = OFF_NEXT_ENTRY + SIZE_LONG
  }

  internal val addr: Long
    get() = checkNotNull(_addr)
  private val io: MappedRandomAccessIO
    get() = checkNotNull(_io)
  private val keyExternalizer: DataExternalizer<K>
    get() = checkNotNull(_keyExternalizer)
  private val valueExternalizer: DataExternalizer<V>
    get() = checkNotNull(_valueExternalizer)

  val entrySize: Int
    get() {
      goto(OFF_ENTRY_SIZE)
      return io.readInt()
    }

  val prevEntry: Long
    get() {
      goto(OFF_PREV_ENTRY)
      val res = io.readLong()
      return res
    }

  val nextEntry: Long
    get() {
      goto(OFF_NEXT_ENTRY)
      return io.readLong()
    }

  val keySize: Int
    get() {
      goto(OFF_KEY_SIZE)
      return io.readInt()
    }

  val key: K?
    get() {
      return seekOverKey(true)?.first
    }

  val valueSize: Int
    get() {
      seekOverKey(false)
      return io.readInt()
    }

  val value: V?
    get() {
      if (seekOverKey(false)?.second == null) {
        // key size is 0, so the slot is empty
        return null
      }

      val size = io.readInt()
      if (size == 0) {
        // 0 == null value
        return null
      }

      return readAndCheckSize(size) { valueExternalizer.read(io) }
    }

  private fun goto(offset: Long) = io.position(this.addr + offset)

  private fun seekOverKey(read: Boolean = false): Pair<K?, Int>? {
    val size = this.keySize
    if (size == 0) {
      return null
    }

    if (!read) {
      io.seekRelative(size)
      return null to size
    }

    val key = readAndCheckSize(size) { keyExternalizer.read(io) }
    return key to size
  }

  private inline fun <T> readAndCheckSize(size: Int, action: () -> T) : T {
    val pos = io.position()
    val result = action()
    val readBytes = (io.position() - pos).toInt()
    check(readBytes == size) {
      "Size mismatch. Expected to read $size bytes, but $readBytes bytes were read."
    }
    return result
  }
}