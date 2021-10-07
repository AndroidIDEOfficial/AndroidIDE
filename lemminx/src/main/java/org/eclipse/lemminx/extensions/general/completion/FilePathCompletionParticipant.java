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

package org.eclipse.lemminx.extensions.general.completion;

import static org.eclipse.lemminx.utils.FilesUtils.getFilePathSlash;
import static org.eclipse.lemminx.utils.StringUtils.isEmpty;
import static org.eclipse.lemminx.utils.platform.Platform.isWindows;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.CompletionParticipantAdapter;
import org.eclipse.lemminx.services.extensions.ICompletionRequest;
import org.eclipse.lemminx.services.extensions.ICompletionResponse;
import org.eclipse.lemminx.utils.CompletionSortTextHelper;
import org.eclipse.lemminx.utils.FilesUtils;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

/**
 * Extension to support completion for file, folder path in:
 *
 * <ul>
 * <li>attribute value:
 *
 * <pre>
 * &lt;item path="file:///C:/folder" /&gt;
 * &lt;item path="file:///C:/folder file:///C:/file.txt" /&gt;
 * &lt;item path="/folder" /&gt;
 * </pre>
 *
 * </li>
 * <li>DTD DOCTYPE SYSTEM
 *
 * <pre>
 * &lt;!DOCTYPE parent SYSTEM "file.dtd"&gt;
 * </pre>
 *
 * </li>
 *
 * </ul>
 *
 * <p>
 *
 * </p>
 */
public class FilePathCompletionParticipant extends CompletionParticipantAdapter {

	private static final Logger LOGGER = Logger.getLogger(FilePathCompletionParticipant.class.getName());

	@Override
	public void onAttributeValue(String value, ICompletionRequest request, ICompletionResponse response,
			CancelChecker cancelChecker) throws Exception {
		// File path completion on attribute value
		addCompletionItems(value, request, response);
	}

	@Override
	public void onDTDSystemId(String value, ICompletionRequest request, ICompletionResponse response,
			CancelChecker cancelChecker) throws Exception {
		// File path completion on DTD DOCTYPE SYSTEM
		addCompletionItems(value, request, response);
	}

	private static void addCompletionItems(String value, ICompletionRequest request, ICompletionResponse response)
			throws Exception {
		String fullValue = value;
		if (isEmpty(fullValue)) {
			return;
		}

		DOMDocument xmlDocument = request.getXMLDocument();
		String text = xmlDocument.getText();

		// Get value and range for file path declared inside the attribute value
		// ex value="file:///C:/fold|er"
		int valuePathStartOffset = xmlDocument.offsetAt(request.getReplaceRange().getStart());
		int endPathOffset = request.getOffset(); // offset after the typed character
		int startPathOffset = StringUtils.getOffsetAfterWhitespace(fullValue, endPathOffset - valuePathStartOffset)
				+ valuePathStartOffset; // first character of URI
		Range filePathRange = XMLPositionUtility.createRange(startPathOffset, endPathOffset, xmlDocument);
		String originalValuePath = text.substring(startPathOffset, endPathOffset);
		// ex: valuePath="file:///C:/fold"
		String valuePath = originalValuePath;
		String slashInAttribute = getFilePathSlash(valuePath);

		boolean hasFileScheme = valuePath.startsWith(FilesUtils.FILE_SCHEME);
		if (hasFileScheme) {
			// remove file:// scheme
			// ex: valuePath="/C:/fold"
			valuePath = FilesUtils.removeFileScheme(valuePath);
			if (valuePath.length() == 0 || valuePath.charAt(0) != '/') {
				// use of 'file://' and the path was not absolute
				return;
			}
			if (isWindows) {
				// For Windows OS, remove the last '/' from file:///
				// ex: valuePath="C:/fold"
				valuePath = valuePath.substring(1, valuePath.length());
				if (valuePath.length() == 1) {
					// only '/', so list Windows Drives
					Range replaceRange = adjustReplaceRange(xmlDocument, filePathRange, originalValuePath, "/");
					File[] drives = File.listRoots();
					for (File drive : drives) {
						createFilePathCompletionItem(drive, replaceRange, response, "/");
					}
					return;
				}
			}
		}
		// On Linux, Mac OS replace '\\' with '/'
		if (!isWindows) {
			if ("\\".equals(slashInAttribute)) { // Backslash used in Unix
				valuePath = valuePath.replace("\\", "/");
			}
		}

		// Get IO path from the given value path
		Path validAttributePath = getPath(valuePath, xmlDocument.getTextDocument().getUri());
		if (validAttributePath == null) {
			return;
		}

		// Get adjusted range for the completion item (insert at end, or overwrite some
		// existing text in the path)
		Range replaceRange = adjustReplaceRange(xmlDocument, filePathRange, originalValuePath, slashInAttribute);
		createNextValidCompletionPaths(validAttributePath, slashInAttribute, replaceRange, response, null);
	}

