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

import android.os.Build

/**
 * @author Akash Yadav
 */
object ManufacturerUtils {
  private const val LGE = "lge"
  private const val SAMSUNG = "samsung"
  private const val MEIZU = "meizu"
  
  /** Returns true if the device manufacturer is Meizu.  */
  fun isMeizuDevice(): Boolean {
    return Build.MANUFACTURER.lowercase() == MEIZU
  }
  
  /** Returns true if the device manufacturer is LG.  */
  fun isLGEDevice(): Boolean {
    return Build.MANUFACTURER.lowercase() == LGE
  }
  
  /** Returns true if the device manufacturer is Samsung.  */
  fun isSamsungDevice(): Boolean {
    return Build.MANUFACTURER.lowercase() == SAMSUNG
  }
  
  /**
   * Returns true if the date input keyboard is potentially missing separator characters such as /.
   */
  fun isDateInputKeyboardMissingSeparatorCharacters(): Boolean {
    return isLGEDevice() || isSamsungDevice()
  }
}