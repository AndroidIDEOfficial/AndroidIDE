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
import jaxp.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
import jaxp.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
import jaxp.sun.org.apache.xerces.internal.util.FeatureState;
import jaxp.sun.org.apache.xerces.internal.util.PropertyState;
import jaxp.sun.org.apache.xerces.internal.util.SymbolTable;
import jaxp.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;

/**
 * This is the "standard" parser configuration. It extends the DTD
 * configuration with the standard set of parser components.
 * The standard set of parser components include those needed
 * to parse and validate with DTD's, and those needed for XML
 * Schema.</p>
 * <p>
 * In addition to the features and properties recognized by the base
 * parser configuration, this class recognizes these additional
 * features and properties:
 * <ul>
 * <li>Features
 *  <ul>
 *  <li>http://apache.org/xml/features/validation/schema</li>
 *  <li>http://apache.org/xml/features/validation/schema-full-checking</li>
 *  <li>http://apache.org/xml/features/validation/schema/normalized-value</li>
 *  <li>http://apache.org/xml/features/validation/schema/element-default</li>
 *  </ul>
 * <li>Properties
 *  <ul>
 *   <li>http://apache.org/xml/properties/internal/error-reporter</li>
 *   <li>http://apache.org/xml/properties/internal/entity-manager</li>
 *   <li>http://apache.org/xml/properties/internal/document-scanner</li>
 *   <li>http://apache.org/xml/properties/internal/dtd-scanner</li>
 *   <li>http://apache.org/xml/properties/internal/grammar-pool</li>
 *   <li>http://apache.org/xml/properties/internal/validator/dtd</li>
 *   <li>http://apache.org/xml/properties/internal/datatype-validator-factory</li>
 *  </ul>
 * </ul>
 *
 * @author Arnaud  Le Hors, IBM
 * @author Andy Clark, IBM
 *
 * @version $Id: StandardParserConfiguration.java,v 1.7 2010-11-01 04:40:10 joehw Exp $
 */
