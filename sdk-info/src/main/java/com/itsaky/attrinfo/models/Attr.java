package com.itsaky.attrinfo.models;

import java.util.HashSet;
import java.util.Set;

public class Attr {
    public String prefix;
    public String name;

    public Set<String> possibleValues;

    public Attr(String name, boolean isAndroid) {
        this.name = name;
        this.prefix = isAndroid ? "android" : "app";
        this.possibleValues = new HashSet<>();
    }

    public boolean hasPossibleValues() {
        return possibleValues != null && possibleValues.size() > 0;
    }

    @Override
    public String toString() {
        return String.format("[%s=%s]", name, possibleValues.toString());
    }
}
