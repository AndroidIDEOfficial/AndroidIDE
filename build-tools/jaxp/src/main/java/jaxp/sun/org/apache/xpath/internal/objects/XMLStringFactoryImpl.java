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
 * $Id: XMLStringFactoryImpl.java,v 1.2.4.1 2005/09/10 17:44:29 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.objects;

import jaxp.sun.org.apache.xml.internal.utils.FastStringBuffer;
import jaxp.sun.org.apache.xml.internal.utils.XMLString;
import jaxp.sun.org.apache.xml.internal.utils.XMLStringFactory;

/**
 * Class XMLStringFactoryImpl creates XString versions of XMLStrings.
 * @xsl.usage internal
 */
public class XMLStringFactoryImpl extends XMLStringFactory
{
  /** The XMLStringFactory to pass to DTM construction.   */
  private static XMLStringFactory m_xstringfactory =
    new XMLStringFactoryImpl();

  /**
   * Get the XMLStringFactory to pass to DTM construction.
   *
   *
   * @return A never-null static reference to a String factory.
   */
  public static XMLStringFactory getFactory()
  {
    return m_xstringfactory;
  }

  /**
   * Create a new XMLString from a Java string.
   *
   *
   * @param string Java String reference, which must be non-null.
   *
   * @return An XMLString object that wraps the String reference.
   */
  public XMLString newstr(String string)
  {
    return new XString(string);
  }

  /**
   * Create a XMLString from a FastStringBuffer.
   *
   *
   * @param fsb FastStringBuffer reference, which must be non-null.
   * @param start The start position in the array.
   * @param length The number of characters to read from the array.
   *
   * @return An XMLString object that wraps the FastStringBuffer reference.
   */
  public XMLString newstr(FastStringBuffer fsb, int start, int length)
  {
    return new XStringForFSB(fsb, start, length);
  }

  /**
   * Create a XMLString from a FastStringBuffer.
   *
   *
   * @param string FastStringBuffer reference, which must be non-null.
   * @param start The start position in the array.
   * @param length The number of characters to read from the array.
   *
   * @return An XMLString object that wraps the FastStringBuffer reference.
   */
  public XMLString newstr(char[] string, int start, int length)
  {
    return new XStringForChars(string, start, length);
  }

  /**
   * Get a cheap representation of an empty string.
   *
   * @return An non-null reference to an XMLString that represents "".
   */
  public XMLString emptystr()
  {
    return XString.EMPTYSTRING;
  }

}
