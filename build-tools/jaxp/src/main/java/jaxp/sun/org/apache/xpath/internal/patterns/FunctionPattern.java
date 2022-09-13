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
 * $Id: FunctionPattern.java,v 1.2.4.2 2005/09/15 00:21:15 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.patterns;

import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xml.internal.dtm.DTMIterator;
import jaxp.sun.org.apache.xpath.internal.Expression;
import jaxp.sun.org.apache.xpath.internal.ExpressionOwner;
import jaxp.sun.org.apache.xpath.internal.XPathContext;
import jaxp.sun.org.apache.xpath.internal.XPathVisitor;
import jaxp.sun.org.apache.xpath.internal.objects.XNumber;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;
import jaxp.sun.org.apache.xpath.internal.functions.Function;
import jaxp.xml.transform.TransformerException;

/**
 * Match pattern step that contains a function.
 * @xsl.usage advanced
 */
public class FunctionPattern extends StepPattern
{
    static final long serialVersionUID = -5426793413091209944L;

  /**
   * Construct a FunctionPattern from a
   * {@link Function expression}.
   *
   * NEEDSDOC @param expr
   */
  public FunctionPattern(Expression expr, int axis, int predaxis)
  {

    super(0, null, null, axis, predaxis);

    m_functionExpr = expr;
  }

  /**
   * Static calc of match score.
   */
  public final void calcScore()
  {

    m_score = SCORE_OTHER;

    if (null == m_targetString)
      calcTargetString();
  }

  /**
   * Should be a {@link Function expression}.
   *  @serial
   */
  Expression m_functionExpr;

  /**
   * This function is used to fixup variables from QNames to stack frame
   * indexes at stylesheet build time.
   * @param vars List of QNames that correspond to variables.  This list
   * should be searched backwards for the first qualified name that
   * corresponds to the variable reference qname.  The position of the
   * QName in the vector from the start of the vector will be its position
   * in the stack frame (but variables above the globalsTop value will need
   * to be offset to the current stack frame).
   */
  public void fixupVariables(java.util.Vector vars, int globalsSize)
  {
    super.fixupVariables(vars, globalsSize);
    m_functionExpr.fixupVariables(vars, globalsSize);
  }


  /**
   * Test a node to see if it matches the given node test.
   *
   * @param xctxt XPath runtime context.
   *
   * @return {@link NodeTest#SCORE_NODETEST},
   *         {@link NodeTest#SCORE_NONE},
   *         {@link NodeTest#SCORE_NSWILD},
   *         {@link NodeTest#SCORE_QNAME}, or
   *         {@link NodeTest#SCORE_OTHER}.
   *
   * @throws TransformerException
   */
  public XObject execute(XPathContext xctxt, int context)
          throws TransformerException
  {

    DTMIterator nl = m_functionExpr.asIterator(xctxt, context);
    XNumber score = SCORE_NONE;

    if (null != nl)
    {
      int n;

      while (DTM.NULL != (n = nl.nextNode()))
      {
        score = (n == context) ? SCORE_OTHER : SCORE_NONE;

        if (score == SCORE_OTHER)
        {
          context = n;

          break;
        }
      }

      // nl.detach();
    }
    nl.detach();

    return score;
  }

  /**
   * Test a node to see if it matches the given node test.
   *
   * @param xctxt XPath runtime context.
   *
   * @return {@link NodeTest#SCORE_NODETEST},
   *         {@link NodeTest#SCORE_NONE},
   *         {@link NodeTest#SCORE_NSWILD},
   *         {@link NodeTest#SCORE_QNAME}, or
   *         {@link NodeTest#SCORE_OTHER}.
   *
   * @throws TransformerException
   */
  public XObject execute(XPathContext xctxt, int context,
                         DTM dtm, int expType)
          throws TransformerException
  {

    DTMIterator nl = m_functionExpr.asIterator(xctxt, context);
    XNumber score = SCORE_NONE;

    if (null != nl)
    {
      int n;

      while (DTM.NULL != (n = nl.nextNode()))
      {
        score = (n == context) ? SCORE_OTHER : SCORE_NONE;

        if (score == SCORE_OTHER)
        {
          context = n;

          break;
        }
      }

      nl.detach();
    }

    return score;
  }

  /**
   * Test a node to see if it matches the given node test.
   *
   * @param xctxt XPath runtime context.
   *
   * @return {@link NodeTest#SCORE_NODETEST},
   *         {@link NodeTest#SCORE_NONE},
   *         {@link NodeTest#SCORE_NSWILD},
   *         {@link NodeTest#SCORE_QNAME}, or
   *         {@link NodeTest#SCORE_OTHER}.
   *
   * @throws TransformerException
   */
  public XObject execute(XPathContext xctxt)
          throws TransformerException
  {

    int context = xctxt.getCurrentNode();
    DTMIterator nl = m_functionExpr.asIterator(xctxt, context);
    XNumber score = SCORE_NONE;

    if (null != nl)
    {
      int n;

      while (DTM.NULL != (n = nl.nextNode()))
      {
        score = (n == context) ? SCORE_OTHER : SCORE_NONE;

        if (score == SCORE_OTHER)
        {
          context = n;

          break;
        }
      }

      nl.detach();
    }

    return score;
  }

  class FunctionOwner implements ExpressionOwner
  {
    /**
     * @see ExpressionOwner#getExpression()
     */
    public Expression getExpression()
    {
      return m_functionExpr;
    }


    /**
     * @see ExpressionOwner#setExpression(Expression)
     */
    public void setExpression(Expression exp)
    {
        exp.exprSetParent(FunctionPattern.this);
        m_functionExpr = exp;
    }
  }

  /**
   * Call the visitor for the function.
   */
  protected void callSubtreeVisitors(XPathVisitor visitor)
  {
    m_functionExpr.callVisitors(new FunctionOwner(), visitor);
    super.callSubtreeVisitors(visitor);
  }

}
