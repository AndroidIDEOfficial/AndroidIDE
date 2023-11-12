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
package com.itsaky.androidide.activities

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import com.termux.app.TermuxActivity
import java.io.File

class TerminalActivity : TermuxActivity() {

  companion object {

    const val KEY_WORKING_DIRECTORY = "terminal_workingDirectory"
    private val LOG = ILogger.newInstance("TerminalActivity")
    private val SOURCES_LIST_CONTENT = "deb https://packages.androidide.com/apt/termux-main/ stable main".toByteArray()
  }

  override val navigationBarColor: Int
    get() = ContextCompat.getColor(this, android.R.color.black)
  override val statusBarColor: Int
    get() = ContextCompat.getColor(this, android.R.color.black)

  override fun onCreate(savedInstanceState: Bundle?) {
    val controller = WindowCompat.getInsetsController(
      window, window.decorView)
    controller.isAppearanceLightNavigationBars = false
    controller.isAppearanceLightStatusBars = false
    super.onCreate(savedInstanceState)
  }

  private val workingDirectory: String
    get() {
      val extras = intent.extras
      if (extras != null && extras.containsKey(KEY_WORKING_DIRECTORY)) {
        var directory = extras.getString(KEY_WORKING_DIRECTORY, null)
        if (directory.isNullOrBlank()) {
          directory = Environment.HOME.absolutePath
        }
        return directory
      }
      return Environment.HOME.absolutePath
    }

  private fun writeSourcesList() {
    try {
      File(Environment.PREFIX, "etc/apt/sources.list").writeBytes(SOURCES_LIST_CONTENT)
    } catch (th: Throwable) {
      LOG.error("Unable to update sources.list", th)
    }
  }
}