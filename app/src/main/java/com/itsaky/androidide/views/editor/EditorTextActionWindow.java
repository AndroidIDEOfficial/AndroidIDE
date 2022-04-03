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

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.TextActionItemAdapter;
import com.itsaky.androidide.databinding.LayoutEditorActionsBinding;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.models.CodeActionItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import io.github.rosemoe.sora.event.HandleStateChangeEvent;
import io.github.rosemoe.sora.event.ScrollEvent;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.event.SubscriptionReceipt;
import io.github.rosemoe.sora.event.Unsubscribe;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.EditorTouchEventHandler;
import io.github.rosemoe.sora.widget.base.EditorPopupWindow;

/**
 * Presents text actions in a popup widow.
 *
 * @author Akash Yadav
 * @see io.github.rosemoe.sora.widget.component.EditorTextActionWindow
 */
public class EditorTextActionWindow extends EditorPopupWindow
        implements IDEEditor.ITextActionPresenter {

    private static final Logger LOG = Logger.newInstance("EditorTextActionWindow");

    private static final long DELAY = 200;
    protected final List<SubscriptionReceipt<?>> subscriptionReceipts;
    private final List<CodeActionItem> codeActions = new ArrayList<>(0);
    private final Set<IDEEditor.TextAction> registeredActions = new TreeSet<>();
    private IDEEditor editor;
    private LayoutEditorActionsBinding binding;
    private EditorTouchEventHandler touchHandler;
    private long mLastScroll;
    private int mLastPosition;

    /**
     * Create a popup window for editor
     *
     * @param editor The editor
     * @see #FEATURE_SCROLL_AS_CONTENT
     * @see #FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED
     * @see #FEATURE_HIDE_WHEN_FAST_SCROLL
     */
    public EditorTextActionWindow(@NonNull CodeEditor editor) {
        super(editor, FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED);
        this.subscriptionReceipts = new ArrayList<>();

        getPopup().setAnimationStyle(R.style.PopupAnimation);
    }

    @Override
    public void bindEditor(@NonNull IDEEditor editor) {
        Objects.requireNonNull(editor, "Cannot bind with null editor");

        this.editor = editor;
        this.touchHandler = editor.getEventHandler();
        this.binding = LayoutEditorActionsBinding.inflate(LayoutInflater.from(editor.getContext()));
        this.binding.getRoot().setBackground(createBackground());
        this.binding.textActions.setVerticalFadingEdgeEnabled(true);
        this.binding.textActions.setFadingEdgeLength((int) (10 * editor.getDpUnit()));
        this.binding.textActions.setClipToOutline(
                true); // prevent items from being drawn outside window
        this.editor
                .getComponent(io.github.rosemoe.sora.widget.component.EditorTextActionWindow.class)
                .setEnabled(false);

        setContentView(this.binding.getRoot());
        subscribeToEvents();

        this.registeredActions.clear();
    }

    @Override
    public void registerAction(@NonNull IDEEditor.TextAction action) {
        Objects.requireNonNull(editor, "No editor attached!");
        this.registeredActions.add(action);
    }

    @Nullable
    @Override
    public IDEEditor.TextAction findAction(int id) {
        return this.registeredActions.stream()
                .filter(action -> action.id == id)
                .collect(Collectors.toList())
                .get(0);
    }

    @Override
    public void invalidateActions() {
        if (binding == null || editor == null) {
            return;
        }

        final var actions =
                this.registeredActions.stream()
                        .filter(action -> canShowAction(editor, action))
                        .collect(Collectors.toList());
        this.binding.textActions.setAdapter(
                new TextActionItemAdapter(actions, this::performTextAction));
    }

    @Override
    public void destroy() {

        if (isShowing()) {
            dismiss();
        }

        this.registeredActions.clear();
        this.editor = null;
        this.binding = null;
        this.unsubscribeEvents();
    }

    @Override
    public void updateCodeActions(@NonNull List<CodeActionItem> actions) {
        codeActions.clear();
        codeActions.addAll(actions);
    }

    @NonNull
    @Override
    public List<CodeActionItem> getActions() {
        return codeActions;
    }

    @NonNull
    private Drawable createBackground() {
        final var drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(ContextCompat.getColor(editor.getContext(), R.color.primaryLightColor));
        drawable.setCornerRadius(8 * editor.getDpUnit());
        return drawable;
    }

    @Override
    public void show() {
        Objects.requireNonNull(editor, "No editor attached!");

        final var actionsList = this.binding.textActions;
        if (binding.getRoot().getParent() != null) {
            ((ViewGroup) binding.getRoot().getParent()).removeView(actionsList);
        }

        final var dp8 = SizeUtils.dp2px(8);
        final var dp16 = dp8 * 2;
        final var actions = new ArrayList<>(this.registeredActions);
        actions.removeIf(action -> !canShowAction(editor, action));
        actionsList.setAdapter(new TextActionItemAdapter(actions, this::performTextAction));
        this.binding
                .getRoot()
                .measure(
                        View.MeasureSpec.makeMeasureSpec(
                                editor.getWidth() - dp16 * 2, // 16dp margins from start and end
                                View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.makeMeasureSpec(
                                (int) (260 * editor.getDpUnit())
                                        - dp16 * 2, // 260dp at most and 16dp margins from top and
                                // bottom
                                View.MeasureSpec.AT_MOST));
        setSize(
                this.binding.getRoot().getMeasuredWidth(),
                this.binding.getRoot().getMeasuredHeight());
        LOG.debug("show() called");
        super.show();
    }

    private void subscribeToEvents() {
        this.subscriptionReceipts.add(
                this.editor.subscribeEvent(SelectionChangeEvent.class, this::onSelectionChanged));
        this.subscriptionReceipts.add(
                this.editor.subscribeEvent(ScrollEvent.class, this::onScrollEvent));
        this.subscriptionReceipts.add(
                this.editor.subscribeEvent(
                        HandleStateChangeEvent.class, this::onHandleStateChanged));
    }

    private void unsubscribeEvents() {
        for (final var receipt : this.subscriptionReceipts) {
            receipt.unsubscribe();
        }
        this.subscriptionReceipts.clear();
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
                dismiss();
            }
            if (event.getCause() == SelectionChangeEvent.CAUSE_TAP && !show) {
                mLastPosition = event.getLeft().index;
            } else {
                mLastPosition = -1;
            }
        }
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

    private void performTextAction(@NonNull IDEEditor.TextAction action) {
        this.editor.performTextAction(action);
        if (action.id != IDEEditor.TextAction.SELECT_ALL
                && action.id != IDEEditor.TextAction.EXPAND_SELECTION) {
            dismiss();
        }
    }

    private int selectTop(@NonNull RectF rect) {
        var rowHeight = editor.getRowHeight();
        if (rect.top - rowHeight * 3 / 2F > getHeight()) {
            return (int) (rect.top - rowHeight * 3 / 2 - getHeight());
        } else {
            return (int) (rect.bottom + rowHeight / 2);
        }
    }

    public void displayWindow() {
        int top;
        var cursor = this.editor.getCursor();
        if (cursor.isSelected()) {
            var leftRect = this.editor.getLeftHandleDescriptor().position;
            var rightRect = this.editor.getRightHandleDescriptor().position;
            var top1 = selectTop(leftRect);
            var top2 = selectTop(rightRect);
            top = Math.min(top1, top2);
        } else {
            top = selectTop(this.editor.getInsertHandleDescriptor().position);
        }
        top = Math.max(0, Math.min(top, this.editor.getHeight() - getHeight() - 5));
        float handleLeftX =
                this.editor.getOffset(
                        this.editor.getCursor().getLeftLine(),
                        this.editor.getCursor().getLeftColumn());
        float handleRightX =
                this.editor.getOffset(
                        this.editor.getCursor().getRightLine(),
                        this.editor.getCursor().getRightColumn());
        int panelX = (int) ((handleLeftX + handleRightX) / 2f);
        setLocationAbsolutely(panelX, top);
        show();
    }

    private void postDisplay() {
        if (!isShowing()) {
            return;
        }

        dismiss();

        if (!this.editor.getCursor().isSelected()) {
            return;
        }

        this.editor.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (editor == null) {
                            if (isShowing()) {
                                dismiss();
                            }
                            return;
                        }

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
}
