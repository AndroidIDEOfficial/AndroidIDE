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

import io.github.rosemoe.sora.util.IntPair;

/**
 * This a data class of a character position in {@link Content}
 *
 * @author Rosemoe
 */
public final class CharPosition {

    public int index;

    public int line;

    public int column;

    /**
     * Get the index
     *
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Get column
     *
     * @return column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Get line
     *
     * @return line
     */
    public int getLine() {
        return line;
    }

    /**
     * Make this CharPosition zero and return self
     *
     * @return self
     */
    public CharPosition zero() {
        index = line = column = 0;
        return this;
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof CharPosition) {
            CharPosition a = (CharPosition) another;
            return a.column == column &&
                    a.line == line &&
                    a.index == index;
        }
        return false;
    }

    /**
     * Convert {@link CharPosition#line} and {@link CharPosition#column} to a Long number
     *
     * First integer is line and second integer is column
     * @return A Long integer describing the position
     */
    public long toIntPair() {
        return IntPair.pack(line, column);
    }

    /**
     * Make a copy of this CharPosition and return the copy
     *
     * @return New CharPosition including info of this CharPosition
     */
    @NonNull
    public CharPosition fromThis() {
        var pos = new CharPosition();
        pos.set(this);
        return pos;
    }

    /**
     * Set this {@link CharPosition} object's data the same as {@code another}
     */
    public void set(@NonNull CharPosition another) {
        index = another.index;
        line = another.line;
        column = another.column;
    }

    @NonNull
    @Override
    public String toString() {
        return "CharPosition(line = " + line + ",column = " + column + ",index = " + index + ")";
    }

}
