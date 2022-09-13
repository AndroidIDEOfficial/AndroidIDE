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
 * $Id: PrefixResolverDefault.java,v 1.2.4.1 2005/09/15 08:15:51 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.utils;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * This class implements a generic PrefixResolver that
 * can be used to perform prefix-to-namespace lookup
 * for the XPath object.
 * @xsl.usage general
 */
public class PrefixResolverDefault implements PrefixResolver
{

  /**
   * The context to resolve the prefix from, if the context
   * is not given.
   */
  Node m_context;

  /**
   * Construct a PrefixResolverDefault object.
   * @param xpathExpressionContext The context from
   * which XPath expression prefixes will be resolved.
   * Warning: This will not work correctly if xpathExpressionContext
   * is an attribute node.
   */
  public PrefixResolverDefault(Node xpathExpressionContext)
  {
    m_context = xpathExpressionContext;
  }

  /**
   * Given a namespace, get the corrisponding prefix.  This assumes that
   * the PrevixResolver hold's it's own namespace context, or is a namespace
   * context itself.
   * @param prefix Prefix to resolve.
   * @return Namespace that prefix resolves to, or null if prefix
   * is not bound.
   */
  public String getNamespaceForPrefix(String prefix)
  {
    return getNamespaceForPrefix(prefix, m_context);
  }

  /**
   * Given a namespace, get the corrisponding prefix.
   * Warning: This will not work correctly if namespaceContext
   * is an attribute node.
   * @param prefix Prefix to resolve.
   * @param namespaceContext Node from which to start searching for a
   * xmlns attribute that binds a prefix to a namespace.
   * @return Namespace that prefix resolves to, or null if prefix
   * is not bound.
   */
  public String getNamespaceForPrefix(String prefix,
                                      org.w3c.dom.Node namespaceContext)
  {

    Node parent = namespaceContext;
    String namespace = null;

    if (prefix.equals("xml"))
    {
      namespace = Constants.S_XMLNAMESPACEURI;
    }
    else
    {
      int type;

      while ((null != parent) && (null == namespace)
             && (((type = parent.getNodeType()) == Node.ELEMENT_NODE)
                 || (type == Node.ENTITY_REFERENCE_NODE)))
      {
        if (type == Node.ELEMENT_NODE)
        {
                if (parent.getNodeName().indexOf(prefix+":") == 0)
                        return parent.getNamespaceURI();
          NamedNodeMap nnm = parent.getAttributes();

          for (int i = 0; i < nnm.getLength(); i++)
          {
            Node attr = nnm.item(i);
            String aname = attr.getNodeName();
            boolean isPrefix = aname.startsWith("xmlns:");

            if (isPrefix || aname.equals("xmlns"))
            {
              int index = aname.indexOf(':');
              String p = isPrefix ? aname.substring(index + 1) : "";

              if (p.equals(prefix))
              {
                namespace = attr.getNodeValue();

                break;
              }
            }
          }
        }

        parent = parent.getParentNode();
      }
    }

    return namespace;
  }

  /**
   * Return the base identifier.
   *
   * @return null
   */
  public String getBaseIdentifier()
  {
    return null;
  }
        /**
         * @see PrefixResolver#handlesNullPrefixes()
         */
        public boolean handlesNullPrefixes() {
                return false;
        }

}
