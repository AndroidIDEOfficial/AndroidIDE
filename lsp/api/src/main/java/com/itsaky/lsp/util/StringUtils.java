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

package com.itsaky.lsp.util;

/**
 * @author Akash Yadav
 */
public class StringUtils {

    public static boolean matchesPartialName(CharSequence candidate, CharSequence partialName) {
        return matchesPartialName(candidate, partialName, false);
    }

    public static boolean matchesPartialName(
            CharSequence candidate, CharSequence partialName, boolean allLower) {
        if (candidate.length() < partialName.length()) {
            return false;
        }

        for (int i = 0; i < partialName.length(); i++) {
            char char1 = candidate.charAt(i);
            char char2 = partialName.charAt(i);

            // Match by keeping the first character of the partial name and candidate as is
            // The make all the characters lower case
            // So if the first character is upper case, we could assume that the user is expecting a
            // class
            // name
            // Otherwise a variable name
            if (allLower || i != 0) {
                char1 = Character.toLowerCase(char1);
                char2 = Character.toLowerCase(char2);
            }

            if (char1 != char2) {
                return false;
            }
        }
        return true;
    }
}
