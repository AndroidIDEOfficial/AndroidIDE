/**
 *  Copyright (c) 2018 Angelo ZERR.
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
package org.eclipse.lemminx.dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

/**
 * DOM node.
 *
 */
public abstract class DOMNode implements Node, DOMRange {

	/**
	 * Null value used for offset.
	 */
	public static final int NULL_VALUE = -1;

	/**
	 * The node is a <code>DTD Element Declaration</code>.
	 */
	public static final short DTD_ELEMENT_DECL_NODE = 101;

	/**
	 * The node is a <code>DTD Attribute List</code>.
	 */
	public static final short DTD_ATT_LIST_NODE = 102;

	/**
	 * The node is a <code>DTD Entity Declaraction</code>.
	 */
	public static final short DTD_ENTITY_DECL_NODE = 103;

	/**
	 * The node is a <code>DTD Notation Declaraction</code>.
	 */
	public static final short DTD_NOTATION_DECL = 104;

	/**
	 * The node is a generic <code>DTD Decl Node</code>.
	 */
	public static final short DTD_DECL_NODE = 105;

	boolean closed = false;

	private XMLNamedNodeMap<DOMAttr> attributeNodes;
	private XMLNodeList<DOMNode> children;

	final int start; // |<root> </root>
	int end; // <root> </root>|

	DOMNode parent;

	private static final NodeList EMPTY_CHILDREN = new NodeList() {

		@Override
		public Node item(int index) {
			return null;
		}

		@Override
		public int getLength() {
			return 0;
		}
	};

	static class XMLNodeList<T extends DOMNode> extends ArrayList<T> implements NodeList {

		private static final long serialVersionUID = 1L;

		@Override
		public int getLength() {
			return super.size();
		}

		@Override
		public DOMNode item(int index) {
			return super.get(index);
		}

	}

	static class XMLNamedNodeMap<T extends DOMNode> extends ArrayList<T> implements NamedNodeMap {

		private static final long serialVersionUID = 1L;

		@Override
		public int getLength() {
			return super.size();
		}

		@Override
		public T getNamedItem(String name) {
			for (T node : this) {
				if (name.equals(node.getNodeName())) {
					return node;
				}
			}
			return null;
		}

		@Override
		public T getNamedItemNS(String name, String arg1) throws DOMException {
			throw new UnsupportedOperationException();
		}

		@Override
		public T item(int index) {
			return super.get(index);
		}

		@Override
		public T removeNamedItem(String arg0) throws DOMException {
			throw new UnsupportedOperationException();
		}

		@Override
		public T removeNamedItemNS(String arg0, String arg1) throws DOMException {
			throw new UnsupportedOperationException();
		}

		@Override
		public T setNamedItem(org.w3c.dom.Node arg0) throws DOMException {
			throw new UnsupportedOperationException();
		}

		@Override
		public T setNamedItemNS(org.w3c.dom.Node arg0) throws DOMException {
			throw new UnsupportedOperationException();
		}

	}

	public DOMNode(int start, int end) {
		this.start = start;
		this.end = end;
		this.closed = false;
	}

	/**
	 * Returns the owner document and null otherwise.
	 * 
	 * @return the owner document and null otherwise.
	 */
	@Override
	public DOMDocument getOwnerDocument() {
		Node node = parent;
		while (node != null) {
			if (node.getNodeType() == Node.DOCUMENT_NODE) {
				return (DOMDocument) node;
			}
			node = node.getParentNode();
		}
		return null;
	}

	@Override
	public String toString() {
		return toString(0);
	}

	private String toString(int indent) {
		StringBuilder result = new StringBuilder("");
		for (int i = 0; i < indent; i++) {
			result.append("\t");
		}
		result.append("{start: ");
		result.append(start);
		result.append(", end: ");
		result.append(end);
		result.append(", name: ");
		result.append(getNodeName());
		result.append(", closed: ");
		result.append(closed);
		if (children != null && children.size() > 0) {
			result.append(", \n");
			for (int i = 0; i < indent + 1; i++) {
				result.append("\t");
			}
			result.append("children:[");
			for (int i = 0; i < children.size(); i++) {
				DOMNode node = children.get(i);
				result.append("\n");
				result.append(node.toString(indent + 2));
				if (i < children.size() - 1) {
					result.append(",");
				}
			}
			result.append("\n");
			for (int i = 0; i < indent + 1; i++) {
				result.append("\t");
			}
			result.append("]");
			result.append("\n");
			for (int i = 0; i < indent; i++) {
				result.append("\t");
			}
			result.append("}");
		} else {
			result.append("}");
		}
		return result.toString();
	}

