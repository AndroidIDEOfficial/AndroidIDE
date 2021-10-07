/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.commons;

import java.util.function.BiFunction;

import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * The cache of {@link TextDocument} linked to a model.
 * 
 * @author Angelo ZERR
 *
 * @param <T> the model type (ex : DOM Document)
 */
public class ModelTextDocuments<T> extends TextDocuments<ModelTextDocument<T>> {

	private final BiFunction<TextDocument, CancelChecker, T> parse;

	public ModelTextDocuments(BiFunction<TextDocument, CancelChecker, T> parse) {
		this.parse = parse;
	}

	@Override
	public ModelTextDocument<T> createDocument(TextDocumentItem document) {
		ModelTextDocument<T> doc = new ModelTextDocument<T>(document, parse);
		doc.setIncremental(isIncremental());
		return doc;
	}
}