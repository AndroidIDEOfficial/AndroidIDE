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

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Indexer Impl for Content
 * With cache
 *
 * @author Rose
 */
public class CachedIndexer implements Indexer, ContentListener {

    private final Content mContent;
    private final CharPosition mZeroPoint = new CharPosition().zero();
    private final CharPosition mEndPoint = new CharPosition();
    private final List<CharPosition> mCachePositions = new ArrayList<>();
    private final int mSwitchLine = 50;
    private int mSwitchIndex = 50;
    private int mMaxCacheSize = 50;
    private boolean mHandleEvent = true;
    private boolean mHasException = false;

    /**
     * Create a new CachedIndexer for the given content
     *
     * @param content Content to manage
     */
    CachedIndexer(Content content) {
        mContent = content;
        detectException();
    }

    /**
     * If the querying index is larger than the switch
     * We will add its result to cache
     *
     * @param s Switch
     */
    public void setSwitchIndex(int s) {
        mSwitchIndex = s;
    }

    /**
     * Find out whether things unexpected happened
     */
    private void detectException() {
        if (!isHandleEvent() && !mCachePositions.isEmpty()) {
            mHasException = true;
        }
        mEndPoint.index = mContent.length();
        mEndPoint.line = mContent.getLineCount() - 1;
        mEndPoint.column = mContent.getColumnCount(mEndPoint.line);
    }

    /**
     * Throw a new exception for illegal state
     */
    protected void throwIfHas() {
        if (mHasException) {
            throw new IllegalStateException("there is cache but the content changed");
        }
    }

    /**
     * Get nearest cache for the given index
     *
     * @param index Querying index
     * @return Nearest cache
     */
    private CharPosition findNearestByIndex(int index) {
        int min = index, dis = index;
        CharPosition nearestCharPosition = mZeroPoint;
        int targetIndex = 0;
        for (int i = 0;i < mCachePositions.size();i++) {
            CharPosition pos = mCachePositions.get(i);
            dis = Math.abs(pos.index - index);
            if (dis < min) {
                min = dis;
                nearestCharPosition = pos;
                targetIndex = i;
            }
            if (dis <= mSwitchIndex) {
                break;
            }
        }
        if (Math.abs(mEndPoint.index - index) < dis) {
            nearestCharPosition = mEndPoint;
        }
        if (nearestCharPosition != mZeroPoint && nearestCharPosition != mEndPoint) {
            Collections.swap(mCachePositions, targetIndex, 0);
        }
        return nearestCharPosition;
    }

    /**
     * Get nearest cache for the given line
     *
     * @param line Querying line
     * @return Nearest cache
     */
    private CharPosition findNearestByLine(int line) {
        int min = line, dis = line;
        CharPosition nearestCharPosition = mZeroPoint;
        int targetIndex = 0;
        for (int i = 0;i < mCachePositions.size();i++) {
            CharPosition pos = mCachePositions.get(i);
            dis = Math.abs(pos.line - line);
            if (dis < min) {
                min = dis;
                nearestCharPosition = pos;
                targetIndex = i;
            }
            if (min <= mSwitchLine) {
                break;
            }
        }
        if (Math.abs(mEndPoint.line - line) < dis) {
            nearestCharPosition = mEndPoint;
        }
        if (nearestCharPosition != mZeroPoint && nearestCharPosition != mEndPoint) {
            Collections.swap(mCachePositions, 0, targetIndex);
        }
        return nearestCharPosition;
    }

    /**
     * From the given position to find forward in text
     *
     * @param start Given position
     * @param index Querying index
     */
    private void findIndexForward(@NonNull CharPosition start, int index, @NonNull CharPosition dest) {
        if (start.index > index) {
            throw new IllegalArgumentException("Unable to find backward from method findIndexForward()");
        }
        int workLine = start.line;
        int workColumn = start.column;
        int workIndex = start.index;
        //Move the column to the line end
        {
            int column = mContent.getColumnCount(workLine);
            workIndex += column - workColumn;
            workColumn = column;
        }
        while (workIndex < index) {
            workLine++;
            workColumn = mContent.getColumnCount(workLine);
            workIndex += workColumn + 1;
        }
        if (workIndex > index) {
            workColumn -= workIndex - index;
        }
        dest.column = workColumn;
        dest.line = workLine;
        dest.index = index;
    }

    /**
     * From the given position to find backward in text
     *
     * @param start Given position
     * @param index Querying index
     */
    private void findIndexBackward(CharPosition start, int index, CharPosition dest) {
        if (start.index < index) {
            throw new IllegalArgumentException("Unable to find forward from method findIndexBackward()");
        }
        int workLine = start.line;
        int workColumn = start.column;
        int workIndex = start.index;
        while (workIndex > index) {
            workIndex -= workColumn + 1;
            workLine--;
            if (workLine != -1) {
                workColumn = mContent.getColumnCount(workLine);
            } else {
                //Reached the start of text,we have to use findIndexForward() as this method can not handle it
                findIndexForward(mZeroPoint, index, dest);
                return;
            }
        }
        int dColumn = index - workIndex;
        if (dColumn > 0) {
            workLine++;
            workColumn = dColumn - 1;
        }
        dest.column = workColumn;
        dest.line = workLine;
        dest.index = index;
    }

    /**
     * From the given position to find forward in text
     *
     * @param start  Given position
     * @param line   Querying line
     * @param column Querying column
     */
    private void findLiCoForward(CharPosition start, int line, int column, CharPosition dest) {
        if (start.line > line) {
            throw new IllegalArgumentException("can not find backward from findLiCoForward()");
        }
        int workLine = start.line;
        int workIndex = start.index;
        {
            //Make index to left of line
            workIndex = workIndex - start.column;
        }
        while (workLine < line) {
            workIndex += mContent.getColumnCount(workLine) + 1;
            workLine++;
        }
        dest.column = 0;
        dest.line = workLine;
        dest.index = workIndex;
        findInLine(dest, line, column);
    }

