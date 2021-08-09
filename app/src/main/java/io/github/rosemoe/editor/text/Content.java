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
package io.github.rosemoe.editor.text;

import java.util.ArrayList;
import java.util.List;

import io.github.rosemoe.editor.annotations.Experimental;
import io.github.rosemoe.struct.BlockLinkedList;
import io.github.rosemoe.editor.widget.CodeEditor;

/**
 * This class saves the text content for editor and maintains line widths
 *
 * @author Rose
 */
public class Content implements CharSequence {

    public final static int DEFAULT_MAX_UNDO_STACK_SIZE = 100;
    public final static int DEFAULT_LIST_CAPACITY = 1000;

    private static int sInitialListCapacity;

    static {
        setInitialLineCapacity(DEFAULT_LIST_CAPACITY);
    }

    private List<ContentLine> mLines;
    private int mTextLength;
    private int mNestedBatchEdit;
    private List<ContentListener> mListeners;
    private Indexer mIndexer;
    private UndoManager mUndoManager;
    private Cursor mCursor;
    private LineRemoveListener mLineListener;
    
    private final CodeEditor editor;

    /**
     * Use a BlockLinkedList instead of ArrayList.
     * <p>
     * This can be faster while inserting in large text.
     * But in other conditions, it is quite slow.
     * <p>
     * Disabled by default.
     */
    @Experimental
    public static boolean useBlock = false;

    /**
     * This constructor will create a Content object with no text
     */
    public Content(CodeEditor editor) {
        this(editor, null);
    }

    /**
     * This constructor will create a Content object with the given source
     * If you give us null,it will just create a empty Content object
     *
     * @param src The source of Content
     */
    public Content(CodeEditor editor, CharSequence src) {
        if (src == null) {
            src = "";
        }
        this.editor = editor;
        mTextLength = 0;
        mNestedBatchEdit = 0;
        if (!useBlock)
            mLines = new ArrayList<>(getInitialLineCapacity());
        else
            mLines = new BlockLinkedList<>(5000);
        mLines.add(new ContentLine());
        mListeners = new ArrayList<>();
        mUndoManager = new UndoManager(this);
        setMaxUndoStackSize(Content.DEFAULT_MAX_UNDO_STACK_SIZE);
        mIndexer = new NoCacheIndexer(this);
        if (src.length() == 0) {
            setUndoEnabled(true);
            return;
        }
        setUndoEnabled(false);
        insert(0, 0, src);
        setUndoEnabled(true);
    }

    /**
     * Returns the default capacity of text line list
     *
     * @return Default capacity
     */
    public static int getInitialLineCapacity() {
        return Content.sInitialListCapacity;
    }

