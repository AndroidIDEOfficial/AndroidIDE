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

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import androidx.annotation.StringRes

fun SpannableStringBuilder.appendClickableSpan(@StringRes textRes: Int, span: ClickableSpan,
  context: Context) {
  val str = context.getString(textRes)
  val split = str.split("@@", limit = 3)
  if (split.size != 3) {
    // Not a valid format
    append(str)
    append('\n')
    return
  }
  append(split[0])
  append(split[1], span, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
  append(split[2])
  append('\n')
}
