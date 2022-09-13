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
 * $Id: XNodeSetForDOM.java,v 1.2.4.1 2005/09/14 20:34:46 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.objects;

import jaxp.sun.org.apache.xml.internal.dtm.DTMManager;
import jaxp.sun.org.apache.xpath.internal.NodeSetDTM;
import jaxp.sun.org.apache.xpath.internal.XPathContext;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

import jaxp.xml.transform.TransformerException;

/**
 * This class overrides the XNodeSet#object() method to provide the original
 * Node object, NodeList object, or NodeIterator.
 */
public class XNodeSetForDOM extends XNodeSet
{
    static final long serialVersionUID = -8396190713754624640L;
  Object m_origObj;

  public XNodeSetForDOM(Node node, DTMManager dtmMgr)
  {
    m_dtmMgr = dtmMgr;
    m_origObj = node;
    int dtmHandle = dtmMgr.getDTMHandleFromNode(node);
    setObject(new NodeSetDTM(dtmMgr));
    ((NodeSetDTM) m_obj).addNode(dtmHandle);
  }

  /**
   * Construct a XNodeSet object.
   *
   * @param val Value of the XNodeSet object
   */
  public XNodeSetForDOM(XNodeSet val)
  {
        super(val);
        if(val instanceof XNodeSetForDOM)
        m_origObj = ((XNodeSetForDOM)val).m_origObj;
  }

  public XNodeSetForDOM(NodeList nodeList, XPathContext xctxt)
  {
    m_dtmMgr = xctxt.getDTMManager();
    m_origObj = nodeList;

    // JKESS 20020514: Longer-term solution is to force
    // folks to request length through an accessor, so we can defer this
    // retrieval... but that requires an API change.
    // m_obj=new com.sun.org.apache.xpath.internal.NodeSetDTM(nodeList, xctxt);
    NodeSetDTM nsdtm=new NodeSetDTM(nodeList, xctxt);
    m_last=nsdtm.getLength();
    setObject(nsdtm);
  }

  public XNodeSetForDOM(NodeIterator nodeIter, XPathContext xctxt)
  {
    m_dtmMgr = xctxt.getDTMManager();
    m_origObj = nodeIter;

    // JKESS 20020514: Longer-term solution is to force
    // folks to request length through an accessor, so we can defer this
    // retrieval... but that requires an API change.
    // m_obj = new com.sun.org.apache.xpath.internal.NodeSetDTM(nodeIter, xctxt);
    NodeSetDTM nsdtm=new NodeSetDTM(nodeIter, xctxt);
    m_last=nsdtm.getLength();
    setObject(nsdtm);
  }

  /**
   * Return the original DOM object that the user passed in.  For use primarily
   * by the extension mechanism.
   *
   * @return The object that this class wraps
   */
  public Object object()
  {
    return m_origObj;
  }

  /**
   * Cast result object to a nodelist. Always issues an error.
   *
   * @return null
   *
   * @throws TransformerException
   */
  public NodeIterator nodeset() throws TransformerException
  {
    return (m_origObj instanceof NodeIterator)
                   ? (NodeIterator)m_origObj : super.nodeset();
  }

  /**
   * Cast result object to a nodelist. Always issues an error.
   *
   * @return null
   *
   * @throws TransformerException
   */
  public NodeList nodelist() throws TransformerException
  {
    return (m_origObj instanceof NodeList)
                   ? (NodeList)m_origObj : super.nodelist();
  }



}
