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

import android.Manifest
import android.content.Context
import android.os.Process
import android.widget.Button
import android.widget.GridLayout
import android.widget.Switch
import androidx.annotation.StringRes
import androidx.core.os.UserHandleCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import androidx.viewpager.widget.ViewPager
import com.adevinta.android.barista.rule.cleardata.ClearDatabaseRule
import com.adevinta.android.barista.rule.cleardata.ClearFilesRule
import com.adevinta.android.barista.rule.cleardata.ClearPreferencesRule
import com.adevinta.android.barista.rule.cleardata.internal.FileOperations
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.R
import com.itsaky.androidide.buildinfo.BuildInfo
import com.itsaky.androidide.testing.android.clickAndWaitForNewWindow
import com.itsaky.androidide.testing.android.clickAndWaitForWindowUpdate
import com.itsaky.androidide.testing.android.findObjectWithText
import com.itsaky.androidide.testing.android.getApplicationContext
import com.itsaky.androidide.testing.android.getObjectWithText
import com.itsaky.androidide.testing.android.hasObjectWithText
import com.itsaky.androidide.testing.android.launchAndroidIDE
import com.itsaky.androidide.testing.android.stringRes
import com.itsaky.androidide.testing.android.uiAutomation
import com.itsaky.androidide.utils.isAtLeastT
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
class OnboardingActivityBehaviorTest {

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

  @Test
  fun C_testOnboarding_toolsSetup_autoInstallBehavior() {
    val device = launchAndroidIDE(fromLauncher = true, clearTasks = true)
    val operations = FileOperations()
    operations.getAllFilesRecursively().forEach { operations.deleteFile(it) }

    device.grantAndroidIDEPermissions()

    val autoInstallSwitch = device.findObject(By.checkable(true).clazz(Switch::class.java))
    assertThat(autoInstallSwitch).isNotNull()
    assertThat(autoInstallSwitch.isChecked).isTrue()

    // verify basic switch behavior
    autoInstallSwitch.clickAndWaitForWindowUpdate(device)
    assertThat(autoInstallSwitch.isChecked).isFalse()
    assertThat(device.getObjectWithText(R.string.action_install_git).isEnabled).isFalse()
    assertThat(device.getObjectWithText(R.string.action_install_openssh).isEnabled).isFalse()
    autoInstallSwitch.clickAndWaitForWindowUpdate(device)
    assertThat(autoInstallSwitch.isChecked).isTrue()

    // clicking on the 'done' button with 'Automatic installation' must navigate the user to
    // terminal and start the installation
    val doneButton = device.findObject(By.desc(stringRes(R.string.app_intro_done_button)))
    assertThat(doneButton).isNotNull()
    assertThat(doneButton.isClickable).isTrue()

    if (isAtLeastT()) {
      // grant notification permission first
      device.uiAutomation.grantRuntimePermissionAsUser(
        getApplicationContext<Context>().packageName,
        Manifest.permission.POST_NOTIFICATIONS,
        UserHandleCompat.getUserHandleForUid(Process.myUid())
      )
    }

    doneButton.clickAndWaitForNewWindow()

    // 'Installing bootstrap packages' dialog..
    assertThat(device.findObjectWithText(R.string.bootstrap_installer_body)).isNotNull()

    // wait for the packages to be installed
    // this should take a minute at most
    // we wait until the installation finishes and the keys GridLayout is visible
    device.wait(Until.hasObject(By.clazz(GridLayout::class.java)), 60 * 1000L)

    // verify that the terminal keys view is visible
    assertThat(device.findObject(By.clazz(GridLayout::class.java))).isNotNull()
    assertThat(device.findObject(By.clazz(ViewPager::class.java))).isNotNull()
  }

  @Test
  fun D_testOnboarding_toolsSetup_manualInstallBehavior() {
    val device = launchAndroidIDE(fromLauncher = true, clearTasks = true)
    device.grantAndroidIDEPermissions()

    val autoInstallSwitch = device.findObject(By.checkable(true).clazz(Switch::class.java))
    assertThat(autoInstallSwitch).isNotNull()
    assertThat(autoInstallSwitch.isChecked).isTrue()

    // verify basic switch behavior
    autoInstallSwitch.clickAndWaitForWindowUpdate(device)
    assertThat(autoInstallSwitch.isChecked).isFalse()
    assertThat(device.getObjectWithText(R.string.action_install_git).isEnabled).isFalse()
    assertThat(device.getObjectWithText(R.string.action_install_openssh).isEnabled).isFalse()

    // clicking on the 'done' button with 'Automatic installation' must navigate the user to
    // terminal and start the installation
    val doneButton = device.findObject(By.desc(stringRes(R.string.app_intro_done_button)))
    assertThat(doneButton).isNotNull()
    assertThat(doneButton.isClickable).isTrue()

    if (isAtLeastT()) {
      // grant notification permission first
      device.uiAutomation.grantRuntimePermissionAsUser(
        getApplicationContext<Context>().packageName,
        Manifest.permission.POST_NOTIFICATIONS,
        UserHandleCompat.getUserHandleForUid(Process.myUid())
      )
    }

    doneButton.clickAndWaitForNewWindow()

    if (device.hasObjectWithText(R.string.bootstrap_installer_body)) {
      // wait for the packages to be installed
      // this should take a minute at most
      // we wait until the installation finishes and the keys GridLayout is visible
      device.wait(Until.hasObject(By.clazz(GridLayout::class.java)), 60 * 1000L)
    }

    // verify that the terminal keys view is visible
    assertThat(device.findObject(By.clazz(GridLayout::class.java))).isNotNull()
    assertThat(device.findObject(By.clazz(ViewPager::class.java))).isNotNull()
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
    device.handleAndroidRPermissions(false)

    // verify AndroidIDE is visible
    assertThat(device.currentPackageName).isEqualTo(BuildInfo.PACKAGE_NAME)

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
    device.handleAndroidRPermissions(true)

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