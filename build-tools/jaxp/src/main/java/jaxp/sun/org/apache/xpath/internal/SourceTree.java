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
 * $Id: SourceTree.java,v 1.1.2.1 2005/08/01 01:30:15 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal;


/**
 * This object represents a Source Tree, and any associated
 * information.
 * @xsl.usage internal
 */
public class SourceTree
{

  /**
   * Constructor SourceTree
   *
   *
   * @param root The root of the source tree, which may or may not be a
   * {@link org.w3c.dom.Document} node.
   * @param url The URI of the source tree.
   */
  public SourceTree(int root, String url)
  {
    m_root = root;
    m_url = url;
  }

  /** The URI of the source tree.   */
  public String m_url;

  /** The root of the source tree, which may or may not be a
   * {@link org.w3c.dom.Document} node.  */
  public int m_root;
}
