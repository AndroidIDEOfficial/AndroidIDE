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
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Worker to upload stats to the server.
 *
 * @author Akash Yadav
 */
class StatUploadWorker(context: Context, workerParams: WorkerParameters) : Worker(context,
  workerParams) {

  companion object {

    const val STAT_UPLOAD_BASE_URL = "https://androidide.com"

    const val WORKER_WORK_NAME = "ide.stats.uploadWorker"
    const val KEY_DEVICE_ID = "device_hash"
    const val KEY_DEVICE_NAME = "device_name"
    const val KEY_DEVICE_COUNTRY = "device_country"
    const val KEY_ANDROID_VERSION = "android_version"
    const val KEY_APP_VERSION = "app_version"
    const val KEY_APP_CPU_ARCH = "app_cpu_arch"

    private val log = LoggerFactory.getLogger(StatUploadWorker::class.java)
  }

  override fun doWork(): Result {
    val data = StatData.fromInputData(inputData = inputData)
    log.debug("Uploading stats: {}", data)

    val retrofit = Retrofit.Builder().baseUrl(STAT_UPLOAD_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(StatUploadService::class.java)

    val response = try {
      service.uploadStats(data).execute()
    } catch (err: Exception) {
      log.error("Failed to upload stats to server", err)
      return Result.retry()
    }

    val body = response.body()
    if (!response.isSuccessful || body == null) {
      log.error(
        "Stat upload failed: responseCode: {}, responseBody: {}, errBody: {}", response.code(),
        body, response.errorBody()?.string() ?: "(empty)")

      // try again next time
      return Result.failure()
    }

    if (body.status != 200) {
      log.error("Stat upload failed: response: {}", body)
      return Result.failure()
    }

    log.info("Stat upload successful: {}", body)
    return Result.success()
  }
}