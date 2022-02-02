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

import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;

import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.text.Cursor;
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion;

/**
 * Connection between input method and editor
 *
 * @author Rose
 */
class EditorInputConnection extends BaseInputConnection {

    private final static String LOG_TAG = "EditorInputConnection";

    private final CodeEditor mEditor;
    protected int mComposingLine = -1;
    protected int mComposingStart = -1;
    protected int mComposingEnd = -1;
    private boolean mInvalid;

    /**
     * Create a connection for the given editor
     *
     * @param targetView Host editor
     */
    public EditorInputConnection(CodeEditor targetView) {
        super(targetView, true);
        mEditor = targetView;
        mInvalid = false;
    }

    protected void invalid() {
        mInvalid = true;
        mComposingEnd = mComposingStart = mComposingLine = -1;
        mEditor.invalidate();
    }

    /**
     * Reset the state of this connection
     */
    protected void reset() {
        mComposingEnd = mComposingStart = mComposingLine = -1;
        mInvalid = false;
    }

    /**
     * Private use.
     * Get the Cursor of Content displaying by Editor
     *
     * @return Cursor
     */
    private Cursor getCursor() {
        return mEditor.getCursor();
    }

    @Override
    public synchronized void closeConnection() {
        super.closeConnection();
        Content content = mEditor.getText();
        while (content.isInBatchEdit()) {
            content.endBatchEdit();
        }
        mComposingLine = mComposingEnd = mComposingStart = -1;
        mEditor.onCloseConnection();
    }

    @Override
    public int getCursorCapsMode(int reqModes) {
        return TextUtils.getCapsMode(mEditor.getText(), getCursor().getLeft(), reqModes);
    }

