package com.itsaky.androidide.ui.parser;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ResourceTable {
    
    private int packageIdentifier;
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
