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

import android.graphics.RectF
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

  /** The namespaces that have been declared in this view. */
  val namespaceDecls: Collection<INamespace>

  /** The attributes of this view. */
  val attributes: List<IAttribute>

  /**
   * Add and apply the given attribute to this view.
   *
   * @param attribute The attribute to apply.
   * @param apply Whether the attribute should be applied to the attribute. If this `false` then the
   * attribute will be simply added to the attributes list.
   * @param update Whether the attribute's value should be updated if the attribute is already applied to this view.
   */
  fun addAttribute(attribute: IAttribute, apply: Boolean = true, update: Boolean = false)

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
   * Applies the given attribute without adding it to the attributes list or notifying the attribute
   * change listeners. Not resetting the attribute to its original value may have unexpected result.
   *
   * @param attribute The attribute to apply.
   */
  fun applyAttribute(attribute: IAttribute)

  /**
   * Checks whether this view has an attribute entry with the given [namespaceUri] and [name].
   *
   * @param namespaceUri The namespace uri of the attribute.
   * @param name The name of the attribute to look for.
   * @return `true` if this view has an attribute entry with the given name namespace uri and name,
   * `false` otherwise.
   */
  fun hasAttribute(name: String, namespaceUri: String? = null): Boolean {
    return findAttribute(name, namespaceUri) != null
  }

  /**
   * Finds attribute with the given namespace uri and name.
   *
   * @param namespaceUri The namespace uri of the attribute to find.
   * @param name The name of the attribute.
   * @return The attribute if found or `null`.
   */
  fun findAttribute(name: String, namespaceUri: String? = null): IAttribute?

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
   * Get the bounds of the view.
   *
   * @return The bounds of the view.
   */
  fun getViewRect(): RectF {
    return RectF().apply { getViewRect(this) }
  }

  /**
   * Get the bounds of the view.
   *
   * @return The [RectF] to store the bounds of the view.
   */
  fun getViewRect(out: RectF) {
    out.left = view.left.toFloat()
    out.top = view.top.toFloat()
    out.right = out.left + view.width
    out.bottom = out.top + view.height
  }

  /** Removes this view from its parent view. */
  fun removeFromParent() {
    parent?.removeChild(this)
  }

  /**
   * Registers the given [AttributeChangeListener] to listen for changes in attributes.
   *
   * @param listener The listener to register.
   */
  fun registerAttributeChangeListener(listener: AttributeChangeListener)

  /**
   * Unregisters the given [AttributeChangeListener].
   *
   * @param listener The listener to unregister.
   * @see registerAttributeChangeListener
   */
  fun unregisterAttributeChangeListener(listener: AttributeChangeListener)

  /** Listener to listen for changes in attributes of an [IView]. */
  interface AttributeChangeListener {

    /**
     * Called when a new attribute is added to [view].
     *
     * @param view The view to which the attribute was added.
     * @param attribute The added attribute.
     */
    fun onAttributeAdded(view: IView, attribute: IAttribute)

    /**
     * Called when an attribute is removed from [view].
     *
     * @param view The view from which the attribute was removed.
     * @param attribute The removed attribute.
     */
    fun onAttributeRemoved(view: IView, attribute: IAttribute)

    /**
     * Called when the value of an attribute in the [view] is updated.
     *
     * @param view The view whose attribute was updated.
     * @param attribute The attribute that was updated. This contains the new value of the
     * attribute.
     * @param oldValue The old value of the attribute.
     */
    fun onAttributeUpdated(view: IView, attribute: IAttribute, oldValue: String)
  }

  open class SingleAttributeChangeListener : AttributeChangeListener {

    override fun onAttributeAdded(view: IView, attribute: IAttribute) {}
    override fun onAttributeRemoved(view: IView, attribute: IAttribute) {}
    override fun onAttributeUpdated(view: IView, attribute: IAttribute,
      oldValue: String
    ) {
    }
  }
}
