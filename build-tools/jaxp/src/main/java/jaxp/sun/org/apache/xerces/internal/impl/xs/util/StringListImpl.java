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

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Vector;

import jaxp.sun.org.apache.xerces.internal.xs.StringList;

/**
 * Containts a list of Object's.
 *
 * @xerces.internal
 *
 * @author Sandy Gao, IBM
 *
 * @version $Id: StringListImpl.java,v 1.7 2010-11-01 04:40:06 joehw Exp $
 */
public final class StringListImpl extends AbstractList implements StringList {

    /**
     * An immutable empty list.
     */
    public static final StringListImpl EMPTY_LIST = new StringListImpl(new String[0], 0);

    // The array to hold all data
    private final String[] fArray;
    // Number of elements in this list
    private final int fLength;

    // REVISIT: this is temp solution. In general we need to use this class
    //          instead of the Vector.
    private final Vector fVector;

    public StringListImpl(Vector v) {
        fVector = v;
        fLength = (v == null) ? 0 : v.size();
        fArray = null;
    }

    /**
     * Construct an XSObjectList implementation
     *
     * @param array     the data array
     * @param length    the number of elements
     */
    public StringListImpl(String[] array, int length) {
        fArray = array;
        fLength = length;
        fVector = null;
    }

    /**
     * The number of <code>Objects</code> in the list. The range of valid
     * child node indices is 0 to <code>length-1</code> inclusive.
     */
    public int getLength() {
        return fLength;
    }

    /**
     *  Checks if the <code>GenericString</code> <code>item</code> is a member
     * of this list.
     * @param item  <code>GenericString</code> whose presence in this list is
     *   to be tested.
     * @return  True if this list contains the <code>GenericString</code>
     *   <code>item</code>.
     */
    public boolean contains(String item) {
        if (fVector != null) {
            return fVector.contains(item);
        }
        if (item == null) {
            for (int i = 0; i < fLength; i++) {
                if (fArray[i] == null)
                    return true;
            }
        }
        else {
            for (int i = 0; i < fLength; i++) {
                if (item.equals(fArray[i]))
                    return true;
            }
        }
        return false;
    }

    public String item(int index) {
        if (index < 0 || index >= fLength) {
            return null;
        }
        if (fVector != null) {
            return (String)fVector.elementAt(index);
        }
        return fArray[index];
    }

    /*
     * List methods
     */

    public Object get(int index) {
        if (index >= 0 && index < fLength) {
            if (fVector != null) {
                return fVector.elementAt(index);
            }
            return fArray[index];
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    public int size() {
        return getLength();
    }

    public Object[] toArray() {
        if (fVector != null) {
            return fVector.toArray();
        }
        Object[] a = new Object[fLength];
        toArray0(a);
        return a;
    }

    public Object[] toArray(Object[] a) {
        if (fVector != null) {
            return fVector.toArray(a);
        }
        if (a.length < fLength) {
            Class arrayClass = a.getClass();
            Class componentType = arrayClass.getComponentType();
            a = (Object[]) Array.newInstance(componentType, fLength);
        }
        toArray0(a);
        if (a.length > fLength) {
            a[fLength] = null;
        }
        return a;
    }

    private void toArray0(Object[] a) {
        if (fLength > 0) {
            System.arraycopy(fArray, 0, a, 0, fLength);
        }
    }

} // class StringListImpl
