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
 * $Id: FuncPosition.java,v 1.2.4.1 2005/09/14 20:18:45 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.functions;

import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xml.internal.dtm.DTMIterator;
import jaxp.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import jaxp.sun.org.apache.xpath.internal.XPathContext;
import jaxp.sun.org.apache.xpath.internal.axes.SubContextList;
import jaxp.sun.org.apache.xpath.internal.compiler.Compiler;
import jaxp.sun.org.apache.xpath.internal.objects.XNumber;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;
import jaxp.xml.transform.TransformerException;

/**
 * Execute the Position() function.
 * @xsl.usage advanced
 */
public class FuncPosition extends Function
{
    static final long serialVersionUID = -9092846348197271582L;
  private boolean m_isTopLevel;

  /**
   * Figure out if we're executing a toplevel expression.
   * If so, we can't be inside of a predicate.
   */
  public void postCompileStep(Compiler compiler)
  {
    m_isTopLevel = compiler.getLocationPathDepth() == -1;
  }

  /**
   * Get the position in the current context node list.
   *
   * @param xctxt Runtime XPath context.
   *
   * @return The current position of the itteration in the context node list,
   *         or -1 if there is no active context node list.
   */
  public int getPositionInContextNodeList(XPathContext xctxt)
  {

    // System.out.println("FuncPosition- entry");
    // If we're in a predicate, then this will return non-null.
    SubContextList iter = m_isTopLevel ? null : xctxt.getSubContextList();

    if (null != iter)
    {
      int prox = iter.getProximityPosition(xctxt);

      // System.out.println("FuncPosition- prox: "+prox);
      return prox;
    }

    DTMIterator cnl = xctxt.getContextNodeList();

    if (null != cnl)
    {
      int n = cnl.getCurrentNode();
      if(n == DTM.NULL)
      {
        if(cnl.getCurrentPos() == 0)
          return 0;

        // Then I think we're in a sort.  See sort21.xsl. So the iterator has
        // already been spent, and is not on the node we're processing.
        // It's highly possible that this is an issue for other context-list
        // functions.  Shouldn't be a problem for last(), and it shouldn't be
        // a problem for current().
        try
        {
          cnl = cnl.cloneWithReset();
        }
        catch(CloneNotSupportedException cnse)
        {
          throw new WrappedRuntimeException(cnse);
        }
        int currentNode = xctxt.getContextNode();
        // System.out.println("currentNode: "+currentNode);
        while(DTM.NULL != (n = cnl.nextNode()))
        {
          if(n == currentNode)
            break;
        }
      }
      // System.out.println("n: "+n);
      // System.out.println("FuncPosition- cnl.getCurrentPos(): "+cnl.getCurrentPos());
      return cnl.getCurrentPos();
    }

    // System.out.println("FuncPosition - out of guesses: -1");
    return -1;
  }

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
    double pos = (double) getPositionInContextNodeList(xctxt);

    return new XNumber(pos);
  }

  /**
   * No arguments to process, so this does nothing.
   */
  public void fixupVariables(java.util.Vector vars, int globalsSize)
  {
    // no-op
  }
}
