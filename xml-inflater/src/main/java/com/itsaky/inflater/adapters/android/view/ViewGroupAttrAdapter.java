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
package com.itsaky.inflater.adapters.android.view;

import android.animation.LayoutTransition;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;

import org.jetbrains.annotations.Contract;

/**
 * An attribute adapter implementation for ViewGroups.
 *
 * @author Akash Yadav
 */
public class ViewGroupAttrAdapter extends ViewAttrAdapter {

  public ViewGroupAttrAdapter(
      @NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
    super(resourceFinder, displayMetrics);
  }

  @Override
  public boolean isApplicableTo(View view) {
    return view instanceof ViewGroup;
  }

  @Override
  public boolean apply(@NonNull IAttribute attribute, @NonNull View view) {

    final ViewGroup group = (ViewGroup) view;
    final String name = attribute.getAttributeName();
    final String value = attribute.getValue();

    if (!canHandleNamespace(attribute)) {
      return false;
    }

    boolean handled = true;
    switch (name) {
      case "animateLayoutChanges":
        group.setLayoutTransition(new LayoutTransition());
        break;
      case "clipChildren":
        group.setClipChildren(parseBoolean(value));
        break;
      case "clipToPadding":
        group.setClipToPadding(parseBoolean(value));
        break;
      case "descendantFocusability":
        group.setDescendantFocusability(parseDescendantsFocusability(value));
        break;
      case "layoutMode":
        group.setLayoutMode(parseLayoutMode(value));
        break;
      default:
        handled = false;
        break;
    }

    if (!handled) {
      handled = super.apply(attribute, view);
    }

    return handled;
  }

  @Contract(pure = true)
  private int parseLayoutMode(String value) {
    switch (value) {
      case "opticalBounds":
        return ViewGroup.LAYOUT_MODE_OPTICAL_BOUNDS;
      case "clipBounds":
      default:
        return ViewGroup.LAYOUT_MODE_CLIP_BOUNDS;
    }
  }

  protected int parseDescendantsFocusability(@NonNull String value) {
    switch (value) {
      case "beforeDescendants":
        return ViewGroup.FOCUS_BEFORE_DESCENDANTS;
      case "blocksDescendants":
        return ViewGroup.FOCUS_BLOCK_DESCENDANTS;
      case "afterDescendants":
      default:
        return ViewGroup.FOCUS_AFTER_DESCENDANTS;
    }
  }
}
