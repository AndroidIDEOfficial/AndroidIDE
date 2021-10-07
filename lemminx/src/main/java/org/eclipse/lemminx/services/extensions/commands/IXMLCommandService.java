/*******************************************************************************
 * Copyright (c) 2020 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.lemminx.services.extensions.commands;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Service available to XML LS extensions to add/remove/execute commands via the
 * XML LS
 * 
 * 
 * @author Alex Boyko
 *
 */
public interface IXMLCommandService {

	/**
	 * Command handler to register with the workspace service
	 */
	@FunctionalInterface
	public interface IDelegateCommandHandler {

		/**
		 * Executes a command
		 * 
		 * @param params        command execution parameters
		 * @param sharedSettings the shared settings.
		 * @param cancelChecker check if cancel has been requested
		 * @return the result of the command
		 * @throws Exception the unhandled exception will be wrapped in
		 *                   <code>org.eclipse.lsp4j.jsonrpc.ResponseErrorException</code>
		 *                   and be wired back to the JSON-RPC protocol caller
		 */
		Object executeCommand(ExecuteCommandParams params, SharedSettings sharedSettings, CancelChecker cancelChecker) throws Exception;
	}

	/**
	 * Registers a command with the language server
	 * 
	 * @param commandId unique id of the command
	 * @param handler   command handler function
	 */
	void registerCommand(String commandId, IDelegateCommandHandler handler);

	/**
	 * Unregisters the command from the language server
	 * 
	 * @param commandId unique id of the command to unregister
	 */
	void unregisterCommand(String commandId);

	/**
	 * Executes a command via the client. The command can be registered by any
	 * language server
	 * 
	 * @param command the LSP command
	 * @return the result of the command
	 */
	CompletableFuture<Object> executeClientCommand(ExecuteCommandParams command);

	default void beginCommandsRegistration() {

	}

	default void endCommandsRegistration() {

	}

}
