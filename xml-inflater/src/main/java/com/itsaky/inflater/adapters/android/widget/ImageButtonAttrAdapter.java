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
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;

/**
 * Attribute handler for handling attributes related to
 * ImageButton.
 *
 * @author Akash Yadav
 */
public class ImageButtonAttrAdapter extends ImageViewAttrAdapter {
    
    public ImageButtonAttrAdapter (@NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
        super (resourceFinder, displayMetrics);
    }
    
    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof ImageButton;
    }

    @Override
    public boolean apply(@NonNull IAttribute attribute, @NonNull View view) {
        // No special attributes for ImageButton
        return super.apply(attribute, view);
    }
}
