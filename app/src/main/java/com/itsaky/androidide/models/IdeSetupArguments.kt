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

package com.itsaky.androidide.models

/**
 * Configuration options for IDESetup
 *
 * @property argumentName The name of the argument.
 * @property requiresValue Whether the argument must have a value.
 * @author Akash Yadav
 */
enum class IdeSetupArgument(val argumentName: String, val requiresValue: Boolean = false) {
  INSTALL_DIR("--install-dir", true),
  WITH_GIT("--with-git"),
  ASSUME_YES("--assume-yes"),
  WITH_OPENSSH("--with-openssh"),
  SDK_VERSION("--sdk", true),
  JDK_VERSION("--jdk", true)
}
