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

/**
 * A fixed value which represents an enum of a flag
 *
 * @author Akash Yadav
 */
public interface IFixedValue {

  /**
   * Is this value a flag? An attribute which accepts a flag may be combined with other flags.
   *
   * @return {@code true} if this value is a flag. {@code false} otherwise.
   */
  boolean isFlag();

  /**
   * The name of this enum/flag as represented in an XML file
   *
   * @return The name of this flag
   */
  String getName();

  /**
   * Get the value of this enum/flag. This value is fixed and should not change.
   *
   * @return Value of this enum/flag.
   */
  int getValue();
}
