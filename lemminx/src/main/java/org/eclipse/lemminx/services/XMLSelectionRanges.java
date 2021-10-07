/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMDocumentType;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DOMRange;
import org.eclipse.lemminx.dom.DTDDeclParameter;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.SelectionRange;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.w3c.dom.Node;

/**
 * Calculate <code>textDocument/selectionRange</code> response
 */
class XMLSelectionRanges {

	private static final Logger LOGGER = Logger.getLogger(XMLSelectionRanges.class.getName());

	public XMLSelectionRanges() {
	}

	/**
	 * Returns the selection ranges given the cursor positions and the model of the
	 * XML document
	 *
	 * @param xmlDocument   the model of the XML document
	 * @param positions     the list of cursor positions
	 * @param cancelChecker the cancel checker
	 * @return the selection ranges given the cursor positions and the model of the
	 *         XML document
	 */
	public List<SelectionRange> getSelectionRanges(DOMDocument xmlDocument, List<Position> positions,
			CancelChecker cancelChecker) {
		List<SelectionRange> selectionRanges = new ArrayList<>();
		for (Position position : positions) {
			selectionRanges.add(getSelectionRangeFromCursorPosition(xmlDocument, position));
			cancelChecker.checkCanceled();
		}
		return selectionRanges;
	}

