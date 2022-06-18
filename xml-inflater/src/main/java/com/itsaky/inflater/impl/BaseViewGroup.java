/************************************************************************************
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
 **************************************************************************************/
package com.itsaky.inflater.impl;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttributeAdapter;
import com.itsaky.inflater.IView;
import com.itsaky.inflater.IViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseViewGroup extends BaseView implements IViewGroup {

  protected final List<OnHierarchyChangeListener> mHierarchyChangeListeners = new ArrayList<>();

  public BaseViewGroup(String qualifiedName, ViewGroup view) {
    this(qualifiedName, view, false);
  }

  public BaseViewGroup(String qualifiedName, ViewGroup view, boolean isPlaceholder) {
    super(qualifiedName, view, isPlaceholder);
  }

  @Override
  public boolean isPlaceholder() {
    return false; // We do not use view groups as placeholder
  }

  @Override
  public void addView(IView view, int index) {
    for (IAttributeAdapter adapter : getAttributeAdapters()) {
      view.registerAttributeAdapter(adapter);
    }
  }

  @Override
  public int indexOfChild(@NonNull IView view) {
    return indexOfChild(view.asView());
  }

  @Override
  public int indexOfChild(View view) {
    for (var i = 0; i < getChildCount(); i++) {
      final var child = getChildAt(i);
      if (child != null
          && child.asView() != null
          && child.asView() == view) { // Comparison is same in ViewGroup implementation
        return i;
      }
    }
    return -1;
  }

  @Override
  public void registerHierarchyChangeListener(OnHierarchyChangeListener listener) {
    if (listener == null) {
      return;
    }
    this.mHierarchyChangeListeners.add(listener);
  }

  @Override
  public void unregisterHierarchyChangeListener(OnHierarchyChangeListener listener) {
    if (listener == null) {
      return;
    }

    this.mHierarchyChangeListeners.remove(listener);
  }

  /**
   * Called when a new view has been added to this group
   *
   * @param view The view that was added
   */
  protected void onViewAdded(IView view) {
    for (OnHierarchyChangeListener listener : this.mHierarchyChangeListeners) {
      listener.onViewAdded(view);
    }
  }

  /**
   * Called when a new view has been removed from this group
   *
   * @param view The view that was removed
   */
  protected void onViewRemoved(IView view) {
    for (OnHierarchyChangeListener listener : this.mHierarchyChangeListeners) {
      listener.onViewRemoved(view);
    }
  }
}
