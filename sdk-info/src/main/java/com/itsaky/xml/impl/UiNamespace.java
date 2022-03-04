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
package com.itsaky.xml.impl;

import android.os.Parcel;
import androidx.annotation.NonNull;
import com.itsaky.xml.INamespace;
import java.util.Objects;
import org.jetbrains.annotations.Contract;

/**
 * @author Akash Yadav
 */
public class UiNamespace implements INamespace {

  private final String name;
  private final String uri;

  public static final Creator<UiNamespace> CREATOR =
      new Creator<>() {
        @NonNull
        @Contract("_ -> new")
        @Override
        public UiNamespace createFromParcel(Parcel source) {
          return new UiNamespace(source);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public UiNamespace[] newArray(int size) {
          return new UiNamespace[0];
        }
      };

  public UiNamespace(String name, String uri) {
    this.name = name;
    this.uri = uri;
  }

  private UiNamespace(@NonNull Parcel in) {
    this.name = in.readString();
    this.uri = in.readString();
  }

  @NonNull
  @Override
  public String toString() {
    return "UiNamespace{" + "name='" + name + '\'' + ", uri='" + uri + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof UiNamespace)) {
      return false;
    }

    UiNamespace that = (UiNamespace) o;

    // name doesn't matter while
    // check for URI
    return Objects.equals(getUri(), that.getUri());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUri());
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getUri() {
    return uri;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(@NonNull Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(uri);
  }
}
