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
 * $Id: CachedXPathAPI.java,v 1.2.4.1 2005/09/10 03:47:42 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal;

import jaxp.xml.transform.TransformerException;

import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xml.internal.dtm.DTMManager;
import jaxp.sun.org.apache.xml.internal.utils.PrefixResolver;
import jaxp.sun.org.apache.xml.internal.utils.PrefixResolverDefault;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;

import jaxp.sun.org.apache.xpath.internal.objects.XBoolean;
import jaxp.sun.org.apache.xpath.internal.objects.XNull;
import jaxp.sun.org.apache.xpath.internal.objects.XNumber;
import jaxp.sun.org.apache.xpath.internal.objects.XRTreeFrag;
import jaxp.sun.org.apache.xpath.internal.objects.XString;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

/**
 * The methods in this class are convenience methods into the
 * low-level XPath API.
 *
 * These functions tend to be a little slow, since a number of objects must be
 * created for each evaluation.  A faster way is to precompile the
 * XPaths using the low-level API, and then just use the XPaths
 * over and over.
 *
 * This is an alternative for the old XPathAPI class, which provided
 * static methods for the purpose but had the drawback of
 * instantiating a new XPathContext (and thus building a new DTMManager,
 * and new DTMs) each time it was called. XPathAPIObject instead retains
 * its context as long as the object persists, reusing the DTMs. This
 * does have a downside: if you've changed your source document, you should
 * obtain a new XPathAPIObject to continue searching it, since trying to use
 * the old DTMs will probably yield bad results or malfunction outright... and
 * the cached DTMs may consume memory until this object and its context are
 * returned to the heap. Essentially, it's the caller's responsibility to
 * decide when to discard the cache.
 *
 * @see <a href="http://www.w3.org/TR/xpath">XPath Specification</a>
 * */
public class CachedXPathAPI
{
  /** XPathContext, and thus the document model system (DTMs), persists through multiple
      calls to this object. This is set in the constructor.
  */
  protected XPathContext xpathSupport;

  /**
   * <p>Default constructor. Establishes its own {@link XPathContext}, and hence
   * its own {@link DTMManager}.
   * Good choice for simple uses.</p>
   * <p>Note that any particular instance of {@link CachedXPathAPI} must not be
   * operated upon by multiple threads without synchronization; we do
   * not currently support multithreaded access to a single
   * {@link DTM}.</p>
   */
  public CachedXPathAPI()
  {
    xpathSupport = new XPathContext();
  }

  /**
   * <p>This constructor shares its {@link XPathContext} with a pre-existing
   * {@link CachedXPathAPI}.  That allows sharing document models
   * ({@link DTM}) and previously established location
   * state.</p>
   * <p>Note that the original {@link CachedXPathAPI} and the new one should
   * not be operated upon concurrently; we do not support multithreaded access
   * to a single {@link DTM} at this time.  Similarly,
   * any particular instance of {@link CachedXPathAPI} must not be operated
   * upon by multiple threads without synchronization.</p>
   * <p>%REVIEW% Should this instead do a clone-and-reset on the XPathSupport object?</p>
   *
   */
  public CachedXPathAPI(CachedXPathAPI priorXPathAPI)
  {
    xpathSupport = priorXPathAPI.xpathSupport;
  }


  /** Returns the XPathSupport object used in this CachedXPathAPI
   *
   * %REVIEW% I'm somewhat concerned about the loss of encapsulation
   * this causes, but the xml-security folks say they need it.
   * */
  public XPathContext getXPathContext()
  {
    return this.xpathSupport;
  }


  /**
   * Use an XPath string to select a single node. XPath namespace
   * prefixes are resolved from the context node, which may not
   * be what you want (see the next method).
   *
   * @param contextNode The node to start searching from.
   * @param str A valid XPath string.
   * @return The first node found that matches the XPath, or null.
   *
   * @throws TransformerException
   */
  public  Node selectSingleNode(Node contextNode, String str)
          throws TransformerException
  {
    return selectSingleNode(contextNode, str, contextNode);
  }

  /**
   * Use an XPath string to select a single node.
   * XPath namespace prefixes are resolved from the namespaceNode.
   *
   * @param contextNode The node to start searching from.
   * @param str A valid XPath string.
   * @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
   * @return The first node found that matches the XPath, or null.
   *
   * @throws TransformerException
   */
  public  Node selectSingleNode(
          Node contextNode, String str, Node namespaceNode)
            throws TransformerException
  {

    // Have the XObject return its result as a NodeSetDTM.
    NodeIterator nl = selectNodeIterator(contextNode, str, namespaceNode);

    // Return the first node, or null
    return nl.nextNode();
  }

  /**
   *  Use an XPath string to select a nodelist.
   *  XPath namespace prefixes are resolved from the contextNode.
   *
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @return A NodeIterator, should never be null.
   *
   * @throws TransformerException
   */
  public  NodeIterator selectNodeIterator(Node contextNode, String str)
          throws TransformerException
  {
    return selectNodeIterator(contextNode, str, contextNode);
  }

