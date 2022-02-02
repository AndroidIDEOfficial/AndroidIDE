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
package io.github.rosemoe.sora.event;

import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.widget.CodeEditor;

/**
 * This event happens when {@link CodeEditor#setText(CharSequence)} is called or
 * user edited the displaying content.
 *
 * Note that you should not update the content at this time. Otherwise, there might be some
 * exceptions causing the editor framework to crash. If you do need to update the content, you should
 * post your actions to the main thread so that the user's modification will be successful.
 */
public class ContentChangeEvent extends Event {

    /**
     * Notify that {@link CodeEditor#setText(CharSequence)} is called
     */
    public final static int ACTION_SET_NEW_TEXT = 1;

    /**
     * Notify that user inserted some texts to the content
     */
    public final static int ACTION_INSERT = 2;

    /**
     * Notify that user deleted some texts in the content
     */
    public final static int ACTION_DELETE = 3;

    private final int mAction;
    private final CharPosition mStart;
    private final CharPosition mEnd;
    private final CharSequence mTextChanged;

    public ContentChangeEvent(CodeEditor editor, int action, CharPosition changeStart, CharPosition changeEnd, CharSequence textChanged) {
        super(editor);
        mAction = action;
        mStart = changeStart;
        mEnd = changeEnd;
        mTextChanged = textChanged;
    }

    /**
     * Get action code of the event.
     * @see #ACTION_SET_NEW_TEXT
     * @see #ACTION_INSERT
     * @see #ACTION_DELETE
     */
    public int getAction() {
        return mAction;
    }

    /**
     * Return the CharPosition indicating the start of changed region.
     *
     * Note that you can not modify the values in the returned instance.
     */
    public CharPosition getChangeStart() {
        return mStart;
    }

    /**
     * Return the CharPosition indicating the end of changed region.
     *
     * Note that you can not modify the values in the returned instance.
     */
    public CharPosition getChangeEnd() {
        return mEnd;
    }

    /**
     * Return the changed text in this modification.
     * If action is {@link #ACTION_SET_NEW_TEXT}, Content instance is returned.
     * If action is {@link #ACTION_INSERT}, inserted text is the result.
     * If action is {@link #ACTION_DELETE}, deleted text is the result.
     */
    public CharSequence getChangedText() {
        return mTextChanged;
    }

}
