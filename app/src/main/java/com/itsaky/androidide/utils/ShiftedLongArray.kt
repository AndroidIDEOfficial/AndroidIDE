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

package com.itsaky.androidide.utils

/**
 * Represents an [Long] array whose indices are shifted by a specified number of positions. This class
 * provides methods to access and modify elements in the array based on the shifted indices. The
 * array is not cyclic; attempting to access indices outside the array bounds will result in an
 * [IndexOutOfBoundsException].
 *
 * Shift values are wrapped around. Therefore, a shift value of `array.size` effectively means no
 * shift (`size % size => 0`). Also, when the array is shifted by a negative value, then the indexing
 * starts from the end of the array (`array.size - 1`).
 *
 * @property size The number of elements in the array.
 * @property shift The number of positions to shift the indices of the array. A negative value
 * represents a left shift.
 * @property normalizedShift The normalized shift value.
 * @author Akash Yadav
 */
open class ShiftedLongArray(
  protected val array: LongArray,
  shift: Int = 0
) : Collection<Long> {

  override val size: Int
    get() = array.size

  var shift: Int = shift
    protected set

  val normalizedShift: Int
    get() = ((shift % size) + size) % size

  @Suppress("NOTHING_TO_INLINE")
  protected inline fun checkIdx(idx: Int) {
    if (idx < 0 || idx >= array.size) {
      throw IndexOutOfBoundsException("Index $idx is out of bounds for array of size ${array.size}")
    }
  }

  /**
   * Get the corresponding shifted-index for the given index.
   */
  open fun getShiftedIndex(index: Int): Int {
    val size = this.size
    val idx = if (shift < 0) {
      size - index
    } else index
    return (idx + normalizedShift) % size
  }

  /**
   * Returns whether the contents of this array are equal to the specified array.
   */
  fun contentEquals(array: ShiftedLongArray): Boolean {
    return contentEquals(array.array)
  }

  /**
   * Returns whether the contents of this array are equal to the specified array.
   */
  fun contentEquals(array: LongArray): Boolean {
    return this.array.contentEquals(array)
  }

  /**
   * Returns the hash code value for the contents of this array.
   */
  fun contentHashCode(): Int {
    return array.contentHashCode()
  }

  operator fun get(index: Int): Long {
    checkIdx(index)
    return array[getShiftedIndex(index)]
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ShiftedLongArray) return false

    if (!array.contentEquals(other.array)) return false
    if (shift != other.shift) return false

    return true
  }

  override fun hashCode(): Int {
    var result = array.contentHashCode()
    result = 31 * result + shift
    return result
  }

  override fun isEmpty(): Boolean {
    return array.isEmpty()
  }

  override fun containsAll(elements: Collection<Long>): Boolean {
    return elements.all { array.contains(it) }
  }

  override fun contains(element: Long): Boolean {
    return array.contains(element)
  }

  override fun iterator(): Iterator<Long> {
    return object : Iterator<Long> {
      var index = 0

      override fun hasNext(): Boolean {
        return index < array.size
      }

      override fun next(): Long {
        if (!hasNext()) {
          throw NoSuchElementException()
        } else {
          return this@ShiftedLongArray[index++]
        }
      }
    }
  }

  override fun toString(): String {
    return "ShiftedLongArray(array=${array.contentToString()}, shift=$shift)"
  }
}