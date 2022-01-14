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
package com.itsaky.javac11;

import com.itsaky.androidide.utils.Environment;

import java.nio.file.Path;

/**
 * Javac classes read some important locations from this class file.
 *
 * @author Akash Yadav
 */
public class LocationsProvider {
    
    /**
     * Path of the JDK installation directory. Use by :
     * <br>
     * 1. {@link com.sun.tools.javac.file.Locations Locations} <br>
     * 2. {@link jdk.internal.jrtfs.SystemImage SystemImage}
     */
    public static Path javaHome;
    public static Path javaLibDir;
    public static Path javaLibModules;
    
    public static void init () {
        
        if (Environment.JAVA_HOME == null)
            Environment.init ();
        
        javaHome = Environment.JAVA_HOME.toPath ();
        javaLibDir = javaHome.resolve ("lib");
        javaLibModules = javaLibDir.resolve ("modules");
    }
    
    public static Path getJavaHome () {
        return javaHome;
    }
    
    public static Path getJavaLibDir () {
        return javaLibDir;
    }
    
    public static Path getJavaLibModules () {
        return javaLibModules;
    }
}
