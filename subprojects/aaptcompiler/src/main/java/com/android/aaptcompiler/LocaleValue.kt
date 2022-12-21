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

class LocaleValue {
    // Language should be stored completely as lower case.
    var language: String = ""
        set(word) {
            field = word.lowercase()
        }
    // Region should be stored completely as upper case.
    var region: String = ""
        set(word) {
            field = word.uppercase()
        }
    // Script needs to start with a capital letter, and the rest needs to be lower case.
    var script: String = ""
        set(word) {
            field = word.replaceFirstChar { it.titlecase() }
        }
    // Variant is left as-is.
    var variant: String = ""

    fun initFromParts(parts: List<String>, index: Int): Int {
        val part = parts[index]
        if (part.startsWith("b+")) {
            // This is a "modified" BCP 47 language tag. Same semantics as BCP 47 tags, except that
            // the separator is "+" and not "-". Skip the prefix "b+".
            initFromBcp47TagImpl(part.substring(2), '+')
            // BCP 47 only takes one part, so return 1.
            return 1
        } else {
            if ((part.length == 2 || part.length == 3) && isAlpha(part) && part != "car") {
                language = part

                // It's also possible that the region is also set in the next word.
                if (index + 1 < parts.size) {
                    val maybeRegion = parts[index+1]
                    if (maybeRegion.startsWith('r') && maybeRegion.length == 3) {
                        region = maybeRegion.substring(1)// skip the 'r'
                        // We consumed two parts, so return 2.
                        return 2
                    }
                }
                // If no region present, then we only consumed one part.
                return 1
            }
        }
        return 0
    }

    fun writeTo(config: ResTableConfig) {
        if (language.isNotEmpty()) {
            config.packLanguage(language)
        }
        if (region.isNotEmpty()) {
            config.packRegion(region)
        }

        if (script.isNotEmpty()) {
            if (script.length > config.localeScript.size) {
                throw IllegalStateException(
                    "Locale script '$script' exceeds ${config.localeScript.size} characters.")
            }
            script.toByteArray().copyInto(config.localeScript, 0, 0, script.length)
        }

        if (variant.isNotEmpty()) {
            if (variant.length > config.localeVariant.size) {
                throw IllegalStateException(
                    "Locale variant '$variant' exceeds ${config.localeVariant.size} characters.")
            }
            variant.toByteArray().copyInto(config.localeVariant, 0, 0, variant.length)
        }
    }

    fun initFromBcp47Tag(word: String) = initFromBcp47TagImpl(word, '-')

    private fun isAlpha(word: String): Boolean = word.all { it.isLetter() }

    private fun initFromBcp47TagImpl(word: String, separator: Char): Boolean {
        val subTags = word.split(separator).map { it.lowercase() }
        when (subTags.size) {
            1 -> language = subTags[0]
            2 -> {
                language = subTags[0]

                when (subTags[1].length) {
                    2, 3 -> region = subTags[1]
                    4 -> {
                        if (subTags[1][0].isDigit()) {
                            // This is a variant
                            variant = subTags[1]
                        } else {
                            script = subTags[1]
                        }
                    }
                    in 5..8 -> variant = subTags[1]
                    else -> return false
                }
            }
            3 -> {
                // Language is always the front.
                language = subTags[0]

                // The second is a script or a region code.
                when (subTags[1].length) {
                    2, 3 -> region = subTags[1]
                    4 -> script = subTags[1]
                    else -> return false
                }

                // The third tag is either a region (if 2nd was a script), else a variant code.
                if (subTags[2].length >= 4) {
                    variant = subTags[2]
                } else {
                    region = subTags[2]
                }
            }
            4 -> {
                language = subTags[0]
                script = subTags[1]
                region = subTags[2]
                variant = subTags[3]
            }
            else -> return false
        }
        return true
    }
}
