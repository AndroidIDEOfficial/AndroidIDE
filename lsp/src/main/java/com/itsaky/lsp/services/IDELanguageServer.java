/************************************************************************************
 * This file is part of Java Language Server (https://github.com/itsaky/java-language-server)
 *
 * Copyright (C) 2021 Akash Yadav
 *
 * Java Language Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Java Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Java Language Server.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/

package com.itsaky.lsp.services;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.SetTraceParams;
import org.eclipse.lsp4j.WorkDoneProgressCancelParams;
import org.eclipse.lsp4j.jsonrpc.services.JsonDelegate;
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;

/**
 * Custom implementation of {@link org.eclipse.lsp4j.services.LanguageServer}.
 * This helps providing custom commands and services to Java Language Server
 */
public interface IDELanguageServer {
	/**
	 * The initialize request is sent as the first request from the client to
	 * the server.
	 * 
	 * If the server receives requests or notifications before the initialize request, it should act as follows:
	 *  - for a request, the response should be errored with:
	 *    {@link org.eclipse.lsp4j.jsonrpc.messages.ResponseErrorCode#serverNotInitialized}.
	 *    The message can be picked by the server.
	 *  - notifications should be dropped, except for the exit notification.
	 *    This will allow the client to exit a server without an initialize request.
	 *  
	 * Until the server has responded to the initialize request with an InitializeResult,
	 * the client must not send any additional requests or notifications to the server.
	 * 
	 * During the initialize request, the server is allowed to send the notifications window/showMessage,
	 * window/logMessage, and telemetry/event, as well as the request window/showMessageRequest, to the client.
	 */
	@JsonRequest
	CompletableFuture<InitializeResult> initialize(InitializeParams params);

	/**
	 * The initialized notification is sent from the client to the server after
	 * the client received the result of the initialize request, but before the
	 * client is sending any other request or notification to the server. The
	 * server can use the initialized notification, for example, to dynamically
	 * register capabilities.
	 */
	@JsonNotification
	default void initialized(InitializedParams params) {
		initialized();
	}
	/**
	 * @deprecated see initialized(InitializedParams)
	 */
	@Deprecated
	default void initialized() {
	}

	/**
	 * The shutdown request is sent from the client to the server. It asks the
	 * server to shutdown, but to not exit (otherwise the response might not be
	 * delivered correctly to the client). There is a separate exit notification
	 * that asks the server to exit.
	 */
	@JsonRequest
	CompletableFuture<Object> shutdown();

	/**
	 * A notification to ask the server to exit its process.
	 */
	@JsonNotification
	void exit();

	/**
	 * Provides access to the textDocument services.
	 */
	@JsonDelegate
	IDETextDocumentService getTextDocumentService();

	/**
	 * Provides access to the workspace services.
	 */
	@JsonDelegate
	IDEWorkspaceService getWorkspaceService();
	
	/**
	 * This notification is sent from the client to the server to cancel a progress initiated on the server side.
	 */
	@JsonNotification("window/workDoneProgress/cancel")
	default void cancelProgress(WorkDoneProgressCancelParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A notification that should be used by the client to modify the trace setting of the server.
	 *
	 * Since 3.16.0
	 */
	@JsonNotification("$/setTrace")
	default void setTrace(SetTraceParams params) {
		throw new UnsupportedOperationException();
	}
}