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
 * Copyright 2005 The Apache Software Foundation.
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
import jaxp.sun.org.apache.xerces.internal.util.FeatureState;
import jaxp.sun.org.apache.xerces.internal.util.NamespaceSupport;
import jaxp.sun.org.apache.xerces.internal.util.SymbolTable;
import jaxp.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import jaxp.sun.org.apache.xerces.internal.xinclude.XIncludeNamespaceSupport;
import jaxp.sun.org.apache.xerces.internal.xni.NamespaceContext;
import jaxp.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import jaxp.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;

/**
 * This class is the configuration used to parse XML 1.0 and XML 1.1 documents
 * and provides support for XInclude. This is the default Xerces configuration.
 *
 * @author Michael Glavassevich, IBM
 *
 * @version $Id: XIncludeAwareParserConfiguration.java,v 1.5 2010-11-01 04:40:10 joehw Exp $
 */
public class XIncludeAwareParserConfiguration extends XML11Configuration {

    /** Feature identifier: allow notation and unparsed entity events to be sent out of order. */
    protected static final String ALLOW_UE_AND_NOTATION_EVENTS =
        Constants.SAX_FEATURE_PREFIX + Constants.ALLOW_DTD_EVENTS_AFTER_ENDDTD_FEATURE;

    /** Feature identifier: fixup base URIs. */
    protected static final String XINCLUDE_FIXUP_BASE_URIS =
        Constants.XERCES_FEATURE_PREFIX + Constants.XINCLUDE_FIXUP_BASE_URIS_FEATURE;

    /** Feature identifier: fixup language. */
    protected static final String XINCLUDE_FIXUP_LANGUAGE =
        Constants.XERCES_FEATURE_PREFIX + Constants.XINCLUDE_FIXUP_LANGUAGE_FEATURE;

    /** Feature identifier: XInclude processing */
    protected static final String XINCLUDE_FEATURE =
        Constants.XERCES_FEATURE_PREFIX + Constants.XINCLUDE_FEATURE;

    /** Property identifier: error reporter. */
    protected static final String XINCLUDE_HANDLER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.XINCLUDE_HANDLER_PROPERTY;

    /** Property identifier: error reporter. */
    protected static final String NAMESPACE_CONTEXT =
        Constants.XERCES_PROPERTY_PREFIX + Constants.NAMESPACE_CONTEXT_PROPERTY;

    //
    // Components
    //

    /** XInclude handler. */
    protected XIncludeHandler fXIncludeHandler;

    /** Non-XInclude NamespaceContext. */
    protected NamespaceSupport fNonXIncludeNSContext;

    /** XInclude NamespaceContext. */
    protected XIncludeNamespaceSupport fXIncludeNSContext;

    /** Current NamespaceContext. */
    protected NamespaceContext fCurrentNSContext;

    /** Flag indicating whether XInclude processsing is enabled. */
    protected boolean fXIncludeEnabled = false;

    /** Default constructor. */
    public XIncludeAwareParserConfiguration() {
        this(null, null, null);
    } // <init>()

    /**
     * Constructs a parser configuration using the specified symbol table.
     *
     * @param symbolTable The symbol table to use.
     */
    public XIncludeAwareParserConfiguration(SymbolTable symbolTable) {
        this(symbolTable, null, null);
    } // <init>(SymbolTable)

    /**
     * Constructs a parser configuration using the specified symbol table and
     * grammar pool.
     * <p>
     *
     * @param symbolTable The symbol table to use.
     * @param grammarPool The grammar pool to use.
     */
    public XIncludeAwareParserConfiguration(
            SymbolTable symbolTable,
            XMLGrammarPool grammarPool) {
        this(symbolTable, grammarPool, null);
    } // <init>(SymbolTable,XMLGrammarPool)

    /**
     * Constructs a parser configuration using the specified symbol table,
     * grammar pool, and parent settings.
     * <p>
     *
     * @param symbolTable    The symbol table to use.
     * @param grammarPool    The grammar pool to use.
     * @param parentSettings The parent settings.
     */
    public XIncludeAwareParserConfiguration(
            SymbolTable symbolTable,
            XMLGrammarPool grammarPool,
            XMLComponentManager parentSettings) {
        super(symbolTable, grammarPool, parentSettings);

        final String[] recognizedFeatures = {
                ALLOW_UE_AND_NOTATION_EVENTS,
                XINCLUDE_FIXUP_BASE_URIS,
                XINCLUDE_FIXUP_LANGUAGE
        };
        addRecognizedFeatures(recognizedFeatures);

        // add default recognized properties
        final String[] recognizedProperties =
        { XINCLUDE_HANDLER, NAMESPACE_CONTEXT };
        addRecognizedProperties(recognizedProperties);

        setFeature(ALLOW_UE_AND_NOTATION_EVENTS, true);
        setFeature(XINCLUDE_FIXUP_BASE_URIS, true);
        setFeature(XINCLUDE_FIXUP_LANGUAGE, true);

        fNonXIncludeNSContext = new NamespaceSupport();
        fCurrentNSContext = fNonXIncludeNSContext;
        setProperty(NAMESPACE_CONTEXT, fNonXIncludeNSContext);
    }


