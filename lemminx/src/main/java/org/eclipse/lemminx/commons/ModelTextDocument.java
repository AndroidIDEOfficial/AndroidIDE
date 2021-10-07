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

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.logging.Logger;

import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.CompletableFutures;

/**
 * A {@link TextDocument} which is associate to a model loaded in async.
 * 
 * @author Angelo ZERR
 *
 * @param <T> the model type (ex : DOM Document)
 */
public class ModelTextDocument<T> extends TextDocument {

	private static final Logger LOGGER = Logger.getLogger(ModelTextDocument.class.getName());

	private final BiFunction<TextDocument, CancelChecker, T> parse;

	private CompletableFuture<T> model;

	public ModelTextDocument(TextDocumentItem document, BiFunction<TextDocument, CancelChecker, T> parse) {
		super(document);
		this.parse = parse;
	}

	public ModelTextDocument(String text, String uri, BiFunction<TextDocument, CancelChecker, T> parse) {
		super(text, uri);
		this.parse = parse;
	}

	/**
	 * Returns the completable future which loads the model. The process of parse od
	 * the model is stopped as soon as possible when text content changed.
	 * 
	 * @return the completable future which loads the model.
	 */
	public CompletableFuture<T> getModel() {
		if (model == null) {
			int version = super.getVersion();
			model = CompletableFutures.computeAsync((requestCancelChecker) -> {
				long start = System.currentTimeMillis();
				try {
					LOGGER.fine("Start parsing of model with version '" + version);
					// Stop of parse process can be done when completable future is canceled or when
					// version of document changes
					MultiCancelChecker cancelChecker = new MultiCancelChecker(requestCancelChecker,
							new TextDocumentVersionChecker(this, version));
					// parse the model
					return parse.apply(this, cancelChecker);
				} catch (CancellationException e) {
					LOGGER.fine("Stop parsing parsing of model with version '" + version + "' in "
							+ (System.currentTimeMillis() - start) + "ms");
					throw e;
				} finally {
					LOGGER.fine("End parse of model with version '" + version + "' in "
							+ (System.currentTimeMillis() - start) + "ms");
				}
			});
		}
		return model;
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		// text changed, cancel the completable future which load the model
		cancelModel();
	}

	@Override
	public void setVersion(int version) {
		super.setVersion(version);
		// version changed, cancel the completable future which load the model
		cancelModel();
	}

	/**
	 * Cancel the completable future which loads the model.
	 */
	private void cancelModel() {
		if (model != null) {
			model.cancel(true);
			model = null;
		}
	}

}