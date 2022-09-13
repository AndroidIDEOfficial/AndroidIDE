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
 * $Id: XNull.java,v 1.2.4.1 2005/09/14 20:34:46 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.objects;

import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xpath.internal.XPathContext;

/**
 * This class represents an XPath null object, and is capable of
 * converting the null to other types, such as a string.
 * @xsl.usage general
 */
public class XNull extends XNodeSet
{
    static final long serialVersionUID = -6841683711458983005L;

  /**
   * Create an XObject.
   */
  public XNull()
  {
    super();
  }

  /**
   * Tell what kind of class this is.
   *
   * @return type CLASS_NULL
   */
  public int getType()
  {
    return CLASS_NULL;
  }

  /**
   * Given a request type, return the equivalent string.
   * For diagnostic purposes.
   *
   * @return type string "#CLASS_NULL"
   */
  public String getTypeString()
  {
    return "#CLASS_NULL";
  }

  /**
   * Cast result object to a number.
   *
   * @return 0.0
   */

  public double num()
  {
    return 0.0;
  }

  /**
   * Cast result object to a boolean.
   *
   * @return false
   */
  public boolean bool()
  {
    return false;
  }

  /**
   * Cast result object to a string.
   *
   * @return empty string ""
   */
  public String str()
  {
    return "";
  }

  /**
   * Cast result object to a result tree fragment.
   *
   * @param support XPath context to use for the conversion
   *
   * @return The object as a result tree fragment.
   */
  public int rtf(XPathContext support)
  {
    // DTM frag = support.createDocumentFragment();
    // %REVIEW%
    return DTM.NULL;
  }

//  /**
//   * Cast result object to a nodelist.
//   *
//   * @return null
//   */
//  public DTMIterator iter()
//  {
//    return null;
//  }

  /**
   * Tell if two objects are functionally equal.
   *
   * @param obj2 Object to compare this to
   *
   * @return True if the given object is of type CLASS_NULL
   */
  public boolean equals(XObject obj2)
  {
    return obj2.getType() == CLASS_NULL;
  }
}
