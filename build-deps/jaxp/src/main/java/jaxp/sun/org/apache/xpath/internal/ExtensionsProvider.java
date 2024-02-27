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
 * $Id: ExtensionsProvider.java,v 1.1.2.1 2005/08/01 01:30:08 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal;

import java.util.Vector;

import jaxp.sun.org.apache.xpath.internal.functions.FuncExtFunction;
import jaxp.xml.transform.TransformerException;

/**
 * Interface that XPath objects can call to obtain access to an
 * ExtensionsTable.
 *
 */
public interface ExtensionsProvider
{
  /**
   * Is the extension function available?
   */

  public boolean functionAvailable(String ns, String funcName)
          throws TransformerException;

  /**
   * Is the extension element available?
   */
  public boolean elementAvailable(String ns, String elemName)
          throws TransformerException;

  /**
   * Execute the extension function.
   */
  public Object extFunction(String ns, String funcName,
                            Vector argVec, Object methodKey)
            throws TransformerException;

  /**
   * Execute the extension function.
   */
  public Object extFunction(FuncExtFunction extFunction,
                            Vector argVec)
            throws TransformerException;
}
