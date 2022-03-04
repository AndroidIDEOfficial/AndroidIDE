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
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;

/**
 * Attribute handler for handling attributes related to CheckedTextView.
 *
 * @author Akash Yadav
 */
public class CheckedTextViewAttrAdapter extends TextViewAttrAdapter {

    public CheckedTextViewAttrAdapter(
            @NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
        super(resourceFinder, displayMetrics);
    }

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof CheckedTextView;
    }

    @Override
    public boolean apply(@NonNull IAttribute attribute, @NonNull View view) {

        final CheckedTextView text = (CheckedTextView) view;
        final String name = attribute.getAttributeName();
        final String value = attribute.getValue();

        if (!canHandleNamespace(attribute)) {
            return false;
        }

        boolean handled = true;

        switch (name) {
            case "checkMarkTintMode":
                text.setCheckMarkTintMode(parsePorterDuffMode(value));
                break;
            case "checkMarkTint":
                text.setCheckMarkTintList(parseColorStateList(value, text.getContext()));
                break;
            case "checkMark":
                // Ignored...
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
