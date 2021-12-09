/************************************************************************************
 * This file is part of AndroidIDE.
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
package com.itsaky.layoutinflater.adapters.android.widget;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IResourceFinder;
import com.itsaky.layoutinflater.adapters.android.view.ViewAttrAdapter;

/**
 * Attribute adapter for handling attributes related to
 * ProgressBar.
 *
 * @author Akash Yadav
 */
public class ProgressBarAttrAdapter extends ViewAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof ProgressBar;
    }

    @Override
    public boolean apply(IAttribute attribute, View view, IResourceFinder resFinder) {
        final ProgressBar pb = (ProgressBar) view;
        final Context context = pb.getContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        final String namespace = attribute.getNamespace();
        final String name = attribute.getAttributeName();
        final String value = attribute.getValue();
        
        if (!canHandleNamespace(namespace)) {
            return false;
        }
        
        boolean handled = true;
        
        switch (name) {
            case "indeterminate" :
                pb.setIndeterminate(parseBoolean(value, resFinder));
                break;
            case "indeterminateDrawable" :
                pb.setIndeterminateDrawable(parseDrawable(value, resFinder, context));
                break;
            case "indeterminateTint" :
                // TODO Parse color state list
                break;
            case "indeterminateTintMode" :
                pb.setIndeterminateTintMode(parsePorterDuffMode(value));
                break;
            case "max" :
                pb.setMax(parseInteger(value, 100));
                break;
            case "maxHeight" :
                pb.setMaxHeight(parseDimension(value, Integer.MAX_VALUE, dm, resFinder));
                break;
            case "maxWidth" :
                pb.setMaxWidth(parseDimension(value, Integer.MAX_VALUE, dm, resFinder));
                break;
            case "min" :
                if (isApi26()) {
                    pb.setMin(parseInteger(value, 0));
                }
                break;
            case "minHeight" :
                pb.setMinHeight(parseDimension(value, 0, dm, resFinder));
                break;
            case "minWidth" :
                pb.setMinWidth(parseDimension(value, 0, dm, resFinder));
                break;
            case "progress" :
                pb.setProgress(parseInteger(value, 50));
                break;
            case "progressBackgroundTint" :
                // TODO Parse color state list
                break;
            case "progressBackgroundTintMode" :
                pb.setProgressBackgroundTintMode(parsePorterDuffMode(value));
                break;
            case "progressDrawable" :
                pb.setProgressDrawable(parseDrawable(value, resFinder, context));
                break;
            case "progressTint" :
                // TODO Parse color state list
                break;
            case "progressTintMode" :
                pb.setProgressTintMode(parsePorterDuffMode(value));
                break;
            case "secondaryProgress" :
                pb.setSecondaryProgress(parseInteger(value, 0));
                break;
            case "secondaryProgressTint" :
                // TODO Parse color state list
                break;
            case "secondaryProgressTintMode" :
                pb.setSecondaryProgressTintMode(parsePorterDuffMode(value));
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
