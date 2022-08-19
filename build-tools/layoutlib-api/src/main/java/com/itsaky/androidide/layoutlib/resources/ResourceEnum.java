/*
 * Copyright (C) 2010 The Android Open Source Project
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

/**
 * An enum representing a resource qualifier value.
 */
public interface ResourceEnum {

    /**
     * Returns the resource string. This is to be used in resource folder names.
     */
    String getResourceValue();

    /**
     * Whether the value actually used on device. This returns true only if a device can report
     * this value, false if it's just used to qualify resources.
     */
    boolean isValidValueForDevice();

    /**
     * Whether the value is neither used for device nor resources. This returns false when
     * the value is only used for internal usage in the custom editors.
     */
    boolean isFakeValue();

    /**
     * Returns a short string for display value. The string does not need to show the context.
     * <p>For instance "exposed", which can be the value for the keyboard state or the navigation
     * state, would be valid since something else in the UI is expected to show if this is about the
     * keyboard or the navigation state.
     *
     * @see #getLongDisplayValue()
     */
    String getShortDisplayValue();

    /**
     * Returns a long string for display value. This must not only include the enum value but
     * context (qualifier) about what the value represents.
     * <p>For instance "Exposed keyboard", and "Export navigation", as "exposed" would not be
     * enough to know what qualifier the value is about.
     *
     * @see #getShortDisplayValue()
     */
    String getLongDisplayValue();
}
