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

import android.os.Build
import androidx.work.Data
import androidx.work.workDataOf
import com.google.gson.annotations.SerializedName
import com.itsaky.androidide.stats.StatUploadWorker.Companion.KEY_ANDROID_VERSION
import com.itsaky.androidide.stats.StatUploadWorker.Companion.KEY_APP_CPU_ARCH
import com.itsaky.androidide.stats.StatUploadWorker.Companion.KEY_APP_VERSION
import com.itsaky.androidide.stats.StatUploadWorker.Companion.KEY_DEVICE_COUNTRY
import com.itsaky.androidide.stats.StatUploadWorker.Companion.KEY_DEVICE_ID
import com.itsaky.androidide.stats.StatUploadWorker.Companion.KEY_DEVICE_NAME

/**
 * Data that is uploaded to the server.
 *
 * @author Akash Yadav
 */
data class StatData(
  @SerializedName(KEY_DEVICE_ID)
  val deviceId: String,

  @SerializedName(KEY_DEVICE_NAME)
  val deviceName: String,

  @SerializedName(KEY_DEVICE_COUNTRY)
  val deviceCountry: String,

  @SerializedName(KEY_ANDROID_VERSION)
  val androidVersion: Int,

  @SerializedName(KEY_APP_VERSION)
  val appVersion: String,

  @SerializedName(KEY_APP_CPU_ARCH)
  val cpuArch: String
) {

  companion object {

    /**
     * Creates the [StatData] from the given [input data][Data].
     *
     * @param inputData The input data.
     */
    @JvmStatic
    fun fromInputData(inputData: Data): StatData {
      val deviceId = inputData.getString(KEY_DEVICE_ID)
      val deviceName = inputData.getString(KEY_DEVICE_NAME)
      val deviceCountry = inputData.getString(KEY_DEVICE_COUNTRY)
      val androidVersion = inputData.getInt(KEY_ANDROID_VERSION, 0)
      val appVersion = inputData.getString(KEY_APP_VERSION)
      val cpuArch = inputData.getString(KEY_APP_CPU_ARCH)

      if (deviceId == null
        || deviceName == null
        || deviceCountry == null
        || androidVersion < Build.VERSION_CODES.O
        || appVersion == null
        || cpuArch == null
      ) {
        throw IllegalArgumentException(
          "Work data does not contain required fields or has invalid field values")
      }

      return StatData(deviceId, deviceName, deviceCountry, androidVersion, appVersion, cpuArch)
    }
  }

  /**
   * Get the stat data as [input data][Data].
   */
  fun toInputData(): Data {
    return workDataOf(
      KEY_DEVICE_ID to deviceId,
      KEY_DEVICE_NAME to deviceName,
      KEY_DEVICE_COUNTRY to deviceCountry,
      KEY_ANDROID_VERSION to androidVersion,
      KEY_APP_VERSION to appVersion,
      KEY_APP_CPU_ARCH to cpuArch
    )
  }
}
