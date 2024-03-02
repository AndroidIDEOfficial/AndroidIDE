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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.common.collect.Maps;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Allowed values of screen density.
 *
 * <p>This enum is used in the manifest in the uses-configuration node and in the resource folder
 * names as well as in other places that need to know the density values.
 */
public enum Density implements ResourceEnum {
    XXXHIGH("xxxhdpi", "XXX-High Density", 640, 18), //$NON-NLS-1$
    DPI_560("560dpi",  "560 DPI Density",  560,  1), //$NON-NLS-1$
    XXHIGH( "xxhdpi",  "XX-High Density",  480, 16), //$NON-NLS-1$
    DPI_440("440dpi",  "440 DPI Density",  440, 28),
    DPI_420("420dpi",  "420 DPI Density",  420, 23), //$NON-NLS-1$
    DPI_400("400dpi",  "400 DPI Density",  400,  1), //$NON-NLS-1$
    DPI_360("360dpi",  "360 DPI Density",  360, 23), //$NON-NLS-1$
    XHIGH(  "xhdpi",   "X-High Density",   320,  8), //$NON-NLS-1$
    DPI_260("260dpi",  "260 DPI Density",  260, 25), //$NON-NLS-1$
    DPI_280("280dpi",  "280 DPI Density",  280, 22), //$NON-NLS-1$
    DPI_300("300dpi",  "300 DPI Density",  300, 25), //$NON-NLS-1$
    DPI_340("340dpi",  "340 DPI Density",  340, 25), //$NON-NLS-1$
    HIGH(   "hdpi",    "High Density",     240,  4), //$NON-NLS-1$
    DPI_220("220dpi",  "220 DPI Density",  220, 29),
    TV(     "tvdpi",   "TV Density",       213, 13), //$NON-NLS-1$
    DPI_200("200dpi",  "200 DPI Density",  200, 29),
    DPI_180("180dpi",  "180 DPI Density",  180, 29),
    MEDIUM( "mdpi",    "Medium Density",   160,  4), //$NON-NLS-1$
    DPI_140("140dpi",  "140 DPI Density",  140, 29),
    LOW(    "ldpi",    "Low Density",      120,  4), //$NON-NLS-1$
    ANYDPI( "anydpi",  "Any Density",   0xFFFE, 21), // 0xFFFE is the value used by the framework.
    NODPI(  "nodpi",   "No Density",    0xFFFF,  4); // 0xFFFF is the value used by the framework.

    public static final int DEFAULT_DENSITY = MEDIUM.getDpiValue();
    private static final Map<String, Density> densityByValue =
            Maps.newHashMapWithExpectedSize(values().length);

    static {
        for (Density density : values()) {
            densityByValue.put(density.mValue, density);
        }
    }

    @NonNull private final String mValue;
    @NonNull private final String mDisplayValue;
    private final int mDpi;
    private final int mSince;

    Density(@NonNull String value, @NonNull String displayValue, int density, int since) {
        mValue = value;
        mDisplayValue = displayValue;
        mDpi = density;
        mSince = since;
    }

    /**
     * Returns the enum matching the provided qualifier value.
     *
     * @param value The qualifier value.
     * @return the enum for the qualifier value or null if no match was found.
     */
    @Nullable
    public static Density getEnum(@Nullable String value) {
        return densityByValue.get(value);
    }

    /**
     * Returns the enum matching the given DPI value.
     *
     * @param dpiValue The density value.
     * @return the enum for the density value or null if no match was found.
     */
    @Nullable
    public static Density getEnum(int dpiValue) {
        Density[] densities = values();
        for (Density density : densities) {
            if (density.mDpi == dpiValue) {
                return density;
            }
        }
        return null;
    }

    @Override
    @NonNull
    public String getResourceValue() {
        return mValue;
    }

    public int getDpiValue() {
        return mDpi;
    }

    public int since() {
        return mSince;
    }

    @Override
    @NonNull
    public String getShortDisplayValue() {
        return mDisplayValue;
    }

    @Override
    @NonNull
    public String getLongDisplayValue() {
        return mDisplayValue;
    }

    public static int getIndex(@Nullable Density value) {
        return value == null ? -1 : value.ordinal();
    }

    @Nullable
    public static Density getByIndex(int index) {
        try {
            return values()[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns all densities which are recommended and valid for a device.
     *
     * @see #isRecommended()
     * @see #isValidValueForDevice()
     */
    @NonNull
    public static Set<Density> getRecommendedValuesForDevice() {
        EnumSet<Density> result = EnumSet.noneOf(Density.class);
        for (Density value : values()) {
            if (value.isRecommended() && value.isValidValueForDevice()) {
                result.add(value);
            }
        }

        return result;
    }

    /**
     * Returns true if this density is relevant for app developers (e.g.
     * a density you should consider providing resources for)
     */
    public boolean isRecommended() {
        switch (this) {
            case TV:
            case DPI_140:
            case DPI_180:
            case DPI_200:
            case DPI_220:
            case DPI_260:
            case DPI_280:
            case DPI_300:
            case DPI_340:
            case DPI_360:
            case DPI_400:
            case DPI_420:
            case DPI_440:
            case DPI_560:
                return false;
            default:
                return true;
        }
    }

    @Override
    public boolean isFakeValue() {
        return false;
    }

    @Override
    public boolean isValidValueForDevice() {
        return this != NODPI && this != ANYDPI; // nodpi/anydpi is not a valid config for devices.
    }
}
