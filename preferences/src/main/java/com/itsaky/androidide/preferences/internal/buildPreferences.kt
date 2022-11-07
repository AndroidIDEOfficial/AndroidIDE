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

package com.itsaky.androidide.preferences.internal

// Defined in PreferenceManager so that it could be accessible from Environment class
import com.itsaky.androidide.managers.PreferenceManager.KEY_TP_FIX

const val STACKTRACE = "idepref_gradleCmd_stacktrace"
const val DEBUG = "idepref_gradleCmd_debug"
const val SCAN = "idepref_gradleCmd_scan"
const val INFO = "idepref_gradleCmd_info"
const val WARNING_MODE = "idepref_gradleCmd_warningMode"
const val BUILD_CACHE = "idepref_gradleCmd_buildCache"
const val OFFLINE_MODE = "idepref_gradleCmd_offlineMode"

const val GRADLE_COMMANDS = "idepref_build_gradleCommands"
const val GRADLE_CLEAR_CACHE = "idepref_build_gradleClearCache"
const val CUSTOM_GRADLE_INSTALLATION = "idepref_build_customGradleInstallation"
const val TP_FIX = KEY_TP_FIX

/** Switch for Gradle `--debug` option. */
var isDebugEnabled: Boolean
  get() = prefManager.getBoolean(DEBUG)
  set(enabled) {
    prefManager.putBoolean(DEBUG, enabled)
  }

/** Switch for Gradle `--scan` option. */
var isScanEnabled: Boolean
  get() = prefManager.getBoolean(SCAN)
  set(enabled) {
    prefManager.putBoolean(SCAN, enabled)
  }

/** Switch for Gradle `--warning-mode all` option. */
var isWarningModeAllEnabled: Boolean
  get() = prefManager.getBoolean(WARNING_MODE)
  set(enabled) {
    prefManager.putBoolean(WARNING_MODE, enabled)
  }

/** Switch for Gradle `--build-cache` option. */
var isBuildCacheEnabled: Boolean
  get() = prefManager.getBoolean(BUILD_CACHE)
  set(enabled) {
    prefManager.putBoolean(BUILD_CACHE, enabled)
  }

/** Switch for Gradle `--offline` option. */
var isOfflineEnabled: Boolean
  get() = prefManager.getBoolean(OFFLINE_MODE)
  set(enabled) {
    prefManager.putBoolean(OFFLINE_MODE, enabled)
  }

/** Switch for Gradle `--stacktrace` option. */
var isStacktraceEnabled: Boolean
  get() = prefManager.getBoolean(STACKTRACE)
  set(value) {
    prefManager.putBoolean(STACKTRACE, value)
  }

/** Switch for Gradle `--info` option. */
var isInfoEnabled: Boolean
  get() = prefManager.getBoolean(INFO, isFirstBuild)
  set(enabled) {
    prefManager.putBoolean(INFO, enabled)
  }

/** Custom Gradle installation directory path. */
var gradleInstallationDir: String
  get() = prefManager.getString(CUSTOM_GRADLE_INSTALLATION, "")
  set(value) {
    prefManager.putString(CUSTOM_GRADLE_INSTALLATION, value)
  }

/**
 * Whether the tag pointer fix should be enabled or not. The fix is needed only if JDK 11 is being
 * used.
 */
var tpFix: Boolean
  get() = prefManager.getBoolean(TP_FIX, false)
  set(value) {
    prefManager.putBoolean(TP_FIX, value)
  }
