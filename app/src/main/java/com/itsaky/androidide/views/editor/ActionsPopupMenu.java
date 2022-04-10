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

import static com.itsaky.androidide.utils.Logger.newInstance;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.androidide.actions.ActionData;
import com.itsaky.androidide.actions.ActionItem;
import com.itsaky.androidide.actions.ActionsRegistry;
import com.itsaky.androidide.actions.editor.SelectAllAction;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.IDELanguage;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.java.JavaLanguageServer;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.util.DiagnosticUtil;
import com.itsaky.lsp.xml.XMLLanguageServer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.rosemoe.sora.event.HandleStateChangeEvent;
import io.github.rosemoe.sora.event.ScrollEvent;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.event.SubscriptionReceipt;
import io.github.rosemoe.sora.event.Unsubscribe;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.EditorTouchEventHandler;

/**
 * @author Akash Yadav
 */
public class ActionsPopupMenu extends PopupMenu implements ActionsRegistry.ActionExecListener {

    private static final Logger LOG = newInstance("ActionsPopMenu");
    private static final long DELAY = 200;
    private final List<SubscriptionReceipt<?>> receipts = new ArrayList<>();
    private final EditorTouchEventHandler touchHandler;
    private final IDEEditor editor;
    private long mLastScroll;
    private int mLastPosition;
    private boolean showing;

    public ActionsPopupMenu(@NonNull Context context, @NonNull View anchor, int gravity) {
        super(context, anchor, gravity);
        this.editor = (IDEEditor) anchor;
        this.touchHandler = editor.getEventHandler();

        subscribe();
    }

    @Override
    public void show() {
        super.show();
        showing = true;
    }

    @Override
    public void dismiss() {}

    @Override
    public void onExec(@NotNull ActionItem action, @NotNull Object result) {
        if (!(action instanceof SelectAllAction)) {
            dismissPopup();
        }
    }

    public void unsubscribeEvents() {
        for (var receipt : this.receipts) {
            receipt.unsubscribe();
        }
    }

    public void dismissPopup() {
        super.dismiss();
        showing = false;

        ActionsRegistry.getInstance().unregisterActionExecListener(this);
    }

    public boolean isShowing() {
        return showing;
    }

    public void fillMenu() {
        getMenu().clear();

        final var data = new ActionData();
        data.put(Context.class, editor.getContext());
        data.put(IDEEditor.class, editor);
        data.put(
                CodeEditor.class, editor); // For LSP actions, as they cannot access IDEEditor class
        data.put(File.class, editor.getFile());
        data.put(DiagnosticItem.class, getDiagnosticAtCursor());
        data.put(
                JavaLanguageServer.class,
                (JavaLanguageServer) StudioApp.getInstance().getJavaLanguageServer());
        data.put(
                XMLLanguageServer.class,
                (XMLLanguageServer) StudioApp.getInstance().getXMLLanguageServer());

        final var registry = ActionsRegistry.getInstance();
        registry.registerActionExecListener(this);
        registry.fillMenu(data, ActionItem.Location.EDITOR_TEXT_ACTIONS, getMenu());
    }

    public void show(int x, int y) {
        fillMenu();

        if (isShowing()) {
            dismissPopup();
        }

        try {
            final var field = PopupMenu.class.getDeclaredField("mPopup");
            field.setAccessible(true);
            final var popup = field.get(this);
            assert popup != null;
            final var show = popup.getClass().getDeclaredMethod("show", int.class, int.class);
            show.invoke(popup, x, y);
            showing = true;
        } catch (Throwable e) {
            LOG.error("Unable to show text actions at location", e);
            show();
        }
    }

    protected void onSelectionChanged(SelectionChangeEvent event, Unsubscribe unsubscribe) {
        if (touchHandler.hasAnyHeldHandle()) {
            return;
        }
        if (event.isSelected()) {
            if (!isShowing()) {
                this.editor.post(this::displayWindow);
            }
            mLastPosition = -1;
        } else {
            var show = false;
            if (event.getCause() == SelectionChangeEvent.CAUSE_TAP
                    && event.getLeft().index == mLastPosition
                    && !isShowing()
                    && !this.editor.getText().isInBatchEdit()) {
                this.editor.post(this::displayWindow);
                show = true;
            } else {
                dismissPopup();
            }
            if (event.getCause() == SelectionChangeEvent.CAUSE_TAP && !show) {
                mLastPosition = event.getLeft().index;
            } else {
                mLastPosition = -1;
            }
        }
    }

    private void subscribe() {
        receipts.add(editor.subscribeEvent(SelectionChangeEvent.class, this::onSelectionChanged));
        receipts.add(editor.subscribeEvent(ScrollEvent.class, this::onScrollEvent));
        receipts.add(
                editor.subscribeEvent(HandleStateChangeEvent.class, this::onHandleStateChanged));
    }

    protected void onScrollEvent(ScrollEvent event, Unsubscribe unsubscribe) {
        var last = mLastScroll;
        mLastScroll = System.currentTimeMillis();
        if (mLastScroll - last < DELAY) {
            postDisplay();
        }
    }

    protected void onHandleStateChanged(
            @NonNull HandleStateChangeEvent event, Unsubscribe unsubscribe) {
        if (event.isHeld()) {
            postDisplay();
        }
    }

    public void displayWindow() {
        final var loc =
                editor.getLayout()
                        .getCharLayoutOffset(
                                editor.getCursor().getLeftLine(),
                                editor.getCursor().getLeftColumn());
        final int x = (int) loc[1];
        final int y = (int) (loc[0] - SizeUtils.dp2px(24));
        show(x, y);
    }

    private void postDisplay() {
        if (!isShowing()) {
            return;
        }

        dismissPopup();

        if (!this.editor.getCursor().isSelected()) {
            return;
        }

        this.editor.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        if (!touchHandler.hasAnyHeldHandle()
                                && System.currentTimeMillis() - mLastScroll > DELAY
                                && touchHandler.getScroller().isFinished()) {
                            displayWindow();
                        } else {
                            editor.postDelayed(this, DELAY);
                        }
                    }
                },
                DELAY);
    }

    @Nullable
    private DiagnosticItem getDiagnosticAtCursor() {
        final var language = editor.getEditorLanguage();
        if (!(language instanceof IDELanguage)) {
            return null;
        }

        final var diagnostics = ((IDELanguage) language).getDiagnostics();
        return DiagnosticUtil.binarySearchDiagnostic(
                diagnostics, editor.getCursorRange().getStart());
    }
}