	/**
	 * Returns the node before
	 */
	public DOMNode findNodeBefore(int offset) {
		List<DOMNode> children = getChildren();
		int idx = findFirst(children, c -> offset <= c.start) - 1;
		if (idx >= 0) {
			DOMNode child = children.get(idx);
			if (offset > child.start) {
				if (offset < child.end) {
					return child.findNodeBefore(offset);
				}
				DOMNode lastChild = child.getLastChild();
				if (lastChild != null && lastChild.end == child.end) {
					return child.findNodeBefore(offset);
				}
				return child;
			}
		}
		return this;
	}

	public DOMNode findNodeAt(int offset) {
		List<DOMNode> children = getChildren();
		int idx = findFirst(children, c -> offset <= c.start) - 1;
		if (idx >= 0) {
			DOMNode child = children.get(idx);
			if (isIncluded(child, offset)) {
				return child.findNodeAt(offset);
			}
		}
		return this;
	}

	/**
	 * Returns true if the node included the given offset and false otherwise.
	 * 
	 * @param node
	 * @param offset
	 * @return true if the node included the given offset and false otherwise.
	 */
	public static boolean isIncluded(DOMRange node, int offset) {
		if (node == null) {
			return false;
		}
		return isIncluded(node.getStart(), node.getEnd(), offset);
	}

	public static boolean isIncluded(int start, int end, int offset) {
		return offset >= start && offset <= end;
	}

	public DOMAttr findAttrAt(int offset) {
		DOMNode node = findNodeAt(offset);
		return findAttrAt(node, offset);
	}

	public static DOMAttr findAttrAt(DOMNode node, int offset) {
		if (node != null && node.hasAttributes()) {
			for (DOMAttr attr : node.getAttributeNodes()) {
				if (attr.isIncluded(offset)) {
					return attr;
				}
			}
		}
		return null;
	}

	public static DOMNode findNodeOrAttrAt(DOMDocument document, int offset) {
		DOMNode node = document.findNodeAt(offset);
		if (node != null) {
			DOMAttr attr = findAttrAt(node, offset);
			if (attr != null) {
				return attr;
			}
		}
		return node;
	}

	/**
	 * Takes a sorted array and a function p. The array is sorted in such a way that
	 * all elements where p(x) is false are located before all elements where p(x)
	 * is true.
	 * 
	 * @returns the least x for which p(x) is true or array.length if no element
	 *          full fills the given function.
	 */
	private static <T> int findFirst(List<T> array, Function<T, Boolean> p) {
		int low = 0, high = array.size();
		if (high == 0) {
			return 0; // no children
		}
		while (low < high) {
			int mid = (int) Math.floor((low + high) / 2);
			if (p.apply(array.get(mid))) {
				high = mid;
			} else {
				low = mid + 1;
			}
		}
		return low;
	}

	public DOMAttr getAttributeNode(String name) {
		return getAttributeNode(null, name);
	}

	/**
	 * Returns the attribute that matches the given name.
	 * 
	 * If there is no namespace, set prefix to null.
	 */
	public DOMAttr getAttributeNode(String prefix, String suffix) {
		StringBuilder sb = new StringBuilder();
		if (prefix != null) {
			sb.append(prefix);
			sb.append(":");
		}
		sb.append(suffix);
		String name = sb.toString();
		if (!hasAttributes()) {
			return null;
		}
		for (DOMAttr attr : attributeNodes) {
			if (name.equals(attr.getName())) {
				return attr;
			}
		}
		return null;
	}

	public String getAttribute(String name) {
		DOMAttr attr = getAttributeNode(name);
		String value = attr != null ? attr.getValue() : null;
		if (value == null) {
			return null;
		}
		if (value.isEmpty()) {
			return value;
		}
		// remove quote
		char c = value.charAt(0);
		if (c == '"' || c == '\'') {
			if (value.charAt(value.length() - 1) == c) {
				return value.substring(1, value.length() - 1);
			}
			return value.substring(1, value.length());
		}
		return value;
	}

	/**
	 * Returns the attribute at the given index, the order is how the attributes
	 * appear in the document.
	 * 
	 * @param index Starting at 0, index of attribute you want
	 * @return
	 */
	public DOMAttr getAttributeAtIndex(int index) {
		if (!hasAttributes()) {
			return null;
		}

		if (index > attributeNodes.getLength() - 1) {
			return null;
		}
		return attributeNodes.get(index);
	}

