package com.itsaky.lsp.services;

import java.util.List;
import java9.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.CreateFilesParams;
import org.eclipse.lsp4j.DeleteFilesParams;
import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.DidChangeWorkspaceFoldersParams;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.RenameFilesParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.WorkspaceSymbolParams;
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;

public interface IDEWorkspaceService {
	/**
	 * The workspace/executeCommand request is sent from the client to the
	 * server to trigger command execution on the server. In most cases the
	 * server creates a WorkspaceEdit structure and applies the changes to the
	 * workspace using the request workspace/applyEdit which is sent from the
	 * server to the client.
	 *
	 * Registration Options: ExecuteCommandRegistrationOptions
	 */
	@JsonRequest
	default CompletableFuture<Object> executeCommand(ExecuteCommandParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The workspace symbol request is sent from the client to the server to
	 * list project-wide symbols matching the query string.
	 *
	 * Registration Options: void
	 */
	@JsonRequest
	default CompletableFuture<List<? extends SymbolInformation>> symbol(WorkspaceSymbolParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A notification sent from the client to the server to signal the change of
	 * configuration settings.
	 */
	@JsonNotification
	void didChangeConfiguration(DidChangeConfigurationParams params);

	/**
	 * The watched files notification is sent from the client to the server when
	 * the client detects changes to file watched by the language client.
	 */
	@JsonNotification
	void didChangeWatchedFiles(DidChangeWatchedFilesParams params);

	/**
	 * The workspace/didChangeWorkspaceFolders notification is sent from the client
	 * to the server to inform the server about workspace folder configuration changes.
	 * The notification is sent by default if both ServerCapabilities/workspaceFolders
	 * and ClientCapabilities/workspace/workspaceFolders are true; or if the server has
	 * registered to receive this notification it first.
	 * 
	 * Since 3.6.0
	 */
	@JsonNotification
	default void didChangeWorkspaceFolders(DidChangeWorkspaceFoldersParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The will create files request is sent from the client to the server before files
	 * are actually created as long as the creation is triggered from within the client.
	 * The request can return a {@link WorkspaceEdit} which will be applied to workspace
	 * before the files are created. Please note that clients might drop results if computing
	 * the edit took too long or if a server constantly fails on this request. This is
	 * done to keep creates fast and reliable.
	 *
	 * Since 3.16.0
	 */
	@JsonRequest
	default CompletableFuture<WorkspaceEdit> willCreateFiles(CreateFilesParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The did create files notification is sent from the client to the server when files
	 * were created from within the client.
	 * 
	 * Since 3.16.0
	 */
	@JsonNotification
	default void didCreateFiles(CreateFilesParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The will rename files request is sent from the client to the server before files
	 * are actually renamed as long as the rename is triggered from within the client.
	 * The request can return a {@link WorkspaceEdit} which will be applied to workspace
	 * before the files are renamed. Please note that clients might drop results if computing
	 * the edit took too long or if a server constantly fails on this request. This is
	 * done to keep renames fast and reliable.
	 *
	 * Since 3.16.0
	 */
	@JsonRequest
	default CompletableFuture<WorkspaceEdit> willRenameFiles(RenameFilesParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The did rename files notification is sent from the client to the server when files
	 * were renamed from within the client.
	 * 
	 * Since 3.16.0
	 */
	@JsonNotification
	default void didRenameFiles(RenameFilesParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The will delete files request is sent from the client to the server before files
	 * are actually deleted as long as the deletion is triggered from within the client.
	 * The request can return a {@link WorkspaceEdit} which will be applied to workspace
	 * before the files are deleted. Please note that clients might drop results if computing
	 * the edit took too long or if a server constantly fails on this request. This is
	 * done to keep deletes fast and reliable.
	 *
	 * Since 3.16.0
	 */
	@JsonRequest
	default CompletableFuture<WorkspaceEdit> willDeleteFiles(DeleteFilesParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The did delete files notification is sent from the client to the server when files
	 * were deleted from within the client.
	 * 
	 * Since 3.16.0
	 */
	@JsonNotification
	default void didDeleteFiles(DeleteFilesParams params) {
		throw new UnsupportedOperationException();
	}
}
