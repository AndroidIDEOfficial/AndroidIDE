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

import android.app.ProgressDialog;
import android.widget.Toast;

import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.text.Cursor;

/**
 * Search text in editor
 *
 * @author Rose
 */
@SuppressWarnings("deprecated")
public class EditorSearcher {

    private final CodeEditor mEditor;
    protected String mSearchText;

    EditorSearcher(CodeEditor editor) {
        mEditor = editor;
    }

    private void checkState() {
        if (mSearchText == null) {
            throw new IllegalStateException("search text has not been set");
        }
    }

    public void search(String text) {
        if (text != null && text.length() == 0) {
            text = null;
        }
        mSearchText = text;
        mEditor.postInvalidate();
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean replaceThis(String newText) {
        checkState();
        Content text = mEditor.getText();
        Cursor cursor = text.getCursor();
        if (cursor.isSelected()) {
            String selectedText = text.subContent(cursor.getLeftLine(), cursor.getLeftColumn(), cursor.getRightLine(), cursor.getRightColumn()).toString();
            if (selectedText.equals(mSearchText)) {
                mEditor.commitText(newText);
                mEditor.hideAutoCompleteWindow();
                gotoNext(false);
                return true;
            }
        }
        gotoNext(false);
        return false;
    }

    public void replaceAll(final String newText) {
        checkState();
        final ProgressDialog progressDialog = ProgressDialog.show(mEditor.getContext(), "Replacing", "Editor is now replacing texts, please wait", true, false);
        final String searchText = mSearchText;
        new Thread() {

            @Override
            public void run() {
                String text = null;
                Exception ex = null;
                try {
                    text = mEditor.getText().toString().replace(searchText, newText);
                } catch (Exception e) {
                    e.printStackTrace();
                    ex = e;
                }
                final Exception ex2 = ex;
                final String text2 = text;
                mEditor.post(() -> {
                    if (text2 == null) {
                        Toast.makeText(mEditor.getContext(), String.valueOf(ex2), Toast.LENGTH_SHORT).show();
                    } else {
                        int line = mEditor.getCursor().getLeftLine();
                        int column = mEditor.getCursor().getLeftColumn();
                        mEditor.getText().replace(0, 0, mEditor.getLineCount() - 1, mEditor.getText().getColumnCount(mEditor.getLineCount() - 1), text2);
                        mEditor.setSelectionAround(line, column);
                        mEditor.invalidate();
                    }
                    progressDialog.cancel();
                });
            }

        }.start();
    }

    public void gotoNext() {
        gotoNext(true);
    }

    private void gotoNext(boolean tip) {
        checkState();
        Content text = mEditor.getText();
        Cursor cursor = text.getCursor();
        int line = cursor.getRightLine();
        int column = cursor.getRightColumn();
        for (int i = line; i < text.getLineCount(); i++) {
            int idx = column >= text.getColumnCount(i) ? -1 : text.getLine(i).indexOf(mSearchText, column);
            if (idx != -1) {
                mEditor.setSelectionRegion(i, idx, i, idx + mSearchText.length());
                return;
            }
            column = 0;
        }
        if (tip) {
            Toast.makeText(mEditor.getContext(), "Not found in this direction", Toast.LENGTH_SHORT).show();
            mEditor.jumpToLine(0);
        }
    }

    public void gotoLast() {
        checkState();
        Content text = mEditor.getText();
        Cursor cursor = text.getCursor();
        int line = cursor.getLeftLine();
        int column = cursor.getLeftColumn();
        for (int i = line; i >= 0; i--) {
            int idx = column - 1 < 0 ? -1 : text.getLine(i).lastIndexOf(mSearchText, column - 1);
            if (idx != -1) {
                mEditor.setSelectionRegion(i, idx, i, idx + mSearchText.length());
                return;
            }
            column = i - 1 >= 0 ? text.getColumnCount(i - 1) : 0;
        }
        Toast.makeText(mEditor.getContext(), "Not found in this direction", Toast.LENGTH_SHORT).show();
    }

    public void stopSearch() {
        search(null);
    }

}
