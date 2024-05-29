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

package com.itsaky.androidide.eventbus.events.preferences

import com.itsaky.androidide.eventbus.events.Event

/**
 * Dispatched when a preference's is value changed in IDE preferences.
 *
 * @param key The key of the preference that was changed.
 * @param value The new value of the preference.
 * @author Akash Yadav
 */
data class PreferenceChangeEvent(val key: String, val value: Any?) : Event()

/**
 * Dispatched when a preference is removed from shared preferences.
 *
 * @param key The key of the preference that was removed.
 */
data class PreferenceRemoveEvent(val key: String) : Event()
