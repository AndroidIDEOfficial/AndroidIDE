/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.android.builder.model.level2;


import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import java.io.File;
import java.util.Collection;

/**
 * Represent a variant/module/artifact dependency.
 * @since 2.3
 */
public interface Library {

    int LIBRARY_ANDROID = 1;
    int LIBRARY_JAVA = 2;
    int LIBRARY_MODULE = 3;

    /**
     * The type of the dependency
     *
     * @return the type
     * @see #LIBRARY_ANDROID
     * @see #LIBRARY_JAVA
     * @see #LIBRARY_MODULE
     */
    int getType();

    /**
     * Returns the artifact address in a unique way.
     *
     * This is either a module path for sub-modules (with optional variant name), or a maven
     * coordinate for external dependencies.
     */
    @NonNull
    String getArtifactAddress();

    /**
     * Returns the artifact location.
     */
    @NonNull
    File getArtifact();

    /**
     * Returns the build id.
     *
     * <p>This is only valid if the {@link #getProjectPath()} is not null. However this can still be
     * null if this is the root project.
     *
     * @return the build id or null.
     */
    @Nullable
    String getBuildId();

    /**
     * Returns the gradle path.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_MODULE}
     */
    @Nullable
    String getProjectPath();

    /**
     * Returns an optional variant name if the consumed artifact of the library is associated
     * to one.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_MODULE}
     */
    @Nullable
    String getVariant();

    /**
     * Returns the location of the unzipped bundle folder.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     */
    @NonNull
    File getFolder();

    /**
     * Returns the location of the manifest relative to the folder.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     */
    @NonNull
    String getManifest();

    /**
     * Returns the location of the jar file to use for compiling and packaging.
     *
     * <p>Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}.
     *
     * @return the path to the jar file. The path may not point to an existing file.
     */
    @NonNull
    String getJarFile();

    /**
     * Returns the location of the jar file to use for compilation.
     *
     * <p>Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}.
     *
     * @return path to the jar file used for compilation. The path may not point to an existing
     *     file.
     */
    @NonNull
    String getCompileJarFile();

    /**
     * Returns the location of the res folder.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     *
     * @return a File for the res folder. The file may not point to an existing folder.
     */
    @NonNull
    String getResFolder();

    /**
     * Returns the location of the namespaced resources static library (res.apk).
     *
     * <p>Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     *
     * <p>TODO(b/109854607): When rewriting dependencies, this should be populated with the
     * rewritten artifact, which will not be in the exploded AAR directory.
     *
     * @return the static library apk. Null if the library is not namespaced.
     */
    @Nullable
    File getResStaticLibrary();

    /**
     * Returns the location of the assets folder.
     *
     * <p>Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     *
     * @return a File for the assets folder. The file may not point to an existing folder.
     */
    @NonNull
    String getAssetsFolder();

    /**
     * Returns the list of local Jar files that are included in the dependency.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     *
     * @return a list of File. May be empty but not null.
     */
    @NonNull
    Collection<String> getLocalJars();

    /**
     * Returns the location of the jni libraries folder.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     *
     * @return a File for the folder. The file may not point to an existing folder.
     */
    @NonNull
    String getJniFolder();

    /**
     * Returns the location of the aidl import folder.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     *
     * @return a File for the folder. The file may not point to an existing folder.
     */
    @NonNull
    String getAidlFolder();

    /**
     * Returns the location of the renderscript import folder.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     *
     * @return a File for the folder. The file may not point to an existing folder.
     */
    @NonNull
    String getRenderscriptFolder();

    /**
     * Returns the location of the proguard files.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     *
     * @return a File for the file. The file may not point to an existing file.
     */
    @NonNull
    String getProguardRules();

    /**
     * Returns the location of the lint jar.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     *
     * @return a File for the jar file. The file may not point to an existing file.
     */
    @NonNull
    String getLintJar();

    /**
     * Returns the location of the external annotations zip file (which may not exist)
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     *
     * @return a File for the zip file. The file may not point to an existing file.
     */
    @NonNull
    String getExternalAnnotations();

    /**
     * Returns the location of an optional file that lists the only
     * resources that should be considered public.
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     *
     * @return a File for the file. The file may not point to an existing file.
     */
    @NonNull
    String getPublicResources();

    /**
     * Returns the location of the text symbol file
     *
     * Only valid for Android Library where {@link #getType()} is {@link #LIBRARY_ANDROID}
     */
    @NonNull
    String getSymbolFile();
}
