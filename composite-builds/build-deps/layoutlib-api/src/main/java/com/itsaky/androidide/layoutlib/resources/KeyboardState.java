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
 * Keyboard state enum.
 * <p>This is used in the manifest in the uses-configuration node and in the resource folder names.
 */
public enum KeyboardState implements ResourceEnum {
    EXPOSED("keysexposed", "Exposed", "Exposed keyboard"), //$NON-NLS-1$
    HIDDEN("keyshidden", "Hidden", "Hidden keyboard"),    //$NON-NLS-1$
    SOFT("keyssoft", "Soft", "Soft keyboard");          //$NON-NLS-1$

    private final String mValue;
    private final String mShortDisplayValue;
    private final String mLongDisplayValue;

    KeyboardState(String value, String shortDisplayValue, String longDisplayValue) {
        mValue = value;
        mShortDisplayValue = shortDisplayValue;
        mLongDisplayValue = longDisplayValue;
    }

    /**
     * Returns the enum for matching the provided qualifier value.
     * @param value The qualifier value.
     * @return the enum for the qualifier value or null if no matching was found.
     */
    public static KeyboardState getEnum(String value) {
        for (KeyboardState state : values()) {
            if (state.mValue.equals(value)) {
                return state;
            }
        }

        return null;
    }

    @Override
    public String getResourceValue() {
        return mValue;
    }

    @Override
    public String getShortDisplayValue() {
        return mShortDisplayValue;
    }

    @Override
    public String getLongDisplayValue() {
        return mLongDisplayValue;
    }

    public static int getIndex(KeyboardState value) {
        return value == null ? -1 : value.ordinal();
    }

    public static KeyboardState getByIndex(int index) {
        KeyboardState[] values = values();
        if (index >= 0 && index < values.length) {
            return values[index];
        }
        return null;
    }

    @Override
    public boolean isFakeValue() {
        return false;
    }

    @Override
    public boolean isValidValueForDevice() {
        return true;
    }

}
