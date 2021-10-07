/**
 *  Copyright (c) 2020 Angelo ZERR.
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

import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.lsp4j.DocumentSymbol;

/**
 * Result for document symbols computation for the textDocument/documentSymbol
 * request
 * 
 */
public class DocumentSymbolsResult extends LimitList<DocumentSymbol> {

	private static final long serialVersionUID = 1L;
	public static final DocumentSymbolsResult EMPTY_LIMITLESS_LIST = new DocumentSymbolsResult(null);

	public DocumentSymbolsResult(AtomicLong limit) {
		super(limit);
	}

	public DocumentSymbolsResult createList() {
		return new DocumentSymbolsResult(getLimit());
	}
}
