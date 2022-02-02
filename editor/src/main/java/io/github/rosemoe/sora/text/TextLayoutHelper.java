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

import android.text.DynamicLayout;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.TextPaint;

/**
 * Helper class for indirectly calling Paint#getTextRunCursor(), which is
 * responsible for cursor controlling.
 *
 * @author Rosemoe
 */
public class TextLayoutHelper {

    private static final ThreadLocal<TextLayoutHelper> sLocal;

    static {
        sLocal = new ThreadLocal<>();
    }

    public static TextLayoutHelper get() {
        var v = sLocal.get();
        if (v == null) {
            v = new TextLayoutHelper();
            sLocal.set(v);
        }
        return v;
    }

    private final Editable text = Editable.Factory.getInstance().newEditable("");
    private final DynamicLayout layout;

    private TextLayoutHelper() {
        layout = new DynamicLayout(text, new TextPaint(), Integer.MAX_VALUE / 2, Layout.Alignment.ALIGN_NORMAL, 0, 0 , true);
    }

    public int getCurPosLeft(int offset, CharSequence s) {
        int left = Math.max(0, offset - 20);
        int index = offset - left;
        text.append(s, left, Math.min(s.length(), offset + 20));
        Selection.setSelection(text, index);
        Selection.moveLeft(text, layout);
        index = Selection.getSelectionStart(text);
        text.clear();
        Selection.removeSelection(text);
        return left + index;
    }

    public int getCurPosRight(int offset, CharSequence s) {
        int left = Math.max(0, offset - 20);
        int index = offset - left;
        text.append(s, left, Math.min(s.length(), offset + 20));
        Selection.setSelection(text, index);
        Selection.moveRight(text, layout);
        index = Selection.getSelectionStart(text);
        text.clear();
        Selection.removeSelection(text);
        return left + index;
    }

}
