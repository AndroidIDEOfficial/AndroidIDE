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

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageInstaller
import com.itsaky.androidide.services.InstallationResultReceiver
import org.slf4j.LoggerFactory

/**
 * Handles result of APK installation.
 *
 * @author Akash Yadav
 */
object InstallationResultHandler {

  private const val INSTALL_PACKAGE_REQ_CODE = 2304
  private const val INSTALL_PACKAGE_ACTION = "com.itsaky.androidide.installer.INSTALL_PACKAGE"

  private val log = LoggerFactory.getLogger(InstallationResultHandler::class.java)

  @JvmStatic
  fun createEditorActivitySender(context: Context): IntentSender {
    val intent = Intent(context, InstallationResultReceiver::class.java)
    intent.action = INSTALL_PACKAGE_ACTION
    return PendingIntent.getBroadcast(
      context,
      INSTALL_PACKAGE_REQ_CODE,
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT
    )
      .intentSender
  }

  @JvmStatic
  fun onResult(context: Context?, intent: Intent?): String? {
    if (context == null || intent == null || intent.action != INSTALL_PACKAGE_ACTION) {
      log.warn("Invalid broadcast received. action={}", intent?.action)
      return null
    }

    val extras =
      intent.extras
        ?: run {
          log.warn("Invalid intent received in broadcast")
          return null
        }

    val packageName = extras.getString(PackageInstaller.EXTRA_PACKAGE_NAME)
    val status = extras.getInt(PackageInstaller.EXTRA_STATUS)
    val message = extras.getString(PackageInstaller.EXTRA_STATUS_MESSAGE)
    return when (status) {
      PackageInstaller.STATUS_PENDING_USER_ACTION -> {
        @Suppress("DEPRECATION")
        extras.get(Intent.EXTRA_INTENT)?.let {
          if (it is Intent) {
            if ((it.flags and Intent.FLAG_ACTIVITY_NEW_TASK) != Intent.FLAG_ACTIVITY_NEW_TASK) {
              it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(it)
          }
        }
        null
      }

      PackageInstaller.STATUS_SUCCESS -> {
        log.info("Package installed successfully!")
        packageName
      }

      PackageInstaller.STATUS_FAILURE,
      PackageInstaller.STATUS_FAILURE_ABORTED,
      PackageInstaller.STATUS_FAILURE_BLOCKED,
      PackageInstaller.STATUS_FAILURE_CONFLICT,
      PackageInstaller.STATUS_FAILURE_INCOMPATIBLE,
      PackageInstaller.STATUS_FAILURE_INVALID,
      PackageInstaller.STATUS_FAILURE_STORAGE -> {
        log.error("Package installation failed with status code {} and message {}", status, message)
        null
      }

      else -> {
        log.warn("Invalid status code received in broadcast: {}", status)
        null
      }
    }
  }
}
