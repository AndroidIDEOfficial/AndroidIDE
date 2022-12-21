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

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import com.blankj.utilcode.util.ThrowableUtils
import com.itsaky.androidide.BuildConfig
import com.itsaky.androidide.activities.CrashHandlerActivity
import com.itsaky.androidide.events.AppEventsIndex
import com.itsaky.androidide.events.LspApiEventsIndex
import com.itsaky.androidide.events.LspJavaEventsIndex
import com.itsaky.androidide.events.ProjectsApiEventsIndex
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.services.GradleBuildService
import com.itsaky.androidide.services.GradleBuildService.GradleServiceBinder
import com.itsaky.androidide.utils.ILogger
import com.itsaky.toaster.Toaster.Type.ERROR
import com.itsaky.toaster.toast
import java.lang.Thread.UncaughtExceptionHandler
import kotlin.system.exitProcess
import org.greenrobot.eventbus.EventBus

class IDEApplication : BaseApplication() {

  private var uncaughtExceptionHandler: UncaughtExceptionHandler? = null
  private val log = ILogger.newInstance("IDEApplication")

  private var onGradleBuildServiceConnected: ((GradleBuildService) -> Unit)? = null

  private val mGradleServiceConnection: ServiceConnection =
    object : ServiceConnection {
      override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val buildService = (service as GradleServiceBinder).service
        Lookup.DEFAULT.update(BuildService.KEY_BUILD_SERVICE, buildService)
        onGradleBuildServiceConnected?.invoke(buildService)
      }
      
      override fun onServiceDisconnected(name: ComponentName?) {
        log.info("Disconnected from Gradle build service")
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
  }

  fun bindGradleBuildService(onBound: (GradleBuildService) -> Unit): Boolean {
    this.onGradleBuildServiceConnected = onBound
    return bindService(
      Intent(this, GradleBuildService::class.java),
      mGradleServiceConnection,
      BIND_AUTO_CREATE or BIND_IMPORTANT
    )
  }

  fun unbindGradleBuildService() {
    this.onGradleBuildServiceConnected = null
    try {
      unbindService(mGradleServiceConnection)
    } catch (err: Throwable) {
      log.error("Unable to unbind Gradle build service")
    }
  }

  private fun handleCrash(thread: Thread, th: Throwable) {
    writeException(th)

    try {

      val intent = Intent()
      intent.action = CrashHandlerActivity.REPORT_ACTION
      intent.putExtra(CrashHandlerActivity.TRACE_KEY, ThrowableUtils.getFullStackTrace(th))
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
