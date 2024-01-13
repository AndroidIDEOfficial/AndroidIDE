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

package com.itsaky.androidide.activities

import android.widget.Button
import androidx.annotation.StringRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import com.adevinta.android.barista.rule.cleardata.ClearDatabaseRule
import com.adevinta.android.barista.rule.cleardata.ClearFilesRule
import com.adevinta.android.barista.rule.cleardata.ClearPreferencesRule
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.R
import com.itsaky.androidide.buildinfo.BuildInfo
import com.itsaky.androidide.testing.android.clickAndWaitForNewWindow
import com.itsaky.androidide.testing.android.clickAndWaitForWindowUpdate
import com.itsaky.androidide.testing.android.getObjectWithText
import com.itsaky.androidide.testing.android.launchAndroidIDE
import com.itsaky.androidide.utils.isAtLeastR
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * @author Akash Yadav
 */
@Suppress("TestFunctionName")
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class OnboardingActivityPermissionsBehaviorTest {

  /**
   * Clear shared preferences after tests.
   */
  @JvmField
  @Rule
  val clearPrefsRule = ClearPreferencesRule()

  /**
   * Clear databases after tests.
   */
  @JvmField
  @Rule
  val clearDBsRule = ClearDatabaseRule()

  /**
   * Delete private data files after each test.
   */
  @JvmField
  @Rule
  val clearFilesRule = ClearFilesRule()

  @Test
  fun A_testOnboarding_permissionScreen_storagePermissionBehavior() {
    checkPermissionsBehavior(R.string.permission_desc_storage)
  }

  @Test
  fun B_testOnboarding_permissionScreen_installPackagesPermissionBehavior() {
    checkPermissionsBehavior(R.string.permission_desc_install_packages)
  }

  private fun checkPermissionsBehavior(@StringRes permissionDesc: Int) {
    val device = launchAndroidIDE(fromLauncher = true, clearTasks = true)

    device.nextButton().clickAndWaitForWindowUpdate(device)
    device.nextButton().clickAndWaitForWindowUpdate(device)

    var grantButton = device.getObjectWithText(permissionDesc)
      .parent!!
      .parent!!
      .findObject(By.clazz(Button::class.java))

    // verify that clicking the 'Grant' button next to storage navigates requests the permissions
    grantButton.clickAndWaitForNewWindow()

    // check permission denied behavior
    if (permissionDesc == R.string.permission_desc_install_packages || isAtLeastR()) {
      device.handlePermissionInSettings(false)
    } else {
      device.getObjectWithText("DENY").clickAndWaitForWindowUpdate(device)
    }

    grantButton = device.getObjectWithText(permissionDesc)
      .parent!!
      .parent!!
      .findObject(By.clazz(Button::class.java))

    // verify button state after permission was not granted
    assertThat(grantButton).isNotNull()
    assertThat(grantButton.text).isNotNull()

    // navigate to the permission page again
    grantButton.clickAndWaitForNewWindow()

    // check permission granted behavior
    if (permissionDesc == R.string.permission_desc_install_packages || isAtLeastR()) {
      device.handlePermissionInSettings(true)
    } else {
      device.getObjectWithText("ALLOW").clickAndWaitForWindowUpdate(device)
    }

    grantButton = device.getObjectWithText(permissionDesc)
      .parent!!
      .parent!!
      .findObject(By.clazz(Button::class.java))

    // verify AndroidIDE is visible
    assertThat(device.currentPackageName).isEqualTo(BuildInfo.PACKAGE_NAME)

    // verify button state after permission was granted
    assertThat(grantButton).isNotNull()
    assertThat(grantButton.text).isNull()
  }
}