  /**
   *  Use an XPath string to select a nodelist.
   *  XPath namespace prefixes are resolved from the namespaceNode.
   *
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
   *  @return A NodeIterator, should never be null.
   *
   * @throws TransformerException
   */
  public  NodeIterator selectNodeIterator(
          Node contextNode, String str, Node namespaceNode)
            throws TransformerException
  {

    // Execute the XPath, and have it return the result
    XObject list = eval(contextNode, str, namespaceNode);

    // Have the XObject return its result as a NodeSetDTM.
    return list.nodeset();
  }

  /**
   *  Use an XPath string to select a nodelist.
   *  XPath namespace prefixes are resolved from the contextNode.
   *
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @return A NodeIterator, should never be null.
   *
   * @throws TransformerException
   */
  public  NodeList selectNodeList(Node contextNode, String str)
          throws TransformerException
  {
    return selectNodeList(contextNode, str, contextNode);
  }

  /**
   *  Use an XPath string to select a nodelist.
   *  XPath namespace prefixes are resolved from the namespaceNode.
   *
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
   *  @return A NodeIterator, should never be null.
   *
   * @throws TransformerException
   */
  public  NodeList selectNodeList(
          Node contextNode, String str, Node namespaceNode)
            throws TransformerException
  {

    // Execute the XPath, and have it return the result
    XObject list = eval(contextNode, str, namespaceNode);

    // Return a NodeList.
    return list.nodelist();
  }

  /**
   *  Evaluate XPath string to an XObject.  Using this method,
   *  XPath namespace prefixes will be resolved from the namespaceNode.
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @return An XObject, which can be used to obtain a string, number, nodelist, etc, should never be null.
   *  @see XObject
   *  @see XNull
   *  @see XBoolean
   *  @see XNumber
   *  @see XString
   *  @see XRTreeFrag
   *
   * @throws TransformerException
   */
  public  XObject eval(Node contextNode, String str)
          throws TransformerException
  {
    return eval(contextNode, str, contextNode);
  }

  /**
   *  Evaluate XPath string to an XObject.
   *  XPath namespace prefixes are resolved from the namespaceNode.
   *  The implementation of this is a little slow, since it creates
   *  a number of objects each time it is called.  This could be optimized
   *  to keep the same objects around, but then thread-safety issues would arise.
   *
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
   *  @return An XObject, which can be used to obtain a string, number, nodelist, etc, should never be null.
   *  @see XObject
   *  @see XNull
   *  @see XBoolean
   *  @see XNumber
   *  @see XString
   *  @see XRTreeFrag
   *
   * @throws TransformerException
   */
  public  XObject eval(Node contextNode, String str, Node namespaceNode)
          throws TransformerException
  {

    // Since we don't have a XML Parser involved here, install some default support
    // for things like namespaces, etc.
    // (Changed from: XPathContext xpathSupport = new XPathContext();
    //    because XPathContext is weak in a number of areas... perhaps
    //    XPathContext should be done away with.)

    // Create an object to resolve namespace prefixes.
    // XPath namespaces are resolved from the input context node's document element
    // if it is a root node, or else the current context node (for lack of a better
    // resolution space, given the simplicity of this sample code).
    PrefixResolverDefault prefixResolver = new PrefixResolverDefault(
      (namespaceNode.getNodeType() == Node.DOCUMENT_NODE)
      ? ((Document) namespaceNode).getDocumentElement() : namespaceNode);

    // Create the XPath object.
    XPath xpath = new XPath(str, null, prefixResolver, XPath.SELECT, null);

    // Execute the XPath, and have it return the result
    // return xpath.execute(xpathSupport, contextNode, prefixResolver);
    int ctxtNode = xpathSupport.getDTMHandleFromNode(contextNode);

    return xpath.execute(xpathSupport, ctxtNode, prefixResolver);
  }

  /**
   *   Evaluate XPath string to an XObject.
   *   XPath namespace prefixes are resolved from the namespaceNode.
   *   The implementation of this is a little slow, since it creates
   *   a number of objects each time it is called.  This could be optimized
   *   to keep the same objects around, but then thread-safety issues would arise.
   *
   *   @param contextNode The node to start searching from.
   *   @param str A valid XPath string.
   *   @param prefixResolver Will be called if the parser encounters namespace
   *                         prefixes, to resolve the prefixes to URLs.
   *   @return An XObject, which can be used to obtain a string, number, nodelist, etc, should never be null.
   *   @see XObject
   *   @see XNull
   *   @see XBoolean
   *   @see XNumber
   *   @see XString
   *   @see XRTreeFrag
   *
   * @throws TransformerException
   */
  public  XObject eval(
          Node contextNode, String str, PrefixResolver prefixResolver)
            throws TransformerException
  {

    // Since we don't have a XML Parser involved here, install some default support
    // for things like namespaces, etc.
    // (Changed from: XPathContext xpathSupport = new XPathContext();
    //    because XPathContext is weak in a number of areas... perhaps
    //    XPathContext should be done away with.)
    // Create the XPath object.
    XPath xpath = new XPath(str, null, prefixResolver, XPath.SELECT, null);

    // Execute the XPath, and have it return the result
    XPathContext xpathSupport = new XPathContext();
    int ctxtNode = xpathSupport.getDTMHandleFromNode(contextNode);

    return xpath.execute(xpathSupport, ctxtNode, prefixResolver);
  }
}
