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

package com.itsaky.androidide.db.utils

import io.realm.RealmDictionary

/**
 * Check if two RealmDictionaries are equal.
 */
fun <T> RealmDictionary<out T>?.isEqualTo(other: Any?): Boolean {
  // implementation taken from java/util/AbstractMap from JDK 21
  if (other == this) return true
  if (other == null || this == null) return false
  if (other !is RealmDictionary<*>) return false
  if (this.size != other.size) return false

  try {
    for (entry in this.entries) {
      val key = entry.key
      val value = entry.value
      if (value == null) {
        if (!(other[key] == null && other.containsKey(key))) {
          return false
        }
      } else {
        if (value != other[key]) {
          return false
        }
      }
    }
  } catch (e: ClassCastException) {
    return false
  } catch (e: NullPointerException) {
    return false
  }

  return true
}

/**
 * Generate the hash code for a RealmDictionary.
 */
fun <T> RealmDictionary<T>.hash(): Int {
  return this.entries.sumOf { it.hashCode() }
}