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
// SAXParserHandler.java - An entity-resolving DefaultHandler

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

import java.io.IOException;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * An entity-resolving DefaultHandler.
 *
 * <p>This class provides a SAXParser DefaultHandler that performs
 * entity resolution.
 * </p>
 *
 * @author Norman Walsh
 * <a href="mailto:Norman.Walsh@Sun.COM">Norman.Walsh@Sun.COM</a>
 */
public class SAXParserHandler extends DefaultHandler {
  private EntityResolver er = null;
  private ContentHandler ch = null;

  public SAXParserHandler() {
    super();
  }

  public void setEntityResolver(EntityResolver er) {
    this.er = er;
  }

  public void setContentHandler(ContentHandler ch) {
    this.ch = ch;
  }

  // Entity Resolver
  public InputSource resolveEntity(String publicId, String systemId)
    throws SAXException {

    if (er != null) {
      try {
        return er.resolveEntity(publicId, systemId);
      } catch (IOException e) {
          System.out.println("resolveEntity threw IOException!");
          return null;
      }
    } else {
      return null;
    }
  }

  // Content Handler
  public void characters(char[] ch, int start, int length)
    throws SAXException {
    if (this.ch != null) {
      this.ch.characters(ch, start, length);
    }
  }

  public void endDocument()
    throws SAXException {
    if (ch != null) {
      ch.endDocument();
    }
  }

  public void endElement(String namespaceURI, String localName, String qName)
    throws SAXException {
    if (ch != null) {
      ch.endElement(namespaceURI, localName, qName);
    }
  }

  public void endPrefixMapping(String prefix)
    throws SAXException {
    if (ch != null) {
      ch.endPrefixMapping(prefix);
    }
  }

  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException {
    if (this.ch != null) {
      this.ch.ignorableWhitespace(ch, start, length);
    }
  }

  public void processingInstruction(String target, String data)
    throws SAXException {
    if (ch != null) {
      ch.processingInstruction(target, data);
    }
  }

  public void setDocumentLocator(Locator locator) {
    if (ch != null) {
      ch.setDocumentLocator(locator);
    }
  }

  public void skippedEntity(String name)
    throws SAXException {
    if (ch != null) {
      ch.skippedEntity(name);
    }
  }

  public void startDocument()
    throws SAXException {
    if (ch != null) {
      ch.startDocument();
    }
  }

  public void startElement(String namespaceURI, String localName,
                           String qName, Attributes atts)
    throws SAXException {
    if (ch != null) {
      ch.startElement(namespaceURI, localName, qName, atts);
    }
  }

  public void startPrefixMapping(String prefix, String uri)
    throws SAXException {
    if (ch != null) {
      ch.startPrefixMapping(prefix, uri);
    }
  }
}
