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
 * along with AndroidIDE.  If not, see <https:></https:>//www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.app

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.multidex.MultiDexApplication
import com.blankj.utilcode.util.ThrowableUtils
import com.google.android.material.color.DynamicColors
import com.itsaky.androidide.managers.PreferenceManager
import com.itsaky.androidide.managers.ToolsManager
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.shell.ShellServer
import com.itsaky.androidide.shell.ShellServer.Callback
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.FileUtil
import com.itsaky.androidide.utils.JavaCharacter
import com.itsaky.toaster.Toaster.Type.ERROR
import com.itsaky.toaster.init
import com.itsaky.toaster.toast
import java.io.File

abstract class BaseApplication : MultiDexApplication() {
  lateinit var prefManager: PreferenceManager
    private set

  override fun onCreate() {
    baseInstance = this
    Environment.init()
    init()
    super.onCreate()
    prefManager = PreferenceManager(this)
    JavaCharacter.initMap()
    ToolsManager.init(this, null)
    DynamicColors.applyToActivitiesIfAvailable(this)
    createNotificationChannels()
  }

  private fun createNotificationChannels() {
    val buildNotificationChannel =
      NotificationChannel(
        NOTIFICATION_GRADLE_BUILD_SERVICE,
        getString(string.title_gradle_service_notification_channel),
        NotificationManager.IMPORTANCE_LOW
      )
    NotificationManagerCompat.from(this).createNotificationChannel(buildNotificationChannel)
  }

  @JvmOverloads
  fun newShell(callback: Callback?, redirectErrors: Boolean = true): ShellServer {
    val shellServer =
      ShellServer(
        callback,
        "sh",
        Environment.mkdirIfNotExits(rootDir).absolutePath,
        Environment.getEnvironment(),
        redirectErrors
      )
    shellServer.start()
    return shellServer
  }

  val rootDir: File
    get() = File(iDEDataDir, "home")

  @get:SuppressLint("SdCardPath")
  val iDEDataDir: File
    get() = Environment.mkdirIfNotExits(File("/data/data/com.itsaky.androidide/files"))

  fun writeException(th: Throwable?) {
    FileUtil.writeFile(
      File(FileUtil.getExternalStorageDir(), "idelog.txt").absolutePath,
      ThrowableUtils.getFullStackTrace(th)
    )
  }

  val tempProjectDir: File
    get() = Environment.mkdirIfNotExits(File(Environment.TMP_DIR, "tempProject"))

  val projectsDir: File
    get() = Environment.PROJECTS_DIR

  fun openTelegramGroup() {
    openTelegram(TELEGRAM_GROUP_URL)
  }

  fun openTelegramChannel() {
    openTelegram(TELEGRAM_CHANNEL_URL)
  }

  fun openGitHub() {
    openUrl(GITHUB_URL)
  }

  fun openWebsite() {
    openUrl(WEBSITE)
  }

  fun openSponsors() {
    openUrl(SPONSOR_URL)
  }

  fun openDocs() {
    openUrl(DOCS_URL)
  }

  fun emailUs() {
    openUrl("mailto:$EMAIL")
  }

  private fun openTelegram(url: String?) {
    openUrl(url, "org.telegram.messenger")
  }

  @JvmOverloads
  fun openUrl(url: String?, pkg: String? = null) {
    try {
      val open = Intent()
      open.action = Intent.ACTION_VIEW
      open.data = Uri.parse(url)
      open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      if (pkg != null) {
        open.setPackage(pkg)
      }
      startActivity(open)
    } catch (th: Throwable) {
      if (pkg != null) {
        openUrl(url)
      } else {
        toast(th.message, ERROR)
      }
    }
  }

  companion object {
    const val NOTIFICATION_GRADLE_BUILD_SERVICE = "17571"
    const val TELEGRAM_GROUP_URL = "https://t.me/androidide_discussions"
    const val TELEGRAM_CHANNEL_URL = "https://t.me/AndroidIDEOfficial"
    const val GITHUB_URL = "https://github.com/AndroidIDEOfficial/AndroidIDE"
    const val WEBSITE = "https://androidide.com"
    const val SPONSOR_URL = "https://androidide.com/donate.php"

    // TODO Replace when available on website
    const val DOCS_URL = "https://github.com/AndroidIDEOfficial/AndroidIDE/tree/main/docs"
    const val EMAIL = "contact@androidide.com"

    private lateinit var baseInstance: BaseApplication
      private set

    @JvmStatic fun getBaseInstance() = baseInstance

    @JvmStatic
    val isAbiSupported: Boolean
      get() = isAarch64 || isArmv7a
    
    @JvmStatic
    val isAarch64: Boolean
      get() = Build.SUPPORTED_ABIS.contains("arm64-v8a")
    val isArmv7a: Boolean
      get() = Build.SUPPORTED_ABIS.contains("armeabi-v7a")

    @JvmStatic
    val arch: String?
      get() {
        if (isAarch64) {
          return "arm64-v8a"
        } else if (isArmv7a) {
          return "armeabi-v7a"
        }
        return null
      }
  }
}
