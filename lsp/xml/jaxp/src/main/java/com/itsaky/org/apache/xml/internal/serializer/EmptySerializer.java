/*
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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
 * $Id: EmptySerializer.java,v 1.2.4.1 2005/09/15 08:15:16 suresh_emailid Exp $
 */
package com.itsaky.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import jaxp.xml.transform.SourceLocator;
import jaxp.xml.transform.Transformer;

import jaxp.w3c.dom.Node;
import jaxp.sax.Attributes;
import jaxp.sax.ContentHandler;
import jaxp.sax.Locator;
import jaxp.sax.SAXException;
import jaxp.sax.SAXParseException;
import jaxp.sax.DTDHandler;
import jaxp.sax.ErrorHandler;
import jaxp.sax.ext.DeclHandler;
import jaxp.sax.ext.LexicalHandler;

/**
 * This class is an adapter class. Its only purpose is to be extended and
 * for that extended class to over-ride all methods that are to be used.
 *
 * This class is not a public API, it is only public because it is used
 * across package boundaries.
 *
 * @xsl.usage internal
 */
public class EmptySerializer implements SerializationHandler
{
    protected static final String ERR = "EmptySerializer method not over-ridden";
    /**
     * @see SerializationHandler#asContentHandler()
     */

    protected void couldThrowIOException() throws IOException
    {
        return; // don't do anything.
    }

    protected void couldThrowSAXException() throws SAXException
    {
        return; // don't do anything.
    }

    protected void couldThrowSAXException(char[] chars, int off, int len) throws SAXException
    {
        return; // don't do anything.
    }

    protected void couldThrowSAXException(String elemQName) throws SAXException
    {
        return; // don't do anything.
    }

    protected void couldThrowException() throws Exception
    {
        return; // don't do anything.
    }

    void aMethodIsCalled()
    {

        // throw new RuntimeException(err);
        return;
    }


