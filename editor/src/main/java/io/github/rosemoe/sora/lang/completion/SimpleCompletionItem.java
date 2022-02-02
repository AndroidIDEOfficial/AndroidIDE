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
package io.github.rosemoe.sora.lang.completion;

import android.graphics.drawable.Drawable;

import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.widget.CodeEditor;

/**
 * SimpleCompletionItem represents a simple replace action for auto-completion.
 * {@code prefixLength} is the length of prefix (text length you want to replace before the
 * auto-completion position).
 * {@code commitText} is the text you want to replace the original text.
 *
 * Note that you must make sure the start position of replacement is on the same line as auto-completion's
 * required position.
 *
 * @see CompletionItem
 */
public class SimpleCompletionItem extends CompletionItem {

    public int prefixLength;
    public String commitText;

    public SimpleCompletionItem(int prefixLength, String commitText) {
        this(commitText, prefixLength, commitText);
    }

    public SimpleCompletionItem(CharSequence label, int prefixLength, String commitText) {
        this(label, null, prefixLength, commitText);
    }

    public SimpleCompletionItem(CharSequence label, CharSequence desc, int prefixLength, String commitText) {
        this(label, desc, null, prefixLength, commitText);
    }

    public SimpleCompletionItem(CharSequence label, CharSequence desc, Drawable icon, int prefixLength, String commitText) {
        super(label, desc, icon);
        this.commitText = commitText;
        this.prefixLength = prefixLength;
    }

    @Override
    public SimpleCompletionItem desc(CharSequence desc) {
        super.desc(desc);
        return this;
    }

    @Override
    public SimpleCompletionItem icon(Drawable icon) {
        super.icon(icon);
        return this;
    }

    @Override
    public SimpleCompletionItem label(CharSequence label) {
        super.label(label);
        return this;
    }

    public SimpleCompletionItem commit(int prefixLength, String commitText) {
        this.prefixLength = prefixLength;
        this.commitText = commitText;
        return this;
    }

    @Override
    public void performCompletion(CodeEditor editor, Content text, int line, int column) {
        if (commitText == null) {
            return;
        }
        if (prefixLength == 0) {
            text.insert(line, column, commitText);
            return;
        }
        text.replace(line, column - prefixLength, line, column, commitText);
    }

}
