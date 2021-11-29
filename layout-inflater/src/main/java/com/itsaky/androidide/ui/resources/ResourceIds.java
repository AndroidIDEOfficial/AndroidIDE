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
package com.itsaky.androidide.ui.resources;

public class ResourceIds {
    public static boolean isFrameworkResource(int resId) {
        return ((resId >>> 24) == 0x1);
    }

    public static int getPackageIdentifier(int resId) {
        return (resId >>> 24);
    }

    public static int getTypeIdentifier(int resId) {
        return (resId & 0x00FF0000) >>> 16;
    }

    public static int getEntryIdentifier(int resId) {
        return resId & 0x0000FFFF;
    }

    public static int makeIdentifer(int packageIdentifier, int typeIdentifier, int entryIdenifier) {
        return packageIdentifier << 24 | typeIdentifier << 16 | entryIdenifier;
    }
}
