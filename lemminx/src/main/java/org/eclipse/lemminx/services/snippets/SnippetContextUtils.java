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

import org.eclipse.lemminx.dom.DOMCDATASection;
import org.eclipse.lemminx.dom.DOMComment;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.ICompletionRequest;
import org.w3c.dom.Node;

/**
 * Snippet context utilities.
 * 
 */
public class SnippetContextUtils {

	private SnippetContextUtils() {

	}

	/**
	 * Returns true if the expression (with or without bracket) can be proceed
	 * according the completion trigger and false otherwise.
	 * 
	 * @param request the completion request.
	 * @return true if the expression (with or without bracket) can be proceed
	 *         according the completion trigger and false otherwise.
	 */
	public static boolean canAcceptExpression(ICompletionRequest request) {
		DOMNode node = request.getNode();
		int offset = request.getOffset();
		if (node.getNodeType() == Node.DOCUMENT_NODE) {
			// |<a></a>
			return true;
		}
		if (node.isElement()) {
			DOMElement element = (DOMElement) node;
			if (element.isOrphanEndTag()) {
				// </ 
				//</foo>
				return false;
			}
			if (!element.hasTagName()) {
				// <|
				// <!|
				return true;
			}
			if (element.isInInsideStartEndTag(offset)) {
				// <a>|</a>
				// <a></|</a>
				String text = request.getXMLDocument().getText();
				if (text.charAt(offset - 1) == '/') {
					// <a></|</a> -> should be ignore
					return false;
				}
				return true;
			}
			if (element.isInStartTag(offset)) {
				// <a |>
				// <a></a |>
				// <a></a>|
				return offset >= node.getEnd();
			}
			if (element.isInEndTag(offset)) {
				// <a></a |>
				return false;
			}
			if (!element.hasEndTag()) {
				// <a>|
				// <a></|
				String text = request.getXMLDocument().getText();
				if (text.charAt(node.getEnd() - 1) == '/') {
					// <a></ -> should be ignore
					return false;
				}
				return true;
			}
			if (offset >= node.getEnd()) {
				// <a></a>|
				return true;
			}
			return false;
		}
		if (offset > node.getEnd()) {
			DOMElement parent = node.getParentElement();
			if (parent != null && parent.isInEndTag(offset)) {
				return false;
			}
		}
		if (offset < node.getEnd()) {
			// <?xml |?> --> should ignore expression
			// text node like <a> | </a> -> it can be an expression
			if (node.isComment()) {
				DOMComment comment = (DOMComment) node;
				return comment.getStartContent() == offset;
			} else if (node.isCDATA()) {
				DOMCDATASection cdata = (DOMCDATASection) node;
				return cdata.getStartContent() == offset;
			}
			return node.isText();
		}
		return true;
	}
}
