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
 * Copyright 2001-2004 The Apache Software Foundation.
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
/*
 * $Id: NodeIterator.java,v 1.2.4.1 2005/08/31 10:26:27 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc;

import jaxp.sun.org.apache.xml.internal.dtm.DTM;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public interface NodeIterator extends Cloneable {
    public static final int END = DTM.NULL;

    /**
     * Callers should not call next() after it returns END.
     */
    public int next();

    /**
     * Resets the iterator to the last start node.
     */
    public NodeIterator reset();

    /**
     * Returns the number of elements in this iterator.
     */
    public int getLast();

    /**
     * Returns the position of the current node in the set.
     */
    public int getPosition();

    /**
     * Remembers the current node for the next call to gotoMark().
     */
    public void setMark();

    /**
     * Restores the current node remembered by setMark().
     */
    public void gotoMark();

    /**
     * Set start to END should 'close' the iterator,
     * i.e. subsequent call to next() should return END.
     */
    public NodeIterator setStartNode(int node);

    /**
     * True if this iterator has a reversed axis.
     */
    public boolean isReverse();

    /**
     * Returns a deep copy of this iterator.
     */
    public NodeIterator cloneIterator();

    /**
     * Prevents or allows iterator restarts.
     */
    public void setRestartable(boolean isRestartable);

}
