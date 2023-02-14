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

import org.gradle.api.Project
import java.io.File

val KEY_ALIAS = "IDE_SIGNING_ALIAS"
val AUTH_PASS = "IDE_SIGNING_AUTH_PASS"
val AUTH_USER = "IDE_SIGNING_AUTH_USER"
val KEY_PASS = "IDE_SIGNING_KEY_PASS"
val KEY_STORE_PASS = "IDE_SIGNING_STORE_PASS"
val KEY_URL = "IDE_SIGNING_URL"

val Project.signingKey: File
  get() = rootProject.file("signing-key.jks")