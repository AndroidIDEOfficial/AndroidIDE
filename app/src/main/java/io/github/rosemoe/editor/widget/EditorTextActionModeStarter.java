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

import android.content.res.TypedArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import io.github.rosemoe.editor.util.IntPair;

/**
 * Action Mode style text action panel for editor
 *
 * @author Rose
 */
class EditorTextActionModeStarter implements CodeEditor.EditorTextActionPresenter {

    private final CodeEditor mEditor;
    private ActionMode mActionMode;

    EditorTextActionModeStarter(CodeEditor editor) {
        mEditor = editor;
    }

    @Override
    public void onBeginTextSelect() {
        if (mActionMode != null) {
            return;
        }
        mActionMode = mEditor.startActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                mEditor.mStartedActionMode = CodeEditor.ACTION_MODE_SELECT_TEXT;
                actionMode.setTitle(android.R.string.selectTextMode);
                TypedArray array = mEditor.getContext().getTheme().obtainStyledAttributes(new int[]{
                        android.R.attr.actionModeSelectAllDrawable,
                        android.R.attr.actionModeCutDrawable,
                        android.R.attr.actionModeCopyDrawable,
                        android.R.attr.actionModePasteDrawable,
                });
                
                menu.add(0, 0, 0, mEditor.getContext().getString(android.R.string.selectAll))
                        .setShowAsActionFlags(2)
                        .setIcon(array.getDrawable(0));

                menu.add(0, 1, 0, mEditor.getContext().getString(android.R.string.cut))
                        .setShowAsActionFlags(2)
                        .setIcon(array.getDrawable(1));

                menu.add(0, 2, 0, mEditor.getContext().getString(android.R.string.copy))
                        .setShowAsActionFlags(2)
                        .setIcon(array.getDrawable(2));

                menu.add(0, 3, 0, mEditor.getContext().getString(android.R.string.paste))
                        .setShowAsActionFlags(2)
                        .setIcon(array.getDrawable(3));
                
                menu.add(0, 4, 0, mEditor.getContext().getString(com.itsaky.androidide.R.string.menu_navigate_definition))
                    .setIcon(com.itsaky.androidide.R.drawable.ic_goto_definition);
                menu.add(0, 5, 0, mEditor.getContext().getString(com.itsaky.androidide.R.string.menu_navigate_references))
                    .setIcon(com.itsaky.androidide.R.drawable.ic_find_references);
                menu.add(0, 6, 0, mEditor.getContext().getString(com.itsaky.androidide.R.string.menu_comment_line))
                    .setIcon(com.itsaky.androidide.R.drawable.ic_comment_line);
                menu.add(0, 7, 0, mEditor.getContext().getString(com.itsaky.androidide.R.string.menu_uncomment_line))
                    .setIcon(com.itsaky.androidide.R.drawable.ic_uncomment_line);
                array.recycle();
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case 0:
                        mEditor.selectAll();
                        break;
                    case 1:
                        mEditor.cutText();
                        break;
                    case 2:
                        mEditor.copyText();
                        break;
                    case 3:
                        mEditor.pasteText();
                        break;
                    case 4:
                        mEditor.findDefinition();
                        break;
                    case 5:
                        mEditor.findReferences();
                        break;
                    case 6:
                        mEditor.commentLine();
                        break;
                    case 7:
                        mEditor.uncommentLine();
                        break;
                }
                if(menuItem.getItemId() != 0)
                    onExit();
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                mEditor.mStartedActionMode = CodeEditor.ACTION_MODE_NONE;
                mActionMode = null;
                mEditor.setSelection(mEditor.getCursor().getLeftLine(), mEditor.getCursor().getLeftColumn());
            }

        });
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onUpdate(int updateReason) {

    }

    @Override
    public void onSelectedTextClicked(MotionEvent event) {
        long packed = mEditor.getPointPositionOnScreen(event.getX(), event.getY());
        int line = IntPair.getFirst(packed);
        int column = IntPair.getSecond(packed);
        mEditor.setSelection(line, column);
        mEditor.hideAutoCompleteWindow();
        mEditor.hideDiagnosticWindow();
    }

    @Override
    public void onTextSelectionEnd() {

    }

    @Override
    public boolean onExit() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldShowCursor() {
        return true;
    }

}
