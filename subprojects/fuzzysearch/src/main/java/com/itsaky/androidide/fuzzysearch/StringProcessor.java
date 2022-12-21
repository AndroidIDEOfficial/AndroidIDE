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

package com.itsaky.androidide.fuzzysearch;

/**
 * Transforms the input string
 *
 * @deprecated Use {@code ToStringFunction<String>} instead.
 */
@Deprecated
public abstract class StringProcessor implements ToStringFunction<String> {
  // now abstract class because JDK1.7 does not allow default methods in interfaces
  @Override
  public String apply(String item) {
    return process(item);
  }

  /**
   * Transforms the input string
   *
   * @deprecated Use {@code ToStringFunction#apply(String)} instead.
   * @param in Input string
   * @return The processed string
   */
  @Deprecated
  public abstract String process(String in);
}
