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
package org.eclipse.lemminx.extensions.contentmodel.participants.codeactions;

import java.util.List;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.CodeActionFactory;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.LineIndentInfo;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

/**
 * Code action to fix close start tag element.
 *
 */
public class CloseTagCodeAction implements ICodeActionParticipant {

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {
		Range diagnosticRange = diagnostic.getRange();
		try {
			int startOffset = document.offsetAt(diagnosticRange.getStart()) + 1;
			DOMNode node = document.findNodeAt(startOffset);
			if (node == null || !node.isElement()) {
				return;
			}
			DOMElement element = (DOMElement) node;
			if (element.hasEndTag() && !element.isEndTagClosed() && element.hasTagName()) {
				// Code actions for fixing end tag
				doCodeActionsForEndTagUnclosed(element, document, diagnostic, codeActions);
			} else {
				// Code actions for fixing start tag
				boolean startTagClosed = element.isStartTagClosed();
				if (!startTagClosed) {
					doCodeActionsForStartTagUnclosed(element, document, diagnosticRange, diagnostic, codeActions);
				} else {
					doCodeActionsForStartTagClosed(element, document, diagnosticRange, diagnostic, codeActions);
				}
			}
		} catch (BadLocationException e) {
			// do nothing
		}
	}

	/**
	 * Add code actions to fix unclosed end-tag.
	 *
	 * @param element     the end tag element to fix.
	 * @param document    the owner DOM document.
	 * @param diagnostic  the diagnostic.
	 * @param codeActions the code actions list to fill.
	 *
	 * @throws BadLocationException
	 */
	private void doCodeActionsForEndTagUnclosed(DOMElement element, DOMDocument document, Diagnostic diagnostic,
			List<CodeAction> codeActions) throws BadLocationException {
		// ex: <foo> </foo
		Position endTagPosition = document
				.positionAt(element.getEndTagOpenOffset() + 2 + element.getTagName().length());
		// Close with '>'
		CodeAction autoCloseAction = CodeActionFactory.insert("Close end-tag with '>'", endTagPosition, ">",
				document.getTextDocument(), diagnostic);
		codeActions.add(autoCloseAction);
	}

	/**
	 * Add code actions to fix unclosed start-tag.
	 *
	 * @param element         the start tag element to fix.
	 * @param document        the owner DOM document.
	 * @param diagnosticRange the diagnostic range.
	 * @param diagnostic      the diagnostic.
	 * @param codeActions     the code actions list to fill.
	 *
	 * @throws BadLocationException
	 */
	private void doCodeActionsForStartTagUnclosed(DOMElement element, DOMDocument document, Range diagnosticRange,
			Diagnostic diagnostic, List<CodeAction> codeActions) throws BadLocationException {
		// Here start tag element is not closed with '>'.
		String text = document.getText();
		int closeAngleBracketOffset = element.getUnclosedStartTagCloseOffset();
		final Position closeAngleBracketPosition = document.positionAt(closeAngleBracketOffset);
		if (!element.hasEndTag()) {
			// The element has no an end tag
			// ex : <foo
			// ex : <foo attr="
			// ex : <foo /
			boolean hasSlash = isCharAt(text, closeAngleBracketOffset - 1, '/');
			if (hasSlash) {
				// ex : <foo /
				CodeAction autoCloseAction = insertGreaterThanCharacterCodeAction(document, diagnostic,
						closeAngleBracketPosition);
				codeActions.add(autoCloseAction);
			} else {
				// ex : <foo
				// ex : <foo attr="
				// if element has tag name
				String tagName = element.getTagName();
				if (tagName != null) {
					// Close with '/>'
					CodeAction autoCloseAction = CodeActionFactory.insert("Close with '/>'", closeAngleBracketPosition,
							"/>", document.getTextDocument(), diagnostic);
					codeActions.add(autoCloseAction);
					// Close with '></foo>'
					String insertText = "></" + tagName + ">";
					CodeAction closeEndTagAction = CodeActionFactory.insert("Close with '" + insertText + "'",
							closeAngleBracketPosition, insertText, document.getTextDocument(), diagnostic);
					codeActions.add(closeEndTagAction);
				}
			}
		} else {
			// ex : <foo </foo>
			CodeAction autoCloseAction = insertGreaterThanCharacterCodeAction(document, diagnostic, closeAngleBracketPosition);
			codeActions.add(autoCloseAction);
		}
	}

