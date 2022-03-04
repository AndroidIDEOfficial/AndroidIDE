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

package com.itsaky.lsp.java.utils;

import androidx.annotation.NonNull;

import com.google.common.reflect.ClassPath;
import com.itsaky.androidide.utils.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ScanClassPath {

    public static Set<String> classPathTopLevelClasses(Set<Path> classPath) {
        LOG.info(
                String.format(
                        Locale.getDefault(),
                        "Searching for top-level classes in %d classpath locations",
                        classPath.size()));

        URL[] urls = classPath.stream().map(ScanClassPath::toUrl).toArray(URL[]::new);
        ClassLoader classLoader = new URLClassLoader(urls, null);

        ClassPath scanner;

        try {
            scanner = ClassPath.from(classLoader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Set<String> classes = new HashSet<>();
        for (ClassPath.ClassInfo c : scanner.getTopLevelClasses()) {
            classes.add(c.getName());
        }

        LOG.info(String.format(Locale.ROOT, "Found %d classes in classpath", classes.size()));

        return classes;
    }

    private static URL toUrl(@NonNull Path p) {
        try {
            return p.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Logger LOG = Logger.instance("ScanClassPath");
}
