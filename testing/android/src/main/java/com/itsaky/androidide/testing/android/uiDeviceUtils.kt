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

import android.view.KeyCharacterMap
import androidx.annotation.StringRes
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.buildinfo.BuildInfo

private val keyCharMap by lazy {
  KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD)
}

/**
 * @see UiDevice.waitForWindowUpdate
 */
fun UiDevice.waitForWindowUpdate(timeout: Long) =
  waitForWindowUpdate(BuildInfo.PACKAGE_NAME, timeout)

/**
 * @see UiDevice.hasObject
 */
fun UiDevice.hasObjectWithText(text: String) =
  hasObject(By.text(text))

/**
 * @see UiDevice.hasObject
 */
fun UiDevice.hasObjectWithText(@StringRes text: Int) =
  hasObjectWithText(stringRes(text))

/**
 * @see UiDevice.findObject
 */
fun UiDevice.findObjectWithText(text: String): UiObject2? =
  findObject(By.text(text))

/**
 * @see UiDevice.findObjectWithText
 */
fun UiDevice.findObjectWithText(@StringRes text: Int): UiObject2? =
  findObjectWithText(stringRes(text))


fun UiDevice.getObjectWithText(text: String): UiObject2 {
  val obj = findObjectWithText(text)
  assertThat(obj).isNotNull()
  return obj!!
}

fun UiDevice.getObjectWithText(@StringRes text: Int): UiObject2 = getObjectWithText(stringRes(text))

/**
 * Sends key events to this [UiDevice] to type the given [text].
 */
fun UiDevice.sendKeyEvents(text: String) {
  waitForIdle()
  val keyEvents = keyCharMap.getEvents(text.toCharArray())
  for (keyEvent in keyEvents) {
    uiAutomation.injectInputEvent(keyEvent, true)
    waitForIdle()
  }
}