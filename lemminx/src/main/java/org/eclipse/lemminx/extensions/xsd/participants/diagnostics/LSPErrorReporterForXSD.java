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

import java.util.List;
import java.util.Map;

import org.apache.xerces.xni.XMLLocator;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.participants.XMLSchemaErrorCode;
import org.eclipse.lemminx.extensions.contentmodel.participants.XMLSyntaxErrorCode;
import org.eclipse.lemminx.extensions.xerces.AbstractReferencedGrammarLSPErrorReporter;
import org.eclipse.lemminx.extensions.xerces.ReferencedGrammarDiagnosticsInfo;
import org.eclipse.lemminx.extensions.xsd.participants.XSDErrorCode;
import org.eclipse.lemminx.extensions.xsd.utils.XSDUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Range;
import org.xml.sax.ErrorHandler;

/**
 * The SAX {@link ErrorHandler} gives just information of the offset where there
 * is an error. To improve highlight XML error, this class extends the Xerces
 * XML reporter to catch location, key, arguments which is helpful to adjust the
 * LSP range.
 *
 */
public class LSPErrorReporterForXSD extends AbstractReferencedGrammarLSPErrorReporter {

	private static final String XSD_DIAGNOSTIC_SOURCE = "xsd";

	public LSPErrorReporterForXSD(DOMDocument xmlDocument, List<Diagnostic> diagnostics,
			ContentModelManager contentModelManager, boolean hasRelatedInformation,
			Map<String, ReferencedGrammarDiagnosticsInfo> referencedGrammarDiagnosticsInfoCache) {
		super(XSD_DIAGNOSTIC_SOURCE, xmlDocument, diagnostics, contentModelManager, hasRelatedInformation,
				referencedGrammarDiagnosticsInfoCache);
	}

	@Override
	protected Range toLSPRange(XMLLocator location, String key, Object[] arguments, String message,
			DiagnosticSeverity diagnosticSeverity, boolean fatalError, DOMDocument document,
			String documentOrGrammarURI, boolean errorForDocument) {
		// try adjust positions for XSD error
		XSDErrorCode xsdCode = XSDErrorCode.get(key);
		if (xsdCode != null) {
			if (errorForDocument || XSDErrorCode.src_import_1_2.equals(xsdCode)
					|| XSDErrorCode.src_import_3_1.equals(xsdCode) || XSDErrorCode.src_import_3_2.equals(xsdCode)) {
				Range range = XSDErrorCode.toLSPRange(location, xsdCode, arguments, document);
				if (range != null) {
					return range;
				}
			} else {
				fillReferencedGrammarDiagnostic(location, key, arguments, message, diagnosticSeverity, fatalError,
						document.getResolverExtensionManager(), null, null, null, xsdCode, documentOrGrammarURI);
				return NO_RANGE;
			}
		}
		// try adjust positions for XML syntax error
		XMLSyntaxErrorCode syntaxCode = XMLSyntaxErrorCode.get(key);
		if (syntaxCode != null) {
			if (errorForDocument) {
				Range range = XMLSyntaxErrorCode.toLSPRange(location, syntaxCode, arguments, document);
				if (range != null) {
					return range;
				}
			} else {
				fillReferencedGrammarDiagnostic(location, key, arguments, message, diagnosticSeverity, fatalError,
						document.getResolverExtensionManager(), syntaxCode, null, null, null, documentOrGrammarURI);
				return NO_RANGE;
			}
		}
		// try adjust positions for XML schema error
		XMLSchemaErrorCode schemaCode = XMLSchemaErrorCode.get(key);
		if (schemaCode != null) {
			if (errorForDocument) {
				Range range = XMLSchemaErrorCode.toLSPRange(location, schemaCode, arguments, document);
				if (range != null) {
					return range;
				}
			} else {
				fillReferencedGrammarDiagnostic(location, key, arguments, message, diagnosticSeverity, fatalError,
						document.getResolverExtensionManager(), null, schemaCode, null, null, documentOrGrammarURI);
				return NO_RANGE;
			}
		}

		return null;
	}

	@Override
	protected Range getReferencedGrammarRange(String grammarURI) {
		// search grammar uri from xs:include/@schemaLocation or
		// xs:import/@schemaLocation which reference the grammar URI
		DOMAttr schemaLocationAttr = XSDUtils.findSchemaLocationAttrByURI(getDOMDocument(), grammarURI);
		if (schemaLocationAttr != null) {
			return XMLPositionUtility.selectAttributeValue(schemaLocationAttr);
		}
		// Set the error range in the root start tag
		return XMLPositionUtility.selectRootStartTag(getDOMDocument());
	}

}
