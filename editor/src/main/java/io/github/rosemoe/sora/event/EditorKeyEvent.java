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

import android.view.KeyEvent;

import io.github.rosemoe.sora.widget.CodeEditor;

/**
 * Receives key related events in editor.
 * <p>
 * You may set a boolean for editor to return to the Android KeyEvent framework.
 * @see ResultedEvent#setResult(Object)
 * <p>
 * This class mirrors methods of {@link KeyEvent}, but some methods are changed:
 * @see #isAltPressed()
 * @see #isShiftPressed()
 *
 * @author Rosemoe
 */
public class EditorKeyEvent extends ResultedEvent<Boolean> {

    private final KeyEvent src;

    public EditorKeyEvent(CodeEditor editor, KeyEvent src) {
        super(editor);
        this.src = src;
    }

    public int getAction() {
        return src.getAction();
    }

    public int getKeyCode() {
        return src.getKeyCode();
    }

    public int getRepeatCount() {
        return src.getRepeatCount();
    }

    public int getMetaState() {
        return src.getMetaState();
    }

    public int getModifiers() {
        return src.getModifiers();
    }

    public long getDownTime() {
        return src.getDownTime();
    }

    @Override
    public long getEventTime() {
        return src.getEventTime();
    }

    /**
     * editor change: track shift/alt by {@link io.github.rosemoe.sora.widget.KeyMetaStates}
     */
    public boolean isShiftPressed() {
        return getEditor().getKeyMetaStates().isShiftPressed();
    }

    /**
     * editor change: track shift/alt by {@link io.github.rosemoe.sora.widget.KeyMetaStates}
     */
    public boolean isAltPressed() {
        return getEditor().getKeyMetaStates().isAltPressed();
    }

    public boolean isCtrlPressed() {
        return (src.getMetaState() & KeyEvent.META_CTRL_ON) != 0;
    }

    public final boolean result(boolean editorResult) {
        var userResult = isResultSet() ? getResult() : false;
        if (isIntercepted()) {
            return userResult;
        } else {
            return userResult || editorResult;
        }
    }
}
