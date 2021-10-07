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

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentItem;

/**
 * Text document extends LSP4j {@link TextDocumentItem} to provide methods to
 * retrieve position.
 *
 */
public class TextDocument extends TextDocumentItem {

	private static final Logger LOGGER = Logger.getLogger(TextDocument.class.getName());

	private final Object lock = new Object();

	private static String DEFAULT_DELIMTER = System.lineSeparator();

	private ILineTracker lineTracker;

	private boolean incremental;

	public TextDocument(TextDocumentItem document) {
		this(document.getText(), document.getUri());
		super.setVersion(document.getVersion());
		super.setLanguageId(document.getLanguageId());
	}

	public TextDocument(String text, String uri) {
		super.setUri(uri);
		super.setText(text);
	}

	public void setIncremental(boolean incremental) {
		this.incremental = incremental;
		// reset line tracker
		lineTracker = null;
		getLineTracker();
	}

	public boolean isIncremental() {
		return incremental;
	}

	public Position positionAt(int position) throws BadLocationException {
		ILineTracker lineTracker = getLineTracker();
		return lineTracker.getPositionAt(position);
	}

	public int offsetAt(Position position) throws BadLocationException {
		ILineTracker lineTracker = getLineTracker();
		return lineTracker.getOffsetAt(position);
	}

	public String lineText(int lineNumber) throws BadLocationException {
		ILineTracker lineTracker = getLineTracker();
		Line line = lineTracker.getLineInformation(lineNumber);
		String text = super.getText();
		return text.substring(line.offset, line.offset + line.length);
	}

	public String lineDelimiter(int lineNumber) throws BadLocationException {
		ILineTracker lineTracker = getLineTracker();
		String lineDelimiter = lineTracker.getLineDelimiter(lineNumber);
		if (lineDelimiter == null) {
			if (lineTracker.getNumberOfLines() > 0) {
				lineDelimiter = lineTracker.getLineInformation(0).delimiter;
			}
		}
		if (lineDelimiter == null) {
			lineDelimiter = DEFAULT_DELIMTER;
		}
		return lineDelimiter;
	}

	public Range getWordRangeAt(int textOffset, Pattern wordDefinition) {
		try {
			Position pos = positionAt(textOffset);
			ILineTracker lineTracker = getLineTracker();
			Line line = lineTracker.getLineInformation(pos.getLine());
			String text = super.getText();
			String lineText = text.substring(line.offset, textOffset);
			int position = lineText.length();
			Matcher m = wordDefinition.matcher(lineText);
			int currentPosition = 0;
			while (currentPosition != position) {
				if (m.find()) {
					currentPosition = m.end();
					if (currentPosition == position) {
						return new Range(new Position(pos.getLine(), m.start()), pos);
					}
				} else {
					currentPosition++;
				}
				m.region(currentPosition, position);
			}
			return new Range(pos, pos);
		} catch (BadLocationException e) {
			return null;
		}
	}

	private ILineTracker getLineTracker() {
		if (lineTracker == null) {
			lineTracker = createLineTracker();
		}
		return lineTracker;
	}

	private synchronized ILineTracker createLineTracker() {
		if (lineTracker != null) {
			return lineTracker;
		}
		ILineTracker lineTracker = isIncremental() ? new TreeLineTracker(new ListLineTracker()) : new ListLineTracker();
		lineTracker.set(super.getText());
		return lineTracker;
	}

	/**
	 * Update text of the document by using the changes and according the
	 * incremental support.
	 * 
	 * @param changes the text document changes.
	 */
	public void update(List<TextDocumentContentChangeEvent> changes) {
		if (changes.size() < 1) {
			// no changes, ignore it.
			return;
		}
		if (isIncremental()) {
			try {
				long start = System.currentTimeMillis();
				synchronized (lock) {
					// Initialize buffer and line tracker from the current text document
					StringBuilder buffer = new StringBuilder(getText());

					// Loop for each changes and update the buffer
					for (int i = 0; i < changes.size(); i++) {

						TextDocumentContentChangeEvent changeEvent = changes.get(i);
						Range range = changeEvent.getRange();
						int length = 0;

						if (range != null) {
							length = changeEvent.getRangeLength().intValue();
						} else {
							// range is optional and if not given, the whole file content is replaced
							length = buffer.length();
							range = new Range(positionAt(0), positionAt(length));
						}
						String text = changeEvent.getText();
						int startOffset = offsetAt(range.getStart());
						buffer.replace(startOffset, startOffset + length, text);
						lineTracker.replace(startOffset, length, text);
					}
					// Update the new text content from the updated buffer
					setText(buffer.toString());
				}
				LOGGER.fine("Text document content updated in " + (System.currentTimeMillis() - start) + "ms");
			} catch (BadLocationException e) {
				// Should never occur.
			}
		} else {
			// like vscode does, get the last changes
			// see
			// https://github.com/Microsoft/vscode-languageserver-node/blob/master/server/src/main.ts
			TextDocumentContentChangeEvent last = changes.size() > 0 ? changes.get(changes.size() - 1) : null;
			if (last != null) {
				setText(last.getText());
				lineTracker.set(last.getText());
			}
		}
	}
}
