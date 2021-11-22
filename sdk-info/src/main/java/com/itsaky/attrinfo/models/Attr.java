/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
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
