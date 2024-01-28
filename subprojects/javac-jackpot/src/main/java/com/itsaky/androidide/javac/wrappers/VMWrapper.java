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

/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.itsaky.androidide.javac.wrappers;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class VMWrapper {
    private VMWrapper() {
    }

    public static String[] getRuntimeArguments() {
        return new String[0];
    }

    private static final String[] symbolFileLocation = { "lib", "ct.sym" };
    private static Reference<Path> cachedCtSym = new SoftReference<>(null);

    public static Path findCtSym() {
        Path obj = cachedCtSym.get();
        if (obj instanceof Path) {
            return obj;
        }
        try {
            ClassLoader loader = VMWrapper.class.getClassLoader();
            if (loader == null) {
                loader = ClassLoader.getSystemClassLoader();
            }
            Enumeration<URL> en = loader.getResources("META-INF/services/com.sun.tools.javac.platform.PlatformProvider");
            URL res = en.hasMoreElements() ? en.nextElement() : null;
            if (res == null) {
                //runnning inside a JDK image, try to look for lib/ct.sym:
                String javaHome = System.getProperty("java.home");
                Path file = Paths.get(javaHome);
                for (String name : symbolFileLocation) {
                    file = file.resolve(name);
                }
                if (!Files.exists(file)) {
                    throw new IllegalStateException("Cannot find ct.sym at " + file);
                }
                return FileSystems.newFileSystem(file, (ClassLoader)null).getRootDirectories().iterator().next();
            }
            if (!res.getProtocol().equals("jar")) {
                res = en.hasMoreElements() ? en.nextElement() : null;
            }
            URL jar = ((JarURLConnection)res.openConnection()).getJarFileURL();
            Path path = Paths.get(jar.toURI());
            FileSystem fs = FileSystems.newFileSystem(path, (ClassLoader) null);
            Path ctSym = fs.getPath("META-INF", "ct.sym");
            cachedCtSym = new SoftReference<>(ctSym);
            return ctSym;
        } catch (IOException | URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static DirectoryStream<Path> newDirectoryStream(Path dir) throws IOException {
        List<Path> all = new ArrayList<>();
        for (Path ch : Files.newDirectoryStream(dir)) {
            final String fileName = ch.getFileName().toString();
            if (fileName.endsWith("/")) {
                all.add(dir.resolve(fileName.substring(0, fileName.length() - 1)));
            } else {
                all.add(ch);
            }
        }
        return new DirectoryStream<Path>() {
            @Override
            public Iterator<Path> iterator() {
                return all.iterator();
            }

            @Override
            public void close() {
            }
        };
    }

    public static FileSystem pathFs(Path p) {
        return new FileSystem() {
            @Override
            public FileSystemProvider provider() {
                return p.getFileSystem().provider();
            }

            @Override
            public void close() throws IOException {
            }

            @Override
            public boolean isOpen() {
                return p.getFileSystem().isOpen();
            }

            @Override
            public boolean isReadOnly() {
                return p.getFileSystem().isReadOnly();
            }

            @Override
            public String getSeparator() {
                return p.getFileSystem().getSeparator();
            }

            @Override
            public Iterable<Path> getRootDirectories() {
                return Collections.singleton(p);
            }

            @Override
            public Iterable<FileStore> getFileStores() {
                return p.getFileSystem().getFileStores();
            }

            @Override
            public Set<String> supportedFileAttributeViews() {
                return p.getFileSystem().supportedFileAttributeViews();
            }

            @Override
            public Path getPath(String first, String... more) {
                Path r = p.resolve(first);
                for (String m : more) {
                    r = r.resolve(m);
                }
                return r;
            }

            @Override
            public PathMatcher getPathMatcher(String syntaxAndPattern) {
                return p.getFileSystem().getPathMatcher(syntaxAndPattern);
            }

            @Override
            public UserPrincipalLookupService getUserPrincipalLookupService() {
                return p.getFileSystem().getUserPrincipalLookupService();
            }

            @Override
            public WatchService newWatchService() throws IOException {
                return p.getFileSystem().newWatchService();
            }
        };
    }
}