    /**
     * From the given position to find backward in text
     *
     * @param start  Given position
     * @param line   Querying line
     * @param column Querying column
     */
    private void findLiCoBackward(CharPosition start, int line, int column, CharPosition dest) {
        if (start.line < line) {
            throw new IllegalArgumentException("can not find forward from findLiCoBackward()");
        }
        int workLine = start.line;
        int workIndex = start.index;
        {
            //Make index to the left of line
            workIndex = workIndex - start.column;
        }
        while (workLine > line) {
            workIndex -= mContent.getColumnCount(workLine - 1) + 1;
            workLine--;
        }
        dest.column = 0;
        dest.line = workLine;
        dest.index = workIndex;
        findInLine(dest, line, column);
    }

    /**
     * From the given position to find in this line
     *
     * @param pos    Given position
     * @param line   Querying line
     * @param column Querying column
     */
    private void findInLine(CharPosition pos, int line, int column) {
        if (pos.line != line) {
            throw new IllegalArgumentException("can not find other lines with findInLine()");
        }
        pos.index = pos.index - pos.column + column;
        pos.line = line;
        pos.column = column;
    }

    /**
     * Add new cache
     *
     * @param pos New cache
     */
    private void push(CharPosition pos) {
        if (mMaxCacheSize <= 0) {
            return;
        }
        mCachePositions.add(pos);
        while (mCachePositions.size() > mMaxCacheSize) {
            mCachePositions.remove(0);
        }
    }

    /**
     * Get max cache size
     *
     * @return max cache size
     */
    protected int getMaxCacheSize() {
        return mMaxCacheSize;
    }

    /**
     * Set max cache size
     *
     * @param maxSize max cache size
     */
    protected void setMaxCacheSize(int maxSize) {
        mMaxCacheSize = maxSize;
    }

    /**
     * For NoCacheIndexer
     *
     * @return whether handle changes
     */
    protected boolean isHandleEvent() {
        return mHandleEvent;
    }

    /**
     * For NoCacheIndexer
     *
     * @param handle Whether handle changes to refresh cache
     */
    protected void setHandleEvent(boolean handle) {
        mHandleEvent = handle;
    }

    @Override
    public int getCharIndex(int line, int column) {
        return getCharPosition(line, column).index;
    }

    @Override
    public int getCharLine(int index) {
        return getCharPosition(index).line;
    }

    @Override
    public int getCharColumn(int index) {
        return getCharPosition(index).column;
    }

    @NonNull
    @Override
    public CharPosition getCharPosition(int index) {
        var pos = new CharPosition();
        getCharPosition(index, pos);
        return pos;
    }

    @Override
    public void getCharPosition(int index, @NonNull CharPosition dest) {
        throwIfHas();
        mContent.checkIndex(index);
        CharPosition pos = findNearestByIndex(index);
        if (pos.index == index) {
            dest.set(pos);
        } else if (pos.index < index) {
            findIndexForward(pos, index, dest);
        } else {
            findIndexBackward(pos, index, dest);
        }
        if (Math.abs(index - pos.index) >= mSwitchIndex) {
            push(dest.fromThis());
        }
    }

    @NonNull
    @Override
    public CharPosition getCharPosition(int line, int column) {
        var pos = new CharPosition();
        getCharPosition(line, column, pos);
        return pos;
    }

    @Override
    public void getCharPosition(int line, int column, @NonNull CharPosition dest) {
        throwIfHas();
        mContent.checkLineAndColumn(line, column, true);
        CharPosition pos = findNearestByLine(line);
        if (pos.line == line) {
            dest.set(pos);
            if (pos.column == column) {
                return;
            }
            findInLine(dest, line, column);
        } else if (pos.line < line) {
            findLiCoForward(pos, line, column, dest);
        } else {
            findLiCoBackward(pos, line, column, dest);
        }
        if (Math.abs(pos.line - line) > mSwitchLine) {
            push(dest.fromThis());
        }
    }

    @Override
    public void beforeReplace(Content content) {
        //Do nothing
    }

    @Override
    public void afterInsert(Content content, int startLine, int startColumn, int endLine, int endColumn,
                            CharSequence insertedContent) {
        if (isHandleEvent()) {
            for (CharPosition pos : mCachePositions) {
                if (pos.line == startLine) {
                    if (pos.column >= startColumn) {
                        pos.index += insertedContent.length();
                        pos.line += endLine - startLine;
                        pos.column = endColumn + pos.column - startColumn;
                    }
                } else if (pos.line > startLine) {
                    pos.index += insertedContent.length();
                    pos.line += endLine - startLine;
                }
            }
        }
        detectException();
    }

    @Override
    public void afterDelete(Content content, int startLine, int startColumn, int endLine, int endColumn,
                            CharSequence deletedContent) {
        if (isHandleEvent()) {
            List<CharPosition> garbage = new ArrayList<>();
            for (CharPosition pos : mCachePositions) {
                if (pos.line == startLine) {
                    if (pos.column >= startColumn)
                        garbage.add(pos);
                } else if (pos.line > startLine) {
                    if (pos.line < endLine) {
                        garbage.add(pos);
                    } else if (pos.line == endLine) {
                        garbage.add(pos);
                    } else {
                        pos.index -= deletedContent.length();
                        pos.line -= endLine - startLine;
                    }
                }
            }
            mCachePositions.removeAll(garbage);
        }
        detectException();
    }

}

