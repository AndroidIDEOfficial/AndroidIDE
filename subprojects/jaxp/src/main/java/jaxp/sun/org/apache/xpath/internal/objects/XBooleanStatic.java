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
 * $Id: XBooleanStatic.java,v 1.2.4.2 2005/09/14 20:34:46 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.objects;

import jaxp.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import jaxp.xml.transform.TransformerException;

/**
 * This class doesn't have any XPathContext, so override
 * whatever to ensure it works OK.
 * @xsl.usage internal
 */
public class XBooleanStatic extends XBoolean
{
    static final long serialVersionUID = -8064147275772687409L;

  /** The value of the object.
   *  @serial          */
  private final boolean m_val;

  /**
   * Construct a XBooleanStatic object.
   *
   * @param b The value of the object
   */
  public XBooleanStatic(boolean b)
  {

    super(b);

    m_val = b;
  }

  /**
   * Tell if two objects are functionally equal.
   *
   * @param obj2 Object to compare to this
   *
   * @return True if the two objects are equal
   *
   * @throws TransformerException
   */
  public boolean equals(XObject obj2)
  {
    try
    {
      return m_val == obj2.bool();
    }
    catch(TransformerException te)
    {
      throw new WrappedRuntimeException(te);
    }
  }
}
