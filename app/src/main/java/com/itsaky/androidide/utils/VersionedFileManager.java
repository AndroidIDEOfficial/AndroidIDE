package com.itsaky.androidide.utils;
import java.io.File;
import java.util.HashMap;

public class VersionedFileManager {
    private static final HashMap<File, Integer> map = new HashMap<>();
    
    public static int fileOpened(File file) {
        // Initial version of file should always be 1
        map.put(file, 1);
        return 1;
    }
    
    public static int incrementVersion(File file) {
        if(!map.containsKey(file)) {
            map.put(file, 1);
        }
        
        int version = map.get(file);
        version++;
        map.put(file, version);
        return version;
    }
}
