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
package io.github.rosemoe.sora.widget;

import io.github.rosemoe.sora.event.EventReceiver;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.event.Unsubscribe;

/**
 * This class is used to control cursor visibility
 *
 * @author Rose
 */
final class CursorBlink implements Runnable, EventReceiver<SelectionChangeEvent> {

    final CodeEditor editor;
    long lastSelectionModificationTime = 0;
    int period;
    public boolean visibility;
    public boolean valid;
    private float[] buffer;

    public CursorBlink(CodeEditor editor, int period) {
        visibility = true;
        this.editor = editor;
        this.period = period;
        editor.subscribeEvent(SelectionChangeEvent.class, this);
    }

    @Override
    public void onReceive(SelectionChangeEvent event, Unsubscribe unsubscribe) {
        onSelectionChanged();
    }

    public void setPeriod(int period) {
        this.period = period;
        if (period <= 0) {
            visibility = true;
            valid = false;
        } else {
            valid = true;
        }
    }

    public void onSelectionChanged() {
        lastSelectionModificationTime = System.currentTimeMillis();
        visibility = true;
    }

    public boolean isSelectionVisible() {
        return (buffer[0] >= editor.getOffsetY() && buffer[0] - editor.getRowHeight() <= editor.getOffsetY() + editor.getHeight()
                && buffer[1] >= editor.getOffsetX() && buffer[1] - 100f/* larger than a single character */ <= editor.getOffsetX() + editor.getWidth());
    }

    @Override
    public void run() {
        if (valid && period > 0) {
            if (System.currentTimeMillis() - lastSelectionModificationTime >= period * 2L) {
                visibility = !visibility;
                buffer = editor.mLayout.getCharLayoutOffset(editor.getCursor().getLeftLine(), editor.getCursor().getLeftColumn(), buffer);
                if (!editor.getCursor().isSelected() && isSelectionVisible()) {
                    // Invalidate dirty region
                    var delta = (int)(editor.getDpUnit() * 10);
                    var l = (int)buffer[1] - delta;
                    var r = l + delta * 2;
                    var b = (int)buffer[0] + delta;
                    var t = b - delta * 2;
                    editor.postInvalidate(l, t, r, b);
                }
            } else {
                visibility = true;
            }
            editor.postDelayed(this, period);
        } else {
            visibility = true;
        }
    }

}
