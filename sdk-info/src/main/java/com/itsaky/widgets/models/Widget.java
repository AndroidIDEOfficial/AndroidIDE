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
package com.itsaky.widgets.models;

import androidx.annotation.NonNull;

public class Widget implements Comparable<Widget> {

  public String name;
  public String simpleName;
  public boolean isViewGroup;

  public String[] superclasses = new String[0];

  public Widget(@NonNull String name, boolean isViewGroup) {
    this.name = name;
    this.simpleName = name.substring(name.lastIndexOf(".") + 1);
    this.isViewGroup = isViewGroup;
  }

  @Override
  public int compareTo(Widget that) {
    if (that != null) {
      return this.simpleName.compareTo(that.simpleName);
    }

    return -1;
  }
}
