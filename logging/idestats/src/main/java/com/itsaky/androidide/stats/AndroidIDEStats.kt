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

package com.itsaky.androidide.stats

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import com.blankj.utilcode.util.DeviceUtils
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.app.configuration.IDEBuildConfigProvider
import com.itsaky.androidide.buildinfo.BuildInfo
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Locale


/**
 * Anonymous statistics for AndroidIDE.
 *
 * @author Akash Yadav
 */
object AndroidIDEStats {

  val uniqueDeviceId by lazy {
    digest(DeviceUtils.getUniqueDeviceId(BaseApplication.getBaseInstance().packageName))
  }

  val deviceModel by lazy {
    DeviceUtils.getModel()
  }

  val androidVersion by lazy {
    Build.VERSION.SDK_INT
  }

  val appVersion by lazy {
    BuildInfo.VERSION_NAME_SIMPLE
  }

  val country by lazy {
    val manager = BaseApplication.getBaseInstance()
      .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    manager.simCountryIso?.uppercase(Locale.getDefault())
      .let { country -> if (country.isNullOrBlank()) "<unknown>" else country }
  }

  val cpuArch by lazy {
    IDEBuildConfigProvider.getInstance().cpuAbiName
  }

  val statData by lazy {
    StatData(uniqueDeviceId, deviceModel, country, androidVersion, appVersion, cpuArch)
  }

  private fun digest(input: String): String {
    return try {
      val md = MessageDigest.getInstance("SHA-256")
      BigInteger(1, md.digest(input.toByteArray())).toString(16).uppercase(Locale.getDefault())
    } catch (e: Exception) {
      input
    }
  }
}