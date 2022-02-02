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
package io.github.rosemoe.sora.text;

import android.icu.text.BreakIterator;
import android.os.Build;

import io.github.rosemoe.sora.util.IntPair;
import io.github.rosemoe.sora.util.MyCharacter;

/**
 * Utility class for invoking ICU library according to Android platform.
 * Provide some default implementation on old platforms without ICU.
 *
 * @author Rosemoe
 */
public class ICUUtils {

    /**
     * Get word edges for the given offset
     * @param text Text to analyze
     * @param offset Required char offset of word
     * @return Packed value of (start, end) pair. Always contains the position {@code offset}
     */
    public static long getWordEdges(CharSequence text, int offset) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            var itr = BreakIterator.getWordInstance();
            itr.setText(new CharSequenceIterator(text));
            int end = itr.following(offset);
            int start = itr.previous();
            if (offset >= start && offset <= end) {
                return IntPair.pack(start, end);
            } else {
                return getWordEdgesFallback(text, offset);
            }
        } else {
            return getWordEdgesFallback(text, offset);
        }
    }

    /**
     * Primitive implementation of {@link #getWordEdges(CharSequence, int)}
     */
    private static long getWordEdgesFallback(CharSequence text, int offset) {
        int start = offset;
        int end = offset;
        while (start > 0 && MyCharacter.isJavaIdentifierPart(text.charAt(start - 1))) {
            start--;
        }
        while (end < text.length() && MyCharacter.isJavaIdentifierPart(text.charAt(end))) {
            end++;
        }
        return IntPair.pack(start, end);
    }

}
