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

package com.itsaky.attrinfo.models;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A <code>&lt;declare-styleable&gt;</code>.
 *
 * @author Akash Yadav
 */
public class Styleable {

  public final String name;
  public final Set<Attr> attributes = new HashSet<>();

  public Styleable(String name) {
    if (name == null || name.trim().length() <= 0) {
      throw new IllegalArgumentException("Invalid name specified for declared styleable");
    }

    this.name = name.trim();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Styleable)) {
      return false;
    }
    Styleable styleable = (Styleable) o;
    return Objects.equals(name, styleable.name) && Objects.equals(attributes, styleable.attributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, attributes);
  }

  @NonNull
  @Override
  public String toString() {
    return "Styleable{" + "name='" + name + '\'' + ", attributes=" + attributes + '}';
  }
}
