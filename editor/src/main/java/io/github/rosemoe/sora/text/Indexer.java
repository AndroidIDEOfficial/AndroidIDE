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

/**
 * A helper class for ITextContent to transform (line,column) and index
 *
 * @author Rose
 */
public interface Indexer {

    /**
     * Get the index of (line,column)
     *
     * @param line   The line position of index
     * @param column The column position of index
     * @return Calculated index
     */
    int getCharIndex(int line, int column);

    /**
     * Get the line position of index
     *
     * @param index The index you want to know its line
     * @return Line position of index
     */
    int getCharLine(int index);

    /**
     * Get the column position of index
     *
     * @param index The index you want to know its column
     * @return Column position of index
     */
    int getCharColumn(int index);

    /**
     * Get the CharPosition for the given index
     *
     * @param index The index you want to get
     * @return The CharPosition object.
     */
    @NonNull
    CharPosition getCharPosition(int index);

    /**
     * Get the CharPosition for the given (line,column)
     *
     * @param line   The line position you want to get
     * @param column The column position you want to get
     * @return The CharPosition object.
     */
    @NonNull
    CharPosition getCharPosition(int line, int column);

    /**
     * @see #getCharPosition(int)
     * @param dest Destination of result
     */
    void getCharPosition(int index, @NonNull CharPosition dest);

    /**
     * @see #getCharPosition(int, int)
     * @param dest Destination of result
     */
    void getCharPosition(int line, int column, @NonNull CharPosition dest);

}
