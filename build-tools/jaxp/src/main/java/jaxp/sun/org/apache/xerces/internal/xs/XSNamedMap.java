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
 * Copyright 2003,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.xs;

import java.util.Map;

/**
 * Objects implementing the <code>XSNamedMap</code> interface are used to
 * represent immutable collections of XML Schema components that can be
 * accessed by name. Note that <code>XSNamedMap</code> does not inherit from
 * <code>XSObjectList</code>. The <code>XSObject</code>s in
 * <code>XSNamedMap</code>s are not maintained in any particular order.
 */
public interface XSNamedMap extends Map {
    /**
     * The number of <code>XSObjects</code> in the <code>XSObjectList</code>.
     * The range of valid child object indices is 0 to <code>length-1</code>
     * inclusive.
     */
    public int getLength();

    /**
     *  Returns the <code>index</code>th item in the collection or
     * <code>null</code> if <code>index</code> is greater than or equal to
     * the number of objects in the list. The index starts at 0.
     * @param index  index into the collection.
     * @return  The <code>XSObject</code> at the <code>index</code>th
     *   position in the <code>XSObjectList</code>, or <code>null</code> if
     *   the index specified is not valid.
     */
    public XSObject item(int index);

    /**
     * Retrieves an <code>XSObject</code> specified by local name and
     * namespace URI.
     * <br>Per XML Namespaces, applications must use the value <code>null</code> as the
     * <code>namespace</code> parameter for methods if they wish to specify
     * no namespace.
     * @param namespace The namespace URI of the <code>XSObject</code> to
     *   retrieve, or <code>null</code> if the <code>XSObject</code> has no
     *   namespace.
     * @param localName The local name of the <code>XSObject</code> to
     *   retrieve.
     * @return A <code>XSObject</code> (of any type) with the specified local
     *   name and namespace URI, or <code>null</code> if they do not
     *   identify any object in this map.
     */
    public XSObject itemByName(String namespace,
                               String localName);

}
