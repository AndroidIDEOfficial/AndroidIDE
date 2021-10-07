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
import org.eclipse.lemminx.dom.DOMDocumentType;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.ICompletionRequest;

/**
 * DTD nodes snippet context used to filter the <!ELEMENT, <!ATTLIST, <!ENTITY
 * snippets.
 *
 */
public class DTDNodeSnippetContext implements IXMLSnippetContext {

	public static IXMLSnippetContext DEFAULT_CONTEXT = new DTDNodeSnippetContext();

	@Override
	public boolean isMatch(ICompletionRequest request, Map<String, String> model) {
		DOMNode node = request.getNode();
		if (node == null) {
			return false;
		}
		DOMDocument document = node.getOwnerDocument();
		// <!ELEMENT, <!ATTLIST, <!ENTITY snippets are available only inside DTD content
		// - in XML document, it must define a DOCTYPE
		// - in DTD file

		if (document.isDTD()) {
			// it's a DTD file
			return true;
		}

		// check if XML document defines a DOCTYPE and offset are in this DOCTYPE
		DOMDocumentType docType = null;
		if (node.isDoctype()) {
			docType = (DOMDocumentType) node;
		} else if (node.getParentNode() != null && node.getParentNode().isDoctype()) {
			docType = (DOMDocumentType) node.getParentNode();
		}
		if (docType == null) {
			return false;
		}
		return document.isWithinInternalDTD(request.getOffset());
	}

}
