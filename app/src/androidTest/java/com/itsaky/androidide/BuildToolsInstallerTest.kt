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

package com.itsaky.androidide

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.R.string
import com.itsaky.androidide.shell.executeProcessAsync
import com.itsaky.androidide.testing.android.clickAndWaitForWindowUpdate
import com.itsaky.androidide.testing.android.findObjectWithText
import com.itsaky.androidide.testing.android.hasObjectWithText
import com.itsaky.androidide.testing.android.launchAndroidIDE
import com.itsaky.androidide.utils.Environment
import org.junit.Test
import org.junit.runner.RunWith

/**
 * A special test case which is used to install the build tools on a test device. This is used to
 * setup the test device before running further tests.
 *
 * This class should not define any other tests.
 *
 * @author Akash Yadav
 */
@RunWith(AndroidJUnit4::class)
class BuildToolsInstallerTest {

  companion object {
    private const val TAG = "BuildToolsInstallerTest"
  }

  @Test
  fun setupDevice() {
    if (!System.getenv("IDE_INSTRUMENTATION_SETUP_BUILDTOOLS").toBoolean()) {
      return
    }

    val device = launchAndroidIDE()

    // Allow permissions
    while (true) {
      val obj = device.findObjectWithText("Allow")
      obj?.clickAndWaitForWindowUpdate(device) ?: break
    }

    // Dismiss the 'AndroidIDE statistics' dialog
    if (device.hasObjectWithText(string.title_androidide_statistics)
      && device.hasObjectWithText(string.msg_androidide_statistics)
    ) {
      device.findObjectWithText(string.btn_opt_in).clickAndWaitForWindowUpdate(device)
    }

    // The 'Warning' dialog will be shown, no need to dismiss it.

    // Start the installation process
    execProcAndLogOutput("pkg upd")
    execProcAndLogOutput("yes | pkg upg") // do not use -y
    execProcAndLogOutput("yes | idesetup -c")

    // Check if the tools were installed
    assertThat(Environment.JAVA.exists()).isTrue()
    assertThat(Environment.ANDROID_HOME.exists()).isTrue()

    Thread.sleep(5000L)
  }

  private fun execProcAndLogOutput(cmd: String) {
    val process = executeProcessAsync {
      redirectErrorStream = true
      workingDirectory = null
      environment = Environment.getEnvironment()
      command = listOf(Environment.SHELL.absolutePath, "-c", cmd).also {
        Log.d(TAG, "setupDevice: Full command : ${it.joinToString(separator = " ")}")
      }
    }

    logOutput(cmd, process)

    process.waitFor()
  }

  private fun logOutput(cmd: String, process: Process) {
    process.inputStream.bufferedReader().forEachLine {
      Log.d(TAG, "setupDevice: '${cmd}': $it")
    }
  }
}