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
 * $Id: XPathProcessorException.java,v 1.2.4.1 2005/09/15 01:42:45 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal;

/**
 * Derived from XPathException in order that XPath processor
 * exceptions may be specifically caught.
 * @xsl.usage general
 */
public class XPathProcessorException extends XPathException
{
    static final long serialVersionUID = 1215509418326642603L;

  /**
   * Create an XPathProcessorException object that holds
   * an error message.
   * @param message The error message.
   */
  public XPathProcessorException(String message)
  {
    super(message);
  }


  /**
   * Create an XPathProcessorException object that holds
   * an error message, and another exception
   * that caused this exception.
   * @param message The error message.
   * @param e The exception that caused this exception.
   */
  public XPathProcessorException(String message, Exception e)
  {
    super(message, e);
  }
}
