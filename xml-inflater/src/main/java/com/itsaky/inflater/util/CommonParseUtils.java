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

package com.itsaky.inflater.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.blankj.utilcode.util.ImageUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.inflater.IResourceFinder;
import com.itsaky.inflater.drawable.DrawableParserFactory;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * Contains methods which are commonly used by layout inflater,
 * attribute adapters and drawable parsers.
 *
 * @author Akash Yadav
 */
public class CommonParseUtils {
    
    private static final Pattern HEX_COLOR = Pattern.compile("#[a-fA-F0-9]{6,8}");
    
    protected IResourceFinder resourceFinder;
    protected final DisplayMetrics displayMetrics;
    
    private static final Logger LOG = Logger.instance ("CommonParseUtils");
    
    public CommonParseUtils (@NonNull IResourceFinder resourceFinder, DisplayMetrics displayMetrics) {
        this.resourceFinder = resourceFinder;
        this.displayMetrics = displayMetrics;
    }
    
    protected boolean parseBoolean(@NonNull String value) {
        if (!value.startsWith("@")) {
            if ("true".equals(value)) {
                return true;
            } else if ("false".equals(value)) {
                return false;
            }
        } else {
            // TODO Find resource value in booleans.xml
        }
        return false;
    }
    
    protected int parseDimension(final String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        
        char c = value.charAt(0);
        if (Character.isDigit(c)) {
            // A dimension value which starts with a digit. E.g.: 1dp, 12sp, 123px, etc.
            String dimensionVal = "";
            int index = 0;
            while (Character.isDigit(c = value.charAt(index))) {
                dimensionVal += c;
                index ++;
            }
            
            final int dimen = Integer.parseInt(dimensionVal);
            final String dimensionType = value.substring(index);
            return (int) TypedValue.applyDimension(getUnitForDimensionType(dimensionType), dimen, displayMetrics);
        } else if (c == '@') {
            String name = value.substring("@dimen/".length());
            return parseDimension(resourceFinder.findDimension(name), defaultValue);
        } else if (Character.isLetter(c)) {
            // This could be one of the following :
            // 1. match_parent
            // 2. wrap_content
            // 3. fill_parent
            switch (value) {
                case "match_parent" :
                case "fill_parent"  :
                    return ViewGroup.LayoutParams.MATCH_PARENT;
                case "wrap_content" :
                default :
                    return ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }
        
        return defaultValue;
    }
    
    protected int getUnitForDimensionType(@NonNull String dimensionType) {
        switch (dimensionType) {
            case "dp" :
                return TypedValue.COMPLEX_UNIT_DIP;
            case "sp" :
                return TypedValue.COMPLEX_UNIT_SP;
            case "px" :
                return TypedValue.COMPLEX_UNIT_PX;
            case "pt" :
                return TypedValue.COMPLEX_UNIT_PT;
            case "in" :
                return TypedValue.COMPLEX_UNIT_IN;
            case "mm" :
                return TypedValue.COMPLEX_UNIT_MM;
        }
        return TypedValue.COMPLEX_UNIT_DIP;
    }
    
    protected int parseColor (String color, final Context ctx) {
        if (HEX_COLOR.matcher(color).matches()) {
            try {
                return Color.parseColor(color);
            } catch (Throwable th) {
                // Ignored
            }
        } else if (color.startsWith("@color/")) {
            return parseColor(resourceFinder.findColor(color.substring("@color/".length())), ctx);
        } else if (color.startsWith("@android:color/")) {
            final int id = findAndroidResId("color", color.substring("@android:color/".length()));
            return ContextCompat.getColor(ctx, id);
        }
        
        return Color.parseColor("#00ffffff");
    }
    
    protected Drawable parseDrawable (String value, final Context ctx) {
        if (HEX_COLOR.matcher(value).matches()) {
            return drawableForColor(value);
        } else if (value.startsWith("@")) {
            
            // First check if this is a reference to an android resource
            if (value.startsWith("@android:")) {
                final String typeAndValue = value.substring("@android:".length());
                final String[] split = typeAndValue.split(Pattern.quote("/")); // For @android:color/white, it will be ["color", "white"]
                final String type = split[0];
                final String typeVal = split[1];
                final int id = findAndroidResId (type, typeVal);
                
                if (id != -1) {
                    switch (type) {
                        case "color" :
                            return drawableForColor(ContextCompat.getColor(ctx, id));
                        case "drawable" :
                            return ContextCompat.getDrawable(ctx, id);
                        default :
                            return newTransparentDrawable();
                    }
                }
                
            } else {
                // We found a reference to another resource
                if (value.startsWith("@drawable/")) {
                    final File drawable = resourceFinder.findDrawable (value.substring("@drawable/".length()));
                    
                    if (drawable == null) {
                        return null;
                    }
    
                    try {
                        final var parser = DrawableParserFactory.newParser (ctx, drawable, resourceFinder);
                        if (parser == null) {
                            return null;
                        }
                        
                        return parser.parse();
                    } catch (Exception e) {
                        LOG.error ("Error parsing drawable", e);
                        return null;
                    }
                } else if (value.startsWith("@color/")) {
                    final String color = resourceFinder.findColor(value.substring("@color/".length()));
                    // TODO Check if this color resource is a selector
                    return parseDrawable(color, ctx);
                }
            }
        }
        return newTransparentDrawable();
    }
    
    private int findAndroidResId(String type, String name) {
        try {
            final Class<?> typeClass = android.R.class.getClassLoader().loadClass("android.R$" + type);
            final Field typeField = typeClass.getDeclaredField(name);
            typeField.setAccessible(true);
            return (int) typeField.get(null);
        } catch (Throwable th) {
            return -1;
        }
    }
    
    protected Drawable drawableForColor (String color) {
        try {
            return drawableForColor(Color.parseColor(color));
        } catch (Throwable th) {
            return newTransparentDrawable();
        }
    }
    
    protected Drawable drawableForColor (int color) {
        return new ColorDrawable (color);
    }
    
    protected Drawable newTransparentDrawable () {
        return new ColorDrawable (Color.TRANSPARENT);
    }
    
    protected int parseGravity (@NonNull String value) {
        final String[] splits = value.split(Pattern.quote("|"));
        int result = -1;
        for (String split : splits) {
            final int gravity = gravityFor(split);
            if (result == -1) {
                result = gravity;
            } else {
                result |= gravity;
            }
        }
        return result;
    }
    
    protected int gravityFor(@NonNull String gravity) {
        switch (gravity) {
            case "center" :
                return Gravity.CENTER;
            case "center_vertical" :
                return Gravity.CENTER_VERTICAL;
            case "center_horizontal" :
                return Gravity.CENTER_HORIZONTAL;
            case "left" :
                return Gravity.LEFT;
            case "right" :
                return Gravity.RIGHT;
            case "top" :
                return Gravity.TOP;
            case "bottom" :
                return Gravity.BOTTOM;
            case "start" :
                return GravityCompat.START;
            case "end" :
                return GravityCompat.END;
            default :
                return Gravity.TOP|Gravity.START;
        }
    }
}
