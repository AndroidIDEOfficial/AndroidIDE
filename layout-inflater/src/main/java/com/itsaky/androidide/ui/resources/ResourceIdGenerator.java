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

import java.util.HashMap;
import java.util.Map;

public class ResourceIdGenerator {

    private final Map<String, TypeTracker> typeInfo = new HashMap<>();
    private int packageIdentifier;

    private static class TypeTracker {
        private int typeIdentifier;
        private int currentMaxEntry;

        TypeTracker(int typeIdentifier) {
            this.typeIdentifier = typeIdentifier;
        }

        void record(int entryIdentifier) {
            currentMaxEntry = Math.max(currentMaxEntry, entryIdentifier);
        }

        public int getFreeIdentifier() {
            return ++currentMaxEntry;
        }

        public int getTypeIdentifier() {
            return typeIdentifier;
        }
    }

    ResourceIdGenerator(int packageIdentifier) {
        this.packageIdentifier = packageIdentifier;
    }

    public void record(int resId, String type, String name) {
        TypeTracker typeTracker = typeInfo.get(type);
        if (typeTracker == null) {
            typeTracker = new TypeTracker(ResourceIds.getTypeIdentifier(resId));
            typeInfo.put(type, typeTracker);
        }
        typeTracker.record(ResourceIds.getEntryIdentifier(resId));
    }

    public int generate(String type, String name) {
        TypeTracker typeTracker = typeInfo.get(type);
        if (typeTracker == null) {
            typeTracker = new TypeTracker(getNextFreeTypeIdentifier());
            typeInfo.put(type, typeTracker);
        }
        return ResourceIds.makeIdentifer(packageIdentifier, typeTracker.getTypeIdentifier(), typeTracker.getFreeIdentifier());
    }

    private int getNextFreeTypeIdentifier() {
        int result = 0;
        for (TypeTracker typeTracker : typeInfo.values()) {
            result = Math.max(result, typeTracker.getTypeIdentifier());
        }
        return ++result;
    }
}
