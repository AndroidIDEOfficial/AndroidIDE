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
package org.eclipse.lemminx.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionKind;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CreateFile;
import org.eclipse.lsp4j.CreateFileOptions;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ResourceOperation;
import org.eclipse.lsp4j.TextDocumentEdit;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.VersionedTextDocumentIdentifier;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

/**
 * Factory for simple {@link CodeAction}
 *
 */
public class CodeActionFactory {

	/**
	 * Create a CodeAction to remove the content from the given range.
	 *
	 * @param title
	 * @param range
	 * @param document
	 * @param diagnostic
	 * @return
	 */
	public static CodeAction remove(String title, Range range, TextDocumentItem document, Diagnostic diagnostic) {
		return replace(title, range, "", document, diagnostic);
	}

	/**
	 * Create a CodeAction to insert a new content at the end of the given range.
	 *
	 * @param title
	 * @param range
	 * @param insertText
	 * @param document
	 * @param diagnostic
	 *
	 * @return the CodeAction to insert a new content at the end of the given range.
	 */
	public static CodeAction insert(String title, Position position, String insertText, TextDocumentItem document,
			Diagnostic diagnostic) {
		CodeAction insertContentAction = new CodeAction(title);
		insertContentAction.setKind(CodeActionKind.QuickFix);
		insertContentAction.setDiagnostics(Arrays.asList(diagnostic));
		TextDocumentEdit textDocumentEdit = insertEdit(insertText, position, document);
		WorkspaceEdit workspaceEdit = new WorkspaceEdit(Collections.singletonList(Either.forLeft(textDocumentEdit)));

		insertContentAction.setEdit(workspaceEdit);
		return insertContentAction;
	}

	/**
	 * Returns the text edit to insert a new content at the end of the given range.
	 *
	 * @param insertText text to insert.
	 * @param position   the position.
	 * @param document   the text document.
	 *
	 * @return the text edit to insert a new content at the end of the given range.
	 */
	public static TextDocumentEdit insertEdit(String insertText, Position position, TextDocumentItem document) {
		TextEdit edit = insertEdit(insertText, position);
		return insertEdits(document, Collections.singletonList(edit));
	}

	public static TextEdit insertEdit(String insertText, Position position) {
		return new TextEdit(new Range(position, position), insertText);
	}

	public static TextDocumentEdit insertEdits(TextDocumentItem document, List<TextEdit> edits) {
		VersionedTextDocumentIdentifier versionedTextDocumentIdentifier = new VersionedTextDocumentIdentifier(
				document.getUri(), document.getVersion());
		return new TextDocumentEdit(versionedTextDocumentIdentifier, edits);
	}

	public static CodeAction replace(String title, Range range, String replaceText, TextDocumentItem document,
			Diagnostic diagnostic) {
		TextEdit replace = new TextEdit(range, replaceText);
		return replace(title, Collections.singletonList(replace), document, diagnostic);
	}

	public static CodeAction replace(String title, List<TextEdit> replace, TextDocumentItem document,
			Diagnostic diagnostic) {

		CodeAction insertContentAction = new CodeAction(title);
		insertContentAction.setKind(CodeActionKind.QuickFix);
		insertContentAction.setDiagnostics(Arrays.asList(diagnostic));

		VersionedTextDocumentIdentifier versionedTextDocumentIdentifier = new VersionedTextDocumentIdentifier(
				document.getUri(), document.getVersion());
		TextDocumentEdit textDocumentEdit = new TextDocumentEdit(versionedTextDocumentIdentifier, replace);
		WorkspaceEdit workspaceEdit = new WorkspaceEdit(Collections.singletonList(Either.forLeft(textDocumentEdit)));
		insertContentAction.setEdit(workspaceEdit);
		return insertContentAction;
	}

	public static CodeAction replaceAt(String title, String replaceText, TextDocumentItem document,
			Diagnostic diagnostic, Collection<Range> ranges) {
		CodeAction insertContentAction = new CodeAction(title);
		insertContentAction.setKind(CodeActionKind.QuickFix);
		insertContentAction.setDiagnostics(Arrays.asList(diagnostic));

		VersionedTextDocumentIdentifier versionedTextDocumentIdentifier = new VersionedTextDocumentIdentifier(
				document.getUri(), document.getVersion());
		List<TextEdit> edits = new ArrayList<>();
		for (Range range : ranges) {
			TextEdit edit = new TextEdit(range, replaceText);
			edits.add(edit);
		}
		TextDocumentEdit textDocumentEdit = new TextDocumentEdit(versionedTextDocumentIdentifier, edits);
		WorkspaceEdit workspaceEdit = new WorkspaceEdit(Collections.singletonList(Either.forLeft(textDocumentEdit)));

		insertContentAction.setEdit(workspaceEdit);
		return insertContentAction;
	}

	/**
	 * Makes a CodeAction to create a file and add content to the file.
	 *
	 * @param title      The displayed name of the CodeAction
	 * @param docURI     The file to create
	 * @param content    The text to put into the newly created document.
	 * @param diagnostic The diagnostic that this CodeAction will fix
	 */
	public static CodeAction createFile(String title, String docURI, String content, Diagnostic diagnostic) {

		List<Either<TextDocumentEdit, ResourceOperation>> actionsToTake = new ArrayList<>(2);

		// 1. create an empty file
		actionsToTake.add(Either.forRight(new CreateFile(docURI, new CreateFileOptions(false, true))));

		// 2. update the created file with the given content
		VersionedTextDocumentIdentifier identifier = new VersionedTextDocumentIdentifier(docURI, 0);
		TextEdit te = new TextEdit(new Range(new Position(0, 0), new Position(0, 0)), content);
		actionsToTake.add(Either.forLeft(new TextDocumentEdit(identifier, Collections.singletonList(te))));

		WorkspaceEdit createAndAddContentEdit = new WorkspaceEdit(actionsToTake);

		CodeAction codeAction = new CodeAction(title);
		codeAction.setEdit(createAndAddContentEdit);
		codeAction.setDiagnostics(Collections.singletonList(diagnostic));
		codeAction.setKind(CodeActionKind.QuickFix);

		return codeAction;
	}

	/**
	 * Makes a CodeAction to call a command from the available server commands.
	 *
	 * @param title         The displayed name of the CodeAction
	 * @param commandId     The id of the given command to add as CodeAction
	 * @param commandParams The document URI of the document the command is called on
	 * @param diagnostic    The diagnostic that this CodeAction will fix
	 */
	public static CodeAction createCommand(String title, String commandId, List<Object> commandParams, Diagnostic diagnostic) {
		CodeAction codeAction = new CodeAction(title);
		Command command = new Command(title, commandId, commandParams);
		codeAction.setCommand(command);
		codeAction.setDiagnostics(Collections.singletonList(diagnostic));
		codeAction.setKind(CodeActionKind.QuickFix);

		return codeAction;
	}
}
