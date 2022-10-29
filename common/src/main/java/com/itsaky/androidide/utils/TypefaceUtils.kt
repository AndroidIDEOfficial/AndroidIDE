/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.utils

import android.graphics.Typeface
import com.itsaky.androidide.app.BaseApplication
import java.io.File

fun quicksand(): Typeface =
  Typeface.createFromAsset(BaseApplication.getBaseInstance().assets, "fonts/quicksand.ttf")

fun jetbrainsMono(): Typeface =
  Typeface.createFromAsset(BaseApplication.getBaseInstance().assets, "fonts/jetbrains-mono.ttf")

fun josefinSans(): Typeface =
  Typeface.createFromAsset(BaseApplication.getBaseInstance().assets, "fonts/josefin-sans.ttf")

fun customOrJBMono(useCustom: Boolean = true): Typeface {
  val fontFile = File(Environment.ANDROIDIDE_UI, "font.ttf")
  if (fontFile.exists() && fontFile.length() > 0 && useCustom) {
    return Typeface.createFromFile(fontFile)
  } else {
    return jetbrainsMono()
  }
}
