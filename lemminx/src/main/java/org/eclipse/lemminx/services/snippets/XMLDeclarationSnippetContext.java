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
package org.eclipse.lemminx.services.snippets;

import java.util.Map;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.ICompletionRequest;
import org.eclipse.lsp4j.Position;

/**
 * Snippet context used to filter the XML declaration snippets.
 * 
 */
public class XMLDeclarationSnippetContext implements IXMLSnippetContext {

	public static IXMLSnippetContext DEFAULT_CONTEXT = new XMLDeclarationSnippetContext();

	@Override
	public boolean isMatch(ICompletionRequest request, Map<String, String> model) {
		DOMDocument document = request.getXMLDocument();
		DOMNode node = request.getNode();
		int offset = request.getOffset();
		if ((node.isComment() || node.isDoctype()) && offset < node.getEnd()) {
			// completion was triggered inside comment, xml processing instruction
			// --> <?xml version="1.0" encoding="UTF-8" | ?>
			return false;
		}
		// check if document already defined a xml declaration.
		if (document.isBeforeProlog(offset) || inProlog(document, offset)) {
			return false;
		}
		Position start = request.getReplaceRange().getStart();
		if (start.getLine() > 0) {
			// The xml processing instruction must be declared in the first line.
			return false;
		}
		// No xml processing instruction, check if completion was triggered before the
		// document element
		DOMElement documentElement = document.getDocumentElement();
		if (documentElement != null && documentElement.hasTagName()) {
			return offset <= documentElement.getStart();
		}
		return true;
	}

	private static boolean inProlog(DOMDocument document, int offset) {
		DOMNode node = document.getFirstChild();
		if (node == null) {
			return false;
		}
		if (node.isProlog()) {
			// is offset is the prolog ?
			return offset > node.getStart() + "<?xml".length();
		}
		return false;
	}
}
