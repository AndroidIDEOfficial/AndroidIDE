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

import java.text.CharacterIterator;

/**
 * CharacterIterator implementation
 *
 * @author Rosemoe
 */
public class CharSequenceIterator implements CharacterIterator {

    private final CharSequence s;
    private int index;

    public CharSequenceIterator(CharSequence source) {
        s = source;
    }

    @Override
    public char first() {
        index = 0;
        return current();
    }

    @Override
    public char last() {
        index = s.length() - 1;
        if (index < 0) {
            index = 0;
        }
        return current();
    }

    @Override
    public char current() {
        return index == getEndIndex() ? CharacterIterator.DONE : s.charAt(index);
    }

    @Override
    public char next() {
        index++;
        return current();
    }

    @Override
    public char previous() {
        index --;
        if (index < 0) {
            index = 0;
        }
        return current();
    }

    @Override
    public char setIndex(int i) {
        index = i;
        return current();
    }

    @Override
    public int getBeginIndex() {
        return 0;
    }

    @Override
    public int getEndIndex() {
        return s.length();
    }

    @Override
    public int getIndex() {
        return index;
    }

    @NonNull
    @Override
    public Object clone() {
        var another = new CharSequenceIterator(s);
        another.index = index;
        return another;
    }

}
