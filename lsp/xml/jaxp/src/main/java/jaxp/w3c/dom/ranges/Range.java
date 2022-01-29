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

package jaxp.w3c.dom.ranges;

import jaxp.w3c.dom.DOMException;
import jaxp.w3c.dom.DocumentFragment;
import jaxp.w3c.dom.Node;

public interface Range {

    public Node getStartContainer() throws DOMException;

    public int getStartOffset() throws DOMException;

    public Node getEndContainer() throws DOMException;

    public int getEndOffset() throws DOMException;

    public boolean getCollapsed() throws DOMException;

    public Node getCommonAncestorContainer() throws DOMException;

    public void setStart(Node refNode, int offset) throws RangeException, DOMException;

    public void setEnd(Node refNode, int offset) throws RangeException, DOMException;

    public void setStartBefore(Node refNode) throws RangeException, DOMException;

    public void setStartAfter(Node refNode) throws RangeException, DOMException;

    public void setEndBefore(Node refNode) throws RangeException, DOMException;

    public void setEndAfter(Node refNode) throws RangeException, DOMException;

    public void collapse(boolean toStart) throws DOMException;
    
    public void selectNode(Node node) throws RangeException, DOMException;

    public void selectNodeContents(Node node) throws RangeException, DOMException;

    public static final short START_TO_START            = 0;
    public static final short START_TO_END              = 1;
    public static final short END_TO_END                = 2;
    public static final short END_TO_START              = 3;

    public short compareBoundaryPoints(short how,
                                         Range sourceRange)
                                         throws DOMException;

    public void deleteContents() throws DOMException;

    public DocumentFragment extractContents() throws DOMException;

    public DocumentFragment cloneContents() throws DOMException;

    public void insertNode(Node node) throws DOMException, RangeException;

    public void surroundContents(Node newParent) throws DOMException, RangeException;

    public Range cloneRange() throws DOMException;

    public String toString() throws DOMException;

    public void detach() throws DOMException;
}
