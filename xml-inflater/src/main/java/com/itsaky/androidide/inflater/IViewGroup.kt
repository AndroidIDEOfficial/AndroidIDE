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
 * Inflated model for a [ViewGroup][android.view.ViewGroup].
 *
 * @author Akash Yadav
 */
interface IViewGroup : IView {

  /** The number of children in this view group. */
  val childCount: Int

  /**
   * Add the given child.
   *
   * @param view The view to add.
   */
  fun addChild(view: IView)

  /**
   * Add the given [view] at the given [index].
   *
   * @param index The index at which the child should be added.
   * @param view The child view to add .
   */
  fun addChild(index: Int, view: IView)

  /**
   * Remove the given child.
   *
   * @param view The child to remove.
   */
  fun removeChild(view: IView)

  /**
   * Remove the child at the given index.
   *
   * @param index The index of the child to remove.
   */
  fun removeChild(index: Int)

  /**
   * Get the child at the given index.
   *
   * @param index The index of the child.
   */
  operator fun get(index: Int): IView

  /**
   * Replace the child at the given index.
   *
   * @param index The index of the child to replace.
   * @param view The new view to set.
   * @return The existing child or `null`.
   */
  operator fun set(index: Int, view: IView): IView

  /**
   * Finds the index of the given child in this view group.
   *
   * @param child The child.
   * @return The index of the child if found or -1.
   */
  fun indexOfChild(child: IView): Int {
    for (i in 0 until childCount) {
      if (this[i].view == child.view) {
        return i
      }
    }
    return -1
  }

  /**
   * Adds the given hierarchy change listener.
   *
   * @param listener The listener to add.
   */
  fun addOnHierarchyChangeListener(listener: OnHierarchyChangeListener)

  /**
   * Removes the given hierarchy change listener.
   *
   * @param listener The listener to remove.
   */
  fun removeOnHierarchyChangeListener(listener: OnHierarchyChangeListener)

  /** Listener to listen for hierarchy changes in an [IViewGroup]. */
  interface OnHierarchyChangeListener {
  
    /**
     * Called just before the [view] is added to the [group].
     *
     * @param group The group in which the view will be added.
     * @param view The view that will be added to the given view group.
     */
    fun beforeViewAdded(group: IViewGroup, view: IView) {}
  
    /**
     * Called just before the [view] is removed from the [group].
     *
     * @param group The group from which the view will be removed.
     * @param view The view that will be removed from the given view group.
     */
    fun beforeViewRemoved(group: IViewGroup, view: IView) {}
    
    /**
     * Called when a view is added.
     *
     * @param group The view group in which the view was added.
     * @param view The added view.
     */
    fun onViewAdded(group: IViewGroup, view: IView)

    /**
     * Called when a view is removed.
     *
     * @param group The view group from which the view was removed.
     * @param view The removed view.
     */
    fun onViewRemoved(group: IViewGroup, view: IView)
  }

  /** Allows overriding a single method in [OnHierarchyChangeListener]. */
  open class SingleOnHierarchyChangeListener : OnHierarchyChangeListener {
    override fun onViewAdded(group: IViewGroup, view: IView) {}
    override fun onViewRemoved(group: IViewGroup, view: IView) {}
  }
}
