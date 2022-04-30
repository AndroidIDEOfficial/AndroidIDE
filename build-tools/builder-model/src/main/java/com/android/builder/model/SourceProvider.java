/*
 * Copyright (C) 2012 The Android Open Source Project
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
import com.android.builder.model.v2.CustomSourceDirectory;
import java.io.File;
import java.util.Collection;

/**
 * Represent a SourceProvider for a given configuration.
 *
 * TODO: source filters?
 */
public interface SourceProvider {

    /**
     * Returns the name of this source set.
     *
     * @return The name. Never returns null.
     */
    @NonNull
    String getName();

    /**
     * Returns the manifest file.
     *
     * @return the manifest file. It may not exist.
     */
    @NonNull
    File getManifestFile();

    /**
     * Returns the java source folders.
     *
     * @return a list of folders. They may not all exist.
     */
    @NonNull
    Collection<File> getJavaDirectories();

    /**
     * Returns the kotlin source folders.
     *
     * @return a list of folders. They may not all exist.
     */
    @NonNull
    Collection<File> getKotlinDirectories();

    /**
     * Returns the java resources folders.
     *
     * @return a list of folders. They may not all exist.
     */
    @NonNull
    Collection<File> getResourcesDirectories();

    /**
     * Returns the aidl source folders.
     *
     * @return a list of folders. They may not all exist.
     */
    @NonNull
    Collection<File> getAidlDirectories();

    /**
     * Returns the renderscript source folders.
     *
     * @return a list of folders. They may not all exist.
     */
    @NonNull
    Collection<File> getRenderscriptDirectories();

    /**
     * Returns the C source folders.
     *
     * @return a list of folders. They may not all exist.
     * @deprecated since ndk-compile is deprecated
     */
    @NonNull
    @Deprecated
    Collection<File> getCDirectories();

    /**
     * Returns the C++ source folders.
     *
     * @return a list of folders. They may not all exist.
     * @deprecated since ndk-compile is deprecated
     */
    @NonNull
    @Deprecated
    Collection<File> getCppDirectories();

    /**
     * Returns the android resources folders.
     *
     * @return a list of folders. They may not all exist.
     */
    @NonNull
    Collection<File> getResDirectories();

    /**
     * Returns the android assets folders.
     *
     * @return a list of folders. They may not all exist.
     */
    @NonNull
    Collection<File> getAssetsDirectories();

    /**
     * Returns the native libs folders.
     *
     * @return a list of folders. They may not all exist.
     */
    @NonNull
    Collection<File> getJniLibsDirectories();

    /**
     * Returns the shader folders.
     *
     * @return a list of folders. They may not all exist.
     */
    @NonNull
    Collection<File> getShadersDirectories();

    /**
     * Returns the machine learning models folders.
     *
     * @return a list of folders. They may not all exist.
     */
    @NonNull
    Collection<File> getMlModelsDirectories();

    /**
     * Returns the map of registered custom directories, key is the name provided when registering
     * the source set, and value is the list of folders for that custom source set.
     *
     * @return a map of source set name to source set folders.
     */
    @NonNull
    Collection<CustomSourceDirectory> getCustomDirectories();
}
