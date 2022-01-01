/************************************************************************************
 * This file is part of AndroidIDE.
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
     * @return The drawable resource.
     */
    File findDrawable (@NonNull String name);
    
    /**
     * Find the layout file with the specified name.
     *
     * @param name The name of the layout file
     * @return The found layout resource file
     */
    File inflateLayout (@NonNull String name);
    
    /**
     * Find a string resource. Must be a valid string or {@code null}
     *
     * @param name The name of the resource
     * @return Value of the resource
     */
    String findString (@NonNull String name);
    
    /**
     * Find a color resource. The returned value must be a valid color value or {@code -1}
     *
     * @param name The name of the resource
     * @return Value of the resource
     */
    String findColor (@NonNull String name);
    
    /**
     * Find an array resource. Must be a valid array or {@code null}
     *
     * @param name The name of the resource
     * @return Value of the resource
     */
    String[] findArray (@NonNull String name);
    
    /**
     * Find the dimension value with the given name.
     *
     * @param name The name of the dimension resource
     * @return The String representation of the dimension. It could one of the following format :
     *   1dp, 12sp, 123px, etc.
     */
    String findDimension (@NonNull String name);
    
    /**
     * Set the file which is currently being inflated.
     *
     * @param file The file.
     */
    void setInflatingFile (@NonNull File file);
}
