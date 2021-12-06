/************************************************************************************
 * This file is part of AndroidIDE
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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import com.itsaky.androidide.ui.inflater.IResourceFinder;
import com.itsaky.androidide.ui.view.IAttribute;
import com.itsaky.androidide.ui.view.IAttributeAdapter;
import com.itsaky.androidide.utils.Logger;
import java.io.File;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * Handles attributes common to all android views
 */
public class BaseViewAttrAdapter implements IAttributeAdapter {
    
    private final Pattern HEX_COLOR = Pattern.compile("#[a-fA-F0-9]{6,8}");
    
    protected static final Logger LOG = Logger.instance ("BaseViewAttrAdapter");
    public static final int ENUM_NOT_FOUND = -4116;
    
    @Override
    public boolean isApplicableTo(View view) {
        return true; // Can be applied to any view
    }
    
    @Override
    public boolean apply(IAttribute attribute, View view, IResourceFinder resFinder) {
        final String namespace = attribute.getNamespace();
        final String name = attribute.getAttributeName();
        final String value = attribute.getValue();
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        final Resources res = view.getResources();
        final DisplayMetrics dm = res.getDisplayMetrics();
        final Context ctx = view.getContext();
        
        if (!canHandleNamespace(namespace)) {
            LOG.error("Cannot handle namespace: " + namespace);
            return false;
        }
        
        LOG.debug ("Adding attribute", attribute);
        
        boolean handled = true;
        switch (name) {
            case "layout_height" :
                params.height = parseDimension (value, -2, dm, resFinder);
                LOG.error("Added layout_height", params.height);
                break;
            case "layout_width"  :
                params.width  = parseDimension (value, -2, dm, resFinder);
                LOG.error("Added layout_width", params.width);
                break;
            case "alpha" :
                view.setAlpha(parseFloat(value));
                break;
            case "background" :
                view.setBackground(parseDrawable(value, resFinder, ctx));
                break;
            case "backgroundTint" :
                // TODO Parse color state list (<selector>) and apply to this view
                break;
            case "backgroundTintMode" :
                view.setBackgroundTintMode(toPorterDuffMode(value));
                break;
            case "clipToOutline" :
                view.setClipToOutline(parseBoolean(value, resFinder));
                break;
            case "contentDescription" :
                view.setContentDescription(parseString (value, resFinder));
                break;
            case "contextClickable" :
                view.setContextClickable(parseBoolean(value, resFinder));
                break;
            case "defaultFocusHighlightEnabled" :
                if(Build.VERSION.SDK_INT >= 26) {
                    view.setDefaultFocusHighlightEnabled(parseBoolean(value, resFinder));
                }
                break;
            case "drawingCacheQuality" :
                view.setDrawingCacheQuality(parseDrawingCacheQuality(value));
                break;
            case "duplicateParentState" :
                view.setDuplicateParentStateEnabled(parseBoolean(value, resFinder));
                break;
            case "elevation" :
                view.setElevation(parseDimension(value, 0, dm, resFinder));
                break;
            case "fadeScrollbars" :
                view.setScrollbarFadingEnabled(parseBoolean(value, resFinder));
                break;
            case "fadingEdgeLength" :
                view.setFadingEdgeLength(parseDimension(value, 0, dm, resFinder));
                break;
            case "filterTouchesWhenObscured" :
                view.setFilterTouchesWhenObscured(parseBoolean(value, resFinder));
                break;
            case "foreground" :
                view.setForeground(parseDrawable(value, resFinder, ctx));
                break;
            case "foregroundGravity" :
                view.setForegroundGravity(parseGravity(value));
                break;
            case "foregroundTint" :
                // TODO Parse color state list (<selector>) and apply to this view
                break;
            case "foregroundTintMode" :
                view.setForegroundTintMode(toPorterDuffMode(value));
                break;
            case "minHeight" :
                view.setMinimumHeight(parseDimension(value, 0, dm, resFinder));
                break;
            case "minWidth" :
                view.setMinimumWidth(parseDimension(value, 0, dm, resFinder));
                break;
            case "padding" :
                final int padding = parseDimension(value, 0, dm, resFinder);
                view.setPaddingRelative(padding, padding, padding, padding);
                break;
            case "paddingLeft" :
                final int paddingLeft = parseDimension(value, 0, dm, resFinder);
                view.setPadding(paddingLeft, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
                break;
            case "paddingTop" :
                final int paddingTop = parseDimension(value, 0, dm, resFinder);
                view.setPadding(view.getPaddingLeft(), paddingTop, view.getPaddingRight(), view.getPaddingBottom());
                break;
            case "paddingRight" :
                final int paddingRight = parseDimension(value, 0, dm, resFinder);
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), paddingRight, view.getPaddingBottom());
                break;
            case "paddingBottom" :
                final int paddingBottom = parseDimension(value, 0, dm, resFinder);
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), paddingBottom);
                break;
            case "paddingStart" :
                final int paddingStart = parseDimension(value, 0, dm, resFinder);
                view.setPaddingRelative(paddingStart, view.getPaddingTop(), view.getPaddingEnd(), view.getPaddingBottom());
                break;
            case "paddingEnd" :
                final int paddingEnd = parseDimension(value, 0, dm, resFinder);
                view.setPaddingRelative(view.getPaddingStart(), view.getPaddingTop(), paddingEnd, view.getPaddingBottom());
                break;
            case "rotation" :
                view.setRotation(parseFloat(value));
                break;
            case "rotationX" :
                view.setRotationX(parseFloat(value));
                break;
            case "rotationY" :
                view.setRotationY(parseFloat(value));
                break;
            case "scaleX" :
                view.setScaleX(parseFloat(value));
                break;
            case "scaleY" :
                view.setScaleY(parseFloat(value));
                break;
            case "scrollX" :
                view.setScrollY((int) parseFloat(value));
                break;
            case "scrollY" :
                view.setScrollY((int) parseFloat(value));
                break;
            case "textAlignment" :
                view.setTextAlignment(parseTextAlignment(value));
                break;
            case "textDirection" :
                view.setTextDirection(parseTextDirection(value));
                break;
            case "tooltipText" :
                if (isAndroid8()) {
                    view.setTooltipText(parseString(value, resFinder));
                }
                break;
            case "transformPivotX" :
                view.setPivotX(parseFloat(value));
                break;
            case "transformPivotY" :
                view.setPivotY(parseFloat(value));
                break;
            case "translationX" :
                view.setTranslationX(parseFloat(value));
                break;
            case "translationY" :
                view.setTranslationY(parseFloat(value));
                break;
            case "translationZ" :
                view.setTranslationZ(parseFloat(value));
                break;
            case "visibility" :
                view.setVisibility(parseVisibility(value));
                break;
            default :
                handled = false;
                break;
        }
        
        // ----- Handle attributes related to parent ------
        if (!handled) {
            
            // Layout Margins
            if (params instanceof ViewGroup.MarginLayoutParams) {
                handleMarginParams((ViewGroup.MarginLayoutParams) params, name, value, dm, resFinder);
            }
        }
        
        view.setLayoutParams(params);
        
        return handled;
    }

    private void handleMarginParams(ViewGroup.MarginLayoutParams params, String name, String value, DisplayMetrics dm, IResourceFinder resFinder) {
        switch (name) {
            case "layout_margin" :
                final int margin = parseDimension(value, 0, dm, resFinder);
                params.setMargins(margin, margin, margin, margin);
                break;
            case "layout_marginLeft" :
                params.leftMargin = parseDimension(value, 0, dm, resFinder);
                break;
            case "layout_marginTop" :
                params.topMargin = parseDimension(value, 0, dm, resFinder);
                break;
            case "layout_marginRight" :
                params.rightMargin = parseDimension(value, 0, dm, resFinder);
                break;
            case "layout_marginBottom" :
                params.bottomMargin = parseDimension(value, 0, dm, resFinder);
                break;
            case "layout_marginStart" :
                params.setMarginStart(parseDimension(value, 0, dm, resFinder));
                break;
            case "layout_marginEnd" :
                params.setMarginEnd(parseDimension(value, 0, dm, resFinder));
                break;
        }
    }
    
    protected boolean canHandleNamespace(String namespace) {
        // TODO Compare namespace URI instead of name
        return "android".equals(namespace);
    }
    
    protected int parseInteger(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (Throwable th) {
            return defaultVal;
        }
    }

    protected int parseDrawingCacheQuality(String value) {
        switch (value) {
            case "high" :
                return View.DRAWING_CACHE_QUALITY_HIGH;
            case "low" :
                return View.DRAWING_CACHE_QUALITY_LOW;
            case "auto" :
            default :
                return View.DRAWING_CACHE_QUALITY_AUTO;
        }
    }

    protected int parseVisibility(String value) {
        switch (value) {
            case "gone" :
                return View.GONE;
            case "invisible" :
                return View.INVISIBLE;
            case "visible" :
            default :
                return View.VISIBLE;
        }
    }
    
    protected int parseGravity (String value) {
        final String[] splits = value.split(Pattern.quote("|"));
        int result = 0;
        for (String split : splits) {
            result |= gravityFor(split);
        }
        return result;
    }

    protected int gravityFor(String gravity) {
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
    
    protected int parseTextAlignment (String value) {
        switch (value) {
            case "center" :
                return View.TEXT_ALIGNMENT_CENTER;
            case "gravity" :
                return View.TEXT_ALIGNMENT_GRAVITY;
            case "textEnd" :
                return View.TEXT_ALIGNMENT_TEXT_END;
            case "textStart" :
                return View.TEXT_ALIGNMENT_TEXT_START;
            case "viewEnd" :
                return View.TEXT_ALIGNMENT_VIEW_END;
            case "viewStart" :
                return View.TEXT_ALIGNMENT_VIEW_START;
            case "inherit" :
            default :
                return View.TEXT_ALIGNMENT_INHERIT;
        }
    }
    
    protected int parseTextDirection (String value) {
        switch (value) {
            case "anyRtl" :
                return View.TEXT_DIRECTION_ANY_RTL;
            case "firstStrong" :
                return View.TEXT_DIRECTION_FIRST_STRONG;
            case "firstStrongLtr" :
                return View.TEXT_DIRECTION_FIRST_STRONG_LTR;
            case "firstStrongRtl" :
                return View.TEXT_DIRECTION_FIRST_STRONG_RTL;
            case "locale" :
                return View.TEXT_DIRECTION_LOCALE;
            case "ltr" :
                return View.TEXT_DIRECTION_LTR;
            case "rtl" :
                return View.TEXT_DIRECTION_RTL;
            case "inherit" :
            default :
                return View.TEXT_DIRECTION_INHERIT;
        }
    }

    protected String parseString(String value, IResourceFinder resFinder) {
        if (value.startsWith("@")) {
            return parseString(resFinder.findString(value.substring("@string/".length())), resFinder);
        }
        return value;
    }

    protected boolean parseBoolean(String value, IResourceFinder resFinder) {
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

    protected PorterDuff.Mode toPorterDuffMode(String mode) {
        switch (mode) {
            case "add" :
                return PorterDuff.Mode.ADD;
            case "multiply" :
                return PorterDuff.Mode.MULTIPLY;
            case "screen" :
                return PorterDuff.Mode.SCREEN;
            case "src_atop" :
                return PorterDuff.Mode.SRC_ATOP;
            case "src_in" :
                return PorterDuff.Mode.SRC_IN;
            case "src_over" :
                return PorterDuff.Mode.SRC_OVER;
            default :
                return PorterDuff.Mode.SRC;
        }
    }
    
    protected Drawable parseDrawable (String value, IResourceFinder resFinder, final Context ctx) {
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
                    final File drawable = resFinder.findDrawable(value.substring("@drawable/".length()));
                    // TODO Parse drawables
                } else if (value.startsWith("@color/")) {
                    final String color = resFinder.findColor(value.substring("@color/".length()));
                    // TODO Check if this color resource is a selector
                    return parseDrawable(color, resFinder, ctx);
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
    
    protected int parseColor (String color, IResourceFinder resFinder, final Context ctx) {
        if (HEX_COLOR.matcher(color).matches()) {
            try {
                return Color.parseColor(color);
            } catch (Throwable th) {
                // Ignored
            }
        } else if (color.startsWith("@color/")) {
            return parseColor(resFinder.findColor(color.substring("@color/".length())), resFinder, ctx);
        } else if (color.startsWith("@android:color/")) {
            final int id = findAndroidResId("color", color.substring("@android:color/".length()));
            return ContextCompat.getColor(ctx, id);
        }
        
        return Color.parseColor("#00ffffff");
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
    
    protected float parseFloat (String value) {
        try {
            return Float.parseFloat(value);
        } catch (Throwable th) {
            return 1f;
        }
    }

    protected int parseDimension(final String value, int defaultValue, final DisplayMetrics dm, IResourceFinder resFinder) {
        
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
            return (int) TypedValue.applyDimension(getUnitForDimensionType(dimensionType), dimen, dm);
        } else if (c == '@') {
            String name = value.substring("@dimen/".length());
            return parseDimension(resFinder.findDimension(name), defaultValue, dm, resFinder);
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

    protected int getUnitForDimensionType(String dimensionType) {
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
    
    protected boolean isAndroid8() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
    
    protected boolean isAndroid9() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }
    
    protected boolean isAndroid10() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }
    
    protected boolean isAndroid11() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }
}
