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

package com.itsaky.androidide.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.IntDef

// Source: https://gist.github.com/Thorsten1976/07d61b3f697364e5f1c08ae076641d58

object NavigationBar {
  /** Classic three-button navigation (Back, Home, Recent Apps) */
  const val MODE_THREE_BUTTONS = 0

  /** Two-button navigation (Android P navigation mode: Back, combined Home and Recent Apps) */
  const val MODE_TWO_BUTTONS = 1

  /** Full screen gesture mode (introduced with Android Q) */
  const val MODE_GESTURES = 2

  /**
   * Returns the interaction mode of the system navigation bar as defined by
   * [NavigationBarInteractionMode]. Depending on the Android version and OEM implementation,
   * users might change the interaction mode via system settings: System>Gestures>System Navigation.
   * This can lead to conflicts with apps that use specific system-gestures internally for
   * navigation (i. e. swiping), especially if the Android 10 full screen gesture mode is enabled.
   *
   *
   * Before Android P the system has used a classic three button navigation. Starting with Android P
   * a two-button-based interaction mode was introduced (also referred as Android P navigation).
   *
   *
   * Android Q changed the interaction and navigation concept to a gesture approach, the so called
   * full screen gesture mode: system-wide gestures are used to navigate within an app or to
   * interact with the system (i. e. back-navigation, open the home-screen, changing apps, or toggle
   * a fullscreen mode).
   *
   *
   * Based on [https://stackoverflow.com/questions/56689210/how-to-detect-full-screen-gesture-mode-in-android-10](https://stackoverflow.com/questions/56689210/how-to-detect-full-screen-gesture-mode-in-android-10)
   *
   * @param context The [Context] that is used to read the resource configuration.
   * @return the [NavigationBarInteractionMode]
   * @see .MODE_THREE_BUTTONS
   *
   * @see .MODE_TWO_BUTTONS
   *
   * @see .MODE_GESTURES
   */
  @SuppressLint("DiscouragedApi")
  @NavigationBarInteractionMode
  fun getInteractionMode(context: Context): Int {
    val resources = context.resources

    val resourceId = resources.getIdentifier(
      "config_navBarInteractionMode",
      "integer",
      "android"
    )

    return if (resourceId > 0)
      resources.getInteger(resourceId)
    else
      MODE_THREE_BUTTONS
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(MODE_THREE_BUTTONS, MODE_TWO_BUTTONS, MODE_GESTURES)
  internal annotation class NavigationBarInteractionMode

}
