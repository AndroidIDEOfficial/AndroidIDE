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
package io.github.rosemoe.sora.util;

/**
 * ArrayList for primitive type long
 */
public class LongArrayList {

    private long[] data;
    private int length;

    public LongArrayList() {
        data = new long[64];
    }

    public void add(long value) {
        data[length++] = value;
        if (data.length == length) {
            long[] newData = new long[length << 1];
            System.arraycopy(data, 0, newData, 0, length);
            data = newData;
        }
    }

    public int size() {
        return length;
    }

    public long get(int index) {
        if (index > length || index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return data[index];
    }

    public void clear() {
        length = 0;
    }

}
