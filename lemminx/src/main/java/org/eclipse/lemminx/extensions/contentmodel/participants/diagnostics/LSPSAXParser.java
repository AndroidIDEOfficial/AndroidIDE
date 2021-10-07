/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.contentmodel.participants.diagnostics;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.XMLDTDDescription;
import org.apache.xerces.impl.dtd.XMLEntityDecl;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMDocumentType;
import org.eclipse.lemminx.extensions.contentmodel.participants.DTDErrorCode;
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Range;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Extension of Xerces SAX Parser to fix some Xerces bugs:
 *
 * <ul>
 * <li>[BUG 1]: when the DTD file path is wrong on DOCTYPE, Xerces breaks all
 * validation like syntax validation</li>
 * <li>[BUG 2]: when Xerces XML grammar pool is used, the second validation
 * ignore the existing of entities. See
 * https://github.com/redhat-developer/vscode-xml/issues/234</li>
 * </ul>
 *
 * @author Angelo ZERR
 *
 */
public class LSPSAXParser extends SAXParser {

	private static final String DTD_NOT_FOUND = "Cannot find DTD ''{0}''.\nCreate the DTD file or configure an XML catalog for this DTD.";

	protected static final String VALIDATION_MANAGER = Constants.XERCES_PROPERTY_PREFIX
			+ Constants.VALIDATION_MANAGER_PROPERTY;

	protected static final String ENTITY_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_MANAGER_PROPERTY;

	private final DOMDocument document;

	private final LSPErrorReporterForXML reporter;

	private final XMLGrammarPool grammarPool;

	public LSPSAXParser(DOMDocument document, LSPErrorReporterForXML reporter, XMLParserConfiguration config,
			XMLGrammarPool grammarPool) {
		super(config);
		this.document = document;
		this.reporter = reporter;
		this.grammarPool = grammarPool;
		init(reporter);
	}

	private void init(LSPErrorReporterForXML reporter) {
		try {
			// Add LSP error reporter to fill LSP diagnostics from Xerces errors
			super.setProperty("http://apache.org/xml/properties/internal/error-reporter", reporter);
			super.setFeature("http://apache.org/xml/features/continue-after-fatal-error", false); //$NON-NLS-1$
			super.setFeature("http://xml.org/sax/features/namespace-prefixes", true); //$NON-NLS-1$
			super.setFeature("http://xml.org/sax/features/namespaces", true); //$NON-NLS-1$
			super.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
		} catch (SAXNotRecognizedException | SAXNotSupportedException e) {
			// Should never occur.
		}
	}

	private XMLLocator locator;