	private SelectionRange getSelectionRangeFromCursorPosition(DOMDocument xmlDocument, Position position) {
		DOMNode node;
		try {
			int offset = xmlDocument.offsetAt(position);
			node = getNodeAtOffset(xmlDocument, offset);
			return getSelectionRangeFromNode(node, offset);
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "Unable to calculate selection range for position " + position, e);
			return null;
		}
	}

	private DOMNode getNodeAtOffset(DOMDocument xmlDocument, int offset) {
		DOMNode node = DOMDocument.findNodeOrAttrAt(xmlDocument, offset);
		if (node.isAttribute()) {
			DOMAttr attr = (DOMAttr) node;
			for (DOMNode attrChild : attr.getChildren()) {
				if (attrChild.getStart() <= offset && offset <= attrChild.getEnd()) {
					return attrChild;
				}
			}
		} else if (node.isDoctype()) {
			DOMDocumentType doctype = (DOMDocumentType) node;
			DTDDeclParameter subset = doctype.getInternalSubsetNode();
			if (subset != null && subset.getStart() < offset && offset < subset.getEnd()) {
				for (DOMNode child : doctype.getChildren()) {
					if (child.getStart() != -1 && child.getStart() <= offset && offset < child.getEnd()) {
						return child;
					}
				}
			}
		}
		return node;
	}

	private SelectionRange getSelectionRangeFromNode(DOMNode node, int offset) {
		// The leaf of the SelectionRange structure is a dummy node that doesn't
		// represent an actual range in the document
		final SelectionRange leafSelectionRange = new SelectionRange();
		leafSelectionRange.setRange(new Range());
		SelectionRange selectionRange = leafSelectionRange;

		do {
			selectionRange = handleNodeSelectionRange(selectionRange, node, offset);
			if (node.isAttribute() && ((DOMAttr) node).getOwnerElement() != null) {
				node = ((DOMAttr) node).getOwnerElement();
			}
			DOMNode prolog = node.getOwnerDocument().getProlog();
			if (prolog != null && node != node.getOwnerDocument() && node != prolog && prolog.getStart() <= offset
					&& offset <= prolog.getEnd()) {
				node = node.getOwnerDocument().getProlog();
			} else {
				node = node.getParentNode();
			}
		} while (node != null);

		// Skip the leaf node, which is a dummy node
		return leafSelectionRange.getParent();
	}

	private SelectionRange handleNodeSelectionRange(SelectionRange selectionRange, DOMNode node, int offset) {
		switch (node.getNodeType()) {
			case Node.ELEMENT_NODE:
				return handleElementSelectionRange(selectionRange, (DOMElement) node, offset);
			case Node.ATTRIBUTE_NODE:
				return handleAttributeSelectionRange(selectionRange, (DOMAttr) node, offset);
			case Node.DOCUMENT_TYPE_NODE:
				return handleDoctypeSelectionRange(selectionRange, (DOMDocumentType) node, offset);
			default:
				return handleGenericNodeSelectionRange(selectionRange, node, offset);
		}
	}

	private SelectionRange handleAttributeSelectionRange(SelectionRange selectionRange, DOMAttr attr, int offset) {
		selectionRange = handleGenericNodeSelectionRange(selectionRange, attr, offset);
		DOMElement ownerElement = attr.getOwnerElement();
		if (ownerElement != null) {
			selectionRange = handleGenericNodeSelectionRange(selectionRange, ownerElement, offset);
		}
		return selectionRange;
	}

	private SelectionRange handleElementSelectionRange(SelectionRange selectionRange, DOMElement element, int offset) {
		int contentStart = element.getStartTagCloseOffset() + 1;
		int contentEnd = element.getEndTagOpenOffset();
		SelectionRange parentSelectionRange = new SelectionRange();
		if (contentStart != DOMNode.NULL_VALUE && contentEnd != DOMNode.NULL_VALUE && offset >= contentStart
				&& offset <= contentEnd) {
			Range range = XMLPositionUtility.createRange(contentStart, contentEnd, element.getOwnerDocument());
			if (range != null && !range.equals(selectionRange.getRange())) {
				parentSelectionRange.setRange(range);
				selectionRange.setParent(parentSelectionRange);
				selectionRange = parentSelectionRange;
			}
		} else if (contentStart != DOMNode.NULL_VALUE && offset < contentStart) {
			parentSelectionRange.setRange(XMLPositionUtility.selectStartTagName(element));
			selectionRange.setParent(parentSelectionRange);
			selectionRange = parentSelectionRange;
		} else if (offset <= element.getEndTagCloseOffset()) {
			parentSelectionRange.setRange(XMLPositionUtility.selectEndTagName(element));
			selectionRange.setParent(parentSelectionRange);
			selectionRange = parentSelectionRange;
		}
		return handleGenericNodeSelectionRange(selectionRange, element, offset);
	}

	private SelectionRange handleDoctypeSelectionRange(SelectionRange selectionRange, DOMDocumentType doctype,
			int offset) {
		DOMRange internalSubset = doctype.getInternalSubsetNode();
		if (internalSubset != null) {
			int subsetStart = internalSubset.getStart();
			int subsetEnd = internalSubset.getEnd();
			// the next line implies (subsetEnd != -1), so there should be no need to check it
			if (subsetStart != -1 && subsetStart < offset && offset < subsetEnd) {
				try {
					Position startPos = doctype.getOwnerDocument().positionAt(subsetStart + 1);
					Position endPos = doctype.getOwnerDocument().positionAt(subsetEnd - 1);
					Range range = new Range(startPos, endPos);
					SelectionRange parentSelectionRange = new SelectionRange();
					parentSelectionRange.setRange(range);
					selectionRange.setParent(parentSelectionRange);
					selectionRange = parentSelectionRange;
				} catch (BadLocationException e) {
					LOGGER.log(Level.SEVERE, "Failed to select DOCTYPE subset", e);
				}
			}
		}
		return handleGenericNodeSelectionRange(selectionRange, doctype, offset);
	}

	private SelectionRange handleGenericNodeSelectionRange(SelectionRange selectionRange, DOMNode node, int offset) {
		Range range = XMLPositionUtility.createRange(node);
		if (!range.equals(selectionRange.getRange())) {
			SelectionRange parentSelectionRange = new SelectionRange();
			parentSelectionRange.setRange(range);
			selectionRange.setParent(parentSelectionRange);
			selectionRange = parentSelectionRange;
		}
		return selectionRange;
	}

}
