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
 * $Id: NSInfo.java,v 1.2.4.1 2005/09/15 08:15:48 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.utils;

/**
 * This class holds information about the namespace info
 * of a node.  It is used to optimize namespace lookup in
 * a generic DOM.
 * @xsl.usage internal
 */
public class NSInfo
{

  /**
   * Constructor NSInfo
   *
   *
   * @param hasProcessedNS Flag indicating whether namespaces
   * have been processed for this node
   * @param hasXMLNSAttrs Flag indicating whether this node
   * has XMLNS attributes.
   */
  public NSInfo(boolean hasProcessedNS, boolean hasXMLNSAttrs)
  {

    m_hasProcessedNS = hasProcessedNS;
    m_hasXMLNSAttrs = hasXMLNSAttrs;
    m_namespace = null;
    m_ancestorHasXMLNSAttrs = ANCESTORXMLNSUNPROCESSED;
  }

  // Unused at the moment

  /**
   * Constructor NSInfo
   *
   *
   * @param hasProcessedNS Flag indicating whether namespaces
   * have been processed for this node
   * @param hasXMLNSAttrs Flag indicating whether this node
   * has XMLNS attributes.
   * @param ancestorHasXMLNSAttrs Flag indicating whether one of this node's
   * ancestor has XMLNS attributes.
   */
  public NSInfo(boolean hasProcessedNS, boolean hasXMLNSAttrs,
                int ancestorHasXMLNSAttrs)
  {

    m_hasProcessedNS = hasProcessedNS;
    m_hasXMLNSAttrs = hasXMLNSAttrs;
    m_ancestorHasXMLNSAttrs = ancestorHasXMLNSAttrs;
    m_namespace = null;
  }

  /**
   * Constructor NSInfo
   *
   *
   * @param namespace The namespace URI
   * @param hasXMLNSAttrs Flag indicating whether this node
   * has XMLNS attributes.
   */
  public NSInfo(String namespace, boolean hasXMLNSAttrs)
  {

    m_hasProcessedNS = true;
    m_hasXMLNSAttrs = hasXMLNSAttrs;
    m_namespace = namespace;
    m_ancestorHasXMLNSAttrs = ANCESTORXMLNSUNPROCESSED;
  }

  /** The namespace URI          */
  public String m_namespace;

  /** Flag indicating whether this node has an XMLNS attribute          */
  public boolean m_hasXMLNSAttrs;

  /** Flag indicating whether namespaces have been processed for this node */
  public boolean m_hasProcessedNS;

  /** Flag indicating whether one of this node's ancestor has an XMLNS attribute          */
  public int m_ancestorHasXMLNSAttrs;

  /** Constant for ancestors XMLNS atributes not processed          */
  public static final int ANCESTORXMLNSUNPROCESSED = 0;

  /** Constant indicating an ancestor has an XMLNS attribute           */
  public static final int ANCESTORHASXMLNS = 1;

  /** Constant indicating ancestors don't have an XMLNS attribute           */
  public static final int ANCESTORNOXMLNS = 2;
}
