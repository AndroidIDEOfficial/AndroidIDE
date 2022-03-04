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
package com.itsaky.androidide.project;

import com.google.gson.annotations.SerializedName;

/** Represents a Gradle task */
public class IDETask {

  /** Name of this task */
  @SerializedName("name")
  public String name;

  /** Description of this task */
  @SerializedName("description")
  public String description;

  /** Name of the group that this task belongs to */
  @SerializedName("group")
  public String group;

  /**
   * Path of this task
   *
   * <p>For example, ':app:assembleDebug', ':app:build'
   */
  @SerializedName("path")
  public String path;
}
