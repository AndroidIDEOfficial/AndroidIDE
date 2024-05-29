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

import android.app.Application
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class ShiftedArrayTest {

  @Test
  fun `test positive shift`() {
    val backing = LongArray(10) { it.toLong() }
    val array = MutableShiftedLongArray(backing)
    for (s in 0..<array.size) {
      val sh = array.shift
      val add = if (s > 0) {
        array.shift(1)
        1
      } else 0
      for (i in 0..<array.size) {
        assertThat(array[i]).isEqualTo(backing[(sh + i + add) % backing.size])
      }
    }
  }

  @Test
  fun `test negative shift`() {
    val backing = LongArray(10) { it.toLong() }
    val array = MutableShiftedLongArray(backing)
    array.shift(-1)
    assertThat(array[0]).isEqualTo(backing[backing.size - 1])
    assertThat(array[1]).isEqualTo(backing[backing.size - 2])
    assertThat(array[2]).isEqualTo(backing[backing.size - 3])
    assertThat(array[3]).isEqualTo(backing[backing.size - 4])
    array.shift(-1)
    assertThat(array[0]).isEqualTo(backing[backing.size - 2])
    array.shift(-1)
    assertThat(array[0]).isEqualTo(backing[backing.size - 3])
    array.shift(-2)
    assertThat(array[0]).isEqualTo(backing[backing.size - 5])
  }

  @Test
  fun `test negative shift - 2`() {
    val backing = LongArray(10) { it.toLong() }
    val array = MutableShiftedLongArray(backing)
    array.shift(-2)
    assertThat(array[0]).isEqualTo(backing[backing.size - 2])
    assertThat(array[1]).isEqualTo(backing[backing.size - 3])
    assertThat(array[2]).isEqualTo(backing[backing.size - 4])
    assertThat(array[3]).isEqualTo(backing[backing.size - 5])
    array.shift(-3) // -5
    assertThat(array[0]).isEqualTo(backing[backing.size - 5])
    array.shift(-4) // -9
    assertThat(array[0]).isEqualTo(backing[backing.size - 9])
    array.shift(-5) // -14
    assertThat(array[0]).isEqualTo(backing[backing.size - 4]) // -14 % 10 = -4
  }

  @Test
  fun `test cyclic shift`() {
    val backing = LongArray(10) { it.toLong() }
    val array = MutableShiftedLongArray(backing)
    array.shift(10)
    assertThat(array[0]).isEqualTo(backing[0])
    assertThat(array[1]).isEqualTo(backing[1])
    assertThat(array[2]).isEqualTo(backing[2])
    assertThat(array[3]).isEqualTo(backing[3])
    array.shift(1) // 11
    assertThat(array[0]).isEqualTo(backing[1])
    assertThat(array[1]).isEqualTo(backing[2])
    assertThat(array[2]).isEqualTo(backing[3])
    assertThat(array[3]).isEqualTo(backing[4])
    array.shift(1) // 12
    assertThat(array[0]).isEqualTo(backing[2])
    assertThat(array[1]).isEqualTo(backing[3])
    assertThat(array[2]).isEqualTo(backing[4])
    assertThat(array[3]).isEqualTo(backing[5])
    array.shift(1) // 13
    assertThat(array[0]).isEqualTo(backing[3])
    assertThat(array[1]).isEqualTo(backing[4])
    assertThat(array[2]).isEqualTo(backing[5])
    assertThat(array[3]).isEqualTo(backing[6])
    array.shift(1) // 14
    assertThat(array[0]).isEqualTo(backing[4])
    assertThat(array[1]).isEqualTo(backing[5])
    assertThat(array[2]).isEqualTo(backing[6])
    assertThat(array[3]).isEqualTo(backing[7])
    array.shift(1) // 15
    assertThat(array[0]).isEqualTo(backing[5])
    assertThat(array[1]).isEqualTo(backing[6])
    assertThat(array[2]).isEqualTo(backing[7])
    assertThat(array[3]).isEqualTo(backing[8])
    array.shift(10) // 25
    assertThat(array[0]).isEqualTo(backing[5])
    assertThat(array[1]).isEqualTo(backing[6])
    assertThat(array[2]).isEqualTo(backing[7])
    assertThat(array[3]).isEqualTo(backing[8])
    array.shift(100) // 125
    assertThat(array[0]).isEqualTo(backing[5])
    assertThat(array[1]).isEqualTo(backing[6])
    assertThat(array[2]).isEqualTo(backing[7])
    assertThat(array[3]).isEqualTo(backing[8])
  }

  @Test
  fun `test shift normalization`() {
    val backing = LongArray(10) { it.toLong() }
    val array = MutableShiftedLongArray(backing)
    array.shift(100)
    assertThat(array.normalizedShift).isEqualTo(0)
    array.shift(101) // 201
    assertThat(array.normalizedShift).isEqualTo(1)
    array.shift(101) // 302
    assertThat(array.normalizedShift).isEqualTo(2)
    array.shift(-303) // -1
    assertThat(array.normalizedShift).isEqualTo(backing.size - 1)
    array.shift(-1) // -2
    assertThat(array.normalizedShift).isEqualTo(backing.size - 2)
    array.shift(-101) // -103
    assertThat(array.normalizedShift).isEqualTo(backing.size - 3)
  }

  @Test
  fun `test shifted array iterator`() {
    val backing = LongArray(10) { it.toLong() }
    val array = MutableShiftedLongArray(backing)
    val iterator = array.iterator()

    array.shift(1)

    assertThat(iterator.hasNext()).isTrue()
    assertThat(iterator.next()).isEqualTo(backing[1])
    assertThat(iterator.hasNext()).isTrue()
    assertThat(iterator.next()).isEqualTo(backing[2])
    assertThat(iterator.hasNext()).isTrue()
    assertThat(iterator.next()).isEqualTo(backing[3])
    assertThat(array.last()).isEqualTo(backing[0])
    assertThat(array.first()).isEqualTo(backing[1])
  }
}