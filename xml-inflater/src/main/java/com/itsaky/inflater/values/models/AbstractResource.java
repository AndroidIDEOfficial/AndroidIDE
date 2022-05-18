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
package com.itsaky.inflater.values.models;

import androidx.annotation.NonNull;

import com.itsaky.inflater.values.IResource;

import java.util.Objects;

/**
 * Abstract class for resource value entries.
 *
 * @author Akash Yadav
 */
abstract class AbstractResource implements IResource {

  private final String name;
  private final String value;

  public AbstractResource(@NonNull String name, @NonNull String value) {
    this.name = name;
    this.value = value;
  }

  @NonNull
  @Override
  public String getName() {
    return name;
  }

  @NonNull
  @Override
  public String getValue() {
    return value;
  }

  @NonNull
  @Override
  public String toString() {
    return getClass().getSimpleName()
        + "{"
        + "name='"
        + name
        + '\''
        + ", value='"
        + value
        + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AbstractResource)) {
      return false;
    }

    AbstractResource that = (AbstractResource) o;
    return Objects.equals(getName(), that.getName()) && Objects.equals(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getValue());
  }
}
