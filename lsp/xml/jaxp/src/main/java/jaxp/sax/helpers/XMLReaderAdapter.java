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

// XMLReaderAdapter.java - adapt an SAX2 XMLReader to a SAX1 Parser
// http://www.saxproject.org
// Written by David Megginson
// NO WARRANTY!  This class is in the public domain.
// $Id: XMLReaderAdapter.java,v 1.3 2004/11/03 22:53:09 jsuttor Exp $

package jaxp.sax.helpers;

import java.io.IOException;
import java.util.Locale;

import jaxp.sax.Parser;      // deprecated
import jaxp.sax.Locator;
import jaxp.sax.InputSource;
import jaxp.sax.AttributeList; // deprecated
import jaxp.sax.EntityResolver;
import jaxp.sax.DTDHandler;
import jaxp.sax.DocumentHandler; // deprecated
import jaxp.sax.ErrorHandler;
import jaxp.sax.SAXException;

import jaxp.sax.XMLReader;
import jaxp.sax.Attributes;
import jaxp.sax.ContentHandler;
import jaxp.sax.SAXNotSupportedException;


/**
 * Adapt a SAX2 XMLReader as a SAX1 Parser.
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * See <a href='http://www.saxproject.org'>http://www.saxproject.org</a>
 * for further information.
 * </blockquote>
 *
 * <p>This class wraps a SAX2 {@link XMLReader XMLReader}
 * and makes it act as a SAX1 {@link Parser Parser}.  The XMLReader
 * must support a true value for the
 * http://xml.org/sax/features/namespace-prefixes property or parsing will fail
 * with a {@link SAXException SAXException}; if the XMLReader
 * supports a false value for the http://xml.org/sax/features/namespaces
 * property, that will also be used to improve efficiency.</p>
 *
 * @since SAX 2.0
 * @author David Megginson
 * @see Parser
 * @see XMLReader
 */
public class XMLReaderAdapter implements Parser, ContentHandler
{


    ////////////////////////////////////////////////////////////////////
    // Constructor.
    ////////////////////////////////////////////////////////////////////


    /**
     * Create a new adapter.
     *
     * <p>Use the "org.xml.sax.driver" property to locate the SAX2
     * driver to embed.</p>
     *
     * @exception SAXException If the embedded driver
     *            cannot be instantiated or if the
     *            org.xml.sax.driver property is not specified.
     */
    public XMLReaderAdapter ()
      throws SAXException
    {
        setup(XMLReaderFactory.createXMLReader());
    }


    /**
     * Create a new adapter.
     *
     * <p>Create a new adapter, wrapped around a SAX2 XMLReader.
     * The adapter will make the XMLReader act like a SAX1
     * Parser.</p>
     *
     * @param xmlReader The SAX2 XMLReader to wrap.
     * @exception java.lang.NullPointerException If the argument is null.
     */
    public XMLReaderAdapter (XMLReader xmlReader)
    {
        setup(xmlReader);
    }



    /**
     * Internal setup.
     *
     * @param xmlReader The embedded XMLReader.
     */
    private void setup (XMLReader xmlReader)
    {
        if (xmlReader == null) {
            throw new NullPointerException("XMLReader must not be null");
        }
        this.xmlReader = xmlReader;
        qAtts = new AttributesAdapter();
    }



    ////////////////////////////////////////////////////////////////////
    // Implementation of org.xml.sax.Parser.
    ////////////////////////////////////////////////////////////////////


    /**
     * Set the locale for error reporting.
     *
     * <p>This is not supported in SAX2, and will always throw
     * an exception.</p>
     *
     * @param locale the locale for error reporting.
     * @see Parser#setLocale
     * @exception SAXException Thrown unless overridden.
     */
    public void setLocale (Locale locale)
        throws SAXException
    {
        throw new SAXNotSupportedException("setLocale not supported");
    }


    /**
     * Register the entity resolver.
     *
     * @param resolver The new resolver.
     * @see Parser#setEntityResolver
     */
    public void setEntityResolver (EntityResolver resolver)
    {
        xmlReader.setEntityResolver(resolver);
    }


    /**
     * Register the DTD event handler.
     *
     * @param handler The new DTD event handler.
     * @see Parser#setDTDHandler
     */
    public void setDTDHandler (DTDHandler handler)
    {
        xmlReader.setDTDHandler(handler);
    }


