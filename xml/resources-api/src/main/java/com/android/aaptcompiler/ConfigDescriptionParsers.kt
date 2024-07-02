/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Copyright (C) 2019 The Android Open Source Project
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

package com.android.aaptcompiler

import com.android.aaptcompiler.android.AConfiguration
import com.android.aaptcompiler.android.ResTableConfig
import com.android.aaptcompiler.android.ResTableConfig.COLOR_MODE
import com.android.aaptcompiler.android.ResTableConfig.INPUT_FLAGS
import com.android.aaptcompiler.android.ResTableConfig.SCREEN_LAYOUT
import com.android.aaptcompiler.android.ResTableConfig.SCREEN_LAYOUT2
import com.android.aaptcompiler.android.ResTableConfig.UI_MODE
import com.android.aaptcompiler.android.isTruthy

/** attempt to parse the Mobile Country Code */
fun parseMcc(part: String, config: ConfigDescription): Boolean {
    if (part == WILDCARD_NAME) {
        config.mcc = 0
        return true
    }

    // The only acceptable format is "mccDDD" (where D is a digit).
    if (!part.startsWith("mcc") || part.length != 6) {
        return false
    }
    val value = part.substring(3).toIntOrNull()?.toShort() ?: return false
    config.mcc = value
    return true
}

/** attempt to parse the Mobile Network Code */
fun parseMnc(part: String, config: ConfigDescription): Boolean {
    if (part == WILDCARD_NAME) {
        config.mnc = 0
        return true
    }

    // mnc may have a dynamic sized value ranging from 2 to 3 digits. It is possible to have unique
    // mcn such as 01 and 001 that are represented by the number digits.
    if (!part.startsWith("mnc") || part.length !in 4..6) {
        return false
    }
    val value = part.substring(3).toIntOrNull()?.toShort() ?: return false

    // Zero is reserved, so we need to use MINC_ZERO instead.
    config.mnc = if (value.isTruthy()) value else AConfiguration.ACONFIGURATION_MNC_ZERO.toShort()
    return true
}

// TODO(@daniellabar): Make more like the aapt2 parser.
fun parseLayoutDirection(part: String, config: ConfigDescription) : Boolean {
    val value = when(part) {
        WILDCARD_NAME -> ResTableConfig.SCREEN_LAYOUT.DIR_ANY
        "ldltr" -> ResTableConfig.SCREEN_LAYOUT.DIR_LTR
        "ldrtl" -> ResTableConfig.SCREEN_LAYOUT.DIR_RTL
        else -> return false
    }
    // AND existing config with the mask and then OR with the found layout direction.
    config.screenLayout =
      maskAndApply(config.screenLayout, SCREEN_LAYOUT.DIR_MASK, value)
    return true
}

fun parseSmallestScreenWidthDp(part: String, config: ConfigDescription): Boolean {
    if (part == WILDCARD_NAME) {
        config.smallestScreenWidthDp = 0
        return true
    }
    if (!part.startsWith("sw") || !part.endsWith("dp")) {
        return false
    }
    val value = part.substring(2, part.length-2).toIntOrNull() ?: return false
    config.smallestScreenWidthDp = value
    return true
}

fun parseScreenWidthDp(part: String, config: ConfigDescription): Boolean {
    if (part == WILDCARD_NAME) {
        config.screenWidthDp = 0
        return true
    }
    if (!part.startsWith('w') || !part.endsWith("dp")) {
        return false
    }
    val value = part.substring(1, part.length-2).toIntOrNull() ?: return false
    config.screenWidthDp = value
    return true
}

fun parseScreenHeightDp(part: String, config: ConfigDescription): Boolean {
    if (part == WILDCARD_NAME) {
        config.screenHeightDp = 0
        return true
    }
    if (!part.startsWith('h') || !part.endsWith("dp")) {
        return false
    }
    val value = part.substring(1, part.length-2).toIntOrNull() ?: return false
    config.screenHeightDp = value
    return true
}

fun parseScreenLayoutSize(part: String, config: ConfigDescription): Boolean {
    val value = when (part) {
        WILDCARD_NAME -> 0
        "small" -> ResTableConfig.SCREEN_LAYOUT.SIZE_SMALL
        "normal" -> ResTableConfig.SCREEN_LAYOUT.SIZE_NORMAL
        "large" -> ResTableConfig.SCREEN_LAYOUT.SIZE_LARGE
        "xlarge" -> ResTableConfig.SCREEN_LAYOUT.SIZE_XLARGE
        else -> return false
    }
    // AND existing config with the mask and then OR with the found layout size.
    config.screenLayout =
      maskAndApply(config.screenLayout, SCREEN_LAYOUT.SIZE_MASK, value)
    return true
}

