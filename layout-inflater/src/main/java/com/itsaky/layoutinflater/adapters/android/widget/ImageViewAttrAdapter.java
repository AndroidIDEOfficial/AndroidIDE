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
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IResourceFinder;
import com.itsaky.layoutinflater.adapters.android.view.ViewAttrAdapter;

/**
 * Attribute handler for handling attributes related to
 * ImageButton.
 *
 * @author Akash Yadav
 */
public class ImageViewAttrAdapter extends ViewAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof ImageView;
    }

    @Override
    public boolean apply(IAttribute attribute, View view, IResourceFinder resFinder) {
        final ImageView image = (ImageView) view;
        final Context context = image.getContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        final String namespace = attribute.getNamespace();
        final String name = attribute.getAttributeName();
        final String value = attribute.getValue();

        if (!canHandleNamespace(namespace)) {
            return false;
        }

        boolean handled = true;

        switch (name) {
            case "adjustViewBounds" :
                image.setAdjustViewBounds(parseBoolean(value, resFinder));
                break;
            case "baseline" :
                image.setBaseline(parseDimension(value, 0, dm, resFinder));
                break;
            case "baselineAlignBottom" :
                image.setBaselineAlignBottom(parseBoolean(value, resFinder));
                break;
            case "cropToPadding" :
                image.setCropToPadding(parseBoolean(value, resFinder));
                break;
            case "maxHeight" :
                image.setMaxHeight(parseDimension(value, 0, dm, resFinder));
                break;
            case "maxWidth" :
                image.setMaxWidth(parseDimension(value, 0, dm, resFinder));
                break;
            case "scaleType" :
                image.setScaleType(parseScaleType(value));
                break;
            case "src" :
                image.setImageDrawable(parseImageResource(value, resFinder, context));
                break;
            case "tint" :
                // TODO Parse color state list
                break;
            case "tintMode" :
                image.setImageTintMode(parsePorterDuffMode(value));
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

    private Drawable parseImageResource(String value, IResourceFinder resFinder, Context context) {
        if (value.startsWith("@drawable/") || value.startsWith("@mipmap/")) {
            return parseDrawable(value, resFinder, context);
        }
        return ContextCompat.getDrawable(context, android.R.drawable.ic_delete);
    }

    private ImageView.ScaleType parseScaleType(String value) {
        switch (value) {
            case "center" :
                return ImageView.ScaleType.CENTER;
            case "centerCrop" :
                return ImageView.ScaleType.CENTER_CROP;
            case "centerInside" :
                return ImageView.ScaleType.CENTER_INSIDE;
            case "fitEnd" :
                return ImageView.ScaleType.FIT_END;
            case "fitStart" :
                return ImageView.ScaleType.FIT_START;
            case "fitXY" :
                return ImageView.ScaleType.FIT_XY;
            case "matrix" :
                return ImageView.ScaleType.MATRIX;
            case "fitCenter" :
            default :
                return ImageView.ScaleType.FIT_CENTER;
        }
    }
}
