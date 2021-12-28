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

package com.itsaky.androidide.colorpicker;

import static android.graphics.Color.*;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.itsaky.androidide.common.R;
import com.itsaky.androidide.common.databinding.LayoutColorPickerBinding;

/**
 * A color picker dialog.
 *
 * @author Akash Yadav
 */
public class ColorPickerView extends LinearLayout {
    
    private int alpha;
    private int red;
    private int green;
    private int blue;
    
    private final LayoutColorPickerBinding binding;
    
    private final int DEFAULT_COLOR;
    
    public ColorPickerView (Context context) {
        this (context, null);
    }
    
    public ColorPickerView (Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }
    
    public ColorPickerView (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this (context, attrs, defStyleAttr, 0);
    }
    
    public ColorPickerView (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super (context, attrs, defStyleAttr, defStyleRes);
        
        this.DEFAULT_COLOR = ContextCompat.getColor (context, R.color.color_picker_default_color);
        
        this.binding = LayoutColorPickerBinding.inflate (LayoutInflater.from (context));
        
        init ();
    }
    
    private void init () {
        final var color = this.DEFAULT_COLOR;
        this.alpha = alpha (color);
        this.red = red (color);
        this.green = green(color);
        this.blue = blue (color);
        
        this.binding.alphaSlider.setValue (alpha);
        this.binding.redSlider.setValue (red);
        this.binding.greenSlider.setValue (green);
        this.binding.blueSlider.setValue (blue);
        
        this.binding.colorPreview.setBackgroundColor (color);
    }
}
