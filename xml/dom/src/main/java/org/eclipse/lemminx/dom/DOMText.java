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

import org.eclipse.lemminx.utils.StringUtils;
import org.w3c.dom.DOMException;

/**
 * A Text node.
 *
 */
public class DOMText extends DOMCharacterData implements org.w3c.dom.Text {

	public DOMText(int start, int end) {
		super(start, end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNodeType()
	 */
	@Override
	public short getNodeType() {
		return DOMNode.TEXT_NODE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNodeName()
	 */
	@Override
	public String getNodeName() {
		return "#text";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Text#getWholeText()
	 */
	@Override
	public String getWholeText() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Text#isElementContentWhitespace()
	 */
	@Override
	public boolean isElementContentWhitespace() {
		String text = getOwnerDocument().getOwnerDocument().getText();
		return StringUtils.isWhitespace(text, getStart(), getEnd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Text#replaceWholeText(java.lang.String)
	 */
	@Override
	public DOMText replaceWholeText(String content) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Text#splitText(int)
	 */
	@Override
	public DOMText splitText(int offset) throws DOMException {
		throw new UnsupportedOperationException();
	}
}
