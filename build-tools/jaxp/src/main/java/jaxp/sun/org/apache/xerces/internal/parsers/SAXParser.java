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
 * Copyright 2000-2005 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.parsers;

import jaxp.sun.org.apache.xerces.internal.impl.Constants;
import jaxp.sun.org.apache.xerces.internal.util.SymbolTable;
import jaxp.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import jaxp.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import jaxp.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * This is the main Xerces SAX parser class. It uses the abstract SAX
 * parser with a document scanner, a dtd scanner, and a validator, as
 * well as a grammar pool.
 *
 * @author Arnaud  Le Hors, IBM
 * @author Andy Clark, IBM
 *
 * @version $Id: SAXParser.java,v 1.7 2010-11-01 04:40:09 joehw Exp $
 */
public class SAXParser
    extends AbstractSAXParser {

    //
    // Constants
    //

    // features

    /** Feature identifier: notify built-in refereces. */
    protected static final String NOTIFY_BUILTIN_REFS =
        Constants.XERCES_FEATURE_PREFIX + Constants.NOTIFY_BUILTIN_REFS_FEATURE;

    protected static final String REPORT_WHITESPACE =
            Constants.SUN_SCHEMA_FEATURE_PREFIX + Constants.SUN_REPORT_IGNORED_ELEMENT_CONTENT_WHITESPACE;

    /** Recognized features. */
    private static final String[] RECOGNIZED_FEATURES = {
        NOTIFY_BUILTIN_REFS,
        REPORT_WHITESPACE
    };

    // properties

    /** Property identifier: symbol table. */
    protected static final String SYMBOL_TABLE =
        Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;

    /** Property identifier: XML grammar pool. */
    protected static final String XMLGRAMMAR_POOL =
        Constants.XERCES_PROPERTY_PREFIX+Constants.XMLGRAMMAR_POOL_PROPERTY;

    /** Recognized properties. */
    private static final String[] RECOGNIZED_PROPERTIES = {
        SYMBOL_TABLE,
        XMLGRAMMAR_POOL,
    };


    //
    // Constructors
    //

    /**
     * Constructs a SAX parser using the specified parser configuration.
     */
    public SAXParser(XMLParserConfiguration config) {
        super(config);
    } // <init>(XMLParserConfiguration)

    /**
     * Constructs a SAX parser using the dtd/xml schema parser configuration.
     */
    public SAXParser() {
        this(null, null);
    } // <init>()

    /**
     * Constructs a SAX parser using the specified symbol table.
     */
    public SAXParser(SymbolTable symbolTable) {
        this(symbolTable, null);
    } // <init>(SymbolTable)

    /**
     * Constructs a SAX parser using the specified symbol table and
     * grammar pool.
     */
    public SAXParser(SymbolTable symbolTable, XMLGrammarPool grammarPool) {
        super(new XIncludeAwareParserConfiguration());

        // set features
        fConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
        fConfiguration.setFeature(NOTIFY_BUILTIN_REFS, true);

        // set properties
        fConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
        if (symbolTable != null) {
            fConfiguration.setProperty(SYMBOL_TABLE, symbolTable);
        }
        if (grammarPool != null) {
            fConfiguration.setProperty(XMLGRAMMAR_POOL, grammarPool);
        }

    } // <init>(SymbolTable,XMLGrammarPool)

    /**
     * Sets the particular property in the underlying implementation of
     * org.xml.sax.XMLReader.
     */
    public void setProperty(String name, Object value)
        throws SAXNotRecognizedException, SAXNotSupportedException {
        /**
         * It's possible for users to set a security manager through the interface.
         * If it's the old SecurityManager, convert it to the new XMLSecurityManager
         */
        if (name.equals(Constants.SECURITY_MANAGER)) {
            securityManager = XMLSecurityManager.convert(value, securityManager);
            super.setProperty(Constants.SECURITY_MANAGER, securityManager);
            return;
        }
        if (name.equals(Constants.XML_SECURITY_PROPERTY_MANAGER)) {
            if (value == null) {
                securityPropertyManager = new XMLSecurityPropertyManager();
            } else {
                securityPropertyManager = (XMLSecurityPropertyManager)value;
            }
            super.setProperty(Constants.XML_SECURITY_PROPERTY_MANAGER, securityPropertyManager);
            return;
        }

        if (securityManager == null) {
            securityManager = new XMLSecurityManager(true);
            super.setProperty(Constants.SECURITY_MANAGER, securityManager);
        }

        if (securityPropertyManager == null) {
            securityPropertyManager = new XMLSecurityPropertyManager();
            super.setProperty(Constants.XML_SECURITY_PROPERTY_MANAGER, securityPropertyManager);
        }

        int index = securityPropertyManager.getIndex(name);
        if (index > -1) {
            /**
             * this is a direct call to this parser, not a subclass since
             * internally the support of this property is done through
             * XMLSecurityPropertyManager
             */
            securityPropertyManager.setValue(index, XMLSecurityPropertyManager.State.APIPROPERTY, (String)value);
        } else {
            //check if the property is managed by security manager
            if (!securityManager.setLimit(name, XMLSecurityManager.State.APIPROPERTY, value)) {
                //fall back to the default configuration to handle the property
                super.setProperty(name, value);
            }
        }
    }
} // class SAXParser
