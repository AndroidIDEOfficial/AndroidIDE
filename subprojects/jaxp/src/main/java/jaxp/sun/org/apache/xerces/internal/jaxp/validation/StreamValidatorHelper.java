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

package jaxp.sun.org.apache.xerces.internal.jaxp.validation;

import jaxp.sun.org.apache.xerces.internal.impl.Constants;
import jaxp.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import jaxp.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
import jaxp.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
import jaxp.sun.org.apache.xerces.internal.parsers.XML11Configuration;
import jaxp.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import jaxp.sun.org.apache.xerces.internal.xni.XNIException;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import java.io.IOException;
import java.lang.ref.SoftReference;
import jaxp.xml.XMLConstants;
import jaxp.xml.transform.Result;
import jaxp.xml.transform.Source;
import jaxp.xml.transform.TransformerConfigurationException;
import jaxp.xml.transform.TransformerFactory;
import jaxp.xml.transform.TransformerFactoryConfigurationError;
import jaxp.xml.transform.sax.SAXTransformerFactory;
import jaxp.xml.transform.sax.TransformerHandler;
import jaxp.xml.transform.stream.StreamResult;
import jaxp.xml.transform.stream.StreamSource;
import org.xml.sax.SAXException;

/**
 * <p>A validator helper for <code>StreamSource</code>s.</p>
 *
 * @author Michael Glavassevich, IBM
 * @author <a href="mailto:Sunitha.Reddy@Sun.com">Sunitha Reddy</a>
 * @version $Id: StreamValidatorHelper.java,v 1.7 2010-11-01 04:40:08 joehw Exp $
 */
final class StreamValidatorHelper implements ValidatorHelper {

    // feature identifiers

    /** Feature identifier: parser settings. */
    private static final String PARSER_SETTINGS =
        Constants.XERCES_FEATURE_PREFIX + Constants.PARSER_SETTINGS;

    // property identifiers

    /** Property identifier: entity resolver. */
    private static final String ENTITY_RESOLVER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_RESOLVER_PROPERTY;

    /** Property identifier: error handler. */
    private static final String ERROR_HANDLER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_HANDLER_PROPERTY;

    /** Property identifier: error reporter. */
    private static final String ERROR_REPORTER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;

    /** Property identifier: XML Schema validator. */
    private static final String SCHEMA_VALIDATOR =
        Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_VALIDATOR_PROPERTY;

    /** Property identifier: symbol table. */
    private static final String SYMBOL_TABLE =
        Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;

    /** Property identifier: validation manager. */
    private static final String VALIDATION_MANAGER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.VALIDATION_MANAGER_PROPERTY;

    private static final String DEFAULT_TRANSFORMER_IMPL = "jaxp.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl";

    /** Property id: security manager. */
    private static final String SECURITY_MANAGER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.SECURITY_MANAGER_PROPERTY;

    //
    // Data
    //

    /** SoftReference to parser configuration. **/
    private SoftReference fConfiguration = new SoftReference(null);

    /** Schema validator. **/
    private XMLSchemaValidator fSchemaValidator;

    /** Component manager. **/
    private XMLSchemaValidatorComponentManager fComponentManager;

    private ValidatorHandlerImpl handler = null;

    public StreamValidatorHelper(XMLSchemaValidatorComponentManager componentManager) {
        fComponentManager = componentManager;
        fSchemaValidator = (XMLSchemaValidator) fComponentManager.getProperty(SCHEMA_VALIDATOR);
    }

    public void validate(Source source, Result result)
        throws SAXException, IOException {
        if (result == null || result instanceof StreamResult) {
            final StreamSource streamSource = (StreamSource) source;
            TransformerHandler identityTransformerHandler ;

            if( result!=null ) {
                try {
                    SAXTransformerFactory tf = fComponentManager.getFeature(Constants.ORACLE_FEATURE_SERVICE_MECHANISM) ?
                                    (SAXTransformerFactory)SAXTransformerFactory.newInstance()
                                    : (SAXTransformerFactory) TransformerFactory.newInstance(DEFAULT_TRANSFORMER_IMPL, StreamValidatorHelper.class.getClassLoader());
                    identityTransformerHandler = tf.newTransformerHandler();
                } catch (TransformerConfigurationException e) {
                    throw new TransformerFactoryConfigurationError(e);
                }

                handler = new ValidatorHandlerImpl(fComponentManager);
                handler.setContentHandler(identityTransformerHandler);
                identityTransformerHandler.setResult(result);
            }

            XMLInputSource input = new XMLInputSource(streamSource.getPublicId(), streamSource.getSystemId(), null);
            input.setByteStream(streamSource.getInputStream());
            input.setCharacterStream(streamSource.getReader());

            // Gets the parser configuration. We'll create and initialize a new one, if we
            // haven't created one before or if the previous one was garbage collected.
            XMLParserConfiguration config = (XMLParserConfiguration) fConfiguration.get();
            if (config == null) {
                config = initialize();
            }
            // If settings have changed on the component manager, refresh the error handler and entity resolver.
            else if (fComponentManager.getFeature(PARSER_SETTINGS)) {
                config.setProperty(ENTITY_RESOLVER, fComponentManager.getProperty(ENTITY_RESOLVER));
                config.setProperty(ERROR_HANDLER, fComponentManager.getProperty(ERROR_HANDLER));
            }

            // prepare for parse
            fComponentManager.reset();
            fSchemaValidator.setDocumentHandler(handler);

            try {
                config.parse(input);
            }
            catch (XMLParseException e) {
                throw Util.toSAXParseException(e);
            }
            catch (XNIException e) {
                throw Util.toSAXException(e);
            }
            return;
        }
        throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(),
                "SourceResultMismatch",
                new Object [] {source.getClass().getName(), result.getClass().getName()}));
    }

    private XMLParserConfiguration initialize() {
        XML11Configuration config = new XML11Configuration();
        if (fComponentManager.getFeature(XMLConstants.FEATURE_SECURE_PROCESSING)) {
            config.setProperty(SECURITY_MANAGER, new XMLSecurityManager());
        }
        config.setProperty(ENTITY_RESOLVER, fComponentManager.getProperty(ENTITY_RESOLVER));
        config.setProperty(ERROR_HANDLER, fComponentManager.getProperty(ERROR_HANDLER));
        XMLErrorReporter errorReporter = (XMLErrorReporter) fComponentManager.getProperty(ERROR_REPORTER);
        config.setProperty(ERROR_REPORTER, errorReporter);
        // add message formatters
        if (errorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN) == null) {
            XMLMessageFormatter xmft = new XMLMessageFormatter();
            errorReporter.putMessageFormatter(XMLMessageFormatter.XML_DOMAIN, xmft);
            errorReporter.putMessageFormatter(XMLMessageFormatter.XMLNS_DOMAIN, xmft);
        }
        config.setProperty(SYMBOL_TABLE, fComponentManager.getProperty(SYMBOL_TABLE));
        config.setProperty(VALIDATION_MANAGER, fComponentManager.getProperty(VALIDATION_MANAGER));
        config.setDocumentHandler(fSchemaValidator);
        config.setDTDHandler(null);
        config.setDTDContentModelHandler(null);
        config.setProperty(Constants.XML_SECURITY_PROPERTY_MANAGER,
                fComponentManager.getProperty(Constants.XML_SECURITY_PROPERTY_MANAGER));
        config.setProperty(Constants.SECURITY_MANAGER,
                fComponentManager.getProperty(Constants.SECURITY_MANAGER));
        fConfiguration = new SoftReference(config);
        return config;
    }

} // StreamValidatorHelper
