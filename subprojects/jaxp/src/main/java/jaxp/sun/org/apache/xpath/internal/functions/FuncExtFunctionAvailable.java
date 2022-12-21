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
 * Copyright 1999-2005 The Apache Software Foundation.
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
 * $Id: FuncExtFunctionAvailable.java,v 1.2.4.1 2005/09/14 20:05:08 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.functions;

import jaxp.sun.org.apache.xalan.internal.templates.Constants;
import jaxp.sun.org.apache.xpath.internal.ExtensionsProvider;
import jaxp.sun.org.apache.xpath.internal.XPathContext;
import jaxp.sun.org.apache.xpath.internal.compiler.FunctionTable;
import jaxp.sun.org.apache.xpath.internal.objects.XBoolean;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;
import jaxp.xml.transform.TransformerException;

/**
 * Execute the ExtFunctionAvailable() function.
 * @xsl.usage advanced
 */
public class FuncExtFunctionAvailable extends FunctionOneArg
{
    static final long serialVersionUID = 5118814314918592241L;

    transient private FunctionTable m_functionTable = null;

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

    String prefix;
    String namespace;
    String methName;

    String fullName = m_arg0.execute(xctxt).str();
    int indexOfNSSep = fullName.indexOf(':');

    if (indexOfNSSep < 0)
    {
      prefix = "";
      namespace = Constants.S_XSLNAMESPACEURL;
      methName = fullName;
    }
    else
    {
      prefix = fullName.substring(0, indexOfNSSep);
      namespace = xctxt.getNamespaceContext().getNamespaceForPrefix(prefix);
      if (null == namespace)
        return XBoolean.S_FALSE;
        methName = fullName.substring(indexOfNSSep + 1);
    }

    if (namespace.equals(Constants.S_XSLNAMESPACEURL))
    {
      try
      {
        if (null == m_functionTable) m_functionTable = new FunctionTable();
        return m_functionTable.functionAvailable(methName) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
      }
      catch (Exception e)
      {
        return XBoolean.S_FALSE;
      }
    }
    else
    {
      //dml
      ExtensionsProvider extProvider = (ExtensionsProvider)xctxt.getOwnerObject();
      return extProvider.functionAvailable(namespace, methName)
             ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
  }

  /**
   * The function table is an instance field. In order to access this instance
   * field during evaluation, this method is called at compilation time to
   * insert function table information for later usage. It should only be used
   * during compiling of XPath expressions.
   * @param aTable an instance of the function table
   */
  public void setFunctionTable(FunctionTable aTable){
          m_functionTable = aTable;
  }
}
