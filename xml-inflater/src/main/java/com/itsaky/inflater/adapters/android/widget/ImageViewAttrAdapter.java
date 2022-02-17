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
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.adapters.android.view.ViewAttrAdapter;

/**
 * Attribute handler for handling attributes related to
 * ImageButton.
 *
 * @author Akash Yadav
 */
public class ImageViewAttrAdapter extends ViewAttrAdapter {
    
    public ImageViewAttrAdapter (@NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
        super (resourceFinder, displayMetrics);
    }
    
    @Override
    public boolean isApplicableTo (View view) {
        return view instanceof ImageView;
    }
    
    @Override
    public boolean apply (@NonNull IAttribute attribute, @NonNull View view) {
        final ImageView image = (ImageView) view;
        final Context context = image.getContext ();
        final DisplayMetrics dm = context.getResources ().getDisplayMetrics ();
        final String name = attribute.getAttributeName ();
        final String value = attribute.getValue ();
        
        if (!canHandleNamespace (attribute)) {
            return false;
        }
        
        boolean handled = true;
        
        switch (name) {
            case "adjustViewBounds":
                image.setAdjustViewBounds (parseBoolean (value));
                break;
            case "baseline":
                image.setBaseline (parseDimension (value, 0, dm));
                break;
            case "baselineAlignBottom":
                image.setBaselineAlignBottom (parseBoolean (value));
                break;
            case "cropToPadding":
                image.setCropToPadding (parseBoolean (value));
                break;
            case "maxHeight":
                image.setMaxHeight (parseDimension (value, 0, dm));
                break;
            case "maxWidth":
                image.setMaxWidth (parseDimension (value, 0, dm));
                break;
            case "scaleType":
                image.setScaleType (parseScaleType (value));
                break;
            case "src":
                image.setImageDrawable (parseImageResource (value, context));
                break;
            case "tint":
                image.setImageTintList (parseColorStateList (value, context));
                break;
            case "tintMode":
                image.setImageTintMode (parsePorterDuffMode (value));
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
    
    private Drawable parseImageResource (String value, Context context) {
        if (value.startsWith ("@drawable/") || value.startsWith ("@mipmap/")) {
            return parseDrawable (value, context);
        }
        return ContextCompat.getDrawable (context, android.R.drawable.ic_delete);
    }
    
    private ImageView.ScaleType parseScaleType (String value) {
        switch (value) {
            case "center":
                return ImageView.ScaleType.CENTER;
            case "centerCrop":
                return ImageView.ScaleType.CENTER_CROP;
            case "centerInside":
                return ImageView.ScaleType.CENTER_INSIDE;
            case "fitEnd":
                return ImageView.ScaleType.FIT_END;
            case "fitStart":
                return ImageView.ScaleType.FIT_START;
            case "fitXY":
                return ImageView.ScaleType.FIT_XY;
            case "matrix":
                return ImageView.ScaleType.MATRIX;
            case "fitCenter":
            default:
                return ImageView.ScaleType.FIT_CENTER;
        }
    }
}
