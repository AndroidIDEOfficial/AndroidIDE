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

import android.content.ComponentName
import android.os.Bundle
import android.os.IBinder
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import com.termux.app.TermuxActivity
import com.termux.shared.termux.TermuxConstants
import java.io.File

class TerminalActivity : TermuxActivity() {

  companion object {

    const val KEY_WORKING_DIRECTORY = TermuxConstants.TERMUX_APP.TERMUX_ACTIVITY.EXTRA_SESSION_WORKING_DIR
    const val KEY_SESSION_NAME = TermuxConstants.TERMUX_APP.TERMUX_ACTIVITY.EXTRA_SESSION_NAME

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

  override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
    super.onServiceConnected(componentName, service)
    writeSourcesList()
  }

  private fun writeSourcesList() {
    try {
      File(Environment.PREFIX, "etc/apt/sources.list").writeBytes(SOURCES_LIST_CONTENT)
    } catch (th: Throwable) {
      LOG.error("Unable to update sources.list", th)
    }
  }
}