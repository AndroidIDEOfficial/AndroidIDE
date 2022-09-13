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
/*
 * $Id: DTMNodeList.java,v 1.2.4.1 2005/09/15 08:15:04 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.dtm.ref;

import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xml.internal.dtm.DTMIterator;
import org.w3c.dom.Node;

/**
 * <code>DTMNodeList</code> gives us an implementation of the DOM's
 * NodeList interface wrapped around a DTM Iterator. The author
 * considers this something of an abominations, since NodeList was not
 * intended to be a general purpose "list of nodes" API and is
 * generally considered by the DOM WG to have be a mistake... but I'm
 * told that some of the XPath/XSLT folks say they must have this
 * solution.
 *
 * Please note that this is not necessarily equivlaent to a DOM
 * NodeList operating over the same document. In particular:
 * <ul>
 *
 * <li>If there are several Text nodes in logical succession (ie,
 * across CDATASection and EntityReference boundaries), we will return
 * only the first; the caller is responsible for stepping through
 * them.
 * (%REVIEW% Provide a convenience routine here to assist, pending
 * proposed DOM Level 3 getAdjacentText() operation?) </li>
 *
 * <li>Since the whole XPath/XSLT architecture assumes that the source
 * document is not altered while we're working with it, we do not
 * promise to implement the DOM NodeList's "live view" response to
 * document mutation. </li>
 *
 * </ul>
 *
 * <p>State: In progress!!</p>
 * */
public class DTMNodeList extends DTMNodeListBase {
    private DTMIterator m_iter;

    //================================================================
    // Methods unique to this class
    private DTMNodeList() {
    }

    /**
     * Public constructor: Wrap a DTMNodeList around an existing
     * and preconfigured DTMIterator
     *
     * WARNING: THIS HAS THE SIDE EFFECT OF ISSUING setShouldCacheNodes(true)
     * AGAINST THE DTMIterator.
     *
     */
    public DTMNodeList(DTMIterator dtmIterator) {
        if (dtmIterator != null) {
            int pos = dtmIterator.getCurrentPos();
            try {
                m_iter=(DTMIterator)dtmIterator.cloneWithReset();
            } catch(CloneNotSupportedException cnse) {
                m_iter = dtmIterator;
            }
            m_iter.setShouldCacheNodes(true);
            m_iter.runTo(-1);
            m_iter.setCurrentPos(pos);
        }
    }

    /**
     * Access the wrapped DTMIterator. I'm not sure whether anyone will
     * need this or not, but let's write it and think about it.
     *
     */
    public DTMIterator getDTMIterator() {
        return m_iter;
    }

    //================================================================
    // org.w3c.dom.NodeList API follows

    /**
     * Returns the <code>index</code>th item in the collection. If
     * <code>index</code> is greater than or equal to the number of nodes in
     * the list, this returns <code>null</code>.
     * @param index Index into the collection.
     * @return The node at the <code>index</code>th position in the
     *   <code>NodeList</code>, or <code>null</code> if that is not a valid
     *   index.
     */
    public Node item(int index)
    {
        if (m_iter != null) {
            int handle=m_iter.item(index);
            if (handle == DTM.NULL) {
                return null;
            }
            return m_iter.getDTM(handle).getNode(handle);
        } else {
            return null;
        }
    }

    /**
     * The number of nodes in the list. The range of valid child node indices
     * is 0 to <code>length-1</code> inclusive.
     */
    public int getLength() {
        return (m_iter != null) ? m_iter.getLength() : 0;
    }
}
