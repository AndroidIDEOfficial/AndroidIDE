/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.views.editor;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.content.ContextCompat;

import com.itsaky.androidide.R;

import io.github.rosemoe.sora.event.ClickEvent;
import io.github.rosemoe.sora.event.LongPressEvent;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.widget.component.EditorTextActionWindow;

/**
 * Text action mode actions for the editor.
 *
 * @author Akash Yadav
 */
public class EditorTextActionMode {
    
    private final IDEEditor editor;
    private ActionMode actionMode;
    
    public EditorTextActionMode (IDEEditor editor) {
        this.editor = editor;
        
        //  Disable default text action window
        this.editor.getComponent (EditorTextActionWindow.class).setEnabled (false);
        
        this.editor.subscribeEvent (SelectionChangeEvent.class, ((event, unsubscribe) -> {
            final var left = event.getLeft ();
            final var right = event.getRight ();
            if (left.index != right.index) {
                showAction();
            }
        }));
        
        // When click event is received, finish the action mode if it is started
        this.editor.subscribeEvent (ClickEvent.class, (event, unsubscribe) -> exit ());
        
        // Start action mode even if no text is selected
        this.editor.subscribeEvent (LongPressEvent.class, (event, unsubscribe) -> {
            if (actionMode == null) {
                showAction ();
            }
        });
    }
    
    private void showAction () {
        if (actionMode != null) {
            return;
        }
        
        actionMode = ((AppCompatActivity) editor.getContext ()).startSupportActionMode (new ActionMode.Callback () {
            
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onCreateActionMode (ActionMode mode, Menu menu) {
                if (menu instanceof MenuBuilder) {
                    MenuBuilder builder = (MenuBuilder) menu;
                    builder.setOptionalIconsVisible(true);
                }
                mode.setTitle(android.R.string.selectTextMode);
                TypedArray array = editor.getContext().getTheme().obtainStyledAttributes(new int[]{
                        android.R.attr.actionModeSelectAllDrawable,
                        android.R.attr.actionModeCutDrawable,
                        android.R.attr.actionModeCopyDrawable,
                        android.R.attr.actionModePasteDrawable,
                });
    
                menu.add (0, 8, 0, R.string.action_expand_selection)
                        .setShowAsActionFlags (1)
                        .setIcon (createDrawable (R.drawable.ic_expand_selection));
    
                menu.add(0, 0, 0, editor.getContext().getString(android.R.string.selectAll))
                        .setShowAsActionFlags(1)
                        .setIcon(array.getDrawable(0));
    
                if (editor.isEditable()) {
                    menu.add(0, 1, 0, editor.getContext().getString(android.R.string.cut))
                            .setShowAsActionFlags(1)
                            .setIcon(array.getDrawable(1));
                }
    
                menu.add(0, 2, 0, editor.getContext().getString(android.R.string.copy))
                        .setShowAsActionFlags(1)
                        .setIcon(array.getDrawable(2));
    
                if (editor.isEditable()) {
                    menu.add(0, 3, 0, editor.getContext().getString(android.R.string.paste))
                            .setShowAsActionFlags(1)
                            .setIcon(array.getDrawable(3));
                }
    
                menu.add(0, 4, 0, editor.getContext().getString(com.itsaky.androidide.R.string.menu_navigate_definition))
                        .setShowAsActionFlags(1)
                        .setIcon(createDrawable(com.itsaky.androidide.R.drawable.ic_goto_definition));
    
                menu.add(0, 5, 0, editor.getContext().getString(com.itsaky.androidide.R.string.menu_navigate_references))
                        .setShowAsActionFlags(1)
                        .setIcon(createDrawable(com.itsaky.androidide.R.drawable.ic_find_references));
    
                menu.add(0, 6, 0, editor.getContext().getString(com.itsaky.androidide.R.string.menu_comment_line))
                        .setShowAsActionFlags(1)
                        .setIcon(createDrawable(com.itsaky.androidide.R.drawable.ic_comment_line));
    
                menu.add(0, 7, 0, editor.getContext().getString(com.itsaky.androidide.R.string.menu_uncomment_line))
                        .setShowAsActionFlags(1)
                        .setIcon(createDrawable(com.itsaky.androidide.R.drawable.ic_uncomment_line));
    
                array.recycle();
                return true;
            }
    
            private Drawable createDrawable(int icon) {
                final Drawable d = ContextCompat.getDrawable(editor.getContext(), icon);
                if (d != null) {
                    d.setColorFilter(ContextCompat.getColor(editor.getContext(), com.itsaky.androidide.R.color.secondaryColor), PorterDuff.Mode.SRC_ATOP);
                }
                return d;
            }
    
            @Override
            public boolean onPrepareActionMode (ActionMode mode, Menu menu) {
                if (editor.getFile() == null) return false;
    
                final String name = editor.getFile().getName();
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
            public boolean onActionItemClicked (ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        editor.selectAll();
                        break;
                    case 1:
                        editor.cutText();
                        break;
                    case 2:
                        editor.copyText();
                        break;
                    case 3:
                        editor.pasteText();
                        break;
                    case 4:
                        editor.findDefinition();
                        break;
                    case 5:
                        editor.findReferences();
                        break;
                    case 6:
                        editor.commentLine();
                        break;
                    case 7:
                        editor.uncommentLine();
                        break;
                    case 8 :
                        editor.expandSelection ();
                        break;
                }
    
                final var id = item.getItemId ();
                if (id == 0 || id == 8) {
                    return true;
                }
    
                return exit ();
            }
    
            @Override
            public void onDestroyActionMode (ActionMode mode) {
                actionMode = null;
                editor.setSelection (editor.getCursorRange ().getStart ());
            }
        });
    }
    
    public boolean exit () {
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
            return true;
        }
        return false;
    }
}
