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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import io.github.rosemoe.editor.R;

/**
 * This will show when selecting text
 *
 * @author Rose
 */
class EditorTextActionWindow extends EditorBasePopupWindow implements View.OnClickListener, CodeEditor.EditorTextActionPresenter {
    private final CodeEditor mEditor;
    private final Button mPasteBtn;
    private final Button mCopyBtn;
    private final Button mCutBtn;
    private final View mRootView;
    private int maxWidth;

    /**
     * Create a panel for the given editor
     *
     * @param editor Target editor
     */
    public EditorTextActionWindow(CodeEditor editor) {
        super(editor);
        mEditor = editor;
        // Since popup window does provide decor view, we have to pass null to this method
        @SuppressLint("InflateParams")
        View root = LayoutInflater.from(editor.getContext()).inflate(R.layout.text_compose_panel, null);
        Button selectAll = root.findViewById(R.id.panel_btn_select_all);
        Button cut = root.findViewById(R.id.panel_btn_cut);
        Button copy = root.findViewById(R.id.panel_btn_copy);
        mPasteBtn = root.findViewById(R.id.panel_btn_paste);
        mCopyBtn = copy;
        mCutBtn = cut;
        selectAll.setOnClickListener(this);
        cut.setOnClickListener(this);
        copy.setOnClickListener(this);
        mPasteBtn.setOnClickListener(this);
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setColor(0xffffffff);
        root.setBackground(gd);
        setContentView(root);
        mRootView = root;
    }

    @Override
    public void onBeginTextSelect() {
        float dpUnit = mEditor.getDpUnit();
        setHeight((int) (dpUnit * 60));
        maxWidth = (int) (dpUnit * 230);
        setWidth(maxWidth);
    }

    @Override
    public boolean onExit() {
        boolean result = isShowing();
        hide();
        return result;
    }

    @Override
    public void onUpdate() {
        hide();
    }

    @Override
    public void onUpdate(int updateReason) {
        hide();
    }

    @Override
    public void onSelectedTextClicked(MotionEvent event) {
        EditorTextActionWindow panel = this;
        if (panel.isShowing()) {
            panel.hide();
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
            panel.setExtendedX(panelX);
            panel.setExtendedY(panelY);
            panel.show();
        }
    }

    @Override
    public void onTextSelectionEnd() {

    }

    @Override
    public boolean shouldShowCursor() {
        return !isShowing();
    }

    /**
     * Update the state of paste button
     */
    private void updateBtnState() {
        mPasteBtn.setEnabled(mEditor.hasClip());
        mCopyBtn.setVisibility(mEditor.getCursor().isSelected() ? View.VISIBLE : View.GONE);
        mCutBtn.setVisibility(mEditor.getCursor().isSelected() ? View.VISIBLE : View.GONE);
        mRootView.measure(View.MeasureSpec.makeMeasureSpec(1000000, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(100000, View.MeasureSpec.AT_MOST));
        setWidth(Math.min(mRootView.getMeasuredWidth(), maxWidth));
    }

    @Override
    public void show() {
        updateBtnState();
        setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, Resources.getSystem().getDisplayMetrics()));
        super.show();
    }

    @Override
    public void onClick(View p1) {
        int id = p1.getId();
        if (id == R.id.panel_btn_select_all) {
            mEditor.selectAll();
        } else if (id == R.id.panel_btn_cut) {
            mEditor.copyText();
            if (mEditor.getCursor().isSelected()) {
                mEditor.getCursor().onDeleteKeyPressed();
            }
        } else if (id == R.id.panel_btn_paste) {
            mEditor.pasteText();
            mEditor.setSelection(mEditor.getCursor().getRightLine(), mEditor.getCursor().getRightColumn());
        } else if (id == R.id.panel_btn_copy) {
            mEditor.copyText();
            mEditor.setSelection(mEditor.getCursor().getRightLine(), mEditor.getCursor().getRightColumn());
        }
        hide();
    }

}

