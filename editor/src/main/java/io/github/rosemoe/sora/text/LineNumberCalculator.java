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

/**
 * A line number calculator for spanner
 *
 * @author Rose
 */
public class LineNumberCalculator {

    private final CharSequence mTarget;
    private final int mLength;
    private int mOffset;
    private int mLine;
    private int mColumn;

    /**
     * Create a new helper for the given text and set offset to start
     *
     * @param target Target text
     */
    public LineNumberCalculator(CharSequence target) {
        mTarget = target;
        mOffset = mLine = mColumn = 0;
        mLength = mTarget.length();
    }

    /**
     * Update line and column for the given advance
     *
     * @param length Advance
     */
    public void update(int length) {
        for (int i = 0; i < length; i++) {
            if (mOffset + i == mLength) {
                break;
            }
            if (mTarget.charAt(mOffset + i) == '\n') {
                mLine++;
                mColumn = 0;
            } else {
                mColumn++;
            }
        }
        mOffset = mOffset + length;
    }

    /**
     * Get line start index
     *
     * @return line start index
     */
    public int findLineStart() {
        return mOffset - mColumn;
    }

    /**
     * Get line end index
     *
     * @return line end index
     */
    public int findLineEnd() {
        int i = 0;
        for (; i + mOffset < mLength; i++) {
            if (mTarget.charAt(mOffset + i) == '\n') {
                break;
            }
        }
        return mOffset + i;
    }

    /**
     * Get current line position
     *
     * @return line
     */
    public int getLine() {
        return mLine;
    }

    /**
     * Get current column position
     *
     * @return column
     */
    public int getColumn() {
        return mColumn;
    }

}

