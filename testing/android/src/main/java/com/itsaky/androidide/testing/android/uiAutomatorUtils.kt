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

package com.itsaky.androidide.testing.android

import android.app.Activity
import android.app.UiAutomation
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiDeviceAccessor
import androidx.test.uiautomator.Until
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.buildinfo.BuildInfo
import com.itsaky.androidide.utils.uncheckedCast

const val LAUNCH_TIMEOUT = 5000L

/**
 * Launches the AndroidIDE application using UI Automator.
 *
 * @param fromLauncher Whether the application must be launched from the Android launcher.
 * @param clearTasks Whether the application's previous tasks must be cleared.
 */
fun launchAndroidIDE(
  fromLauncher: Boolean = true,
  clearTasks: Boolean = true,
): UiDevice {
  val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

  if (fromLauncher) {
    device.pressHome()

    val launcherPackage = device.launcherPackageName
    assertThat(launcherPackage).isNotNull()

    device.wait(
      Until.hasObject(By.pkg(launcherPackage).depth(0)),
      LAUNCH_TIMEOUT
    )
  }

  // Launch the app
  val context = ApplicationProvider.getApplicationContext<Context>()
  val intent = context.packageManager.getLaunchIntentForPackage(BuildInfo.PACKAGE_NAME)
  assertThat(intent).isNotNull()

  if (clearTasks) {
    intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
  }

  context.startActivity(intent)

  // Wait for the app to appear
  device.wait(
    Until.hasObject(By.pkg(BuildInfo.PACKAGE_NAME).depth(0)),
    LAUNCH_TIMEOUT
  )

  return device
}

/**
 * Get the first activity in the given [stage].
 */
fun <T : Activity> getActivityInStage(stage: Stage): T? {
  var activity: T? = null
  InstrumentationRegistry.getInstrumentation().runOnMainSync {
    val activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(stage)
    if (activities.isNotEmpty()) {
      activity = uncheckedCast(activities.first())
    }
  }

  return activity
}

/**
 * @see UiDeviceAccessor.getUiAutomation
 */
val UiDevice.uiAutomation: UiAutomation
  get() = UiDeviceAccessor.getUiAutomation(this)