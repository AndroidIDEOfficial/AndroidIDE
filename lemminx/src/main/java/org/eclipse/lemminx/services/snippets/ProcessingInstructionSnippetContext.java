/*******************************************************************************
* Copyright (c) 2020 Balduin Landolt and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Balduin Landolt - initial API and implementation
*******************************************************************************/


package org.eclipse.lemminx.services.snippets;

import java.util.Map;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.ICompletionRequest;
import org.eclipse.lemminx.utils.DOMUtils;

/**
 * Snippet context used to filter if Processing Instruction Snippets should be triggered or not.
 *
 */
public class ProcessingInstructionSnippetContext implements IXMLSnippetContext {

	public static IXMLSnippetContext DEFAULT_CONTEXT = new ProcessingInstructionSnippetContext();

	@Override
	public boolean isMatch(ICompletionRequest request, Map<String, String> model) {
		DOMNode node = request.getNode();
		int offset = request.getOffset();
		if ((node.isComment() || node.isDoctype() || node.isProlog() || node.isProcessingInstruction()) && offset < node.getEnd()) {
			// completion was triggered inside comment, doctype, prolog or processing instruction
			return false;
		}

		if (node.getParentNode() != null && node.getParentNode().isDoctype()) {
			// completion was triggered after element inside doctype
			return false;
		}

 		DOMDocument document = request.getXMLDocument();
		DOMElement documentElement = document.getDocumentElement();

		if (document.isDTD() || DOMUtils.isXSD(document)) {
			// triggered in a DTD or XSD file
			return false;
		}

		if (document.isBeforeProlog(offset)){
			// triggered before prolog
			return false;
		}

		if (documentElement != null && documentElement.hasTagName()) {
			return offset <= documentElement.getStart();
		}

		return true;
	}
	
}
