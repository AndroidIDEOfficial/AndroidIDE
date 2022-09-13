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
 * Copyright 2001-2005 The Apache Software Foundation.
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
import jaxp.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import jaxp.sun.org.apache.xerces.internal.xinclude.XIncludeNamespaceSupport;
import jaxp.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import jaxp.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;

/**
 * This parser configuration includes an <code>XIncludeHandler</code> in the pipeline
 * before the schema validator, or as the last component in the pipeline if there is
 * no schema validator.  Using this pipeline will enable processing according to the
 * XML Inclusions specification, to the conformance level described in
 * <code>XIncludeHandler</code>.
 *
 * @author Peter McCracken, IBM
 * @see XIncludeHandler
 */
public class XIncludeParserConfiguration extends XML11Configuration {

    private XIncludeHandler fXIncludeHandler;

    /** Feature identifier: allow notation and unparsed entity events to be sent out of order. */
    protected static final String ALLOW_UE_AND_NOTATION_EVENTS =
        Constants.SAX_FEATURE_PREFIX + Constants.ALLOW_DTD_EVENTS_AFTER_ENDDTD_FEATURE;

    /** Feature identifier: fixup base URIs. */
    protected static final String XINCLUDE_FIXUP_BASE_URIS =
        Constants.XERCES_FEATURE_PREFIX + Constants.XINCLUDE_FIXUP_BASE_URIS_FEATURE;

    /** Feature identifier: fixup language. */
    protected static final String XINCLUDE_FIXUP_LANGUAGE =
        Constants.XERCES_FEATURE_PREFIX + Constants.XINCLUDE_FIXUP_LANGUAGE_FEATURE;

    /** Property identifier: error reporter. */
    protected static final String XINCLUDE_HANDLER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.XINCLUDE_HANDLER_PROPERTY;

    /** Property identifier: error reporter. */
    protected static final String NAMESPACE_CONTEXT =
        Constants.XERCES_PROPERTY_PREFIX + Constants.NAMESPACE_CONTEXT_PROPERTY;

    /** Default constructor. */
    public XIncludeParserConfiguration() {
        this(null, null, null);
    } // <init>()

    /**
     * Constructs a parser configuration using the specified symbol table.
     *
     * @param symbolTable The symbol table to use.
     */
    public XIncludeParserConfiguration(SymbolTable symbolTable) {
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
    public XIncludeParserConfiguration(
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
    public XIncludeParserConfiguration(
        SymbolTable symbolTable,
        XMLGrammarPool grammarPool,
        XMLComponentManager parentSettings) {
        super(symbolTable, grammarPool, parentSettings);

        fXIncludeHandler = new XIncludeHandler();
        addCommonComponent(fXIncludeHandler);

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

        setProperty(XINCLUDE_HANDLER, fXIncludeHandler);
        setProperty(NAMESPACE_CONTEXT, new XIncludeNamespaceSupport());
    } // <init>(SymbolTable,XMLGrammarPool)}


        /** Configures the pipeline. */
    protected void configurePipeline() {
        super.configurePipeline();

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

         if (prev != null) {
            XMLDocumentHandler next = prev.getDocumentHandler();
            prev.setDocumentHandler(fXIncludeHandler);
            fXIncludeHandler.setDocumentSource(prev);
            if (next != null) {
                fXIncludeHandler.setDocumentHandler(next);
                next.setDocumentSource(fXIncludeHandler);
            }
         }
         else {
            setDocumentHandler(fXIncludeHandler);
         }

    } // configurePipeline()

        protected void configureXML11Pipeline() {
                super.configureXML11Pipeline();

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

        } // configureXML11Pipeline()

    public void setProperty(String propertyId, Object value)
        throws XMLConfigurationException {

        if (propertyId.equals(XINCLUDE_HANDLER)) {
        }

        super.setProperty(propertyId, value);
    } // setProperty(String,Object)
}
