/*
 * Copyright (c) 2000, 2005, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jaxp.xml.transform.sax;

import jaxp.xml.transform.TransformerConfigurationException;

import jaxp.sax.XMLFilter;

/**
 * This class extends TransformerFactory to provide SAX-specific
 * factory methods.  It provides two types of ContentHandlers,
 * one for creating Transformers, the other for creating Templates
 * objects.
 *
 * <p>If an application wants to set the ErrorHandler or EntityResolver
 * for an XMLReader used during a transformation, it should use a URIResolver
 * to return the SAXSource which provides (with getXMLReader) a reference to
 * the XMLReader.</p>
 */
public abstract class SAXTransformerFactory extends jaxp.xml.transform.TransformerFactory {

    /** If {@link jaxp.xml.transform.TransformerFactory#getFeature}
     * returns true when passed this value as an argument,
     * the TransformerFactory returned from
     * {@link jaxp.xml.transform.TransformerFactory#newInstance} may
     * be safely cast to a SAXTransformerFactory.
     */
    public static final String FEATURE =
        "http://javax.xml.transform.sax.SAXTransformerFactory/feature";

    /** If {@link jaxp.xml.transform.TransformerFactory#getFeature}
     * returns true when passed this value as an argument,
     * the {@link #newXMLFilter(jaxp.xml.transform.Source src)}
     * and {@link #newXMLFilter(jaxp.xml.transform.Templates templates)} methods are supported.
     */
    public static final String FEATURE_XMLFILTER =
        "http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter";

    /**
     * The default constructor is protected on purpose.
     */
    protected SAXTransformerFactory() {}

    /**
     * Get a TransformerHandler object that can process SAX
     * ContentHandler events into a Result, based on the transformation
     * instructions specified by the argument.
     *
     * @param src The Source of the transformation instructions.
     *
     * @return TransformerHandler ready to transform SAX events.
     *
     * @throws jaxp.xml.transform.TransformerConfigurationException If for some reason the
     * TransformerHandler can not be created.
     */
    public abstract TransformerHandler newTransformerHandler(jaxp.xml.transform.Source src)
        throws jaxp.xml.transform.TransformerConfigurationException;

    /**
     * Get a TransformerHandler object that can process SAX
     * ContentHandler events into a Result, based on the Templates argument.
     *
     * @param templates The compiled transformation instructions.
     *
     * @return TransformerHandler ready to transform SAX events.
     *
     * @throws jaxp.xml.transform.TransformerConfigurationException If for some reason the
     * TransformerHandler can not be created.
     */
    public abstract TransformerHandler newTransformerHandler(
        jaxp.xml.transform.Templates templates) throws jaxp.xml.transform.TransformerConfigurationException;

    /**
     * Get a TransformerHandler object that can process SAX
     * ContentHandler events into a Result. The transformation
     * is defined as an identity (or copy) transformation, for example
     * to copy a series of SAX parse events into a DOM tree.
     *
     * @return A non-null reference to a TransformerHandler, that may
     * be used as a ContentHandler for SAX parse events.
     *
     * @throws jaxp.xml.transform.TransformerConfigurationException If for some reason the
     * TransformerHandler cannot be created.
     */
    public abstract TransformerHandler newTransformerHandler()
        throws jaxp.xml.transform.TransformerConfigurationException;

    /**
     * Get a TemplatesHandler object that can process SAX
     * ContentHandler events into a Templates object.
     *
     * @return A non-null reference to a TransformerHandler, that may
     * be used as a ContentHandler for SAX parse events.
     *
     * @throws jaxp.xml.transform.TransformerConfigurationException If for some reason the
     * TemplatesHandler cannot be created.
     */
    public abstract TemplatesHandler newTemplatesHandler()
        throws jaxp.xml.transform.TransformerConfigurationException;

    /**
     * Create an XMLFilter that uses the given Source as the
     * transformation instructions.
     *
     * @param src The Source of the transformation instructions.
     *
     * @return An XMLFilter object, or null if this feature is not supported.
     *
     * @throws jaxp.xml.transform.TransformerConfigurationException If for some reason the
     * TemplatesHandler cannot be created.
     */
    public abstract XMLFilter newXMLFilter(jaxp.xml.transform.Source src)
        throws jaxp.xml.transform.TransformerConfigurationException;

    /**
     * Create an XMLFilter, based on the Templates argument..
     *
     * @param templates The compiled transformation instructions.
     *
     * @return An XMLFilter object, or null if this feature is not supported.
     *
     * @throws jaxp.xml.transform.TransformerConfigurationException If for some reason the
     * TemplatesHandler cannot be created.
     */
    public abstract XMLFilter newXMLFilter(jaxp.xml.transform.Templates templates)
        throws TransformerConfigurationException;
}
