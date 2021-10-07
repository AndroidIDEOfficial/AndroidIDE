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
package org.eclipse.lemminx.services;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.LinkedEditingRanges;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML linked editing support.
 *
 */
class XMLLinkedEditing {

	private static Logger LOGGER = Logger.getLogger(XMLLinkedEditing.class.getName());

	/**
	 * Returns the linked editing ranges for the given <code>xmlDocument</code> at
	 * the given <code>position</code> and null otherwise.
	 *
	 * @param xmlDocument   the DOM document.
	 * @param position      the position.
	 * @param cancelChecker the cancel checker.
	 * @return the linked editing ranges for the given <code>xmlDocument</code> at
	 *         the given <code>position</code> and null otherwise.
	 */
	public LinkedEditingRanges findLinkedEditingRanges(DOMDocument document, Position position,
			CancelChecker cancelChecker) {
		try {
			int offset = document.offsetAt(position);
			DOMNode node = document.findNodeAt(offset);
			if (node == null || !node.isElement()) {
				return null;
			}
			DOMElement element = (DOMElement) node;
			if (element.isOrphanEndTag() || !element.hasEndTag()) {
				return null;
			}

			if (element.isInStartTag(offset) || element.isInEndTag(offset, true)) {
				List<Range> ranges = Arrays.asList(XMLPositionUtility.selectStartTagName(element),
						XMLPositionUtility.selectEndTagName(element));
				return new LinkedEditingRanges(ranges);
			}
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "In XMLLinkedEditing, position error", e);
		}
		return null;
	}
}
