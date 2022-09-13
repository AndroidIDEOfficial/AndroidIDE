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
 * $Id: DOM.java,v 1.2.4.1 2005/08/31 10:18:49 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc;

import jaxp.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import jaxp.sun.org.apache.xml.internal.dtm.DTMAxisIterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jaxp.sun.org.apache.xml.internal.serializer.SerializationHandler;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public interface DOM {
    public final static int  FIRST_TYPE             = 0;

    public final static int  NO_TYPE                = -1;

    // 0 is reserved for NodeIterator.END
    public final static int NULL     = 0;

    // used by some node iterators to know which node to return
    public final static int RETURN_CURRENT = 0;
    public final static int RETURN_PARENT  = 1;

    // Constants used by getResultTreeFrag to indicate the types of the RTFs.
    public final static int SIMPLE_RTF   = 0;
    public final static int ADAPTIVE_RTF = 1;
    public final static int TREE_RTF     = 2;

    /** returns singleton iterator containg the document root */
    public DTMAxisIterator getIterator();
    public String getStringValue();

    public DTMAxisIterator getChildren(final int node);
    public DTMAxisIterator getTypedChildren(final int type);
    public DTMAxisIterator getAxisIterator(final int axis);
    public DTMAxisIterator getTypedAxisIterator(final int axis, final int type);
    public DTMAxisIterator getNthDescendant(int node, int n, boolean includeself);
    public DTMAxisIterator getNamespaceAxisIterator(final int axis, final int ns);
    public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iter, int returnType,
                                             String value, boolean op);
    public DTMAxisIterator orderNodes(DTMAxisIterator source, int node);
    public String getNodeName(final int node);
    public String getNodeNameX(final int node);
    public String getNamespaceName(final int node);
    public int getExpandedTypeID(final int node);
    public int getNamespaceType(final int node);
    public int getParent(final int node);
    public int getAttributeNode(final int gType, final int element);
    public String getStringValueX(final int node);
    public void copy(final int node, SerializationHandler handler)
        throws TransletException;
    public void copy(DTMAxisIterator nodes, SerializationHandler handler)
        throws TransletException;
    public String shallowCopy(final int node, SerializationHandler handler)
        throws TransletException;
    public boolean lessThan(final int node1, final int node2);
    public void characters(final int textNode, SerializationHandler handler)
        throws TransletException;
    public Node makeNode(int index);
    public Node makeNode(DTMAxisIterator iter);
    public NodeList makeNodeList(int index);
    public NodeList makeNodeList(DTMAxisIterator iter);
    public String getLanguage(int node);
    public int getSize();
    public String getDocumentURI(int node);
    public void setFilter(StripFilter filter);
    public void setupMapping(String[] names, String[] urisArray, int[] typesArray, String[] namespaces);
    public boolean isElement(final int node);
    public boolean isAttribute(final int node);
    public String lookupNamespace(int node, String prefix)
        throws TransletException;
    public int getNodeIdent(final int nodehandle);
    public int getNodeHandle(final int nodeId);
    public DOM getResultTreeFrag(int initialSize, int rtfType);
    public DOM getResultTreeFrag(int initialSize, int rtfType, boolean addToDTMManager);
    public SerializationHandler getOutputDomBuilder();
    public int getNSType(int node);
    public int getDocument();
    public String getUnparsedEntityURI(String name);
    public Hashtable getElementsWithIDs();
}
