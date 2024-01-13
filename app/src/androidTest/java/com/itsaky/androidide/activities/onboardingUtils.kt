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
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.R
import com.itsaky.androidide.testing.android.clickAndWaitForNewWindow
import com.itsaky.androidide.testing.android.clickAndWaitForWindowUpdate
import com.itsaky.androidide.testing.android.getObjectWithText
import com.itsaky.androidide.testing.android.hasObjectWithText
import com.itsaky.androidide.utils.isAtLeastR

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
        if (desc == R.string.permission_desc_install_packages || isAtLeastR()) {
          grantButton.clickAndWaitForNewWindow()
          handlePermissionInSettings(allowPermission = true)
        } else {
          getObjectWithText("ALLOW").clickAndWaitForWindowUpdate(this)
        }
      }
    }

    nextButton().clickAndWaitForWindowUpdate(this)
  }
}

fun UiDevice.nextButton(): UiObject2 {
  return findObject(By.desc("NEXT"))!!
}

fun UiDevice.handlePermissionInSettings(
  allowPermission: Boolean = true,
) {
  assertThat(currentPackageName).startsWith("com.android.")
  assertThat(hasObjectWithText(R.string.app_name)).isTrue()

  val switch = findObject(By.clazz(Switch::class.java))
  assertThat(switch).isNotNull()

  // verify initial state
  assertThat(switch.isCheckable).isTrue()
  assertThat(switch.isEnabled).isTrue()

  if (allowPermission) {
    if (!switch.isChecked) {
      // enable permission
      switch.click()
    }

    // verify changed state
    assertThat(switch.isChecked).isTrue()
  } else if (switch.isChecked) {
    // disable permission
    switch.click()

    // verify changed state
    assertThat(switch.isChecked).isFalse()
  }

  // navigate back without enabling the permission
  pressBack()
}