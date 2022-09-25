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

import java.util.List;

import org.eclipse.lemminx.utils.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

/**
 * An attribute node.
 *
 */
public class DOMAttr extends DOMNode implements org.w3c.dom.Attr {

	public static final String XMLNS_ATTR = "xmlns";
	public static final String XMLNS_NO_DEFAULT_ATTR = "xmlns:";

	private String name;

	private final AttrName nodeAttrName;

	private int delimiter;

	private AttrValue nodeAttrValue;

	private String quotelessValue;// Value without quotes

	private String originalValue;// Exact value from document

	private final DOMNode ownerElement;

	abstract class AttrNameOrValue implements DOMRange {

		private final int start;

		private final int end;

		public AttrNameOrValue(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public int getStart() {
			return start;
		}

		@Override
		public int getEnd() {
			return end;
		}

		public DOMAttr getOwnerAttr() {
			return DOMAttr.this;
		}

		@Override
		public DOMDocument getOwnerDocument() {
			return getOwnerAttr().getOwnerDocument();
		}

		public String getContent() {
			return getOwnerDocument().getText().substring(getStart(), getEnd());
		}
	}

	class AttrName extends AttrNameOrValue {

		public AttrName(int start, int end) {
			super(start, end);
		}
	}

	class AttrValue extends AttrNameOrValue {

		public AttrValue(int start, int end) {
			super(start, end);
		}

		@Override
		public String getContent() {
			if (getOwnerAttr().delimiter < getStart()) {
				return super.getContent();
			}
			return null;
		}
	}

	public DOMAttr(String name, DOMNode ownerElement) {
		this(name, NULL_VALUE, NULL_VALUE, ownerElement);
	}

