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
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.toaster.Toaster;

import java.util.List;

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

    @SuppressLint("RestrictedApi")
    @Override
    public void onBeginTextSelect() {
        if (mActionMode != null) {
            return;
        }
        mActionMode = ((AppCompatActivity) mEditor.getContext()).startSupportActionMode(new ActionMode.Callback() {

                private List<CodeActionItem> fixes;

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    mEditor.mStartedActionMode = CodeEditor.ACTION_MODE_SELECT_TEXT;
                    if (menu instanceof MenuBuilder) {
                        MenuBuilder builder = (MenuBuilder) menu;
                        builder.setOptionalIconsVisible(true);
                    }
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

                    if (mEditor.isEditable()) {
                        menu.add(0, 1, 0, mEditor.getContext().getString(android.R.string.cut))
                            .setShowAsActionFlags(2)
                            .setIcon(array.getDrawable(1));
                    }

                    menu.add(0, 2, 0, mEditor.getContext().getString(android.R.string.copy))
                        .setShowAsActionFlags(2)
                        .setIcon(array.getDrawable(2));

                    if (mEditor.isEditable()) {
                        menu.add(0, 3, 0, mEditor.getContext().getString(android.R.string.paste))
                            .setShowAsActionFlags(2)
                            .setIcon(array.getDrawable(3));
                    }
    
                    menu.add(0, 4, 0, mEditor.getContext().getString(com.itsaky.androidide.R.string.menu_navigate_definition))
                            .setShowAsActionFlags(1)
                            .setIcon(createDrawable(com.itsaky.androidide.R.drawable.ic_goto_definition));
    
                    menu.add(0, 5, 0, mEditor.getContext().getString(com.itsaky.androidide.R.string.menu_navigate_references))
                            .setShowAsActionFlags(1)
                            .setIcon(createDrawable(com.itsaky.androidide.R.drawable.ic_find_references));

                    menu.add(0, 6, 0, mEditor.getContext().getString(com.itsaky.androidide.R.string.menu_comment_line))
                        .setShowAsActionFlags(1)
                        .setIcon(createDrawable(com.itsaky.androidide.R.drawable.ic_comment_line));

                    menu.add(0, 7, 0, mEditor.getContext().getString(com.itsaky.androidide.R.string.menu_uncomment_line))
                        .setShowAsActionFlags(1)
                        .setIcon(createDrawable(com.itsaky.androidide.R.drawable.ic_uncomment_line));

                    array.recycle();
                    return true;
                }

                private Drawable createDrawable(int icon) {
                    final Drawable d = ContextCompat.getDrawable(mEditor.getContext(), icon);
                    if (d != null) {
                        d.setColorFilter(ContextCompat.getColor(mEditor.getContext(), com.itsaky.androidide.R.color.secondaryColor), PorterDuff.Mode.SRC_ATOP);
                    }
                    return d;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    if (mEditor.getFile() == null) return false;

                    final String name = mEditor.getFile().getName();
                    final boolean isJava = name.endsWith(".java") || name.endsWith(".gradle");
                    final boolean isXml = name.endsWith(".xml");
                    final MenuItem def = menu.findItem(4);
                    final MenuItem ref = menu.findItem(5);
                    final MenuItem comment = menu.findItem(6);
                    final MenuItem uncomment = menu.findItem(7);
                    
                    comment.setEnabled(isJava || isXml).getIcon().setAlpha(isJava || isXml ? 255 : 76);
                    uncomment.setEnabled(isJava || isXml).getIcon().setAlpha(isJava || isXml ? 255 : 76);
    
                    // These menu items may, or may not be added
                    // So we need to check if its null or not
                    // before we perform any further actions
                    if (def != null) {
                        def.getIcon().setAlpha(isJava ? 255 : 76);
                        def.setVisible(isJava);
                    }

                    if (ref != null) {
                        ref.getIcon().setAlpha(isJava ? 255 : 76);
                        ref.setVisible(isJava);
                    }
                    
                    return true;
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
                        case 8 :
                            if (fixes == null || fixes.isEmpty()) {
                                StudioApp.getInstance().toast(com.itsaky.androidide.R.string.msg_no_code_actions, Toaster.Type.INFO);
                                break;
                            }
                            showFixes();
                            break;
                    }

                    if (menuItem.getItemId() != 0) {
                        onExit();
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {
                    mEditor.mStartedActionMode = CodeEditor.ACTION_MODE_NONE;
                    mActionMode = null;
                    mEditor.setSelection(mEditor.getCursor().getLeftLine(), mEditor.getCursor().getLeftColumn());
                }

                void showFixes() {
                    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder (mEditor.getContext ());
                    builder.setTitle(com.itsaky.androidide.R.string.msg_code_actions);
                    builder.setView(createFixesListView());
                    builder.show();
                }

                ListView createFixesListView() {
                    final ListView list = new ListView(mEditor.getContext());
                    final QuickFixAdapter adapter = new QuickFixAdapter(fixes);
                    list.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    list.setAdapter(adapter);
                    list.setOnItemClickListener((a, b, c, d) -> performCodeAction(adapter.getItem(c)));
                    return list;
                }

                class QuickFixAdapter extends BaseAdapter {

                    private final List<CodeActionItem> actions;
                    private final int dp16 = SizeUtils.dp2px(16);
                    private final int dp4 = SizeUtils.dp2px(4);
                    private final int dp48 = SizeUtils.dp2px(48);

                    public QuickFixAdapter(List<CodeActionItem> actions) {
                        this.actions = actions;
                    }

                    @Override
                    public int getCount() {
                        return actions.size();
                    }

                    @Override
                    public CodeActionItem getItem(int position) {
                        return actions.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = createItemTextView(position, parent);
                        }
                        return convertView;
                    }

                    private View createItemTextView(int position, ViewGroup parent) {
                        final var action = getItem(position);
                        final TextView text = new TextView(parent.getContext());
                        final String title = action.getTitle();

                        text.setText(title);
                        text.setPaddingRelative(dp16, dp4, dp16, dp4);
                        text.setMinHeight(dp48);
                        text.setGravity(Gravity.CENTER_VERTICAL);

                        return text;
                    }
                }

                void performCodeAction(CodeActionItem action) {
                    if (mEditor.getLanguageClient() != null) {
                        mEditor.getLanguageClient().performCodeAction(mEditor, action);
                    }
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
