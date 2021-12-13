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
import android.widget.AbsSpinner;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IResourceFinder;

public class AbsSpinnerAttrAdapter extends AdapterViewAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof AbsSpinner;
    }

    @Override
    public boolean apply(IAttribute attribute, View view, IResourceFinder resFinder) {
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
            case "entries" :
                final var adapter = newSimpleAdapter(context, resFinder.findArray(value));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                break;
            default:
                handled = false;
                break;
        }

        if (!handled){
            handled = super.apply(attribute, view, resFinder);
        }

        return handled;
    }
}
