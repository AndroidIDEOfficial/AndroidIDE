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
 * Copyright 2001, 2002,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jaxp.sun.org.apache.xerces.internal.impl.Constants;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;

/**
 * This class implements the basic operations for managing parser
 * configuration features and properties. This utility class can
 * be used as a base class for parser configurations or separately
 * to encapsulate a number of parser settings as a component
 * manager.
 * <p>
 * This class can be constructed with a "parent" settings object
 * (in the form of an <code>XMLComponentManager</code>) that allows
 * parser configuration settings to be "chained" together.
 *
 * @author Andy Clark, IBM
 *
 * @version $Id: ParserConfigurationSettings.java,v 1.6 2010-11-01 04:40:14 joehw Exp $
 */
public class ParserConfigurationSettings
    implements XMLComponentManager {

        protected static final String PARSER_SETTINGS =
                        Constants.XERCES_FEATURE_PREFIX + Constants.PARSER_SETTINGS;

    //
    // Data
    //

    // data

    /** Recognized properties. */
    protected Set<String> fRecognizedProperties;

    /** Properties. */
    protected Map<String, Object> fProperties;

    /** Recognized features. */
    protected Set<String> fRecognizedFeatures;

    /** Features. */
    protected Map<String, Boolean> fFeatures;

    /** Parent parser configuration settings. */
    protected XMLComponentManager fParentSettings;

    //
    // Constructors
    //

    /** Default Constructor. */
    public ParserConfigurationSettings() {
        this(null);
    } // <init>()

    /**
     * Constructs a parser configuration settings object with a
     * parent settings object.
     */
    public ParserConfigurationSettings(XMLComponentManager parent) {

        // create storage for recognized features and properties
        fRecognizedFeatures = new HashSet<String>();
        fRecognizedProperties = new HashSet<String>();

        // create table for features and properties
        fFeatures = new HashMap<String, Boolean>();
        fProperties = new HashMap<String, Object>();

        // save parent
        fParentSettings = parent;

    } // <init>(XMLComponentManager)

    //
    // XMLParserConfiguration methods
    //

    /**
     * Allows a parser to add parser specific features to be recognized
     * and managed by the parser configuration.
     *
     * @param featureIds An array of the additional feature identifiers
     *                   to be recognized.
     */
    public void addRecognizedFeatures(String[] featureIds) {

        // add recognized features
        int featureIdsCount = featureIds != null ? featureIds.length : 0;
        for (int i = 0; i < featureIdsCount; i++) {
            String featureId = featureIds[i];
            if (!fRecognizedFeatures.contains(featureId)) {
                fRecognizedFeatures.add(featureId);
            }
        }

    } // addRecognizedFeatures(String[])

    /**
     * Set the state of a feature.
     *
     * Set the state of any feature in a SAX2 parser.  The parser
     * might not recognize the feature, and if it does recognize
     * it, it might not be able to fulfill the request.
     *
     * @param featureId The unique identifier (URI) of the feature.
     * @param state The requested state of the feature (true or false).
     *
     * @exception XMLConfigurationException If the
     *            requested feature is not known.
     */
    public void setFeature(String featureId, boolean state)
        throws XMLConfigurationException {

        // check and store
        FeatureState checkState = checkFeature(featureId);
        if (checkState.isExceptional()) {
            throw new XMLConfigurationException(checkState.status, featureId);
        }

        fFeatures.put(featureId, state);
    } // setFeature(String,boolean)

    /**
     * Allows a parser to add parser specific properties to be recognized
     * and managed by the parser configuration.
     *
     * @param propertyIds An array of the additional property identifiers
     *                    to be recognized.
     */
    public void addRecognizedProperties(String[] propertyIds) {
        fRecognizedProperties.addAll(Arrays.asList(propertyIds));
    } // addRecognizedProperties(String[])

    /**
     * setProperty
     *
     * @param propertyId
     * @param value
     * @exception XMLConfigurationException If the
     *            requested feature is not known.
     */
    public void setProperty(String propertyId, Object value)
        throws XMLConfigurationException {

        // check and store
        PropertyState checkState = checkProperty(propertyId);
        if (checkState.isExceptional()) {
            throw new XMLConfigurationException(checkState.status, propertyId);
        }
        fProperties.put(propertyId, value);

    } // setProperty(String,Object)

    //
    // XMLComponentManager methods
    //

    /**
     * Returns the state of a feature.
     *
     * @param featureId The feature identifier.
                 * @return true if the feature is supported
     *
     * @throws XMLConfigurationException Thrown for configuration error.
     *                                   In general, components should
     *                                   only throw this exception if
     *                                   it is <strong>really</strong>
     *                                   a critical error.
     */
    public final boolean getFeature(String featureId)
        throws XMLConfigurationException {

        FeatureState state = getFeatureState(featureId);
        if (state.isExceptional()) {
            throw new XMLConfigurationException(state.status, featureId);
        }
        return state.state;
    } // getFeature(String):boolean

    public final boolean getFeature(String featureId, boolean defaultValue) {
        FeatureState state = getFeatureState(featureId);
        if (state.isExceptional()) {
            return defaultValue;
        }
        return state.state;
    }

    public FeatureState getFeatureState(String featureId) {
        Boolean state = (Boolean) fFeatures.get(featureId);

        if (state == null) {
            FeatureState checkState = checkFeature(featureId);
            if (checkState.isExceptional()) {
                return checkState;
            }
            return FeatureState.is(false);
        }
        return FeatureState.is(state);
    }

    /**
     * Returns the value of a property.
     *
     * @param propertyId The property identifier.
                 * @return the value of the property
     *
     * @throws XMLConfigurationException Thrown for configuration error.
     *                                   In general, components should
     *                                   only throw this exception if
     *                                   it is <strong>really</strong>
     *                                   a critical error.
     */
    public final Object getProperty(String propertyId)
        throws XMLConfigurationException {

        PropertyState state = getPropertyState(propertyId);
        if (state.isExceptional()) {
            throw new XMLConfigurationException(state.status, propertyId);
        }

        return state.state;
    } // getProperty(String):Object

    public final Object getProperty(String propertyId, Object defaultValue) {
        PropertyState state = getPropertyState(propertyId);
        if (state.isExceptional()) {
            return defaultValue;
        }

        return state.state;
    }

    public PropertyState getPropertyState(String propertyId) {
        Object propertyValue = fProperties.get(propertyId);

        if (propertyValue == null) {
            PropertyState state = checkProperty(propertyId);
            if (state.isExceptional()) {
                return state;
            }
        }

        return PropertyState.is(propertyValue);
    }

    //
    // Protected methods
    //

    /**
     * Check a feature. If feature is known and supported, this method simply
     * returns. Otherwise, the appropriate exception is thrown.
     *
     * @param featureId The unique identifier (URI) of the feature.
     *
     * @exception XMLConfigurationException If the
     *            requested feature is not known.
     */
    protected FeatureState checkFeature(String featureId)
        throws XMLConfigurationException {

        // check feature
        if (!fRecognizedFeatures.contains(featureId)) {
            if (fParentSettings != null) {
                return fParentSettings.getFeatureState(featureId);
            }
            else {
                return FeatureState.NOT_RECOGNIZED;
            }
        }

        // TODO: reasonable default?
        return FeatureState.RECOGNIZED;
    } // checkFeature(String)

    /**
     * Check a property. If the property is known and supported, this method
     * simply returns. Otherwise, the appropriate exception is thrown.
     *
     * @param propertyId The unique identifier (URI) of the property
     *                   being set.
     * @exception XMLConfigurationException If the
     *            requested feature is not known.
     */
    protected PropertyState checkProperty(String propertyId)
        throws XMLConfigurationException {

        // check property
        if (!fRecognizedProperties.contains(propertyId)) {
            if (fParentSettings != null) {
                PropertyState state = fParentSettings.getPropertyState(propertyId);
                if (state.isExceptional()) {
                    return state;
                }
            }
            else {
                return PropertyState.NOT_RECOGNIZED;
            }
        }
        return PropertyState.RECOGNIZED;
    } // checkProperty(String)

} // class ParserConfigurationSettings
