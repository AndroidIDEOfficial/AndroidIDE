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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DOMText;
import org.eclipse.lemminx.dom.parser.Scanner;
import org.eclipse.lemminx.dom.parser.TokenType;
import org.eclipse.lemminx.dom.parser.XMLScanner;
import org.eclipse.lemminx.services.extensions.IHoverParticipant;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.MarkupContentFactory;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML hover support.
 *
 */
class XMLHover {

	private static final Logger LOGGER = Logger.getLogger(XMLHover.class.getName());

	private final XMLExtensionsRegistry extensionsRegistry;

	public XMLHover(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	public Hover doHover(DOMDocument xmlDocument, Position position, SharedSettings settings,
			CancelChecker cancelChecker) {
		HoverRequest hoverRequest = null;
		try {
			hoverRequest = new HoverRequest(xmlDocument, position, settings, extensionsRegistry);
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "Failed creating HoverRequest", e);
			return null;
		}
		int offset = hoverRequest.getOffset();
		DOMNode node = hoverRequest.getNode();
		if (node == null) {
			return null;
		}
		if (node.isElement() && ((DOMElement) node).hasTagName()) {
			// Element is hovered
			DOMElement element = (DOMElement) node;
			if (element.hasEndTag() && offset >= element.getEndTagOpenOffset()) {
				Range tagRange = getTagNameRange(TokenType.EndTag, element.getEndTagOpenOffset(), offset, xmlDocument);
				if (tagRange != null) {
					return getTagHover(hoverRequest, tagRange, false);
				}
				return null;
			}

			Range tagRange = getTagNameRange(TokenType.StartTag, node.getStart(), offset, xmlDocument);
			if (tagRange != null) {
				return getTagHover(hoverRequest, tagRange, true);
			}
		} else if (node.isAttribute()) {
			// Attribute is hovered
			DOMAttr attr = (DOMAttr) node;
			if (attr.valueContainsOffset(offset)) {
				// Attribute value is hovered
				Range attrRange = XMLPositionUtility.selectAttributeValue(attr);
				return getAttrValueHover(hoverRequest, attrRange);
			}
			// Attribute name is hovered
			Range attrRange = XMLPositionUtility.selectAttributeName(attr);
			return getAttrNameHover(hoverRequest, attrRange);
		} else if (node.isText()) {
			// Text is hovered
			DOMText text = (DOMText) node;
			Range textRange = XMLPositionUtility.selectText(text);
			return getTextHover(hoverRequest, textRange);
		}
		return null;
	}

	/**
	 * Returns the LSP hover from the hovered element.
	 * 
	 * @param hoverRequest the hover request.
	 * @param tagRange     the tag range
	 * @param open         true if it's the start tag which is hovered and false if
	 *                     it's the end tag.
	 * @return the LSP hover from the hovered element.
	 */
	private Hover getTagHover(HoverRequest hoverRequest, Range tagRange, boolean open) {
		hoverRequest.setHoverRange(tagRange);
		hoverRequest.setOpen(open);
		List<Hover> hovers = new ArrayList<>();
		for (IHoverParticipant participant : extensionsRegistry.getHoverParticipants()) {
			try {
				Hover hover = participant.onTag(hoverRequest);
				if (hover != null) {
					hovers.add(hover);
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "While performing IHoverParticipant#onTag", e);
			}
		}
		return mergeHover(hovers, tagRange);
	}

	private Range getTagNameRange(TokenType tokenType, int startOffset, int offset, DOMDocument document) {
		Scanner scanner = XMLScanner.createScanner(document.getText(), startOffset);
		TokenType token = scanner.scan();
		while (token != TokenType.EOS
				&& (scanner.getTokenEnd() < offset || scanner.getTokenEnd() == offset && token != tokenType)) {
			token = scanner.scan();
		}
		if (token == tokenType && offset <= scanner.getTokenEnd()) {
			try {
				return new Range(document.positionAt(scanner.getTokenOffset()),
						document.positionAt(scanner.getTokenEnd()));
			} catch (BadLocationException e) {
				LOGGER.log(Level.SEVERE, "While creating Range in XMLHover the Scanner's Offset was a BadLocation", e);
				return null;
			}
		}
		return null;
	}

	/**
	 * Returns the LSP hover from the hovered attribute.
	 * 
	 * @param hoverRequest the hover request.
	 * @param attrRange    the attribute range
	 * @return the LSP hover from the hovered attribute.
	 */
	private Hover getAttrNameHover(HoverRequest hoverRequest, Range attrRange) {
		hoverRequest.setHoverRange(attrRange);
		List<Hover> hovers = new ArrayList<>();
		for (IHoverParticipant participant : extensionsRegistry.getHoverParticipants()) {
			try {
				Hover hover = participant.onAttributeName(hoverRequest);
				if (hover != null) {
					hovers.add(hover);
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "While performing IHoverParticipant#onAttributeName", e);
			}
		}
		return mergeHover(hovers, attrRange);
	}

	/**
	 * Returns the LSP hover from the hovered attribute.
	 * 
	 * @param hoverRequest the hover request.
	 * @param attrRange    the attribute range
	 * @return the LSP hover from the hovered attribute.
	 */
	private Hover getAttrValueHover(HoverRequest hoverRequest, Range attrRange) {
		hoverRequest.setHoverRange(attrRange);
		List<Hover> hovers = new ArrayList<>();
		for (IHoverParticipant participant : extensionsRegistry.getHoverParticipants()) {
			try {
				Hover hover = participant.onAttributeValue(hoverRequest);
				if (hover != null) {
					hovers.add(hover);
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "While performing IHoverParticipant#onAttributeValue", e);
			}
		}
		return mergeHover(hovers, attrRange);
	}

	/**
	 * Returns the LSP hover from the hovered text.
	 * 
	 * @param hoverRequest the hover request.
	 * @param attrRange    the attribute range
	 * @return the LSP hover from the hovered text.
	 */
	private Hover getTextHover(HoverRequest hoverRequest, Range textRange) {
		hoverRequest.setHoverRange(textRange);
		List<Hover> hovers = new ArrayList<>();
		for (IHoverParticipant participant : extensionsRegistry.getHoverParticipants()) {
			try {
				Hover hover = participant.onText(hoverRequest);
				if (hover != null) {
					hovers.add(hover);
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "While performing IHoverParticipant#onText", e);
			}
		}
		return mergeHover(hovers, textRange);
	}

	/**
	 * Returns the aggregated LSP hover from the given hover list.
	 * 
	 * @param hovers       the hover list.
	 * @param defaultRange the default range according to the hovered DOM node.
	 * @return the aggregated LSP hover from the given hover list.
	 */
	private static Hover mergeHover(List<Hover> hovers, Range defaultRange) {
		if (hovers.isEmpty()) {
			// no hover
			return null;
		}
		if (hovers.size() == 1) {
			// One hover
			Hover hover = hovers.get(0);
			if (hover.getRange() == null) {
				hover.setRange(defaultRange);
			}
			return hover;
		}
		// Several hovers.

		// Get list of markup content
		List<MarkupContent> contents = hovers.stream() //
				.filter(hover -> hover.getContents() != null && hover.getContents().isRight()
						&& hover.getContents().getRight() != null) //
				.map(hover -> hover.getContents().getRight()).collect(Collectors.toList());

		// Find the best hover range
		Range range = defaultRange;
		for (Hover hover : hovers) {
			if (hover.getRange() != null) {
				if (range == null) {
					range = hover.getRange();
				} else {
					// TODO : compute the best range
				}
			}
		}
		return MarkupContentFactory.createHover(contents, range);
	}
}
