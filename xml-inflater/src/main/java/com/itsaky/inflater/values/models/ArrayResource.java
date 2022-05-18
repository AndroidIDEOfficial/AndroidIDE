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
import android.text.TextUtils;

import androidx.annotation.NonNull;

/**
 * An array resource. Can be any of the following :
 *
 * <ul>
 *   <li>&lt;integer-array&gt;
 *   <li>&lt;string-array&gt;
 *   <li>&lt;array&gt;
 * </ul>
 *
 * @author Akash Yadav
 */
public class ArrayResource extends AbstractResource {

  private final String[] values;

  public ArrayResource(@NonNull String name, @NonNull String[] values) {
    super(name, TextUtils.join(", ", values));
    this.values = values;
  }

  public String[] getValues() {
    return values;
  }

  public int[] getIntValues() {
    final var ints = new int[values.length];
    for (int i = 0; i < ints.length; i++) {
      var value = values[i];
      if (value == null || value.trim().length() == 0) {
        value = "0";
      }

      ints[i] = Integer.parseInt(value);
    }

    return ints;
  }

  public int[] getColorValues() {
    final var colors = new int[this.values.length];
    for (int i = 0; i < colors.length; i++) {
      var val = values[i];
      if (val == null || val.trim().length() == 0) {
        val = "#ffffff";
      }

      colors[i] = Color.parseColor(val);
    }

    return colors;
  }
}
