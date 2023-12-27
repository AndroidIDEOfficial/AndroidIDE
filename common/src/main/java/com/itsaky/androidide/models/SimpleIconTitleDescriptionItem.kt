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

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * @author Akash Yadav
 */
data class SimpleIconTitleDescriptionItem(
  val id: Int,
  override val icon: Drawable?,
  override val title: CharSequence,
  override val description: CharSequence
) : IconTitleDescriptionItem {

  companion object {

    @JvmStatic
    fun create(
      context: Context,
      id: Int,
      @DrawableRes icon: Int,
      @StringRes title: Int,
      @StringRes description: Int
    ): SimpleIconTitleDescriptionItem {
      return SimpleIconTitleDescriptionItem(
        id,
        ContextCompat.getDrawable(context, icon),
        ContextCompat.getString(context, title),
        ContextCompat.getString(context, description)
      )
    }
  }
}
