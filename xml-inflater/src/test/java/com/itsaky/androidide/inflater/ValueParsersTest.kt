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

package com.itsaky.androidide.inflater

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.inflater.internal.utils.endParse
import com.itsaky.androidide.inflater.internal.utils.parseBoolean
import com.itsaky.androidide.inflater.internal.utils.parseColor
import com.itsaky.androidide.inflater.internal.utils.parseDimension
import com.itsaky.androidide.inflater.internal.utils.parseDrawable
import com.itsaky.androidide.inflater.internal.utils.parseInteger
import com.itsaky.androidide.inflater.internal.utils.parseIntegerArray
import com.itsaky.androidide.inflater.internal.utils.parseString
import com.itsaky.androidide.inflater.internal.utils.parseStringArray
import com.itsaky.androidide.inflater.internal.utils.startParse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.math.roundToInt

@RunWith(RobolectricTestRunner::class)
class ValueParsersTest {

  @Test
  fun `hardcoded dimension parser test`() {
    inflaterTest { module ->
      requiresActivity { activity ->
        startParse(module)

        // Hardcoded dimensions
        val units = arrayOf("dp", "sp", "pt", "px", "in", "mm")
        val results = intArrayOf(1, 1, 2, 1, 160, 6)
        for (i in units.indices) {
          assertThat(parseDimension(activity, "1" + units[i], -1f).roundToInt())
            .isEqualTo(results[i])
        }

        // Dimensions from platform resources
        assertThat(parseDimension(activity, "@android:dimen/app_icon_size")).isEqualTo(48)
        assertThat(parseDimension(activity, "@android:dimen/status_bar_height_portrait"))
          .isEqualTo(24)

        val exists =
          try {
            // The app module here targets android 31 and the dimension resource below was added in
            // android 31. So, this should throw an exception
            assertThat(parseDimension(activity, "@android:dimen/status_bar_height_default"))
              .isEqualTo(-2)
            true
          } catch (err: IllegalArgumentException) {
            false
          }

        assertThat(exists).isFalse()

        assertThat(parseDimension(activity, "@dimen/test_dimen_0dp", def = 1f)).isEqualTo(0)
        assertThat(parseDimension(activity, "@dimen/test_dimen_1dp", def = 0f)).isEqualTo(1)
        assertThat(parseDimension(activity, "@dimen/test_dimen_1dp_ref", def = 0f)).isEqualTo(1)
        assertThat(parseDimension(activity, "@dimen/test_dimen_0dp_ref", def = 0f)).isEqualTo(0)

        assertThat(parseDimension(activity, "@dimen/test_dimen_0pt", def = 1f)).isEqualTo(0)
        assertThat(parseDimension(activity, "@dimen/test_dimen_1pt", def = 0f)).isEqualTo(1)
        assertThat(parseDimension(activity, "@dimen/test_dimen_1pt_ref", def = 0f)).isEqualTo(1)
        assertThat(parseDimension(activity, "@dimen/test_dimen_0pt_ref", def = 0f)).isEqualTo(0)
        endParse()
      }
    }
  }

  @Test
  fun `simple color drawable parse test`() {
    inflaterTest { module ->
      requiresActivity { activity ->
        startParse(module)
        parseDrawable(activity, "#ff0000").apply {
          assertThat(this).isNotNull()
          assertThat(this).isInstanceOf(ColorDrawable::class.java)
          assertThat((this as ColorDrawable).color).isEqualTo(Color.RED)
        }
        endParse()
      }
    }
  }

  @Test
  fun `boolean parser test`() {
    inflaterTest { module ->
      requiresActivity {
        startParse(module)
        assertThat(parseBoolean("true")).isTrue()
        assertThat(parseBoolean("false", def = true)).isFalse()
        assertThat(parseBoolean("@android:bool/resolver_landscape_phone")).isTrue()
        assertThat(parseBoolean("@android:bool/use_lock_pattern_drawable")).isFalse()
        assertThat(parseBoolean("@bool/test_bool_true")).isTrue()
        assertThat(parseBoolean("@bool/test_bool_false", def = true)).isFalse()
        assertThat(parseBoolean("@bool/test_bool_true_ref")).isTrue()
        assertThat(parseBoolean("@bool/test_bool_false_ref", def = true)).isFalse()
        endParse()
      }
    }
  }

