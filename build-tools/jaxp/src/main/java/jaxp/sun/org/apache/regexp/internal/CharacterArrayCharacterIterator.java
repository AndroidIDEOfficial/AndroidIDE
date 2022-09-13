/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jaxp.sun.org.apache.regexp.internal;

/**
 * Encapsulates char[] as CharacterIterator
 *
 * @author <a href="mailto:ales.novak@netbeans.com">Ales Novak</a>
 */
public final class CharacterArrayCharacterIterator implements CharacterIterator
{
    /** encapsulated */
    private final char[] src;
    /** offset in the char array */
    private final int off;
    /** used portion of the array */
    private final int len;

    /** @param src - encapsulated String */
    public CharacterArrayCharacterIterator(char[] src, int off, int len)
    {
        this.src = src;
        this.off = off;
        this.len = len;
    }

    /** @return a substring */
    public String substring(int beginIndex, int endIndex)
    {
        if (endIndex > len) {
            throw new IndexOutOfBoundsException("endIndex=" + endIndex
                                                + "; sequence size=" + len);
        }
        if (beginIndex < 0 || beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("beginIndex=" + beginIndex
                                                + "; endIndex=" + endIndex);
        }
        return new String(src, off + beginIndex, endIndex - beginIndex);
    }

    /** @return a substring */
    public String substring(int beginIndex)
    {
        return substring(beginIndex, len);
    }

    /** @return a character at the specified position. */
    public char charAt(int pos)
    {
        return src[off + pos];
    }

    /** @return <tt>true</tt> iff if the specified index is after the end of the character stream */
    public boolean isEnd(int pos)
    {
        return (pos >= len);
    }
}
