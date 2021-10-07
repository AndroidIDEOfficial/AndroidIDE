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

/**
 * DOCTYPE snippet context used to filter the DOCTYPE snippets.
 *
 */
public class DocTypeSnippetContext implements IXMLSnippetContext {

	private static final String ROOT_ELEMENT = "root-element";
	public static IXMLSnippetContext DEFAULT_CONTEXT = new DocTypeSnippetContext();

	@Override
	public boolean isMatch(ICompletionRequest request, Map<String, String> model) {
		DOMDocument document = request.getXMLDocument();
		DOMElement documentElement = document.getDocumentElement();
		if (documentElement == null) {
			return false;
		}
		if (document.getDoctype() != null) {
			return false;
		}
		String tagName = documentElement.getTagName();
		if (tagName == null && documentElement.hasChildNodes() && documentElement.getChild(0).isElement()) {
			documentElement = ((DOMElement) documentElement.getChild(0));
			tagName = documentElement.getTagName();
		}
		if (tagName == null) {
			return false;
		}
		DOMNode node = request.getNode();
		DOMNode parent = node.getParentNode();
		if (parent != null && parent.isDoctype()) {
			// inside DTD
			return false;
		}
		int offset = request.getOffset();
		if ((node.isComment() || node.isProcessingInstruction() || node.isProlog()) && offset < node.getEnd()) {
			// completion was triggered inside comment, xml processing instruction
			// --> <?xml version="1.0" encoding="UTF-8" | ?>
			return false;
		}

		if (document.isBeforeProlog(offset)){
			// triggered before prolog
			return false;
		}

		if (offset > documentElement.getStart()) {
			return false;
		}
		DOMNode previous = documentElement.getPreviousSibling();
		while (previous != null) {
			if (!(previous.isText() || previous.isProlog() || previous.isProcessingInstruction() || previous.isComment())) {
				return false;
			}
			previous = previous.getPreviousSibling();
		}
		model.put(ROOT_ELEMENT, tagName);
		return true;
	}

}
