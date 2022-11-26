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
  fun addAttribute(attribute: IAttribute, update: Boolean = false)

  /**
   * Remove the given attribute and update the view accordingly.
   *
   * @param attribute The attribute to remove.
   */
  fun removeAttribute(attribute: IAttribute)

  /**
   * Updates the already-added attribute entry with the value of the given [attribute].
   *
   * @param attribute The attribute containing information about the attribute to update. This
   * should contain the new value of the attribute.
   */
  fun updateAttribute(attribute: IAttribute)

  /**
   * Checks whether this view has an attribute entry with the given [namespaceUri] and [name].
   *
   * @param namespaceUri The namespace uri of the attribute.
   * @param name The name of the attribute to look for.
   * @return `true` if this view has an attribute entry with the given name namespace uri and name,
   * `false` otherwise.
   */
  fun hasAttribute(namespaceUri: String, name: String): Boolean {
    return findAttribute(namespaceUri, name) != null
  }

  /**
   * Finds attribute with the given namespace uri and name.
   *
   * @param namespaceUri The namespace uri of the attribute to find.
   * @param name The name of the attribute.
   * @return The attribute if found or `null`.
   */
  fun findAttribute(namespaceUri: String, name: String): IAttribute?

  /**
   * Called when the highlighted state of the view is changed. The highlighted state is changed in
   * case of a [ACTION_DOWN][android.view.MotionEvent.ACTION_DOWN] or [ACTION_UP]
   * [android.view.MotionEvent.ACTION_UP] motion event. In case of an [IViewGroup], the highlighted
   * state is also changed when the view group receives the drag events.
   *
   * @param highlight Whether the view should be highlighted or not.
   */
  fun onHighlightStateUpdated(highlight: Boolean)
  
  /**
   * Removes this view from its parent view.
   */
  fun removeFromParent() {
    parent?.removeChild(this)
  }
}
