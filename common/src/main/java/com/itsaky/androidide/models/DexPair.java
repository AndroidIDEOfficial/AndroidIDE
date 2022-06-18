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

import android.os.Parcel;
import android.os.Parcelable;

public class DexPair implements Parcelable {

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel p1, int p2) {
    p1.writeString(first);
    p1.writeString(second);
  }

  public String first;
  public String second;

  private DexPair(Parcel in) {
    first = in.readString();
    second = in.readString();
  }

  public DexPair(String first, String second) {
    this.first = first;
    this.second = second;
  }

  public static final Creator<DexPair> CREATOR =
      new Creator<DexPair>() {

        @Override
        public DexPair createFromParcel(Parcel p1) {
          return new DexPair(p1);
        }

        @Override
        public DexPair[] newArray(int p1) {
          return new DexPair[p1];
        }
      };
}
