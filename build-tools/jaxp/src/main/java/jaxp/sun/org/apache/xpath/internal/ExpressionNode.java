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
 * $Id: ExpressionNode.java,v 1.1.2.1 2005/08/01 01:30:15 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal;

import jaxp.xml.transform.SourceLocator;

/**
 * A class that implements this interface can construct expressions,
 * give information about child and parent expressions,
 * and give the originating source information.  A class that implements
 * this interface does not lay any claim to being directly executable.
 *
 * <p>Note: This interface should not be considered stable.  Only exprSetParent
 * and exprGetParent can be counted on to work reliably.  Work in progress.</p>
 */
public interface ExpressionNode extends SourceLocator
{
  /** This pair of methods are used to inform the node of its
    parent. */
  public void exprSetParent(ExpressionNode n);
  public ExpressionNode exprGetParent();

  /** This method tells the node to add its argument to the node's
    list of children.  */
  public void exprAddChild(ExpressionNode n, int i);

  /** This method returns a child node.  The children are numbered
     from zero, left to right. */
  public ExpressionNode exprGetChild(int i);

  /** Return the number of children the node has. */
  public int exprGetNumChildren();
}