	public DOMAttr(String name, int start, int end, DOMNode ownerElement) {
		super(NULL_VALUE, NULL_VALUE);
		this.name = name;
		this.delimiter = NULL_VALUE;
		this.nodeAttrName = start != -1 ? new AttrName(start, end) : null;
		this.ownerElement = ownerElement;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.Node#getNodeType()
	 */
	@Override
	public short getNodeType() {
		return DOMNode.ATTRIBUTE_NODE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.Node#getNodeName()
	 */
	@Override
	public String getNodeName() {
		return getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.Attr#getName()
	 */
	@Override
	public String getName() {
		if (name == null && nodeAttrName != null) {
			name = nodeAttrName.getContent();
		}
		return name;
	}

	@Override
	public String getNodeValue() throws DOMException {
		return getValue();
	}

	@Override
	public String getLocalName() {
		String name = getName();
		int colonIndex = name.indexOf(":");
		if (colonIndex > 0) {
			return name.substring(colonIndex + 1);
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.Attr#getOwnerElement()
	 */
	public DOMElement getOwnerElement() {
		return ownerElement.isElement() ? (DOMElement) ownerElement : null;
	}

	@Override
	public DOMDocument getOwnerDocument() {
		return ownerElement != null ? ownerElement.getOwnerDocument() : null;
	}

	/*
	 *
	 * Returns the attribute's value without quotes.
	 */
	@Override
	public String getValue() {
		getOriginalValue();
		return quotelessValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.Attr#getSchemaTypeInfo()
	 */
	@Override
	public TypeInfo getSchemaTypeInfo() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.Attr#getSpecified()
	 */
	@Override
	public boolean getSpecified() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.Attr#isId()
	 */
	@Override
	public boolean isId() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.Attr#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) throws DOMException {
		setValue(value, NULL_VALUE, NULL_VALUE);
	}

	public DOMRange getNodeAttrName() {
		return nodeAttrName;
	}

	public void setDelimiter(int delimiter) {
		this.delimiter = delimiter;
	}

	public boolean hasDelimiter() {
		return delimiter != NULL_VALUE;
	}

	/**
	 * Get original attribute value from the document.
	 *
	 * This will include quotations (", ').
	 *
	 * @return attribute value with quotations if it had them.
	 */
	public String getOriginalValue() {
		if (originalValue == null && nodeAttrValue != null) {
			originalValue = nodeAttrValue.getContent();
			this.quotelessValue = StringUtils.convertToQuotelessValue(originalValue);
		}
		return originalValue;
	}

	public void setValue(String value, int start, int end) {
		this.originalValue = value;
		this.quotelessValue = StringUtils.convertToQuotelessValue(value);
		this.nodeAttrValue = start != -1 ? new AttrValue(start, end) : null;
	}

	public DOMRange getNodeAttrValue() {
		return nodeAttrValue;
	}

	public boolean valueContainsOffset(int offset) {
		return nodeAttrValue != null && offset >= nodeAttrValue.getStart() && offset < nodeAttrValue.getEnd();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.Node#getPrefix()
	 */
	@Override
	public String getPrefix() {
		String name = getName();
		if (name == null) {
			return null;
		}
		String prefix = null;
		int index = name.indexOf(":"); //$NON-NLS-1$
		if (index != -1) {
			prefix = name.substring(0, index);
		}
		return prefix;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.Node#getNamespaceURI()
	 */
	@Override
	public String getNamespaceURI() {
		if (ownerElement == null || ownerElement.getNodeType() != Node.ELEMENT_NODE) {
			return null;
		}
		String prefix = getPrefix();
		// Try to get xmlns attribute from the element
		return ((DOMElement) ownerElement).getNamespaceURI(prefix);
	}

	/**
	 * Returns true if attribute name is a xmlns attribute and false otherwise.
	 *
	 * @param attributeName
	 * @return true if attribute name is a xmlns attribute and false otherwise.
	 */
	public boolean isXmlns() {
		return isXmlns(getName());
	}

	public static boolean isXmlns(String attributeName) {
		return attributeName.startsWith(XMLNS_ATTR);
	}

	/**
	 * Returns true if attribute name is the default xmlns attribute and false
	 * otherwise.
	 *
	 * @param attributeName
	 * @return true if attribute name is the default xmlns attribute and false
	 *         otherwise.
	 */
	public boolean isDefaultXmlns() {
		return isDefaultXmlns(getName());
	}

	public static boolean isDefaultXmlns(String attributeName) {
		return attributeName.equals(XMLNS_ATTR);
	}

	public String extractPrefixFromXmlns() {
		String name = getName();
		if (isDefaultXmlns()) {
			return name.substring(XMLNS_ATTR.length(), name.length());
		}
		return name.substring(XMLNS_NO_DEFAULT_ATTR.length(), name.length());
	}

	/**
	 * Returns the prefix if the given URI matches this attributes value.
	 *
	 * If the URI doesnt match, null is returned.
	 *
	 * @param uri
	 * @return
	 */
	public String getPrefixIfMatchesURI(String uri) {
		if (isXmlns()) {
			String value = getValue();
			if (value != null && value.equals(uri)) {
				if (isDefaultXmlns()) {
					// xmlns="http://"
					return null;
				}
				// xmlns:xxx="http://"
				return extractPrefixFromXmlns();
			}
		}
		return null;
	}

	/**
	 * Returns true if attribute name is the no default xmlns attribute and false
	 * otherwise.
	 *
	 * @param attributeName
	 * @return true if attribute name is the no default xmlns attribute and false
	 *         otherwise.
	 */
	public boolean isNoDefaultXmlns() {
		return isNoDefaultXmlns(getName());
	}

	public static boolean isNoDefaultXmlns(String attributeName) {
		return attributeName.startsWith(XMLNS_NO_DEFAULT_ATTR);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.Node#getNextSibling()
	 */
	@Override
	public DOMNode getNextSibling() {
		DOMNode parentNode = getOwnerElement();
		if (parentNode == null) {
			return null;
		}
		List<DOMAttr> children = parentNode.getAttributeNodes();
		int nextIndex = children.indexOf(this) + 1;
		return nextIndex < children.size() ? children.get(nextIndex) : null;
	}

	public boolean isIncluded(int offset) {
		return DOMNode.isIncluded(getStart(), getEnd(), offset);
	}

	@Override
	public int getStart() {
		return nodeAttrName.getStart();
	}

	@Override
	public int getEnd() {
		if (nodeAttrValue != null) {
			// <foo attr="value"| >
			return nodeAttrValue.getEnd();
		}
		if (hasDelimiter()) {
			// <foo attr=| >
			return delimiter + 1;
		}
		// <foo attr| >
		return nodeAttrName.getEnd();
	}

	@Override
	public int hashCode() {
		String name = getName();
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((quotelessValue == null) ? 0 : quotelessValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DOMAttr other = (DOMAttr) obj;
		String name = getName();
		if (name == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!name.equals(other.getName())) {
			return false;
		}
		String value = getValue();
		if (value == null) {
			if (other.getValue() != null) {
				return false;
			}
		} else if (!value.equals(other.getValue())) {
			return false;
		}
		return true;
	}

	public int getDelimiterOffset() {
		return delimiter;
	}

}
