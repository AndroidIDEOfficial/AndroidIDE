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
import com.itsaky.androidide.inflater.internal.utils.parseDimension
import com.itsaky.androidide.inflater.internal.utils.parseDrawable
import com.itsaky.androidide.inflater.internal.utils.parseInteger
import com.itsaky.androidide.inflater.internal.utils.startParse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ValueParsersTest {

  @Test
  fun `hardcoded dimension parser test`() {
    inflaterTest { module ->
      requiresActivity { activity ->
        startParse(module)
        val units = arrayOf("dp", "sp", "pt", "px", "in", "mm")
        val results = intArrayOf(1, 1, 2, 1, 160, 6)
        for (i in units.indices) {
          assertThat(parseDimension(activity, "1" + units[i], -1)).isEqualTo(results[i])
        }
        endParse()
      }
    }
  }

  @Test
  fun `reference dimension parser test`() {
    inflaterTest { module ->
      requiresActivity { activity ->
        startParse(module)
        assertThat(parseDimension(activity, "@android:dimen/app_icon_size")).isEqualTo(12289)
        assertThat(parseDimension(activity, "@android:dimen/status_bar_height_portrait"))
          .isEqualTo(6145)

        // The app module here targets android 31 and the dimension resource below was added in
        // android 31. So, the resource should not be resolved and
        // ViewGroup.LayoutParams.WRAP_CONTENT must be returned instead.
        assertThat(parseDimension(activity, "@android:dimen/status_bar_height_default"))
          .isEqualTo(-2)
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
      requiresActivity { activity ->
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
      requiresActivity { activity ->
        startParse(module)
        assertThat(parseInteger("0", def = 1)).isEqualTo(0)
        assertThat(parseInteger("10")).isEqualTo(10)
        assertThat(parseInteger("110")).isEqualTo(110)
        assertThat(parseInteger("@android:integer/button_pressed_animation_duration")).isEqualTo(100)
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
}
