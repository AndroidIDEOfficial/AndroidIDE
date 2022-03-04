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

  public NewProjectDetails() {}

  public NewProjectDetails(String name, String packageName, int minSdk, int targetSdk) {
    this.name = name;
    this.packageName = packageName;
    this.minSdk = minSdk;
    this.targetSdk = targetSdk;
  }

  public NewProjectDetails setName(String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }

  public NewProjectDetails setPackageName(String packageName) {
    this.packageName = packageName;
    return this;
  }

  public String getPackageName() {
    return packageName;
  }

  public NewProjectDetails setMinSdk(int minSdk) {
    this.minSdk = minSdk;
    return this;
  }

  public int getMinSdk() {
    return minSdk;
  }

  public NewProjectDetails setTargetSdk(int targetSdk) {
    this.targetSdk = targetSdk;
    return this;
  }

  public int getTargetSdk() {
    return targetSdk;
  }
}
