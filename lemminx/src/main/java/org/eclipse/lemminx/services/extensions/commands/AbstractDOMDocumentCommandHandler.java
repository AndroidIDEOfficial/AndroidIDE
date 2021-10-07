/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.services.extensions.commands;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.IXMLDocumentProvider;
import org.eclipse.lemminx.services.extensions.commands.IXMLCommandService.IDelegateCommandHandler;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Abstract command to work with a given {@link DOMDocument} filled in the first
 * argument of the command as {@link TextDocumentIdentifier}.
 * 
 * @author Angelo ZERR
 *
 */
public abstract class AbstractDOMDocumentCommandHandler implements IDelegateCommandHandler {

	private final IXMLDocumentProvider documentProvider;

	public AbstractDOMDocumentCommandHandler(IXMLDocumentProvider documentProvider) {
		this.documentProvider = documentProvider;
	}

	@Override
	public final Object executeCommand(ExecuteCommandParams params, SharedSettings sharedSettings,
			CancelChecker cancelChecker) throws Exception {
		TextDocumentIdentifier identifier = ArgumentsUtils.getArgAt(params, 0, TextDocumentIdentifier.class);
		String uri = identifier.getUri();
		DOMDocument document = documentProvider.getDocument(uri);
		if (document == null) {
			throw new UnsupportedOperationException(String
					.format("Command '%s' cannot find the DOM document with the URI '%s'.", params.getCommand(), uri));
		}
		return executeCommand(document, params, sharedSettings, cancelChecker);
	}

	/**
	 * Executes a command
	 * 
	 * @param document       the DOM document retrieve by the
	 *                       {@link TextDocumentIdentifier} argument.
	 * 
	 * @param params         command execution parameters
	 * @param sharedSettings the shared settings
	 * @param cancelChecker  check if cancel has been requested
	 * @return the result of the command
	 * @throws Exception the unhandled exception will be wrapped in
	 *                   <code>org.eclipse.lsp4j.jsonrpc.ResponseErrorException</code>
	 *                   and be wired back to the JSON-RPC protocol caller
	 */
	protected abstract Object executeCommand(DOMDocument document, ExecuteCommandParams params,
			SharedSettings sharedSettings, CancelChecker cancelChecker) throws Exception;

}