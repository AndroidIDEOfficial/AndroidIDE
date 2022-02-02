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
package io.github.rosemoe.sora.text;

import io.github.rosemoe.sora.util.IntPair;

/**
 * The cursor position will update automatically when the content has been changed by other ways.
 *
 * @author Rosemoe
 */
public final class Cursor {

    private final Content mContent;
    private final CachedIndexer mIndexer;
    private CharPosition mLeft, mRight;
    private CharPosition cache0, cache1, cache2;

    /**
     * Create a new Cursor for Content
     *
     * @param content Target content
     */
    public Cursor(Content content) {
        mContent = content;
        mIndexer = new CachedIndexer(content);
        mLeft = new CharPosition().zero();
        mRight = new CharPosition().zero();
    }

    /**
     * Whether the given character is a white space character
     *
     * @param c Character to check
     * @return Result whether a space char
     */
    private static boolean isWhitespace(char c) {
        return (c == '\t' || c == ' ');
    }

    /**
     * Make left and right cursor on the given position
     *
     * @param line   The line position
     * @param column The column position
     */
    public void set(int line, int column) {
        setLeft(line, column);
        setRight(line, column);
    }

    /**
     * Make left cursor on the given position
     *
     * @param line   The line position
     * @param column The column position
     */
    public void setLeft(int line, int column) {
        mLeft = mIndexer.getCharPosition(line, column).fromThis();
    }

    /**
     * Make right cursor on the given position
     *
     * @param line   The line position
     * @param column The column position
     */
    public void setRight(int line, int column) {
        mRight = mIndexer.getCharPosition(line, column).fromThis();
    }

    /**
     * Get the left cursor line
     *
     * @return line of left cursor
     */
    public int getLeftLine() {
        return mLeft.getLine();
    }

    /**
     * Get the left cursor column
     *
     * @return column of left cursor
     */
    public int getLeftColumn() {
        return mLeft.getColumn();
    }

    /**
     * Get the right cursor line
     *
     * @return line of right cursor
     */
    public int getRightLine() {
        return mRight.getLine();
    }

    /**
     * Get the right cursor column
     *
     * @return column of right cursor
     */
    public int getRightColumn() {
        return mRight.getColumn();
    }

    /**
     * Whether the given position is in selected region
     *
     * @param line   The line to query
     * @param column The column to query
     * @return Whether is in selected region
     */
    public boolean isInSelectedRegion(int line, int column) {
        if (line >= getLeftLine() && line <= getRightLine()) {
            boolean yes = true;
            if (line == getLeftLine()) {
                yes = column >= getLeftColumn();
            }
            if (line == getRightLine()) {
                yes = yes && column < getRightColumn();
            }
            return yes;
        }
        return false;
    }

    /**
     * Get the left cursor index
     *
     * @return index of left cursor
     */
    public int getLeft() {
        return mLeft.index;
    }

    /**
     * Get the right cursor index
     *
     * @return index of right cursor
     */
    public int getRight() {
        return mRight.index;
    }

    /**
     * Notify the Indexer to update its cache for current display position
     * <p>
     * This will make querying actions quicker
     * <p>
     * Especially when the editor user want to set a new cursor position after scrolling long time
     *
     * @param line First visible line
     */
    public void updateCache(int line) {
        mIndexer.getCharIndex(line, 0);
    }

    /**
     * Get the using Indexer object
     *
     * @return Using Indexer
     */
    public CachedIndexer getIndexer() {
        return mIndexer;
    }

    /**
     * Get whether text is selected
     *
     * @return Whether selected
     */
    public boolean isSelected() {
        return mLeft.index != mRight.index;
    }

    /**
     * Get position after moving left once
     * @param position A packed pair (line, column) describing the original position
     * @return A packed pair (line, column) describing the result position
     */
    public long getLeftOf(long position) {
        int line = IntPair.getFirst(position);
        int column = IntPair.getSecond(position);
        if (column - 1 >= 0) {
            column = TextLayoutHelper.get().getCurPosLeft(column, mContent.getLine(line));
            return IntPair.pack(line, column);
        } else {
            if (line == 0) {
                return 0;
            } else {
                int c_column = mContent.getColumnCount(line - 1);
                return IntPair.pack(line - 1, c_column);
            }
        }
    }

    /**
     * Get position after moving right once
     * @param position A packed pair (line, column) describing the original position
     * @return A packed pair (line, column) describing the result position
     */
    public long getRightOf(long position) {
        int line = IntPair.getFirst(position);
        int column = IntPair.getSecond(position);
        int c_column = mContent.getColumnCount(line);

        if (column + 1 <= c_column) {
            column = TextLayoutHelper.get().getCurPosRight(column, mContent.getLine(line));
           return IntPair.pack(line, column);
        } else {
            if (line + 1 == mContent.getLineCount()) {
                return IntPair.pack(line, c_column);
            } else {
                return IntPair.pack(line + 1, 0);
            }
        }
    }

