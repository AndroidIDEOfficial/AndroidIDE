package com.itsaky.androidide.ui.inflater;

import java.io.File;

/**
 * Finds values in resources of a project
 */
public interface IResourceFinder {
    
    /**
     * Find the path of the drawable with the provided name
     * <p>
     * This method will be called if the value of attribute starts with '@drawable/'
     *
     * @param name The name of the drawable
     * @return The path of the drawable resource.
     */
    File findDrawable (String name);
    
    /**
     * Find the layout file with the specified name.
     *
     * @param name The name of the layout file
     * @return The found layout resource file
     */
    File findLayout (String name);
    
    /**
     * Find a string resource. Must be a valid string or {@code null}
     *
     * @param name The name of the resource
     * @return Value of the resource
     */
    String findString (String name);
    
    /**
     * Find a color resource. The returned value must be a valid color value or {@code -1}
     *
     * @param name The name of the resource
     * @return Value of the resource
     */
    String findColor (String name);
    
    /**
     * Find an array resource. Must be a valid array or {@code null}
     *
     * @param name The name of the resource
     * @return Value of the resource
     */
    Object[] findArray (String name);
    
    /**
     * Find the dimension value with the given name.
     *
     * @param name The name of the dimension resource
     * @return The String representation of the dimension. It could one of the following format :
     *   1dp, 12sp, 123px, etc.
     */
    String findDimension (String name);
}
