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
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.adapters.android.view.ViewAttrAdapter;
import java.util.regex.Pattern;

/**
 * Adapter for handling attributes related to TextView.
 *
 * @author Akash Yadav
 */
public class TextViewAttrAdapter extends ViewAttrAdapter {
    
    public TextViewAttrAdapter (@NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
        super (resourceFinder, displayMetrics);
    }
    
    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof TextView;
    }

    @Override
    public boolean apply(IAttribute attribute, View view) {
        final TextView text = (TextView) view;
        final String namespace = attribute.getNamespace();
        final String name = attribute.getAttributeName();
        final String value = attribute.getValue();
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        final Resources res = view.getResources();
        final DisplayMetrics dm = res.getDisplayMetrics();
        final Context ctx = view.getContext();
        final Drawable[] drawables = text.getCompoundDrawables();
        final Drawable[] drawablesRelative = text.getCompoundDrawablesRelative();
        
        if (!canHandleNamespace(namespace)) {
            return false;
        }
        
        boolean handled = true;
        switch (name) {
            case "autoLink" :
                text.setAutoLinkMask(parseAutoLinkMask(value));
                break;
            case "drawableLeft" :
                text.setCompoundDrawables(parseDrawable(value, ctx), drawables[1], drawables[2], drawables[3]);
                break;
            case "drawableTop" :
                text.setCompoundDrawables(drawables[0], parseDrawable(value, ctx), drawables[2], drawables[3]);
                break;
            case "drawableRight" :
                text.setCompoundDrawables(drawables[0], drawables[1], parseDrawable(value, ctx), drawables[3]);
                break;
            case "drawableBottom" :
                text.setCompoundDrawables(drawables[0], drawables[1], drawables[2], parseDrawable(value, ctx));
                break;
            case "drawableStart" :
                text.setCompoundDrawables(parseDrawable(value, ctx), drawablesRelative[1], drawablesRelative[2], drawablesRelative[3]);
                break;
            case "drawableEnd" :
                text.setCompoundDrawables(drawablesRelative[0], drawablesRelative[1], parseDrawable(value, ctx), drawablesRelative[3]);
                break;
            case "drawablePadding" :
                text.setCompoundDrawablePadding(parseDimension(value, 0, dm));
                break;
            case "ellipsize" :
                text.setEllipsize(parseEllipsize(value));
                break;
            case "gravity" :
                text.setGravity(parseGravity(value));
                break;
            case "hint" :
                text.setHint(parseString(value));
                break;
            case "letterSpacing" :
                text.setLetterSpacing(parseFloat(value));
                break;
            case "lineHeight" :
                text.setLines(parseInteger(value, Integer.MAX_VALUE));
                break;
            case "linksClickable" :
                text.setLinksClickable(parseBoolean(value));
                break;
            case "marqueeRepeatLimit" :
                text.setMarqueeRepeatLimit(parseInteger(value, Integer.MAX_VALUE));
                break;
            case "maxLines" :
                text.setMaxLines(parseInteger(value, Integer.MAX_VALUE));
                break;
            case "minLines" :
                text.setMinLines(parseInteger(value, 1));
                break;
            case "singleLine" :
                text.setSingleLine(parseBoolean(value));
                break;
            case "text" :
                text.setText(parseString(value));
                break;
            case "textAllCaps" :
                text.setAllCaps(parseBoolean(value));
                break;
            case "textColor" :
                text.setTextColor(parseColor(value, ctx));
                break;
            case "textColorHint" :
                text.setHintTextColor(parseColor(value, ctx));
                break;
            case "textSize" :
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, parseDimension(value, SizeUtils.sp2px(14), dm));
                break;
            case "textStyle" :
                text.setTypeface(text.getTypeface(), parseTextStyle(value));
                break;
            case "typeface" :
                text.setTypeface(parseTypeface(value));
                break;
            default :
                handled = false;
                break;
        }
        
        if (!handled) {
            handled = super.apply(attribute, view);
        }

        return handled;
    }

    protected int parseTextStyle(@NonNull String value) {
        final String[] splits = value.split(Pattern.quote("|"));
        int mask = 0;
        for (String split : splits) {
            mask |= textStyleFor(split);
        }
        return mask;
    }

    protected int textStyleFor(@NonNull String split) {
        switch (split) {
            case "bold" :
                return Typeface.BOLD;
            case "italic" :
                return Typeface.ITALIC;
            case "normal" :
            default :
                return Typeface.NORMAL;
        }
    }

    protected Typeface parseTypeface(@NonNull String value) {
        switch (value) {
            case "sans" :
                return Typeface.SANS_SERIF;
            case "serif" :
                return Typeface.SERIF;
            case "monospace" :
                return Typeface.MONOSPACE;
            case "normal" :
            default :
                return Typeface.DEFAULT;
        }
    }
    
    protected TextUtils.TruncateAt parseEllipsize(@NonNull String value) {
        switch (value) {
            case "end" :
                return TextUtils.TruncateAt.END;
            case "start" :
                return TextUtils.TruncateAt.START;
            case "marquee" :
                return TextUtils.TruncateAt.MARQUEE;
            case "middle" :
                return TextUtils.TruncateAt.MIDDLE;
            case "none" :
            default :
                return null;
        }
    }

    protected int parseAutoLinkMask(@NonNull String value) {
        final String[] splits = value.split(Pattern.quote("|"));
        int mask = 0;
        for (String split : splits) {
            mask |= autoLinkMaskFor (split);
        }
        return mask;
    }

    protected int autoLinkMaskFor(@NonNull String mask) {
        switch (mask) {
            case "all" :
                return Linkify.ALL;
            case "web" :
                return Linkify.WEB_URLS;
            case "phone" :
                return Linkify.PHONE_NUMBERS;
            case "map" :
                return Linkify.MAP_ADDRESSES;
            case "email" :
                return Linkify.EMAIL_ADDRESSES;
            case "none" :
            default :
            return 0;
        }
    }
}
