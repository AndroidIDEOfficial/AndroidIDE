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
package io.github.rosemoe.sora.widget.component;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.lang.completion.CompletionCancelledException;
import io.github.rosemoe.sora.lang.completion.CompletionItem;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.lang.styling.Spans;
import io.github.rosemoe.sora.lang.styling.TextStyle;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.text.Cursor;
import io.github.rosemoe.sora.text.TextReference;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.base.EditorPopupWindow;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

/**
 * Auto complete window for editing code quicker
 *
 * @author Rosemoe
 */
public class EditorAutoCompletion extends EditorPopupWindow implements EditorBuiltinComponent {
    private final CodeEditor mEditor;
    protected boolean mCancelShowUp = false;
    private int mCurrent = -1;
    private long mRequestTime;
    private int mMaxHeight;
    private EditorCompletionAdapter mAdapter;
    private CompletionLayout mLayout;
    private CompletionThread mThread;
    private long requestShow = 0;
    private long requestHide = -1;
    private boolean enabled = true;

    /**
     * Create a panel instance for the given editor
     *
     * @param editor Target editor
     */
    public EditorAutoCompletion(CodeEditor editor) {
        super(editor, FEATURE_HIDE_WHEN_FAST_SCROLL);
        mEditor = editor;
        setLayout(new DefaultCompletionLayout());
        mAdapter = new DefaultCompletionItemAdapter();
    }

    @SuppressWarnings("unchecked")
    public void setLayout(@NonNull CompletionLayout layout) {
        mLayout = layout;
        layout.setEditorCompletion(this);
        setContentView(layout.inflate(mEditor.getContext()));
        applyColorScheme();
        if (mAdapter != null) {
            mLayout.getCompletionList().setAdapter(mAdapter);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            hide();
        }
    }

    public void setAdapter(EditorCompletionAdapter adapter) {
        mAdapter = adapter;
        if (adapter == null) {
            mAdapter = new DefaultCompletionItemAdapter();
        }
    }

    @Override
    public void show() {
        if (mCancelShowUp || !isEnabled()) {
            return;
        }
        requestShow = System.currentTimeMillis();
        final var requireRequest = mRequestTime;
        mEditor.postDelayed(() -> {
            if (requestHide < requestShow && mRequestTime == requireRequest) {
                super.show();
            }
        }, 70);
    }

    public void hide() {
        super.dismiss();
        cancelCompletion();
        requestHide = System.currentTimeMillis();
    }

    public Context getContext() {
        return mEditor.getContext();
    }

    public int getCurrentPosition() {
        return mCurrent;
    }

    /**
     * Apply colors for self
     */
    public void applyColorScheme() {
        EditorColorScheme colors = mEditor.getColorScheme();
        mLayout.onApplyColorScheme(colors);
    }

    /**
     * Change layout to loading/idle
     *
     * @param state Whether loading
     */
    public void setLoading(boolean state) {
        mLayout.setLoading(state);
    }

    /**
     * Move selection down
     */
    public void moveDown() {
        var adpView = mLayout.getCompletionList();
        if (mCurrent + 1 >= adpView.getAdapter().getCount()) {
            return;
        }
        mCurrent++;
        ((EditorCompletionAdapter) adpView.getAdapter()).notifyDataSetChanged();
        ensurePosition();
    }

    /**
     * Move selection up
     */
    public void moveUp() {
        var adpView = mLayout.getCompletionList();
        if (mCurrent - 1 < 0) {
            return;
        }
        mCurrent--;
        ((EditorCompletionAdapter) adpView.getAdapter()).notifyDataSetChanged();
        ensurePosition();
    }

    /**
     * Make current selection visible
     */
    private void ensurePosition() {
        if (mCurrent != -1)
            mLayout.ensureListPositionVisible(mCurrent, mAdapter.getItemHeight());
    }

    /**
     * Select current position
     */
    public void select() {
        select(mCurrent);
    }

    /**
     * Reject the IME's requests to set composing region/text
     */
    public boolean shouldRejectComposing() {
        return mCancelShowUp;
    }

    /**
     * Select the given position
     *
     * @param pos Index of auto complete item
     */
    public void select(int pos) {
        if (pos == -1) {
            mEditor.commitText("\n");
            return;
        }
        var adpView = mLayout.getCompletionList();
        CompletionItem item = ((EditorCompletionAdapter) adpView.getAdapter()).getItem(pos);
        Cursor cursor = mEditor.getCursor();
        if (!cursor.isSelected()) {
            mCancelShowUp = true;
            mEditor.restartInput();
            mEditor.getText().beginBatchEdit();
            item.performCompletion(mEditor, mEditor.getText(), mThread.mPosition.line, mThread.mPosition.column);
            mEditor.getText().endBatchEdit();
            mCancelShowUp = false;
        }
        hide();
    }

