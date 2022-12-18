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
 * Handles logic for applying attributes to a view.
 *
 * @author Akash Yadav
 */
abstract class IViewAdapter : AbstractParser() {

  /** Superclasses of the view that this adpater handles. */
  var superclassHierarchy: List<String> = emptyList()
    set(value) {
      if (field.isNotEmpty()) {
        throw UnsupportedOperationException()
      }
      field = value
    }

  /**
   * The package name or namespace of the module/artifact in which the view that this adapter handles is defined.
   * The value is set to "android" by default unless explicitly specified in the [ViewAdapter]
   * [com.itsaky.androidide.annotations.inflater.ViewAdapter] annotation.
   *
   * This is used by the UI designer to quickly look for attributes of an inflated view from the
   * resource tables.
   */
  var moduleNamespace: String = ""
    set(value) {
      if (field.isNotEmpty()) {
        throw UnsupportedOperationException()
      }
      field = value
    }

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

  /**
   * Called to check whether the given attribute required for the view. A required view cannot be
   * removed by the user.
   *
   * @param attribute The attribute to check.
   * @return `true` if the attribute is required, `false` otherwise.
   */
  abstract fun isRequiredAttribute(attribute: IAttribute): Boolean
}
