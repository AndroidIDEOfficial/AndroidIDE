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
 * Mutable version of [ShiftedLongArray].
 *
 * @author Akash Yadav
 */
class MutableShiftedLongArray(
  array: LongArray,
  shift: Int = 0
) : ShiftedLongArray(array, shift) {

  /**
   * @param capacity The capacity of the array.
   * @param shift The shift amount.
   * @param init A function to initialize the values of the array.
   */
  constructor(capacity: Int, shift: Int = 0, init: (Int) -> Long = { 0 }) : this(
    LongArray(capacity, init),
    shift)

  operator fun set(index: Int, value: Long) {
    checkIdx(index)
    array[getShiftedIndex(index)] = value
  }

  /**
   * Sets the given value at the specified absolute (un-shifted) index.
   */
  fun setAbsolute(index: Int, value: Long) {
    array[index] = value
  }

  /**
   * Shifts the array by the specified amount. The shift amount is added to the current shift.
   *
   * @param shift The shift amount.
   */
  fun shift(shift: Int) {
    this.shift = (this.shift + shift) % size
  }
}