    /**
     * Stop previous completion thread
     */
    public void cancelCompletion() {
        var previous = mThread;
        if (previous != null && previous.isAlive()) {
            previous.cancel();
            previous.mTime = -1;
        }
        mThread = null;
    }

    /**
     * Check cursor position's span.
     * If {@link io.github.rosemoe.sora.lang.styling.TextStyle#NO_COMPLETION_BIT} is set, true is returned.
     */
    public boolean checkNoCompletion() {
        var pos = mEditor.getCursor().left();
        var line = pos.line;
        var column = pos.column;
        var styles = mEditor.getStyles();
        Spans spans;
        // Do not make completion without styles. The language may be empty or busy analyzing spans
        if (styles == null || (spans = styles.spans) == null) {
            return true;
        }
        var reader = spans.read();
        try {
            reader.moveToLine(line);
            int index = reader.getSpanCount() - 1;
            if (index == -1) {
                return true;
            }
            for (int i = 0; i < reader.getSpanCount(); i++) {
                if (reader.getSpanAt(i).column > column) {
                    index = i - 1;
                    break;
                }
            }
            index = Math.max(0, Math.min(index, reader.getSpanCount() - 1));
            if (TextStyle.isNoCompletion(reader.getSpanAt(index).style)) {
                reader.moveToLine(-1);
                return true;
            }
            reader.moveToLine(-1);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            // Unexpected exception. Maybe there is something wrong in language implementation
            return true;
        }
    }

    /**
     * Start completion at current selection position
     */
    @SuppressWarnings("unchecked")
    public void requireCompletion() {
        if (mCancelShowUp || !isEnabled()) {
            return;
        }
        var text = mEditor.getText();
        if (text.getCursor().isSelected() || checkNoCompletion()) {
            hide();
            return;
        }
        if (System.nanoTime() - mRequestTime < mEditor.getProps().cancelCompletionNs) {
            hide();
            mRequestTime = System.nanoTime();
            return;
        }
        cancelCompletion();
        mRequestTime = System.nanoTime();
        mCurrent = -1;
        var publisher = new CompletionPublisher(mEditor.getHandler(), () -> {
            mAdapter.notifyDataSetChanged();
            float newHeight = mAdapter.getItemHeight() * mAdapter.getCount();
            setSize(getWidth(), (int) Math.min(newHeight, mMaxHeight));
            if (!isShowing()) {
                show();
            }
        }, mEditor.getEditorLanguage().getInterruptionLevel());
        mAdapter.attachValues(this, publisher.getItems());
        var adpView = mLayout.getCompletionList();
        adpView.setAdapter(mAdapter);
        mThread = new CompletionThread(mRequestTime, publisher);
        setLoading(true);
        mThread.start();
    }

    public void setMaxHeight(int height) {
        mMaxHeight = height;
    }

    /**
     * Auto-completion Analyzing thread
     *
     * @author Rosemoe
     */
    public final class CompletionThread extends Thread implements TextReference.Validator {

        private long mTime;
        private final Bundle mExtra;
        private final CharPosition mPosition;
        private final Language mLanguage;
        private final ContentReference mRef;
        private final CompletionPublisher mPublisher;
        private boolean mAborted;

        public CompletionThread(long requestTime, CompletionPublisher publisher) {
            mTime = requestTime;
            mPosition = mEditor.getCursor().left();
            mLanguage = mEditor.getEditorLanguage();
            mRef = new ContentReference(mEditor.getText());
            mRef.setValidator(this);
            mPublisher = publisher;
            mExtra = mEditor.getExtraArguments();
            mAborted = false;
        }

        /**
         * Abort the completion thread
         */
        public void cancel() {
            mAborted = true;
            var level = mLanguage.getInterruptionLevel();
            if (level == Language.INTERRUPTION_LEVEL_STRONG) {
                interrupt();
            }
            mPublisher.cancel();
        }

        public boolean isCancelled() {
            return mAborted;
        }

        @Override
        public void validate() {
            if (mRequestTime != mTime || mAborted) {
                throw new CompletionCancelledException();
            }
        }

        @Override
        public void run() {
            try {
                mLanguage.requireAutoComplete(mRef, mPosition, mPublisher, mExtra);
                if (mPublisher.hasData()) {
                    mPublisher.updateList(true);
                } else {
                    mEditor.post(EditorAutoCompletion.this::hide);
                }
                mEditor.post(() -> setLoading(false));
            } catch (Exception e) {
                if (e instanceof CompletionCancelledException) {
                    Log.v("CompletionThread", "Completion is cancelled");
                }
                e.printStackTrace();
            }
        }


    }

}

