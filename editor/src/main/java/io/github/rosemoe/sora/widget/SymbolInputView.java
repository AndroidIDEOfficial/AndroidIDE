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

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

public class SymbolInputView extends LinearLayout {

    private int textColor;
    private CodeEditor editor;

    public SymbolInputView(Context context) {
        super(context);
        init();
    }

    public SymbolInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SymbolInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SymbolInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setBackgroundColor(Color.WHITE);
        setOrientation(HORIZONTAL);
        textColor = Color.BLACK;
    }

    /**
     * Bind editor for the view
     */
    public void bindEditor(CodeEditor editor) {
        this.editor = editor;
    }

    /**
     * Set text color in the panel
     */
    public void setTextColor(int color) {
        for (int i = 0; i < getChildCount(); i++) {
            ((Button) getChildAt(i)).setTextColor(color);
        }
        textColor = color;
    }

    /**
     * Remove all added symbols
     */
    public void removeSymbols() {
        removeAllViews();
    }

    /**
     * Add symbols to the view.
     *
     * @param display    The texts displayed in button
     * @param insertText The actual text to be inserted to editor when the button is clicked
     */
    public void addSymbols(String[] display, final String[] insertText) {
        int count = Math.max(display.length, insertText.length);
        for (int i = 0; i < count; i++) {
            Button btn = new Button(getContext(), null, android.R.attr.buttonStyleSmall);
            btn.setText(display[i]);
            btn.setBackground(new ColorDrawable(0));
            btn.setTextColor(textColor);
            addView(btn, new LayoutParams(-2, -1));
            int finalI = i;
            btn.setOnClickListener((view) -> {
                if (editor != null)
                    editor.insertText(insertText[finalI], 1);
            });
        }
    }

}
