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
 * $Id: ContextMatchStepPattern.java,v 1.2.4.2 2005/09/15 00:21:15 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.patterns;

import jaxp.sun.org.apache.xml.internal.dtm.Axis;
import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import jaxp.sun.org.apache.xml.internal.dtm.DTMFilter;
import jaxp.sun.org.apache.xpath.internal.XPathContext;
import jaxp.sun.org.apache.xpath.internal.axes.WalkerFactory;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;
import jaxp.xml.transform.TransformerException;

/**
 * Special context node pattern matcher.
 */
public class ContextMatchStepPattern extends StepPattern
{
    static final long serialVersionUID = -1888092779313211942L;

  /**
   * Construct a ContextMatchStepPattern.
   *
   */
  public ContextMatchStepPattern(int axis, int paxis)
  {
    super(DTMFilter.SHOW_ALL, axis, paxis);
  }

  /**
   * Execute this pattern step, including predicates.
   *
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

    if (xctxt.getIteratorRoot() == xctxt.getCurrentNode())
      return getStaticScore();
    else
      return this.SCORE_NONE;
  }

  /**
   * Execute the match pattern step relative to another step.
   *
   *
   * @param xctxt The XPath runtime context.
   * NEEDSDOC @param prevStep
   *
   * @return {@link NodeTest#SCORE_NODETEST},
   *         {@link NodeTest#SCORE_NONE},
   *         {@link NodeTest#SCORE_NSWILD},
   *         {@link NodeTest#SCORE_QNAME}, or
   *         {@link NodeTest#SCORE_OTHER}.
   *
   * @throws TransformerException
   */
  public XObject executeRelativePathPattern(
          XPathContext xctxt, StepPattern prevStep)
            throws TransformerException
  {

    XObject score = SCORE_NONE;
    int context = xctxt.getCurrentNode();
    DTM dtm = xctxt.getDTM(context);

    if (null != dtm)
    {
      int predContext = xctxt.getCurrentNode();
      DTMAxisTraverser traverser;

      int axis = m_axis;

      boolean needToTraverseAttrs = WalkerFactory.isDownwardAxisOfMany(axis);
      boolean iterRootIsAttr = (dtm.getNodeType(xctxt.getIteratorRoot())
                                 == DTM.ATTRIBUTE_NODE);

      if((Axis.PRECEDING == axis) && iterRootIsAttr)
      {
        axis = Axis.PRECEDINGANDANCESTOR;
      }

      traverser = dtm.getAxisTraverser(axis);

      for (int relative = traverser.first(context); DTM.NULL != relative;
              relative = traverser.next(context, relative))
      {
        try
        {
          xctxt.pushCurrentNode(relative);

          score = execute(xctxt);

          if (score != SCORE_NONE)
          {
              //score = executePredicates( xctxt, prevStep, SCORE_OTHER,
              //       predContext, relative);
              if (executePredicates(xctxt, dtm, context))
                  return score;

              score = SCORE_NONE;
          }

          if(needToTraverseAttrs && iterRootIsAttr
             && (DTM.ELEMENT_NODE == dtm.getNodeType(relative)))
          {
            int xaxis = Axis.ATTRIBUTE;
            for (int i = 0; i < 2; i++)
            {
              DTMAxisTraverser atraverser = dtm.getAxisTraverser(xaxis);

              for (int arelative = atraverser.first(relative);
                      DTM.NULL != arelative;
                      arelative = atraverser.next(relative, arelative))
              {
                try
                {
                  xctxt.pushCurrentNode(arelative);

                  score = execute(xctxt);

                  if (score != SCORE_NONE)
                  {
                      //score = executePredicates( xctxt, prevStep, SCORE_OTHER,
                      //       predContext, arelative);

                    if (score != SCORE_NONE)
                      return score;
                  }
                }
                finally
                {
                  xctxt.popCurrentNode();
                }
              }
              xaxis = Axis.NAMESPACE;
            }
          }

        }
        finally
        {
          xctxt.popCurrentNode();
        }
      }

    }

    return score;
  }

}
