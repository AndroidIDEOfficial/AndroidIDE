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

import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class ProjectTemplate {

  private int id;
  private String name;
  private String description;
  private boolean isSupportJava;
  private boolean isSupportKotlin;
  private boolean isCpp;

  @DrawableRes private int imageId;

  public ProjectTemplate() {}

  public ProjectTemplate(int id, String name, String description, int imageId) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.imageId = imageId;
  }

  public int getId() {
    return id;
  }

  public ProjectTemplate setId(int id) {
    this.id = id;
    return this;
  }

  public ProjectTemplate setName(Context ctx, @StringRes int name) {
    return setName(ctx.getString(name));
  }

  public String getName() {
    return name;
  }

  public ProjectTemplate setName(String name) {
    this.name = name;
    return this;
  }

  public ProjectTemplate setDescription(Context ctx, @StringRes int desc) {
    return setDescription(ctx.getString(desc));
  }

  public String getDescription() {
    return description;
  }

  public ProjectTemplate setDescription(String description) {
    this.description = description;
    return this;
  }

  public int getImageId() {
    return imageId;
  }

  public ProjectTemplate setImageId(@DrawableRes int imageId) {
    this.imageId = imageId;
    return this;
  }

  public boolean isSupportJava() {
    return this.isSupportJava;
  }

  public ProjectTemplate setSupportJava(boolean isSupportJava) {
    this.isSupportJava = isSupportJava;
    return this;
  }

  public boolean isSupportKotlin() {
    return this.isSupportKotlin;
  }

  public ProjectTemplate setSupportKotlin(boolean isSupportKotlin) {
    this.isSupportKotlin = isSupportKotlin;
    return this;
  }

  public boolean isCpp() {
    return this.isCpp;
  }

  public ProjectTemplate setIsCpp(boolean isCpp) {
    this.isCpp = isCpp;
    return this;
  }
}
