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

package com.itsaky.androidide.xml.internal.widgets

import com.google.auto.service.AutoService
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.xml.widgets.WidgetTable
import com.itsaky.androidide.xml.widgets.WidgetTableRegistry
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation of the [WidgetTableRegistry].
 *
 * @author Akash Yadav
 */
@AutoService(WidgetTableRegistry::class)
class DefaultWidgetTableRegistry : WidgetTableRegistry {

  private val tables = ConcurrentHashMap<String, WidgetTable>()
  private val log = ILogger.newInstance(WidgetTableRegistry::class.java.simpleName)

  override var isLoggingEnabled: Boolean
    get() = log.isEnabled
    set(value) {
      log.isEnabled = value
    }

  override fun forPlatformDir(platform: File): WidgetTable? {
    var table = tables[platform.path]
    if (table != null) {
      return table
    }

    table = createTable(platform) ?: return null
    tables[platform.path] = table
    return table
  }

  private fun createTable(platformDir: File): WidgetTable? {
    val widgets = File(platformDir, "data/widgets.txt")
    if (!widgets.exists() || !widgets.isFile) {
      log.warn("'widgets.txt' file does not exist in ${platformDir.absolutePath}/data directory")
      return null
    }

    log.info("Creating widget table for platform dir: $platformDir")
    return widgets.inputStream().bufferedReader().useLines {
      val table = DefaultWidgetTable()
      it.forEach { line ->
        table.putWidget(line)
      }
      table
    }
  }

  override fun clear() {
    tables.clear()
  }
}
