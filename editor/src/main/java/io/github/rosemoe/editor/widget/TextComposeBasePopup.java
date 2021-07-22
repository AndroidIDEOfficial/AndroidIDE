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

import android.graphics.RectF;
import android.view.Gravity;
import android.widget.PopupWindow;

/**
 * Editor base panel class
 *
 * @author Rose
 */
class TextComposeBasePopup extends PopupWindow {
    public static int DISMISS = 0;
    public static int DRAG = 1;
    public static int SCROLL = 2;
    private final float textSizePx;
    private CodeEditor mEditor;
    private int[] mLocation;
    private int mTop;
    private int mMaximumTop;
    private int mLeft;
    protected int popHeightPx;
    private int hideType = -1;

    /**
     * Create a panel for editor
     *
     * @param editor Target editor
     */
    public TextComposeBasePopup(CodeEditor editor) {
        if (editor == null) {
            throw new IllegalArgumentException();
        }
        mLocation = new int[2];
        mEditor = editor;
        super.setTouchable(true);
        textSizePx = mEditor.getTextSizePx();
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
        mMaximumTop = (int) y;
    }

    private final RectF selectionRect = new RectF();


    /**
     * Show the panel or update its position(If already shown)
     */
    public void show() {
        int width = mEditor.getWidth();
        RectF leftHandleRect = mEditor.getLeftHandleRect();
        RectF rightHandleRect = mEditor.getRightHandleRect();

        // when right handle goes below visible area, it rect becomes empty. so this feature (or bug) used to calculate popup location
        // if we can not use this,
        // alternative method can be implemented using mMaximumTop
        // @TODO implement a proper way to calculate popup position
        if (rightHandleRect.isEmpty()) {
            rightHandleRect.top = mMaximumTop;
            rightHandleRect.left = width;
            rightHandleRect.bottom = mMaximumTop;
            rightHandleRect.right = width;
        }

        float handleHeight = leftHandleRect.height();
        selectionRect.top = Math.min(leftHandleRect.top, rightHandleRect.top);
        selectionRect.bottom = Math.max(leftHandleRect.bottom, rightHandleRect.bottom);
        selectionRect.left = Math.min(leftHandleRect.left, rightHandleRect.left);
        selectionRect.right = Math.max(leftHandleRect.right, rightHandleRect.right);

        // prevent drawing popup over the keyboard
        /*if (selectionRect.bottom > mMaximumTop - popHeightPx) {
            selectionRect.bottom -= popHeightPx;
        }*/

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
        boolean topCovered = mLocation[1] > selectionRect.top - textSizePx - popHeightPx - handleHeight;

        if (topCovered) {
            mTop = (int) (selectionRect.bottom + (handleHeight));
        } else {
            mTop = (int) (selectionRect.top - textSizePx - popHeightPx - handleHeight);
        }
        if (isShowing()) {
            update(mLocation[0] + mLeft, mLocation[1] + mTop, getWidth(), getHeight());
            return;
        }
        super.showAtLocation(mEditor,
                Gravity.START | Gravity.TOP,
                mLocation[0] + mLeft, mLocation[1] + mTop);
    }

    /**
     * Hide the panel (If shown)
     */
    public void hide(int type) {
        if (isShowing()) {
            hideType = type;
            if ((hideType == DRAG || hideType == SCROLL) && mEditor.getEventHandler() != null) {
                mEditor.getEventHandler().notifyGestureInteractionEnd(type);
            }
            super.dismiss();
        }
    }

}

