/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.app

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import com.blankj.utilcode.util.ThrowableUtils.getFullStackTrace
import com.google.android.material.color.DynamicColors
import com.itsaky.androidide.BuildConfig
import com.itsaky.androidide.activities.CrashHandlerActivity
import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider
import com.itsaky.androidide.events.AppEventsIndex
import com.itsaky.androidide.events.LspApiEventsIndex
import com.itsaky.androidide.events.LspJavaEventsIndex
import com.itsaky.androidide.events.ProjectsApiEventsIndex
import com.itsaky.androidide.preferences.internal.enableMaterialYou
import com.itsaky.androidide.preferences.internal.uiMode
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.VMUtils
import com.itsaky.toaster.Toaster.Type.ERROR
import com.itsaky.toaster.toast
import org.greenrobot.eventbus.EventBus
import java.lang.Thread.UncaughtExceptionHandler
import kotlin.system.exitProcess

class IDEApplication : BaseApplication() {

  private var uncaughtExceptionHandler: UncaughtExceptionHandler? = null

  init {
    if (!VMUtils.isJvm()) {
      System.loadLibrary("android-tree-sitter")
      System.loadLibrary("tree-sitter-java")
      System.loadLibrary("tree-sitter-xml")
    }
  }

  override fun onCreate() {
    instance = this
    uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    Thread.setDefaultUncaughtExceptionHandler { thread, th -> handleCrash(thread, th) }
    super.onCreate()

    EventBus.builder()
      .addIndex(AppEventsIndex())
      .addIndex(ProjectsApiEventsIndex())
      .addIndex(LspApiEventsIndex())
      .addIndex(LspJavaEventsIndex())
      .installDefaultEventBus()
  
    AppCompatDelegate.setDefaultNightMode(uiMode)
    
    if (enableMaterialYou) {
      DynamicColors.applyToActivitiesIfAvailable(this)
    }

    executeAsync { IDEColorSchemeProvider.init() }
  }

  private fun handleCrash(thread: Thread, th: Throwable) {
    writeException(th)

    try {

      val intent = Intent()
      intent.action = CrashHandlerActivity.REPORT_ACTION
      intent.putExtra(CrashHandlerActivity.TRACE_KEY, getFullStackTrace(th))
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      startActivity(intent)
      if (uncaughtExceptionHandler != null) {
        uncaughtExceptionHandler!!.uncaughtException(thread, th)
      }

      exitProcess(1)
    } catch (error: Throwable) {
      LOG.error("Unable to show crash handler activity", error)
    }
  }

  fun showChangelog() {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(GITHUB_URL + "/releases/tag/v" + BuildConfig.VERSION_NAME)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
      startActivity(intent)
    } catch (th: Throwable) {
      LOG.error("Unable to start activity to show changelog", th)
      toast("Unable to start activity", ERROR)
    }
  }

  companion object {

    private val LOG = ILogger.newInstance("IDEApplication")

    @JvmStatic
    lateinit var instance: IDEApplication
      private set
  }
}
