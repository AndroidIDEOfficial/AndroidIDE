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
import org.eclipse.lemminx.extensions.contentmodel.utils.XMLGenerator;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.Range;

/**
 * Completion request API.
 *
 */
public interface ICompletionRequest extends IPositionRequest, ISharedSettingsRequest {

	Range getReplaceRange();

	XMLGenerator getXMLGenerator() throws BadLocationException;

	String getFilterForStartTagName(String tagName);

	String getInsertAttrValue(String value);

	/**
	 * Returns <code>true</code> if the client support snippet and
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if the client support snippet and
	 *         <code>false</code> otherwise.
	 */
	boolean isCompletionSnippetsSupported();

	/**
	 * Returns true if tag should be autoclosed with an end tag and false otherwise.
	 * 
	 * @return true if tag should be autoclosed with an end tag and false otherwise.
	 */
	public boolean isAutoCloseTags();

	/**
	 * Returns the proper insert text format according the support of snippet.
	 * 
	 * @return the proper insert text format according the support of snippet.
	 */
	InsertTextFormat getInsertTextFormat();
}