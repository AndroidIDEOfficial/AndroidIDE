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

import java.util.List;

/**
 *  The <code>XSNamesaceItemList</code> interface provides the abstraction of
 * an immutable ordered collection of <code>XSNamespaceItem</code>s, without
 * defining or constraining how this collection is implemented.
 */
public interface XSNamespaceItemList extends List {
    /**
     *  The number of <code>XSNamespaceItem</code>s in the list. The range of
     * valid child object indices is 0 to <code>length-1</code> inclusive.
     */
    public int getLength();

    /**
     *  Returns the <code>index</code>th item in the collection or
     * <code>null</code> if <code>index</code> is greater than or equal to
     * the number of objects in the list. The index starts at 0.
     * @param index  index into the collection.
     * @return  The <code>XSNamespaceItem</code> at the <code>index</code>th
     *   position in the <code>XSNamespaceItemList</code>, or
     *   <code>null</code> if the index specified is not valid.
     */
    public XSNamespaceItem item(int index);

}
