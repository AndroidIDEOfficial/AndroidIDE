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
package com.itsaky.inflater.impl;

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttribute;
import com.itsaky.xml.INamespace;

import java.util.Objects;

public class UiAttribute implements IAttribute {

  private final INamespace namespace;
  private final String name;
  protected String value;

  public UiAttribute(INamespace namespace, String name, String value) {
    this.namespace = namespace;
    this.name = name;
    this.value = value;
  }

  @NonNull
  @Override
  public INamespace getNamespace() {
    return namespace;
  }

  @NonNull
  @Override
  public String getAttributeName() {
    return name;
  }

  @NonNull
  @Override
  public String getValue() {
    return this.value;
  }

  @Override
  public void apply(String value) {
    this.value = value;
  }

  @NonNull
  @Override
  public String toString() {
    return "UiAttribute ["
        + "\n"
        + "    namespace = "
        + namespace
        + "\n"
        + "    name = "
        + name
        + "\n"
        + "    value = "
        + value
        + "\n"
        + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UiAttribute that = (UiAttribute) o;
    return Objects.equals(getNamespace(), that.getNamespace())
        && Objects.equals(name, that.name)
        && Objects.equals(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getNamespace(), name, getValue());
  }
}
