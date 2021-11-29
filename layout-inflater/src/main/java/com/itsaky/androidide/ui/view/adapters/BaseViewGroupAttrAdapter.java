/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
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
package com.itsaky.androidide.ui.view.adapters;

import android.animation.LayoutTransition;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import com.itsaky.androidide.ui.inflater.IResourceFinder;
import com.itsaky.androidide.ui.view.IAttribute;

public class BaseViewGroupAttrAdapter extends BaseViewAttrAdapter {
    
    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof ViewGroup;
    }
    
    @Override
    public boolean apply(IAttribute attribute, View view, IResourceFinder resFinder) {
        
        // Check if this attribute can be handled by the parent
        if (super.apply(attribute, view, resFinder)) {
            return true;
        }
        final ViewGroup group = (ViewGroup) view;
        final String namespace = attribute.getNamespace();
        final String name = attribute.getNamespace();
        final String value = attribute.getValue();
        
        if (!canHandleNamespace(namespace)) {
            return false;
        }
        
        boolean handled = true;
        switch (name) {
            case "animateLayoutChanges" :
                group.setLayoutTransition(new LayoutTransition());
                break;
            case "clipChildren" :
                group.setClipChildren(parseBoolean(value, resFinder));
                break;
            case "clipToPadding" :
                group.setClipToPadding(parseBoolean(value, resFinder));
                break;
            case "descendantFocusability" :
                group.setDescendantFocusability(parseDescendantsFocusablility(value));
                break;
            case "layoutMode" :
                group.setLayoutMode(parseLayoutMode(value));
                break;
            default :
            handled = false;
                break;
        }
        
        return handled;
    }

    private int parseLayoutMode(String value) {
        switch (value) {
            case "opticalBounds" :
                return ViewGroup.LAYOUT_MODE_OPTICAL_BOUNDS;
            case "clipBounds" :
            default :
                return ViewGroup.LAYOUT_MODE_CLIP_BOUNDS;
        }
    }

    protected int parseDescendantsFocusablility(String value) {
        switch (value) {
            case "beforeDescendants" :
                return ViewGroup.FOCUS_BEFORE_DESCENDANTS;
            case "blocksDescendants" :
                return ViewGroup.FOCUS_BLOCK_DESCENDANTS;
            case "afterDescendants" :
            default :
                return ViewGroup.FOCUS_AFTER_DESCENDANTS;
        }
    }
}
