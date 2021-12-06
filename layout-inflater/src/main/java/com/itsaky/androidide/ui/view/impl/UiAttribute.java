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
package com.itsaky.androidide.ui.view.impl;

import com.itsaky.androidide.ui.view.AttrType;
import com.itsaky.androidide.ui.view.IAttribute;
import com.itsaky.androidide.ui.view.IFixedValue;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

public class UiAttribute implements IAttribute {
    
    private final String namespace;
    private final String name;
    private final String value;
    private final Set<IFixedValue> fixedValues;
    
    public UiAttribute(String namespace, String name, String value, Set<IFixedValue> fixedValues) {
        this.namespace = namespace;
        this.name = name;
        this.value = value;
        this.fixedValues = fixedValues;
    }
    
    public UiAttribute(String namespace, String name, String value) {
        this (namespace, name, value, new HashSet<IFixedValue>());
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
    public Set<IFixedValue> getFixedValues() {
        return fixedValues;
    }

    @Override
    public String toString() {
        return
        "UiAttribute [" + "\n" +
        "    namespace = " + namespace + "\n" + 
        "    name = " + name + "\n" +
        "    value = " + value + "\n" +
        "    fixedValue = " + fixedValues + "\n" +
        "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name, value, fixedValues);
    }
}
