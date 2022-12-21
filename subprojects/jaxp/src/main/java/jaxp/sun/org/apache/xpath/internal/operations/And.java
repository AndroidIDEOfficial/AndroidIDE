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
 * $Id: And.java,v 1.2.4.1 2005/09/14 21:31:42 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.operations;

import jaxp.sun.org.apache.xpath.internal.XPathContext;
import jaxp.sun.org.apache.xpath.internal.objects.XBoolean;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;
import jaxp.xml.transform.TransformerException;

/**
 * The 'and' operation expression executer.
 */
public class And extends Operation
{
    static final long serialVersionUID = 392330077126534022L;

  /**
   * AND two expressions and return the boolean result. Override
   * superclass method for optimization purposes.
   *
   * @param xctxt The runtime execution context.
   *
   * @return {@link XBoolean#S_TRUE} or
   * {@link XBoolean#S_FALSE}.
   *
   * @throws TransformerException
   */
  public XObject execute(XPathContext xctxt) throws TransformerException
  {

    XObject expr1 = m_left.execute(xctxt);

    if (expr1.bool())
    {
      XObject expr2 = m_right.execute(xctxt);

      return expr2.bool() ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
    else
      return XBoolean.S_FALSE;
  }

  /**
   * Evaluate this operation directly to a boolean.
   *
   * @param xctxt The runtime execution context.
   *
   * @return The result of the operation as a boolean.
   *
   * @throws TransformerException
   */
  public boolean bool(XPathContext xctxt)
          throws TransformerException
  {
    return (m_left.bool(xctxt) && m_right.bool(xctxt));
  }

}
