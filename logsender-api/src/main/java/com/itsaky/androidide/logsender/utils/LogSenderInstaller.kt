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

package com.itsaky.androidide.logsender.utils

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.itsaky.androidide.logsender.LogSender

/**
 * Content providers are loaded before the application class is created. [LogSenderInstaller] is
 * used to install [LogSender] on application start.
 *
 * [LogSenderInstaller] automatically sets up the LogSender code that runs in the main app process.
 *
 * @author Akash Yadav
 * @see <a
 *   href="https://github.com/square/leakcanary/blob/main/leakcanary-object-watcher-android/src/main/java/leakcanary/internal/MainProcessAppWatcherInstaller.kt">MainProcessAppWatcherInstaller</a>
 */
internal class LogSenderInstaller : ContentProvider() {

  override fun onCreate(): Boolean {
    val app = context!!.applicationContext as Application
    LogSender.install(app)
    return true
  }

  override fun query(
    uri: Uri,
    projection: Array<out String>?,
    selection: String?,
    selectionArgs: Array<out String>?,
    sortOrder: String?
  ): Cursor? {
    return null
  }

  override fun getType(uri: Uri): String? {
    return null
  }

  override fun insert(uri: Uri, values: ContentValues?): Uri? {
    return null
  }

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
    return 0
  }

  override fun update(
    uri: Uri,
    values: ContentValues?,
    selection: String?,
    selectionArgs: Array<out String>?
  ): Int {
    return 0
  }
}
