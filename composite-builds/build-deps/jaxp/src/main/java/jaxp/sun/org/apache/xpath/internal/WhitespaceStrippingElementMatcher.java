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
 * $Id: WhitespaceStrippingElementMatcher.java,v 1.1.2.1 2005/08/01 01:30:15 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal;

import jaxp.xml.transform.TransformerException;

import org.w3c.dom.Element;

/**
 * A class that implements this interface can tell if a given element should
 * strip whitespace nodes from it's children.
 */
public interface WhitespaceStrippingElementMatcher
{
  /**
   * Get information about whether or not an element should strip whitespace.
   * @see <a href="http://www.w3.org/TR/xslt#strip">strip in XSLT Specification</a>
   *
   * @param support The XPath runtime state.
   * @param targetElement Element to check
   *
   * @return true if the whitespace should be stripped.
   *
   * @throws TransformerException
   */
  public boolean shouldStripWhiteSpace(
          XPathContext support, Element targetElement) throws TransformerException;

  /**
   * Get information about whether or not whitespace can be stripped.
   * @see <a href="http://www.w3.org/TR/xslt#strip">strip in XSLT Specification</a>
   *
   * @return true if the whitespace can be stripped.
   */
  public boolean canStripWhiteSpace();
}
