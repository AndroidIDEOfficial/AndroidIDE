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

package com.itsaky.androidide.inflater

import android.view.View

/**
 * An inflated model for a [View].
 *
 * @author Akash Yadav
 */
interface IView {

  /** The XML tag. */
  val tag: String

  /** The fully qualified name of this view. */
  val name: String

  /** The simple name of this view. Usually same as [tag]. */
  val simpleName: String

  /** The inflated view. */
  val view: View

  /** The parent view. */
  var parent: IViewGroup?

  /**
   * Add and apply the given attribute to this view.
   *
   * @param attribute The attribute to apply.
   */
  fun addAttribute(attribute: IAttribute)

  /**
   * Remove the given attribute and update the view accordingly.
   *
   * @param attribute The attribute to remove.
   */
  fun removeAttribute(attribute: IAttribute)
}
