/**
 *  Copyright (c) 2018-2020 Angelo ZERR
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
package org.eclipse.lemminx.extensions.contentmodel.participants.diagnostics;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.util.URI.MalformedURIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMDocumentType;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.NoNamespaceSchemaLocation;
import org.eclipse.lemminx.dom.SchemaLocationHint;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.participants.XMLSyntaxErrorCode;
import org.eclipse.lemminx.extensions.contentmodel.settings.NamespacesEnabled;
import org.eclipse.lemminx.extensions.contentmodel.settings.SchemaEnabled;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLNamespacesSettings;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLSchemaSettings;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLValidationSettings;
import org.eclipse.lemminx.extensions.xerces.ReferencedGrammarDiagnosticsInfo;
import org.eclipse.lemminx.services.extensions.diagnostics.LSPContentHandler;
import org.eclipse.lemminx.uriresolver.CacheResourceDownloadingException;
import org.eclipse.lemminx.uriresolver.IExternalGrammarLocationProvider;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * XML validator utilities class.
 *
 */
public class XMLValidator {

	private static final Logger LOGGER = Logger.getLogger(XMLValidator.class.getName());

	public static void doDiagnostics(DOMDocument document, XMLEntityResolver entityResolver,
			List<Diagnostic> diagnostics, XMLValidationSettings validationSettings,
			ContentModelManager contentModelManager, CancelChecker monitor) {
		XMLGrammarPool grammarPool = contentModelManager.getGrammarPool();
		Map<String, ReferencedGrammarDiagnosticsInfo> referencedGrammarDiagnosticsInfoCache = new HashMap<>();
		final LSPErrorReporterForXML reporterForXML = new LSPErrorReporterForXML(document, diagnostics,
				contentModelManager, validationSettings != null ? validationSettings.isRelatedInformation() : false,
				referencedGrammarDiagnosticsInfoCache);
		// When referenced grammar (XSD, DTD) have an error (ex : syntax error), the
		// error must be reported.
		// We create a reporter for grammar since Xerces reporter stores the XMLLocator
		// for XML and Grammar.
		final LSPErrorReporterForXML reporterForGrammar = new LSPErrorReporterForXML(document, diagnostics,
				contentModelManager, validationSettings != null ? validationSettings.isRelatedInformation() : false,
				referencedGrammarDiagnosticsInfoCache);
		try {
			LSPXMLParserConfiguration configuration = new LSPXMLParserConfiguration(grammarPool,
					isDisableOnlyDTDValidation(document), reporterForXML, reporterForGrammar, validationSettings);

			if (entityResolver != null) {
				configuration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", entityResolver); //$NON-NLS-1$
			}

			SAXParser parser = new LSPSAXParser(document, reporterForXML, configuration, grammarPool);

			// Add LSP content handler to stop XML parsing if monitor is canceled.
			parser.setContentHandler(new LSPContentHandler(monitor));

			// warn if XML document is not bound to a grammar according the settings
			warnNoGrammar(document, diagnostics, validationSettings);
			// Update external grammar location (file association)
			updateExternalGrammarLocation(document, parser);

			boolean hasSchemaLocation = document.hasSchemaLocation();
			boolean hasNoNamespaceSchemaLocation = document.hasNoNamespaceSchemaLocation();
			boolean hasSchemaGrammar = hasSchemaLocation || hasNoNamespaceSchemaLocation
					|| hasExternalSchemaGrammar(document);
			boolean schemaValidationEnabled = (hasSchemaGrammar
					&& isSchemaValidationEnabled(document, validationSettings)
					|| (hasNoNamespaceSchemaLocation
							&& isNoNamespaceSchemaValidationEnabled(document, validationSettings)));
			parser.setFeature("http://apache.org/xml/features/validation/schema", schemaValidationEnabled); //$NON-NLS-1$

			boolean hasGrammar = document.hasDTD() || hasSchemaGrammar || document.hasExternalGrammar();
			if (hasSchemaGrammar && !schemaValidationEnabled) {
				hasGrammar = false;
			}
			parser.setFeature("http://xml.org/sax/features/validation", hasGrammar); //$NON-NLS-1$

			boolean namespacesValidationEnabled = isNamespacesValidationEnabled(document, validationSettings);
			parser.setFeature("http://xml.org/sax/features/namespace-prefixes", namespacesValidationEnabled); //$NON-NLS-1$
			parser.setFeature("http://xml.org/sax/features/namespaces", namespacesValidationEnabled); //$NON-NLS-1$

			// Parse XML
			String content = document.getText();
			String uri = document.getDocumentURI();
			parseXML(content, uri, parser);
		} catch (IOException | SAXException | CancellationException exception) {
			// ignore error
		} catch (CacheResourceDownloadingException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unexpected XMLValidator error", e);
		} finally {
			reporterForXML.endReport();
			reporterForGrammar.endReport();
		}
	}

