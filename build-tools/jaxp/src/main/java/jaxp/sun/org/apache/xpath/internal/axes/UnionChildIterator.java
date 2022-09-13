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
 * $Id: UnionChildIterator.java,v 1.2.4.1 2005/09/14 19:45:20 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.axes;

import jaxp.sun.org.apache.xpath.internal.XPathContext;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;
import jaxp.sun.org.apache.xpath.internal.patterns.NodeTest;
import jaxp.xml.transform.TransformerException;

/**
 * This class defines a simplified type of union iterator that only
 * tests along the child axes.  If the conditions are right, it is
 * much faster than using a UnionPathIterator.
 */
public class UnionChildIterator extends ChildTestIterator
{
    static final long serialVersionUID = 3500298482193003495L;
  /**
   * Even though these may hold full LocPathIterators, this array does
   * not have to be cloned, since only the node test and predicate
   * portion are used, and these only need static information.  However,
   * also note that index predicates can not be used!
   */
  private PredicatedNodeTest[] m_nodeTests = null;

  /**
   * Constructor for UnionChildIterator
   */
  public UnionChildIterator()
  {
    super(null);
  }

  /**
   * Add a node test to the union list.
   *
   * @param test reference to a NodeTest, which will be added
   * directly to the list of node tests (in other words, it will
   * not be cloned).  The parent of this test will be set to
   * this object.
   */
  public void addNodeTest(PredicatedNodeTest test)
  {

    // Increase array size by only 1 at a time.  Fix this
    // if it looks to be a problem.
    if (null == m_nodeTests)
    {
      m_nodeTests = new PredicatedNodeTest[1];
      m_nodeTests[0] = test;
    }
    else
    {
      PredicatedNodeTest[] tests = m_nodeTests;
      int len = m_nodeTests.length;

      m_nodeTests = new PredicatedNodeTest[len + 1];

      System.arraycopy(tests, 0, m_nodeTests, 0, len);

      m_nodeTests[len] = test;
    }
    test.exprSetParent(this);
  }

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
    if (m_nodeTests != null) {
      for (int i = 0; i < m_nodeTests.length; i++) {
        m_nodeTests[i].fixupVariables(vars, globalsSize);
      }
    }
  }

  /**
   * Test whether a specified node is visible in the logical view of a
   * TreeWalker or NodeIterator. This function will be called by the
   * implementation of TreeWalker and NodeIterator; it is not intended to
   * be called directly from user code.
   * @param n  The node to check to see if it passes the filter or not.
   * @return  a constant to determine whether the node is accepted,
   *   rejected, or skipped, as defined  above .
   */
  public short acceptNode(int n)
  {
    XPathContext xctxt = getXPathContext();
    try
    {
      xctxt.pushCurrentNode(n);
      for (int i = 0; i < m_nodeTests.length; i++)
      {
        PredicatedNodeTest pnt = m_nodeTests[i];
        XObject score = pnt.execute(xctxt, n);
        if (score != NodeTest.SCORE_NONE)
        {
          // Note that we are assuming there are no positional predicates!
          if (pnt.getPredicateCount() > 0)
          {
            if (pnt.executePredicates(n, xctxt))
              return FILTER_ACCEPT;
          }
          else
            return FILTER_ACCEPT;

        }
      }
    }
    catch (TransformerException se)
    {

      // TODO: Fix this.
      throw new RuntimeException(se.getMessage());
    }
    finally
    {
      xctxt.popCurrentNode();
    }
    return FILTER_SKIP;
  }

}
