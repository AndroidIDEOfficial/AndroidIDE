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
 * $Id: NthIterator.java,v 1.2.4.1 2005/09/06 09:57:04 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.dom;

import jaxp.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import jaxp.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import jaxp.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;

/**
 * @author Jacek Ambroziak
 * @author Morten Jorgensen
 */
public final class NthIterator extends DTMAxisIteratorBase {
    // ...[N]
    private DTMAxisIterator _source;
    private final int _position;
    private boolean _ready;

    public NthIterator(DTMAxisIterator source, int n) {
        _source = source;
        _position = n;
    }

    public void setRestartable(boolean isRestartable) {
        _isRestartable = isRestartable;
        _source.setRestartable(isRestartable);
    }

    public DTMAxisIterator cloneIterator() {
        try {
            final NthIterator clone = (NthIterator) super.clone();
            clone._source = _source.cloneIterator();    // resets source
            clone._isRestartable = false;
            return clone;
        }
        catch (CloneNotSupportedException e) {
            BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR,
                                      e.toString());
            return null;
        }
    }

    public int next() {
        if (_ready) {
            _ready = false;
            return _source.getNodeByPosition(_position);
        }
        return DTMAxisIterator.END;
        /*
        if (_ready && _position > 0) {
            final int pos = _source.isReverse()
                                       ? _source.getLast() - _position + 1
                                       : _position;

            _ready = false;
            int node;
            while ((node = _source.next()) != DTMAxisIterator.END) {
                if (pos == _source.getPosition()) {
                    return node;
                }
            }
        }
        return DTMAxisIterator.END;
        */
    }

    public DTMAxisIterator setStartNode(final int node) {
        if (_isRestartable) {
            _source.setStartNode(node);
            _ready = true;
        }
        return this;
    }

    public DTMAxisIterator reset() {
        _source.reset();
        _ready = true;
        return this;
    }

    public int getLast() {
        return 1;
    }

    public int getPosition() {
        return 1;
    }

    public void setMark() {
        _source.setMark();
    }

    public void gotoMark() {
        _source.gotoMark();
    }
}