	private static boolean isNamespacesValidationEnabled(DOMDocument document,
			XMLValidationSettings validationSettings) {
		if (validationSettings == null) {
			return true;
		}
		NamespacesEnabled enabled = NamespacesEnabled.always;
		XMLNamespacesSettings namespacesSettings = validationSettings.getNamespaces();
		if (namespacesSettings != null && namespacesSettings.getEnabled() != null) {
			enabled = namespacesSettings.getEnabled();
		}
		switch (enabled) {
		case always:
			return true;
		case never:
			return false;
		case onNamespaceEncountered:
			return document.hasNamespaces();
		default:
			return true;
		}
	}

	private static boolean isSchemaValidationEnabled(DOMDocument document, XMLValidationSettings validationSettings) {
		if (validationSettings == null) {
			return true;
		}
		SchemaEnabled enabled = SchemaEnabled.always;
		XMLSchemaSettings schemaSettings = validationSettings.getSchema();
		if (schemaSettings != null && schemaSettings.getEnabled() != null) {
			enabled = schemaSettings.getEnabled();
		}
		switch (enabled) {
		case always:
			return true;
		case never:
			return false;
		case onValidSchema:
			return isValidSchemaLocation(document);
		default:
			return true;
		}
	}

	/**
	 * Returns true if the given DOM document declares a xsi:schemaLocation hint for
	 * the document root element is valid and false otherwise.
	 * 
	 * The xsi:schemaLocation is valid if:
	 * 
	 * <ul>
	 * <li>xsi:schemaLocation defines an URI for the namespace of the document
	 * element.</li>
	 * <li>the URI can be opened</li>
	 * </ul>
	 * 
	 * @param document the DOM document.
	 * @return true if the given DOM document declares a xsi:schemaLocation hint for
	 *         the document root element is valid and false otherwise.
	 */
	private static boolean isValidSchemaLocation(DOMDocument document) {
		if (!document.hasSchemaLocation()) {
			return false;
		}
		String namespaceURI = document.getNamespaceURI();
		SchemaLocationHint hint = document.getSchemaLocation().getLocationHint(namespaceURI);
		if (hint == null) {
			return false;
		}
		String location = hint.getHint();
		return isValidLocation(document.getDocumentURI(), location);
	}

	private static boolean isNoNamespaceSchemaValidationEnabled(DOMDocument document,
			XMLValidationSettings validationSettings) {
		if (validationSettings == null) {
			return true;
		}
		SchemaEnabled enabled = SchemaEnabled.always;
		XMLSchemaSettings schemaSettings = validationSettings.getSchema();
		if (schemaSettings != null && schemaSettings.getEnabled() != null) {
			enabled = schemaSettings.getEnabled();
		}
		switch (enabled) {
		case always:
			return true;
		case never:
			return false;
		case onValidSchema:
			return isValidNoNamespaceSchemaLocation(document);
		default:
			return true;
		}
	}

	/**
	 * Returns true if the given DOM document declares a
	 * xsi:noNamespaceSchemaLocation which is valid and false otherwise.
	 * 
	 * The xsi:noNamespaceSchemaLocation is valid if:
	 * 
	 * <ul>
	 * <li>xsi:noNamespaceSchemaLocation defines an URI.</li>
	 * <li>the URI can be opened</li>
	 * </ul>
	 * 
	 * @param document the DOM document.
	 * @return true if the given DOM document declares a xsi:schemaLocation hint for
	 *         the document root element is valid and false otherwise.
	 */
	private static boolean isValidNoNamespaceSchemaLocation(DOMDocument document) {
		NoNamespaceSchemaLocation noNamespaceSchemaLocation = document.getNoNamespaceSchemaLocation();
		if (noNamespaceSchemaLocation == null) {
			return false;
		}
		String location = noNamespaceSchemaLocation.getLocation();
		return isValidLocation(document.getDocumentURI(), location);
	}