    /**
     * Register the SAX1 document event handler.
     *
     * <p>Note that the SAX1 document handler has no Namespace
     * support.</p>
     *
     * @param handler The new SAX1 document event handler.
     * @see Parser#setDocumentHandler
     */
    public void setDocumentHandler (DocumentHandler handler)
    {
        documentHandler = handler;
    }


    /**
     * Register the error event handler.
     *
     * @param handler The new error event handler.
     * @see Parser#setErrorHandler
     */
    public void setErrorHandler (ErrorHandler handler)
    {
        xmlReader.setErrorHandler(handler);
    }


    /**
     * Parse the document.
     *
     * <p>This method will throw an exception if the embedded
     * XMLReader does not support the
     * http://xml.org/sax/features/namespace-prefixes property.</p>
     *
     * @param systemId The absolute URL of the document.
     * @exception java.io.IOException If there is a problem reading
     *            the raw content of the document.
     * @exception SAXException If there is a problem
     *            processing the document.
     * @see #parse(InputSource)
     * @see Parser#parse(java.lang.String)
     */
    public void parse (String systemId)
        throws IOException, SAXException
    {
        parse(new InputSource(systemId));
    }


    /**
     * Parse the document.
     *
     * <p>This method will throw an exception if the embedded
     * XMLReader does not support the
     * http://xml.org/sax/features/namespace-prefixes property.</p>
     *
     * @param input An input source for the document.
     * @exception java.io.IOException If there is a problem reading
     *            the raw content of the document.
     * @exception SAXException If there is a problem
     *            processing the document.
     * @see #parse(java.lang.String)
     * @see Parser#parse(InputSource)
     */
    public void parse (InputSource input)
        throws IOException, SAXException
    {
        setupXMLReader();
        xmlReader.parse(input);
    }


    /**
     * Set up the XML reader.
     */
    private void setupXMLReader ()
        throws SAXException
    {
        xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        try {
            xmlReader.setFeature("http://xml.org/sax/features/namespaces",
                                 false);
        } catch (SAXException e) {
            // NO OP: it's just extra information, and we can ignore it
        }
        xmlReader.setContentHandler(this);
    }



    ////////////////////////////////////////////////////////////////////
    // Implementation of org.xml.sax.ContentHandler.
    ////////////////////////////////////////////////////////////////////


    /**
     * Set a document locator.
     *
     * @param locator The document locator.
     * @see ContentHandler#setDocumentLocator
     */
    public void setDocumentLocator (Locator locator)
    {
        if (documentHandler != null)
            documentHandler.setDocumentLocator(locator);
    }


    /**
     * Start document event.
     *
     * @exception SAXException The client may raise a
     *            processing exception.
     * @see ContentHandler#startDocument
     */
    public void startDocument ()
        throws SAXException
    {
        if (documentHandler != null)
            documentHandler.startDocument();
    }


    /**
     * End document event.
     *
     * @exception SAXException The client may raise a
     *            processing exception.
     * @see ContentHandler#endDocument
     */
    public void endDocument ()
        throws SAXException
    {
        if (documentHandler != null)
            documentHandler.endDocument();
    }


    /**
     * Adapt a SAX2 start prefix mapping event.
     *
     * @param prefix The prefix being mapped.
     * @param uri The Namespace URI being mapped to.
     * @see ContentHandler#startPrefixMapping
     */
    public void startPrefixMapping (String prefix, String uri)
    {
    }


    /**
     * Adapt a SAX2 end prefix mapping event.
     *
     * @param prefix The prefix being mapped.
     * @see ContentHandler#endPrefixMapping
     */
    public void endPrefixMapping (String prefix)
    {
    }


    /**
     * Adapt a SAX2 start element event.
     *
     * @param uri The Namespace URI.
     * @param localName The Namespace local name.
     * @param qName The qualified (prefixed) name.
     * @param atts The SAX2 attributes.
     * @exception SAXException The client may raise a
     *            processing exception.
     * @see ContentHandler#endDocument
     */
    public void startElement (String uri, String localName,
                              String qName, Attributes atts)
        throws SAXException
    {
        if (documentHandler != null) {
            qAtts.setAttributes(atts);
            documentHandler.startElement(qName, qAtts);
        }
    }


