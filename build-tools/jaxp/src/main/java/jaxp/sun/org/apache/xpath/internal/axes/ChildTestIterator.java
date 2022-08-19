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
 * $Id: ChildTestIterator.java,v 1.2.4.2 2005/09/14 19:45:20 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.axes;

import jaxp.sun.org.apache.xml.internal.dtm.Axis;
import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import jaxp.sun.org.apache.xml.internal.dtm.DTMIterator;
import jaxp.sun.org.apache.xpath.internal.compiler.Compiler;
import jaxp.xml.transform.TransformerException;

/**
 * This class implements an optimized iterator for
 * children patterns that have a node test, and possibly a predicate.
 * @see BasicTestIterator
 * @xsl.usage advanced
 */
public class ChildTestIterator extends BasicTestIterator
{
    static final long serialVersionUID = -7936835957960705722L;
  /** The traverser to use to navigate over the descendants. */
  transient protected DTMAxisTraverser m_traverser;

  /** The extended type ID, not set until setRoot. */
//  protected int m_extendedTypeID;


  /**
   * Create a ChildTestIterator object.
   *
   * @param compiler A reference to the Compiler that contains the op map.
   * @param opPos The position within the op map, which contains the
   * location path expression for this itterator.
   *
   * @throws TransformerException
   */
  ChildTestIterator(Compiler compiler, int opPos, int analysis)
          throws TransformerException
  {
    super(compiler, opPos, analysis);
  }

  /**
   * Create a ChildTestIterator object.
   *
   * @param traverser Traverser that tells how the KeyIterator is to be handled.
   *
   * @throws TransformerException
   */
  public ChildTestIterator(DTMAxisTraverser traverser)
  {

    super(null);

    m_traverser = traverser;
  }

  /**
   * Get the next node via getNextXXX.  Bottlenecked for derived class override.
   * @return The next node on the axis, or DTM.NULL.
   */
  protected int getNextNode()
  {
    if(true /* 0 == m_extendedTypeID */)
    {
      m_lastFetched = (DTM.NULL == m_lastFetched)
                   ? m_traverser.first(m_context)
                   : m_traverser.next(m_context, m_lastFetched);
    }
//    else
//    {
//      m_lastFetched = (DTM.NULL == m_lastFetched)
//                   ? m_traverser.first(m_context, m_extendedTypeID)
//                   : m_traverser.next(m_context, m_lastFetched,
//                                      m_extendedTypeID);
//    }

    return m_lastFetched;
  }


  /**
   *  Get a cloned Iterator that is reset to the beginning
   *  of the query.
   *
   *  @return A cloned NodeIterator set of the start of the query.
   *
   *  @throws CloneNotSupportedException
   */
  public DTMIterator cloneWithReset() throws CloneNotSupportedException
  {

    ChildTestIterator clone = (ChildTestIterator) super.cloneWithReset();
    clone.m_traverser = m_traverser;

    return clone;
  }


  /**
   * Initialize the context values for this expression
   * after it is cloned.
   *
   * @param context The XPath runtime context for this
   * transformation.
   */
  public void setRoot(int context, Object environment)
  {
    super.setRoot(context, environment);
    m_traverser = m_cdtm.getAxisTraverser(Axis.CHILD);

//    String localName = getLocalName();
//    String namespace = getNamespace();
//    int what = m_whatToShow;
//    // System.out.println("what: ");
//    // NodeTest.debugWhatToShow(what);
//    if(DTMFilter.SHOW_ALL == what ||
//       ((DTMFilter.SHOW_ELEMENT & what) == 0)
//       || localName == NodeTest.WILD
//       || namespace == NodeTest.WILD)
//    {
//      m_extendedTypeID = 0;
//    }
//    else
//    {
//      int type = getNodeTypeTest(what);
//      m_extendedTypeID = m_cdtm.getExpandedTypeID(namespace, localName, type);
//    }

  }

  /**
   * Returns the axis being iterated, if it is known.
   *
   * @return Axis.CHILD, etc., or -1 if the axis is not known or is of multiple
   * types.
   */
  public int getAxis()
  {
    return Axis.CHILD;
  }

  /**
   *  Detaches the iterator from the set which it iterated over, releasing
   * any computational resources and placing the iterator in the INVALID
   * state. After<code>detach</code> has been invoked, calls to
   * <code>nextNode</code> or<code>previousNode</code> will raise the
   * exception INVALID_STATE_ERR.
   */
  public void detach()
  {
    if(m_allowDetach)
    {
      m_traverser = null;

      // Always call the superclass detach last!
      super.detach();
    }
  }

}
