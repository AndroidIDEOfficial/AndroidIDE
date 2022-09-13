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
 * $Id: DOM2SAX.java,v 1.2.4.1 2005/09/06 11:52:46 pvedula Exp $
 */


package jaxp.sun.org.apache.xalan.internal.xsltc.trax;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import jaxp.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import jaxp.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;

/**
 * @author G. Todd Miller
 */
public class DOM2SAX implements XMLReader, Locator {

    private final static String EMPTYSTRING = "";
    private static final String XMLNS_PREFIX = "xmlns";

    private Node _dom = null;
    private ContentHandler _sax = null;
    private LexicalHandler _lex = null;
    private SAXImpl _saxImpl = null;
    private Hashtable _nsPrefixes = new Hashtable();

    public DOM2SAX(Node root) {
        _dom = root;
    }

    public ContentHandler getContentHandler() {
        return _sax;
    }

    public void setContentHandler(ContentHandler handler) throws
        NullPointerException
    {
        _sax = handler;
        if (handler instanceof LexicalHandler) {
            _lex = (LexicalHandler) handler;
        }

        if (handler instanceof SAXImpl) {
            _saxImpl = (SAXImpl)handler;
        }
    }

    /**
     * Begin the scope of namespace prefix. Forward the event to the
     * SAX handler only if the prefix is unknown or it is mapped to a
     * different URI.
     */
    private boolean startPrefixMapping(String prefix, String uri)
        throws SAXException
    {
        boolean pushed = true;
        Stack uriStack = (Stack) _nsPrefixes.get(prefix);

        if (uriStack != null) {
            if (uriStack.isEmpty()) {
                _sax.startPrefixMapping(prefix, uri);
                uriStack.push(uri);
            }
            else {
                final String lastUri = (String) uriStack.peek();
                if (!lastUri.equals(uri)) {
                    _sax.startPrefixMapping(prefix, uri);
                    uriStack.push(uri);
                }
                else {
                    pushed = false;
                }
            }
        }
        else {
            _sax.startPrefixMapping(prefix, uri);
            _nsPrefixes.put(prefix, uriStack = new Stack());
            uriStack.push(uri);
        }
        return pushed;
    }

    /*
     * End the scope of a name prefix by popping it from the stack and
     * passing the event to the SAX Handler.
     */
    private void endPrefixMapping(String prefix)
        throws SAXException
    {
        final Stack uriStack = (Stack) _nsPrefixes.get(prefix);

        if (uriStack != null) {
            _sax.endPrefixMapping(prefix);
            uriStack.pop();
        }
    }

    /**
     * If the DOM was created using a DOM 1.0 API, the local name may be
     * null. If so, get the local name from the qualified name before
     * generating the SAX event.
     */
    private static String getLocalName(Node node) {
        final String localName = node.getLocalName();

        if (localName == null) {
            final String qname = node.getNodeName();
            final int col = qname.lastIndexOf(':');
            return (col > 0) ? qname.substring(col + 1) : qname;
        }
        return localName;
    }

    public void parse(InputSource unused) throws IOException, SAXException {
        parse(_dom);
    }

    public void parse() throws IOException, SAXException {
        if (_dom != null) {
            boolean isIncomplete =
                (_dom.getNodeType() != org.w3c.dom.Node.DOCUMENT_NODE);

            if (isIncomplete) {
                _sax.startDocument();
                parse(_dom);
                _sax.endDocument();
            }
            else {
                parse(_dom);
            }
        }
    }

