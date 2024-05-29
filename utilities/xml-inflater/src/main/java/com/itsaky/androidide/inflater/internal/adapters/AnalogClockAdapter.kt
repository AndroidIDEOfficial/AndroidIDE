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

@file:Suppress("DEPRECATION")

package com.itsaky.androidide.inflater.internal.adapters

import android.graphics.BlendMode
import android.os.Build.VERSION_CODES
import android.widget.AnalogClock
import androidx.annotation.RequiresApi
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.itsaky.androidide.inflater.AttributeHandlerScope
import com.itsaky.androidide.inflater.models.UiWidget
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string

/**
 * Attribute adapter for [AnalogClock].
 *
 * @author Deep Kr. Ghosh
 */
@com.itsaky.androidide.annotations.inflater.ViewAdapter(AnalogClock::class)
@IncludeInDesigner(group = WIDGETS)
@RequiresApi(VERSION_CODES.S)
open class AnalogClockAdapter<T : AnalogClock> : ViewAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("dialTint") { view.dialTintList = parseColorStateList(context, value) }
    create("dialTintMode") { view.dialTintBlendMode = parseBlendMode(value) }
    create("hand_hourTint") { view.hourHandTintList = parseColorStateList(context, value) }
    create("hand_hourTintMode") { view.hourHandTintBlendMode = parseBlendMode(value) }
    create("hand_minuteTint") { view.minuteHandTintList = parseColorStateList(context, value) }
    create("hand_minuteTintMode") { view.minuteHandTintBlendMode = parseBlendMode(value) }
    create("hand_secondTint") { view.secondHandTintList = parseColorStateList(context, value) }
    create("hand_secondTintMode") { view.secondHandTintBlendMode = parseBlendMode(value) }
    create("timeZone") { view.timeZone = parseString(value) }
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(
      UiWidget(AnalogClock::class.java, string.widget_analog_clock, drawable.ic_widget_analog_clock)
    )
  }

  protected fun parseBlendMode(value: String): BlendMode {
    return when (value) {
      "add" -> BlendMode.PLUS
      "multiply" -> BlendMode.MODULATE
      "screen" -> BlendMode.SCREEN
      "src_atop" -> BlendMode.SRC_ATOP
      "src_in" -> BlendMode.SRC_IN
      "src_over" -> BlendMode.SRC_OVER
      else -> BlendMode.SRC_IN
    }
  }
}
