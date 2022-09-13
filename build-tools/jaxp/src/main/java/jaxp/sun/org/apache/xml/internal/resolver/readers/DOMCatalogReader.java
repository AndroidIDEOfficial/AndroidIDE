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
// DOMCatalogReader.java - Read XML Catalog files

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

import jaxp.sun.org.apache.xml.internal.resolver.Catalog;
import jaxp.sun.org.apache.xml.internal.resolver.CatalogException;
import jaxp.sun.org.apache.xml.internal.resolver.helpers.Namespaces;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import jaxp.xml.parsers.DocumentBuilder;
import jaxp.xml.parsers.DocumentBuilderFactory;
import jaxp.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * A DOM-based CatalogReader.
 *
 * <p>This class is used to read XML Catalogs using the DOM. This reader
 * has an advantage over the SAX-based reader that it can analyze the
 * DOM tree rather than simply a series of SAX events. It has the disadvantage
 * that it requires all of the code necessary to build and walk a DOM
 * tree.</p>
 *
 * <p>Since the choice of CatalogReaders (in the InputStream case) can only
 * be made on the basis of MIME type, the following problem occurs: only
 * one CatalogReader can exist for all XML mime types. In order to get
 * around this problem, the DOMCatalogReader relies on a set of external
 * CatalogParsers to actually build the catalog.</p>
 *
 * <p>The selection of CatalogParsers is made on the basis of the QName
 * of the root element of the document.</p>
 *
 * <p>This class requires the <a href="http://java.sun.com/aboutJava/communityprocess/final/jsr005/index.html">Java API for XML Parsing</a>.</p>
 *
 * @see Catalog
 * @see CatalogReader
 * @see SAXCatalogReader
 * @see TextCatalogReader
 * @see DOMCatalogParser
 *
 * @author Norman Walsh
 * <a href="mailto:Norman.Walsh@Sun.COM">Norman.Walsh@Sun.COM</a>
 *
 */
public class DOMCatalogReader implements CatalogReader {
  /**
   * Mapping table from QNames to CatalogParser classes.
   *
   * <p>Each key in this hash table has the form "elementname"
   * or "{namespaceuri}elementname". The former is used if the
   * namespace URI is null.</p>
   */
  protected Hashtable namespaceMap = new Hashtable();

  /**
   * Add a new parser to the reader.
   *
   * <p>This method associates the specified parserClass with the
   * namespaceURI/rootElement names specified.</p>
   *
   * @param namespaceURI The namespace URI. <em>Not</em> the prefix.
   * @param rootElement The name of the root element.
   * @param parserClass The name of the parserClass to instantiate
   * for this kind of catalog.
   */
  public void setCatalogParser(String namespaceURI,
                               String rootElement,
                               String parserClass) {
    if (namespaceURI == null) {
      namespaceMap.put(rootElement, parserClass);
    } else {
      namespaceMap.put("{"+namespaceURI+"}"+rootElement, parserClass);
    }
  }

  /**
   * Get the name of the parser class for a given catalog type.
   *
   * <p>This method returns the parserClass associated with the
   * namespaceURI/rootElement names specified.</p>
   *
   * @param namespaceURI The namespace URI. <em>Not</em> the prefix.
   * @param rootElement The name of the root element.
   * @return The parser class.
   */
  public String getCatalogParser(String namespaceURI,
                                 String rootElement) {
    if (namespaceURI == null) {
      return (String) namespaceMap.get(rootElement);
    } else {
      return (String) namespaceMap.get("{"+namespaceURI+"}"+rootElement);
    }
  }

  /**
   * Null constructor; something for subclasses to call.
   */
  public DOMCatalogReader() { }

  public void readCatalog(Catalog catalog, InputStream is)
    throws IOException, CatalogException {

    DocumentBuilderFactory factory = null;
    DocumentBuilder builder = null;

    factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(false);
    factory.setValidating(false);
    try {
      builder = factory.newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      throw new CatalogException(CatalogException.UNPARSEABLE);
    }

    Document doc = null;

    try {
      doc = builder.parse(is);
    } catch (SAXException se) {
      throw new CatalogException(CatalogException.UNKNOWN_FORMAT);
    }

    Element root = doc.getDocumentElement();

    String namespaceURI = Namespaces.getNamespaceURI(root);
    String localName    = Namespaces.getLocalName(root);

    String domParserClass = getCatalogParser(namespaceURI,
                                             localName);

    if (domParserClass == null) {
      if (namespaceURI == null) {
        catalog.getCatalogManager().debug.message(1, "No Catalog parser for "
                                                  + localName);
      } else {
        catalog.getCatalogManager().debug.message(1, "No Catalog parser for "
                                                  + "{" + namespaceURI + "}"
                                                  + localName);
      }
      return;
    }

    DOMCatalogParser domParser = null;

    try {
      domParser = (DOMCatalogParser) Class.forName(domParserClass).newInstance();
    } catch (ClassNotFoundException cnfe) {
      catalog.getCatalogManager().debug.message(1, "Cannot load XML Catalog Parser class", domParserClass);
      throw new CatalogException(CatalogException.UNPARSEABLE);
    } catch (InstantiationException ie) {
      catalog.getCatalogManager().debug.message(1, "Cannot instantiate XML Catalog Parser class", domParserClass);
      throw new CatalogException(CatalogException.UNPARSEABLE);
    } catch (IllegalAccessException iae) {
      catalog.getCatalogManager().debug.message(1, "Cannot access XML Catalog Parser class", domParserClass);
      throw new CatalogException(CatalogException.UNPARSEABLE);
    } catch (ClassCastException cce ) {
      catalog.getCatalogManager().debug.message(1, "Cannot cast XML Catalog Parser class", domParserClass);
      throw new CatalogException(CatalogException.UNPARSEABLE);
    }

    Node node = root.getFirstChild();
    while (node != null) {
      domParser.parseCatalogEntry(catalog, node);
      node = node.getNextSibling();
    }
  }

  public void readCatalog(Catalog catalog, String fileUrl)
    throws MalformedURLException, IOException, CatalogException {
    URL url = new URL(fileUrl);
    URLConnection urlCon = url.openConnection();
    readCatalog(catalog, urlCon.getInputStream());
  }
}
