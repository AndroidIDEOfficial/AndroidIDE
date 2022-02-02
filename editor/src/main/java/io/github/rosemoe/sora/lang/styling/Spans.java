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
package io.github.rosemoe.sora.lang.styling;

import java.util.List;

import io.github.rosemoe.sora.text.CharPosition;

/**
 * Spans object saves spans in editor.
 */
public interface Spans {

    /**
     * Adjust spans on insert.
     * Must be implemented.
     */
    void adjustOnInsert(CharPosition start, CharPosition end);

    /**
     * Adjust spans on delete.
     * Must be implemented.
     */
    void adjustOnDelete(CharPosition start, CharPosition end);

    /**
     * Read spans.
     * Must be implemented.
     */
    Reader read();

    /**
     * Check whether the class supports {@link #modify()}
     */
    boolean supportsModify();

    /**
     * Modify the content.
     *
     * Optional to implement.
     */
    Modifier modify();

    /**
     * Get line count of the spans
     */
    int getLineCount();

    /**
     * Reader reads the spans in a {@link Spans} object.
     */
    interface Reader {

        /**
         * Start reading the spans on the given line.
         * You may prepare some data here if the actual spans are not stored by {@link Span} objects.
         *
         * line may be -1 to release the reader.
         */
        void moveToLine(int line);

        /**
         * Get span count on current line
         */
        int getSpanCount();

        /**
         * Get span at position {@code index}.
         * The result object is read-only. Callers should not modify this object.
         */
        Span getSpanAt(int index);

        /**
         * Get all spans on the given line. This ignores the line argument set by {@link Reader#moveToLine(int)}
         * The list contains at least 1 span. And the result list is unmodifiable.
         */
        List<Span> getSpansOnLine(int line);

    }

    /**
     * Modifier updates the spans in a {@link Spans} object.
     */
    interface Modifier {

        /**
         * Set the line's spans to the new ones. The given {@code spans} list should not be stored,
         * but the content of it can be copied.
         * <p>
         * If the line index exceeds the current capacity, implementation of this should expand the capacity
         * without throwing an exception. Set spans of the filled lines to color {@link io.github.rosemoe.sora.widget.schemes.EditorColorScheme#TEXT_NORMAL}
         * or extends previous styles.
         */
        void setSpansOnLine(int line, List<Span> spans);

        /**
         * Add a line at the given position.
         * The given {@code spans} list should not be stored,
         * but the content of it can be copied.
         */
        void addLineAt(int line, List<Span> spans);

        /**
         * Remove a line
         */
        void deleteLineAt(int line);

    }

}
