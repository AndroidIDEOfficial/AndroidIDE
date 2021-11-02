package com.itsaky.androidide.ui.inflater;

import java.io.File;
import java.util.Set;

public class LayoutInflaterConfiguration {
    
    
    private final Set<File> resDirs;

    public LayoutInflaterConfiguration(Set<File> resDirs) {
        this.resDirs = resDirs;
    }

}
