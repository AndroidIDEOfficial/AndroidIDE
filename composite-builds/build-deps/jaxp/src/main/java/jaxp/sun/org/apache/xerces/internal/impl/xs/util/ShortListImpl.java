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
 * Copyright 2002,2003-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jaxp.sun.org.apache.xerces.internal.impl.xs.util;

import java.util.AbstractList;

import jaxp.sun.org.apache.xerces.internal.xs.ShortList;
import jaxp.sun.org.apache.xerces.internal.xs.XSException;

/**
 * Containts a list of Object's.
 *
 * @xerces.internal
 *
 * @author Sandy Gao, IBM
 *
 * @version $Id: ShortListImpl.java,v 1.7 2010-11-01 04:40:06 joehw Exp $
 */
public final class ShortListImpl extends AbstractList implements ShortList {

    /**
     * An immutable empty list.
     */
    public static final ShortListImpl EMPTY_LIST = new ShortListImpl(new short[0], 0);

    // The array to hold all data
    private final short[] fArray;
    // Number of elements in this list
    private final int fLength;

    /**
     * Construct an XSObjectList implementation
     *
     * @param array     the data array
     * @param length    the number of elements
     */
    public ShortListImpl(short[] array, int length) {
        fArray = array;
        fLength = length;
    }

    /**
     * The number of <code>Objects</code> in the list. The range of valid
     * child node indices is 0 to <code>length-1</code> inclusive.
     */
    public int getLength() {
        return fLength;
    }

    /**
     *  Checks if the <code>unsigned short</code> <code>item</code> is a
     * member of this list.
     * @param item  <code>unsigned short</code> whose presence in this list
     *   is to be tested.
     * @return  True if this list contains the <code>unsigned short</code>
     *   <code>item</code>.
     */
    public boolean contains(short item) {
        for (int i = 0; i < fLength; i++) {
            if (fArray[i] == item) {
                return true;
            }
        }
        return false;
    }

    public short item(int index) throws XSException {
        if (index < 0 || index >= fLength) {
            throw new XSException(XSException.INDEX_SIZE_ERR, null);
        }
        return fArray[index];
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ShortList)) {
            return false;
        }
        ShortList rhs = (ShortList)obj;

        if (fLength != rhs.getLength()) {
            return false;
        }
        for (int i = 0;i < fLength; ++i) {
            if (fArray[i] != rhs.item(i)) {
                return false;
            }
        }
        return true;
    }

    /*
     * List methods
     */

    public Object get(int index) {
        if (index >= 0 && index < fLength) {
            return new Short(fArray[index]);
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    public int size() {
        return getLength();
    }

} // class ShortListImpl