    /**
     * Set the default capacity of text line list
     *
     * @param capacity Default capacity
     */
    public static void setInitialLineCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity can not be negative or zero");
        }
        sInitialListCapacity = capacity;
    }

    /**
     * Test whether the two ContentLine have the same content
     *
     * @param a ContentLine
     * @param b another ContentLine
     * @return Whether equals in content
     */
    private static boolean equals(ContentLine a, ContentLine b) {
        if (a.length() != b.length()) {
            return false;
        }
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public char charAt(int index) {
        checkIndex(index);
        CharPosition p = getIndexer().getCharPosition(index);
        return charAt(p.line, p.column);
    }

    @Override
    public int length() {
        return mTextLength;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start > end) {
            throw new StringIndexOutOfBoundsException("start > end");
        }
        CharPosition s = getIndexer().getCharPosition(start);
        CharPosition e = getIndexer().getCharPosition(end);
        return subContent(s.getLine(), s.getColumn(), e.getLine(), e.getColumn());
    }

    /**
     * Set a line listener
     *
     * @param lis the listener,maybe null
     * @see LineRemoveListener
     */
    public void setLineListener(LineRemoveListener lis) {
        this.mLineListener = lis;
    }

    /**
     * Get the character at the given position
     * If (column == getColumnCount(line)),it returns '\n'
     * IndexOutOfBoundsException is thrown
     *
     * @param line   The line position of character
     * @param column The column position of character
     * @return The character at the given position
     */
    public char charAt(int line, int column) {
        if(!checkLineAndColumn(line, column, true)) return 0;
        if (column == getColumnCount(line)) {
            return '\n';
        }
        return mLines.get(line).charAt(column);
    }

    /**
     * Get raw data of line
     * The result is not expected to be modified
     *
     * @param line Line
     * @return Raw ContentLine used by Content
     */
    public ContentLine getLine(int line) {
        return mLines.get(line);
    }

    /**
     * Get how many lines there are
     *
     * @return Line count
     */
    public int getLineCount() {
        return mLines.size();
    }

    /**
     * Get how many characters is on the given line
     * If (line < 0 or line >= getLineCount()),it will throw a IndexOutOfBoundsException
     *
     * @param line The line to get
     * @return Character count on line
     */
    public int getColumnCount(int line) {
        checkLine(line);
        return mLines.get(line).length();
    }

    /**
     * Get the given line text without '\n' character
     *
     * @param line The line to get
     * @return New String object of this line
     */
    public String getLineString(int line) {
        checkLine(line);
        return mLines.get(line).toString();
    }

    /**
     * Get characters of line
     */
    public void getLineChars(int line, char[] dest) {
        mLines.get(line).getChars(0, getColumnCount(line), dest, 0);
    }

    /**
     * Transform the (line,column) position to index
     * This task will usually completed by {@link Indexer}
     *
     * @param line   Line of index
     * @param column Column on line of index
     * @return Transformed index for the given arguments
     */
    public int getCharIndex(int line, int column) {
        return getIndexer().getCharIndex(line, column);
    }
	
	/**
	 * Append text to the end of this content
	 *
	 * @param text Text to append
	 */
	public void append(CharSequence text) {
		if(getLineCount() <= 0)
			return;
		final int line = getLineCount() - 1;
		int col = getColumnCount(line);
		if(col < 0)
			col = 0;
		insert(line, col, text);
	}

    /**
     * Insert content to this object
     *
     * @param line   The insertion's line position
     * @param column The insertion's column position
     * @param text   The text you want to insert at the position
     */
    public void insert(int line, int column, CharSequence text) {
        if(!checkLineAndColumn(line, column, true)) return;
        if (text == null) {
            throw new IllegalArgumentException("text can not be null");
        }
        
        text = text.toString().replace("\t", "    ");
        //-----Notify------
        if (mCursor != null)
            mCursor.beforeInsert(line, column);

        int workLine = line;
        int workIndex = column;
        if (workIndex == -1) {
            workIndex = 0;
        }
        ContentLine currLine = mLines.get(workLine);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                ContentLine newLine = new ContentLine();
                newLine.append(currLine, workIndex, currLine.length());
                currLine.delete(workIndex, currLine.length());
                mLines.add(workLine + 1, newLine);
                currLine = newLine;
                workIndex = 0;
                workLine++;
            } else {
                currLine.insert(workIndex, c);
                workIndex++;
            }
        }
        mTextLength += text.length();
        this.dispatchAfterInsert(line, column, workLine, workIndex, text);
    }

    /**
     * Delete character in [start,end)
     *
     * @param start Start position in content
     * @param end   End position in content
     */
    public void delete(int start, int end) {
        checkIndex(start);
        checkIndex(end);
        CharPosition startPos = getIndexer().getCharPosition(start);
        CharPosition endPos = getIndexer().getCharPosition(end);
        if (start != end) {
            delete(startPos.line, startPos.column, endPos.line, endPos.column);
        }
    }

    /**
     * Delete text in the given region
     *
     * @param startLine         The start line position
     * @param columnOnStartLine The start column position
     * @param endLine           The end line position
     * @param columnOnEndLine   The end column position
     */
    public void delete(int startLine, int columnOnStartLine, int endLine, int columnOnEndLine) {
        StringBuilder changedContent = new StringBuilder();
        if (startLine == endLine) {
            if(!checkLineAndColumn(endLine, columnOnEndLine, true)) return;
            if(!checkLineAndColumn(startLine, columnOnStartLine == -1 ? 0 : columnOnStartLine, true)) return;
            int beginIdx = columnOnStartLine;
            if (columnOnStartLine == -1) {
                beginIdx = 0;
            }
            if (beginIdx > columnOnEndLine) {
                throw new IllegalArgumentException("start > end");
            }
            ContentLine curr = mLines.get(startLine);
            int len = curr.length();
            if (beginIdx < 0 || beginIdx > len || columnOnEndLine > len) {
                throw new StringIndexOutOfBoundsException("column start or column end is out of bounds");
            }

            //-----Notify------
            if (mCursor != null)
                if (columnOnStartLine != -1)
                    mCursor.beforeDelete(startLine, columnOnStartLine, endLine, columnOnEndLine);
                else
                    mCursor.beforeDelete(startLine == 0 ? 0 : startLine - 1, startLine == 0 ? 0 : getColumnCount(startLine - 1), endLine, columnOnEndLine);

            changedContent.append(curr, beginIdx, columnOnEndLine);
            curr.delete(beginIdx, columnOnEndLine);
            mTextLength -= columnOnEndLine - columnOnStartLine;
            if (columnOnStartLine == -1) {
                if (startLine == 0) {
                    mTextLength++;
                } else {
                    ContentLine previous = mLines.get(startLine - 1);
                    previous.append(curr);
                    ContentLine rm = mLines.remove(startLine);
                    if (mLineListener != null) {
                        mLineListener.onRemove(this, rm);
                    }
                    changedContent.insert(0, '\n');
                    startLine--;
                    columnOnStartLine = getColumnCount(startLine);
                }
            }
        } else if (startLine < endLine) {
            if(!checkLineAndColumn(startLine, columnOnStartLine, true)) return;
            if(!checkLineAndColumn(endLine, columnOnEndLine, true)) return;

            //-----Notify------
            if (mCursor != null)
                mCursor.beforeDelete(startLine, columnOnStartLine, endLine, columnOnEndLine);

            for (int i = 0; i < endLine - startLine - 1; i++) {
                ContentLine line = mLines.remove(startLine + 1);
                if (mLineListener != null) {
                    mLineListener.onRemove(this, line);
                }
                mTextLength -= line.length() + 1;
                changedContent.append('\n').append(line);
            }
            int currEnd = startLine + 1;
            ContentLine start = mLines.get(startLine);
            ContentLine end = mLines.get(currEnd);
            mTextLength -= start.length() - columnOnStartLine;
            changedContent.insert(0, start, columnOnStartLine, start.length());
            start.delete(columnOnStartLine, start.length());
            mTextLength -= columnOnEndLine;
            changedContent.append('\n').append(end, 0, columnOnEndLine);
            end.delete(0, columnOnEndLine);
            mTextLength--;
            ContentLine r = mLines.remove(currEnd);
            if (mLineListener != null) {
                mLineListener.onRemove(this, r);
            }
            start.append(end);
        } else {
            throw new IllegalArgumentException("start line > end line");
        }
        this.dispatchAfterDelete(startLine, columnOnStartLine, endLine, columnOnEndLine, changedContent);
    }

    /**
     * Replace the text in the given region
     * This action will completed by calling {@link Content#delete(int, int, int, int)} and {@link Content#insert(int, int, CharSequence)}
     *
     * @param startLine         The start line position
     * @param columnOnStartLine The start column position
     * @param endLine           The end line position
     * @param columnOnEndLine   The end column position
     * @param text              The text to replace old text
     */
    public void replace(int startLine, int columnOnStartLine, int endLine, int columnOnEndLine, CharSequence text) {
        if (text == null) {
            throw new IllegalArgumentException("text can not be null");
        }
        this.dispatchBeforeReplace();
        delete(startLine, columnOnStartLine, endLine, columnOnEndLine);
        insert(startLine, columnOnStartLine, text);
    }

    /**
     * When you are going to use {@link CharSequence#charAt(int)} frequently,you are required to call
     * this method.Because the way Content save text,it is usually slow to transform index to
     * (line,column) from the start of text when the text is big.
     * By calling this method,you will be able to get faster because calling this will
     * cause the ITextContent object use a Indexer with cache.
     * The performance is highly improved while linearly getting characters.
     *
     * @param initialIndex The Indexer with cache will take it into this index to its cache
     */
    public void beginStreamCharGetting(int initialIndex) {
        mIndexer = new CachedIndexer(this);
        mIndexer.getCharPosition(initialIndex);
    }

    /**
     * When you finished calling {@link CharSequence#charAt(int)} frequently,you can call this method
     * to free the Indexer with cache.
     * This is not forced.
     */
    public void endStreamCharGetting() {
        mIndexer = new NoCacheIndexer(this);
    }

    /**
     * Undo the last modification
     * NOTE:When there are too much modification,old modification will be deleted from UndoManager
     */
    public void undo() {
        mUndoManager.undo(this);
    }

    /**
     * Redo the last modification
     */
    public void redo() {
        mUndoManager.redo(this);
    }

    /**
     * Whether we can undo
     *
     * @return Whether we can undo
     */
    public boolean canUndo() {
        return mUndoManager.canUndo();
    }

    /**
     * Whether we can redo
     *
     * @return Whether we can redo
     */
    public boolean canRedo() {
        return mUndoManager.canRedo();
    }

    /**
     * Get whether UndoManager is enabled
     *
     * @return Whether UndoManager is enabled
     */
    public boolean isUndoEnabled() {
        return mUndoManager.isUndoEnabled();
    }

    /**
     * Set whether enable the UndoManager.
     * If false,any modification will not be taken down and previous modification that
     * is already in UndoManager will be removed.Does not make changes to content.
     *
     * @param enabled New state for UndoManager
     */
    public void setUndoEnabled(boolean enabled) {
        mUndoManager.setUndoEnabled(enabled);
    }

    /**
     * Get current max stack size of UndoManager
     *
     * @return current max stack size
     */
    public int getMaxUndoStackSize() {
        return mUndoManager.getMaxUndoStackSize();
    }

    /**
     * Set the max size of stack in UndoManager
     *
     * @param maxSize New max size
     */
    public void setMaxUndoStackSize(int maxSize) {
        mUndoManager.setMaxUndoStackSize(maxSize);
    }

    /**
     * A delegate method.
     * Notify the UndoManager to begin batch edit(enter a new layer).
     * NOTE: batch edit in Android can be nested.
     *
     * @return Whether in batch edit
     */
    public boolean beginBatchEdit() {
        mNestedBatchEdit++;
        return isInBatchEdit();
    }

    /**
     * A delegate method.
     * Notify the UndoManager to end batch edit(exit current layer).
     *
     * @return Whether in batch edit
     */
    public boolean endBatchEdit() {
        mNestedBatchEdit--;
        if (mNestedBatchEdit < 0) {
            mNestedBatchEdit = 0;
        }
        return isInBatchEdit();
    }

    /**
     * Returns whether we are in batch edit
     *
     * @return Whether in batch edit
     */
    public boolean isInBatchEdit() {
        return mNestedBatchEdit > 0;
    }

    /**
     * Add a new {@link ContentListener} to the Content
     *
     * @param listener The listener to add
     */
    public void addContentListener(ContentListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener can not be null");
        }
        if (listener instanceof Indexer) {
            throw new IllegalArgumentException("Permission denied");
        }
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    /**
     * Remove the given listener of this Content
     *
     * @param listener The listener to remove
     */
    public void removeContentListener(ContentListener listener) {
        if (listener instanceof Indexer) {
            throw new IllegalArgumentException("Permission denied");
        }
        mListeners.remove(listener);
    }

    /**
     * Get the using {@link Indexer} object
     *
     * @return Indexer for this object
     */
    public Indexer getIndexer() {
        if (mIndexer.getClass() != CachedIndexer.class && mCursor != null) {
            return mCursor.getIndexer();
        }
        return mIndexer;
    }

    /**
     * Quick method to get sub string of this object
     *
     * @param startLine   The start line position
     * @param startColumn The start column position
     * @param endLine     The end line position
     * @param endColumn   The end column position
     * @return sub sequence of this Content
     */
    public Content subContent(int startLine, int startColumn, int endLine, int endColumn) {
        Content c = new Content(this.editor);
        c.setUndoEnabled(false);
        if (startLine == endLine) {
            c.insert(0, 0, mLines.get(startLine).subSequence(startColumn, endColumn));
        } else if (startLine < endLine) {
            c.insert(0, 0, mLines.get(startLine).subSequence(startColumn, mLines.get(startLine).length()));
            for (int i = startLine + 1; i < endLine; i++) {
                c.mLines.add(new ContentLine(mLines.get(i)));
                c.mTextLength += mLines.get(i).length() + 1;
            }
            ContentLine end = mLines.get(endLine);
            c.mLines.add(new ContentLine().insert(0, end, 0, endColumn));
            c.mTextLength += endColumn + 1;
        } else {
            throw new IllegalArgumentException("start > end");
        }
        c.setUndoEnabled(true);
        return c;
    }

    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject instanceof Content) {
            Content content = (Content) anotherObject;
            if (content.getLineCount() != this.getLineCount()) {
                return false;
            }
            for (int i = 0; i < this.getLineCount(); i++) {
                if (!equals(mLines.get(i), content.mLines.get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (ContentLine line : mLines) {
            if (!first) {
                sb.append('\n');
            } else {
                first = false;
            }
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * Get the text in StringBuilder form
     * Used by TextColorProvider
     * This can improve the speed in char getting for tokenizing
     *
     * @return StringBuilder form of Content
     */
    public StringBuilder toStringBuilder() {
        StringBuilder sb = new StringBuilder();
        sb.ensureCapacity(mTextLength + 10);
        boolean first = true;
        final int lines = getLineCount();
        for (int i = 0; i < lines; i++) {
            ContentLine line = mLines.get(i);
            if (!first) {
                sb.append('\n');
            } else {
                first = false;
            }
            line.appendTo(sb);
        }
        return sb;
    }

    /**
     * Get Cursor for editor (Create if there is not)
     *
     * @return Cursor
     */
    public Cursor getCursor() {
        if (mCursor == null) {
            mCursor = new Cursor(this.editor, this);
        }
        return mCursor;
    }

    /**
     * Dispatch events to listener before replacement
     */
    private void dispatchBeforeReplace() {
        mUndoManager.beforeReplace(this);
        if (mCursor != null)
            mCursor.beforeReplace();
        if (mIndexer instanceof ContentListener) {
            ((ContentListener) mIndexer).beforeReplace(this);
        }
        for (ContentListener lis : mListeners) {
            lis.beforeReplace(this);
        }
    }

    /**
     * Dispatch events to listener after deletion
     *
     * @param a Start line
     * @param b Start Column
     * @param c End line
     * @param d End column
     * @param e Text deleted
     */
    private void dispatchAfterDelete(int a, int b, int c, int d, CharSequence e) {
        mUndoManager.afterDelete(this, a, b, c, d, e);
        if (mCursor != null)
            mCursor.afterDelete(a, b, c, d, e);
        if (mIndexer instanceof ContentListener) {
            ((ContentListener) mIndexer).afterDelete(this, a, b, c, d, e);
        }
        for (ContentListener lis : mListeners) {
            lis.afterDelete(this, a, b, c, d, e);
        }
    }

    /**
     * Dispatch events to listener after insertion
     *
     * @param a Start line
     * @param b Start Column
     * @param c End line
     * @param d End column
     * @param e Text deleted
     */
    private void dispatchAfterInsert(int a, int b, int c, int d, CharSequence e) {
        mUndoManager.afterInsert(this, a, b, c, d, e);
        if (mCursor != null)
            mCursor.afterInsert(a, b, c, d, e);
        if (mIndexer instanceof ContentListener) {
            ((ContentListener) mIndexer).afterInsert(this, a, b, c, d, e);
        }
        for (ContentListener lis : mListeners) {
            lis.afterInsert(this, a, b, c, d, e);
        }
    }

    /**
     * Check whether the index is valid
     *
     * @param index Index to check
     */
    protected void checkIndex(int index) {
        if (index > length()) {
            throw new StringIndexOutOfBoundsException("Index " + index + " out of bounds. length:" + length());
        }
    }
	
	public boolean isPositionValid(int line, int column) {
		return checkLineAndColumn(line, column, true);
	}

    /**
     * Check whether the line is valid
     *
     * @param line Line to check
     */
    protected void checkLine(int line) {
        if (line >= getLineCount()) {
            throw new StringIndexOutOfBoundsException("Line " + line + " out of bounds. line count:" + getLineCount());
        }
    }

    /**
     * Check whether the line and column is valid
     *
     * @param line       The line to check
     * @param column     The column to check
     * @param allowEqual Whether allow (column == getColumnCount(line))
     */
    protected boolean checkLineAndColumn(int line, int column, boolean allowEqual) {
        checkLine(line);
        int len = mLines.get(line).length();
        if (column > len || (!allowEqual && column == len)) {
            return false;
        }
        
        return true;
    }

}
