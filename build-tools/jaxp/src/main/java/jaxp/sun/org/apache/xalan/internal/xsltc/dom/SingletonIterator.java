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
 * $Id: SingletonIterator.java,v 1.2.4.1 2005/09/06 10:15:18 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.dom;

import jaxp.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import jaxp.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public class SingletonIterator extends DTMAxisIteratorBase {
    private int _node;
    private final boolean _isConstant;

    public SingletonIterator() {
        this(Integer.MIN_VALUE, false);
    }

    public SingletonIterator(int node) {
        this(node, false);
    }

    public SingletonIterator(int node, boolean constant) {
        _node = _startNode = node;
        _isConstant = constant;
    }

    /**
     * Override the value of <tt>_node</tt> only when this
     * object was constructed using the empty constructor.
     */
    public DTMAxisIterator setStartNode(int node) {
        if (_isConstant) {
            _node = _startNode;
            return resetPosition();
        }
        else if (_isRestartable) {
            if (_node <= 0)
                _node = _startNode = node;
            return resetPosition();
        }
        return this;
    }

    public DTMAxisIterator reset() {
        if (_isConstant) {
            _node = _startNode;
            return resetPosition();
        }
        else {
            final boolean temp = _isRestartable;
            _isRestartable = true;
            setStartNode(_startNode);
            _isRestartable = temp;
        }
        return this;
    }

    public int next() {
        final int result = _node;
        _node = DTMAxisIterator.END;
        return returnNode(result);
    }

    public void setMark() {
        _markedNode = _node;
    }

    public void gotoMark() {
        _node = _markedNode;
    }
}
