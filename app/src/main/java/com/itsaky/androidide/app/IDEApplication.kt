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
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.Operation
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.blankj.utilcode.util.ThrowableUtils.getFullStackTrace
import com.google.android.material.color.DynamicColors
import com.itsaky.androidide.BuildConfig
import com.itsaky.androidide.activities.CrashHandlerActivity
import com.itsaky.androidide.activities.editor.IDELogcatReader
import com.itsaky.androidide.buildinfo.BuildInfo
import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider
import com.itsaky.androidide.eventbus.events.preferences.PreferenceChangeEvent
import com.itsaky.androidide.events.AppEventsIndex
import com.itsaky.androidide.events.EditorEventsIndex
import com.itsaky.androidide.events.LspApiEventsIndex
import com.itsaky.androidide.events.LspJavaEventsIndex
import com.itsaky.androidide.events.ProjectsApiEventsIndex
import com.itsaky.androidide.preferences.KEY_DEVOPTS_DEBUGGING_DUMPLOGS
import com.itsaky.androidide.preferences.dumpLogs
import com.itsaky.androidide.preferences.internal.STAT_OPT_IN
import com.itsaky.androidide.preferences.internal.statOptIn
import com.itsaky.androidide.preferences.internal.uiMode
import com.itsaky.androidide.stats.AndroidIDEStats
import com.itsaky.androidide.stats.StatUploadWorker
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.treesitter.TreeSitter
import com.itsaky.androidide.ui.themes.IDETheme
import com.itsaky.androidide.ui.themes.ThemeManager
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.RecyclableObjectPool
import com.itsaky.androidide.utils.VMUtils
import com.itsaky.androidide.utils.flashError
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Thread.UncaughtExceptionHandler
import java.time.Duration
import kotlin.system.exitProcess

class IDEApplication : BaseApplication() {

  private var uncaughtExceptionHandler: UncaughtExceptionHandler? = null
  private var ideLogcatReader: IDELogcatReader? = null
  private val log = ILogger.newInstance("IDEApplication")

  init {
    if (!VMUtils.isJvm()) {
      TreeSitter.loadLibrary()
    }

    RecyclableObjectPool.DEBUG = BuildConfig.DEBUG
  }

  override fun onCreate() {
    instance = this
    uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    Thread.setDefaultUncaughtExceptionHandler { thread, th -> handleCrash(thread, th) }
    super.onCreate()

    if (BuildConfig.DEBUG) {
      StrictMode.setVmPolicy(
        StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy()).penaltyLog().detectAll().build()
      )

      if (dumpLogs) {
        startLogcatReader()
      }
    }

    EventBus.builder()
      .addIndex(AppEventsIndex())
      .addIndex(EditorEventsIndex())
      .addIndex(ProjectsApiEventsIndex())
      .addIndex(LspApiEventsIndex())
      .addIndex(LspJavaEventsIndex())
      .installDefaultEventBus(true)

    EventBus.getDefault().register(this)

    AppCompatDelegate.setDefaultNightMode(uiMode)

    if (ThemeManager.getCurrentTheme() == IDETheme.MATERIAL_YOU) {
      DynamicColors.applyToActivitiesIfAvailable(this)
    }

    EditorColorScheme.setDefault(SchemeAndroidIDE.newInstance(null))

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
    var version = BuildInfo.VERSION_NAME_SIMPLE
    if (!version.startsWith('v')) {
      version = "v${version}"
    }
    intent.data = Uri.parse("${BuildInfo.REPO_URL}/releases/tag/${version}")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
      startActivity(intent)
    } catch (th: Throwable) {
      LOG.error("Unable to start activity to show changelog", th)
      flashError("Unable to start activity")
    }
  }

  fun reportStatsIfNecessary() {

    if (!statOptIn) {
      log.info("Stat collection is disabled.")
      return
    }

    val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    val request = PeriodicWorkRequestBuilder<StatUploadWorker>(Duration.ofHours(24))
      .setInputData(AndroidIDEStats.statData.toInputData())
      .setConstraints(constraints)
      .addTag(StatUploadWorker.WORKER_WORK_NAME)
      .build()

    val workManager = WorkManager.getInstance(this)

    log.info("reportStatsIfNecessary: Enqueuing StatUploadWorker...")
    val operation = workManager
      .enqueueUniquePeriodicWork(StatUploadWorker.WORKER_WORK_NAME,
        ExistingPeriodicWorkPolicy.UPDATE, request)

    operation.state.observeForever(object : Observer<Operation.State> {
      override fun onChanged(t: Operation.State?) {
        operation.state.removeObserver(this)
        log.debug("reportStatsIfNecessary: WorkManager enqueue result: $t")
      }
    })
  }

  private fun startLogcatReader() {
    if (ideLogcatReader != null) {
      // already started
      return
    }

    log.info("Starting logcat reader...")
    ideLogcatReader = IDELogcatReader().also { it.start() }
  }

  private fun stopLogcatReader() {
    log.info("Stopping logcat reader...")
    ideLogcatReader?.stop()
    ideLogcatReader = null
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onPrefChanged(event: PreferenceChangeEvent) {
    val enabled = event.value as? Boolean? ?: return
    if (event.key == STAT_OPT_IN) {
      if (enabled) {
        reportStatsIfNecessary()
      } else {
        cancelStatUploadWorker()
      }
    } else if (event.key == KEY_DEVOPTS_DEBUGGING_DUMPLOGS) {
      if (enabled) {
        startLogcatReader()
      } else {
        stopLogcatReader()
      }
    }
  }

  private fun cancelStatUploadWorker() {
    log.info("Opted-out of stat collection. Cancelling StatUploadWorker if enqueued...")
    val operation = WorkManager.getInstance(this)
      .cancelUniqueWork(StatUploadWorker.WORKER_WORK_NAME)
    operation.state.observeForever(object : Observer<Operation.State> {
      override fun onChanged(t: Operation.State?) {
        operation.state.removeObserver(this)
        log.info("StatUploadWorker: Cancellation result state: $t")
      }
    })
  }

  companion object {

    private val LOG = ILogger.newInstance("IDEApplication")

    @JvmStatic
    lateinit var instance: IDEApplication
      private set
  }
}
