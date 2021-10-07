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
package org.eclipse.lemminx.extensions.contentmodel.participants.diagnostics;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xerces.xni.XMLLocator;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMRange;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.model.ReferencedGrammarInfo;
import org.eclipse.lemminx.extensions.contentmodel.participants.DTDErrorCode;
import org.eclipse.lemminx.extensions.contentmodel.participants.XMLSchemaErrorCode;
import org.eclipse.lemminx.extensions.contentmodel.participants.XMLSyntaxErrorCode;
import org.eclipse.lemminx.extensions.xerces.AbstractReferencedGrammarLSPErrorReporter;
import org.eclipse.lemminx.extensions.xerces.ReferencedGrammarDiagnosticsInfo;
import org.eclipse.lemminx.extensions.xsd.participants.XSDErrorCode;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Range;

/**
 * LSP error reporter for XML syntax and error grammar (XML Schema/DTD).
 *
 */
public class LSPErrorReporterForXML extends AbstractReferencedGrammarLSPErrorReporter {

	private static final String XML_DIAGNOSTIC_SOURCE = "xml";

	private Set<ReferencedGrammarInfo> referencedGrammars;

	public LSPErrorReporterForXML(DOMDocument xmlDocument, List<Diagnostic> diagnostics,
			ContentModelManager contentModelManager, boolean hasRelatedInformation,
			Map<String, ReferencedGrammarDiagnosticsInfo> referencedGrammarDiagnosticsInfoCache) {
		super(XML_DIAGNOSTIC_SOURCE, xmlDocument, diagnostics, contentModelManager, hasRelatedInformation,
				referencedGrammarDiagnosticsInfoCache);
	}

	@Override
	protected Range toLSPRange(XMLLocator location, String key, Object[] arguments, String message,
			DiagnosticSeverity diagnosticSeverity, boolean fatalError, DOMDocument document,
			String documentOrGrammarURI, boolean errorForDocument) {
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
		} else {
			// try adjust positions for XML schema error
			XMLSchemaErrorCode schemaCode = XMLSchemaErrorCode.get(key);
			if (schemaCode != null) {
				Range range = XMLSchemaErrorCode.toLSPRange(location, schemaCode, arguments, document);
				if (range != null) {
					return range;
				}
			} else {
				// try adjust positions for DTD error
				DTDErrorCode dtdCode = DTDErrorCode.get(key);
				if (dtdCode != null) {
					if (errorForDocument) {
						Range range = DTDErrorCode.toLSPRange(location, dtdCode, arguments, document);
						if (range != null) {
							return range;
						}
					} else {
						fillReferencedGrammarDiagnostic(location, key, arguments, message, diagnosticSeverity,
								fatalError, document.getResolverExtensionManager(), null, null, dtdCode, null,
								documentOrGrammarURI);
						return NO_RANGE;
					}
				} else {
					XSDErrorCode xsdCode = XSDErrorCode.get(key);
					if (xsdCode != null && !errorForDocument) {
						// The error comes from the referenced XSD (with xsi:schemaLocation, xml-model,
						// etc)

						// Try to get the declared xsi:schemaLocation, xsi:noNamespaceLocation range
						// which declares the XSD.
						fillReferencedGrammarDiagnostic(location, key, arguments, message, diagnosticSeverity,
								fatalError, document.getResolverExtensionManager(), null, null, null, xsdCode,
								documentOrGrammarURI);
						return NO_RANGE;
					}
				}
			}
		}
		return null;
	}

	@Override
	protected boolean isIgnoreFatalError(String key) {
		// Don't stop the validation when there are
		// * EntityNotDeclared error
		return DTDErrorCode.EntityNotDeclared.name().equals(key);
	}

	@Override
	protected Range getReferencedGrammarRange(String grammarURI) {
		Set<ReferencedGrammarInfo> referencedGrammars = getReferencedGrammars();
		for (ReferencedGrammarInfo referencedGrammarInfo : referencedGrammars) {
			if (grammarURI.equals(referencedGrammarInfo.getResolvedURIInfo().getResolvedURI())) {
				DOMRange range = referencedGrammarInfo.getIdentifier() != null
						? referencedGrammarInfo.getIdentifier().getRange()
						: null;
				if (range != null) {
					return XMLPositionUtility.createRange(range);
				}
			}
		}
		// Set the error range in the root start tag
		return XMLPositionUtility.selectRootStartTag(getDOMDocument());
	}

	private Set<ReferencedGrammarInfo> getReferencedGrammars() {
		if (referencedGrammars != null) {
			return referencedGrammars;
		}
		return referencedGrammars = contentModelManager.getReferencedGrammarInfos(super.getDOMDocument());
	}

}
