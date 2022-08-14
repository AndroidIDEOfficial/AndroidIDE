/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
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
package com.itsaky.inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.xml.INamespace;

import java.util.Comparator;

/**
 * Attribute that can be applied to a {@link IView}
 *
 * @author Akash Yadav
 */
public interface IAttribute {

  /**
   * Returned by {@link #compareNull(Object, Object)} to indicate that the comparison must be done
   * further.
   */
  int DO_COMPARE = 2;

  Comparator<IAttribute> COMPARATOR =
      (first, second) -> {
        var result = compareNull(first, second);
        if (result != IAttribute.DO_COMPARE) {
          return result;
        }

        result = compareNull(first.getNamespace(), second.getNamespace());
        if (result != IAttribute.DO_COMPARE) {
          return result;
        }

        // If we reach here, then it means that the attributes and their namespaces are not
        // null
        // First, the attributes must be compared by their namespaces

        final var firstNs = first.getNamespace();
        final var secondNs = second.getNamespace();

        if (firstNs != null && firstNs.equals(secondNs)) {
          // Compare by attribute names if the namespaces are same
          return first.getAttributeName().compareTo(second.getAttributeName());
        }

        // xmlns declarations must be on top
        if (firstNs != null && firstNs.equals(INamespace.DECLARATOR)) {
          return -1;
        }

        if (secondNs != null && secondNs.equals(INamespace.DECLARATOR)) {
          return 1;
        }

        return 0;
      };

  private static int compareNull(Object first, Object second) {
    if (first == null && second == null) {
      return 0;
    }

    if (first != null && second == null) {
      return -1;
    }

    if (first == null) {
      return 1;
    }

    return DO_COMPARE;
  }

  @Nullable
  INamespace getNamespace();

  @NonNull
  String getAttributeName();

  @NonNull
  String getValue();

  /**
   * Set the given string as a value to this attribute. This will set {@link #isApplied()} to return
   * {@code true}.
   *
   * @param value The value to set.
   */
  void apply(String value);
}
