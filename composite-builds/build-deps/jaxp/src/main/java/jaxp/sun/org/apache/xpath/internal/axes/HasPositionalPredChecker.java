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
 * $Id: HasPositionalPredChecker.java,v 1.1.2.1 2005/08/01 01:30:24 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.axes;

import jaxp.sun.org.apache.xpath.internal.Expression;
import jaxp.sun.org.apache.xpath.internal.ExpressionOwner;
import jaxp.sun.org.apache.xpath.internal.XPathVisitor;
import jaxp.sun.org.apache.xpath.internal.functions.FuncLast;
import jaxp.sun.org.apache.xpath.internal.functions.FuncPosition;
import jaxp.sun.org.apache.xpath.internal.functions.Function;
import jaxp.sun.org.apache.xpath.internal.objects.XNumber;
import jaxp.sun.org.apache.xpath.internal.operations.Div;
import jaxp.sun.org.apache.xpath.internal.operations.Minus;
import jaxp.sun.org.apache.xpath.internal.operations.Mod;
import jaxp.sun.org.apache.xpath.internal.operations.Mult;
import jaxp.sun.org.apache.xpath.internal.operations.Number;
import jaxp.sun.org.apache.xpath.internal.operations.Plus;
import jaxp.sun.org.apache.xpath.internal.operations.Quo;
import jaxp.sun.org.apache.xpath.internal.operations.Variable;

public class HasPositionalPredChecker extends XPathVisitor
{
        private boolean m_hasPositionalPred = false;
        private int m_predDepth = 0;

        /**
         * Process the LocPathIterator to see if it contains variables
         * or functions that may make it context dependent.
         * @param path LocPathIterator that is assumed to be absolute, but needs checking.
         * @return true if the path is confirmed to be absolute, false if it
         * may contain context dependencies.
         */
        public static boolean check(LocPathIterator path)
        {
                HasPositionalPredChecker hppc = new HasPositionalPredChecker();
                path.callVisitors(null, hppc);
                return hppc.m_hasPositionalPred;
        }

        /**
         * Visit a function.
         * @param owner The owner of the expression, to which the expression can
         *              be reset if rewriting takes place.
         * @param func The function reference object.
         * @return true if the sub expressions should be traversed.
         */
        public boolean visitFunction(ExpressionOwner owner, Function func)
        {
                if((func instanceof FuncPosition) ||
                   (func instanceof FuncLast))
                        m_hasPositionalPred = true;
                return true;
        }

//      /**
//       * Visit a variable reference.
//       * @param owner The owner of the expression, to which the expression can
//       *              be reset if rewriting takes place.
//       * @param var The variable reference object.
//       * @return true if the sub expressions should be traversed.
//       */
//      public boolean visitVariableRef(ExpressionOwner owner, Variable var)
//      {
//              m_hasPositionalPred = true;
//              return true;
//      }

  /**
   * Visit a predicate within a location path.  Note that there isn't a
   * proper unique component for predicates, and that the expression will
   * be called also for whatever type Expression is.
   *
   * @param owner The owner of the expression, to which the expression can
   *              be reset if rewriting takes place.
   * @param pred The predicate object.
   * @return true if the sub expressions should be traversed.
   */
  public boolean visitPredicate(ExpressionOwner owner, Expression pred)
  {
    m_predDepth++;

    if(m_predDepth == 1)
    {
      if((pred instanceof Variable) ||
         (pred instanceof XNumber) ||
         (pred instanceof Div) ||
         (pred instanceof Plus) ||
         (pred instanceof Minus) ||
         (pred instanceof Mod) ||
         (pred instanceof Quo) ||
         (pred instanceof Mult) ||
         (pred instanceof Number) ||
         (pred instanceof Function))
          m_hasPositionalPred = true;
      else
        pred.callVisitors(owner, this);
    }

    m_predDepth--;

    // Don't go have the caller go any further down the subtree.
    return false;
  }


}
