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
 * $Id: AttributeIterator.java,v 1.2.4.1 2005/09/14 19:45:22 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.axes;

import jaxp.sun.org.apache.xml.internal.dtm.Axis;
import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xpath.internal.compiler.Compiler;
import jaxp.xml.transform.TransformerException;

/**
 * This class implements an optimized iterator for
 * attribute axes patterns.
 * @see com.sun.org.apache.xpath.internal.axes#ChildTestIterator
 * @xsl.usage advanced
 */
public class AttributeIterator extends ChildTestIterator
{
    static final long serialVersionUID = -8417986700712229686L;

  /**
   * Create a AttributeIterator object.
   *
   * @param compiler A reference to the Compiler that contains the op map.
   * @param opPos The position within the op map, which contains the
   * location path expression for this itterator.
   *
   * @throws TransformerException
   */
  AttributeIterator(Compiler compiler, int opPos, int analysis)
          throws TransformerException
  {
    super(compiler, opPos, analysis);
  }

  /**
   * Get the next node via getFirstAttribute && getNextAttribute.
   */
  protected int getNextNode()
  {
    m_lastFetched = (DTM.NULL == m_lastFetched)
                     ? m_cdtm.getFirstAttribute(m_context)
                     : m_cdtm.getNextAttribute(m_lastFetched);
    return m_lastFetched;
  }

  /**
   * Returns the axis being iterated, if it is known.
   *
   * @return Axis.CHILD, etc., or -1 if the axis is not known or is of multiple
   * types.
   */
  public int getAxis()
  {
    return Axis.ATTRIBUTE;
  }



}
