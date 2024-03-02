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
 * $Id: Operation.java,v 1.2.4.1 2005/09/14 21:31:42 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.operations;

import jaxp.sun.org.apache.xpath.internal.Expression;
import jaxp.sun.org.apache.xpath.internal.ExpressionOwner;
import jaxp.sun.org.apache.xpath.internal.XPathContext;
import jaxp.sun.org.apache.xpath.internal.XPathVisitor;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;
import jaxp.sun.org.apache.xpath.internal.XPathVisitable;
import jaxp.xml.transform.TransformerException;

/**
 * The baseclass for a binary operation.
 */
public class Operation extends Expression implements ExpressionOwner
{
    static final long serialVersionUID = -3037139537171050430L;

  /** The left operand expression.
   *  @serial */
  protected Expression m_left;

  /** The right operand expression.
   *  @serial */
  protected Expression m_right;

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
    m_left.fixupVariables(vars, globalsSize);
    m_right.fixupVariables(vars, globalsSize);
  }


  /**
   * Tell if this expression or it's subexpressions can traverse outside
   * the current subtree.
   *
   * @return true if traversal outside the context node's subtree can occur.
   */
  public boolean canTraverseOutsideSubtree()
  {

    if (null != m_left && m_left.canTraverseOutsideSubtree())
      return true;

    if (null != m_right && m_right.canTraverseOutsideSubtree())
      return true;

    return false;
  }

  /**
   * Set the left and right operand expressions for this operation.
   *
   *
   * @param l The left expression operand.
   * @param r The right expression operand.
   */
  public void setLeftRight(Expression l, Expression r)
  {
    m_left = l;
    m_right = r;
    l.exprSetParent(this);
    r.exprSetParent(this);
  }

  /**
   * Execute a binary operation by calling execute on each of the operands,
   * and then calling the operate method on the derived class.
   *
   *
   * @param xctxt The runtime execution context.
   *
   * @return The XObject result of the operation.
   *
   * @throws TransformerException
   */
  public XObject execute(XPathContext xctxt)
          throws TransformerException
  {

    XObject left = m_left.execute(xctxt, true);
    XObject right = m_right.execute(xctxt, true);

    XObject result = operate(left, right);
    left.detach();
    right.detach();
    return result;
  }

  /**
   * Apply the operation to two operands, and return the result.
   *
   *
   * @param left non-null reference to the evaluated left operand.
   * @param right non-null reference to the evaluated right operand.
   *
   * @return non-null reference to the XObject that represents the result of the operation.
   *
   * @throws TransformerException
   */
  public XObject operate(XObject left, XObject right)
          throws TransformerException
  {
    return null;  // no-op
  }

  /** @return the left operand of binary operation, as an Expression.
   */
  public Expression getLeftOperand(){
    return m_left;
  }

  /** @return the right operand of binary operation, as an Expression.
   */
  public Expression getRightOperand(){
    return m_right;
  }

  class LeftExprOwner implements ExpressionOwner
  {
    /**
     * @see ExpressionOwner#getExpression()
     */
    public Expression getExpression()
    {
      return m_left;
    }

    /**
     * @see ExpressionOwner#setExpression(Expression)
     */
    public void setExpression(Expression exp)
    {
        exp.exprSetParent(Operation.this);
        m_left = exp;
    }
  }

  /**
   * @see XPathVisitable#callVisitors(ExpressionOwner, XPathVisitor)
   */
  public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
  {
        if(visitor.visitBinaryOperation(owner, this))
        {
                m_left.callVisitors(new LeftExprOwner(), visitor);
                m_right.callVisitors(this, visitor);
        }
  }

  /**
   * @see ExpressionOwner#getExpression()
   */
  public Expression getExpression()
  {
    return m_right;
  }

  /**
   * @see ExpressionOwner#setExpression(Expression)
   */
  public void setExpression(Expression exp)
  {
        exp.exprSetParent(this);
        m_right = exp;
  }

  /**
   * @see Expression#deepEquals(Expression)
   */
  public boolean deepEquals(Expression expr)
  {
        if(!isSameClass(expr))
                return false;

        if(!m_left.deepEquals(((Operation)expr).m_left))
                return false;

        if(!m_right.deepEquals(((Operation)expr).m_right))
                return false;

        return true;
  }
}
