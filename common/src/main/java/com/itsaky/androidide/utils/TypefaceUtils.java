/************************************************************************************
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
 **************************************************************************************/
package com.itsaky.androidide.utils;

import android.graphics.Typeface;

import com.itsaky.androidide.app.BaseApplication;

public class TypefaceUtils {

  public static Typeface quicksand() {
    return Typeface.createFromAsset(
        BaseApplication.getBaseInstance().getAssets(), "fonts/quicksand.ttf");
  }

  public static Typeface jetbrainsMono() {
    return Typeface.createFromAsset(
        BaseApplication.getBaseInstance().getAssets(), "fonts/jetbrains-mono.ttf");
  }

  public static Typeface josefinSans() {
    return Typeface.createFromAsset(
        BaseApplication.getBaseInstance().getAssets(), "fonts/josefin-sans.ttf");
  }
}
