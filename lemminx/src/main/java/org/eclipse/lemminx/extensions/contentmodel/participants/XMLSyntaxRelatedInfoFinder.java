/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.contentmodel.participants;

import java.util.Collections;
import java.util.List;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.DiagnosticRelatedInformation;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Range;

/**
 * Find related information for an XML syntax error
 */
public class XMLSyntaxRelatedInfoFinder implements IRelatedInfoFinder {

	private static String CLOSING_TAG_EXPECTED_HERE = "Closing tag expected here";

	@Override
	public List<DiagnosticRelatedInformation> findRelatedInformation(int offset, String errorKey,
			DOMDocument document) {

		XMLSyntaxErrorCode syntaxCode = XMLSyntaxErrorCode.get(errorKey);

		if (syntaxCode == null) {
			return Collections.emptyList();
		}

		switch (syntaxCode) {
			case ETagRequired:
			case MarkupEntityMismatch: {
			DOMNode node = document.findNodeAt(offset);
			while (node != null && !node.isElement()) {
				node = node.getParentNode();
			}
			if (node == null) {
				return Collections.emptyList();
			}

			if (node == document.getDocumentElement() && ((DOMElement) node).hasEndTag()){
				return Collections.emptyList();
			}

			int closeTagOffset;
			int numChildren = node.getChildren().size();
			if (numChildren == 0) {
				closeTagOffset = node.getEnd();
			} else {
				closeTagOffset = node.getChildren().get(numChildren - 1).getEnd();
			}

			Range range = XMLPositionUtility.createRange(closeTagOffset, closeTagOffset, document);
			return Collections.singletonList(new DiagnosticRelatedInformation(
					new Location(document.getDocumentURI(), range), CLOSING_TAG_EXPECTED_HERE));
		}
		default:
		}
		return Collections.emptyList();
	}

}
