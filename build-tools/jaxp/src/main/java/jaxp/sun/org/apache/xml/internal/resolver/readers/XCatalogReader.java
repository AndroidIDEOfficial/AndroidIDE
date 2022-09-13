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
// XCatalogReader.java - Read XML Catalog files

/*
 * Copyright 2001-2004 The Apache Software Foundation or its licensors,
 * as applicable.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jaxp.sun.org.apache.xml.internal.resolver.readers;

import java.util.Vector;
import jaxp.sun.org.apache.xml.internal.resolver.Catalog;
import jaxp.sun.org.apache.xml.internal.resolver.CatalogEntry;
import jaxp.sun.org.apache.xml.internal.resolver.CatalogException;
import jaxp.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import org.xml.sax.*;

import javax.xml.parsers.*;

import jaxp.xml.parsers.SAXParserFactory;

/**
 * Parse "xcatalog" XML Catalog files, this is the XML Catalog format
 * developed by John Cowan and supported by Apache.
 *
 * @see Catalog
 *
 * @author Norman Walsh
 * <a href="mailto:Norman.Walsh@Sun.COM">Norman.Walsh@Sun.COM</a>
 *
 */
public class XCatalogReader extends SAXCatalogReader implements SAXCatalogParser {
  /** The catalog object needs to be stored by the object so that
   * SAX callbacks can use it.
   */
  protected Catalog catalog = null;

  /** Set the current catalog. */
  public void setCatalog (Catalog catalog) {
    this.catalog = catalog;
  }

  /** Get the current catalog. */
  public Catalog getCatalog () {
    return catalog;
  }

  /** The constructor */
  public XCatalogReader(SAXParserFactory parserFactory) {
    super(parserFactory);
  }

  // ----------------------------------------------------------------------
  // Implement the SAX DocumentHandler interface

  /** The SAX <code>setDocumentLocator</code> method does nothing. */
  public void setDocumentLocator (Locator locator) {
    return;
  }

  /** The SAX <code>startDocument</code> method does nothing. */
  public void startDocument ()
    throws SAXException {
    return;
  }

  /** The SAX <code>endDocument</code> method does nothing. */
  public void endDocument ()
    throws SAXException {
    return;
  }

  /**
   * The SAX <code>startElement</code> method recognizes elements
   * from the plain catalog format and instantiates CatalogEntry
   * objects for them.
   *
   * @param namespaceURI The namespace name of the element.
   * @param localName The local name of the element.
   * @param qName The QName of the element.
   * @param atts The list of attributes on the element.
   *
   * @see CatalogEntry
   */
  public void startElement (String namespaceURI,
                            String localName,
                            String qName,
                            Attributes atts)
    throws SAXException {

    int entryType = -1;
    Vector entryArgs = new Vector();

    if (localName.equals("Base")) {
      entryType = catalog.BASE;
      entryArgs.add(atts.getValue("HRef"));

      catalog.getCatalogManager().debug.message(4, "Base", atts.getValue("HRef"));
    } else if (localName.equals("Delegate")) {
      entryType = catalog.DELEGATE_PUBLIC;
      entryArgs.add(atts.getValue("PublicId"));
      entryArgs.add(atts.getValue("HRef"));

      catalog.getCatalogManager().debug.message(4, "Delegate",
                    PublicId.normalize(atts.getValue("PublicId")),
                    atts.getValue("HRef"));
    } else if (localName.equals("Extend")) {
      entryType = catalog.CATALOG;
      entryArgs.add(atts.getValue("HRef"));

      catalog.getCatalogManager().debug.message(4, "Extend", atts.getValue("HRef"));
    } else if (localName.equals("Map")) {
      entryType = catalog.PUBLIC;
      entryArgs.add(atts.getValue("PublicId"));
      entryArgs.add(atts.getValue("HRef"));

      catalog.getCatalogManager().debug.message(4, "Map",
                    PublicId.normalize(atts.getValue("PublicId")),
                    atts.getValue("HRef"));
    } else if (localName.equals("Remap")) {
      entryType = catalog.SYSTEM;
      entryArgs.add(atts.getValue("SystemId"));
      entryArgs.add(atts.getValue("HRef"));

      catalog.getCatalogManager().debug.message(4, "Remap",
                    atts.getValue("SystemId"),
                    atts.getValue("HRef"));
    } else if (localName.equals("XMLCatalog")) {
      // nop, start of catalog
    } else {
      // This is equivalent to an invalid catalog entry type
      catalog.getCatalogManager().debug.message(1, "Invalid catalog entry type", localName);
    }

    if (entryType >= 0) {
      try {
        CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
        catalog.addEntry(ce);
      } catch (CatalogException cex) {
        if (cex.getExceptionType() == CatalogException.INVALID_ENTRY_TYPE) {
          catalog.getCatalogManager().debug.message(1, "Invalid catalog entry type", localName);
        } else if (cex.getExceptionType() == CatalogException.INVALID_ENTRY) {
          catalog.getCatalogManager().debug.message(1, "Invalid catalog entry", localName);
        }
      }
    }
    }

    /** The SAX <code>endElement</code> method does nothing. */
    public void endElement (String namespaceURI,
                            String localName,
                            String qName)
      throws SAXException {
      return;
    }

  /** The SAX <code>characters</code> method does nothing. */
  public void characters (char ch[], int start, int length)
    throws SAXException {
    return;
  }

  /** The SAX <code>ignorableWhitespace</code> method does nothing. */
  public void ignorableWhitespace (char ch[], int start, int length)
    throws SAXException {
    return;
  }

  /** The SAX <code>processingInstruction</code> method does nothing. */
  public void processingInstruction (String target, String data)
    throws SAXException {
    return;
  }
}
