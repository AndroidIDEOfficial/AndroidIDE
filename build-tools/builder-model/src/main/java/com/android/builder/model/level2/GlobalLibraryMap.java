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

package com.android.builder.model.level2;

import com.android.annotations.NonNull;
import com.android.builder.model.AndroidProject;
import java.util.Map;

/**
 * Global map of all the {@link Library} instances used in a single or multi-module gradle project.
 *
 * This is a separate model to query (the same way {@link AndroidProject} is queried). It must
 * be queried after all the models have been queried for their {@link AndroidProject}.
 *
 * @since 2.3
 */
public interface GlobalLibraryMap {

    /**
     * the list of external libraries used by all the variants in the module.
     *
     * @return the map of address to library
     */
    @NonNull
    Map<String, Library> getLibraries();
}
