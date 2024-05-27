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

package com.itsaky.androidide.testing.common.utils

fun findAndroidHome(): String {
  var androidHome = System.getenv("ANDROID_HOME")
  if (androidHome != null && androidHome.isNotBlank()) {
    return androidHome
  }

  androidHome = System.getenv("ANDROID_SDK_ROOT")
  if (androidHome != null && androidHome.isNotBlank()) {
    return androidHome
  }

  val home = System.getProperty("user.home")
  return when {
    OperatingSystem.IS_UNIX -> "$home/Android/Sdk"
    OperatingSystem.IS_MAC -> "$home/Library/Android/sdk"
    OperatingSystem.IS_WINDOWS -> "$home\\AppData\\Local\\Android\\Sdk"
    else -> throw IllegalStateException("Unsupported operating system")
  }
}