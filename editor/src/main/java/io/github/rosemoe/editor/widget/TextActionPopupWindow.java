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

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

import io.github.rosemoe.editor.R;

public class TextActionPopupWindow extends TextComposeBasePopup implements View.OnClickListener, CodeEditor.EditorTextActionPresenter {
    private final CodeEditor mEditor;
    private final MaterialButton mPasteBtn;
    private final MaterialButton mSelectAll;
    private final LinearLayout mContainer;

    private float mDpUnit = 0f;

    /**
     * Create a panel for editor
     *
     * @param editor Target editor
     */
    public TextActionPopupWindow(CodeEditor editor) {
        super(editor);
        mEditor = editor;
        mDpUnit = mEditor.getDpUnit();
        int popupHeightInDp = 60;
        popHeightPx = (int) (popupHeightInDp * mDpUnit);

        // Since popup window does provide decor view, we have to pass null to this method
        @SuppressLint("InflateParams")
        View root = LayoutInflater.from(editor.getContext()).inflate(R.layout.text_compose_popup_window, null);
        mSelectAll = root.findViewById(R.id.tcpw_material_button_select_all);
        mContainer = root.findViewById(R.id.text_compose_panel);
        MaterialButton cut = root.findViewById(R.id.tcpw_material_button_cut);
        MaterialButton copy = root.findViewById(R.id.tcpw_material_button_copy);
        mPasteBtn = root.findViewById(R.id.tcpw_material_button_paste);
        mSelectAll.setOnClickListener(this);
        cut.setOnClickListener(this);
        copy.setOnClickListener(this);
        mPasteBtn.setOnClickListener(this);
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(mDpUnit * 8);
        gd.setStroke(1, 0xff808080);
        gd.setColor(0xffffffff);
        root.setBackground(gd);
        setContentView(root);
    }

    @Override
    public void show() {
        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, Resources.getSystem().getDisplayMetrics()));
        }
        super.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tcpw_material_button_select_all) {
            mEditor.selectAll();
        } else if (id == R.id.tcpw_material_button_cut) {
            mEditor.copyText();
            if (mEditor.getCursor().isSelected()) {
                mEditor.getCursor().onDeleteKeyPressed();
            }
        } else if (id == R.id.tcpw_material_button_paste) {
            mEditor.pasteText();
            mEditor.setSelection(mEditor.getCursor().getRightLine(), mEditor.getCursor().getRightColumn());
        } else if (id == R.id.tcpw_material_button_copy) {
            mEditor.copyText();
            mEditor.setSelection(mEditor.getCursor().getRightLine(), mEditor.getCursor().getRightColumn());
        }
        hide(DISMISS);
    }

    @Override
    public void onSelectedTextClicked(MotionEvent event) {

    }

    @Deprecated
    @Override
    public void onUpdate() {

    }

    @Override
    public void onUpdate(int updateReason) {
        hide(updateReason);
    }

    @Override
    public void onBeginTextSelect() {
        setHeight((int) (LinearLayout.LayoutParams.WRAP_CONTENT));
        setWidth((int) (LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public boolean onExit() {
        boolean result = isShowing();
        hide(DISMISS);
        if (mEditor.getCursor().isSelected()) {
            mEditor.setSelection(mEditor.getCursor().getLeftLine(), mEditor.getCursor().getLeftColumn());
        }
        return result && !isShowing();
    }

    @Override
    public boolean shouldShowCursor() {
        // this will show handler along with popup
        return true;
    }

    @Override
    public void onTextSelectionEnd() {
        TextActionPopupWindow panel = this;
        if (panel.isShowing()) {
            panel.hide(DISMISS);
        } else {
            int first = mEditor.getFirstVisibleRow();
            int last = mEditor.getLastVisibleRow();
            int left = mEditor.getCursor().getLeftLine();
            int right = mEditor.getCursor().getRightLine();
            int toLineBottom;
            if (right <= first) {
                toLineBottom = first;
            } else if (right > last) {
                if (left <= first) {
                    toLineBottom = (first + last) / 2;
                } else if (left >= last) {
                    toLineBottom = last - 2;
                } else {
                    if (left + 3 >= last) {
                        toLineBottom = left - 2;
                    } else {
                        toLineBottom = left + 1;
                    }
                }
            } else {
                if (left <= first) {
                    if (right + 3 >= last) {
                        toLineBottom = right - 2;
                    } else {
                        toLineBottom = right + 1;
                    }
                } else {
                    if (left + 5 >= right) {
                        toLineBottom = right + 1;
                    } else {
                        toLineBottom = (left + right) / 2;
                    }
                }
            }
            toLineBottom = Math.max(0, toLineBottom);
            int panelY = mEditor.getRowBottom(toLineBottom) - mEditor.getOffsetY();
            float handleLeftX = mEditor.getOffset(left, mEditor.getCursor().getLeftColumn());
            float handleRightX = mEditor.getOffset(right, mEditor.getCursor().getRightColumn());
            int panelX = (int) ((handleLeftX + handleRightX) / 2f);
            panel.setExtendedX(mDpUnit * 28);
            panel.setExtendedY(panelY);
            Log.d("onTextSelectionEnd", "panelX: " + panelX + ", panelY: " + panelY);
            panel.show();
            mContainer.requestFocus();
            //mSelectAll.clearFocus();
        }
    }


}
