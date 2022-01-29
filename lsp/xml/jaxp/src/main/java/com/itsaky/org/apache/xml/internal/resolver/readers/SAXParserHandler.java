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

package com.itsaky.org.apache.xml.internal.resolver.readers;

import java.io.IOException;

import jaxp.sax.Locator;
import jaxp.sax.helpers.DefaultHandler;

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
  private jaxp.sax.EntityResolver er = null;
  private jaxp.sax.ContentHandler ch = null;

  public SAXParserHandler() {
    super();
  }

  public void setEntityResolver(jaxp.sax.EntityResolver er) {
    this.er = er;
  }

  public void setContentHandler(jaxp.sax.ContentHandler ch) {
    this.ch = ch;
  }

  // Entity Resolver
  public jaxp.sax.InputSource resolveEntity(String publicId, String systemId)
    throws jaxp.sax.SAXException {

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
    throws jaxp.sax.SAXException {
    if (this.ch != null) {
      this.ch.characters(ch, start, length);
    }
  }

  public void endDocument()
    throws jaxp.sax.SAXException {
    if (ch != null) {
      ch.endDocument();
    }
  }

  public void endElement(String namespaceURI, String localName, String qName)
    throws jaxp.sax.SAXException {
    if (ch != null) {
      ch.endElement(namespaceURI, localName, qName);
    }
  }

  public void endPrefixMapping(String prefix)
    throws jaxp.sax.SAXException {
    if (ch != null) {
      ch.endPrefixMapping(prefix);
    }
  }

  public void ignorableWhitespace(char[] ch, int start, int length)
    throws jaxp.sax.SAXException {
    if (this.ch != null) {
      this.ch.ignorableWhitespace(ch, start, length);
    }
  }

  public void processingInstruction(String target, String data)
    throws jaxp.sax.SAXException {
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
    throws jaxp.sax.SAXException {
    if (ch != null) {
      ch.skippedEntity(name);
    }
  }

  public void startDocument()
    throws jaxp.sax.SAXException {
    if (ch != null) {
      ch.startDocument();
    }
  }

  public void startElement(String namespaceURI, String localName,
                           String qName, jaxp.sax.Attributes atts)
    throws jaxp.sax.SAXException {
    if (ch != null) {
      ch.startElement(namespaceURI, localName, qName, atts);
    }
  }

  public void startPrefixMapping(String prefix, String uri)
    throws jaxp.sax.SAXException {
    if (ch != null) {
      ch.startPrefixMapping(prefix, uri);
    }
  }
}
