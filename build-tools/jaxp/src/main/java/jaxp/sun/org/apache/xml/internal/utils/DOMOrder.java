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
 * $Id: DOMOrder.java,v 1.2.4.1 2005/09/15 08:15:41 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.utils;

/**
 * @deprecated Since the introduction of the DTM, this class will be removed.
 * Nodes that implement this index can return a document order index.
 * Eventually, this will be replaced by DOM 3 methods.
 * (compareDocumentOrder and/or compareTreePosition.)
 */
public interface DOMOrder
{

  /**
   * Get the UID (document order index).
   *
   * @return integer whose relative value corresponds to document order
   * -- that is, if node1.getUid()<node2.getUid(), node1 comes before
   * node2, and if they're equal node1 and node2 are the same node. No
   * promises are made beyond that.
   */
  public int getUid();
}
