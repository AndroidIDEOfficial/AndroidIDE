/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
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

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.Logger;

import org.jetbrains.annotations.Contract;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Represents an xml attribute which is read from {@code attrs.xml}.
 *
 * @author Akash Yadav
 * @see com.itsaky.attrinfo.AttrInfo
 */
public class Attr {
    
    public static final int REFERENCE = 1;
    public static final int COLOR = 1 << 1;
    public static final int BOOLEAN = 1 << 2;
    public static final int DIMENSION = 1 << 3;
    public static final int FLOAT = 1 << 4;
    public static final int INTEGER = 1 << 5;
    public static final int FRACTION = 1 << 6;
    public static final int STRING = 1 << 7;
    public static final int ENUM = 1 << 8;
    public static final int FLAG = 1 << 9;
    public static final int UNKNOWN = 1 << 10;
    
    public String namespace;
    public String name;
    public Set<String> possibleValues;
    
    public int format;
    
    private static final Logger LOG = Logger.instance ("AttrInfo::Attr");
    
    public Attr (String name, boolean isAndroid) {
        this.name = name;
        this.namespace = isAndroid ? "android" : "app";
        this.possibleValues = new TreeSet<> ();
    }
    
    public boolean hasPossibleValues () {
        return possibleValues != null && possibleValues.size () > 0;
    }
    
    @Override
    public String toString () {
        return "Attr [" +
                "  name: " + name + "\n" +
                "  namespace: " + namespace + "\n" +
                "  values: " + TextUtils.join (", ", this.possibleValues) + "\n" +
                "  format: " + format + "\n" +
                "]";
    }
    
    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass () != o.getClass ()) {
            return false;
        }
        
        Attr attr = (Attr) o;
        return format == attr.format
                && Objects.equals (namespace, attr.namespace)
                && Objects.equals (name, attr.name)
                && Objects.equals (possibleValues, attr.possibleValues);
    }
    
    @Override
    public int hashCode () {
        return Objects.hash (namespace, name, possibleValues, format);
    }
    
    public static int formatForName (@NonNull String names) {
        var result = 0;
        if (names.contains ("|")) {
            for (var name : names.split (Pattern.quote ("|"))) {
                result |= formatForSingleName (name);
            }
        } else {
            result = formatForSingleName (names);
        }
        return result;
    }
    
    @Contract(pure = true)
    public static int formatForSingleName (@NonNull String name) {
        switch (name) {
            case "reference":
                return REFERENCE;
            case "color":
                return COLOR;
            case "boolean":
                return BOOLEAN;
            case "dimension":
                return DIMENSION;
            case "float":
                return FLOAT;
            case "integer":
                return INTEGER;
            case "fraction":
                return FRACTION;
            case "string":
                return STRING;
            case "enum":
                return ENUM;
            case "flag":
                return FLAG;
            default:
                return UNKNOWN;
        }
    }
}
