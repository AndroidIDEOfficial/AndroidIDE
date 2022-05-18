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
import android.widget.AbsSpinner;

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;

public class AbsSpinnerAttrAdapter extends AdapterViewAttrAdapter {

  public AbsSpinnerAttrAdapter(
      @NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
    super(resourceFinder, displayMetrics);
  }

  @Override
  public boolean isApplicableTo(View view) {
    return view instanceof AbsSpinner;
  }

  @Override
  public boolean apply(@NonNull IAttribute attribute, @NonNull View view) {
    final var spinner = (AbsSpinner) view;
    final var context = spinner.getContext();
    final var namespace = attribute.getNamespace();
    final var name = attribute.getAttributeName();
    final var value = attribute.getValue();

    boolean handled = true;

    if (!canHandleNamespace(namespace)) {
      return false;
    }

    switch (name) {
      case "entries":
        final var array = parseArray(value);
        final var adapter = newSimpleAdapter(context, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
}
