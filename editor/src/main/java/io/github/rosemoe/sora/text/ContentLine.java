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

import android.text.GetChars;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import io.github.rosemoe.sora.annotations.UnsupportedUserUsage;

public class ContentLine implements CharSequence, GetChars {

    @UnsupportedUserUsage
    public char[] value;

    private int length;

    /**
     * Id in BinaryHeap
     */
    private int id;

    /**
     * Measured width of line
     */
    private int width;

    /**
     * Width of each character inside
     */
    @UnsupportedUserUsage
    public float[] widthCache;
    public long timestamp;

    public ContentLine() {
        this(true);
    }

    public ContentLine(CharSequence text) {
        this(true);
        insert(0, text);
    }

    private ContentLine(boolean initialize) {
        if (initialize) {
            length = 0;
            value = new char[32];
        }
        id = -1;
        width = 0;
    }

    static int lastIndexOf(char[] source, int sourceCount,
                           char[] target, int targetCount,
                           int fromIndex) {
        /*
         * Check arguments; return immediately where possible. For
         * consistency, don't check for null str.
         */
        int rightIndex = sourceCount - targetCount;
        if (fromIndex < 0) {
            return -1;
        }
        if (fromIndex > rightIndex) {
            fromIndex = rightIndex;
        }
        /* Empty string always matches. */
        if (targetCount == 0) {
            return fromIndex;
        }

        int strLastIndex = targetCount - 1;
        char strLastChar = target[strLastIndex];
        int min = targetCount - 1;
        int i = min + fromIndex;

        startSearchForLastChar:
        while (true) {
            while (i >= min && source[i] != strLastChar) {
                i--;
            }
            if (i < min) {
                return -1;
            }
            int j = i - 1;
            int start = j - (targetCount - 1);
            int k = strLastIndex - 1;

            while (j > start) {
                if (source[j--] != target[k--]) {
                    i--;
                    continue startSearchForLastChar;
                }
            }
            return start + 1;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private void checkIndex(int index) {
        if (index < 0 || index > length) {
            throw new StringIndexOutOfBoundsException("index = " + index + ", length = " + length);
        }
    }

    private void ensureCapacity(int capacity) {
        if (value.length < capacity) {
            int newLength = value.length * 2 < capacity ? capacity + 2 : value.length * 2;
            char[] newValue = new char[newLength];
            System.arraycopy(value, 0, newValue, 0, length);
            value = newValue;
        }
    }

    /**
     * Inserts the specified {@code CharSequence} into this sequence.
     * <p>
     * The characters of the {@code CharSequence} argument are inserted,
     * in order, into this sequence at the indicated offset, moving up
     * any characters originally above that position and increasing the length
     * of this sequence by the length of the argument s.
     * <p>
     * The result of this method is exactly the same as if it were an
     * invocation of this object's
     * {@link #insert(int, CharSequence, int, int) insert}(dstOffset, s, 0, s.length())
     * method.
     *
     * <p>If {@code s} is {@code null}, then the four characters
     * {@code "null"} are inserted into this sequence.
     *
     * @param dstOffset the offset.
     * @param s         the sequence to be inserted
     * @return a reference to this object.
     * @throws IndexOutOfBoundsException if the offset is invalid.
     */
    public ContentLine insert(int dstOffset, CharSequence s) {
        if (s == null)
            s = "null";
        if (s instanceof String)
            return this.insert(dstOffset, (String) s);
        return this.insert(dstOffset, s, 0, s.length());
    }

    /**
     * Inserts a subsequence of the specified {@code CharSequence} into
     * this sequence.
     * <p>
     * The subsequence of the argument {@code s} specified by
     * {@code start} and {@code end} are inserted,
     * in order, into this sequence at the specified destination offset, moving
     * up any characters originally above that position. The length of this
     * sequence is increased by {@code end - start}.
     * <p>
     * The character at index <i>k</i> in this sequence becomes equal to:
     * <ul>
     * <li>the character at index <i>k</i> in this sequence, if
     * <i>k</i> is less than {@code dstOffset}
     * <li>the character at index <i>k</i>{@code +start-dstOffset} in
     * the argument {@code s}, if <i>k</i> is greater than or equal to
     * {@code dstOffset} but is less than {@code dstOffset+end-start}
     * <li>the character at index <i>k</i>{@code -(end-start)} in this
     * sequence, if <i>k</i> is greater than or equal to
     * {@code dstOffset+end-start}
     * </ul><p>
     * The {@code dstOffset} argument must be greater than or equal to
     * {@code 0}, and less than or equal to the {@linkplain #length() length}
     * of this sequence.
     * <p>The start argument must be nonnegative, and not greater than
     * {@code end}.
     * <p>The end argument must be greater than or equal to
     * {@code start}, and less than or equal to the length of s.
     *
     * <p>If {@code s} is {@code null}, then this method inserts
     * characters as if the s parameter was a sequence containing the four
     * characters {@code "null"}.
     *
     * @param dstOffset the offset in this sequence.
     * @param s         the sequence to be inserted.
     * @param start     the starting index of the subsequence to be inserted.
     * @param end       the end index of the subsequence to be inserted.
     * @return a reference to this object.
     * @throws IndexOutOfBoundsException if {@code dstOffset}
     *                                   is negative or greater than {@code this.length()}, or
     *                                   {@code start} or {@code end} are negative, or
     *                                   {@code start} is greater than {@code end} or
     *                                   {@code end} is greater than {@code s.length()}
     */
    public ContentLine insert(int dstOffset, CharSequence s,
                              int start, int end) {
        if (s == null)
            s = "null";
        if ((dstOffset < 0) || (dstOffset > this.length()))
            throw new IndexOutOfBoundsException("dstOffset " + dstOffset);
        if ((start < 0) || (end < 0) || (start > end) || (end > s.length()))
            throw new IndexOutOfBoundsException(
                    "start " + start + ", end " + end + ", s.length() "
                            + s.length());
        int len = end - start;
        ensureCapacity(length + len);
        System.arraycopy(value, dstOffset, value, dstOffset + len,
                length - dstOffset);
        for (int i = start; i < end; i++)
            value[dstOffset++] = s.charAt(i);
        length += len;
        return this;
    }

    /**
     * Removes the characters in a substring of this sequence.
     * The substring begins at the specified {@code start} and extends to
     * the character at index {@code end - 1} or to the end of the
     * sequence if no such character exists. If
     * {@code start} is equal to {@code end}, no changes are made.
     *
     * @param start The beginning index, inclusive.
     * @param end   The ending index, exclusive.
     * @return This object.
     * @throws StringIndexOutOfBoundsException if {@code start}
     *                                         is negative, greater than {@code length()}, or
     *                                         greater than {@code end}.
     */
    public ContentLine delete(int start, int end) {
        if (start < 0)
            throw new StringIndexOutOfBoundsException(start);
        if (end > length)
            end = length;
        if (start > end)
            throw new StringIndexOutOfBoundsException();
        int len = end - start;
        if (len > 0) {
            System.arraycopy(value, start + len, value, start, length - end);
            length -= len;
        }
        return this;
    }

    public ContentLine insert(int offset, char c) {
        ensureCapacity(length + 1);
        System.arraycopy(value, offset, value, offset + 1, length - offset);
        value[offset] = c;
        length += 1;
        return this;
    }

    public ContentLine append(CharSequence s, int start, int end) {
        if (s == null)
            s = "null";
        if ((start < 0) || (start > end) || (end > s.length()))
            throw new IndexOutOfBoundsException(
                    "start " + start + ", end " + end + ", s.length() "
                            + s.length());
        int len = end - start;
        ensureCapacity(length + len);
        for (int i = start, j = length; i < end; i++, j++)
            value[j] = s.charAt(i);
        length += len;
        return this;
    }

    public ContentLine append(CharSequence text) {
        return this.insert(length, text);
    }

    public int indexOf(CharSequence text, int index) {
        return TextUtils.indexOf(this, text, index);
    }

    public int lastIndexOf(String str, int fromIndex) {
        return lastIndexOf(value, length,
                str.toCharArray(), str.length(), fromIndex);
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        checkIndex(index);
        return value[index];
    }

    @Override
    @NonNull
    public ContentLine subSequence(int start, int end) {
        checkIndex(start);
        checkIndex(end);
        if (end < start) {
            throw new StringIndexOutOfBoundsException("start is bigger than end");
        }
        char[] newValue = new char[end - start + 16];
        System.arraycopy(value, start, newValue, 0, end - start);
        ContentLine res = new ContentLine(false);
        res.value = newValue;
        res.length = end - start;
        return res;
    }

    /**
     * A quick method to append itself to a StringBuilder
     */
    public void appendTo(StringBuilder sb) {
        sb.append(value, 0, length);
    }

    @Override
    @NonNull
    public String toString() {
        return new String(value, 0, length);
    }

    public char[] getRawData() {
        return value;
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        if (srcBegin < 0)
            throw new StringIndexOutOfBoundsException(srcBegin);
        if ((srcEnd < 0) || (srcEnd > length))
            throw new StringIndexOutOfBoundsException(srcEnd);
        if (srcBegin > srcEnd)
            throw new StringIndexOutOfBoundsException("srcBegin > srcEnd");
        System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
    }

}
