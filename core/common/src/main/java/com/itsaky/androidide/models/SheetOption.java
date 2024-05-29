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

import android.graphics.drawable.Drawable;
import java.util.Objects;

public final class SheetOption {

  public String id;
  public Drawable icon;
  public String title;
  public Object extra;

  public SheetOption(String id, Drawable icon, String title, Object extra) {
    this.id = id;
    this.icon = icon;
    this.title = title;
    this.extra = extra;
  }

  public SheetOption setExtra(Object extra) {
    this.extra = extra;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SheetOption)) {
      return false;
    }
    SheetOption that = (SheetOption) o;
    return Objects.equals(id, that.id) && Objects.equals(icon, that.icon)
      && Objects.equals(title, that.title) && Objects.equals(extra, that.extra);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, icon, title, extra);
  }

}
