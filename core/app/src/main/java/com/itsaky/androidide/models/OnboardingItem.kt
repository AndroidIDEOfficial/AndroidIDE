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

package com.itsaky.androidide.models

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

/**
 * Item that is shown on the onboarding fragment.
 *
 * @author Akash Yadav
 */
interface OnboardingItem : Parcelable {

  /**
   * The key for the item.
   */
  val key: String

  /**
   * Title for the item.
   */
  val title: CharSequence

  /**
   * Short description (or subtitle) for the item.
   */
  val description: CharSequence

  /**
   * Icon for the item.
   */
  @get:DrawableRes
  val icon: Int

  /**
   * Icon tint for the item.
   */
  @get:ColorInt
  val iconTint: Int

  /**
   * Whether is item is clickable.
   */
  val isClickable: Boolean

  /**
   * Whether the item is long clickable.
   */
  val isLongClickable: Boolean
}