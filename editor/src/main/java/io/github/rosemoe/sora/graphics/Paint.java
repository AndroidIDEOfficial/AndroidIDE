/*
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2022  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 */
package io.github.rosemoe.sora.graphics;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;

import io.github.rosemoe.sora.text.ContentLine;

public class Paint extends android.graphics.Paint {

    private float spaceWidth;

    public Paint() {
        super();
        spaceWidth = measureText(" ");
    }

    public float getSpaceWidth() {
        return spaceWidth;
    }

    public void setTypefaceWrapped(Typeface typeface) {
        super.setTypeface(typeface);
        spaceWidth = measureText(" ");
    }

    public void setTextSizeWrapped(float textSize) {
        super.setTextSize(textSize);
        spaceWidth = measureText(" ");
    }

    public void setFontFeatureSettingsWrapped(String settings) {
        super.setFontFeatureSettings(settings);
        spaceWidth = measureText(" ");
    }

    @Override
    public void set(android.graphics.Paint src) {
        super.set(src);
        spaceWidth = measureText(" ");
    }

    /**
     * Get the advance of text with the context positions related to shaping the characters
     */
    @SuppressLint("NewApi")
    public float measureTextRunAdvance(char[] text, int start, int end, int contextStart, int contextEnd) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getRunAdvance(text, start, end, contextStart, contextEnd, false, end);
        } else {
            // Hidden, but we can call it directly on Android 21 - 22
            return getTextRunAdvances(text, start, end - start, contextStart, contextEnd - contextStart, false, null, 0);
        }
    }

    /**
     * Find offset for a certain advance returned by {@link #measureTextRunAdvance(char[], int, int, int, int)}
     */
    public int findOffsetByRunAdvance(ContentLine text, int start, int end, float advance) {
        if (text.widthCache != null) {
            var cache = text.widthCache;
            var offset = start;
            var currAdvance = 0f;
            for (;offset < end && currAdvance < advance;offset++) {
                currAdvance += cache[offset];
            }
            if (currAdvance > advance) {
                offset--;
            }
            return Math.max(offset, start);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getOffsetForAdvance(text, start, end, start, end, false, advance);
        } else {
            return start + breakText(text.value, start, end - start, advance, null);
        }
    }

}