	private static boolean isValidLocation(String documentURI, String location) {
		String resolvedLocation = getResolvedLocation(documentURI, location);
		if (resolvedLocation == null) {
			return false;
		}
		try (InputStream is = new URL(resolvedLocation).openStream()) {
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static String getResolvedLocation(String documentURI, String location) {
		if (StringUtils.isBlank(location)) {
			return null;
		}
		try {
			return XMLEntityManager.expandSystemId(location, documentURI, false);
		} catch (MalformedURIException e) {
			return location;
		}
	}

	private static boolean hasExternalSchemaGrammar(DOMDocument document) {
		if (document.getExternalGrammarFromNamespaceURI() != null) {
			return true;
		}
		Map<String, String> externalGrammarLocation = document.getExternalGrammarLocation();
		if (externalGrammarLocation == null) {
			return false;
		}
		return externalGrammarLocation.containsKey(IExternalGrammarLocationProvider.NO_NAMESPACE_SCHEMA_LOCATION)
				|| externalGrammarLocation.containsKey(IExternalGrammarLocationProvider.SCHEMA_LOCATION);
	}

	private static void parseXML(String content, String uri, SAXParser parser) throws SAXException, IOException {
		InputSource inputSource = new InputSource();
		inputSource.setCharacterStream(new StringReader(content));
		inputSource.setSystemId(uri);
		parser.parse(inputSource);
	}

	/**
	 * Returns true is DTD validation must be disabled and false otherwise.
	 * 
	 * @param document the DOM document
	 * @return true is DTD validation must be disabled and false otherwise.
	 */
	private static boolean isDisableOnlyDTDValidation(DOMDocument document) {
		Map<String, String> externalGrammarLocation = document.getExternalGrammarLocation();
		if (externalGrammarLocation != null
				&& externalGrammarLocation.containsKey(IExternalGrammarLocationProvider.DOCTYPE)) {
			return true;
		}

		// When XML declares a DOCTYPE only to define entities like
		// <!DOCTYPE root [
		// <!ENTITY foo "Bar">
		// ]>
		// Xerces try to validate the XML and report an error on each XML elements
		// because they are not declared in the DOCTYPE.
		// In this case, DTD validation must be disabled.
		if (!document.hasDTD()) {
			return false;
		}
		DOMDocumentType docType = document.getDoctype();
		if (docType.getKindNode() != null) {
			return false;
		}
		// Disable the DTD validation only if there are not <!ELEMENT or an <!ATTRLIST
		return !docType.getChildren().stream().anyMatch(node -> node.isDTDElementDecl() || node.isDTDAttListDecl());
	}

	/**
	 * Warn if XML document is not bound to a grammar according the settings
	 * 
	 * @param document           the XML document
	 * @param diagnostics        the diagnostics list to populate
	 * @param validationSettings the settings to use to know the severity of warn.
	 */
	private static void warnNoGrammar(DOMDocument document, List<Diagnostic> diagnostics,
			XMLValidationSettings validationSettings) {
		boolean hasGrammar = document.hasGrammar();
		if (hasGrammar) {
			return;
		}
		// By default "hint" settings.
		DiagnosticSeverity severity = XMLValidationSettings.getNoGrammarSeverity(validationSettings);
		if (severity == null) {
			// "ignore" settings
			return;
		}
		if (!hasGrammar) {
			// No grammar, add a warn diagnostic with the severity coming from the settings.
			Range range = null;
			DOMElement documentElement = document.getDocumentElement();
			if (documentElement != null) {
				range = XMLPositionUtility.selectStartTagName(documentElement);
			}
			if (range == null) {
				range = new Range(new Position(0, 0), new Position(0, 0));
			}
			diagnostics.add(new Diagnostic(range, "No grammar constraints (DTD or XML Schema).", severity,
					document.getDocumentURI(), XMLSyntaxErrorCode.NoGrammarConstraints.name()));
		}
	}

	private static void updateExternalGrammarLocation(DOMDocument document, SAXParser reader)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		Map<String, String> externalGrammarLocation = document.getExternalGrammarLocation();
		if (externalGrammarLocation != null) {
			String xsd = externalGrammarLocation.get(IExternalGrammarLocationProvider.NO_NAMESPACE_SCHEMA_LOCATION);
			if (xsd != null) {
				// Try to get the xmlns attribute (default namespace) value from the DOM
				// document
				String defaultNamespace = null;
				DOMElement documentElement = document.getDocumentElement();
				if (documentElement != null) {
					defaultNamespace = documentElement.getAttribute(DOMAttr.XMLNS_ATTR);
				}
				if (StringUtils.isEmpty(defaultNamespace)) {
					// The DOM document has no namespace, we consider that it's the same thing than
					// xsi:noNamespaceSchemaLocation
					String noNamespaceSchemaLocation = xsd;
					reader.setProperty(IExternalGrammarLocationProvider.NO_NAMESPACE_SCHEMA_LOCATION,
							noNamespaceSchemaLocation);
				} else {
					// The DOM document has namespace, we consider that it's the same thing than
					// xsi:schemaLocation
					String schemaLocation = defaultNamespace + " " + xsd;
					reader.setProperty(IExternalGrammarLocationProvider.SCHEMA_LOCATION, schemaLocation);
				}
			} else {
				String doctype = externalGrammarLocation.get(IExternalGrammarLocationProvider.DOCTYPE);
				if (doctype != null) {
					reader.setProperty(IExternalGrammarLocationProvider.DOCTYPE, doctype);
				}
			}
		}
	}
}
