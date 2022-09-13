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
 * $Id: ExpressionContext.java,v 1.2.4.1 2005/09/10 19:34:03 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xalan.internal.extensions;

import jaxp.xml.transform.ErrorListener;

import jaxp.sun.org.apache.xml.internal.utils.QName;
import jaxp.sun.org.apache.xpath.internal.XPathContext;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

import jaxp.xml.transform.TransformerException;

/**
 * An object that implements this interface can supply
 * information about the current XPath expression context.
 */
public interface ExpressionContext
{

  /**
   * Get the current context node.
   * @return The current context node.
   */
  public Node getContextNode();

  /**
   * Get the current context node list.
   * @return An iterator for the current context list, as
   * defined in XSLT.
   */
  public NodeIterator getContextNodes();

  /**
   * Get the error listener.
   * @return The registered error listener.
   */
  public ErrorListener getErrorListener();

  /**
   * Get the value of a node as a number.
   * @param n Node to be converted to a number.  May be null.
   * @return value of n as a number.
   */
  public double toNumber(Node n);

  /**
   * Get the value of a node as a string.
   * @param n Node to be converted to a string.  May be null.
   * @return value of n as a string, or an empty string if n is null.
   */
  public String toString(Node n);

  /**
   * Get a variable based on it's qualified name.
   *
   * @param qname The qualified name of the variable.
   *
   * @return The evaluated value of the variable.
   *
   * @throws TransformerException
   */
  public XObject getVariableOrParam(QName qname)
            throws TransformerException;

  /**
   * Get the XPathContext that owns this ExpressionContext.
   *
   * Note: exslt:function requires the XPathContext to access
   * the variable stack and TransformerImpl.
   *
   * @return The current XPathContext.
   * @throws TransformerException
   */
  public XPathContext getXPathContext()
            throws TransformerException;

}
