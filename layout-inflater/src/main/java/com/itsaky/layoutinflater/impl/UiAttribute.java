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
package com.itsaky.layoutinflater.impl;

import com.itsaky.layoutinflater.IAttribute;

import java.util.Objects;

public class UiAttribute implements IAttribute {
    
    private final String namespace;
    private final String name;
    private final String value;
    
    public UiAttribute(String namespace, String name, String value) {
        this.namespace = namespace;
        this.name = name;
        this.value = value;
    }
    
    @Override
    public String getNamespace() {
        return namespace;
    }
    
    @Override
    public String getAttributeName() {
        return name;
    }
    
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return
        "UiAttribute [" + "\n" +
        "    namespace = " + namespace + "\n" + 
        "    name = " + name + "\n" +
        "    value = " + value + "\n" +
        "]";
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