    /**
     * Adapt a SAX2 end element event.
     *
     * @param uri The Namespace URI.
     * @param localName The Namespace local name.
     * @param qName The qualified (prefixed) name.
     * @exception SAXException The client may raise a
     *            processing exception.
     * @see ContentHandler#endElement
     */
    public void endElement (String uri, String localName,
                            String qName)
        throws SAXException
    {
        if (documentHandler != null)
            documentHandler.endElement(qName);
    }


    /**
     * Adapt a SAX2 characters event.
     *
     * @param ch An array of characters.
     * @param start The starting position in the array.
     * @param length The number of characters to use.
     * @exception SAXException The client may raise a
     *            processing exception.
     * @see ContentHandler#characters
     */
    public void characters (char ch[], int start, int length)
        throws SAXException
    {
        if (documentHandler != null)
            documentHandler.characters(ch, start, length);
    }


    /**
     * Adapt a SAX2 ignorable whitespace event.
     *
     * @param ch An array of characters.
     * @param start The starting position in the array.
     * @param length The number of characters to use.
     * @exception SAXException The client may raise a
     *            processing exception.
     * @see ContentHandler#ignorableWhitespace
     */
    public void ignorableWhitespace (char ch[], int start, int length)
        throws SAXException
    {
        if (documentHandler != null)
            documentHandler.ignorableWhitespace(ch, start, length);
    }


    /**
     * Adapt a SAX2 processing instruction event.
     *
     * @param target The processing instruction target.
     * @param data The remainder of the processing instruction
     * @exception SAXException The client may raise a
     *            processing exception.
     * @see ContentHandler#processingInstruction
     */
    public void processingInstruction (String target, String data)
        throws SAXException
    {
        if (documentHandler != null)
            documentHandler.processingInstruction(target, data);
    }


    /**
     * Adapt a SAX2 skipped entity event.
     *
     * @param name The name of the skipped entity.
     * @see ContentHandler#skippedEntity
     * @exception SAXException Throwable by subclasses.
     */
    public void skippedEntity (String name)
        throws SAXException
    {
    }



    ////////////////////////////////////////////////////////////////////
    // Internal state.
    ////////////////////////////////////////////////////////////////////

    XMLReader xmlReader;
    DocumentHandler documentHandler;
    AttributesAdapter qAtts;



    ////////////////////////////////////////////////////////////////////
    // Internal class.
    ////////////////////////////////////////////////////////////////////


    /**
     * Internal class to wrap a SAX2 Attributes object for SAX1.
     */
    final class AttributesAdapter implements AttributeList
    {
        AttributesAdapter ()
        {
        }


        /**
         * Set the embedded Attributes object.
         *
         * @param The embedded SAX2 Attributes.
         */
        void setAttributes (Attributes attributes)
        {
            this.attributes = attributes;
        }


        /**
         * Return the number of attributes.
         *
         * @return The length of the attribute list.
         * @see AttributeList#getLength
         */
        public int getLength ()
        {
            return attributes.getLength();
        }


        /**
         * Return the qualified (prefixed) name of an attribute by position.
         *
         * @return The qualified name.
         * @see AttributeList#getName
         */
        public String getName (int i)
        {
            return attributes.getQName(i);
        }


        /**
         * Return the type of an attribute by position.
         *
         * @return The type.
         * @see AttributeList#getType(int)
         */
        public String getType (int i)
        {
            return attributes.getType(i);
        }


        /**
         * Return the value of an attribute by position.
         *
         * @return The value.
         * @see AttributeList#getValue(int)
         */
        public String getValue (int i)
        {
            return attributes.getValue(i);
        }


        /**
         * Return the type of an attribute by qualified (prefixed) name.
         *
         * @return The type.
         * @see AttributeList#getType(java.lang.String)
         */
        public String getType (String qName)
        {
            return attributes.getType(qName);
        }


        /**
         * Return the value of an attribute by qualified (prefixed) name.
         *
         * @return The value.
         * @see AttributeList#getValue(java.lang.String)
         */
        public String getValue (String qName)
        {
            return attributes.getValue(qName);
        }

        private Attributes attributes;
    }

}

// end of XMLReaderAdapter.java
