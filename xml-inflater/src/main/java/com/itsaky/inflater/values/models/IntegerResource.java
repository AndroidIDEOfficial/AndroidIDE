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

import androidx.annotation.NonNull;

/**
 * Represents an integer value.
 *
 * @author Akash Yadav
 */
public class IntegerResource extends AbstractResource {

  public IntegerResource(@NonNull String name, @NonNull String value) {
    super(name, value);
  }

  /**
   * Get the value as integer. Or -1 if the value is not an integer.
   *
   * @return The integer value.
   */
  public int asInteger() {
    try {
      return Integer.parseInt(getValue());
    } catch (Throwable th) {
      return -1;
    }
  }
}
