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

import org.jetbrains.annotations.Contract;

import java.util.Objects;

/**
 * @author Akash Yadav
 */
public class UiNamespace implements INamespace {

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
  private final String prefix;
  private final String packageName;
  private final String uri;

  public UiNamespace(String prefix, String uri) {
    this(prefix, null, uri);
  }

  public UiNamespace(String prefix, String packageName, String uri) {
    this.prefix = prefix;
    this.packageName = packageName;
    this.uri = uri;
  }

  private UiNamespace(@NonNull Parcel in) {
    this.prefix = in.readString();
    this.packageName = in.readString();
    this.uri = in.readString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUri());
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

  @NonNull
  @Override
  public String toString() {
    return "UiNamespace{" + "name='" + prefix + '\'' + ", uri='" + uri + '\'' + '}';
  }

  @Override
  public String getPrefix() {
    return prefix;
  }

  @Override
  public String getPackageName() {
    return packageName;
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
    dest.writeString(prefix);
    dest.writeString(packageName);
    dest.writeString(uri);
  }
}
