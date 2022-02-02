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
 * The class used to save auto complete result items.
 * For functionality, this class only manages the information to be displayed in list view.
 * You can implement {@link CompletionItem#performCompletion(CodeEditor, Content, int, int)} to customize
 * your own completion method so that you can develop complex actions.
 *
 * For the simplest usage, see {@link SimpleCompletionItem}
 *
 * @see SimpleCompletionItem
 * @author Rosemoe
 */
@SuppressWarnings("CanBeFinal")
public abstract class CompletionItem {

    /**
     * Icon for displaying in adapter
     */
    public Drawable icon;

    /**
     * Text to display as title in adapter
     */
    public CharSequence label;

    /**
     * Text to display as description in adapter
     */
    public CharSequence desc;


    public CompletionItem(CharSequence label) {
        this(label, null);
    }

    public CompletionItem(CharSequence label, CharSequence desc) {
        this(label, desc, null);
    }

    public CompletionItem(CharSequence label, CharSequence desc, Drawable icon) {
        this.label = label;
        this.desc = desc;
        this.icon = icon;
    }

    public CompletionItem label(CharSequence label) {
        this.label = label;
        return this;
    }

    public CompletionItem desc(CharSequence desc) {
        this.desc = desc;
        return this;
    }

    public CompletionItem icon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Perform this completion.
     * You can implement custom logic to make your completion better(by updating selection and text
     * from here).
     * To make it considered as a single action, the editor will enter batch edit state before invoking
     * this method. Feel free to update the text by multiple calls to {@code text}.
     *
     * @param editor The editor. You can set cursor position with that.
     * @param text The text in editor. You can make modifications to it.
     * @param line  The auto-completion line
     * @param column The auto-completion column
     */
    public abstract void performCompletion(CodeEditor editor, Content text, int line, int column);

}

