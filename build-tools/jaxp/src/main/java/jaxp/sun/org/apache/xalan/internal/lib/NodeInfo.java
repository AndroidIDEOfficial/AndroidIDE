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
 * $Id: NodeInfo.java,v 1.2.4.1 2005/09/10 18:54:37 jeffsuttor Exp $
 */

package jaxp.sun.org.apache.xalan.internal.lib;

import jaxp.xml.transform.SourceLocator;

import jaxp.sun.org.apache.xalan.internal.extensions.ExpressionContext;
import jaxp.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <code>NodeInfo</code> defines a set of XSLT extension functions to be
 * used from stylesheets.
 *
 * @author <a href="mailto:ovidiu@cup.hp.com">Ovidiu Predescu</a>
 * @since May 24, 2001
 */
public class NodeInfo
{
  /**
   * <code>systemId</code> returns the system id of the current
   * context node.
   *
   * @param context an <code>ExpressionContext</code> value
   * @return a <code>String</code> value
   */
  public static String systemId(ExpressionContext context)
  {
    Node contextNode = context.getContextNode();
    int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
    SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM()
      .getSourceLocatorFor(nodeHandler);

    if (locator != null)
      return locator.getSystemId();
    else
      return null;
  }

  /**
   * <code>systemId</code> returns the system id of the node passed as
   * argument. If a node set is passed as argument, the system id of
   * the first node in the set is returned.
   *
   * @param nodeList a <code>NodeList</code> value
   * @return a <code>String</code> value
   */
  public static String systemId(NodeList nodeList)
  {
    if (nodeList == null || nodeList.getLength() == 0)
      return null;

    Node node = nodeList.item(0);
    int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
    SourceLocator locator = ((DTMNodeProxy)node).getDTM()
      .getSourceLocatorFor(nodeHandler);

    if (locator != null)
      return locator.getSystemId();
    else
      return null;
  }

  /**
   * <code>publicId</code> returns the public identifier of the current
   * context node.
   *
   * Xalan does not currently record this value, and will return null.
   *
   * @param context an <code>ExpressionContext</code> value
   * @return a <code>String</code> value
   */
  public static String publicId(ExpressionContext context)
  {
    Node contextNode = context.getContextNode();
    int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
    SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM()
      .getSourceLocatorFor(nodeHandler);

    if (locator != null)
      return locator.getPublicId();
    else
      return null;
  }

  /**
   * <code>publicId</code> returns the public identifier of the node passed as
   * argument. If a node set is passed as argument, the public identifier of
   * the first node in the set is returned.
   *
   * Xalan does not currently record this value, and will return null.
   *
   * @param nodeList a <code>NodeList</code> value
   * @return a <code>String</code> value
   */
  public static String publicId(NodeList nodeList)
  {
    if (nodeList == null || nodeList.getLength() == 0)
      return null;

    Node node = nodeList.item(0);
    int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
    SourceLocator locator = ((DTMNodeProxy)node).getDTM()
      .getSourceLocatorFor(nodeHandler);

    if (locator != null)
      return locator.getPublicId();
    else
      return null;
  }

  /**
   * <code>lineNumber</code> returns the line number of the current
   * context node.
   *
   * NOTE: Xalan does not normally record location information for each node.
   * To obtain it, you must set the custom TrAX attribute
   * "http://xml.apache.org/xalan/features/source_location"
   * true in the TransformerFactory before generating the Transformer and executing
   * the stylesheet. Storage cost per node will be noticably increased in this mode.
   *
   * @param context an <code>ExpressionContext</code> value
   * @return an <code>int</code> value. This may be -1 to indicate that the
   * line number is not known.
   */
  public static int lineNumber(ExpressionContext context)
  {
    Node contextNode = context.getContextNode();
    int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
    SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM()
      .getSourceLocatorFor(nodeHandler);

    if (locator != null)
      return locator.getLineNumber();
    else
      return -1;
  }

  /**
   * <code>lineNumber</code> returns the line number of the node
   * passed as argument. If a node set is passed as argument, the line
   * number of the first node in the set is returned.
   *
   * NOTE: Xalan does not normally record location information for each node.
   * To obtain it, you must set the custom TrAX attribute
   * "http://xml.apache.org/xalan/features/source_location"
   * true in the TransformerFactory before generating the Transformer and executing
   * the stylesheet. Storage cost per node will be noticably increased in this mode.
   *
   * @param nodeList a <code>NodeList</code> value
   * @return an <code>int</code> value. This may be -1 to indicate that the
   * line number is not known.
   */
  public static int lineNumber(NodeList nodeList)
  {
    if (nodeList == null || nodeList.getLength() == 0)
      return -1;

    Node node = nodeList.item(0);
    int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
    SourceLocator locator = ((DTMNodeProxy)node).getDTM()
      .getSourceLocatorFor(nodeHandler);

    if (locator != null)
      return locator.getLineNumber();
    else
      return -1;
  }

  /**
   * <code>columnNumber</code> returns the column number of the
   * current context node.
   *
   * NOTE: Xalan does not normally record location information for each node.
   * To obtain it, you must set the custom TrAX attribute
   * "http://xml.apache.org/xalan/features/source_location"
   * true in the TransformerFactory before generating the Transformer and executing
   * the stylesheet. Storage cost per node will be noticably increased in this mode.
   *
   * @param context an <code>ExpressionContext</code> value
   * @return an <code>int</code> value. This may be -1 to indicate that the
   * column number is not known.
   */
  public static int columnNumber(ExpressionContext context)
  {
    Node contextNode = context.getContextNode();
    int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
    SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM()
      .getSourceLocatorFor(nodeHandler);

    if (locator != null)
      return locator.getColumnNumber();
    else
      return -1;
  }

  /**
   * <code>columnNumber</code> returns the column number of the node
   * passed as argument. If a node set is passed as argument, the line
   * number of the first node in the set is returned.
   *
   * NOTE: Xalan does not normally record location information for each node.
   * To obtain it, you must set the custom TrAX attribute
   * "http://xml.apache.org/xalan/features/source_location"
   * true in the TransformerFactory before generating the Transformer and executing
   * the stylesheet. Storage cost per node will be noticably increased in this mode.
   *
   * @param nodeList a <code>NodeList</code> value
   * @return an <code>int</code> value. This may be -1 to indicate that the
   * column number is not known.
   */
  public static int columnNumber(NodeList nodeList)
  {
    if (nodeList == null || nodeList.getLength() == 0)
      return -1;

    Node node = nodeList.item(0);
    int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
    SourceLocator locator = ((DTMNodeProxy)node).getDTM()
      .getSourceLocatorFor(nodeHandler);

    if (locator != null)
      return locator.getColumnNumber();
    else
      return -1;
  }
}
