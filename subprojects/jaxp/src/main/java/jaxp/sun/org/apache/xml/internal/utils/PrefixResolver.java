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
 * $Id: PrefixResolver.java,v 1.2.4.1 2005/09/15 08:15:51 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.utils;

/**
 * The class that implements this interface can resolve prefixes to
 * namespaces. Examples would include resolving the meaning of a
 * prefix at a particular point in a document, or mapping the prefixes
 * used in an XPath expression.
 * @xsl.usage advanced
 */
public interface PrefixResolver
{

  /**
   * Given a namespace, get the corrisponding prefix.  This assumes that
   * the PrefixResolver holds its own namespace context, or is a namespace
   * context itself.
   *
   * @param prefix The prefix to look up, which may be an empty string ("") for the default Namespace.
   *
   * @return The associated Namespace URI, or null if the prefix
   *         is undeclared in this context.
   */
  String getNamespaceForPrefix(String prefix);

  /**
   * Given a namespace, get the corresponding prefix, based on the context node.
   *
   * @param prefix The prefix to look up, which may be an empty string ("") for the default Namespace.
   * @param context The node context from which to look up the URI.
   *
   * @return The associated Namespace URI as a string, or null if the prefix
   *         is undeclared in this context.
   */
  String getNamespaceForPrefix(String prefix, org.w3c.dom.Node context);

  /**
   * Return the base identifier.
   *
   * @return The base identifier from where relative URIs should be absolutized, or null
   * if the base ID is unknown.
   * <p>
   * CAVEAT: Note that the base URI in an XML document may vary with where
   * you are in the document, if part of the doc's contents were brought in
   * via an external entity reference or if mechanisms such as xml:base have
   * been used. Unless this PrefixResolver is bound to a specific portion of
   * the document, or has been kept up to date via some other mechanism, it
   * may not accurately reflect that context information.
   */
  public String getBaseIdentifier();

  public boolean handlesNullPrefixes();
}
