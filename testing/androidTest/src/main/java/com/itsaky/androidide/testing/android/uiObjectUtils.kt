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

import androidx.annotation.StringRes
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.google.common.truth.Truth.assertThat

/**
 * Click this object and wait for window update with the given timeout duration.
 *
 * @see UiDevice.waitForWindowUpdate
 */
fun UiObject2?.clickAndWaitForWindowUpdate(device: UiDevice, timeout: Long = 500L) =
  run {
    assertThat(this).isNotNull()
    this!!.click()
    device.waitForWindowUpdate(timeout)
  }

/**
 * Click this object and wait for a new window to appear within the given timeout duration.
 *
 * @see UiObject2.clickAndWait
 */
fun UiObject2?.clickAndWaitForNewWindow(timeout: Long = LAUNCH_TIMEOUT) =
  run {
    assertThat(this).isNotNull()
    this!!.clickAndWait(Until.newWindow(), timeout)
  }

/**
 * @see UiDevice.hasObject
 */
fun UiObject2.hasObjectWithText(text: String) =
  hasObject(By.text(text))

/**
 * @see UiObject2.hasObject
 */
fun UiObject2.hasObjectWithText(@StringRes text: Int) =
  hasObjectWithText(stringRes(text))

/**
 * @see UiObject2.findObject
 */
fun UiObject2.findObjectWithText(text: String): UiObject2? =
  findObject(By.text(text))

/**
 * @see UiObject2.findObjectWithText
 */
fun UiObject2.findObjectWithText(@StringRes text: Int): UiObject2? =
  findObjectWithText(stringRes(text))


fun UiObject2.getObjectWithText(text: String): UiObject2 {
  val obj = findObjectWithText(text)
  assertThat(obj).isNotNull()
  return obj!!
}

fun UiObject2.getObjectWithText(@StringRes text: Int): UiObject2 = getObjectWithText(stringRes(text))