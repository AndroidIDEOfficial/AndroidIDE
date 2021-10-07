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

import org.eclipse.lemminx.services.extensions.ICompletionResponse;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;

/**
 * Completion response implementation.
 *
 */
class CompletionResponse extends CompletionList implements ICompletionResponse {

	private transient List<String> seenAttributes;
	private transient boolean hasSomeItemFromGrammar;

	public CompletionResponse() {
		super.setIsIncomplete(false);
	}

	public void addCompletionItem(CompletionItem completionItem, boolean fromGrammar) {
		if (fromGrammar) {
			hasSomeItemFromGrammar = true;
		}
		addCompletionItem(completionItem);
	}

	@Override
	public void addCompletionItem(CompletionItem completionItem) {
		super.getItems().add(completionItem);
	}

	@Override
	public boolean hasSomeItemFromGrammar() {
		return hasSomeItemFromGrammar;
	}

	@Override
	public boolean hasAttribute(String attribute) {
		/*
		 * if (node != null && node.hasAttribute(attribute)) { return true; }
		 */
		return seenAttributes != null ? seenAttributes.contains(attribute) : false;
	}

	@Override
	public void addCompletionAttribute(CompletionItem completionItem) {
		if (seenAttributes == null) {
			seenAttributes = new ArrayList<>();
		}
		// TODO: Add quotations to the completion item.
		seenAttributes.add(completionItem.getLabel());
		addCompletionItem(completionItem);
	}

}
