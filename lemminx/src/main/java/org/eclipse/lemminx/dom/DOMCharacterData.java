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

import static java.lang.System.lineSeparator;

import java.util.List;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.utils.StringUtils;
import org.w3c.dom.DOMException;

/**
 * A CharacterData node.
 *
 */
public abstract class DOMCharacterData extends DOMNode implements org.w3c.dom.CharacterData {

	private String data;

	private String normalizedData;

	private boolean isWhitespace;

	private String delimiter;

	public DOMCharacterData(int start, int end) {
		super(start, end);
	}

	public boolean hasMultiLine() {
		return getData().contains(getDelimiter());
	}

	public String getDelimiter() {
		if (delimiter != null) {
			return delimiter;
		}
		try {
			delimiter = getOwnerDocument().getTextDocument().lineDelimiter(0);
			return delimiter;
		} catch (BadLocationException e) {
			delimiter = lineSeparator();
			return delimiter;
		}
	}

	/**
	 * If data ends with a new line character.
	 * 
	 * Returns false if a character is found before a new line. Non-newline
	 * whitespace will be ignored while searching.
	 * 
	 * If no data exists, returns false.
	 * 
	 * @return true if newline character ocurrs before non-whitespace character
	 */
	public boolean endsWithNewLine() {
		if (hasData()) {
			for (int i = data.length() - 1; i >= 0; i--) {
				char c = data.charAt(i);
				if (!Character.isWhitespace(c)) {
					return false;
				}
				if (c == '\n') {
					return true;
				}

			}
		}
		return false;
	}

	/**
	 * If data ends with a new line character.
	 * 
	 * Returns false if a character is found before a new line. Non-newline
	 * whitespace will be ignored while searching.
	 * 
	 * @return true if newline character ocurrs before non-whitespace character
	 */
	public boolean startsWithNewLine() {
		if (hasData()) {
			for (int i = 0; i < data.length(); i++) {
				char c = data.charAt(i);
				if (!Character.isWhitespace(c)) {
					return false;
				}
				if (c == '\n' || c == '\r') {
					return true;
				}

			}
		}
		return false;
	}

	public String getNormalizedData() {
		if (normalizedData == null) {
			normalizedData = StringUtils.normalizeSpace(getData());
		}
		return normalizedData;
	}

	public boolean hasData() {
		return !getData().isEmpty();
	}

	/**
	 * Returns true if this node has sibling nodes.
	 */
	public boolean hasSiblings() {
		List<DOMNode> childrenOfParent = this.parent.getChildren();
		return childrenOfParent.size() > 1;
	}

	public int getStartContent() {
		return start;
	}

	public int getEndContent() {
		return end;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.CharacterData#getData()
	 */
	@Override
	public String getData() {
		if (data == null) {
			data = getOwnerDocument().getText().substring(getStartContent(), getEndContent());
		}
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNodeValue()
	 */
	@Override
	public String getNodeValue() throws DOMException {
		return getData();
	}

	/**
	 * @return the isWhitespace
	 */
	public boolean isWhitespace() {
		return isWhitespace;
	}

	/**
	 * Set true if this node's data is all whitespace
	 * 
	 * @param isWhitespace
	 */
	public void setWhitespace(boolean isWhitespace) {
		this.isWhitespace = isWhitespace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.CharacterData#appendData(java.lang.String)
	 */
	@Override
	public void appendData(String data) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.CharacterData#deleteData(int, int)
	 */
	@Override
	public void deleteData(int offset, int count) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.CharacterData#getLength()
	 */
	@Override
	public int getLength() {
		return getData().length();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.CharacterData#insertData(int, java.lang.String)
	 */
	@Override
	public void insertData(int offset, String data) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.CharacterData#replaceData(int, int, java.lang.String)
	 */
	@Override
	public void replaceData(int offset, int count, String data) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.CharacterData#setData(java.lang.String)
	 */
	@Override
	public void setData(String value) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.CharacterData#substringData(int, int)
	 */
	@Override
	public String substringData(int offset, int count) throws DOMException {
		throw new UnsupportedOperationException();
	}
}
