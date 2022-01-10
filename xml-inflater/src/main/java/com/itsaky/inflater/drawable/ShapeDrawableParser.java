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

package com.itsaky.inflater.drawable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.inflater.IResourceTable;

import org.jetbrains.annotations.Contract;
import org.xmlpull.v1.XmlPullParser;

import java.util.Arrays;

/**
 * Parses a drawable whose root element is
 * {@code <shape>}. If the parse was successful, returns a {@link GradientDrawable},
 * otherwise {@code null}.
 *
 * @author Akash Yadav
 */
public class ShapeDrawableParser extends IDrawableParser {
    
    protected ShapeDrawableParser (XmlPullParser parser, IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
        super (parser, resourceFinder, displayMetrics);
    }
    
    @Nullable
    @Override
    public Drawable parse () throws Exception {
    
        final var drawable = new GradientDrawable ();
        
        var index = attrIndex ("shape");
        drawable.setShape (index == -1 ? GradientDrawable.RECTANGLE : parseShape (this.parser.getAttributeValue (index)));
        
        if (isApi29 ()) {
            index = attrIndex ("innerRadius");
            drawable.setInnerRadius (index == -1 ? 0 : parseDimension (value (index), 0));
            
            index = attrIndex ("innerRadiusRatio");
            drawable.setInnerRadiusRatio (index == -1 ? 1f : Float.parseFloat (value (index)));
            
            index = attrIndex ("thickness");
            drawable.setThickness (index == -1 ? 0 : parseDimension (value (index), 0));
    
            index = attrIndex ("thicknessRatio");
            drawable.setThicknessRatio (index == -1 ? 1f : Float.parseFloat (value (index)));
        }
        
        index = attrIndex ("useLevel");
        drawable.setUseLevel (index != -1 && parseBoolean (value (index)));
        
        var event = parser.getEventType ();
        while (event != XmlPullParser.END_DOCUMENT) {
            final var name = parser.getName ();
            if (event == XmlPullParser.START_TAG) {
                switch (name) {
                    case "corners":
                        parseCorners (drawable);
                        break;
                    case "gradient":
                        parseGradient (drawable);
                        break;
                    case "padding" :
                        parsePadding (drawable);
                        break;
                    case "size" :
                        parseSize (drawable);
                        break;
                    case "solid" :
                        parseSolid (drawable);
                        break;
                    case "stroke" :
                        parseStroke (drawable);
                        break;
                }
            }
            event = parser.next ();
        }
        
        return drawable;
    }
    
    private void parseStroke (@NonNull final GradientDrawable drawable) {
        var index = attrIndex ("width");
        var strokeWidth = 0;
        if (index != - 1) {
            drawable.setStroke (strokeWidth = parseDimension (value (index), 0), Color.TRANSPARENT);
        }
        
        index = attrIndex ("color");
        var strokeColor = Color.TRANSPARENT;
        if (index != -1) {
            drawable.setStroke (strokeWidth, strokeColor = parseColor (value (index), appContext ()));
        }
        
        index = attrIndex ("dashWidth");
        var dashWidth = 0;
        if (index != -1) {
            drawable.setStroke (strokeWidth, strokeColor, dashWidth = parseDimension (value (index), 0), 0);
        }
        
        index = attrIndex ("dashGap");
        if (index != -1) {
            drawable.setStroke (strokeWidth, strokeColor, dashWidth, parseDimension (value (index), 0));
        }
    }
    
    private void parseSolid (@NonNull final GradientDrawable drawable) {
        var index = attrIndex ("color");
        if (index != -1) {
            drawable.setColor (parseColor (value (index), appContext ()));
        }
    }
    
    private void parseSize (@NonNull final GradientDrawable drawable) {
        var index = attrIndex ("width");
        if (index != -1) {
            drawable.setSize (parseDimension (value (index), 0), drawable.getIntrinsicHeight ());
        }
        
        index = attrIndex ("height");
        if (index != -1) {
            drawable.setSize (drawable.getIntrinsicWidth (), parseDimension (value (index), 0));
        }
    }
    
    private void parsePadding (@NonNull final GradientDrawable drawable) {
        
        // Padding is available from API 29 only
        if (!isApi29 ()) {
            return;
        }
        
        final var rect = new Rect ();
        if (!drawable.getPadding (rect)) {
            rect.set (0,0,0,0);
        }
        
        var changed = false;
        var index = attrIndex ("left");
        if (index != -1) {
            rect.left = parseDimension (value (index), 0);
            changed = true;
        }
    
        index = attrIndex ("top");
        if (index != -1) {
            rect.top = parseDimension (value (index), 0);
            changed = true;
        }
    
        index = attrIndex ("right");
        if (index != -1) {
            rect.right = parseDimension (value (index), 0);
            changed = true;
        }
    
        index = attrIndex ("bottom");
        if (index != -1) {
            rect.bottom = parseDimension (value (index), 0);
            changed = true;
        }
        
        if (changed) {
            drawable.setPadding (rect.left, rect.top, rect.right, rect.bottom);
        }
    }
    
