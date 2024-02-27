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
 * Transforms an item of type T to a String.
 *
 * @param <T> The type of the item to transform.
 */
public interface ToStringFunction<T> {
  /**
   * A default ToStringFunction that returns the input string; used by methods that use plain
   * strings in {@link FuzzySearch}.
   */
  ToStringFunction<String> NO_PROCESS =
      new ToStringFunction<String>() {
        @Override
        public String apply(String item) {
          return item;
        }
      };

  /**
   * Transforms the input item to a string.
   *
   * @param item The item to transform.
   * @return A string to use for comparing the item.
   */
  String apply(T item);
}
