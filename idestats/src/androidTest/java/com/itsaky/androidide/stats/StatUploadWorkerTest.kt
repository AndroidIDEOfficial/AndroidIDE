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

import android.app.Application
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.buildinfo.BuildInfo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * @author Akash Yadav
 */
@RunWith(AndroidJUnit4::class)
class StatUploadWorkerTest {

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Application>()
    val config = Configuration.Builder()
      .setMinimumLoggingLevel(Log.DEBUG)
      .setExecutor(SynchronousExecutor())
      .build()

    // Initialize WorkManager for instrumentation tests.
    WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
  }

  @Test
  @Throws(Exception::class)
  fun testStatUploadWorker() {
    val context = ApplicationProvider.getApplicationContext<Application>()
    val input = StatData("test_device_id", "test_device_name", "test_device_country", 33,
      BuildInfo.VERSION_NAME_SIMPLE, "aarch64")
    val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    val request = PeriodicWorkRequestBuilder<StatUploadWorker>(24, TimeUnit.HOURS)
      .setInputData(input.toInputData())
      .setConstraints(constraints)
      .build()
    val workManager = WorkManager.getInstance(context)
    val testDriver = WorkManagerTestInitHelper.getTestDriver(context)!!

    workManager.enqueueUniquePeriodicWork(StatUploadWorker.WORKER_WORK_NAME,
      ExistingPeriodicWorkPolicy.KEEP, request).result.get()
    testDriver.setAllConstraintsMet(request.id)
    testDriver.setPeriodDelayMet(request.id)

    val workInfo = workManager.getWorkInfoById(request.id).get()
    assertThat(workInfo.state).isEqualTo(WorkInfo.State.ENQUEUED)
  }
}