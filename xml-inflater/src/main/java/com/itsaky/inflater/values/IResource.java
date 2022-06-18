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

package com.itsaky.inflater.values;

import androidx.annotation.NonNull;

/**
 * Base class for value entries in "res/values" directory.
 *
 * @author Akash Yadav
 */
public interface IResource {

  /**
   * Get the unique identifier (name) of this resource. Defined by "name" attribute in resource
   * value entries in xml files.
   *
   * @return The name of this value.
   */
  @NonNull
  String getName();

  /**
   * Get the value of this resource entry.
   *
   * @return The value of this resource.
   */
  @NonNull
  String getValue();
}