	public boolean hasAttribute(String name) {
		return hasAttributes() && getAttributeNode(name) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#hasAttributes()
	 */
	@Override
	public boolean hasAttributes() {
		return attributeNodes != null && attributeNodes.size() != 0;
	}

	public void setAttribute(String name, String value) {
		DOMAttr attr = getAttributeNode(name);
		if (attr == null) {
			attr = new DOMAttr(name, this);
			setAttributeNode(attr);
		}
		attr.setValue(value);
	}

	public void setAttributeNode(DOMAttr attr) {
		if (attributeNodes == null) {
			attributeNodes = new XMLNamedNodeMap<>();
		}
		attributeNodes.add(attr);
	}

	public List<DOMAttr> getAttributeNodes() {
		return attributeNodes;
	}

	/**
	 * Returns a list of children, each having an attribute called name, with a
	 * value of value
	 * 
	 * @param name  name of attribute
	 * @param value value of attribute
	 * @return list of children, each having a specified attribute name and value
	 */
	public List<DOMNode> getChildrenWithAttributeValue(String name, String value) {
		List<DOMNode> result = new ArrayList<>();
		for (DOMNode child : getChildren()) {
			if (child.hasAttribute(name)) {
				String attrValue = child.getAttribute(name);
				if (Objects.equals(attrValue, value)) {
					result.add(child);
				}
			}
		}
		return result;
	}

	/**
	 * Returns the node children.
	 * 
	 * @return the node children.
	 */
	public List<DOMNode> getChildren() {
		if (children == null) {
			return Collections.emptyList();
		}
		return children;
	}

	/**
	 * Add node child and set child.parent to {@code this}
	 * 
	 * @param child the node child to add.
	 */
	public void addChild(DOMNode child) {
		child.parent = this;
		if (children == null) {
			children = new XMLNodeList<>();
		}
		getChildren().add(child);
	}

	/**
	 * Returns node child at the given index.
	 * 
	 * @param index
	 * @return node child at the given index.
	 */
	public DOMNode getChild(int index) {
		return getChildren().get(index);
	}

	public boolean isClosed() {
		return closed;
	}

	public DOMElement getParentElement() {
		DOMNode parent = getParentNode();
		DOMDocument ownerDocument = getOwnerDocument();
		while (parent != null && parent != ownerDocument) {
			if (parent.isElement()) {
				return (DOMElement) parent;
			}
			parent = parent.getParentNode();
		}
		return null;
	}

	public boolean isComment() {
		return getNodeType() == DOMNode.COMMENT_NODE;
	}

	public boolean isProcessingInstruction() {
		return (getNodeType() == DOMNode.PROCESSING_INSTRUCTION_NODE)
				&& ((DOMProcessingInstruction) this).isProcessingInstruction();
	}

	public boolean isProlog() {
		return (getNodeType() == DOMNode.PROCESSING_INSTRUCTION_NODE) && ((DOMProcessingInstruction) this).isProlog();
	}

	public boolean isCDATA() {
		return getNodeType() == DOMNode.CDATA_SECTION_NODE;
	}

	public boolean isDoctype() {
		return getNodeType() == DOMNode.DOCUMENT_TYPE_NODE;
	}

	public boolean isGenericDTDDecl() {
		return getNodeType() == DOMNode.DTD_DECL_NODE;
	}

	public boolean isElement() {
		return getNodeType() == DOMNode.ELEMENT_NODE;
	}

	public boolean isAttribute() {
		return getNodeType() == DOMNode.ATTRIBUTE_NODE;
	}

	public boolean isText() {
		return getNodeType() == DOMNode.TEXT_NODE;
	}

	public boolean isCharacterData() {
		return isCDATA() || isText() || isProcessingInstruction() || isComment();
	}

	public boolean isDTDElementDecl() {
		return getNodeType() == DOMNode.DTD_ELEMENT_DECL_NODE;
	}

	public boolean isDTDAttListDecl() {
		return getNodeType() == DOMNode.DTD_ATT_LIST_NODE;
	}

	public boolean isDTDEntityDecl() {
		return getNodeType() == Node.ENTITY_NODE;
	}

	public boolean isDTDNotationDecl() {
		return getNodeType() == DOMNode.DTD_NOTATION_DECL;
	}

	public boolean isOwnerDocument() {
		return getNodeType() == Node.DOCUMENT_NODE;
	}

	public boolean isChildOfOwnerDocument() {
		if (parent == null) {
			return false;
		}
		return parent.getNodeType() == Node.DOCUMENT_NODE;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public int getEnd() {
		return end;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getLocalName()
	 */
	@Override
	public String getLocalName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getParentNode()
	 */
	@Override
	public DOMNode getParentNode() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getFirstChild()
	 */
	@Override
	public DOMNode getFirstChild() {
		return this.children != null && children.size() > 0 ? this.children.get(0) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getLastChild()
	 */
	@Override
	public DOMNode getLastChild() {
		return this.children != null && this.children.size() > 0 ? this.children.get(this.children.size() - 1) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getAttributes()
	 */
	@Override
	public NamedNodeMap getAttributes() {
		return attributeNodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getChildNodes()
	 */
	@Override
	public NodeList getChildNodes() {
		return children != null ? children : EMPTY_CHILDREN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#appendChild(org.w3c.dom.Node)
	 */
	@Override
	public org.w3c.dom.Node appendChild(org.w3c.dom.Node newChild) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#cloneNode(boolean)
	 */
	@Override
	public org.w3c.dom.Node cloneNode(boolean deep) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#compareDocumentPosition(org.w3c.dom.Node)
	 */
	@Override
	public short compareDocumentPosition(org.w3c.dom.Node other) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getBaseURI()
	 */
	@Override
	public String getBaseURI() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getFeature(java.lang.String, java.lang.String)
	 */
	@Override
	public Object getFeature(String arg0, String arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getNamespaceURI() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNextSibling()
	 */
	@Override
	public DOMNode getNextSibling() {
		DOMNode parentNode = getParentNode();
		if (parentNode == null) {
			return null;
		}
		List<DOMNode> children = parentNode.getChildren();
		int nextIndex = children.indexOf(this) + 1;
		return nextIndex < children.size() ? children.get(nextIndex) : null;
	}

	@Override
	public String getNodeValue() throws DOMException {
		return null;
	}

	@Override
	public String getPrefix() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getPreviousSibling()
	 */
	@Override
	public DOMNode getPreviousSibling() {
		DOMNode parentNode = getParentNode();
		if (parentNode == null) {
			return null;
		}
		List<DOMNode> children = parentNode.getChildren();
		int previousIndex = children.indexOf(this) - 1;
		return previousIndex >= 0 ? children.get(previousIndex) : null;
	}

	public DOMNode getPreviousNonTextSibling() {
		DOMNode prev = getPreviousSibling();
		while (prev != null && prev.isText()) {
			prev = prev.getPreviousSibling();
		}
		return prev;
	}

	public DOMElement getOrphanEndElement(int offset, String tagName) {
		DOMNode next = getNextSibling();
		if (next == null || !next.isElement()) {
			return null;
		}
		// emp| </employe>
		DOMElement nextElement = (DOMElement) next;
		if (nextElement.isOrphanEndTagOf(tagName)) {
			return nextElement;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getTextContent()
	 */
	@Override
	public String getTextContent() throws DOMException {
		return getNodeValue();
	}

	@Override
	public Object getUserData(String arg0) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#hasChildNodes()
	 */
	@Override
	public boolean hasChildNodes() {
		return children != null && !children.isEmpty();
	}

	@Override
	public org.w3c.dom.Node insertBefore(org.w3c.dom.Node arg0, org.w3c.dom.Node arg1) throws DOMException {
		return null;
	}

	@Override
	public boolean isDefaultNamespace(String arg0) {
		return false;
	}

	@Override
	public boolean isEqualNode(org.w3c.dom.Node arg0) {
		return false;
	}

	@Override
	public boolean isSameNode(org.w3c.dom.Node arg0) {
		return false;
	}

	@Override
	public boolean isSupported(String arg0, String arg1) {
		return false;
	}

	@Override
	public String lookupNamespaceURI(String arg0) {
		return null;
	}

	@Override
	public String lookupPrefix(String arg0) {
		return null;
	}

	@Override
	public void normalize() {
	}

	@Override
	public org.w3c.dom.Node removeChild(org.w3c.dom.Node arg0) throws DOMException {
		return null;
	}

	@Override
	public org.w3c.dom.Node replaceChild(org.w3c.dom.Node arg0, org.w3c.dom.Node arg1) throws DOMException {
		return null;
	}

	@Override
	public void setNodeValue(String arg0) throws DOMException {
	}

	@Override
	public void setPrefix(String arg0) throws DOMException {
	}

	@Override
	public void setTextContent(String arg0) throws DOMException {
	}

	@Override
	public Object setUserData(String arg0, Object arg1, UserDataHandler arg2) {
		return null;
	}

}