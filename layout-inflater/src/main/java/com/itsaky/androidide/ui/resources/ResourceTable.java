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
package com.itsaky.androidide.ui.resources;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ResourceTable {
    
    private int packageIdentifier = 0;
    private final String packageName;
    private final BiMap<Integer, ResName> resourceTable = HashBiMap.create();
    
    private final ResourceIdGenerator androidResourceIdGenerator = new ResourceIdGenerator(0x01);
    
    public ResourceTable(String packageName) {
        this.packageName = packageName;
    }
    
    public Integer getResourceId(ResName resName) {
        Integer id = resourceTable.inverse().get(resName);
        if (id == null && resName != null && resName.name.contains(".")) {
            id = resourceTable.inverse().get(new ResName(resName.packageName, resName.type, underscorize(resName.name)));
        }
        return id != null ? id : 0;
    }
    
    public int getPackageIdentifier() {
        return packageIdentifier;
    }
    
    void addResource(int resId, String type, String name) {
        if (ResourceIds.isFrameworkResource(resId)) {
            androidResourceIdGenerator.record(resId, type, name);
        }
        ResName resName = new ResName(packageName, type, name);
        int resIdPackageIdentifier = ResourceIds.getPackageIdentifier(resId);
        if (getPackageIdentifier() == 0) {
            this.packageIdentifier = resIdPackageIdentifier;
        } else if (getPackageIdentifier() != resIdPackageIdentifier) {
            throw new IllegalArgumentException("Incompatible package for " + packageName + ":" + type + "/" + name + " with resId " + resIdPackageIdentifier + " to ResourceIndex with packageIdentifier " + getPackageIdentifier());
        }

        ResName existingEntry = resourceTable.put(resId, resName);
        if (existingEntry != null && !existingEntry.equals(resName)) {
            throw new IllegalArgumentException("ResId " + Integer.toHexString(resId) + " mapped to both " + resName + " and " + existingEntry);
        }
    }
    
    private static String underscorize(String s) {
        return s == null ? null : s.replace('.', '_');
    }
}
