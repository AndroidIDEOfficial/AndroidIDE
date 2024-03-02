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
// ResolvingXMLReader.java - An XMLReader that performs catalog resolution

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

package jaxp.sun.org.apache.xml.internal.resolver.tools;

import jaxp.sun.org.apache.xml.internal.resolver.CatalogManager;

import javax.xml.parsers.*;

import jaxp.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
import jaxp.sun.org.apache.xml.internal.resolver.*;

import jaxp.xml.parsers.SAXParser;
import jaxp.xml.parsers.SAXParserFactory;

/**
 * A SAX XMLReader that performs catalog-based entity resolution.
 *
 * <p>This class implements a SAX XMLReader that performs entity resolution
 * using the CatalogResolver. The actual, underlying parser is obtained
 * from a SAXParserFactory.</p>
 * </p>
 *
 * @see CatalogResolver
 * @see org.xml.sax.XMLReader
 *
 * @author Norman Walsh
 * <a href="mailto:Norman.Walsh@Sun.COM">Norman.Walsh@Sun.COM</a>
 *
 * @version 1.0
 */
public class ResolvingXMLReader extends ResolvingXMLFilter {
  /** Make the parser Namespace aware? */
  public static boolean namespaceAware = true;

  /** Make the parser validating? */
  public static boolean validating = false;

  /**
   * Construct a new reader from the JAXP factory.
   *
   * <p>In order to do its job, a ResolvingXMLReader must in fact be
   * a filter. So the only difference between this code and the filter
   * code is that the constructor builds a new reader.</p>
   */
  public ResolvingXMLReader() {
    super();
    SAXParserFactory spf = catalogManager.useServicesMechanism() ?
                    SAXParserFactory.newInstance() : new SAXParserFactoryImpl();
    spf.setNamespaceAware(namespaceAware);
    spf.setValidating(validating);
    try {
      SAXParser parser = spf.newSAXParser();
      setParent(parser.getXMLReader());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Construct a new reader from the JAXP factory.
   *
   * <p>In order to do its job, a ResolvingXMLReader must in fact be
   * a filter. So the only difference between this code and the filter
   * code is that the constructor builds a new reader.</p>
   */
  public ResolvingXMLReader(CatalogManager manager) {
    super(manager);
    SAXParserFactory spf = catalogManager.useServicesMechanism() ?
                    SAXParserFactory.newInstance() : new SAXParserFactoryImpl();
    spf.setNamespaceAware(namespaceAware);
    spf.setValidating(validating);
    try {
      SAXParser parser = spf.newSAXParser();
      setParent(parser.getXMLReader());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
