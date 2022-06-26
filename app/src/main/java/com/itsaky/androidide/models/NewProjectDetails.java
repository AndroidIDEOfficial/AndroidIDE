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
package com.itsaky.androidide.models;

public class NewProjectDetails {

  public String name;
  public String packageName;
  public int minSdk;
  public int targetSdk;
  public String language;
  public String savePath;
  public String cppFlags;

  public NewProjectDetails() {}

  public NewProjectDetails(
      String name,
      String packageName,
      int minSdk,
      int targetSdk,
      String language,
      String cppFlags,
      String savePath) {
    this.name = name;
    this.packageName = packageName;
    this.minSdk = minSdk;
    this.targetSdk = targetSdk;
    this.language = language;
    this.cppFlags = cppFlags;
    this.savePath = savePath;
  }

  public String getName() {
    return name;
  }

  public NewProjectDetails setName(String name) {
    this.name = name;
    return this;
  }

  public String getPackageName() {
    return packageName;
  }

  public NewProjectDetails setPackageName(String packageName) {
    this.packageName = packageName;
    return this;
  }

  public int getMinSdk() {
    return minSdk;
  }

  public NewProjectDetails setMinSdk(int minSdk) {
    this.minSdk = minSdk;
    return this;
  }

  public int getTargetSdk() {
    return targetSdk;
  }

  public NewProjectDetails setTargetSdk(int targetSdk) {
    this.targetSdk = targetSdk;
    return this;
  }

  public String getCppFlags() {
    return cppFlags;
  }

  public NewProjectDetails setCppFlags(String cppFlags) {
    this.cppFlags = cppFlags;
    return this;
  }
}
