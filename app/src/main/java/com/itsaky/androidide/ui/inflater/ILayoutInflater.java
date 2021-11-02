package com.itsaky.androidide.ui.inflater;

import java.io.File;
import java.util.Set;

/**
 * A layout inflater which inflates layout from a raw XML file
 *
 * @author Akash Yadav
 */
public abstract class ILayoutInflater {
    
    /**
     * Creates an instance of {@code ILayoutInflater}
     *
     * @param config The configuration of the LayoutInflater
     * @return A new layout inflater instance
     */
    public static ILayoutInflater newInstance (LayoutInflaterConfiguration config) {
        return new XMLLayoutInflater (config);
    }
}
