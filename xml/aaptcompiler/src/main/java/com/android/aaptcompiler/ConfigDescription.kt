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

import com.android.aaptcompiler.android.ResTableConfig


class ConfigDescription(base: ResTableConfig = ResTableConfig()): ResTableConfig(base) {
    override fun equals(other: Any?): Boolean {
        if (other is ConfigDescription) {
            return compareTo(other) == 0
        }
        return false
    }

    private fun compatible(first: Int, second: Int) = first == 0 || second == 0 || first == second
}

const val WILDCARD_NAME: String = "any"

fun parse(config: String): ConfigDescription {
    val parts = config.split('-').map { it.lowercase()}
    var index = 0

    val configDescription = ConfigDescription()
    val locale = LocaleValue()

    if (parts.isEmpty() || config.isEmpty()) {
        return applyVersionForCompatibility(configDescription)
    }

    if (parseMcc(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseMnc(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    // Locale spans a few '-' separators, so we let it control the index
    index += locale.initFromParts(parts, index)
    locale.writeTo(configDescription)
    if (index == parts.size) {
        return configDescription
    }

    if (parseGrammaticalInflection(parts[index], configDescription)) {
      index++
      if (index == parts.size) {
        return applyVersionForCompatibility(configDescription)
      }
    }

    if(parseLayoutDirection(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseSmallestScreenWidthDp(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseScreenWidthDp(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseScreenHeightDp(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseScreenLayoutSize(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseScreenLayoutLong(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseScreenRound(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseWideColorGamut(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseHdr(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseOrientation(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseUiModeType(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseUiModeNight(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseDensity(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseTouchscreen(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseKeysHidden(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseKeyboard(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseNavHidden(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseNavigation(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseScreenSize(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    if (parseVersion(parts[index], configDescription)) {
        index++
        if (index == parts.size) {
            return applyVersionForCompatibility(configDescription)
        }
    }

    throw error("Unrecognized part '${parts[index]}' in configuration $config")
}

private fun applyVersionForCompatibility(config: ConfigDescription): ConfigDescription {
    val minSdk = when {
        config.uiModeType() ==  ResTableConfig.UI_MODE.TYPE_VR_HEADSET ||
            config.wideColorGamut() != ResTableConfig.COLOR_MODE.WIDE_GAMUT_ANY ||
            config.hdr() != ResTableConfig.COLOR_MODE.HDR_ANY -> SDKConstants.SDK_O
        config.layoutRound() != 0.toByte() -> SDKConstants.SDK_MARSHMALLOW
        config.density == ResTableConfig.DENSITY.ANY -> SDKConstants.SDK_LOLLIPOP
        config.smallestScreenWidthDp != 0 ||
            config.screenWidthDp != 0 ||
            config.screenHeightDp != 0-> SDKConstants.SDK_HONEYCOMB_MR2
        config.uiModeType() != ResTableConfig.UI_MODE.TYPE_ANY ||
            config.uiModeNight() != ResTableConfig.UI_MODE.NIGHT_ANY -> SDKConstants.SDK_FROYO
        config.layoutSize() != ResTableConfig.SCREEN_LAYOUT.SIZE_ANY ||
            config.layoutLong() != ResTableConfig.SCREEN_LAYOUT.SCREENLONG_ANY ||
            config.density != ResTableConfig.DENSITY.DEFAULT -> SDKConstants.SDK_DONUT
        config.grammaticalInflection != ResTableConfig.GRAMMATICAL_GENDER.ANY -> SDKConstants.SDK_U
        else -> 0
    }

    if (minSdk > config.sdkVersion) {
        config.sdkVersion = minSdk
    }

    return config
}
