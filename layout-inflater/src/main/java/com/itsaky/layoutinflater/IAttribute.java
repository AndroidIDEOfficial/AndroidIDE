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
package com.itsaky.layoutinflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.Set;

/**
 * Attribute that can be applied to a {@link IView}
 *
 * @author Akash Yadav
 */
public interface IAttribute {
    
    /**
     * The android namespace
     */
    public static final String NS_ANDROID = "android";
    
    @NonNull
    String getNamespace ();
    
    @NonNull
    String getAttributeName ();
    
    @NonNull
    String getValue ();

    public static final Comparator <IAttribute> COMPARATOR = (first, second) -> {
        var result = compareNull(first, second);
        if (result != IAttribute.DO_COMPARE) {
            return result;
        }

        result = compareNull(first.getNamespace(), second.getNamespace());
        if (result != IAttribute.DO_COMPARE) {
            return result;
        }

        // If we reach here, then it means that the attributes and their namespaces are not null
        // First, the attributes must be compared by their namespaces

        final var firstNs = first.getNamespace();
        final var secondNs = second.getNamespace();

        if (firstNs.equals(secondNs)) {

            // Compare by attribute names if the namespaces are same
            return first.getAttributeName().compareTo(second.getAttributeName());
        }

        // xmlns declarations must be on top
        if (firstNs.equals("xmlns") && !secondNs.equals("xmlns")) {
            return -1;
        }

        if (!firstNs.equals("xmlns") && secondNs.equals("xmlns")) {
            return 1;
        }

        return 0;
    };

    /**
     * Returned by {@link #compareNull(Object, Object)} to indicate
     * that the comparison must be done further.
     */
    static final int DO_COMPARE = 2;

    private static int compareNull (Object first, Object second) {
        if (first == null && second == null) {
            return 0;
        }

        if (first != null && second == null) {
            return -1;
        }

        if (first == null && second != null) {
            return 1;
        }

        return DO_COMPARE;
    }
}