fun parseScreenLayoutLong(part: String, config: ConfigDescription): Boolean {
    val value = when (part) {
        WILDCARD_NAME -> ResTableConfig.SCREEN_LAYOUT.SCREENLONG_ANY
        "long" -> ResTableConfig.SCREEN_LAYOUT.SCREENLONG_YES
        "notlong" -> ResTableConfig.SCREEN_LAYOUT.SCREENLONG_NO
        else -> return false
    }
    // AND existing config with the mask and then OR with the found layout size.
    config.screenLayout =
      maskAndApply(config.screenLayout, SCREEN_LAYOUT.SCREENLONG_MASK, value)
    return true
}

fun parseScreenRound(part: String, config: ConfigDescription): Boolean {
    val value = when (part) {
        WILDCARD_NAME -> ResTableConfig.SCREEN_LAYOUT2.SCREENROUND_ANY
        "round" -> ResTableConfig.SCREEN_LAYOUT2.SCREENROUND_YES
        "notround" -> ResTableConfig.SCREEN_LAYOUT2.SCREENROUND_NO
        else -> return false
    }
    config.screenLayout2 =
      maskAndApply(config.screenLayout2, SCREEN_LAYOUT2.SCREENROUND_MASK, value)
    return true
}

fun parseWideColorGamut(part: String, config: ConfigDescription): Boolean {
    val value = when (part) {
        WILDCARD_NAME -> ResTableConfig.COLOR_MODE.WIDE_GAMUT_ANY
        "widecg" -> ResTableConfig.COLOR_MODE.WIDE_GAMUT_YES
        "nowidecg" -> ResTableConfig.COLOR_MODE.WIDE_GAMUT_NO
        else -> return false
    }
    config.colorMode =
      maskAndApply(config.colorMode, COLOR_MODE.WIDE_GAMUT_MASK, value)
    return true
}

fun parseHdr(part: String, config: ConfigDescription): Boolean {
    val value = when (part) {
        WILDCARD_NAME -> ResTableConfig.COLOR_MODE.HDR_ANY
        "highdr" -> ResTableConfig.COLOR_MODE.HDR_YES
        "lowdr" -> ResTableConfig.COLOR_MODE.HDR_NO
        else -> return false
    }
    config.colorMode = maskAndApply(config.colorMode, COLOR_MODE.HDR_MASK, value)
    return true
}

fun parseOrientation(part: String, config: ConfigDescription): Boolean {
    config.orientation = when (part) {
        WILDCARD_NAME -> ResTableConfig.ORIENTATION.ANY
        "port" -> ResTableConfig.ORIENTATION.PORT
        "land" -> ResTableConfig.ORIENTATION.LAND
        "square" -> ResTableConfig.ORIENTATION.SQUARE
        else -> return false
    }
    return true
}

fun parseUiModeType(part: String, config: ConfigDescription): Boolean {
    val value = when (part) {
        WILDCARD_NAME -> ResTableConfig.UI_MODE.TYPE_ANY
        "desk" -> ResTableConfig.UI_MODE.TYPE_DESK
        "car" -> ResTableConfig.UI_MODE.TYPE_CAR
        "television" -> ResTableConfig.UI_MODE.TYPE_TELEVISION
        "appliance" -> ResTableConfig.UI_MODE.TYPE_APPLIANCE
        "watch" -> ResTableConfig.UI_MODE.TYPE_WATCH
        "vrheadset" -> ResTableConfig.UI_MODE.TYPE_VR_HEADSET
        else -> return false
    }
    config.uiMode = maskAndApply(config.uiMode, UI_MODE.TYPE_MASK, value)
    return true
}

fun parseUiModeNight(part: String, config: ConfigDescription): Boolean {
    val value = when (part) {
        WILDCARD_NAME -> ResTableConfig.UI_MODE.NIGHT_ANY
        "night" -> ResTableConfig.UI_MODE.NIGHT_YES
        "notnight" -> ResTableConfig.UI_MODE.NIGHT_NO
        else -> return false
    }
    config.uiMode = maskAndApply(config.uiMode, UI_MODE.NIGHT_MASK, value)
    return true
}

fun parseDensity(part: String, config: ConfigDescription): Boolean {
    if (part.isNotEmpty() && part[0].isLetter()) {
        config.density = when (part) {
            WILDCARD_NAME -> ResTableConfig.DENSITY.DEFAULT
            "anydpi" -> ResTableConfig.DENSITY.ANY
            "nodpi" -> ResTableConfig.DENSITY.NONE
            "ldpi" -> ResTableConfig.DENSITY.LOW
            "mdpi" -> ResTableConfig.DENSITY.MEDIUM
            "tvdpi" -> ResTableConfig.DENSITY.TV
            "hdpi" -> ResTableConfig.DENSITY.HIGH
            "xhdpi" -> ResTableConfig.DENSITY.XHIGH
            "xxhdpi" -> ResTableConfig.DENSITY.XXHIGH
            "xxxhdpi" -> ResTableConfig.DENSITY.XXXHIGH
            else -> return false
        }
        return true
    } else {
        // we might be dealing with "<number>dpi"
        if (!part.endsWith("dpi")) {
            return false
        }
        val value = part.substring(0, part.length-3).toIntOrNull() ?: return false
        config.density = value
        return true
    }
}

