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

/**
 * An enum representing visibility of an android resource.
 *
 * <p>The below is the description of how AAPT2 understands resource visibility:
 *
 * <p>Public visibility.
 *
 * <p>A resource can be marked as public by adding the {@code public} element to a values XML file:
 *
 * <p>{@code <public name="my_public_string" type="string"/>}.
 *
 * <p>These elements are usually defined in the {@code public.xml} file inside the {@code
 * res/values} directory.
 *
 * <p>Private visibility.
 *
 * <p>Sometimes a libraries can have a great number of resources and it might be confusing or
 * tiresome to find the correct one when writing the Java or Kotlin code. In order to restrict the
 * resources visible from the source code, one can list the only resources they want to be visible
 * by using the {@code java-symbol} element:
 *
 * <p>{@code <java-symbol name="my_visible_string" type="string/>}.
 *
 * <p>These elements are usually defined in the {@code symbols.xml} file inside the {@code
 * res/values} directory. The name {@code private} comes from these resources being present in the
 * {@code private R.java} along with {@code public} resources.
 *
 * <p>Private XML only visibility.
 *
 * <p>All resources that were not marked as {@code public} or {@code private} have the {@code
 * default} visibility. They are not placed in either {@code public} nor {@code private} R classes
 * and are only accessible from other XML resources within that library/module.
 *
 * <p>Without the package for the private R.java specified, only the public R.java will be generated
 * and it will contain all resources (ones marked as public, java-symbol and those not marked as
 * either).
 *
 * <p>Additionally in the Gradle plugin we have the last enum value, {@code UNDEFINED}. It
 * represents a case where the visibility was not defined at all. This should be only used in the
 * case when we are not generating the public and private R class.
 */
public enum ResourceVisibility {
    PRIVATE_XML_ONLY("default"),
    PRIVATE("private"),
    PUBLIC("public"),
    UNDEFINED("undefined");

    private final String qualifier;

    ResourceVisibility(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getName() {
        return qualifier;
    }

    public static ResourceVisibility getEnum(@NonNull String qualifier) {
        for (ResourceVisibility accessibility : values()) {
            if (accessibility.qualifier.equals(qualifier)) {
                return accessibility;
            }
        }

        return null;
    }

    public static ResourceVisibility max(
            @NonNull ResourceVisibility v1, @NonNull ResourceVisibility v2) {
        return v1.compareTo(v2) <= 0 ? v2 : v1;
    }
}
