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

package com.itsaky.androidide.utils

import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageInstaller
import android.content.pm.PackageInstaller.Session
import android.content.pm.PackageInstaller.SessionCallback
import com.itsaky.androidide.tasks.executeAsync
import java.io.File
import java.io.IOException

/**
 * Utility class for installing APKs.
 *
 * @author Akash Yadav
 */
object ApkInstaller {

  private val log = ILogger.newInstance("ApkInstaller")

  const val PROGRESS_UPDATE_DELAY = 100
  // ms

  /**
   * Starts a session-based package installation workflow.
   *
   * @param context The context.
   * @param sender The componenent which is requesting the installation. This component receives the
   * installation result.
   * @param apk The APK file to install.
   */
  @JvmStatic
  fun installApk(context: Context, sender: IntentSender, apk: File, callback: SessionCallback) {
    if (!apk.exists() || !apk.isFile || apk.extension != "apk") {
      log.error("File is not an APK:", apk)
      return
    }

    var session: Session? = null
    try {
      val installer =
        context.packageManager.packageInstaller.apply { registerSessionCallback(callback) }
      val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
      val sessionId = installer.createSession(params)
      session = installer.openSession(sessionId)
      executeAsync(
        callable = {
          addToSession(session, apk)
          session
        }
      ) {
        it?.let {
          it.commit(sender)
          log.info("Started package install session")
        }
      }
    } catch (io: IOException) {
      log.error("Package installation failed", io)
    } catch (runtime: RuntimeException) {
      session?.abandon()
      log.error("Package installation failed", runtime)
    }
  }

  private fun addToSession(session: Session, apk: File) {
    val length = apk.length()
    if (length == 0L) {
      throw RuntimeException("File is empty (has length 0)")
    }
    session.openWrite(apk.name, 0, length).use { outStream ->
      apk.inputStream().use { inStream ->
        val bytes = ByteArray(8 * 1024)
        var n: Int = inStream.read(bytes)
        var count = n
        while (n >= 0) {
          outStream.write(bytes, 0, n)
          n = inStream.read(bytes)
          count += n
        }
      }
      session.fsync(outStream)
    }
  }
}