fun parseTouchscreen(part: String, config: ConfigDescription): Boolean {
    config.touchscreen = when (part) {
        WILDCARD_NAME -> ResTableConfig.TOUCHSCREEN.ANY
        "notouch" -> ResTableConfig.TOUCHSCREEN.NOTOUCH
        "stylus" -> ResTableConfig.TOUCHSCREEN.STYLUS
        "finger" -> ResTableConfig.TOUCHSCREEN.FINGER
        else -> return false
    }
    return true
}

fun parseKeysHidden(part: String, config: ConfigDescription): Boolean {
    val value = when (part) {
        WILDCARD_NAME -> ResTableConfig.INPUT_FLAGS.KEYSHIDDEN_ANY
        "keysexposed" -> ResTableConfig.INPUT_FLAGS.KEYSHIDDEN_NO
        "keyshidden" -> ResTableConfig.INPUT_FLAGS.KEYSHIDDEN_YES
        "keyssoft" -> ResTableConfig.INPUT_FLAGS.KEYSHIDDEN_SOFT
        else -> return false
    }
    config.inputFlags =
      maskAndApply(config.inputFlags, INPUT_FLAGS.KEYSHIDDEN_MASK, value)
    return true
}

fun parseKeyboard(part: String, config: ConfigDescription): Boolean {
    config.keyboard = when (part) {
        WILDCARD_NAME -> ResTableConfig.KEYBOARD.ANY
        "nokeys" -> ResTableConfig.KEYBOARD.NOKEYS
        "qwerty" -> ResTableConfig.KEYBOARD.QWERTY
        "12key" -> ResTableConfig.KEYBOARD.TWELVEKEY
        else -> return false
    }
    return true
}

fun parseNavHidden(part: String, config: ConfigDescription): Boolean {
    val value = when (part) {
        WILDCARD_NAME -> ResTableConfig.INPUT_FLAGS.NAVHIDDEN_ANY
        "navexposed" -> ResTableConfig.INPUT_FLAGS.NAVHIDDEN_NO
        "navhidden" -> ResTableConfig.INPUT_FLAGS.NAVHIDDEN_YES
        else -> return false
    }
    config.inputFlags =
      maskAndApply(config.inputFlags, INPUT_FLAGS.NAVHIDDEN_MASK, value)
    return true
}

fun parseNavigation(part: String, config: ConfigDescription): Boolean {
    config.navigation = when (part) {
        WILDCARD_NAME -> ResTableConfig.NAVIGATION.ANY
        "nonav" -> ResTableConfig.NAVIGATION.NONAV
        "dpad" -> ResTableConfig.NAVIGATION.DPAD
        "trackball" -> ResTableConfig.NAVIGATION.TRACKBALL
        "wheel" -> ResTableConfig.NAVIGATION.WHEEL
        else -> return false
    }
    return true
}

fun parseGrammaticalInflection(part: String, config: ConfigDescription): Boolean {
  config.grammaticalInflection = when (part) {
    WILDCARD_NAME -> ResTableConfig.GRAMMATICAL_GENDER.ANY
    "neuter" -> ResTableConfig.GRAMMATICAL_GENDER.NEUTER
    "feminine" -> ResTableConfig.GRAMMATICAL_GENDER.FEMININE
    "masculine" -> ResTableConfig.GRAMMATICAL_GENDER.MASCULINE
    else -> return false
  }
  return true
}

fun parseScreenSize(part: String, config: ConfigDescription): Boolean {
    if (part == WILDCARD_NAME) {
        config.screenWidth = 0
        config.screenHeight = 0
        return true
    }

    // Format is NxM
    val width = part.substringBefore('x').toIntOrNull() ?: return false
    val height = part.substringAfter('x').toIntOrNull() ?: return false
    if (width < height) {
        return false
    }
    config.screenWidth = width
    config.screenHeight = height
    return true
}

fun parseVersion(part: String, config: ConfigDescription): Boolean {
    if (part == WILDCARD_NAME) {
        config.sdkVersion = 0
        config.minorVersion = 0
        return true
    }

    // Format is vN
    if (!part.startsWith('v')) {
        return false
    }
    val version = part.substring(1).toIntOrNull()?.toShort() ?: return false
    config.sdkVersion = version
    config.minorVersion = 0
    return true
}

fun maskAndApply(current: Byte, mask: Int, value: Byte) =
    maskAndApply(current.toInt(), mask, value.toInt()).toByte()
fun maskAndApply(current: Int, mask: Int, value: Int): Int =
    (current and mask.inv()) or value

