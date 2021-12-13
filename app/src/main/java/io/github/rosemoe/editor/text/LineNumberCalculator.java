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