    /**
     * Get position after moving up once
     * @param position A packed pair (line, column) describing the original position
     * @return A packed pair (line, column) describing the result position
     */
    public long getUpOf(long position) {
        int line = IntPair.getFirst(position);
        int column = IntPair.getSecond(position);
        if (line - 1 < 0) {
            return IntPair.pack(0, 0);
        }
        int c_column = mContent.getColumnCount(line - 1);
        if (column > c_column) {
            column = c_column;
        }
        return IntPair.pack(line - 1, column);
    }

    /**
     * Get position after moving down once
     * @param position A packed pair (line, column) describing the original position
     * @return A packed pair (line, column) describing the result position
     */
    public long getDownOf(long position) {
        int line = IntPair.getFirst(position);
        int column = IntPair.getSecond(position);
        int c_line = mContent.getLineCount();
        if (line + 1 >= c_line) {
            return IntPair.pack(line, mContent.getColumnCount(line));
        } else {
            int c_column = mContent.getColumnCount(line + 1);
            if (column > c_column) {
                column = c_column;
            }
            return IntPair.pack(line + 1, column);
        }
    }

    /**
     * Get copy of left cursor
     */
    public CharPosition left() {
        return mLeft.fromThis();
    }

    /**
     * Get copy of right cursor
     */
    public CharPosition right() {
        return mRight.fromThis();
    }

    /**
     * Internal call back before insertion
     *
     * @param startLine   Start line
     * @param startColumn Start column
     */
    void beforeInsert(int startLine, int startColumn) {
        cache0 = mIndexer.getCharPosition(startLine, startColumn).fromThis();
    }

    /**
     * Internal call back before deletion
     *
     * @param startLine   Start line
     * @param startColumn Start column
     * @param endLine     End line
     * @param endColumn   End column
     */
    void beforeDelete(int startLine, int startColumn, int endLine, int endColumn) {
        cache1 = mIndexer.getCharPosition(startLine, startColumn).fromThis();
        cache2 = mIndexer.getCharPosition(endLine, endColumn).fromThis();
    }

    /**
     * Internal call back before replace
     */
    void beforeReplace() {
        mIndexer.beforeReplace(mContent);
    }

    /**
     * Internal call back after insertion
     *
     * @param startLine       Start line
     * @param startColumn     Start column
     * @param endLine         End line
     * @param endColumn       End column
     * @param insertedContent Inserted content
     */
    void afterInsert(int startLine, int startColumn, int endLine, int endColumn,
                     CharSequence insertedContent) {
        mIndexer.afterInsert(mContent, startLine, startColumn, endLine, endColumn, insertedContent);
        int beginIdx = cache0.getIndex();
        if (getLeft() >= beginIdx) {
            mLeft = mIndexer.getCharPosition(getLeft() + insertedContent.length()).fromThis();
        }
        if (getRight() >= beginIdx) {
            mRight = mIndexer.getCharPosition(getRight() + insertedContent.length()).fromThis();
        }
    }

    /**
     * Internal call back
     *
     * @param startLine      Start line
     * @param startColumn    Start column
     * @param endLine        End line
     * @param endColumn      End column
     * @param deletedContent Deleted content
     */
    void afterDelete(int startLine, int startColumn, int endLine, int endColumn,
                     CharSequence deletedContent) {
        mIndexer.afterDelete(mContent, startLine, startColumn, endLine, endColumn, deletedContent);
        int beginIdx = cache1.getIndex();
        int endIdx = cache2.getIndex();
        int left = getLeft();
        int right = getRight();
        if (beginIdx > right) {
            return;
        }
        if (endIdx <= left) {
            mLeft = mIndexer.getCharPosition(left - (endIdx - beginIdx)).fromThis();
            mRight = mIndexer.getCharPosition(right - (endIdx - beginIdx)).fromThis();
        } else if (/* endIdx > left && */ endIdx < right) {
            if (beginIdx <= left) {
                mLeft = mIndexer.getCharPosition(beginIdx).fromThis();
                mRight = mIndexer.getCharPosition(right - (endIdx - left)).fromThis();
            } else {
                mRight = mIndexer.getCharPosition(right - (endIdx - beginIdx)).fromThis();
            }
        } else {
            if (beginIdx <= left) {
                mLeft = mIndexer.getCharPosition(beginIdx).fromThis();
                mRight = mLeft.fromThis();
            } else {
                mRight = mIndexer.getCharPosition(left + (right - beginIdx)).fromThis();
            }
        }
    }

}