  @Test
  fun `integer parser test`() {
    inflaterTest { module ->
      requiresActivity {
        startParse(module)
        assertThat(parseInteger("0", def = 1)).isEqualTo(0)
        assertThat(parseInteger("10")).isEqualTo(10)
        assertThat(parseInteger("110")).isEqualTo(110)
        assertThat(parseInteger("@android:integer/button_pressed_animation_duration"))
          .isEqualTo(100)
        assertThat(parseInteger("@android:integer/dock_enter_exit_duration")).isEqualTo(250)
        assertThat(parseInteger("@android:integer/kg_carousel_angle")).isEqualTo(75)
        assertThat(parseInteger("@android:integer/date_picker_mode")).isEqualTo(1)
        assertThat(parseInteger("@integer/test_integer_1")).isEqualTo(1)
        assertThat(parseInteger("@integer/test_integer_1_ref")).isEqualTo(1)
        assertThat(parseInteger("@integer/test_integer_0", def = 1)).isEqualTo(0)
        assertThat(parseInteger("@integer/test_integer_0_ref", def = 1)).isEqualTo(0)
        endParse()
      }
    }
  }

  @Test
  fun `string parser test`() {
    inflaterTest {
      startParse(it)
      assertThat(parseString("Hello World!")).isEqualTo("Hello World!")
      assertThat(parseString("@android:string/ok")).isEqualTo("OK")
      assertThat(parseString("@android:string/cancel")).isEqualTo("Cancel")
      assertThat(parseString("@string/test_string")).isEqualTo("I love Android!")
      assertThat(parseString("@string/test_string_ref")).isEqualTo("I love Android!")
      assertThat(parseString("@string/test_string_styled")).isEqualTo("I love Android!")
      assertThat(parseString("@string/test_string_styled_ref")).isEqualTo("I love Android!")
      endParse()
    }
  }

  @Test
  fun `string array parser test`() {
    inflaterTest {
      startParse(it)
      assertThat(parseStringArray("@android:array/phoneTypes"))
        .isEqualTo(
          arrayOf("Home", "Mobile", "Work", "Work Fax", "Home Fax", "Pager", "Other", "Custom")
        )
      assertThat(parseStringArray("@array/test_string_array"))
        .isEqualTo(arrayOf("I", "love", "Android"))
      endParse()
    }
  }

  @Test
  fun `integer array parser test`() {
    inflaterTest {
      startParse(it)
      assertThat(
          parseIntegerArray("@android:array/config_defaultImperceptibleKillingExemptionProcStates")
        )
        .isEqualTo(intArrayOf(0, 1, 2, 4, 12))
      assertThat(parseIntegerArray("@array/test_integer_array")).isEqualTo(intArrayOf(2, 4, 8))
      endParse()
    }
  }

  @Test
  fun `color parser test`() {
    inflaterTest {
      requiresActivity { activity ->
        startParse(it)
        assertThat(parseColor(activity, "@android:color/red")).isEqualTo(Color.RED)
        assertThat(parseColor(activity, "@android:color/black")).isEqualTo(Color.BLACK)
        assertThat(parseColor(activity, "@android:color/white")).isEqualTo(Color.WHITE)
        assertThat(parseColor(activity, "@android:color/transparent")).isEqualTo(Color.TRANSPARENT)
        assertThat(parseColor(activity, "@color/test_color")).isEqualTo(Color.parseColor("#f44336"))
        assertThat(parseColor(activity, "@color/test_color_ref")).isEqualTo(Color.parseColor("#f44336"))
        
        // TODO Implement color state list parser
        assertThat(parseColor(activity, "@color/test_selector")).isEqualTo(Color.TRANSPARENT)
        endParse()
      }
    }
  }
}
