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

package com.itsaky.androidide.levelhash

import com.itsaky.androidide.levelhash.util.DataExternalizers
import java.io.DataInput
import java.io.DataOutput

/**
 * A [DataExternalizer] serializes and deserializes data in a level hash.
 *
 * @author Akash Yadav
 */
interface DataExternalizer<T> {

  /**
   * Serialize the given [data] into the [out][DataOutput].
   *
   * @param out The output stream to serialize to.
   * @param data The data to serialize.
   */
  fun write(out: DataOutput, data: T)

  /**
   * Deserialize the data from the [input][DataInput].
   *
   * @param input The input stream to deserialize from.
   * @return Data deserialized from the input stream.
   */
  fun read(input: DataInput): T
}

/**
 * Allows random access to the underlying data.
 */
interface RandomAccessIO {

  /**
   * Set the position to read from.
   */
  fun position(position: Long)

  /**
   * Seek the position by given count.
   */
  fun seekRelative(count: Int) {
    seekRelative(count.toLong())
  }

  /**
   * Seek the position by given count.
   */
  fun seekRelative(count: Long) {
    position(position() + count)
  }

  /**
   * Get the current positon where data is read from.
   */
  fun position(): Long
}

/**
 * Seek the position by the size of a boolean.
 */
fun RandomAccessIO.seekBoolean() {
  seekRelative(DataExternalizers.SIZE_BOOLEAN)
}

/**
 * Seek the position by the size of a byte.
 */
fun RandomAccessIO.seekByte() {
  seekRelative(DataExternalizers.SIZE_BYTE)
}

/**
 * Seek the position by the size of a short.
 */
fun RandomAccessIO.seekShort() {
  seekRelative(DataExternalizers.SIZE_SHORT)
}

/**
 * Seek the position by the size of a char.
 */
fun RandomAccessIO.seekChar() {
  seekRelative(DataExternalizers.SIZE_CHAR)
}

/**
 * Seek the position by the size of an int.
 */
fun RandomAccessIO.seekInt() {
  seekRelative(DataExternalizers.SIZE_INT)
}

/**
 * Seek the position by the size of a long.
 */
fun RandomAccessIO.seekLong() {
  seekRelative(DataExternalizers.SIZE_LONG)
}

/**
 * Seek the position by the size of a float.
 */
fun RandomAccessIO.seekFloat() {
  seekRelative(DataExternalizers.SIZE_FLOAT)
}

/**
 * Seek the position by the size of a double.
 */
fun RandomAccessIO.seekDouble() {
  seekRelative(DataExternalizers.SIZE_DOUBLE)
}

