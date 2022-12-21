/*
 * Copyright (C) 2015 The Android Open Source Project
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
 * Screen Round enum.
 * <p>This is used in the resource folder names.
 */
public enum ScreenRound implements ResourceEnum {
    NOTROUND("notround", "Not Round", "Not Round screen"), //$NON-NLS-1$
    ROUND(   "round",    "Round",     "Round screen"); //$NON-NLS-1$

    private final String mValue;
    private final String mShortDisplayValue;
    private final String mLongDisplayValue;

    ScreenRound(String value, String displayValue, String longDisplayValue) {
        mValue = value;
        mShortDisplayValue = displayValue;
        mLongDisplayValue = longDisplayValue;
    }

    /**
     * Returns the enum for matching the provided qualifier value.
     * @param value The qualifier value.
     * @return the enum for the qualifier value or null if no matching was found.
     */
    public static ScreenRound getEnum(String value) {
        for (ScreenRound orient : values()) {
            if (orient.mValue.equals(value)) {
                return orient;
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

    public static int getIndex(ScreenRound value) {
        return value == null ? -1 : value.ordinal();
    }

    public static ScreenRound getByIndex(int index) {
        ScreenRound[] values = values();
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

