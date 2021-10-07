/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.contentmodel.commands;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.services.IXMLDocumentProvider;
import org.eclipse.lemminx.services.extensions.commands.AbstractDOMDocumentCommandHandler;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML Command "xml.check.bound.grammar" to verify if an XML document already
 * has a bound grammar/schema, opened from the command palette.
 *
 */
public class CheckBoundGrammarCommand extends AbstractDOMDocumentCommandHandler {

	public static final String COMMAND_ID = "xml.check.bound.grammar";

	public CheckBoundGrammarCommand(IXMLDocumentProvider documentProvider) {
		super(documentProvider);
	}

	@Override
	protected Object executeCommand(DOMDocument document, ExecuteCommandParams params, SharedSettings sharedSettings,
			CancelChecker cancelChecker) throws Exception {
		// Check if the document has a bound grammar.
		return canBindWithGrammar(document);
	}

	/**
	 * Returns true if the given DOM document can be bound with a given grammar and
	 * false otherwise.
	 * 
	 * @param document the DOM document.
	 * 
	 * @return true if the given DOM document can be bound with a given grammar and
	 *         false otherwise.
	 */
	public static boolean canBindWithGrammar(DOMDocument document) {
		DOMElement documentElement = document.getDocumentElement();
		if (documentElement == null) {
			return false;
		}
		return !document.hasGrammar();
	}
}
