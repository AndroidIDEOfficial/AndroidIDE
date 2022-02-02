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

import java.util.NoSuchElementException;

/**
 * Row iterator.
 * This iterator is able to return a series of Row objects linearly
 * Editor uses this to get information of rows and paint them accordingly
 *
 * @author Rose
 */
public interface RowIterator {

    /**
     * Return next Row object
     * <p>
     * The result should not be stored, because implementing classes will always return the same
     * object due to performance
     *
     * @return Row object contains the information about a row
     * @throws NoSuchElementException If no more row available
     */
    Row next();

    /**
     * Whether there is more Row object
     *
     * @return Whether more row available
     */
    boolean hasNext();

    /**
     * Reset the position to its original position.
     *
     * This can be useful when the elements should be iterated for
     * several times.
     */
    void reset();

}
