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
 * Copyright 2001-2004 The Apache Software Foundation.
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
 * $Id: DOM2TO.java,v 1.5 2005/09/28 13:48:44 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.trax;

import java.io.IOException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import jaxp.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.ext.Locator2;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import jaxp.sun.org.apache.xml.internal.serializer.NamespaceMappings;

/**
 * @author Santiago Pericas-Geertsen
 * @author Sunitha Reddy
 */
public class DOM2TO implements XMLReader, Locator2 {

    private final static String EMPTYSTRING = "";
    private static final String XMLNS_PREFIX = "xmlns";

    /**
     * A reference to the DOM to be traversed.
     */
    private Node _dom;

    /**
     * A reference to the output handler receiving the events.
     */
    private SerializationHandler _handler;


    private String xmlVersion = null;

    private String xmlEncoding = null;


    public DOM2TO(Node root, SerializationHandler handler) {
        _dom = root;
        _handler = handler;
    }

    public ContentHandler getContentHandler() {
        return null;
    }

    public void setContentHandler(ContentHandler handler) {
        // Empty
    }

    public void parse(InputSource unused) throws IOException, SAXException {
        parse(_dom);
    }

    public void parse() throws IOException, SAXException {

        if (_dom != null) {
            boolean isIncomplete =
                (_dom.getNodeType() != org.w3c.dom.Node.DOCUMENT_NODE);

            if (isIncomplete) {
                _handler.startDocument();
                parse(_dom);
                _handler.endDocument();
            }
            else {
                parse(_dom);
            }
        }
    }

