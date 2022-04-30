/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.builder.model;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Represents some of an {@link AndroidLibrary}.
 *
 * <p>The separation from AndroidLibrary is a historical artifact.
 */
public interface AndroidBundle extends Library {

    /**
     * Returns an optional configuration name if the library is output by a module
     * that publishes more than one variant.
     */
    @Nullable
    String getProjectVariant();

    /**
     * Returns the location of the dependency bundle.
     */
    @NonNull
    File getBundle();

    /**
     * Returns the location of the unzipped AAR folder.
     *
     * @deprecated Users of this model are strongly encouraged to migrate to using the methods for
     *     the individual artifacts within the AAR instead.
     */
    @Deprecated
    @NonNull
    File getFolder();

    /**
     * Returns the list of direct library dependencies of this dependency.
     * The order is important.
     */
    @NonNull
    List<? extends AndroidLibrary> getLibraryDependencies();

    /**
     * Returns the collection of external Jar files that are included in the dependency.
     * @return a list of JavaDependency. May be empty but not null.
     */
    @NonNull
    Collection<? extends JavaLibrary> getJavaDependencies();

    /**
     * Returns the location of the manifest.
     */
    @NonNull
    File getManifest();

    /**
     * Returns the location of the jar file to use for packaging.
     *
     * @return a File for the jar file. The file may not point to an existing file.
     * @see #getCompileJarFile()
     */
    @NonNull
    File getJarFile();

    /**
     * Returns the location of the jar file to use for compiling.
     *
     * @return a File for the jar file. The file may not point to an existing file.
     * @see #getJarFile()
     */
    @NonNull
    File getCompileJarFile();

    /**
     * Returns the location of the non-namespaced res folder.
     *
     * @return a File for the res folder. The file may not point to an existing folder.
     */
    @NonNull
    File getResFolder();

    /**
     * Returns the location of the namespaced resources static library (res.apk).
     *
     * @return the static library apk. Does not exist if the library is not namespaced. May be null
     *     in Android Gradle Plugin < 4.1.0, where namespace support is experimental anyway.
     */
    @Nullable
    File getResStaticLibrary();

    /**
     * Returns the location of the assets folder.
     *
     * @return a File for the assets folder. The file may not point to an existing folder.
     */
    @NonNull
    File getAssetsFolder();

}
