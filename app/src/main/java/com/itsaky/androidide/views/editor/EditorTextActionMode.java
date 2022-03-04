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
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuBuilder;

import com.itsaky.androidide.utils.Logger;

import io.github.rosemoe.sora.event.ClickEvent;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.widget.component.EditorTextActionWindow;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Text action mode actions for the editor.
 *
 * @author Akash Yadav
 */
public class EditorTextActionMode implements IDEEditor.ITextActionPresenter {

    private IDEEditor editor;
    private ActionMode actionMode;
    private boolean unsubscribeEvents = false;

    private final Set<IDEEditor.TextAction> registeredActions = new TreeSet<>();

    private static final Logger LOG = Logger.instance("EditorTextActionMode");

    @Override
    public void bindEditor(@NonNull IDEEditor editor) {
        Objects.requireNonNull(editor, "Cannot bind with null editor");

        this.editor = editor;

        //  Disable default text action window
        this.editor.getComponent(EditorTextActionWindow.class).setEnabled(false);

        this.editor.subscribeEvent(
                SelectionChangeEvent.class,
                ((event, unsubscribe) -> {
                    if (unsubscribeEvents) {
                        unsubscribe.unsubscribe();
                        return;
                    }

                    final var left = event.getLeft();
                    final var right = event.getRight();
                    if (left.index != right.index) {
                        showAction();
                    }
                }));

        // When click event is received, finish the action mode if it is started
        this.editor.subscribeEvent(
                ClickEvent.class,
                (event, unsubscribe) -> {
                    if (unsubscribeEvents) {
                        unsubscribe.unsubscribe();
                        return;
                    }

                    exit();
                });

        clearRegisteredActions();
    }

    @Override
    public void registerAction(@NonNull IDEEditor.TextAction action) {
        Objects.requireNonNull(this.editor, "No editor attached!");

        this.registeredActions.add(action);
    }

    @Override
    public void destroy() {

        if (actionMode != null) {
            exit();
        }

        this.registeredActions.clear();
        this.editor = null;
        this.actionMode = null;
        this.unsubscribeEvents = true;
    }

    public void clearRegisteredActions() {
        this.registeredActions.clear();
    }

    private void showAction() {
        if (actionMode != null) {
            return;
        }

        actionMode =
                ((AppCompatActivity) editor.getContext())
                        .startSupportActionMode(
                                new ActionMode.Callback() {

                                    @SuppressLint("RestrictedApi")
                                    @Override
                                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                                        if (editor == null) {
                                            return false;
                                        }

                                        if (menu instanceof MenuBuilder) {
                                            MenuBuilder builder = (MenuBuilder) menu;
                                            builder.setOptionalIconsVisible(true);
                                        }

                                        mode.setTitle(android.R.string.selectTextMode);

                                        for (var action : registeredActions) {
                                            if (editor.shouldShowTextAction(action.id)) {
                                                menu.add(0, action.id, 0, action.titleId)
                                                        .setIcon(action.icon)
                                                        .setShowAsActionFlags(
                                                                MenuItem.SHOW_AS_ACTION_IF_ROOM);
                                            }
                                        }

                                        return true;
                                    }

                                    @Override
                                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                                        return editor != null;
                                    }

                                    @Override
                                    public boolean onActionItemClicked(
                                            ActionMode mode, MenuItem item) {

                                        if (editor == null) {
                                            return false;
                                        }

                                        final var id = item.getItemId();
                                        final var optional =
                                                registeredActions.stream()
                                                        .filter(a -> a.id == id)
                                                        .findFirst();

                                        //noinspection SimplifyOptionalCallChains
                                        if (!optional.isPresent()) {
                                            return false;
                                        }

                                        editor.performTextAction(optional.get());

                                        if (id == IDEEditor.TextAction.SELECT_ALL
                                                || id == IDEEditor.TextAction.EXPAND_SELECTION) {
                                            return true;
                                        }

                                        return exit();
                                    }

                                    @Override
                                    public void onDestroyActionMode(ActionMode mode) {
                                        actionMode = null;

                                        if (editor != null) {
                                            editor.setSelection(editor.getCursorRange().getStart());
                                        }
                                    }
                                });
    }

    public boolean exit() {
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
            return true;
        }
        return false;
    }
}
