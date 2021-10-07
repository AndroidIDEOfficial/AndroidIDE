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

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.LineIndentInfo;
import org.eclipse.lemminx.services.extensions.IPositionRequest;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.Position;
import org.w3c.dom.Node;

/**
 * Abstract class for position request.
 *
 */
abstract class AbstractPositionRequest implements IPositionRequest {

	private final DOMDocument xmlDocument;
	private final Position position;
	private final XMLExtensionsRegistry extensionsRegistry;
	private final int offset;

	private final DOMNode node;
	private LineIndentInfo indentInfo;

	public AbstractPositionRequest(DOMDocument xmlDocument, Position position, XMLExtensionsRegistry extensionsRegistry)
			throws BadLocationException {
		this.xmlDocument = xmlDocument;
		this.position = position;
		this.extensionsRegistry = extensionsRegistry;
		offset = xmlDocument.offsetAt(position);
		this.node = findNodeAt(xmlDocument, offset);
		if (node == null) {
			throw new BadLocationException("node is null at offset " + offset);
		}
	}

	protected DOMNode findNodeAt(DOMDocument document, int offset) {
		return DOMNode.findNodeOrAttrAt(document, offset);
	}

	@Override
	public DOMNode getNode() {
		return node;
	}

	@Override
	public DOMElement getParentElement() {
		DOMNode currentNode = getNode();
		if (!currentNode.isElement()) {
			// Node is not an element, search parent element.
			return currentNode.getParentElement();
		}
		DOMElement element = (DOMElement) currentNode;
		// node is an element, there are 3 cases
		// - case 1: <|
		// - case 2: <bean | >
		// - case 3: <bean /> | or <bean></bean> |
		// --> in thoses cases we must search parent of bean element
		if (element.isInStartTag(offset) || element.isInEndTag(offset)
				|| (element.isEndTagClosed() && element.getEnd() <= offset)) {
			return element.getParentElement();
		}
		// case 2: <bean> | --> in this case, parent element is the bean
		return (DOMElement) currentNode;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public DOMDocument getXMLDocument() {
		return xmlDocument;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public String getCurrentTag() {
		if (node != null && node.isElement() && ((DOMElement) node).hasTagName()) {
			return ((DOMElement) node).getTagName();
		}
		return null;
	}

	@Override
	public String getCurrentAttributeName() {
		DOMAttr attr = getCurrentAttribute();
		return attr != null ? attr.getName() : null;
	}

	/**
	 * Returns the current attribute at the given offset and null otherwise.
	 * 
	 * @return the current attribute at the given offset and null otherwise.
	 */
	private DOMAttr getCurrentAttribute() {
		if (node == null) {
			return null;
		}
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
			return node.findAttrAt(offset);
		case Node.ATTRIBUTE_NODE:
			return ((DOMAttr) node);
		default:
			return null;
		}
	}

	@Override
	public LineIndentInfo getLineIndentInfo() throws BadLocationException {
		if (indentInfo == null) {
			int lineNumber = getPosition().getLine();
			indentInfo = getXMLDocument().getLineIndentInfo(lineNumber);
		}
		return indentInfo;
	}

	@Override
	public <T> T getComponent(Class clazz) {
		return extensionsRegistry.getComponent(clazz);
	}

}
