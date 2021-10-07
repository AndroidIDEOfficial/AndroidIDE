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
package org.eclipse.lemminx.settings;

import org.eclipse.lsp4j.CompletionCapabilities;

/**
 * A wrapper around LSP {@link CompletionCapabilities}.
 *
 */
public class XMLCompletionSettings {

	private CompletionCapabilities completionCapabilities;

	private boolean autoCloseTags;

	private boolean autoCloseRemovesContent;

	public XMLCompletionSettings(boolean autoCloseTags, boolean autoCloseRemovesContent) {
		this.autoCloseTags = autoCloseTags;
		this.autoCloseRemovesContent = autoCloseRemovesContent;
	}

	public XMLCompletionSettings() {
		this(true, true);
	}

	public void setCapabilities(CompletionCapabilities completionCapabilities) {
		this.completionCapabilities = completionCapabilities;
	}

	public CompletionCapabilities getCompletionCapabilities() {
		return completionCapabilities;
	}

	/**
	 * Tag should be autoclosed with an end tag.
	 *
	 * @param autoCloseTags
	 */
	public void setAutoCloseTags(boolean autoCloseTags) {
		this.autoCloseTags = autoCloseTags;
	}

	/**
	 * If tag should be autoclosed with an end tag.
	 *
	 * @return
	 */
	public boolean isAutoCloseTags() {
		return autoCloseTags;
	}

	/**
	 * If turning a start tag into a self closing tag should remove the content of the element
	 *
	 * @param autoCloseRemovesContent
	 */
	public void setAutoCloseRemovesContent(boolean autoCloseRemovesContent) {
		this.autoCloseRemovesContent = autoCloseRemovesContent;
	}

	/**
	 * Returns true if turning a start tag into a self closing tag should remove the content of the element and false otherwise
	 *
	 * @return true if turning a start tag into a self closing tag should remove the content of the element and false otherwise
	 */
	public boolean isAutoCloseRemovesContent() {
		return autoCloseRemovesContent;
	}

	/**
	 * Returns <code>true</code> if the client support snippet and
	 * <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if the client support snippet and
	 *         <code>false</code> otherwise.
	 */
	public boolean isCompletionSnippetsSupported() {
		return completionCapabilities != null && completionCapabilities.getCompletionItem() != null
				&& completionCapabilities.getCompletionItem().getSnippetSupport() != null
				&& completionCapabilities.getCompletionItem().getSnippetSupport();
	}

	/**
	 * Merge only the given completion settings (and not the capability) in the
	 * settings.
	 *
	 * @param newCompletion the new settings to merge.
	 */
	public void merge(XMLCompletionSettings newCompletion) {
		this.setAutoCloseTags(newCompletion.isAutoCloseTags());
		this.setAutoCloseRemovesContent(newCompletion.isAutoCloseRemovesContent());
	}
}