	/**
	 * Add code actions to fix closed start-tag.
	 *
	 * @param element     the start tag element to fix.
	 * @param document    the owner DOM document.
	 * @param diagnostic  the diagnostic.
	 * @param codeActions the code actions list to fill.
	 *
	 * @throws BadLocationException
	 */
	private void doCodeActionsForStartTagClosed(DOMElement element, DOMDocument document, Range diagnosticRange,
			Diagnostic diagnostic, List<CodeAction> codeActions) throws BadLocationException {
		// Here start tag element is closed with '>'.
		String text = document.getText();
		if (!element.hasEndTag()) {
			// The element has no an end tag
			// ex : <foo attr="" >
			// // Close with '</foo>'
			String tagName = element.getTagName();
			// ex: <foo><
			// ex: <foo> <
			// ex: <foo></
			// ex: <foo> </
			if (!element.hasChildNodes()) {
				// ex : <foo><bar></foo>
				String insertText = "</" + tagName + ">";
				int endOffset = element.getStartTagCloseOffset() + 1;
				Position endPosition = document.positionAt(endOffset);
				CodeAction closeEndTagAction = CodeActionFactory.insert("Close with '" + insertText + "'", endPosition,
						insertText, document.getTextDocument(), diagnostic);
				codeActions.add(closeEndTagAction);
			} else {
				// the element have some children(Text node, Element node, etc)
				boolean hasChildWithNoTagName = false;
				// Search orphan elements in the children to replace
				List<DOMNode> children = element.getChildren();
				for (DOMNode child : children) {
					if (child.isElement()) {
						DOMElement childElement = (DOMElement) child;
						if (!childElement.hasTagName() || childElement.isOrphanEndTag()) {
							// // ex : <a> <b> </c> --> the </c> breaks the <b> element, it should be
							// replaced with </b>
							// ex : <a> <b> </ --> the </ breaks the <b> element, it should be replaced with
							// </b>
							String replaceTagName = element.getTagName();
							CodeAction replaceTagAction = replaceEndTagNameCodeAction(childElement, replaceTagName,
									diagnostic);
							codeActions.add(replaceTagAction);
							hasChildWithNoTagName = !childElement.hasTagName();
						}
					}
				}
				if (!hasChildWithNoTagName) {
					// Here, there are no child end tag element with '</'
					int endOffset = element.getLastChild().getEnd() - 1;
					if (endOffset < text.length()) {
						// remove whitespaces
						char ch = text.charAt(endOffset);
						while (Character.isWhitespace(ch)) {
							endOffset--;
							ch = text.charAt(endOffset);
						}
					}
					endOffset++;
					String label = "</" + tagName + ">";
					String insertText = label;
					if (hasElements(element)) {
						// The element have element node as children
						// the </foo> must be inserted with a new line and indent
						LineIndentInfo indentInfo = document.getLineIndentInfo(diagnosticRange.getStart().getLine());
						insertText = indentInfo.getLineDelimiter() + indentInfo.getWhitespacesIndent() + insertText;
					}
					// ex : <foo>bar --> the </foo> must be inserted after bar text
					// ex : <a> <b> </c> --> the </> must be inserted after the </c>
					Position endPosition = document.positionAt(endOffset);
					CodeAction closeEndTagAction = CodeActionFactory.insert("Close with '" + label + "'", endPosition,
							insertText, document.getTextDocument(), diagnostic);
					codeActions.add(closeEndTagAction);
				}
			}
		} else {
			// The element has an end tag
			// Search orphan end tag elements in the children which breaks the XML.
			List<DOMNode> children = element.getChildren();
			for (DOMNode child : children) {
				if (child.isElement()) {
					DOMElement childElement = (DOMElement) child;
					if (!childElement.hasTagName() || childElement.isOrphanEndTag()) {
						// ex: <foo> </ </foo> --> here the code action removes '</'
						// ex: <foo> </bar> </foo> --> here the code action removes '</bar>'
						CodeAction removeAction = removeTagCodeAction(childElement, document, diagnostic);
						codeActions.add(removeAction);
					}
				}
			}
		}
	}

	/**
	 * Create a code action which insert '>' at the end of the diagnostic error.
	 *
	 * @param diagnostic      the diagnostic.
	 * @param document        the DOM document.
	 * @param position        the position where the '>' should be inserted
	 * @return a code action which insert '>' at the end of the diagnostic error.
	 */
	private static CodeAction insertGreaterThanCharacterCodeAction(DOMDocument document, Diagnostic diagnostic,
			Position position) {
		CodeAction autoCloseAction = CodeActionFactory.insert("Close with '>'", position, ">",
				document.getTextDocument(), diagnostic);
		return autoCloseAction;
	}

	/**
	 * Create a code action which remove the content of the given DOM element.
	 *
	 * @param element    the DOM element to remove.
	 * @param document   the DOM document.
	 * @param diagnostic the diagnostic.
	 * @return
	 * @throws BadLocationException
	 */
	private static CodeAction removeTagCodeAction(DOMElement element, DOMDocument document, Diagnostic diagnostic)
			throws BadLocationException {
		String text = document.getText();
		Position startPosition = document.positionAt(element.getStart());
		Position endPosition = document.positionAt(element.getEnd());
		String contentToRemove = text.substring(element.getStart(), element.getEnd());
		CodeAction removeAction = CodeActionFactory.remove("Remove '" + contentToRemove + "'",
				new Range(startPosition, endPosition), document.getTextDocument(), diagnostic);
		return removeAction;
	}

	/**
	 * Create a code action which replaces the end tag name of the given element
	 * with the given replace tag name.
	 *
	 * @param element        the DOM element to replace
	 * @param replaceTagName the replace tag name
	 * @param diagnostic     the diagnostic.
	 * @return a code action which replaces the end tag name of the given element
	 *         with the given replace tag name.
	 */
	private static CodeAction replaceEndTagNameCodeAction(DOMElement element, String replaceTagName,
			Diagnostic diagnostic) {
		// <a><b></c>
		// Replace with 'b' closing tag
		DOMDocument document = element.getOwnerDocument();
		Range replaceRange = XMLPositionUtility.selectEndTagName(element);
		String tagName = element.getTagName();
		if (tagName == null) {
			tagName = "</";
		}
		String replaceText = replaceTagName;
		if (!element.isEndTagClosed()) {
			replaceText = replaceText + ">";
		}
		return CodeActionFactory.replace("Replace '" + tagName + "' with '" + replaceTagName + "' closing tag",
				replaceRange, replaceText, document.getTextDocument(), diagnostic);
	}

	/**
	 * Returns true if the given element has elements as children and false
	 * otherwise.
	 *
	 * @param element the DOM element.
	 *
	 * @return true if the given element has elements as children and false
	 *         otherwise.
	 */
	private static boolean hasElements(DOMElement element) {
		for (DOMNode node : element.getChildren()) {
			if (node.isElement()) {
				return true;
			}
		}
		return false;
	}

	private static boolean isCharAt(String text, int offset, char ch) {
		if (text.length() <= offset) {
			return false;
		}
		return text.charAt(offset) == ch;
	}

}
