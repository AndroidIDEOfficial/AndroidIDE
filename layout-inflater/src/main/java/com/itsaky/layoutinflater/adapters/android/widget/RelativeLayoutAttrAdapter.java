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
import android.widget.RelativeLayout;
import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IResourceFinder;
import com.itsaky.layoutinflater.adapters.android.view.ViewGroupAttrAdapter;

/**
 * Attribute adapter for handling attributes related to
 * RelativeLayout.
 *
 * @author Akash Yadav
 */
public class RelativeLayoutAttrAdapter extends ViewGroupAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof RelativeLayout;
    }

    @Override
    public boolean apply(IAttribute attribute, View view, IResourceFinder resFinder) {
        
        final RelativeLayout relative = (RelativeLayout) view;
        final String namespace = attribute.getNamespace();
        final String name = attribute.getAttributeName();
        final String value = attribute.getValue();
        
        if (!canHandleNamespace(namespace)) {
            return false;
        }
        
        boolean handled = true;
        
        switch (name) {
            case "gravity" :
                relative.setGravity(parseGravity(value));
                break;
            case "ignoreGravity" :
                relative.setIgnoreGravity(parseId(value));
                break;
            default :
                handled = false;
                break;
        }
        
        if (!handled) {
            handled = super.apply(attribute, view, resFinder);
        }
        
        return handled;
    }
}
