/**
 *  Copyright (c) 2018 Angelo ZERR
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.services;

import static org.eclipse.lemminx.utils.XMLPositionUtility.covers;
import static org.eclipse.lemminx.utils.XMLPositionUtility.doesTagCoverPosition;
import static org.eclipse.lemminx.utils.XMLPositionUtility.getTagNameRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.parser.TokenType;
import org.eclipse.lemminx.services.extensions.IHighlightingParticipant;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentHighlightKind;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML highlighting support.
 *
 */
class XMLHighlighting {

	private static final Logger LOGGER = Logger.getLogger(XMLHighlighting.class.getName());

	private final XMLExtensionsRegistry extensionsRegistry;

	public XMLHighlighting(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	public List<DocumentHighlight> findDocumentHighlights(DOMDocument xmlDocument, Position position,
			CancelChecker cancelChecker) {
		int offset = -1;
		try {
			offset = xmlDocument.offsetAt(position);
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "In XMLHighlighting the client provided Position is at a BadLocation", e);
			return Collections.emptyList();
		}
		DOMNode node = xmlDocument.findNodeAt(offset);
		if (node == null) {
			return Collections.emptyList();
		}
		List<DocumentHighlight> highlights = new ArrayList<>();
		fillWithDefaultHighlights(node, position, offset, highlights, cancelChecker);
		fillWithCustomHighlights(node, position, offset, highlights, cancelChecker);
		return highlights;
	}

	private static void fillWithDefaultHighlights(DOMNode node, Position position, int offset,
			List<DocumentHighlight> highlights, CancelChecker cancelChecker) {
		if (!node.isElement() || !((DOMElement) node).hasTagName()) {
			return;
		}

		DOMDocument xmlDocument = node.getOwnerDocument();
		Range startTagRange = null;
		Range endTagRange = null;
		if (node.isCDATA()) {
			Position startPos = null;
			Position endPos = null;
			Range tempRange = null;
			try {
				startPos = xmlDocument.positionAt(node.getStart());
				endPos = xmlDocument.positionAt(node.getEnd());
				tempRange = new Range(startPos, endPos);

			} catch (BadLocationException e) {
				LOGGER.log(Level.SEVERE, "In XMLHighlighting the Node at provided Offset is a BadLocation", e);
				return;
			}
			if (covers(tempRange, position)) {
				startPos.setCharacter(startPos.getCharacter() + 1); // {Cursor}<![CDATA[ -> <{Cursor}![CDATA[
				endPos.setCharacter(endPos.getCharacter() - 1); // ]]>{Cursor} -> ]]{Cursor}>
				Position startPosEnd = new Position(startPos.getLine(), startPos.getCharacter() + 8);
				Position endPosStart = new Position(endPos.getLine(), endPos.getCharacter() - 2);
				fillHighlightsList(new Range(startPos, startPosEnd), new Range(endPosStart, endPos), highlights);
			}
		} else if (node.isElement()) {
			DOMElement element = (DOMElement) node;
			startTagRange = getTagNameRange(TokenType.StartTag, node.getStart(), xmlDocument);
			endTagRange = element.hasEndTag()
					? getTagNameRange(TokenType.EndTag, element.getEndTagOpenOffset(), xmlDocument)
					: null;
			if (doesTagCoverPosition(startTagRange, endTagRange, position)) {
				fillHighlightsList(startTagRange, endTagRange, highlights);
			}
		}
	}

	private static void fillHighlightsList(Range startTagRange, Range endTagRange, List<DocumentHighlight> result) {
		if (startTagRange != null) {
			result.add(new DocumentHighlight(startTagRange, DocumentHighlightKind.Read));
		}
		if (endTagRange != null) {
			result.add(new DocumentHighlight(endTagRange, DocumentHighlightKind.Read));
		}
	}

	private void fillWithCustomHighlights(DOMNode node, Position position, int offset,
			List<DocumentHighlight> highlights, CancelChecker cancelChecker) {
		// Consume highlighting participant
		for (IHighlightingParticipant highlightingParticipant : extensionsRegistry.getHighlightingParticipants()) {
			try {
				highlightingParticipant.findDocumentHighlights(node, position, offset, highlights, cancelChecker);
			} catch (CancellationException e) {
				throw e;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Error while processing highlights for the participant '"
						+ highlightingParticipant.getClass().getName() + "'.", e);
			}
		}
	}
}
