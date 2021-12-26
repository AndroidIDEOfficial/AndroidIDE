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

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IResourceFinder;
import com.itsaky.layoutinflater.adapters.android.view.ViewGroupAttrAdapter;

/**
 * Adapter for handling attributes related to
 * AdapterView.
 *
 * @author Akash Yadav
 */
public abstract class AdapterViewAttrAdapter extends ViewGroupAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof AdapterView;
    }

    @Override
    public boolean apply(IAttribute attribute, View view) {
        // No special attributes for AdapterView
        return super.apply(attribute, view);
    }

    protected ArrayAdapter <String> newSimpleAdapter (Context ctx) {
        return newSimpleAdapter(ctx, newAdapterItems(4));
    }

    protected ArrayAdapter <String> newSimpleAdapter (Context ctx, String[] items) {
        return new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, items);
    }

    protected String[] newAdapterItems (int size) {
        final var items = new String [size];
        for (var i = 0;i<size;i++) {
            items[i] = "Item " + i;
        }
        return items;
    }
}