    /**
     * Get content region internally
     */
    private CharSequence getTextRegionInternal(int start, int end, int flags) {
        Content origin = mEditor.getText();
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (start < 0) {
            start = 0;
        }
        if (end > origin.length()) {
            end = origin.length();
        }
        if (end < start) {
            start = end = 0;
        }
        if (end - start > mEditor.getProps().maxIPCTextLength) {
            end = start + mEditor.getProps().maxIPCTextLength;
        }
        Content sub = (Content) origin.subSequence(start, end);
        if (flags == GET_TEXT_WITH_STYLES) {
            SpannableStringBuilder text = new SpannableStringBuilder(sub);
            // Apply composing span
            if (mComposingLine != -1) {
                try {
                    int originalComposingStart = getCursor().getIndexer().getCharIndex(mComposingLine, mComposingStart);
                    int originalComposingEnd = getCursor().getIndexer().getCharIndex(mComposingLine, mComposingEnd);
                    int transferredStart = originalComposingStart - start;
                    if (transferredStart >= text.length()) {
                        return text;
                    }
                    if (transferredStart < 0) {
                        transferredStart = 0;
                    }
                    int transferredEnd = originalComposingEnd - start;
                    if (transferredEnd <= 0) {
                        return text;
                    }
                    if (transferredEnd >= text.length()) {
                        transferredEnd = text.length();
                    }
                    text.setSpan(Spanned.SPAN_COMPOSING, transferredStart, transferredEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (IndexOutOfBoundsException e) {
                    //ignored
                }
            }
            return text;
        }
        return sub.toString();
    }

    protected CharSequence getTextRegion(int start, int end, int flags) {
        try {
            return getTextRegionInternal(start, end, flags);
        } catch (IndexOutOfBoundsException e) {
            Log.w(LOG_TAG, "Failed to get text region for IME", e);
            return "";
        }
    }

    @Override
    public CharSequence getSelectedText(int flags) {
        //This text should be limited because when the user try to select all text
        //it can be quite large text and costs time, which will finally cause ANR
        int left = getCursor().getLeft();
        int right = getCursor().getRight();
        return left == right ? null : getTextRegion(left, right, flags);
    }

    @Override
    public CharSequence getTextBeforeCursor(int length, int flags) {
        int start = getCursor().getLeft();
        return getTextRegion(start - length, start, flags);
    }

    @Override
    public CharSequence getTextAfterCursor(int length, int flags) {
        int end = getCursor().getRight();
        return getTextRegion(end, end + length, flags);
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        if (!mEditor.isEditable() || mInvalid) {
            return false;
        }
        if (text.equals("\n")) {
            // #67
            sendEnterKeyEvent();
            return true;
        }
        commitTextInternal(text, true);
        return true;
    }

    /**
     * Perform enter key pressed
     */
    private void sendEnterKeyEvent() {
        long eventTime = SystemClock.uptimeMillis();
        sendKeyEvent(new KeyEvent(eventTime, eventTime,
                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, 0, 0,
                KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE));
        sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), eventTime,
                KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER, 0, 0,
                KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE));
    }

    protected void commitTextInternal(CharSequence text, boolean applyAutoIndent) {
        // NOTE: Text styles are ignored by editor
        //Remove composing text first if there is
        deleteComposingText();
        // Replace text
        SymbolPairMatch.Replacement replacement = null;
        if (text.length() == 1 && mEditor.getProps().symbolPairAutoCompletion) {
            replacement = mEditor.mLanguageSymbolPairs.getCompletion(text.charAt(0));
        }
        // newCursorPosition ignored
        // Call onCommitText() can make auto indent and delete text selected automatically
        if (replacement == null || replacement == SymbolPairMatch.Replacement.NO_REPLACEMENT
                || (replacement.shouldNotDoReplace(mEditor.getText()) && replacement.notHasAutoSurroundPair())) {
            mEditor.commitText(text, applyAutoIndent);
        } else {
            String[] autoSurroundPair;
            if (getCursor().isSelected() && (autoSurroundPair = replacement.getAutoSurroundPair()) != null) {
                mEditor.getText().beginBatchEdit();
                //insert left
                mEditor.getText().insert(getCursor().getLeftLine(), getCursor().getLeftColumn(), autoSurroundPair[0]);
                //insert right
                mEditor.getText().insert(getCursor().getRightLine(), getCursor().getRightColumn(), autoSurroundPair[1]);
                mEditor.getText().endBatchEdit();
                //cancel selected
                mEditor.setSelection(getCursor().getLeftLine(), getCursor().getLeftColumn() + autoSurroundPair[0].length()-1, SelectionChangeEvent.CAUSE_TEXT_MODIFICATION);

            } else {
                mEditor.commitText(replacement.text, applyAutoIndent);
                int delta = (replacement.text.length() - replacement.selection);
                if (delta != 0) {
                    int newSel = Math.max(getCursor().getLeft() - delta, 0);
                    CharPosition charPosition = getCursor().getIndexer().getCharPosition(newSel);
                    mEditor.setSelection(charPosition.line, charPosition.column, SelectionChangeEvent.CAUSE_TEXT_MODIFICATION);
                }
            }
        }
    }

    /**
     * Delete composing region
     */
    private void deleteComposingText() {
        if (mComposingLine == -1) {
            return;
        }
        try {
            mEditor.getText().delete(mComposingLine, mComposingStart, mComposingLine, mComposingEnd);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        mComposingLine = mComposingStart = mComposingEnd = -1;
    }

    @Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        if (!mEditor.isEditable() || mInvalid) {
            return false;
        }
        if (beforeLength < 0 || afterLength < 0) {
            return false;
        }

        // Start a batch edit when the operation can not be finished by one call to delete()
        if (beforeLength > 0 && afterLength > 0) {
            beginBatchEdit();
        }

        boolean composing = mComposingLine != -1;
        int composingStart = composing ? getCursor().getIndexer().getCharIndex(mComposingLine, mComposingStart) : 0;
        int composingEnd = composing ? getCursor().getIndexer().getCharIndex(mComposingLine, mComposingEnd) : 0;

        int rangeEnd = getCursor().getLeft();
        int rangeStart = rangeEnd - beforeLength;
        if (rangeStart < 0) {
            rangeStart = 0;
        }
        mEditor.getText().delete(rangeStart, rangeEnd);

        if (composing) {
            int crossStart = Math.max(rangeStart, composingStart);
            int crossEnd = Math.min(rangeEnd, composingEnd);
            composingEnd -= Math.max(0, crossEnd - crossStart);
            int delta = Math.max(0, crossStart - rangeStart);
            composingEnd -= delta;
            composingStart -= delta;
        }

        rangeStart = getCursor().getRight();
        rangeEnd = rangeStart + afterLength;
        if (rangeEnd > mEditor.getText().length()) {
            rangeEnd = mEditor.getText().length();
        }
        mEditor.getText().delete(rangeStart, rangeEnd);

        if (composing) {
            int crossStart = Math.max(rangeStart, composingStart);
            int crossEnd = Math.min(rangeEnd, composingEnd);
            composingEnd -= Math.max(0, crossEnd - crossStart);
            int delta = Math.max(0, crossStart - rangeStart);
            composingEnd -= delta;
            composingStart -= delta;
        }

        if (beforeLength > 0 && afterLength > 0) {
            endBatchEdit();
        }

        if (composing) {
            CharPosition start = getCursor().getIndexer().getCharPosition(composingStart);
            CharPosition end = getCursor().getIndexer().getCharPosition(composingEnd);
            if (start.line != end.line) {
                invalid();
                return false;
            }
            if (start.column == end.column) {
                mComposingLine = -1;
            } else {
                mComposingLine = start.line;
                mComposingStart = start.column;
                mComposingEnd = end.column;
            }
        }

        return true;
    }

    @Override
    public boolean deleteSurroundingTextInCodePoints(int beforeLength, int afterLength) {
        // Unsupported operation
        // According to document, we should return false
        return false;
    }

    @Override
    public synchronized boolean beginBatchEdit() {
        return mEditor.getText().beginBatchEdit();
    }

    @Override
    public synchronized boolean endBatchEdit() {
        boolean inBatch = mEditor.getText().endBatchEdit();
        if (!inBatch) {
            mEditor.updateSelection();
        }
        return inBatch;
    }

    private void deleteSelected() {
        if (getCursor().isSelected()) {
            // Delete selected text
            mEditor.deleteText();
        }
    }

    private boolean shouldRejectComposing() {
        return mEditor.getComponent(EditorAutoCompletion.class).shouldRejectComposing();
    }

    @Override
    public boolean setComposingText(CharSequence text, int newCursorPosition) {
        if (!mEditor.isEditable() || mInvalid || shouldRejectComposing()|| mEditor.getProps().disallowSuggestions) {
            return false;
        }
        if (TextUtils.indexOf(text, '\n') != -1) {
            return false;
        }
        if (mComposingLine == -1) {
            // Create composing info
            deleteSelected();
            mComposingLine = getCursor().getLeftLine();
            mComposingStart = getCursor().getLeftColumn();
            mComposingEnd = mComposingStart + text.length();
            mEditor.commitText(text);
        } else {
            // Already have composing text
            // Delete first
            beginBatchEdit();
            if (mComposingStart != mComposingEnd) {
                mEditor.getText().delete(mComposingLine, mComposingStart, mComposingLine, mComposingEnd);
            }
            // Reset range
            mComposingEnd = mComposingStart + text.length();
            mEditor.getText().insert(mComposingLine, mComposingStart, text);
            endBatchEdit();
        }
        if (text.length() == 0) {
            finishComposingText();
            return true;
        }
        return true;
    }

    @Override
    public boolean finishComposingText() {
        //Logs.log("Finish composing text");
        if (!mEditor.isEditable() || mInvalid) {
            return false;
        }
        mComposingLine = mComposingStart = mComposingEnd = -1;
        mEditor.invalidate();
        return true;
    }

    private int getWrappedIndex(int index) {
        if (index < 0) {
            return 0;
        }
        if (index > mEditor.getText().length()) {
            return mEditor.getText().length();
        }
        return index;
    }

    @Override
    public boolean setSelection(int start, int end) {
        if (!mEditor.isEditable() || mInvalid || mComposingLine != -1) {
            return false;
        }
        start = getWrappedIndex(start);
        end = getWrappedIndex(end);
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (start == getCursor().getLeft() && end == getCursor().getRight()) {
            return true;
        }
        mEditor.getComponent(EditorAutoCompletion.class).hide();
        Content content = mEditor.getText();
        CharPosition startPos = content.getIndexer().getCharPosition(start);
        CharPosition endPos = content.getIndexer().getCharPosition(end);
        mEditor.setSelectionRegion(startPos.line, startPos.column, endPos.line, endPos.column, false, SelectionChangeEvent.CAUSE_IME);
        return true;
    }

    @Override
    public boolean setComposingRegion(int start, int end) {
        if (!mEditor.isEditable() || mInvalid || shouldRejectComposing() || mEditor.getProps().disallowSuggestions) {
            return false;
        }
        try {
            if (start > end) {
                int tmp = start;
                start = end;
                end = tmp;
            }
            if (start < 0) {
                start = 0;
            }
            Content content = mEditor.getText();
            if (end > content.length()) {
                end = content.length();
            }
            CharPosition startPos = content.getIndexer().getCharPosition(start);
            CharPosition endPos = content.getIndexer().getCharPosition(end);
            if (startPos.line != endPos.line) {
                mEditor.restartInput();
                return false;
            }
            mComposingLine = startPos.line;
            mComposingStart = startPos.column;
            mComposingEnd = endPos.column;
            mEditor.invalidate();
        } catch (IndexOutOfBoundsException e) {
            Log.w(LOG_TAG, "set composing region for IME failed", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean performContextMenuAction(int id) {
        switch (id) {
            case android.R.id.selectAll:
                mEditor.selectAll();
                return true;
            case android.R.id.cut:
                mEditor.copyText();
                if (getCursor().isSelected()) {
                    mEditor.deleteText();
                }
                return true;
            case android.R.id.paste:
            case android.R.id.pasteAsPlainText:
                mEditor.pasteText();
                return true;
            case android.R.id.copy:
                mEditor.copyText();
                return true;
            case android.R.id.undo:
                mEditor.undo();
                return true;
            case android.R.id.redo:
                mEditor.redo();
                return true;
        }
        return false;
    }

    @Override
    public boolean requestCursorUpdates(int cursorUpdateMode) {
        mEditor.updateCursorAnchor();
        return true;
    }

    @Override
    public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
        if ((flags & GET_EXTRACTED_TEXT_MONITOR) != 0) {
            mEditor.setExtracting(request);
        }

        return mEditor.extractText(request);
    }

    @Override
    public boolean clearMetaKeyStates(int states) {
        mEditor.mKeyMetaStates.clearMetaStates(states);
        return true;
    }

    @Override
    public boolean reportFullscreenMode(boolean enabled) {
        return false;
    }
}
