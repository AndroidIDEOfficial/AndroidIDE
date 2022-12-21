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
 * $Id: ArrayNodeListIterator.java,v 1.0 2009-11-25 04:34:24 joehw Exp $
 */
package jaxp.sun.org.apache.xalan.internal.xsltc.dom;

import jaxp.sun.org.apache.xml.internal.dtm.DTMAxisIterator;

public class ArrayNodeListIterator implements DTMAxisIterator {

    private int _pos = 0;

    private int _mark = 0;

    private int _nodes[];

    private static final int[] EMPTY = { };

    public ArrayNodeListIterator(int[] nodes) {
        _nodes = nodes;
    }

    public int next() {
        return _pos < _nodes.length ? _nodes[_pos++] : END;
    }

    public DTMAxisIterator reset() {
        _pos = 0;
        return this;
    }

    public int getLast() {
        return _nodes.length;
    }

    public int getPosition() {
        return _pos;
    }

    public void setMark() {
        _mark = _pos;
    }

    public void gotoMark() {
        _pos = _mark;
    }

    public DTMAxisIterator setStartNode(int node) {
        if (node == END) _nodes = EMPTY;
        return this;
    }

    public int getStartNode() {
        return END;
    }

    public boolean isReverse() {
        return false;
    }

    public DTMAxisIterator cloneIterator() {
        return new ArrayNodeListIterator(_nodes);
    }

    public void setRestartable(boolean isRestartable) {
    }

    public int getNodeByPosition(int position) {
        return _nodes[position - 1];
    }

}
