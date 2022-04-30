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

/**
 * Options for aapt, but only those needed by the IDE.
 */
public interface AaptOptions {
    enum Namespacing {
        /**
         * Resources are not namespaced.
         *
         * <p>They are merged at the application level, as was the behavior with AAPT1
         */
        DISABLED,
        /**
         * Resources must be namespaced.
         *
         * <p>Each library is compiled in to an AAPT2 static library with its own namespace.
         *
         * <p>Projects using this <em>cannot</em> consume non-namespaced dependencies.
         */
        REQUIRED,
        // TODO: add more modes as implemented.
    }

    /** Returns the resource namespacing strategy for this sub-project */
    @NonNull
    Namespacing getNamespacing();
}
