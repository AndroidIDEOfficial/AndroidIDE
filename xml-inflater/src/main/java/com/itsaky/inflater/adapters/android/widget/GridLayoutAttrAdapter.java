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

package com.itsaky.inflater.adapters.android.widget;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridLayout;

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.adapters.android.view.ViewGroupAttrAdapter;

/**
 * @author Akash Yadav
 */
public class GridLayoutAttrAdapter extends ViewGroupAttrAdapter {

  public GridLayoutAttrAdapter(
      @NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
    super(resourceFinder, displayMetrics);
  }

  @Override
  public boolean isApplicableTo(View view) {
    return view instanceof GridLayout;
  }

  @Override
  public boolean apply(@NonNull IAttribute attribute, @NonNull View view) {
    final var grid = (GridLayout) view;
    final var name = attribute.getAttributeName();
    final var value = attribute.getValue();

    if (!canHandleNamespace(attribute)) {
      return false;
    }

    boolean handled = true;
    switch (name) {
      case "alignmentMode":
        grid.setAlignmentMode(parseAlignmentMode(value));
        break;
      case "columnCount":
        grid.setColumnCount(parseInteger(value, Integer.MIN_VALUE));
        break;
      case "columnOrderPreserved":
        grid.setColumnOrderPreserved(parseBoolean(value));
        break;
      case "orientation":
        grid.setOrientation(parseOrientation(value));
        break;
      case "rowCount":
        grid.setRowCount(parseInteger(value, Integer.MIN_VALUE));
        break;
      case "rowOrderPreserved":
        grid.setRowOrderPreserved(parseBoolean(value));
        break;
      case "useDefaultMargins":
        grid.setUseDefaultMargins(parseBoolean(value));
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

  protected int parseOrientation(String value) {
    if ("vertical".equals(value)) {
      return GridLayout.VERTICAL;
    }

    return GridLayout.HORIZONTAL;
  }

  protected int parseAlignmentMode(String value) {
    if ("alignBounds".equals(value)) {
      return GridLayout.ALIGN_BOUNDS;
    }

    return GridLayout.ALIGN_MARGINS;
  }
}
