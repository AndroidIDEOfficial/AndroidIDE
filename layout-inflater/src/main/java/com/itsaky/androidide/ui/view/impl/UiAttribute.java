package com.itsaky.androidide.ui.view.impl;

import com.itsaky.androidide.ui.view.AttrType;
import com.itsaky.androidide.ui.view.IAttribute;
import com.itsaky.androidide.ui.view.IFixedValue;
import java.util.HashSet;
import java.util.Set;

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
        if (obj instanceof UiAttribute) {
            UiAttribute that = (UiAttribute) obj;
            return this.getNamespace().equals(that.getNamespace())
            && this.getAttributeName().equals(that.getAttributeName())
            && this.getValue().equals(that.getValue())
            && this.getFixedValues().equals(that.getFixedValues());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
