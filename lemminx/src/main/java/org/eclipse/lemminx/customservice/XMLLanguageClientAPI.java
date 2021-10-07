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
package org.eclipse.lemminx.customservice;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;
import org.eclipse.lsp4j.services.LanguageClient;

/**
 * XML language client API.
 *
 */
@JsonSegment("xml")
public interface XMLLanguageClientAPI extends LanguageClient {

	/**
	 * Notification to be sent to the client with a list of commands
	 * 
	 * @param command
	 */
	@JsonNotification("actionableNotification")
	default void actionableNotification(ActionableNotification command) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Executes the command on the client which gives an opportunity to execute a
	 * command registered by a different Language Server
	 * 
	 * @param params command execution parameters
	 * @return the result of the command execution
	 */
	@JsonRequest("executeClientCommand")
	default CompletableFuture<Object> executeClientCommand(ExecuteCommandParams params) {
		throw new UnsupportedOperationException();
	}
	
}