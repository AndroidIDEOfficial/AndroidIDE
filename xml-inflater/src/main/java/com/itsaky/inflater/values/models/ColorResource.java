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
package com.itsaky.inflater.values.models;

import android.graphics.Color;

import androidx.annotation.NonNull;

/**
 * Represents a color resource value.
 *
 * @author Akash Yadav
 */
public class ColorResource extends AbstractResource {

  public ColorResource(@NonNull String name, @NonNull String value) {
    super(name, value);
  }

  /**
   * Parse the value of this color resource and get the int color.
   *
   * @return The int color value or -1 if this is not a valid color.
   */
  public int parseColor() {
    try {
      return Color.parseColor(getValue());
    } catch (Throwable th) {
      return -1;
    }
  }
}
