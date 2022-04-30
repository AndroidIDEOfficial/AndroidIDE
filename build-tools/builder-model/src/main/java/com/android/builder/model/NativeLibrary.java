/*
 * Copyright (C) 2020 The Android Open Source Project
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
import java.io.File;
import java.util.List;

/**
 * A Native Library. The configurations used to create a shared object.
 *
 * <p>Deprecated since ndk-compile is deprecated.
 */
@Deprecated
public interface NativeLibrary {

    /**
     * Returns the name of the native library.
     *
     * A native library "libfoo.so" would have the name of "foo".
     *
     * @return name of the native library.
     */
    @NonNull
    String getName();

    /**
     * Returns the ABI of the library.
     *
     * @return abi of the library.
     */
    @NonNull
    String getAbi();

    /**
     * Returns the name of the toolchain used to compile the native library.
     *
     * @return name of the toolchain.
     */
    @NonNull
    String getToolchainName();

    /**
     * A list of include directories for compiling C code.
     *
     * @return list of include directories.
     */
    @NonNull
    List<File> getCIncludeDirs();

    /**
     * A list of include directories for compiling C++ code.
     *
     * @return list of include directories.
     */
    @NonNull
    List<File> getCppIncludeDirs();

    /**
     * A list of system include directories for compiling C code.
     *
     * @return list of include directories.
     */
    @NonNull
    List<File> getCSystemIncludeDirs();

    /**
     * A list of system include directories for compiling C++ code.
     *
     * @return list of include directories.
     */
    @NonNull
    List<File> getCppSystemIncludeDirs();

    /**
     * A list of defines for C code.
     *
     * @return list of defines.
     */
    @NonNull
    List<String> getCDefines();

    /**
     * A list of defines for C++ code.
     *
     * @return list of defines.
     */
    @NonNull
    List<String> getCppDefines();

    /**
     * A list of compiler flags for C code.
     *
     * @return list of compiler flags.
     */
    @NonNull
    List<String> getCCompilerFlags();

    /**
     * A list of compiler flags for C++ code.
     *
     * @return list of compiler flags.
     */
    @NonNull
    List<String> getCppCompilerFlags();

    /**
     * The folders containing built libraries with debug information.
     *
     * @return list of paths to locate shared objects with debug information.
     */
    @NonNull
    List<File> getDebuggableLibraryFolders();

}
