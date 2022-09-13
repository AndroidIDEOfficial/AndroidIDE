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
 * Copyright 2000-2002,2004,2005 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.jaxp;

import java.util.Hashtable;

import jaxp.xml.XMLConstants;
import jaxp.xml.parsers.DocumentBuilder;
import jaxp.xml.parsers.DocumentBuilderFactory;
import jaxp.xml.parsers.ParserConfigurationException;
import jaxp.xml.validation.Schema;

import jaxp.sun.org.apache.xerces.internal.parsers.DOMParser;
import jaxp.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * @author Rajiv Mordani
 * @author Edwin Goei
 * @version $Id: DocumentBuilderFactoryImpl.java,v 1.8 2010-11-01 04:40:06 joehw Exp $
 */
public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {
    /** These are DocumentBuilderFactory attributes not DOM attributes */
    private Hashtable attributes;
    private Hashtable features;
    private Schema grammar;
    private boolean isXIncludeAware;

    /**
     * State of the secure processing feature, initially <code>false</code>
     */
    private boolean fSecureProcess = true;

    /**
     * Creates a new instance of a {@link DocumentBuilder}
     * using the currently configured parameters.
     */
    public DocumentBuilder newDocumentBuilder()
        throws ParserConfigurationException
    {
        /** Check that if a Schema has been specified that neither of the schema properties have been set. */
        if (grammar != null && attributes != null) {
            if (attributes.containsKey(JAXPConstants.JAXP_SCHEMA_LANGUAGE)) {
                throw new ParserConfigurationException(
                        SAXMessageFormatter.formatMessage(null,
                        "schema-already-specified", new Object[] {JAXPConstants.JAXP_SCHEMA_LANGUAGE}));
            }
            else if (attributes.containsKey(JAXPConstants.JAXP_SCHEMA_SOURCE)) {
                throw new ParserConfigurationException(
                        SAXMessageFormatter.formatMessage(null,
                        "schema-already-specified", new Object[] {JAXPConstants.JAXP_SCHEMA_SOURCE}));
            }
        }

        try {
            return new DocumentBuilderImpl(this, attributes, features, fSecureProcess);
        } catch (SAXException se) {
            // Handles both SAXNotSupportedException, SAXNotRecognizedException
            throw new ParserConfigurationException(se.getMessage());
        }
    }

    /**
     * Allows the user to set specific attributes on the underlying
     * implementation.
     * @param name    name of attribute
     * @param value   null means to remove attribute
     */
    public void setAttribute(String name, Object value)
        throws IllegalArgumentException
    {
        // This handles removal of attributes
        if (value == null) {
            if (attributes != null) {
                attributes.remove(name);
            }
            // Unrecognized attributes do not cause an exception
            return;
        }

        // This is ugly.  We have to collect the attributes and then
        // later create a DocumentBuilderImpl to verify the attributes.

        // Create Hashtable if none existed before
        if (attributes == null) {
            attributes = new Hashtable();
        }

        attributes.put(name, value);

        // Test the attribute name by possibly throwing an exception
        try {
            new DocumentBuilderImpl(this, attributes, features);
        } catch (Exception e) {
            attributes.remove(name);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Allows the user to retrieve specific attributes on the underlying
     * implementation.
     */
    public Object getAttribute(String name)
        throws IllegalArgumentException
    {
        // See if it's in the attributes Hashtable
        if (attributes != null) {
            Object val = attributes.get(name);
            if (val != null) {
                return val;
            }
        }

        DOMParser domParser = null;
        try {
            // We create a dummy DocumentBuilderImpl in case the attribute
            // name is not one that is in the attributes hashtable.
            domParser =
                new DocumentBuilderImpl(this, attributes, features).getDOMParser();
            return domParser.getProperty(name);
        } catch (SAXException se1) {
            // assert(name is not recognized or not supported), try feature
            try {
                boolean result = domParser.getFeature(name);
                // Must have been a feature
                return result ? Boolean.TRUE : Boolean.FALSE;
            } catch (SAXException se2) {
                // Not a property or a feature
                throw new IllegalArgumentException(se1.getMessage());
            }
        }
    }

    public Schema getSchema() {
        return grammar;
    }

    public void setSchema(Schema grammar) {
        this.grammar = grammar;
    }

    public boolean isXIncludeAware() {
        return this.isXIncludeAware;
    }

    public void setXIncludeAware(boolean state) {
        this.isXIncludeAware = state;
    }

    public boolean getFeature(String name)
        throws ParserConfigurationException {
        if (name.equals(XMLConstants.FEATURE_SECURE_PROCESSING)) {
            return fSecureProcess;
        }
        // See if it's in the features Hashtable
        if (features != null) {
            Object val = features.get(name);
            if (val != null) {
                return ((Boolean) val).booleanValue();
            }
        }
        try {
            DOMParser domParser = new DocumentBuilderImpl(this, attributes, features).getDOMParser();
            return domParser.getFeature(name);
        }
        catch (SAXException e) {
            throw new ParserConfigurationException(e.getMessage());
        }
    }

    public void setFeature(String name, boolean value)
        throws ParserConfigurationException {
        if (features == null) {
            features = new Hashtable();
        }
        // If this is the secure processing feature, save it then return.
        if (name.equals(XMLConstants.FEATURE_SECURE_PROCESSING)) {
            if (System.getSecurityManager() != null && (!value)) {
                throw new ParserConfigurationException(
                        SAXMessageFormatter.formatMessage(null,
                        "jaxp-secureprocessing-feature", null));
            }
            fSecureProcess = value;
            features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
            return;
        }

        features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
        // Test the feature by possibly throwing SAX exceptions
        try {
            new DocumentBuilderImpl(this, attributes, features);
        }
        catch (SAXNotSupportedException e) {
            features.remove(name);
            throw new ParserConfigurationException(e.getMessage());
        }
        catch (SAXNotRecognizedException e) {
            features.remove(name);
            throw new ParserConfigurationException(e.getMessage());
        }
    }
}
