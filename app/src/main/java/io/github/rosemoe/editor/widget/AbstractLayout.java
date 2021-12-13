/*
 *   Copyright 2020-2021 Rosemoe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.rosemoe.editor.widget;

import android.graphics.Paint;

import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.FontCache;

/**
 * Base layout implementation of {@link Layout}
 * This class has basic methods for its subclasses to measure texts
 *
 * @author Rose
 */
abstract class AbstractLayout implements Layout {

    protected CodeEditor editor;
    protected Content text;
    protected Paint shadowPaint;
    protected FontCache fontCache;

    public AbstractLayout(CodeEditor editor, Content text) {
        this.editor = editor;
        this.text = text;
        shadowPaint = new Paint(editor.getTextPaint());
        fontCache = new FontCache();
    }

    protected float measureText(CharSequence text, int start, int end) {
        int tabCount = 0;
        end = Math.min(text.length(), end);
        for (int i = start; i < end; i++) {
            if (text.charAt(i) == '\t') {
                tabCount++;
            }
        }
        float extraWidth = fontCache.measureChar(' ', shadowPaint) * editor.getTabWidth() - fontCache.measureChar('\t', shadowPaint);
        return fontCache.measureText(text, start, end, shadowPaint) + tabCount * extraWidth;
    }

    protected float[] orderedFindCharIndex(float targetOffset, CharSequence str, int index, int end) {
        float width = 0f;
        while (index < end && width < targetOffset) {
            float single = fontCache.measureChar(str.charAt(index), shadowPaint);
            if (str.charAt(index) == '\t') {
                single = editor.getTabWidth() * fontCache.measureChar(' ', shadowPaint);
            }
            width += single;
            index++;
        }
        return new float[]{index, width};
    }

    protected float[] orderedFindCharIndex(float targetOffset, CharSequence str) {
        float width = 0f;
        int index = 0;
        int length = str.length();
        while (index < length && width < targetOffset) {
            float single = fontCache.measureChar(str.charAt(index), shadowPaint);
            if (str.charAt(index) == '\t') {
                single = editor.getTabWidth() * fontCache.measureChar(' ', shadowPaint);
            }
            width += single;
            index++;
        }
        return new float[]{index, width};
    }

    @Override
    public void destroyLayout() {
        editor = null;
        text = null;
        shadowPaint = null;
        fontCache = null;
    }

}