	@Override
	public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext,
			Augmentations augs) throws XNIException {
		this.locator = locator;
		super.startDocument(locator, encoding, namespaceContext, augs);
	}

	@Override
	public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs)
			throws XNIException {
		if (systemId != null) {
			// There a declared DTD in the DOCTYPE
			// <!DOCTYPE root-element SYSTEM "./extended.dtd" []>

			XMLEntityManager entityManager = (XMLEntityManager) fConfiguration.getProperty(ENTITY_MANAGER);
			XMLDTDDescription grammarDesc = createGrammarDescription(rootElement, publicId, systemId);

			// Expand the system ID of the DTD and resolve it.
			String expandedSystemId = getExpandedSystemId(grammarDesc, entityManager);
			if (!isDTDExists(expandedSystemId)) {
				// The declared DTD doesn't exist
				// <!DOCTYPE root-element SYSTEM "./dtd-doesnt-exist.dtd" []>
				try {
					// Report the error
					DOMDocumentType docType = document.getDoctype();
					Range range = new Range(document.positionAt(docType.getSystemIdNode().getStart()),
							document.positionAt(docType.getSystemIdNode().getEnd()));
					reporter.addDiagnostic(range, MessageFormat.format(DTD_NOT_FOUND, expandedSystemId),
							DiagnosticSeverity.Error, DTDErrorCode.dtd_not_found.getCode(), null);
				} catch (BadLocationException e) {
					// Do nothing
				}

				// FIX [BUG 1]
				// To avoid breaking the validation (ex : syntax validation) we mark
				// the cache DTD as true to avoid having an IOException error which breaks the
				// validation.
				// boolean readExternalSubset must be false in
				// Xerces
				// https://github.com/apache/xerces2-j/blob/e5a239b96fd2cff6566a29e7a4a3a4a2bbf9b0d4/src/org/apache/xerces/impl/XMLDocumentScannerImpl.java#L950
				ValidationManager fValidationManager = (ValidationManager) fConfiguration
						.getProperty(VALIDATION_MANAGER);
				if (fValidationManager != null) {
					fValidationManager.setCachedDTD(true);
				}
			} else {
				if (grammarPool != null) {
					// FIX [BUG 2]
					// DTD exists, get the DTD grammar from the cache

					DTDGrammar grammar = (DTDGrammar) grammarPool.retrieveGrammar(grammarDesc);
					if (grammar != null) {
						// The DTD grammar is in cache, we need to fill XML entity manager with the
						// entities declared in the cached DTD grammar
						fillEntities(grammar, entityManager);
					}
				}
			}
		}
		super.doctypeDecl(rootElement, publicId, systemId, augs);
	}

	/**
	 * Create DTD grammar description by expanding the system id.
	 *
	 * @param rootElement the root element
	 * @param publicId    the public ID.
	 * @param systemId    the system ID.
	 * @return the DTD grammar description by expanding the system id.
	 */
	private XMLDTDDescription createGrammarDescription(String rootElement, String publicId, String systemId) {
		String eid = null;
		try {
			eid = XMLEntityManager.expandSystemId(systemId, locator.getExpandedSystemId(), false);
		} catch (java.io.IOException e) {
		}

		return new XMLDTDDescription(publicId, systemId, locator.getExpandedSystemId(), eid, rootElement);
	}

	/**
	 * Resolve the expanded system ID by resolving the system ID of the given
	 * grammar description with uri resolver (XML catalog, cache, etc with
	 * {@link URIResolverExtensionManager}).
	 *
	 * @param grammarDesc   the DTD grammar description.
	 * @param entityManager the entity manager.
	 * @return the expanded system ID by resolving the system ID of the given
	 *         grammar description with uri resolver (XML catalog, cache, etc with
	 *         {@link URIResolverExtensionManager}).
	 */
	private static String getExpandedSystemId(XMLDTDDescription grammarDesc, XMLEntityManager entityManager) {
		try {
			XMLInputSource input = entityManager.resolveEntity(grammarDesc);
			return input.getSystemId();
		} catch (Exception e) {
		}
		return grammarDesc.getExpandedSystemId();
	}

	private static boolean isDTDExists(String expandedSystemId) {
		if (expandedSystemId == null || expandedSystemId.isEmpty()) {
			return true;
		}
		try {
			URL location = new URL(expandedSystemId);
			URLConnection connect = location.openConnection();
			if (!(connect instanceof HttpURLConnection)) {
				InputStream stream = connect.getInputStream();
				stream.close();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Fill entities from the given DTD grammar to the given entity manager.
	 *
	 * @param grammar       the DTD grammar
	 * @param entityManager the entitymanager to update with entities of the DTD
	 *                      grammar.
	 */
	private static void fillEntities(DTDGrammar grammar, XMLEntityManager entityManager) {
		int index = 0;
		XMLEntityDecl entityDecl = new XMLEntityDecl() {

			@Override
			public void setValues(String name, String publicId, String systemId, String baseSystemId, String notation,
					String value, boolean isPE, boolean inExternal) {
				if (inExternal) {
					// Only entities declared in the cached DTD grammar must be added in the XML
					// entity manager.
					entityManager.addInternalEntity(name, value);
				}
			};
		};
		while (grammar.getEntityDecl(index, entityDecl)) {
			index++;
		}
	}
}
