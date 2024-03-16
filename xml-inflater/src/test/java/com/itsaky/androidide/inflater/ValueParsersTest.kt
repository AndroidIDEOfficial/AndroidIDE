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
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.inflater.internal.utils.parseBoolean
import com.itsaky.androidide.inflater.internal.utils.parseColor
import com.itsaky.androidide.inflater.internal.utils.parseDimension
import com.itsaky.androidide.inflater.internal.utils.parseDrawable
import com.itsaky.androidide.inflater.internal.utils.parseInteger
import com.itsaky.androidide.inflater.internal.utils.parseIntegerArray
import com.itsaky.androidide.inflater.internal.utils.parseString
import com.itsaky.androidide.inflater.internal.utils.parseStringArray
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.math.roundToInt

@RunWith(RobolectricTestRunner::class)
class ValueParsersTest {

  @Before
  fun `setup project`() {
    XmlInflaterTest.initIfNeeded()
  }

  @Test
  fun `hardcoded dimension parser test`() {
    inflaterTest {
      requiresActivity {

        // Hardcoded dimensions
        val units = arrayOf("dp", "sp", "pt", "px", "in", "mm")
        val results = intArrayOf(1, 1, 2, 1, 160, 6)
        for (i in units.indices) {
          assertThat(parseDimension(this, "1" + units[i], -1f).roundToInt()).isEqualTo(results[i])
        }

        // Dimensions from platform resources
        assertThat(parseDimension(this, "@android:dimen/app_icon_size")).isEqualTo(48)
        assertThat(parseDimension(this, "@android:dimen/status_bar_height_portrait")).isEqualTo(24)

        val exists =
          try {
            // The app module here targets android 31 and the dimension resource below was added in
            // android 31. So, this should throw an exception
            assertThat(parseDimension(this, "@android:dimen/status_bar_height_default"))
              .isEqualTo(-2)
            true
          } catch (err: IllegalArgumentException) {
            false
          }

        assertThat(exists).isFalse()

        assertThat(parseDimension(this, "@dimen/test_dimen_0dp", def = 1f)).isEqualTo(0)
        assertThat(parseDimension(this, "@dimen/test_dimen_1dp", def = 0f)).isEqualTo(1)
        assertThat(parseDimension(this, "@dimen/test_dimen_1dp_ref", def = 0f)).isEqualTo(1)
        assertThat(parseDimension(this, "@dimen/test_dimen_0dp_ref", def = 0f)).isEqualTo(0)

        assertThat(parseDimension(this, "@dimen/test_dimen_0pt", def = 1f)).isEqualTo(0)
        assertThat(parseDimension(this, "@dimen/test_dimen_1pt", def = 0f)).isEqualTo(1)
        assertThat(parseDimension(this, "@dimen/test_dimen_1pt_ref", def = 0f)).isEqualTo(1)
        assertThat(parseDimension(this, "@dimen/test_dimen_0pt_ref", def = 0f)).isEqualTo(0)
      }
    }
  }

  @Test
  fun `drawable parse test`() {
    inflaterTest {
      requiresActivity {
        parseDrawable(this, "#ff0000").apply {
          assertThat(this).isNotNull()
          assertThat(this).isInstanceOf(ColorDrawable::class.java)
          assertThat((this as ColorDrawable).color).isEqualTo(Color.RED)
        }
        parseDrawable(this, "@android:drawable/ab_share_pack_material").apply {
          assertThat(this).isNotNull()
          assertThat(this).isInstanceOf(BitmapDrawable::class.java)
        }
        parseDrawable(this, "@android:drawable/action_bar_background").apply {
          assertThat(this).isNotNull()
          assertThat(this).isInstanceOf(GradientDrawable::class.java)
        }
      }
    }
  }

  @Test
  fun `boolean parser test`() {
    inflaterTest {
      requiresActivity {
        assertThat(parseBoolean("true")).isTrue()
        assertThat(parseBoolean("false", def = true)).isFalse()
        assertThat(parseBoolean("@android:bool/resolver_landscape_phone")).isTrue()
        assertThat(parseBoolean("@android:bool/use_lock_pattern_drawable")).isFalse()
        assertThat(parseBoolean("@bool/test_bool_true")).isTrue()
        assertThat(parseBoolean("@bool/test_bool_false", def = true)).isFalse()
        assertThat(parseBoolean("@bool/test_bool_true_ref")).isTrue()
        assertThat(parseBoolean("@bool/test_bool_false_ref", def = true)).isFalse()
      }
    }
  }

  @Test
  fun `integer parser test`() {
    inflaterTest {
      requiresActivity {
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
      }
    }
  }

  @Test
  fun `string parser test`() {
    inflaterTest {
      assertThat(parseString("Hello World!")).isEqualTo("Hello World!")
      assertThat(parseString("@android:string/ok")).isEqualTo("OK")
      assertThat(parseString("@android:string/cancel")).isEqualTo("Cancel")
      assertThat(parseString("@string/test_string")).isEqualTo("I love Android!")
      assertThat(parseString("@string/test_string_ref")).isEqualTo("I love Android!")
      assertThat(parseString("@string/test_string_styled")).isEqualTo("I love Android!")
      assertThat(parseString("@string/test_string_styled_ref")).isEqualTo("I love Android!")
    }
  }

  @Test
  fun `string array parser test`() {
    inflaterTest {
      assertThat(parseStringArray("@android:array/phoneTypes"))
        .isEqualTo(
          arrayOf("Home", "Mobile", "Work", "Work Fax", "Home Fax", "Pager", "Other", "Custom")
        )
      assertThat(parseStringArray("@array/test_string_array"))
        .isEqualTo(arrayOf("I", "love", "Android"))
    }
  }

  @Test
  fun `integer array parser test`() {
    inflaterTest {
      assertThat(
          parseIntegerArray("@android:array/config_defaultImperceptibleKillingExemptionProcStates")
        )
        .isEqualTo(intArrayOf(0, 1, 2, 4, 12))
      assertThat(parseIntegerArray("@array/test_integer_array")).isEqualTo(intArrayOf(2, 4, 8))
    }
  }

  @Test
  fun `color parser test`() {
    inflaterTest {
      requiresActivity {
        assertThat(parseColor(this, "@android:color/red")).isEqualTo(Color.RED)
        assertThat(parseColor(this, "@android:color/black")).isEqualTo(Color.BLACK)
        assertThat(parseColor(this, "@android:color/white")).isEqualTo(Color.WHITE)
        assertThat(parseColor(this, "@android:color/transparent")).isEqualTo(Color.TRANSPARENT)
        assertThat(parseColor(this, "@color/test_color")).isEqualTo(Color.parseColor("#f44336"))
        assertThat(parseColor(this, "@color/test_color_ref")).isEqualTo(Color.parseColor("#f44336"))

        // TODO Implement color state list parser
        assertThat(parseColor(this, "@color/test_selector")).isEqualTo(Color.TRANSPARENT)
      }
    }
  }
}
