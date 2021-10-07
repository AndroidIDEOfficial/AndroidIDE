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
package org.eclipse.lemminx.services.extensions;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.LineIndentInfo;
import org.eclipse.lsp4j.Position;

public interface IPositionRequest {

	/**
	 * Returns the offset where completion was triggered.
	 * 
	 * @return the offset where completion was triggered
	 */
	int getOffset();

	/**
	 * Returns the position
	 * 
	 * @return the position
	 */
	Position getPosition();

	/**
	 * Returns the node where completion was triggered.
	 * 
	 * @return the offset where completion was triggered
	 */
	DOMNode getNode();

	/**
	 * Returns the parent element of the node where completion was triggered and
	 * null otherwise.
	 * 
	 * @return the parent element of the node where completion was triggered and
	 *         null otherwise.
	 */
	DOMElement getParentElement();

	/**
	 * Returns the XML document.
	 * 
	 * @return the XML document.
	 */
	DOMDocument getXMLDocument();

	String getCurrentTag();

	String getCurrentAttributeName();

	/**
	 * Returns the line indent information of the offset where completion was
	 * triggered.
	 * 
	 * @return
	 * @throws BadLocationException
	 */
	LineIndentInfo getLineIndentInfo() throws BadLocationException;

	<T> T getComponent(Class clazz);
}