    private void parseGradient (@NonNull final GradientDrawable drawable) {
        var index = attrIndex ("angle");
        drawable.setOrientation (index == -1 ? GradientDrawable.Orientation.LEFT_RIGHT : parseGradientOrientation (value (index)));
        
        index = attrIndex ("centerX");
        var center = index == - 1 ? drawable.getGradientCenterX () : Float.parseFloat (value (index));
        drawable.setGradientCenter (center, drawable.getGradientCenterY ());
    
        index = attrIndex ("centerY");
        center = index == - 1 ? drawable.getGradientCenterX () : Float.parseFloat (value (index));
        drawable.setGradientCenter (drawable.getGradientCenterX (), center);
        
        final var colors = new int [3];
        var changed = false;
        Arrays.fill (colors, -1);
        
        index = attrIndex ("centerColor");
        if (index != -1) {
            colors [1] = parseColor (value (index), appContext ());
            changed = true;
        }
        
        index = attrIndex ("endColor");
        if (index != -1) {
            colors [2] = parseColor (value (index), appContext ());
            changed = true;
        }
        
        index = attrIndex ("gradientRadius");
        drawable.setGradientRadius (index == -1 ? drawable.getGradientRadius () : Float.parseFloat (value (index)));
        
        index = attrIndex ("startColor");
        if (index != -1) {
            colors[0] = parseColor (value (index), appContext ());
            changed = true;
        }
        
        if (changed) {
            drawable.setColors (colors);
        }
        
        index = attrIndex ("type");
        drawable.setGradientType (index == -1 ? GradientDrawable.LINEAR_GRADIENT : parseGradientType (value (index)));
        
        index = attrIndex ("useLevel");
        drawable.setUseLevel (index != -1 && parseBoolean (value (index)));
    }
    
    private int parseGradientType (String value) {
        switch (value) {
            case "radial" :
                return GradientDrawable.RADIAL_GRADIENT;
            case "sweep" :
                return GradientDrawable.SWEEP_GRADIENT;
            case "linear" :
            default:
                return GradientDrawable.LINEAR_GRADIENT;
        }
    }
    
    private GradientDrawable.Orientation parseGradientOrientation (String value) {
        final var angle = Integer.parseInt (value);
        
        // Angle must be between 0-315 and a multiple of 45
        // Angle moves in anti-clockwise direction
        if (angle == 45) {
            return GradientDrawable.Orientation.BL_TR;
        }
        
        if (angle == 90) {
            return GradientDrawable.Orientation.BOTTOM_TOP;
        }
        
        if (angle == 135) {
            return GradientDrawable.Orientation.BR_TL;
        }
        
        if (angle == 180) {
            return GradientDrawable.Orientation.RIGHT_LEFT;
        }
        
        if (angle == 225) {
            return GradientDrawable.Orientation.TR_BL;
        }
        
        if (angle == 270) {
            return GradientDrawable.Orientation.TOP_BOTTOM;
        }
        
        if (angle == 315) {
            return GradientDrawable.Orientation.TL_BR;
        }
        
        // Angle 0 or any invalid valid should make the orientation left to right
        return GradientDrawable.Orientation.LEFT_RIGHT;
    }
    
    private void parseCorners (@NonNull GradientDrawable drawable) {
        int index;
        index = attrIndex ("radius");
        drawable.setCornerRadius (index == -1 ? 0f : parseDimension (value (index), 0));
        
        var changed = false;
        final var radii = new float[8];
        Arrays.fill (radii, 0f);
        
        index = attrIndex ("topLeftRadius");
        if (index != -1) {
            radii[0] = radii[1] = parseDimension (value (index), 0);
            changed = true;
        }
        
        index = attrIndex ("topRightRadius");
        if (index != -1) {
            radii[2] = radii[3] = parseDimension (value (index), 0);
            changed = true;
    
        }
        
        index = attrIndex ("bottomLeftRadius");
        if (index != -1) {
            radii[4] = radii[5] = parseDimension (value (index), 0);
            changed = true;
    
        }
        
        index = attrIndex ("bottomRightRadius");
        if (index != -1) {
            radii[6] = radii[7] = parseDimension (value (index), 0);
            changed = true;
        }
        
        if (changed) {
            drawable.setCornerRadii (radii);
        }
    }
    
    @Contract(pure = true)
    private int parseShape (@NonNull String value) {
        switch (value) {
            case "oval" :
                return GradientDrawable.OVAL;
            case "line":
                return GradientDrawable.LINE;
            case "ring" :
                return GradientDrawable.RING;
            case "rectangle" :
            default:
                return GradientDrawable.RECTANGLE;
        }
    }
    
    protected Context appContext () {
        return BaseApplication.getBaseInstance ();
    }
}
