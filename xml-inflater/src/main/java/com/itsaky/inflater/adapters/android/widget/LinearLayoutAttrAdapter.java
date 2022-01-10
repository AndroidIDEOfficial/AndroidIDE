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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.adapters.android.view.ViewGroupAttrAdapter;

/**
 * Adapter for handling attributes related to LinearLayout.
 *
 * @author Akash Yadav
 */
public class LinearLayoutAttrAdapter extends ViewGroupAttrAdapter {
    
    public LinearLayoutAttrAdapter (@NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
        super (resourceFinder, displayMetrics);
    }
    
    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof LinearLayout;
    }

    @Override
    public boolean apply(IAttribute attribute, View view) {
        final LinearLayout linear = (LinearLayout) view;
        final String namespace = attribute.getNamespace();
        final String name = attribute.getAttributeName();
        final String value = attribute.getValue();
        
        if (!canHandleNamespace(namespace)) {
            return false;
        }
        
        boolean handled = true;
        
        switch (name) {
            case "baselineAligned" :
                linear.setBaselineAligned(parseBoolean(value));
                break;
            case "baselineAlignedChildIndex" :
                linear.setBaselineAlignedChildIndex(parseInteger(value, linear.getChildCount()));
                break;
            case "gravity" :
                linear.setGravity(parseGravity(value));
                break;
            case "measureWithLargestChild" :
                linear.setMeasureWithLargestChildEnabled(parseBoolean(value));
                break;
            case "orientation" :
                linear.setOrientation(parseOrientation(value));
                break;
            case "weightSum" :
                linear.setWeightSum(parseFloat(value));
                break;
            default :
                handled = false;
                break;
        }
        
        if (!handled) {
            handled = super.apply(attribute, view);
        }

        return handled;
    }

    protected int parseOrientation(@NonNull String value) {
        switch (value) {
            case "vertical" :
                return LinearLayout.VERTICAL;
            case "horizontal" :
            default :
                return LinearLayout.HORIZONTAL;
        }
    }
}