    /**
     * Traverse the DOM and generate SAX events for a handler. A
     * startElement() event passes all attributes, including namespace
     * declarations.
     */
    private void parse(Node node) throws IOException, SAXException {
        Node first = null;
        if (node == null) return;

        switch (node.getNodeType()) {
        case Node.ATTRIBUTE_NODE:         // handled by ELEMENT_NODE
        case Node.DOCUMENT_FRAGMENT_NODE:
        case Node.DOCUMENT_TYPE_NODE :
        case Node.ENTITY_NODE :
        case Node.ENTITY_REFERENCE_NODE:
        case Node.NOTATION_NODE :
            // These node types are ignored!!!
            break;
        case Node.CDATA_SECTION_NODE:
            final String cdata = node.getNodeValue();
            if (_lex != null) {
                _lex.startCDATA();
                _sax.characters(cdata.toCharArray(), 0, cdata.length());
                _lex.endCDATA();
            }
            else {
                // in the case where there is no lex handler, we still
                // want the text of the cdate to make its way through.
                _sax.characters(cdata.toCharArray(), 0, cdata.length());
            }
            break;

        case Node.COMMENT_NODE:           // should be handled!!!
            if (_lex != null) {
                final String value = node.getNodeValue();
                _lex.comment(value.toCharArray(), 0, value.length());
            }
            break;
        case Node.DOCUMENT_NODE:
            _sax.setDocumentLocator(this);

            _sax.startDocument();
            Node next = node.getFirstChild();
            while (next != null) {
                parse(next);
                next = next.getNextSibling();
            }
            _sax.endDocument();
            break;

        case Node.ELEMENT_NODE:
            String prefix;
            Vector pushedPrefixes = new Vector();
            final AttributesImpl attrs = new AttributesImpl();
            final NamedNodeMap map = node.getAttributes();
            final int length = map.getLength();

            // Process all namespace declarations
            for (int i = 0; i < length; i++) {
                final Node attr = map.item(i);
                final String qnameAttr = attr.getNodeName();

                // Ignore everything but NS declarations here
                if (qnameAttr.startsWith(XMLNS_PREFIX)) {
                    final String uriAttr = attr.getNodeValue();
                    final int colon = qnameAttr.lastIndexOf(':');
                    prefix = (colon > 0) ? qnameAttr.substring(colon + 1) : EMPTYSTRING;
                    if (startPrefixMapping(prefix, uriAttr)) {
                        pushedPrefixes.addElement(prefix);
                    }
                }
            }

            // Process all other attributes
            for (int i = 0; i < length; i++) {
                final Node attr = map.item(i);
                String qnameAttr = attr.getNodeName();

                // Ignore NS declarations here
                if (!qnameAttr.startsWith(XMLNS_PREFIX)) {
                    final String uriAttr = attr.getNamespaceURI();
                    final String localNameAttr = getLocalName(attr);

                    // Uri may be implicitly declared
                    if (uriAttr != null) {
                        final int colon = qnameAttr.lastIndexOf(':');
                        if (colon > 0) {
                            prefix = qnameAttr.substring(0, colon);
                        }
                        else {
                            // If no prefix for this attr, we need to create
                            // one because we cannot use the default ns
                            prefix = BasisLibrary.generatePrefix();
                            qnameAttr = prefix + ':' + qnameAttr;
                        }
                        if (startPrefixMapping(prefix, uriAttr)) {
                            pushedPrefixes.addElement(prefix);
                        }
                    }

                    // Add attribute to list
                    attrs.addAttribute(attr.getNamespaceURI(), getLocalName(attr),
                        qnameAttr, "CDATA", attr.getNodeValue());
                }
            }

            // Now process the element itself
            final String qname = node.getNodeName();
            final String uri = node.getNamespaceURI();
            final String localName = getLocalName(node);

            // Uri may be implicitly declared
            if (uri != null) {
                final int colon = qname.lastIndexOf(':');
                prefix = (colon > 0) ? qname.substring(0, colon) : EMPTYSTRING;
                if (startPrefixMapping(prefix, uri)) {
                    pushedPrefixes.addElement(prefix);
                }
            }

            // Generate SAX event to start element
            if (_saxImpl != null) {
                _saxImpl.startElement(uri, localName, qname, attrs, node);
            }
            else {
                _sax.startElement(uri, localName, qname, attrs);
            }

            // Traverse all child nodes of the element (if any)
            next = node.getFirstChild();
            while (next != null) {
                parse(next);
                next = next.getNextSibling();
            }

            // Generate SAX event to close element
            _sax.endElement(uri, localName, qname);

            // Generate endPrefixMapping() for all pushed prefixes
            final int nPushedPrefixes = pushedPrefixes.size();
            for (int i = 0; i < nPushedPrefixes; i++) {
                endPrefixMapping((String) pushedPrefixes.elementAt(i));
            }
            break;

        case Node.PROCESSING_INSTRUCTION_NODE:
            _sax.processingInstruction(node.getNodeName(),
                                       node.getNodeValue());
            break;

        case Node.TEXT_NODE:
            final String data = node.getNodeValue();
            _sax.characters(data.toCharArray(), 0, data.length());
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
