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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** A model that contains data of a project currently opened in AndroidIDE */
public class IDEProject {

  /** Name of this project */
  @SerializedName("name")
  public String name = "";

  /** Name of this project which should be displayed to user */
  @SerializedName("displayName")
  public String displayName = "";

  /** Description of this project. Maybe null. */
  @SerializedName("description")
  public String description = "";

  /**
   * Path of this project. This is NOT the directory path of this project.
   *
   * <p>For example, ':app or ':module:subModule'
   */
  @SerializedName("path")
  public String path = "";

  /** Path of the directory containing this project */
  @SerializedName("projectDir")
  public String projectDir;

  /** File path of the application's icon We get this manually by parsing AndroidManifest.xml */
  public String iconPath;

  /** Subprojects of this project */
  @SerializedName("modules")
  public List<IDEModule> modules = new ArrayList<>();

  /** Tasks included in this project */
  @SerializedName("tasks")
  public List<IDETask> tasks = new ArrayList<>();

  /** Path of jar file of dependencies of this project */
  @SerializedName("dependencies")
  public List<String> dependencies = new ArrayList<>();

  /**
   * Get the module by its path
   *
   * @param path Path of the module
   * @return An Optional containing the IDEModule or Optional.empty()
   */
  public Optional<IDEModule> getModuleByPath(String path) {
    if (modules != null && modules.size() > 0) {
      for (IDEModule module : modules) {
        if (module == null) continue;
        if (module.path.trim().equals(path.trim())) return Optional.of(module);
      }
    }
    return Optional.empty();
  }

  /** Stores information about a SDK. Like, • Codename • API string • API level */
  public static class SDK {

    @SerializedName("codename")
    public String codename;

    @SerializedName("apiString")
    public String apiString;

    @SerializedName("apiLevel")
    public int apiLevel;
  }
}
