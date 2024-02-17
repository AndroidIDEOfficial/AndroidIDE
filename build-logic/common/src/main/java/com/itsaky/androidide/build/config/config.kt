package com.itsaky.androidide.build.config/*
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

import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

const val KEY_ALIAS = "IDE_SIGNING_ALIAS"
const val AUTH_PASS = "IDE_SIGNING_AUTH_PASS"
const val AUTH_USER = "IDE_SIGNING_AUTH_USER"
const val KEY_PASS = "IDE_SIGNING_KEY_PASS"
const val KEY_STORE_PASS = "IDE_SIGNING_STORE_PASS"
const val KEY_URL = "IDE_SIGNING_URL"
const val KEY_BIN = "IDE_SIGNING_KEY_BIN"

/**
 * The minimum Android Gradle Plugin version which is supported by AndroidIDE.
 */
const val AGP_VERSION_MINIMUM = "7.2.0"

val Project.signingKey: Provider<RegularFile>
  get() = rootProject.layout.buildDirectory.file("signing/signing-key.jks")