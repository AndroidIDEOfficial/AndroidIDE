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

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.adapters.android.view.ViewAttrAdapter;

/**
 * Attribute adapter for handling attributes related to
 * ProgressBar.
 *
 * @author Akash Yadav
 */
public class ProgressBarAttrAdapter extends ViewAttrAdapter {
    
    public ProgressBarAttrAdapter (@NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
        super (resourceFinder, displayMetrics);
    }
    
    @Override
    public boolean isApplicableTo (View view) {
        return view instanceof ProgressBar;
    }
    
    @Override
    public boolean apply (@NonNull IAttribute attribute, @NonNull View view) {
        final ProgressBar pb = (ProgressBar) view;
        final Context context = pb.getContext ();
        final DisplayMetrics dm = context.getResources ().getDisplayMetrics ();
        final String name = attribute.getAttributeName ();
        final String value = attribute.getValue ();
        
        if (!canHandleNamespace (attribute)) {
            return false;
        }
        
        boolean handled = true;
        
        switch (name) {
            case "indeterminate":
                pb.setIndeterminate (parseBoolean (value));
                break;
            case "indeterminateDrawable":
                pb.setIndeterminateDrawable (parseDrawable (value, context));
                break;
            case "indeterminateTint":
                pb.setIndeterminateTintList (parseColorStateList (value, context));
                break;
            case "indeterminateTintMode":
                pb.setIndeterminateTintMode (parsePorterDuffMode (value));
                break;
            case "max":
                pb.setMax (parseInteger (value, 100));
                break;
            case "maxHeight":
                if (isApi29 ()) {
                    pb.setMaxHeight (parseDimension (value, Integer.MAX_VALUE, dm));
                }
                break;
            case "maxWidth":
                if (isApi29 ()) {
                    pb.setMaxWidth (parseDimension (value, Integer.MAX_VALUE, dm));
                }
                break;
            case "min":
                if (isApi26 ()) {
                    pb.setMin (parseInteger (value, 0));
                }
                break;
            case "minHeight":
                if (isApi29 ()) {
                    pb.setMinHeight (parseDimension (value, 0, dm));
                }
                break;
            case "minWidth":
                if (isApi29 ()) {
                    pb.setMinWidth (parseDimension (value, 0, dm));
                }
                break;
            case "progress":
                pb.setProgress (parseInteger (value, 50));
                break;
            case "progressBackgroundTint":
                pb.setProgressBackgroundTintList (parseColorStateList (value, context));
                break;
            case "progressBackgroundTintMode":
                pb.setProgressBackgroundTintMode (parsePorterDuffMode (value));
                break;
            case "progressDrawable":
                pb.setProgressDrawable (parseDrawable (value, context));
                break;
            case "progressTint":
                pb.setProgressTintList (parseColorStateList (value, context));
                break;
            case "progressTintMode":
                pb.setProgressTintMode (parsePorterDuffMode (value));
                break;
            case "secondaryProgress":
                pb.setSecondaryProgress (parseInteger (value, 0));
                break;
            case "secondaryProgressTint":
                pb.setSecondaryProgressTintList (parseColorStateList (value, context));
                break;
            case "secondaryProgressTintMode":
                pb.setSecondaryProgressTintMode (parsePorterDuffMode (value));
                break;
            default:
                handled = false;
                break;
        }
        
        if (!handled) {
            handled = super.apply (attribute, view);
        }
        
        return handled;
    }
}
