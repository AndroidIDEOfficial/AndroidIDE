/*
 * Copyright (C) 2018 The Android Open Source Project
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
package com.itsaky.androidide.layoutlib.resources;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents Android quantities.
 *
 * @see <a href="https://developer.android.com/guide/topics/resources/string-resource#Plurals">Arity
 *     strings (plurals)</a>
 */
public enum Arity {
    ZERO("zero"),
    ONE("one"),
    TWO("two"),
    FEW("few"),
    MANY("many"),
    OTHER("other");

    public static final Arity[] EMPTY_ARRAY = {};

    @NonNull private final String name;

    Arity(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public static Arity getEnum(@NonNull String name) {
        for (Arity value : values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }

        return null;
    }
}
