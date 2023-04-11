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

package com.itsaky.androidide.logsender

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Process
import com.itsaky.androidide.logsender.utils.LogReader
import com.itsaky.androidide.logsender.utils.error
import com.itsaky.androidide.logsender.utils.info
import com.itsaky.androidide.logsender.utils.warn
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Entry point for LogSender.
 *
 * @author Akash Yadav
 */
object LogSender : ILogSender.Stub(), ServiceConnection {

  private var installed = AtomicBoolean(false)
  private var reader: LogReader? = null
  private var receiver: ILogReceiver? = null
  private var packageName: String? = null

  /** Action for the LogSender service. */
  const val SERVICE_ACTION = "com.itsaky.androidide.LOG_SERVICE_ACTION"

  /** Constant used to indicate that the package name of the application cannot be determined. */
  const val PACKAGE_UNKNOWN = "<unknown-package-name>"

  /** AndroidIDE's package name. */
  private const val PACKAGE_ANDROIDIDE = "com.itsaky.androidide"

  fun install(app: Application) {
    check(!installed.getAndSet(true)) { "LogSender has already been installed" }
    this.packageName = app.packageName

    if (PACKAGE_ANDROIDIDE == this.packageName) {
      return
    }

    Intent(SERVICE_ACTION).setPackage(PACKAGE_ANDROIDIDE).let {
      if (app.bindService(it, this, 0)) {
        info("Binding to AndroidIDE's LogReceiver service")
      } else {
        error("Failed to bind to log receiver service")
      }
    }

    installed.set(true)
  }

  override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
    this.receiver = ILogReceiver.Stub.asInterface(service)
    checkNotNull(this.receiver) { "Failed to get receiver instance" }

    this.receiver!!.connect(this)
    info("Connected to AndroidIDE's LogRecevier")
  }

  override fun onServiceDisconnected(name: ComponentName?) {
    info("Disconnected from AndroidIDE's LogRecevier")
    this.installed.set(false)
    this.reader?.interrupt()
    this.receiver = null
    this.reader = null
  }

  override fun startReader() {
    if (reader?.isAlive == true) {
      warn("LogReader has already been started")
      return
    }

    reader = LogReader { receiver?.onLog(it) }
    reader?.start()
  }

  override fun getPid(): Int {
    return Process.myPid()
  }

  override fun getPackageName(): String {
    return packageName ?: PACKAGE_UNKNOWN
  }
}
