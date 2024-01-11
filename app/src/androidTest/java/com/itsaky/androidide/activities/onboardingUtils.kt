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
import android.widget.Switch
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.UiSelector
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.R
import com.itsaky.androidide.testing.android.clickAndWaitForNewWindow
import com.itsaky.androidide.testing.android.clickAndWaitForWindowUpdate
import com.itsaky.androidide.testing.android.getObjectWithText
import com.itsaky.androidide.testing.android.hasObjectWithText

/**
 * @author Akash Yadav
 */

fun UiDevice.grantAndroidIDEPermissions() {
  nextButton().clickAndWaitForWindowUpdate(this)

  if (hasObjectWithText(R.string.title_androidide_statistics)) {
    // If we haven't opted-in for statistics yet...
    nextButton().clickAndWaitForWindowUpdate(this)
  }

  if (hasObjectWithText(R.string.onboarding_title_permissions)) {
    // If the permissions are not granted, grant them
    for (desc in arrayOf(R.string.permission_desc_storage,
      R.string.permission_desc_install_packages)) {
      val grantButton = getObjectWithText(desc)
        .parent!!
        .parent!!
        .findObject(By.clazz(Button::class.java))

      if (grantButton.text != null) {
        grantButton.clickAndWaitForNewWindow()
        handleAndroidRPermissions(allowPermission = true)
      }
    }

    nextButton().clickAndWaitForWindowUpdate(this)
  }
}

fun UiDevice.nextButton(): UiObject2 {
  return findObject(By.desc("NEXT"))!!
}

fun UiDevice.handleAndroidRPermissions(
  allowPermission: Boolean = true,
) {
  assertThat(currentPackageName).isAnyOf("com.android.settings", "com.android.permissioncontroller")
  assertThat(hasObjectWithText(R.string.app_name)).isTrue()

  val storageSwitch = findObject(UiSelector().className(Switch::class.java))
  assertThat(storageSwitch).isNotNull()

  // verify initial state
  assertThat(storageSwitch.isCheckable).isTrue()
  assertThat(storageSwitch.isEnabled).isTrue()
  assertThat(storageSwitch.isChecked).isFalse()

  if (allowPermission) {
    // enable permission
    storageSwitch.click()

    // verify changed state
    assertThat(storageSwitch.isChecked).isTrue()
  }

  // navigate back without enabling the permission
  pressBack()
}