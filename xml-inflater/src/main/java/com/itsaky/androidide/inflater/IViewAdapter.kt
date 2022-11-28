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

/**
 * A view adapter handles the application and removal of attributes on a view.
 *
 * @author Akash Yadav
 */
abstract class IViewAdapter : AbstractParser() {

  /**
   * Apply the given attribute to the given view.
   *
   * @param view The view to which the attribute must be applied.
   * @param attribute The attribute to apply.
   * @return Whether the attribute was applied or not.
   */
  abstract fun apply(view: IView, attribute: IAttribute): Boolean

  /**
   * Apply the basic attributes to a view so that it could be rendered.
   *
   * @param view The view to apply basic attributes to.
   */
  abstract fun applyBasic(view: IView)
}
