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
 * $Id: FuncLoader.java,v 1.1.2.1 2005/08/01 01:30:35 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal.compiler;

import jaxp.xml.transform.TransformerException;

import jaxp.sun.org.apache.xpath.internal.functions.Function;
import jaxp.sun.org.apache.xalan.internal.utils.ObjectFactory;
import jaxp.sun.org.apache.xalan.internal.utils.ConfigurationError;

/**
 * Lazy load of functions into the function table as needed, so we don't
 * have to load all the functions allowed in XPath and XSLT on startup.
 * @xsl.usage advanced
 */
public class FuncLoader
{

  /** The function ID, which may correspond to one of the FUNC_XXX values
   *  found in {@link FunctionTable}, but may
   *  be a value installed by an external module.  */
  private int m_funcID;

  /** The class name of the function.  Must not be null.   */
  private String m_funcName;

  /**
   * Get the local class name of the function class.  If function name does
   * not have a '.' in it, it is assumed to be relative to
   * 'com.sun.org.apache.xpath.internal.functions'.
   *
   * @return The class name of the {com.sun.org.apache.xpath.internal.functions.Function} class.
   */
  public String getName()
  {
    return m_funcName;
  }

  /**
   * Construct a function loader
   *
   * @param funcName The class name of the {com.sun.org.apache.xpath.internal.functions.Function}
   *             class, which, if it does not have a '.' in it, is assumed to
   *             be relative to 'com.sun.org.apache.xpath.internal.functions'.
   * @param funcID  The function ID, which may correspond to one of the FUNC_XXX
   *    values found in {@link FunctionTable}, but may
   *    be a value installed by an external module.
   */
  public FuncLoader(String funcName, int funcID)
  {

    super();

    m_funcID = funcID;
    m_funcName = funcName;
  }

  /**
   * Get a Function instance that this instance is liaisoning for.
   *
   * @return non-null reference to Function derivative.
   *
   * @throws TransformerException if ClassNotFoundException,
   *    IllegalAccessException, or InstantiationException is thrown.
   */
  Function getFunction() throws TransformerException
  {
    try
    {
      String className = m_funcName;
      if (className.indexOf(".") < 0) {
        className = "jaxp.sun.org.apache.xpath.internal.functions." + className;
      }
      //hack for loading only built-in function classes.
      String subString = className.substring(0,className.lastIndexOf('.'));
      if(!(subString.equals ("jaxp.sun.org.apache.xalan.internal.templates") ||
           subString.equals ("jaxp.sun.org.apache.xpath.internal.functions"))) {
            throw new TransformerException("Application can't install his own xpath function.");
      }

      return (Function) ObjectFactory.newInstance(className, true);

    }
    catch (ConfigurationError e)
    {
      throw new TransformerException(e.getException());
    }
  }
}
