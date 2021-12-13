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

import android.view.Gravity;
import android.widget.PopupWindow;
import com.itsaky.androidide.R;

/**
 * Editor base panel class
 *
 * @author Rose
 */
class EditorBasePopupWindow extends PopupWindow {
    private CodeEditor mEditor;
    protected int[] mLocation;
    protected int mTop;
    protected int mLeft;

    /**
     * Create a panel for editor
     *
     * @param editor Target editor
     */
    public EditorBasePopupWindow(CodeEditor editor) {
        if (editor == null) {
            throw new IllegalArgumentException();
        }
        mLocation = new int[2];
        mEditor = editor;
        super.setTouchable(true);
    }

    /**
     * Set the left position on the editor rect
     *
     * @param x X on editor
     */
    public void setExtendedX(float x) {
        mLeft = (int) x;
    }

    /**
     * Set the top position on the editor rect
     *
     * @param y Y on editor
     */
    public void setExtendedY(float y) {
        mTop = (int) y;
    }

    public void updatePosition() {
        int width = mEditor.getWidth();
        if (mLeft > width - getWidth()) {
            mLeft = width - getWidth();
        }
        int height = mEditor.getHeight();
        if (mTop > height - getHeight()) {
            mTop = height - getHeight();
        }
        if (mTop < 0) {
            mTop = 0;
        }
        if (mLeft < 0) {
            mLeft = 0;
        }
        mEditor.getLocationInWindow(mLocation);
        if (isShowing()) {
            update(mLocation[0] + mLeft, mLocation[1] + mTop, getWidth(), getHeight());
        }
    }

    /**
     * Show the panel or update its position(If already shown)
     */
    public void show() {
        int width = mEditor.getWidth();
        if (mLeft > width - getWidth()) {
            mLeft = width - getWidth();
        }
        int height = mEditor.getHeight();
        if (mTop > height - getHeight()) {
            mTop = height - getHeight();
        }
        if (mTop < 0) {
            mTop = 0;
        }
        if (mLeft < 0) {
            mLeft = 0;
        }
        mEditor.getLocationInWindow(mLocation);
        if (isShowing()) {
            update(mLocation[0] + mLeft, mLocation[1] + mTop, getWidth(), getHeight());
            return;
        }
		setAnimationStyle(R.style.PopupAnimation);
        super.showAtLocation(mEditor,
                Gravity.START | Gravity.TOP,
                mLocation[0] + mLeft, mLocation[1] + mTop);
    }

    /**
     * Hide the panel (If shown)
     */
    public void hide() {
        if (isShowing()) {
            super.dismiss();
        }
    }

}

