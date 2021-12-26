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
import android.widget.AbsSeekBar;
import androidx.annotation.NonNull;
import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IResourceFinder;

/**
 * Attribute adapter for handling attributes related to
 * AbsSeekBar.
 *
 * @author Akash Yadav
 */
public abstract class AbsSeekBarAttrAdapter extends ProgressBarAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof AbsSeekBar;
    }

    @Override
    public boolean apply(@NonNull IAttribute attribute, View view) {
        final var seek = (AbsSeekBar) view;
        final var namespace = attribute.getNamespace();
        final var name = attribute.getAttributeName();
        final var value = attribute.getValue();

        if (!canHandleNamespace(namespace)) {
            return false;
        }

        boolean handled = true;

        switch (name) {
            case "thumbTint" :
                // TODO Parse color state list
                break;
            case "thumbTintMode" :
                seek.setThumbTintMode(parsePorterDuffMode(value));
                break;
            case "tickMarkTint" :
                // TODO Parse color state list
                break;
            case "tickMarkTintMode":
                seek.setTickMarkTintMode(parsePorterDuffMode(value));
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