    /**
     * @see SerializationHandler#asContentHandler()
     */
    public ContentHandler asContentHandler() throws IOException
    {
        couldThrowIOException();
        return null;
    }
    /**
     * @see SerializationHandler#setContentHandler(ContentHandler)
     */
    public void setContentHandler(ContentHandler ch)
    {
        aMethodIsCalled();
    }
    /**
     * @see SerializationHandler#close()
     */
    public void close()
    {
        aMethodIsCalled();
    }
    /**
     * @see SerializationHandler#getOutputFormat()
     */
    public Properties getOutputFormat()
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see SerializationHandler#getOutputStream()
     */
    public OutputStream getOutputStream()
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see SerializationHandler#getWriter()
     */
    public Writer getWriter()
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see SerializationHandler#reset()
     */
    public boolean reset()
    {
        aMethodIsCalled();
        return false;
    }
    /**
     * @see SerializationHandler#serialize(Node)
     */
    public void serialize(Node node) throws IOException
    {
        couldThrowIOException();
    }
    /**
     * @see SerializationHandler#setCdataSectionElements(java.util.Vector)
     */
    public void setCdataSectionElements(Vector URI_and_localNames)
    {
        aMethodIsCalled();
    }
    /**
     * @see SerializationHandler#setEscaping(boolean)
     */
    public boolean setEscaping(boolean escape) throws SAXException
    {
        couldThrowSAXException();
        return false;
    }
    /**
     * @see SerializationHandler#setIndent(boolean)
     */
    public void setIndent(boolean indent)
    {
        aMethodIsCalled();
    }
    /**
     * @see SerializationHandler#setIndentAmount(int)
     */
    public void setIndentAmount(int spaces)
    {
        aMethodIsCalled();
    }
    /**
     * @see SerializationHandler#setIsStandalone(boolean)
     */
    public void setIsStandalone(boolean isStandalone)
    {
        aMethodIsCalled();
    }
    /**
     * @see SerializationHandler#setOutputFormat(java.util.Properties)
     */
    public void setOutputFormat(Properties format)
    {
        aMethodIsCalled();
    }
    /**
     * @see SerializationHandler#setOutputStream(java.io.OutputStream)
     */
    public void setOutputStream(OutputStream output)
    {
        aMethodIsCalled();
    }
    /**
     * @see SerializationHandler#setVersion(java.lang.String)
     */
    public void setVersion(String version)
    {
        aMethodIsCalled();
    }
    /**
     * @see SerializationHandler#setWriter(java.io.Writer)
     */
    public void setWriter(Writer writer)
    {
        aMethodIsCalled();
    }
    /**
     * @see SerializationHandler#setTransformer(jaxp.xml.transform.Transformer)
     */
    public void setTransformer(Transformer transformer)
    {
        aMethodIsCalled();
    }
    /**
     * @see SerializationHandler#getTransformer()
     */
    public Transformer getTransformer()
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see SerializationHandler#flushPending()
     */
    public void flushPending() throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ExtendedContentHandler#addAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void addAttribute(
        String uri,
        String localName,
        String rawName,
        String type,
        String value,
        boolean XSLAttribute)
        throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ExtendedContentHandler#addAttributes(Attributes)
     */
    public void addAttributes(Attributes atts) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ExtendedContentHandler#addAttribute(java.lang.String, java.lang.String)
     */
    public void addAttribute(String name, String value)
    {
        aMethodIsCalled();
    }

    /**
     * @see ExtendedContentHandler#characters(java.lang.String)
     */
    public void characters(String chars) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ExtendedContentHandler#endElement(java.lang.String)
     */
    public void endElement(String elemName) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ExtendedContentHandler#startDocument()
     */
    public void startDocument() throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ExtendedContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void startElement(String uri, String localName, String qName)
        throws SAXException
    {
        couldThrowSAXException(qName);
    }
    /**
     * @see ExtendedContentHandler#startElement(java.lang.String)
     */
    public void startElement(String qName) throws SAXException
    {
        couldThrowSAXException(qName);
    }
    /**
     * @see ExtendedContentHandler#namespaceAfterStartElement(java.lang.String, java.lang.String)
     */
    public void namespaceAfterStartElement(String uri, String prefix)
        throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ExtendedContentHandler#startPrefixMapping(java.lang.String, java.lang.String, boolean)
     */
    public boolean startPrefixMapping(
        String prefix,
        String uri,
        boolean shouldFlush)
        throws SAXException
    {
        couldThrowSAXException();
        return false;
    }
    /**
     * @see ExtendedContentHandler#entityReference(java.lang.String)
     */
    public void entityReference(String entityName) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ExtendedContentHandler#getNamespaceMappings()
     */
    public NamespaceMappings getNamespaceMappings()
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see ExtendedContentHandler#getPrefix(java.lang.String)
     */
    public String getPrefix(String uri)
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see ExtendedContentHandler#getNamespaceURI(java.lang.String, boolean)
     */
    public String getNamespaceURI(String name, boolean isElement)
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see ExtendedContentHandler#getNamespaceURIFromPrefix(java.lang.String)
     */
    public String getNamespaceURIFromPrefix(String prefix)
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see ContentHandler#setDocumentLocator(Locator)
     */
    public void setDocumentLocator(Locator arg0)
    {
        aMethodIsCalled();
    }
    /**
     * @see ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
     */
    public void startPrefixMapping(String arg0, String arg1)
        throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String arg0) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, Attributes)
     */
    public void startElement(
        String arg0,
        String arg1,
        String arg2,
        Attributes arg3)
        throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String arg0, String arg1, String arg2)
        throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] arg0, int arg1, int arg2) throws SAXException
    {
        couldThrowSAXException(arg0, arg1, arg2);
    }
    /**
     * @see ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
        throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ContentHandler#processingInstruction(java.lang.String, java.lang.String)
     */
    public void processingInstruction(String arg0, String arg1)
        throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ContentHandler#skippedEntity(java.lang.String)
     */
    public void skippedEntity(String arg0) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ExtendedLexicalHandler#comment(java.lang.String)
     */
    public void comment(String comment) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see LexicalHandler#startDTD(java.lang.String, java.lang.String, java.lang.String)
     */
    public void startDTD(String arg0, String arg1, String arg2)
        throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see LexicalHandler#endDTD()
     */
    public void endDTD() throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see LexicalHandler#startEntity(java.lang.String)
     */
    public void startEntity(String arg0) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see LexicalHandler#endEntity(java.lang.String)
     */
    public void endEntity(String arg0) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see LexicalHandler#startCDATA()
     */
    public void startCDATA() throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see LexicalHandler#endCDATA()
     */
    public void endCDATA() throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see LexicalHandler#comment(char[], int, int)
     */
    public void comment(char[] arg0, int arg1, int arg2) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see XSLOutputAttributes#getDoctypePublic()
     */
    public String getDoctypePublic()
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see XSLOutputAttributes#getDoctypeSystem()
     */
    public String getDoctypeSystem()
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see XSLOutputAttributes#getEncoding()
     */
    public String getEncoding()
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see XSLOutputAttributes#getIndent()
     */
    public boolean getIndent()
    {
        aMethodIsCalled();
        return false;
    }
    /**
     * @see XSLOutputAttributes#getIndentAmount()
     */
    public int getIndentAmount()
    {
        aMethodIsCalled();
        return 0;
    }
    /**
     * @see XSLOutputAttributes#getMediaType()
     */
    public String getMediaType()
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see XSLOutputAttributes#getOmitXMLDeclaration()
     */
    public boolean getOmitXMLDeclaration()
    {
        aMethodIsCalled();
        return false;
    }
    /**
     * @see XSLOutputAttributes#getStandalone()
     */
    public String getStandalone()
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see XSLOutputAttributes#getVersion()
     */
    public String getVersion()
    {
        aMethodIsCalled();
        return null;
    }
    /**
     * @see XSLOutputAttributes#setCdataSectionElements
     */
    public void setCdataSectionElements(Hashtable h) throws Exception
    {
        couldThrowException();
    }
    /**
     * @see XSLOutputAttributes#setDoctype(java.lang.String, java.lang.String)
     */
    public void setDoctype(String system, String pub)
    {
        aMethodIsCalled();
    }
    /**
     * @see XSLOutputAttributes#setDoctypePublic(java.lang.String)
     */
    public void setDoctypePublic(String doctype)
    {
        aMethodIsCalled();
    }
    /**
     * @see XSLOutputAttributes#setDoctypeSystem(java.lang.String)
     */
    public void setDoctypeSystem(String doctype)
    {
        aMethodIsCalled();
    }
    /**
     * @see XSLOutputAttributes#setEncoding(java.lang.String)
     */
    public void setEncoding(String encoding)
    {
        aMethodIsCalled();
    }
    /**
     * @see XSLOutputAttributes#setMediaType(java.lang.String)
     */
    public void setMediaType(String mediatype)
    {
        aMethodIsCalled();
    }
    /**
     * @see XSLOutputAttributes#setOmitXMLDeclaration(boolean)
     */
    public void setOmitXMLDeclaration(boolean b)
    {
        aMethodIsCalled();
    }
    /**
     * @see XSLOutputAttributes#setStandalone(java.lang.String)
     */
    public void setStandalone(String standalone)
    {
        aMethodIsCalled();
    }
    /**
     * @see DeclHandler#elementDecl(java.lang.String, java.lang.String)
     */
    public void elementDecl(String arg0, String arg1) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see DeclHandler#attributeDecl(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void attributeDecl(
        String arg0,
        String arg1,
        String arg2,
        String arg3,
        String arg4)
        throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see DeclHandler#internalEntityDecl(java.lang.String, java.lang.String)
     */
    public void internalEntityDecl(String arg0, String arg1)
        throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see DeclHandler#externalEntityDecl(java.lang.String, java.lang.String, java.lang.String)
     */
    public void externalEntityDecl(String arg0, String arg1, String arg2)
        throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ErrorHandler#warning(SAXParseException)
     */
    public void warning(SAXParseException arg0) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ErrorHandler#error(SAXParseException)
     */
    public void error(SAXParseException arg0) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see ErrorHandler#fatalError(SAXParseException)
     */
    public void fatalError(SAXParseException arg0) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see Serializer#asDOMSerializer()
     */
    public DOMSerializer asDOMSerializer() throws IOException
    {
        couldThrowIOException();
        return null;
    }

    /**
     * @see SerializationHandler#setNamespaceMappings(NamespaceMappings)
     */
    public void setNamespaceMappings(NamespaceMappings mappings) {
        aMethodIsCalled();
    }

    /**
     * @see ExtendedContentHandler#setSourceLocator(jaxp.xml.transform.SourceLocator)
     */
    public void setSourceLocator(SourceLocator locator)
    {
        aMethodIsCalled();
    }

    /**
     * @see ExtendedContentHandler#addUniqueAttribute(java.lang.String, java.lang.String, int)
     */
    public void addUniqueAttribute(String name, String value, int flags)
        throws SAXException
    {
        couldThrowSAXException();
    }

    /**
     * @see ExtendedContentHandler#characters(Node)
     */
    public void characters(Node node) throws SAXException
    {
        couldThrowSAXException();
    }

    /**
     * @see ExtendedContentHandler#addXSLAttribute(java.lang.String, java.lang.String, java.lang.String)
     */
    public void addXSLAttribute(String qName, String value, String uri)
    {
        aMethodIsCalled();
    }

    /**
     * @see ExtendedContentHandler#addAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void addAttribute(String uri, String localName, String rawName, String type, String value) throws SAXException
    {
        couldThrowSAXException();
    }
    /**
     * @see DTDHandler#notationDecl(java.lang.String, java.lang.String, java.lang.String)
     */
    public void notationDecl(String arg0, String arg1, String arg2) throws SAXException
    {
        couldThrowSAXException();
    }

    /**
     * @see DTDHandler#unparsedEntityDecl(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void unparsedEntityDecl(
        String arg0,
        String arg1,
        String arg2,
        String arg3)
        throws SAXException {
        couldThrowSAXException();
    }

    /**
     * @see SerializationHandler#setDTDEntityExpansion(boolean)
     */
    public void setDTDEntityExpansion(boolean expand) {
        aMethodIsCalled();

    }
}
