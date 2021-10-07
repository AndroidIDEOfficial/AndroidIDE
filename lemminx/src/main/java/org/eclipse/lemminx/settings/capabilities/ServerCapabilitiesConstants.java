/**
 *  Copyright (c) 2018 Red Hat, Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.settings.capabilities;

import java.util.Arrays;
import java.util.UUID;

import org.eclipse.lsp4j.CodeLensOptions;
import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.DocumentLinkOptions;
import org.eclipse.lsp4j.TextDocumentSyncKind;

/**
 * Server Capabilities Constants
 */
public class ServerCapabilitiesConstants {

	private ServerCapabilitiesConstants() {
	}

	public static final String TEXT_DOCUMENT_FORMATTING = "textDocument/formatting";
	public static final String TEXT_DOCUMENT_RANGE_FORMATTING = "textDocument/rangeFormatting";
	public static final String TEXT_DOCUMENT_ON_TYPE_FORMATTING = "textDocument/onTypeFormatting";
	public static final String TEXT_DOCUMENT_CODE_LENS = "textDocument/codeLens";
	public static final String TEXT_DOCUMENT_SIGNATURE_HELP = "textDocument/signatureHelp";
	public static final String TEXT_DOCUMENT_RENAME = "textDocument/rename";
	public static final String TEXT_DOCUMENT_COMPLETION = "textDocument/completion";
	public static final String TEXT_DOCUMENT_SYNC = "textDocument/synchronization";
	public static final String TEXT_DOCUMENT_LINK = "textDocument/documentLink";
	public static final String TEXT_DOCUMENT_FOLDING_RANGE = "textDocument/foldingRange";
	public static final String TEXT_DOCUMENT_DOCUMENT_SYMBOL = "textDocument/documentSymbol";
	public static final String TEXT_DOCUMENT_CODE_ACTION = "textDocument/codeAction";
	public static final String TEXT_DOCUMENT_DEFINITION = "textDocument/definition";
	public static final String TEXT_DOCUMENT_TYPEDEFINITION = "textDocument/typeDefinition";
	public static final String TEXT_DOCUMENT_HOVER = "textDocument/hover";
	public static final String TEXT_DOCUMENT_REFERENCES = "textDocument/references";
	public static final String TEXT_DOCUMENT_LINKED_EDITING_RANGE = "textDocument/linkedEditingRange";
	public static final String TEXT_DOCUMENT_HIGHLIGHT = "textDocument/documentHighlight";
	public static final String TEXT_DOCUMENT_SELECTION_RANGE = "textDocument/selectionRange";

	public static final String WORKSPACE_CHANGE_FOLDERS = "workspace/didChangeWorkspaceFolders";
	public static final String WORKSPACE_EXECUTE_COMMAND = "workspace/executeCommand";
	public static final String WORKSPACE_SYMBOL = "workspace/symbol";
	public static final String WORKSPACE_WATCHED_FILES = "workspace/didChangeWatchedFiles";

	public static final String FORMATTING_ID = UUID.randomUUID().toString();
	public static final String COMPLETION_ID = UUID.randomUUID().toString();
	public static final String SYNC_ID = UUID.randomUUID().toString();
	public static final String FOLDING_RANGE_ID = UUID.randomUUID().toString();
	public static final String LINK_ID = UUID.randomUUID().toString();
	public static final String FORMATTING_ON_TYPE_ID = UUID.randomUUID().toString();
	public static final String FORMATTING_RANGE_ID = UUID.randomUUID().toString();
	public static final String CODE_LENS_ID = UUID.randomUUID().toString();
	public static final String SIGNATURE_HELP_ID = UUID.randomUUID().toString();
	public static final String RENAME_ID = UUID.randomUUID().toString();
	public static final String WORKSPACE_EXECUTE_COMMAND_ID = UUID.randomUUID().toString();
	public static final String WORKSPACE_SYMBOL_ID = UUID.randomUUID().toString();
	public static final String DOCUMENT_SYMBOL_ID = UUID.randomUUID().toString();
	public static final String CODE_ACTION_ID = UUID.randomUUID().toString();
	public static final String DEFINITION_ID = UUID.randomUUID().toString();
	public static final String TYPEDEFINITION_ID = UUID.randomUUID().toString();
	public static final String HOVER_ID = UUID.randomUUID().toString();
	public static final String REFERENCES_ID = UUID.randomUUID().toString();
	public static final String DOCUMENT_HIGHLIGHT_ID = UUID.randomUUID().toString();
	public static final String SELECTION_RANGE_ID = UUID.randomUUID().toString();
	public static final String WORKSPACE_CHANGE_FOLDERS_ID = UUID.randomUUID().toString();
	public static final String WORKSPACE_WATCHED_FILES_ID = UUID.randomUUID().toString();
	public static final String LINKED_EDITING_RANGE_ID = UUID.randomUUID().toString();
	
	public static final CompletionOptions DEFAULT_COMPLETION_OPTIONS = new CompletionOptions(false,
			Arrays.asList(".", ":", "<", "\"", "=", "/", "\\", "?", "\'", "&"));
	public static final TextDocumentSyncKind DEFAULT_SYNC_OPTION = TextDocumentSyncKind.Full;
	public static final DocumentLinkOptions DEFAULT_LINK_OPTIONS = new DocumentLinkOptions(true);
	public static final CodeLensOptions DEFAULT_CODELENS_OPTIONS = new CodeLensOptions();
}