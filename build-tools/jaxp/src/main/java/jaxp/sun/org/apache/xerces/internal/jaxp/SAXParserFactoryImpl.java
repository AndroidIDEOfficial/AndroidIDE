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
import jaxp.xml.parsers.ParserConfigurationException;
import jaxp.xml.parsers.SAXParser;
import jaxp.xml.parsers.SAXParserFactory;
import jaxp.xml.validation.Schema;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import jaxp.sun.org.apache.xerces.internal.impl.Constants;
import jaxp.sun.org.apache.xerces.internal.util.SAXMessageFormatter;

/**
 * This is the implementation specific class for the
 * <code>javax.xml.parsers.SAXParserFactory</code>. This is the platform
 * default implementation for the platform.
 *
 * @author Rajiv Mordani
 * @author Edwin Goei
 *
 * @version $Id: SAXParserFactoryImpl.java,v 1.9 2010-11-01 04:40:06 joehw Exp $
 */
public class SAXParserFactoryImpl extends SAXParserFactory {

    /** Feature identifier: validation. */
    private static final String VALIDATION_FEATURE =
        Constants.SAX_FEATURE_PREFIX + Constants.VALIDATION_FEATURE;

    /** Feature identifier: namespaces. */
    private static final String NAMESPACES_FEATURE =
        Constants.SAX_FEATURE_PREFIX + Constants.NAMESPACES_FEATURE;

    /** Feature identifier: XInclude processing */
    private static final String XINCLUDE_FEATURE =
        Constants.XERCES_FEATURE_PREFIX + Constants.XINCLUDE_FEATURE;

    private Hashtable features;
    private Schema grammar;
    private boolean isXIncludeAware;

    /**
     * State of the secure processing feature, initially <code>false</code>
     */
    private boolean fSecureProcess = true;

    /**
     * Creates a new instance of <code>SAXParser</code> using the currently
     * configured factory parameters.
     * @return javax.xml.parsers.SAXParser
     */
    public SAXParser newSAXParser()
        throws ParserConfigurationException
    {
        SAXParser saxParserImpl;
        try {
            saxParserImpl = new SAXParserImpl(this, features, fSecureProcess);
        } catch (SAXException se) {
            // Translate to ParserConfigurationException
            throw new ParserConfigurationException(se.getMessage());
        }
        return saxParserImpl;
    }

    /**
     * Common code for translating exceptions
     */
    private SAXParserImpl newSAXParserImpl()
        throws ParserConfigurationException, SAXNotRecognizedException,
        SAXNotSupportedException
    {
        SAXParserImpl saxParserImpl;
        try {
            saxParserImpl = new SAXParserImpl(this, features);
        } catch (SAXNotSupportedException e) {
            throw e;
        } catch (SAXNotRecognizedException e) {
            throw e;
        } catch (SAXException se) {
            throw new ParserConfigurationException(se.getMessage());
        }
        return saxParserImpl;
    }

    /**
     * Sets the particular feature in the underlying implementation of
     * org.xml.sax.XMLReader.
     */
    public void setFeature(String name, boolean value)
        throws ParserConfigurationException, SAXNotRecognizedException,
                SAXNotSupportedException {
        if (name == null) {
            throw new NullPointerException();
        }
        // If this is the secure processing feature, save it then return.
        if (name.equals(XMLConstants.FEATURE_SECURE_PROCESSING)) {
            if (System.getSecurityManager() != null && (!value)) {
                throw new ParserConfigurationException(
                        SAXMessageFormatter.formatMessage(null,
                        "jaxp-secureprocessing-feature", null));
            }
            fSecureProcess = value;
            putInFeatures(name, value);
            return;
        }

        // XXX This is ugly.  We have to collect the features and then
        // later create an XMLReader to verify the features.
        putInFeatures(name, value);
        // Test the feature by possibly throwing SAX exceptions
        try {
            newSAXParserImpl();
        } catch (SAXNotSupportedException e) {
            features.remove(name);
            throw e;
        } catch (SAXNotRecognizedException e) {
            features.remove(name);
            throw e;
        }
    }

    /**
     * returns the particular property requested for in the underlying
     * implementation of org.xml.sax.XMLReader.
     */
    public boolean getFeature(String name)
        throws ParserConfigurationException, SAXNotRecognizedException,
                SAXNotSupportedException {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.equals(XMLConstants.FEATURE_SECURE_PROCESSING)) {
            return fSecureProcess;
        }
        // Check for valid name by creating a dummy XMLReader to get
        // feature value
        return newSAXParserImpl().getXMLReader().getFeature(name);
    }

    public Schema getSchema() {
        return grammar;
    }

    public void setSchema(Schema grammar) {
        this.grammar = grammar;
    }

    public boolean isXIncludeAware() {
        return getFromFeatures(XINCLUDE_FEATURE);
    }

    public void setXIncludeAware(boolean state) {
        putInFeatures(XINCLUDE_FEATURE, state);
    }


    public void setValidating(boolean validating) {
        putInFeatures(VALIDATION_FEATURE, validating);
    }

    public boolean isValidating() {
         return getFromFeatures(VALIDATION_FEATURE);
    }

    private void putInFeatures(String name, boolean value){
         if (features == null) {
            features = new Hashtable();
        }
        features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
    }

    private boolean getFromFeatures(String name){
         if (features == null){
            return false;
         }
         else {
             Object value = features.get(name);
             return (value == null) ? false : Boolean.valueOf(value.toString()).booleanValue();
         }
    }

    public boolean isNamespaceAware() {
        return getFromFeatures(NAMESPACES_FEATURE);
    }

    public void setNamespaceAware(boolean awareness) {
       putInFeatures(NAMESPACES_FEATURE, awareness);
    }

 }
