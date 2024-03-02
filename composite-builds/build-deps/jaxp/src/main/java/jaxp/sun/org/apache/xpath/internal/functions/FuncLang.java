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
 * $Id: FuncLang.java,v 1.2.4.1 2005/09/14 20:18:44 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.functions;

import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xpath.internal.XPathContext;
import jaxp.sun.org.apache.xpath.internal.objects.XBoolean;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;
import jaxp.xml.transform.TransformerException;

/**
 * Execute the Lang() function.
 * @xsl.usage advanced
 */
public class FuncLang extends FunctionOneArg
{
    static final long serialVersionUID = -7868705139354872185L;

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

    String lang = m_arg0.execute(xctxt).str();
    int parent = xctxt.getCurrentNode();
    boolean isLang = false;
    DTM dtm = xctxt.getDTM(parent);

    while (DTM.NULL != parent)
    {
      if (DTM.ELEMENT_NODE == dtm.getNodeType(parent))
      {
        int langAttr = dtm.getAttributeNode(parent, "http://www.w3.org/XML/1998/namespace", "lang");

        if (DTM.NULL != langAttr)
        {
          String langVal = dtm.getNodeValue(langAttr);
          // %OPT%
          if (langVal.toLowerCase().startsWith(lang.toLowerCase()))
          {
            int valLen = lang.length();

            if ((langVal.length() == valLen)
                    || (langVal.charAt(valLen) == '-'))
            {
              isLang = true;
            }
          }

          break;
        }
      }

      parent = dtm.getParent(parent);
    }

    return isLang ? XBoolean.S_TRUE : XBoolean.S_FALSE;
  }
}
