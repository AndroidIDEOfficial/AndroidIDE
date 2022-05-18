/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.inflater;

import android.view.View;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a {@link android.view.ViewGroup}
 *
 * @author Akash Yadav
 */
public interface IViewGroup extends IView {

  /**
   * Add this view to this group
   *
   * @param view The view to add
   */
  void addView(IView view);

  /**
   * Add this view to this group
   *
   * @param view The view to add
   * @param index Add this view to this index
   */
  void addView(IView view, int index);

  /**
   * Remove the view at the specified index
   *
   * @param index The index of the view to remove
   */
  void removeView(int index);

  /**
   * Remove the given if it exists in this group
   *
   * @param view The view to remove
   */
  void removeView(IView view);

  /** Remove all children in this group */
  void removeAll();

  /**
   * Perform an action in every child in this group
   *
   * @param consumer The consumer which will consume the views
   */
  void forEachChild(Consumer<IView> consumer);

  /**
   * Get the number of child views this layout contains
   *
   * @return The child count
   */
  int getChildCount();

  /**
   * Get the list of children in this group
   *
   * @return The list of children
   */
  List<IView> getChildren();

  /**
   * Get child at the provided index
   *
   * @param index The index of child
   * @throws IndexOutOfBoundsException If there is no child at the given index
   */
  IView getChildAt(int index);

  /**
   * Find the index of the given children in this group.
   *
   * @param view The view to find index of.
   * @return The index of the child or -1 if this group does not contain the given child.
   */
  int indexOfChild(IView view);

  /**
   * Find the index of the given children in this group.
   *
   * @param view The view to find index of.
   * @return The index of the child or -1 if this group does not contain the given child.
   */
  int indexOfChild(View view);

  /**
   * Register the given hierarchy change listener to this group.
   *
   * @param listener The listener to register.
   */
  void registerHierarchyChangeListener(OnHierarchyChangeListener listener);

  /**
   * Unregister the given hierarchy change listener from this view group.
   *
   * @param listener The listener to unregister.
   */
  void unregisterHierarchyChangeListener(OnHierarchyChangeListener listener);

  /**
   * A listener which can be used to get notification when a children is added or removed from an
   * {@link IViewGroup}.
   *
   * @author Akash Yadav
   */
  public static interface OnHierarchyChangeListener {

    /**
     * Called when a child view is added in the view group to which this listener is attached.
     *
     * @param view The view that was added.
     */
    void onViewAdded(IView view);

    /**
     * Called when a child view is removed from the view group to which this listener is attached.
     *
     * @param view The view that was removed. Note that this view's parent will be null.
     */
    void onViewRemoved(IView view);
  }
}