    /**
     * Traverse the DOM and generate TO events for a handler. Notice that
     * we need to handle implicit namespace declarations too.
     */
    private void parse(Node node)
        throws IOException, SAXException
    {
        if (node == null) return;

        switch (node.getNodeType()) {
        case Node.ATTRIBUTE_NODE:         // handled by ELEMENT_NODE
        case Node.DOCUMENT_TYPE_NODE :
        case Node.ENTITY_NODE :
        case Node.ENTITY_REFERENCE_NODE:
        case Node.NOTATION_NODE :
            // These node types are ignored!!!
            break;
        case Node.CDATA_SECTION_NODE:
            _handler.startCDATA();
            _handler.characters(node.getNodeValue());
            _handler.endCDATA();
            break;

        case Node.COMMENT_NODE:           // should be handled!!!
            _handler.comment(node.getNodeValue());
            break;

        case Node.DOCUMENT_NODE:
             setDocumentInfo((Document)node);
             _handler.setDocumentLocator(this);
             _handler.startDocument();
            Node next = node.getFirstChild();
            while (next != null) {
                parse(next);
                next = next.getNextSibling();
            }
            _handler.endDocument();
            break;

        case Node.DOCUMENT_FRAGMENT_NODE:
            next = node.getFirstChild();
            while (next != null) {
                parse(next);
                next = next.getNextSibling();
            }
            break;

        case Node.ELEMENT_NODE:
            // Generate SAX event to start element
            final String qname = node.getNodeName();
            _handler.startElement(null, null, qname);

            int colon;
            String prefix;
            final NamedNodeMap map = node.getAttributes();
            final int length = map.getLength();

            // Process all namespace attributes first
            for (int i = 0; i < length; i++) {
                final Node attr = map.item(i);
                final String qnameAttr = attr.getNodeName();

                // Is this a namespace declaration?
                if (qnameAttr.startsWith(XMLNS_PREFIX)) {
                    final String uriAttr = attr.getNodeValue();
                    colon = qnameAttr.lastIndexOf(':');
                    prefix = (colon > 0) ? qnameAttr.substring(colon + 1)
                                         : EMPTYSTRING;
                    _handler.namespaceAfterStartElement(prefix, uriAttr);
                }
            }

            // Process all non-namespace attributes next
            NamespaceMappings nm = new NamespaceMappings();
            for (int i = 0; i < length; i++) {
                final Node attr = map.item(i);
                final String qnameAttr = attr.getNodeName();

                // Is this a regular attribute?
                if (!qnameAttr.startsWith(XMLNS_PREFIX)) {
                    final String uriAttr = attr.getNamespaceURI();
                    // Uri may be implicitly declared
                    if (uriAttr != null && !uriAttr.equals(EMPTYSTRING) ) {
                        colon = qnameAttr.lastIndexOf(':');

                        // Fix for bug 26319
                        // For attributes not given an prefix explictly
                        // but having a namespace uri we need
                        // to explicitly generate the prefix
                        String newPrefix = nm.lookupPrefix(uriAttr);
                        if (newPrefix == null)
                            newPrefix = nm.generateNextPrefix();
                        prefix = (colon > 0) ? qnameAttr.substring(0, colon)
                            : newPrefix;
                        _handler.namespaceAfterStartElement(prefix, uriAttr);
                        _handler.addAttribute((prefix + ":" + qnameAttr),
                            attr.getNodeValue());
                    } else {
                         _handler.addAttribute(qnameAttr, attr.getNodeValue());
                    }
                }
            }

            // Now element namespace and children
            final String uri = node.getNamespaceURI();
            final String localName = node.getLocalName();

            // Uri may be implicitly declared
            if (uri != null) {
                colon = qname.lastIndexOf(':');
                prefix = (colon > 0) ? qname.substring(0, colon) : EMPTYSTRING;
                _handler.namespaceAfterStartElement(prefix, uri);
            }else {
                  // Fix for bug 26319
                  // If an element foo is created using
                  // createElementNS(null,locName)
                  // then the  element should be serialized
                  // <foo xmlns=" "/>
                  if (uri == null  && localName != null) {
                     prefix = EMPTYSTRING;
                     _handler.namespaceAfterStartElement(prefix, EMPTYSTRING);
                 }
            }

            // Traverse all child nodes of the element (if any)
            next = node.getFirstChild();
            while (next != null) {
                parse(next);
                next = next.getNextSibling();
            }

            // Generate SAX event to close element
            _handler.endElement(qname);
            break;

        case Node.PROCESSING_INSTRUCTION_NODE:
            _handler.processingInstruction(node.getNodeName(),
                                           node.getNodeValue());
            break;

        case Node.TEXT_NODE:
            _handler.characters(node.getNodeValue());
            break;
        }
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public DTDHandler getDTDHandler() {
        return null;
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public ErrorHandler getErrorHandler() {
        return null;
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public boolean getFeature(String name) throws SAXNotRecognizedException,
        SAXNotSupportedException
    {
        return false;
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public void setFeature(String name, boolean value) throws
        SAXNotRecognizedException, SAXNotSupportedException
    {
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public void parse(String sysId) throws IOException, SAXException {
        throw new IOException("This method is not yet implemented.");
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public void setDTDHandler(DTDHandler handler) throws NullPointerException {
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public void setEntityResolver(EntityResolver resolver) throws
        NullPointerException
    {
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public EntityResolver getEntityResolver() {
        return null;
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public void setErrorHandler(ErrorHandler handler) throws
        NullPointerException
    {
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public void setProperty(String name, Object value) throws
        SAXNotRecognizedException, SAXNotSupportedException {
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public Object getProperty(String name) throws SAXNotRecognizedException,
        SAXNotSupportedException
    {
        return null;
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public int getColumnNumber() {
        return 0;
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public int getLineNumber() {
        return 0;
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public String getPublicId() {
        return null;
    }

    /**
     * This class is only used internally so this method should never
     * be called.
     */
    public String getSystemId() {
        return null;
    }


    private void setDocumentInfo(Document document) {
        if (!document.getXmlStandalone())
            _handler.setStandalone(Boolean.toString(document.getXmlStandalone()));
        setXMLVersion(document.getXmlVersion());
        setEncoding(document.getXmlEncoding());
    }

    public String getXMLVersion() {
        return xmlVersion;
    }

    private void setXMLVersion(String version) {
        if (version != null) {
            xmlVersion = version;
            _handler.setVersion(xmlVersion);
        }
    }

    public String getEncoding() {
        return xmlEncoding;
    }

    private void setEncoding(String encoding) {
        if (encoding != null) {
            xmlEncoding = encoding;
            _handler.setEncoding(encoding);
        }
    }

    // Debugging
    private String getNodeTypeFromCode(short code) {
        String retval = null;
        switch (code) {
        case Node.ATTRIBUTE_NODE :
            retval = "ATTRIBUTE_NODE"; break;
        case Node.CDATA_SECTION_NODE :
            retval = "CDATA_SECTION_NODE"; break;
        case Node.COMMENT_NODE :
            retval = "COMMENT_NODE"; break;
        case Node.DOCUMENT_FRAGMENT_NODE :
            retval = "DOCUMENT_FRAGMENT_NODE"; break;
        case Node.DOCUMENT_NODE :
            retval = "DOCUMENT_NODE"; break;
        case Node.DOCUMENT_TYPE_NODE :
            retval = "DOCUMENT_TYPE_NODE"; break;
        case Node.ELEMENT_NODE :
            retval = "ELEMENT_NODE"; break;
        case Node.ENTITY_NODE :
            retval = "ENTITY_NODE"; break;
        case Node.ENTITY_REFERENCE_NODE :
            retval = "ENTITY_REFERENCE_NODE"; break;
        case Node.NOTATION_NODE :
            retval = "NOTATION_NODE"; break;
        case Node.PROCESSING_INSTRUCTION_NODE :
            retval = "PROCESSING_INSTRUCTION_NODE"; break;
        case Node.TEXT_NODE:
            retval = "TEXT_NODE"; break;
        }
        return retval;
    }
}
