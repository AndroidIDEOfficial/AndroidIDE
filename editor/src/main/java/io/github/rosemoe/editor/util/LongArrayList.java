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
package io.github.rosemoe.editor.util;

/**
 * ArrayList for primitive type long
 */
public class LongArrayList {

    private long[] data;
    private int length;

    public LongArrayList() {
        data = new long[128];
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
