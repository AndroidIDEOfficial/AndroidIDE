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
import org.eclipse.lemminx.utils.DOMUtils;

/**
 * Snippet context used to filter snippets if XML file is empty or not.
 *
 */
public abstract class NewFileSnippetContext implements IXMLSnippetContext {

	public static final IXMLSnippetContext XML_CONTEXT = new NewFileSnippetContext() {
		@Override
		protected boolean isMatchType(DOMDocument document) {
			return !(document.isDTD() || DOMUtils.isXSD(document));
		}
	};

	public static final IXMLSnippetContext XSD_CONTEXT = new NewFileSnippetContext() {
		@Override
		protected boolean isMatchType(DOMDocument document) {
			return DOMUtils.isXSD(document);
		}
	};

	public static final IXMLSnippetContext DTD_CONTEXT = new NewFileSnippetContext() {
		@Override
		protected boolean isMatchType(DOMDocument document) {
			return document.isDTD();
		}
	};

	@Override
	public boolean isMatch(ICompletionRequest request, Map<String, String> model) {
		DOMDocument document = request.getXMLDocument();
		if (!isMatchType(document)) {
			return false;
		}
		if (!document.hasChildNodes()) {
			// Empty file
			return true;
		}
		DOMNode node = request.getNode();
		int offset = request.getOffset();
		if ((node.isComment() || node.isProcessingInstruction() || node.isProlog()) && offset < node.getEnd()) {
			// completion was triggered inside comment, xml processing instruction
			// --> <?xml version="1.0" encoding="UTF-8" | ?>
			return false;
		}
		if (document.isBeforeProlog(offset)) {
			// triggered before prolog
			return false;
		}
		// The file contains some contents, the contents allowed are:
		// - comments
		// - processing instruction
		// - text
		// - '<', '<!' characters
		for (DOMNode child : document.getChildren()) {
			if (child.isElement()) {
				DOMElement first = (DOMElement) child;
				// check if element is just '<' or '<!'
				if (!(first.isElement() && ((DOMElement) first).getTagName() == null && !first.hasChildNodes())) {
					return false;
				}
			} else if (!(child.isText() || child.isProlog() || child.isProcessingInstruction() || child.isComment())) {
				return false;
			}
		}
		return true;
	}

	protected abstract boolean isMatchType(DOMDocument document);
}
