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
package org.eclipse.lemminx.extensions.xerces;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.impl.xs.XSMessageFormatter;
import org.apache.xerces.impl.xs.opti.SchemaDOMParser;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.util.MessageFormatter;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParseException;
import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.TextDocument;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.participants.AggregateRelatedInfoFinder;
import org.eclipse.lemminx.extensions.xerces.xmlmodel.msg.XMLModelMessageFormatter;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticRelatedInformation;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.xml.sax.ErrorHandler;

/**
 * The SAX {@link ErrorHandler} gives just information of the offset where there
 * is an error. To improve highlight XML error, this class extends the Xerces
 * XML reporter to catch location, key, arguments which is helpful to adjust the
 * LSP range.
 *
 */
public abstract class AbstractLSPErrorReporter extends XMLErrorReporter {

	private static final Logger LOGGER = Logger.getLogger(AbstractLSPErrorReporter.class.getName());

	protected final static Range NO_RANGE = new Range();
	private final DOMDocument xmlDocument;
	private final List<Diagnostic> diagnostics;

	private final String source;
	private boolean hasRelatedInfo;

	public AbstractLSPErrorReporter(String source, DOMDocument xmlDocument, List<Diagnostic> diagnostics, boolean hasRelatedInfo) {
		this.source = source;
		this.xmlDocument = xmlDocument;
		this.diagnostics = diagnostics;
		this.hasRelatedInfo = hasRelatedInfo;
		XMLMessageFormatter xmft = new XMLMessageFormatter();
		super.putMessageFormatter(XMLMessageFormatter.XML_DOMAIN, xmft);
		super.putMessageFormatter(XMLMessageFormatter.XMLNS_DOMAIN, xmft);
		super.putMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN, new LSPMessageFormatter());
		super.putMessageFormatter(XMLModelMessageFormatter.XML_MODEL_DOMAIN, new XMLModelMessageFormatter());
	}

	public String reportError(XMLLocator location, String domain, String key, Object[] arguments, short severity,
			Exception exception) throws XNIException {
		// format message
		MessageFormatter messageFormatter = getMessageFormatter(domain);
		String message;
		if (messageFormatter != null) {
			message = messageFormatter.formatMessage(fLocale, key, arguments);
		} else {
			StringBuilder str = new StringBuilder();
			str.append(domain);
			str.append('#');
			str.append(key);
			int argCount = arguments != null ? arguments.length : 0;
			if (argCount > 0) {
				str.append('?');
				for (int i = 0; i < argCount; i++) {
					str.append(arguments[i]);
					if (i < argCount - 1) {
						str.append('&');
					}
				}
			}
			message = str.toString();
		}

		boolean fatalError = severity == SEVERITY_FATAL_ERROR;
		DiagnosticSeverity diagnosticSeverity = toLSPSeverity(severity);
		Range adjustedRange = internalToLSPRange(location, key, arguments, message, diagnosticSeverity, fatalError,
				xmlDocument);
		List<DiagnosticRelatedInformation> relatedInformations = null;
		if (adjustedRange == null || NO_RANGE.equals(adjustedRange)) {
			return null;
		}
		if (hasRelatedInfo) {
			try {
				relatedInformations = AggregateRelatedInfoFinder.getInstance().findRelatedInformation(xmlDocument.offsetAt(adjustedRange.getStart()), key, xmlDocument);
			} catch (BadLocationException e) {
				LOGGER.severe("Passed bad Range: " + e);
			}
		}
		if (addDiagnostic(adjustedRange, message, diagnosticSeverity, key, relatedInformations) == null) {
			return null;
		}
		if (fatalError && !fContinueAfterFatalError) {
			XMLParseException parseException = (exception != null) ? new XMLParseException(location, message, exception)
					: new XMLParseException(location, message);
			throw parseException;
		}
		return message;
	}

	protected boolean isIgnoreFatalError(String key) {
		return false;
	}

	public Diagnostic addDiagnostic(Range adjustedRange, String message, DiagnosticSeverity severity, String key, List<DiagnosticRelatedInformation> relatedInformation) {
		Diagnostic d = new Diagnostic(adjustedRange, message, severity, source, key);
		if (hasRelatedInfo && relatedInformation != null && relatedInformation.size() > 0){
			d.setRelatedInformation(relatedInformation);
		}
		if (diagnostics.contains(d)) {
			return null;
		}
		// Fill diagnostic
		diagnostics.add(d);
		return d;
	}

	/**
	 * Returns the LSP diagnostic severity according the SAX severity.
	 *
	 * @param severity the SAX severity
	 * @return the LSP diagnostic severity according the SAX severity.
	 */
	private static DiagnosticSeverity toLSPSeverity(int severity) {
		switch (severity) {
		case SEVERITY_WARNING:
			return DiagnosticSeverity.Warning;
		default:
			return DiagnosticSeverity.Error;
		}
	}

	/**
	 * Create the LSP range from the SAX error.
	 *
	 * @param location           the Xerces location.
	 * @param key                the Xerces error key.
	 * @param arguments          the Xerces error arguments.
	 * @param message            the Xerces error message.
	 * @param diagnosticSeverity the the Xerces severity.
	 * @param fatalError         true if Xerces report the error as fatal and false
	 *                           otherwise.
	 * @param document           the DOM document.
	 * @return the LSP range from the SAX error.
	 */
	private Range internalToLSPRange(XMLLocator location, String key, Object[] arguments, String message,
			DiagnosticSeverity diagnosticSeverity, boolean fatalError, DOMDocument document) {
		if (location == null) {
			Position start = toLSPPosition(0, location, document.getTextDocument());
			Position end = toLSPPosition(0, location, document.getTextDocument());
			return new Range(start, end);
		}

		Range range = toLSPRange(location, key, arguments, message, diagnosticSeverity, fatalError, document);
		if (range != null) {
			return range;
		}
		return createDefaultRange(location, document);
	}

	protected Range createDefaultRange(XMLLocator location, DOMDocument document) {
		int startOffset = location.getCharacterOffset() - 1;
		int endOffset = location.getCharacterOffset() - 1;

		if (startOffset < 0 || endOffset < 0) {
			return null;
		}

		// Create LSP range
		Position start = toLSPPosition(startOffset, location, document.getTextDocument());
		Position end = toLSPPosition(endOffset, location, document.getTextDocument());
		return new Range(start, end);
	}

	/**
	 * Returns the range of the given error information, or {{@link #NO_RANGE} if
	 * diagnostic must not be created and null otherwise.
	 *
	 * @param location           the Xerces location.
	 * @param key                the Xerces error key.
	 * @param arguments          the Xerces error arguments.
	 * @param message            the Xerces error message.
	 * @param diagnosticSeverity the the Xerces severity.
	 * @param fatalError         true if Xerces report the error as fatal and false
	 *                           otherwise.
	 * @param document           the DOM document.
	 * @return
	 */
	protected abstract Range toLSPRange(XMLLocator location, String key, Object[] arguments, String message,
			DiagnosticSeverity diagnosticSeverity, boolean fatalError, DOMDocument document);

	/**
	 * Returns the LSP position from the SAX location.
	 *
	 * @param offset   the adjusted offset.
	 * @param location the original SAX location.
	 * @param document the text document.
	 * @return the LSP position from the SAX location.
	 */
	private static Position toLSPPosition(int offset, XMLLocator location, TextDocument document) {
		if (location != null && offset == location.getCharacterOffset() - 1) {
			return new Position(location.getLineNumber() - 1, location.getColumnNumber() - 1);
		}
		try {
			return document.positionAt(offset);
		} catch (BadLocationException e) {
			return location != null ? new Position(location.getLineNumber() - 1, location.getColumnNumber() - 1) : null;
		}
	}

	/**
	 * Returns the DOM document which is validating.
	 *
	 * @return the DOM document which is validating.
	 */
	protected DOMDocument getDOMDocument() {
		return xmlDocument;
	}

	public static boolean initializeReporter(XMLSchemaValidator schemaValidator, XMLErrorReporter reporter) {
		try {
			XMLSchemaLoader schemaLoader = ReflectionUtils.getFieldValue(schemaValidator, "fSchemaLoader");
			return initializeReporter(schemaLoader, reporter);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while initializing XML error reporter", e);
		}
		return false;
	}

	public static boolean initializeReporter(XMLSchemaLoader schemaLoader, XMLErrorReporter reporter) {
		try {
			XSDHandler handler = ReflectionUtils.getFieldValue(schemaLoader, "fSchemaHandler");
			SchemaDOMParser domParser = ReflectionUtils.getFieldValue(handler, "fSchemaParser");
			domParser.setProperty("http://apache.org/xml/properties/internal/error-reporter", reporter);
			return true;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while initializing XML error reporter", e);
		}
		return false;
	}
}
