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
package io.github.rosemoe.sora.widget.layout;

import io.github.rosemoe.sora.text.ContentListener;
import io.github.rosemoe.sora.text.LineRemoveListener;

/**
 * Layout is a manager class for editor to display text
 * Different layout may display texts in different way
 * Implementations of this interface should manage 'row's in editor.
 *
 * @author Rose
 */
public interface Layout extends LineRemoveListener, ContentListener {

    /**
     * Called by editor to destroy this layout
     * This means the layout will never be used again
     */
    void destroyLayout();

    /**
     * Get line index of a row in layout
     *
     * @param row The row index in layout
     * @return Line index in text
     */
    int getLineNumberForRow(int row);

    /**
     * Return a {@link RowIterator} object for editor to draw text rows
     *
     * @param initialRow The first row in result iterator
     * @return Iterator contains rows
     */
    RowIterator obtainRowIterator(int initialRow);

    /**
     * Get the width of this layout
     * Editor will use this to compute scroll range
     *
     * @return Width of layout
     */
    int getLayoutWidth();

    /**
     * Get the height of this layout
     * Editor will use this to compute scroll range
     *
     * @return Height of layout
     */
    int getLayoutHeight();

    /**
     * Get character line and column for offsets in layout
     *
     * @param xOffset Horizontal offset on layout
     * @param yOffset Vertical offset on layout
     * @return Packed IntPair, first is line and second is column
     * @see io.github.rosemoe.sora.util.IntPair
     */
    long getCharPositionForLayoutOffset(float xOffset, float yOffset);

    /**
     * Get layout offset of a position in text
     *
     * @param line   The line index
     * @param column Column on line
     * @return An array containing layout offset, first element is the bottom of character and second element is the left of character
     */
    default float[] getCharLayoutOffset(int line, int column) {
        return getCharLayoutOffset(line, column, new float[2]);
    }

    /**
     * Get layout offset of a position in text
     *
     * @param line   The line index
     * @param column Column on line
     * @param array If the array is given, it will try to save the two elements in this array. Otherwise, a new array is created
     * @return An array containing layout offset, first element is the bottom of character and second element is the left of character
     */
    float[] getCharLayoutOffset(int line, int column, float[] array);

    /**
     * Get how many rows are in the given line
     */
    int getRowCountForLine(int line);

}
