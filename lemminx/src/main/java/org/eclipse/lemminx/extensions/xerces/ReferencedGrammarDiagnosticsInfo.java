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
package org.eclipse.lemminx.extensions.xerces;

import java.util.ArrayList;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticRelatedInformation;

/**
 * The root diagnostic information when a grammar (XSD, DTD) have some errors
 * and it is referenced in a XML. The diagnostic range covers teh location where
 * the grammar is declared (with DOCTYPE SYSTEM, xsi:noNamespaceSchemaLocation,
 * etc).
 * 
 * @author Angelo ZERR
 *
 */
public class ReferencedGrammarDiagnosticsInfo {

	private final String grammarURI;

	private final String grammarFileName;

	private final URIResolverExtensionManager resolverExtensionManager;

	private final Diagnostic diagnostic;

	private DOMDocument document;

	private int nbError;

	private boolean fatalError;

	public ReferencedGrammarDiagnosticsInfo(String grammarURI, URIResolverExtensionManager resolverExtensionManager,
			Diagnostic diagnostic) {
		this.grammarURI = grammarURI;
		this.grammarFileName = computeFileName(grammarURI);
		this.resolverExtensionManager = resolverExtensionManager;
		this.diagnostic = diagnostic;
		this.fatalError = false;
	}

	private static String computeFileName(String grammarURI) {
		if (grammarURI.startsWith("http")) {
			return grammarURI;
		}
		String fileName = grammarURI;
		int index = grammarURI.lastIndexOf('/');
		if (index == -1) {
			index = grammarURI.lastIndexOf('\\');
		}
		if (index != -1) {
			fileName = grammarURI.substring(index + 1, grammarURI.length());
		}
		return fileName;
	}

	/**
	 * Add diagnostic related information.
	 * 
	 * @param relatedInformation the related information.
	 */
	public void addDiagnosticRelatedInformation(DiagnosticRelatedInformation relatedInformation) {
		if (diagnostic.getRelatedInformation() == null) {
			diagnostic.setRelatedInformation(new ArrayList<>());
		}
		diagnostic.getRelatedInformation().add(relatedInformation);
	}

	/**
	 * Returns the DOM document of the referenced grammar.
	 * 
	 * @return the DOM document of the referenced grammar.
	 */
	public DOMDocument getGrammarDocument() {
		if (document == null) {
			document = DOMUtils.loadDocument(grammarURI, resolverExtensionManager);
		}
		return document;
	}

	/**
	 * Returns the referenced grammar file name .
	 * 
	 * @return the referenced grammar file name .
	 */
	private String getGrammarFileName() {
		return grammarFileName;
	}

	/**
	 * Increment the error number to update the diagnostic root message.
	 * 
	 * @param diagnosticSeverity
	 */
	public void addError(boolean fatalError) {
		this.fatalError = fatalError;
		nbError++;
		diagnostic.setMessage("There " + (nbError > 1 ? "are" : "is") + " '" + String.valueOf(nbError) + "' error"
				+ (nbError > 1 ? "s" : "") + " in '" + getGrammarFileName() + "'.");
	}

	public boolean isFatalError() {
		return fatalError;
	}
}