	/**
	 * Returns the IO Path from the given value path.
	 *
	 * @param valuePath  the value path
	 * @param xmlFileUri the XML file URI where completion has been triggered.
	 * @return the IO Path from the given value path.
	 */
	private static Path getPath(String valuePath, String xmlFileUri) {
		// the value path is the filepath URI without file://
		try {
			Path validAttributePath = FilesUtils.getPath(valuePath);
			if (!validAttributePath.isAbsolute()) {
				// Absolute path, use the XML file URI folder as base dirctory.
				Path workingDirectoryPath = FilesUtils.getPath(xmlFileUri).getParent();
				validAttributePath = workingDirectoryPath.resolve(validAttributePath).normalize();
			}
			if (!".".equals(valuePath) && !valuePath.endsWith("/") && !valuePath.endsWith("\\")) {
				// ex : C:/folder|/ -> in this case the path is the folder parent (C:)
				validAttributePath = validAttributePath.getParent();
			}
			return Files.exists(validAttributePath) ? validAttributePath : null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns a Range that covers trailing content after a slash, or if it already
	 * ends with a slash then a Range right after it.
	 *
	 * @param xmlDocument
	 * @param fullRange
	 * @param attributeValue
	 * @param slash
	 * @return
	 */
	private static Range adjustReplaceRange(DOMDocument xmlDocument, Range fullRange, String attributeValue,
			String slash) {
		// In the case the currently typed file/directory needs to be overwritten
		Position replaceStart = null;
		Position currentEnd = fullRange.getEnd();

		int startOffset;
		try {
			startOffset = xmlDocument.offsetAt(fullRange.getStart());
		} catch (BadLocationException e) {
			return null;
		}
		int lastSlashIndex = attributeValue.lastIndexOf(slash);
		if (lastSlashIndex > -1) {
			try {
				replaceStart = xmlDocument.positionAt(startOffset + lastSlashIndex);
			} catch (BadLocationException e) {
				return null;
			}
		}
		Range replaceRange = new Range();
		if (replaceStart != null) {
			replaceRange.setStart(replaceStart);
		} else {
			replaceRange.setStart(currentEnd);
		}
		replaceRange.setEnd(currentEnd);
		return replaceRange;
	}

	/**
	 * Creates the completion items based off the given absolute path
	 *
	 * @param pathToAttributeDirectory
	 * @param attributePath
	 * @param replaceRange
	 * @param response
	 * @param filter
	 */
	private static void createNextValidCompletionPaths(Path pathToAttributeDirectory, String slash, Range replaceRange,
			ICompletionResponse response, FilenameFilter filter) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(pathToAttributeDirectory)) {
			for (Path entry : stream) {
				createFilePathCompletionItem(entry.toFile(), replaceRange, response, slash);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error while getting files/directories", e);
		}
	}

	private static void createFilePathCompletionItem(File f, Range replaceRange, ICompletionResponse response,
			String slash) {
		CompletionItem item = new CompletionItem();
		String fName = FilesUtils.encodePath(f.getName());
		if (isWindows && fName.isEmpty()) { // Edge case for Windows drive letter
			fName = f.getPath();
			fName = fName.substring(0, fName.length() - 1);
		}
		String insertText;
		insertText = slash + fName;
		item.setLabel(insertText);

		CompletionItemKind kind = f.isDirectory() ? CompletionItemKind.Folder : CompletionItemKind.File;
		item.setKind(kind);

		item.setSortText(CompletionSortTextHelper.getSortText(kind));
		item.setFilterText(insertText);
		item.setTextEdit(Either.forLeft(new TextEdit(replaceRange, insertText)));
		response.addCompletionItem(item);
	}

}