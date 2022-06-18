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

public final class SheetOption {
  public int id;
  public int iconRes;
  public int titleRes;
  public Object extra;

  public SheetOption(int id, int iconRes, int titleRes) {
    this.id = id;
    this.iconRes = iconRes;
    this.titleRes = titleRes;
  }

  public SheetOption(int id, int iconRes, int titleRes, Object extra) {
    this.id = id;
    this.iconRes = iconRes;
    this.titleRes = titleRes;
    this.extra = extra;
  }

  public SheetOption setExtra(Object extra) {
    this.extra = extra;
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof SheetOption) {
      SheetOption that = (SheetOption) obj;
      return this.iconRes == that.iconRes && this.titleRes == that.titleRes;
    }
    return false;
  }
}