public class StandardParserConfiguration
    extends DTDConfiguration {

    //
    // Constants
    //

    // feature identifiers

    /** Feature identifier: expose schema normalized value */
    protected static final String NORMALIZE_DATA =
    Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_NORMALIZED_VALUE;


    /** Feature identifier: send element default value via characters() */
    protected static final String SCHEMA_ELEMENT_DEFAULT =
    Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_ELEMENT_DEFAULT;


    /** Feature identifier: augment PSVI */
    protected static final String SCHEMA_AUGMENT_PSVI =
    Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_AUGMENT_PSVI;


    /** feature identifier: XML Schema validation */
    protected static final String XMLSCHEMA_VALIDATION =
    Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_VALIDATION_FEATURE;

    /** feature identifier: XML Schema validation -- full checking */
    protected static final String XMLSCHEMA_FULL_CHECKING =
    Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_FULL_CHECKING;

    /** Feature: generate synthetic annotations */
    protected static final String GENERATE_SYNTHETIC_ANNOTATIONS =
        Constants.XERCES_FEATURE_PREFIX + Constants.GENERATE_SYNTHETIC_ANNOTATIONS_FEATURE;

    /** Feature identifier: validate annotations */
    protected static final String VALIDATE_ANNOTATIONS =
        Constants.XERCES_FEATURE_PREFIX + Constants.VALIDATE_ANNOTATIONS_FEATURE;

    /** Feature identifier: honour all schemaLocations */
    protected static final String HONOUR_ALL_SCHEMALOCATIONS =
        Constants.XERCES_FEATURE_PREFIX + Constants.HONOUR_ALL_SCHEMALOCATIONS_FEATURE;

    /** Feature identifier: namespace growth */
    protected static final String NAMESPACE_GROWTH =
        Constants.XERCES_FEATURE_PREFIX + Constants.NAMESPACE_GROWTH_FEATURE;

    /** Feature identifier: tolerate duplicates */
    protected static final String TOLERATE_DUPLICATES =
        Constants.XERCES_FEATURE_PREFIX + Constants.TOLERATE_DUPLICATES_FEATURE;

    // property identifiers

    /** Property identifier: XML Schema validator. */
    protected static final String SCHEMA_VALIDATOR =
        Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_VALIDATOR_PROPERTY;

    /** Property identifier: schema location. */
    protected static final String SCHEMA_LOCATION =
    Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_LOCATION;

    /** Property identifier: no namespace schema location. */
    protected static final String SCHEMA_NONS_LOCATION =
    Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_NONS_LOCATION;

    /** Property identifier: Schema DV Factory */
    protected static final String SCHEMA_DV_FACTORY =
        Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_DV_FACTORY_PROPERTY;

    //
    // Data
    //

    // components (non-configurable)

    /** XML Schema Validator. */
    protected XMLSchemaValidator fSchemaValidator;

    //
    // Constructors
    //

    /** Default constructor. */
    public StandardParserConfiguration() {
        this(null, null, null);
    } // <init>()

    /**
     * Constructs a parser configuration using the specified symbol table.
     *
     * @param symbolTable The symbol table to use.
     */
    public StandardParserConfiguration(SymbolTable symbolTable) {
        this(symbolTable, null, null);
    } // <init>(SymbolTable)

    /**
     * Constructs a parser configuration using the specified symbol table and
     * grammar pool.
     * <p>
     * <strong>REVISIT:</strong>
     * Grammar pool will be updated when the new validation engine is
     * implemented.
     *
     * @param symbolTable The symbol table to use.
     * @param grammarPool The grammar pool to use.
     */
    public StandardParserConfiguration(SymbolTable symbolTable,
                                       XMLGrammarPool grammarPool) {
        this(symbolTable, grammarPool, null);
    } // <init>(SymbolTable,XMLGrammarPool)

    /**
     * Constructs a parser configuration using the specified symbol table,
     * grammar pool, and parent settings.
     * <p>
     * <strong>REVISIT:</strong>
     * Grammar pool will be updated when the new validation engine is
     * implemented.
     *
     * @param symbolTable    The symbol table to use.
     * @param grammarPool    The grammar pool to use.
     * @param parentSettings The parent settings.
     */
    public StandardParserConfiguration(SymbolTable symbolTable,
                                       XMLGrammarPool grammarPool,
                                       XMLComponentManager parentSettings) {
        super(symbolTable, grammarPool, parentSettings);

        // add default recognized features
        final String[] recognizedFeatures = {
            NORMALIZE_DATA,
            SCHEMA_ELEMENT_DEFAULT,
            SCHEMA_AUGMENT_PSVI,
            GENERATE_SYNTHETIC_ANNOTATIONS,
            VALIDATE_ANNOTATIONS,
            HONOUR_ALL_SCHEMALOCATIONS,
            NAMESPACE_GROWTH,
            TOLERATE_DUPLICATES,
            // NOTE: These shouldn't really be here but since the XML Schema
            //       validator is constructed dynamically, its recognized
            //       features might not have been set and it would cause a
            //       not-recognized exception to be thrown. -Ac
            XMLSCHEMA_VALIDATION,
            XMLSCHEMA_FULL_CHECKING,
        };
        addRecognizedFeatures(recognizedFeatures);

        // set state for default features
        setFeature(SCHEMA_ELEMENT_DEFAULT, true);
        setFeature(NORMALIZE_DATA, true);
        setFeature(SCHEMA_AUGMENT_PSVI, true);
        setFeature(GENERATE_SYNTHETIC_ANNOTATIONS, false);
        setFeature(VALIDATE_ANNOTATIONS, false);
        setFeature(HONOUR_ALL_SCHEMALOCATIONS, false);
        setFeature(NAMESPACE_GROWTH, false);
        setFeature(TOLERATE_DUPLICATES, false);

        // add default recognized properties

        final String[] recognizedProperties = {
            // NOTE: These shouldn't really be here but since the XML Schema
            //       validator is constructed dynamically, its recognized
            //       properties might not have been set and it would cause a
            //       not-recognized exception to be thrown. -Ac
            SCHEMA_LOCATION,
            SCHEMA_NONS_LOCATION,
            SCHEMA_DV_FACTORY,
            };

                        addRecognizedProperties(recognizedProperties);

    } // <init>(SymbolTable,XMLGrammarPool)

    //
    // Public methods
    //

    /** Configures the pipeline. */
    protected void configurePipeline() {
        super.configurePipeline();
        if ( getFeature(XMLSCHEMA_VALIDATION )) {
            // If schema validator was not in the pipeline insert it.
            if (fSchemaValidator == null) {
                fSchemaValidator = new XMLSchemaValidator();

                // add schema component
                fProperties.put(SCHEMA_VALIDATOR, fSchemaValidator);
                addComponent(fSchemaValidator);
                 // add schema message formatter
                if (fErrorReporter.getMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN) == null) {
                    XSMessageFormatter xmft = new XSMessageFormatter();
                    fErrorReporter.putMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN, xmft);
                }

            }
            fLastComponent = fSchemaValidator;
            fNamespaceBinder.setDocumentHandler(fSchemaValidator);

            fSchemaValidator.setDocumentHandler(fDocumentHandler);
            fSchemaValidator.setDocumentSource(fNamespaceBinder);
        }


    } // configurePipeline()

    // features and properties

    /**
     * Check a feature. If feature is know and supported, this method simply
     * returns. Otherwise, the appropriate exception is thrown.
     *
     * @param featureId The unique identifier (URI) of the feature.
     *
     * @throws XMLConfigurationException Thrown for configuration error.
     *                                   In general, components should
     *                                   only throw this exception if
     *                                   it is <strong>really</strong>
     *                                   a critical error.
     */
    protected FeatureState checkFeature(String featureId)
        throws XMLConfigurationException {

        //
        // Xerces Features
        //

        if (featureId.startsWith(Constants.XERCES_FEATURE_PREFIX)) {
            final int suffixLength = featureId.length() - Constants.XERCES_FEATURE_PREFIX.length();

            //
            // http://apache.org/xml/features/validation/schema
            //   Lets the user turn Schema validation support on/off.
            //
            if (suffixLength == Constants.SCHEMA_VALIDATION_FEATURE.length() &&
                featureId.endsWith(Constants.SCHEMA_VALIDATION_FEATURE)) {
                return FeatureState.RECOGNIZED;
            }
            // activate full schema checking
            if (suffixLength == Constants.SCHEMA_FULL_CHECKING.length() &&
                featureId.endsWith(Constants.SCHEMA_FULL_CHECKING)) {
                return FeatureState.RECOGNIZED;
            }
            // Feature identifier: expose schema normalized value
            //  http://apache.org/xml/features/validation/schema/normalized-value
            if (suffixLength == Constants.SCHEMA_NORMALIZED_VALUE.length() &&
                featureId.endsWith(Constants.SCHEMA_NORMALIZED_VALUE)) {
                return FeatureState.RECOGNIZED;
            }
            // Feature identifier: send element default value via characters()
            // http://apache.org/xml/features/validation/schema/element-default
            if (suffixLength == Constants.SCHEMA_ELEMENT_DEFAULT.length() &&
                featureId.endsWith(Constants.SCHEMA_ELEMENT_DEFAULT)) {
                return FeatureState.RECOGNIZED;
            }
        }

        //
        // Not recognized
        //

        return super.checkFeature(featureId);

    } // checkFeature(String)

    /**
     * Check a property. If the property is know and supported, this method
     * simply returns. Otherwise, the appropriate exception is thrown.
     *
     * @param propertyId The unique identifier (URI) of the property
     *                   being set.
     *
     * @throws XMLConfigurationException Thrown for configuration error.
     *                                   In general, components should
     *                                   only throw this exception if
     *                                   it is <strong>really</strong>
     *                                   a critical error.
     */
    protected PropertyState checkProperty(String propertyId)
        throws XMLConfigurationException {

        //
        // Xerces Properties
        //

        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            final int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();

            if (suffixLength == Constants.SCHEMA_LOCATION.length() &&
                propertyId.endsWith(Constants.SCHEMA_LOCATION)) {
                return PropertyState.RECOGNIZED;
            }
            if (suffixLength == Constants.SCHEMA_NONS_LOCATION.length() &&
                propertyId.endsWith(Constants.SCHEMA_NONS_LOCATION)) {
                return PropertyState.RECOGNIZED;
            }
        }

        if (propertyId.startsWith(Constants.JAXP_PROPERTY_PREFIX)) {
            final int suffixLength = propertyId.length() - Constants.JAXP_PROPERTY_PREFIX.length();

            if (suffixLength == Constants.SCHEMA_SOURCE.length() &&
                propertyId.endsWith(Constants.SCHEMA_SOURCE)) {
                return PropertyState.RECOGNIZED;
            }
        }

        //
        // Not recognized
        //

        return super.checkProperty(propertyId);

    } // checkProperty(String)

} // class StandardParserConfiguration
