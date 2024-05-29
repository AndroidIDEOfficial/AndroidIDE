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

package com.itsaky.androidide.services.log

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Akash Yadav
 */
internal class LogSendersRegistry {

  private val senders = ConcurrentHashMap<String, CachingLogSender>()
  private val idToPck = ConcurrentHashMap<String, String>()

  val size: Int
    get() = this.senders.size

  fun getPendingSenders(): List<CachingLogSender> {
    return this.senders.mapNotNull { (_, sender) -> if (sender.isStarted) null else sender }
  }

  fun getByPackage(packageName: String): CachingLogSender? {
    return this.senders[packageName]
  }

  fun containsKey(packageOrId: String): Boolean {
    if (senders.containsKey(packageOrId)) {
      return true
    }

    return getById(packageOrId) != null
  }

  fun getById(senderId: String): CachingLogSender? {
    return this.idToPck[senderId]?.let(senders::get)
  }

  fun put(sender: CachingLogSender) {
    this.senders[sender.packageName] = sender
    this.idToPck[sender.id] = sender.packageName
  }

  fun remove(packageName: String) {
    this.senders.remove(packageName)

    val iterator = this.idToPck.iterator()
    for (entry in iterator) {
      if (entry.value == packageName) {
        iterator.remove()
      }
    }
  }

  fun clear() {
    this.senders.clear()
    this.idToPck.clear()
  }

  fun forEach(action: (CachingLogSender) -> Unit) {
    this.senders.forEachValue(1, action::invoke)
  }
}