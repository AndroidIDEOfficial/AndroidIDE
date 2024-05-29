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

/**
 * @author Akash Yadav
 */
@Suppress("MemberVisibilityCanBePrivate")
object StatPreferences {

  const val STAT_COLLECTION_CONSENT_SHOWN = "ide.stats.consentShown"
  const val STAT_OPT_IN = "ide.stats.optIn"
  const val STAT_LAST_REPORTED = "ide.stats.lastReported"

  var statConsentDialogShown: Boolean
    get() = prefManager.getBoolean(STAT_COLLECTION_CONSENT_SHOWN, false)
    set(value) {
      prefManager.putBoolean(STAT_COLLECTION_CONSENT_SHOWN, value)
    }

  var statOptIn: Boolean
    get() = prefManager.getBoolean(STAT_OPT_IN, true)
    set(value) {
      prefManager.putBoolean(STAT_OPT_IN, value)
    }

  var statLastReported: Long
    get() = prefManager.getLong(STAT_LAST_REPORTED, 0L)
    set(value) {
      prefManager.putLong(STAT_LAST_REPORTED, value)
    }
}