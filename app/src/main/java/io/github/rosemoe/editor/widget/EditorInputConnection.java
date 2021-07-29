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

import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.Cursor;

/**
 * Connection between input method and editor
 *
 * @author Rose
 */
class EditorInputConnection extends BaseInputConnection {

    private final static String LOG_TAG = "EditorInputConnection";
    final static int TEXT_LENGTH_LIMIT = 1000000;

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
        //Logs.log("Connection is set to invalid");
        //Logs.dumpStack();
        mInvalid = true;
        mComposingEnd = mComposingStart = mComposingLine = -1;
        mEditor.invalidate();
    }

    /**
     * Reset the state of this connection
     */
    protected void reset() {
        //Logs.log("Connection reset");
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
        //Logs.log("close connection");
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
        Content sub = (Content) origin.subSequence(start, end);
        if (flags == GET_TEXT_WITH_STYLES) {
            sub.beginStreamCharGetting(0);
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
        //Logs.log("getSelectedText()");
        //This text should be limited because when the user try to select all text
        //it can be quite large text and costs time, which will finally cause ANR
        int left = getCursor().getLeft();
        int right = getCursor().getRight();
        if (right - left > TEXT_LENGTH_LIMIT) {
            right = left + TEXT_LENGTH_LIMIT;
        }
        return left == right ? null : getTextRegion(left, right, flags);
    }

    @Override
    public CharSequence getTextBeforeCursor(int length, int flags) {
        //Logs.log("getTextBeforeCursor()");
        int start = getCursor().getLeft();
        return getTextRegion(start - length, start, flags);
    }

    @Override
    public CharSequence getTextAfterCursor(int length, int flags) {
        //Logs.log("getTextAfterCursor()");
        int end = getCursor().getRight();
        return getTextRegion(end, end + length, flags);
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        //Logs.log("Commit text: text = " + text + ", newCursorPosition = " + newCursorPosition);
        //Log.d(LOG_TAG, "commit text:text = " + text + ", newCur = " + newCursorPosition);
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
        if (text.length() == 1 && mEditor.isSymbolCompletionEnabled()) {
            replacement = mEditor.mLanguageSymbolPairs.getCompletion(text.charAt(0));
        }
        // newCursorPosition ignored
        // Call onCommitText() can make auto indent and delete text selected automatically
        if (replacement == null || replacement == SymbolPairMatch.Replacement.NO_REPLACEMENT) {
            getCursor().onCommitText(text, applyAutoIndent);
        } else {
            getCursor().onCommitText(replacement.text, applyAutoIndent);
            int delta = (replacement.text.length() - replacement.selection);
            if (delta != 0) {
                int newSel = Math.max(getCursor().getLeft() - delta, 0);
                CharPosition charPosition = getCursor().getIndexer().getCharPosition(newSel);
                mEditor.setSelection(charPosition.line, charPosition.column);
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
        //Logs.log("deleteSurroundingText: before = " + beforeLength + ", after = " + afterLength);
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
        //Logs.log("beginBatchEdit()");
        return mEditor.getText().beginBatchEdit();
    }

    @Override
    public synchronized boolean endBatchEdit() {
        //Logs.log("endBatchEdit()");
        boolean inBatch = mEditor.getText().endBatchEdit();
        if (!inBatch) {
            mEditor.updateSelection();
        }
        return inBatch;
    }

    private void deleteSelected() {
        if (getCursor().isSelected()) {
            // Delete selected text
            getCursor().onDeleteKeyPressed();
        }
    }

    @Override
    public boolean setComposingText(CharSequence text, int newCursorPosition) {
        if (!mEditor.isEditable() || mInvalid) {
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
            getCursor().onCommitText(text);
        } else {
            // Already have composing text
            // Delete first
            if (mComposingStart != mComposingEnd) {
                mEditor.getText().delete(mComposingLine, mComposingStart, mComposingLine, mComposingEnd);
            }
            // Reset range
            mComposingEnd = mComposingStart + text.length();
            mEditor.getText().insert(mComposingLine, mComposingStart, text);
        }
        if (text.length() == 0) {
            finishComposingText();
            return true;
        }
        return true;
    }

    @Override
    public boolean finishComposingText() {
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
        if (!mEditor.isEditable() || mInvalid) {
            return false;
        }
        start = getWrappedIndex(start);
        end = getWrappedIndex(end);
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        mEditor.getAutoCompleteWindow().hide();
        Content content = mEditor.getText();
        CharPosition startPos = content.getIndexer().getCharPosition(start);
        CharPosition endPos = content.getIndexer().getCharPosition(end);
        mEditor.setSelectionRegion(startPos.line, startPos.column, endPos.line, endPos.column, false);
        return true;
    }

    @Override
    public boolean setComposingRegion(int start, int end) {
        //Logs.log("set composing region:" + start + ".." + end);
        //Log.d(LOG_TAG, "set composing region:" + start + ".." + end);
        if (!mEditor.isEditable() || mInvalid) {
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
                    getCursor().onDeleteKeyPressed();
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
