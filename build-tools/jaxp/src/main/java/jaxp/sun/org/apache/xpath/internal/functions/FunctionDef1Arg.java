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
 * $Id: FunctionDef1Arg.java,v 1.2.4.1 2005/09/14 20:18:42 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.functions;

import jaxp.sun.org.apache.xalan.internal.res.XSLMessages;
import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xml.internal.utils.XMLString;
import jaxp.sun.org.apache.xpath.internal.XPathContext;
import jaxp.sun.org.apache.xpath.internal.objects.XString;
import jaxp.sun.org.apache.xpath.internal.res.XPATHErrorResources;
import jaxp.xml.transform.TransformerException;

/**
 * Base class for functions that accept one argument that can be defaulted if
 * not specified.
 * @xsl.usage advanced
 */
public class FunctionDef1Arg extends FunctionOneArg
{
    static final long serialVersionUID = 2325189412814149264L;

  /**
   * Execute the first argument expression that is expected to return a
   * nodeset.  If the argument is null, then return the current context node.
   *
   * @param xctxt Runtime XPath context.
   *
   * @return The first node of the executed nodeset, or the current context
   *         node if the first argument is null.
   *
   * @throws TransformerException if an error occurs while
   *                                   executing the argument expression.
   */
  protected int getArg0AsNode(XPathContext xctxt)
          throws TransformerException
  {

    return (null == m_arg0)
           ? xctxt.getCurrentNode() : m_arg0.asNode(xctxt);
  }

  /**
   * Tell if the expression is a nodeset expression.
   * @return true if the expression can be represented as a nodeset.
   */
  public boolean Arg0IsNodesetExpr()
  {
    return (null == m_arg0) ? true : m_arg0.isNodesetExpr();
  }

  /**
   * Execute the first argument expression that is expected to return a
   * string.  If the argument is null, then get the string value from the
   * current context node.
   *
   * @param xctxt Runtime XPath context.
   *
   * @return The string value of the first argument, or the string value of the
   *         current context node if the first argument is null.
   *
   * @throws TransformerException if an error occurs while
   *                                   executing the argument expression.
   */
  protected XMLString getArg0AsString(XPathContext xctxt)
          throws TransformerException
  {
    if(null == m_arg0)
    {
      int currentNode = xctxt.getCurrentNode();
      if(DTM.NULL == currentNode)
        return XString.EMPTYSTRING;
      else
      {
        DTM dtm = xctxt.getDTM(currentNode);
        return dtm.getStringValue(currentNode);
      }

    }
    else
      return m_arg0.execute(xctxt).xstr();
  }

  /**
   * Execute the first argument expression that is expected to return a
   * number.  If the argument is null, then get the number value from the
   * current context node.
   *
   * @param xctxt Runtime XPath context.
   *
   * @return The number value of the first argument, or the number value of the
   *         current context node if the first argument is null.
   *
   * @throws TransformerException if an error occurs while
   *                                   executing the argument expression.
   */
  protected double getArg0AsNumber(XPathContext xctxt)
          throws TransformerException
  {

    if(null == m_arg0)
    {
      int currentNode = xctxt.getCurrentNode();
      if(DTM.NULL == currentNode)
        return 0;
      else
      {
        DTM dtm = xctxt.getDTM(currentNode);
        XMLString str = dtm.getStringValue(currentNode);
        return str.toDouble();
      }

    }
    else
      return m_arg0.execute(xctxt).num();
  }

  /**
   * Check that the number of arguments passed to this function is correct.
   *
   * @param argNum The number of arguments that is being passed to the function.
   *
   * @throws WrongNumberArgsException if the number of arguments is not 0 or 1.
   */
  public void checkNumberArgs(int argNum) throws WrongNumberArgsException
  {
    if (argNum > 1)
      reportWrongNumberArgs();
  }

  /**
   * Constructs and throws a WrongNumberArgException with the appropriate
   * message for this function object.
   *
   * @throws WrongNumberArgsException
   */
  protected void reportWrongNumberArgs() throws WrongNumberArgsException {
      throw new WrongNumberArgsException(XSLMessages.createXPATHMessage(XPATHErrorResources.ER_ZERO_OR_ONE, null)); //"0 or 1");
  }

  /**
   * Tell if this expression or it's subexpressions can traverse outside
   * the current subtree.
   *
   * @return true if traversal outside the context node's subtree can occur.
   */
  public boolean canTraverseOutsideSubtree()
  {
    return (null == m_arg0) ? false : super.canTraverseOutsideSubtree();
  }
}
