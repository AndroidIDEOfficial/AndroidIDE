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

import org.eclipse.lsp4j.CompletionItem;

/**
 * Completion response API.
 *
 */
public interface ICompletionResponse {

	/**
	 * Add completion item and mark as coming from grammar.
	 * 
	 * @param completionItem
	 * @param comingFromGrammar
	 */
	void addCompletionItem(CompletionItem completionItem, boolean comingFromGrammar);

	/**
	 * Add completion item.
	 * 
	 * @param completionItem
	 */
	void addCompletionItem(CompletionItem completionItem);

	boolean hasAttribute(String attribute);

	/**
	 * Add completion attribute.
	 * 
	 * @param item
	 */
	void addCompletionAttribute(CompletionItem item);

	/**
	 * Returns <code>true</code> if there are completion items coming from grammar
	 * and false otherwise.
	 * 
	 * @return <code>true</code> if there are completion items coming from grammar
	 *         and false otherwise.
	 */
	boolean hasSomeItemFromGrammar();

}
