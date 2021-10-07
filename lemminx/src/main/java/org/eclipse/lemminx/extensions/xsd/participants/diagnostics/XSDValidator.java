/**
 *  Copyright (c) 2018 Angelo ZERR
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.extensions.xsd.participants.diagnostics;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.parsers.XMLGrammarPreparser;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLValidationSettings;
import org.eclipse.lemminx.extensions.xerces.AbstractLSPErrorReporter;
import org.eclipse.lemminx.extensions.xerces.ReferencedGrammarDiagnosticsInfo;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XSD validator utilities class.
 *
 */
public class XSDValidator {

	private static final Logger LOGGER = Logger.getLogger(XSDValidator.class.getName());

	private static boolean canCustomizeReporter = true;

	public static void doDiagnostics(DOMDocument document, XMLEntityResolver entityResolver,
			List<Diagnostic> diagnostics, XMLValidationSettings validationSettings,
			ContentModelManager contentModelManager, CancelChecker monitor) {

		Map<String, ReferencedGrammarDiagnosticsInfo> referencedGrammarDiagnosticsInfoCache = new HashMap<>();
		// When referenced grammar (XSD, DTD) have an error (ex : syntax error), the
		// error must be reported.
		// We create a reporter for grammar since Xerces reporter stores the XMLLocator
		// for XML and Grammar.
		LSPErrorReporterForXSD reporterForXSD = new LSPErrorReporterForXSD(document, diagnostics, contentModelManager,
				validationSettings != null ? validationSettings.isRelatedInformation() : false,
				referencedGrammarDiagnosticsInfoCache);

		try {
			XMLGrammarPreparser grammarPreparser = new LSPXMLGrammarPreparser();
			XMLSchemaLoader schemaLoader = createSchemaLoader(reporterForXSD);

			grammarPreparser.registerPreparser(XMLGrammarDescription.XML_SCHEMA, schemaLoader);

			grammarPreparser.setProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.XMLGRAMMAR_POOL_PROPERTY,
					new XMLGrammarPoolImpl());
			grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE,
					false);
			grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.NAMESPACES_FEATURE, true);
			grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.NAMESPACE_PREFIXES_FEATURE, true);
			grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.VALIDATION_FEATURE, true);
			grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_VALIDATION_FEATURE, true);

			grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.EXTERNAL_GENERAL_ENTITIES_FEATURE,
					true);
			grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.EXTERNAL_PARAMETER_ENTITIES_FEATURE,
					true);
			grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.WARN_ON_DUPLICATE_ATTDEF_FEATURE,
					true);

			// Add LSP content handler to stop XML parsing if monitor is canceled.
			// grammarPreparser.setContentHandler(new LSPContentHandler(monitor));

			// Add LSP error reporter to fill LSP diagnostics from Xerces errors
			grammarPreparser.setProperty("http://apache.org/xml/properties/internal/error-reporter", reporterForXSD);

			if (entityResolver != null) {
				grammarPreparser.setEntityResolver(entityResolver);
			}

			String content = document.getText();
			String uri = document.getDocumentURI();
			InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
			XMLInputSource is = new XMLInputSource(null, uri, uri, inputStream, null);
			grammarPreparser.getLoader(XMLGrammarDescription.XML_SCHEMA);
			grammarPreparser.preparseGrammar(XMLGrammarDescription.XML_SCHEMA, is);
		} catch (IOException | CancellationException | XMLParseException exception) {
			// ignore error
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unexpected XSDValidator error", e);
		} finally {
			reporterForXSD.endReport();
		}
	}

	/**
	 * Create the XML Schema loader to use to validate the XML Schema.
	 *
	 * @param reporter the lsp reporter.
	 * @return the XML Schema loader to use to validate the XML Schema.
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	private static XMLSchemaLoader createSchemaLoader(XMLErrorReporter reporter) {
		XMLSchemaLoader schemaLoader = new XMLSchemaLoader();

		// To validate XML syntax for XML Schema, we need to use the Xerces Reporter
		// (XMLErrorReporter)
		// (and not the Xerces XML ErrorHandler because we need the arguments array to
		// retrieve the attribut e name, element name, etc)

		// Xerces XSD validator can work with Xerces reporter for XSD error but not for
		// XML syntax (only XMLErrorHandler is allowed).
		// To fix this problem, we set the Xerces reporter with Java Reflection.
		if (canCustomizeReporter) {
			canCustomizeReporter = AbstractLSPErrorReporter.initializeReporter(schemaLoader, reporter);
		}
		return schemaLoader;
	}

}
