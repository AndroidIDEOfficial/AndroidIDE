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

package com.itsaky.layoutinflater.adapters.android.widget;

import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IResourceFinder;

/**
 * Attribute adapter for handling attributes related to
 * Spinner.
 *
 * @author Akash Yadav
 */
public class SpinnerAttrAdapter extends AbsSpinnerAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof Spinner;
    }

    @Override
    public boolean apply(@NonNull IAttribute attribute, View view) {
        final var spinner = (Spinner) view;
        final var context = spinner.getContext();
        final var dm = context.getResources().getDisplayMetrics();
        final var namespace = attribute.getNamespace();
        final var name = attribute.getAttributeName();
        final var value = attribute.getValue();

        if (!canHandleNamespace(namespace)) {
            return false;
        }

        boolean handled = true;

        switch (name) {
            case "dropDownHorizontalOffset":
                spinner.setDropDownHorizontalOffset(parseDimension(value, 0, dm));
                break;
            case "dropDownVerticalOffset":
                spinner.setDropDownVerticalOffset(parseDimension(value, 0, dm));
                break;
            case "dropDownWidth":
                spinner.setDropDownWidth(parseDimension(value, 0, dm));
                break;
            case "gravity" :
                spinner.setGravity(parseGravity(value));
                break;
            case "popupBackground" :
                spinner.setPopupBackgroundDrawable(parseDrawable(value, context));
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
