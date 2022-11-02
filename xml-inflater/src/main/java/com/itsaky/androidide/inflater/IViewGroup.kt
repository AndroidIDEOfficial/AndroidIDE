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
   * @return The removed child.
   */
  fun removeChild(index: Int): IView

  /**
   * Get the child at the given index.
   *
   * @param index The index of the child.
   */
  fun get(index: Int): IView

  /**
   * Replace the child at the given index.
   *
   * @param index The index of the child to replace.
   * @param view The new view to set.
   * @return The existing child or `null`.
   */
  fun set(index: Int, view: IView): IView
}
