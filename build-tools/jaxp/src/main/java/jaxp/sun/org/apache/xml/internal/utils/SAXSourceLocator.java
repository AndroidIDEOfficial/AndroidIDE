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
 * $Id: SAXSourceLocator.java,v 1.2.4.1 2005/09/15 08:15:52 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.utils;

import java.io.Serializable;

import jaxp.xml.transform.SourceLocator;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

/**
 * Class SAXSourceLocator extends org.xml.sax.helpers.LocatorImpl
 * for the purpose of implementing the SourceLocator interface,
 * and thus can be both a SourceLocator and a SAX Locator.
 */
public class SAXSourceLocator extends LocatorImpl
        implements SourceLocator, Serializable
{
    static final long serialVersionUID = 3181680946321164112L;
  /** The SAX Locator object.
   *  @serial
   */
  Locator m_locator;

  /**
   * Constructor SAXSourceLocator
   *
   */
  public SAXSourceLocator(){}

  /**
   * Constructor SAXSourceLocator
   *
   *
   * @param locator Source locator
   */
  public SAXSourceLocator(Locator locator)
  {
    m_locator = locator;
    this.setColumnNumber(locator.getColumnNumber());
    this.setLineNumber(locator.getLineNumber());
    this.setPublicId(locator.getPublicId());
    this.setSystemId(locator.getSystemId());
  }

  /**
   * Constructor SAXSourceLocator
   *
   *
   * @param locator Source locator
   */
  public SAXSourceLocator(SourceLocator locator)
  {
    m_locator = null;
    this.setColumnNumber(locator.getColumnNumber());
    this.setLineNumber(locator.getLineNumber());
    this.setPublicId(locator.getPublicId());
    this.setSystemId(locator.getSystemId());
  }


  /**
   * Constructor SAXSourceLocator
   *
   *
   * @param spe SAXParseException exception.
   */
  public SAXSourceLocator(SAXParseException spe)
  {
    this.setLineNumber( spe.getLineNumber() );
    this.setColumnNumber( spe.getColumnNumber() );
    this.setPublicId( spe.getPublicId() );
    this.setSystemId( spe.getSystemId() );
  }

  /**
   * Return the public identifier for the current document event.
   *
   * <p>The return value is the public identifier of the document
   * entity or of the external parsed entity in which the markup
   * triggering the event appears.</p>
   *
   * @return A string containing the public identifier, or
   *         null if none is available.
   * @see #getSystemId
   */
  public String getPublicId()
  {
    return (null == m_locator) ? super.getPublicId() : m_locator.getPublicId();
  }

  /**
   * Return the system identifier for the current document event.
   *
   * <p>The return value is the system identifier of the document
   * entity or of the external parsed entity in which the markup
   * triggering the event appears.</p>
   *
   * <p>If the system identifier is a URL, the parser must resolve it
   * fully before passing it to the application.</p>
   *
   * @return A string containing the system identifier, or null
   *         if none is available.
   * @see #getPublicId
   */
  public String getSystemId()
  {
    return (null == m_locator) ? super.getSystemId() : m_locator.getSystemId();
  }

  /**
   * Return the line number where the current document event ends.
   *
   * <p><strong>Warning:</strong> The return value from the method
   * is intended only as an approximation for the sake of error
   * reporting; it is not intended to provide sufficient information
   * to edit the character content of the original XML document.</p>
   *
   * <p>The return value is an approximation of the line number
   * in the document entity or external parsed entity where the
   * markup triggering the event appears.</p>
   *
   * @return The line number, or -1 if none is available.
   * @see #getColumnNumber
   */
  public int getLineNumber()
  {
    return (null == m_locator) ? super.getLineNumber() : m_locator.getLineNumber();
  }

  /**
   * Return the column number where the current document event ends.
   *
   * <p><strong>Warning:</strong> The return value from the method
   * is intended only as an approximation for the sake of error
   * reporting; it is not intended to provide sufficient information
   * to edit the character content of the original XML document.</p>
   *
   * <p>The return value is an approximation of the column number
   * in the document entity or external parsed entity where the
   * markup triggering the event appears.</p>
   *
   * @return The column number, or -1 if none is available.
   * @see #getLineNumber
   */
  public int getColumnNumber()
  {
    return (null == m_locator) ? super.getColumnNumber() : m_locator.getColumnNumber();
  }
}