    /** Configures the pipeline. */
    protected void configurePipeline() {
        super.configurePipeline();
        if (fXIncludeEnabled) {
            // If the XInclude handler was not in the pipeline insert it.
            if (fXIncludeHandler == null) {
                fXIncludeHandler = new XIncludeHandler();
                // add XInclude component
                setProperty(XINCLUDE_HANDLER, fXIncludeHandler);
                addCommonComponent(fXIncludeHandler);
                fXIncludeHandler.reset(this);
            }
            // Setup NamespaceContext
            if (fCurrentNSContext != fXIncludeNSContext) {
                if (fXIncludeNSContext == null) {
                    fXIncludeNSContext = new XIncludeNamespaceSupport();
                }
                fCurrentNSContext = fXIncludeNSContext;
                setProperty(NAMESPACE_CONTEXT, fXIncludeNSContext);
            }
            //configure DTD pipeline
            fDTDScanner.setDTDHandler(fDTDProcessor);
            fDTDProcessor.setDTDSource(fDTDScanner);
            fDTDProcessor.setDTDHandler(fXIncludeHandler);
            fXIncludeHandler.setDTDSource(fDTDProcessor);
            fXIncludeHandler.setDTDHandler(fDTDHandler);
            if (fDTDHandler != null) {
                fDTDHandler.setDTDSource(fXIncludeHandler);
            }

            // configure XML document pipeline: insert after DTDValidator and
            // before XML Schema validator
            XMLDocumentSource prev = null;
            if (fFeatures.get(XMLSCHEMA_VALIDATION) == Boolean.TRUE) {
                // we don't have to worry about fSchemaValidator being null, since
                // super.configurePipeline() instantiated it if the feature was set
                prev = fSchemaValidator.getDocumentSource();
            }
            // Otherwise, insert after the last component in the pipeline
            else {
                prev = fLastComponent;
                fLastComponent = fXIncludeHandler;
            }

            XMLDocumentHandler next = prev.getDocumentHandler();
            prev.setDocumentHandler(fXIncludeHandler);
            fXIncludeHandler.setDocumentSource(prev);
            if (next != null) {
                fXIncludeHandler.setDocumentHandler(next);
                next.setDocumentSource(fXIncludeHandler);
            }
        }
        else {
            // Setup NamespaceContext
            if (fCurrentNSContext != fNonXIncludeNSContext) {
                fCurrentNSContext = fNonXIncludeNSContext;
                setProperty(NAMESPACE_CONTEXT, fNonXIncludeNSContext);
            }
        }
    } // configurePipeline()

    protected void configureXML11Pipeline() {
        super.configureXML11Pipeline();
        if (fXIncludeEnabled) {
            // If the XInclude handler was not in the pipeline insert it.
            if (fXIncludeHandler == null) {
                fXIncludeHandler = new XIncludeHandler();
                // add XInclude component
                setProperty(XINCLUDE_HANDLER, fXIncludeHandler);
                addCommonComponent(fXIncludeHandler);
                fXIncludeHandler.reset(this);
            }
            // Setup NamespaceContext
            if (fCurrentNSContext != fXIncludeNSContext) {
                if (fXIncludeNSContext == null) {
                    fXIncludeNSContext = new XIncludeNamespaceSupport();
                }
                fCurrentNSContext = fXIncludeNSContext;
                setProperty(NAMESPACE_CONTEXT, fXIncludeNSContext);
            }
            // configure XML 1.1. DTD pipeline
            fXML11DTDScanner.setDTDHandler(fXML11DTDProcessor);
            fXML11DTDProcessor.setDTDSource(fXML11DTDScanner);
            fXML11DTDProcessor.setDTDHandler(fXIncludeHandler);
            fXIncludeHandler.setDTDSource(fXML11DTDProcessor);
            fXIncludeHandler.setDTDHandler(fDTDHandler);
            if (fDTDHandler != null) {
                fDTDHandler.setDTDSource(fXIncludeHandler);
            }

            // configure XML document pipeline: insert after DTDValidator and
            // before XML Schema validator
            XMLDocumentSource prev = null;
            if (fFeatures.get(XMLSCHEMA_VALIDATION) == Boolean.TRUE) {
                // we don't have to worry about fSchemaValidator being null, since
                // super.configurePipeline() instantiated it if the feature was set
                prev = fSchemaValidator.getDocumentSource();
            }
            // Otherwise, insert after the last component in the pipeline
            else {
                prev = fLastComponent;
                fLastComponent = fXIncludeHandler;
            }

            XMLDocumentHandler next = prev.getDocumentHandler();
            prev.setDocumentHandler(fXIncludeHandler);
            fXIncludeHandler.setDocumentSource(prev);
            if (next != null) {
                fXIncludeHandler.setDocumentHandler(next);
                next.setDocumentSource(fXIncludeHandler);
            }
        }
        else {
            // Setup NamespaceContext
            if (fCurrentNSContext != fNonXIncludeNSContext) {
                fCurrentNSContext = fNonXIncludeNSContext;
                setProperty(NAMESPACE_CONTEXT, fNonXIncludeNSContext);
            }
        }
    } // configureXML11Pipeline()

    public FeatureState getFeatureState(String featureId)
        throws XMLConfigurationException {
        if (featureId.equals(PARSER_SETTINGS)) {
            return FeatureState.is(fConfigUpdated);
        }
        else if (featureId.equals(XINCLUDE_FEATURE)) {
            return FeatureState.is(fXIncludeEnabled);
        }
        return super.getFeatureState0(featureId);

    } // getFeature(String):boolean

    public void setFeature(String featureId, boolean state)
        throws XMLConfigurationException {
        if (featureId.equals(XINCLUDE_FEATURE)) {
            fXIncludeEnabled = state;
            fConfigUpdated = true;
            return;
        }
        super.setFeature(featureId,state);
    }

}
