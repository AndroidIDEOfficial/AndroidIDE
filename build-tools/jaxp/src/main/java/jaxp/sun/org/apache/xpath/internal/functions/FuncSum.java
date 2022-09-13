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
 * $Id: FuncSum.java,v 1.2.4.1 2005/09/14 20:18:46 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.functions;

import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xml.internal.dtm.DTMIterator;
import jaxp.sun.org.apache.xml.internal.utils.XMLString;
import jaxp.sun.org.apache.xpath.internal.XPathContext;
import jaxp.sun.org.apache.xpath.internal.objects.XNumber;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;
import jaxp.xml.transform.TransformerException;

/**
 * Execute the Sum() function.
 * @xsl.usage advanced
 */
public class FuncSum extends FunctionOneArg
{
    static final long serialVersionUID = -2719049259574677519L;

  /**
   * Execute the function.  The function must return
   * a valid object.
   * @param xctxt The current execution context.
   * @return A valid XObject.
   *
   * @throws TransformerException
   */
  public XObject execute(XPathContext xctxt) throws TransformerException
  {

    DTMIterator nodes = m_arg0.asIterator(xctxt, xctxt.getCurrentNode());
    double sum = 0.0;
    int pos;

    while (DTM.NULL != (pos = nodes.nextNode()))
    {
      DTM dtm = nodes.getDTM(pos);
      XMLString s = dtm.getStringValue(pos);

      if (null != s)
        sum += s.toDouble();
    }
    nodes.detach();

    return new XNumber(sum);
  }
}
