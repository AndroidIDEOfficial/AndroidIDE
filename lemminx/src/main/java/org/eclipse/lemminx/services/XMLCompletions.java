/**
 *  Copyright (c) 2018 Angelo ZERR
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
package org.eclipse.lemminx.services;

import static java.lang.Character.isWhitespace;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.TextDocument;
import org.eclipse.lemminx.commons.snippets.SnippetRegistry;
import org.eclipse.lemminx.customservice.AutoCloseTagResponse;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DOMText;
import org.eclipse.lemminx.dom.DTDDeclParameter;
import org.eclipse.lemminx.dom.parser.Scanner;
import org.eclipse.lemminx.dom.parser.ScannerState;
import org.eclipse.lemminx.dom.parser.TokenType;
import org.eclipse.lemminx.dom.parser.XMLScanner;
import org.eclipse.lemminx.services.extensions.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.ICompletionRequest;
import org.eclipse.lemminx.services.extensions.ICompletionResponse;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.snippets.IXMLSnippetContext;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.settings.XMLCompletionSettings;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.MarkupKind;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.w3c.dom.Node;

/**
 * XML completions support.
 *
 */
public class XMLCompletions {

	private static final Logger LOGGER = Logger.getLogger(XMLCompletions.class.getName());
	private static final Pattern regionCompletionRegExpr = Pattern.compile("^(\\s*)(<(!(-(-\\s*(#\\w*)?)?)?)?)?$");

	private final XMLExtensionsRegistry extensionsRegistry;
	private SnippetRegistry snippetRegistry;

	public XMLCompletions(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	public CompletionList doComplete(DOMDocument xmlDocument, Position position, SharedSettings settings,
			CancelChecker cancelChecker) {
		CompletionResponse completionResponse = new CompletionResponse();
		CompletionRequest completionRequest = null;
		try {
			completionRequest = new CompletionRequest(xmlDocument, position, settings, extensionsRegistry);
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "Creation of CompletionRequest failed", e);
			return completionResponse;
		}

		String text = xmlDocument.getText();
		int offset = completionRequest.getOffset();
		DOMNode node = completionRequest.getNode();
		try {
			if (text.isEmpty()) {
				// When XML document is empty, try to collect root element (from file
				// association)
				collectInsideContent(completionRequest, completionResponse, cancelChecker);
				return completionResponse;
			}

			Scanner scanner = XMLScanner.createScanner(text, node.getStart(), isInsideDTDContent(node, xmlDocument));
			String currentTag = "";
			TokenType token = scanner.scan();
			while (token != TokenType.EOS && scanner.getTokenOffset() <= offset) {
				cancelChecker.checkCanceled();
				switch (token) {
				case StartTagOpen:
					if (scanner.getTokenEnd() == offset) {
						int endPos = scanNextForEndPos(offset, scanner, TokenType.StartTag);
						collectTagSuggestions(offset, endPos, completionRequest, completionResponse, cancelChecker);
						return completionResponse;
					}
					break;
				case StartTag:
					if (scanner.getTokenOffset() <= offset && offset <= scanner.getTokenEnd()) {
						collectOpenTagSuggestions(scanner.getTokenOffset(), scanner.getTokenEnd(), completionRequest,
								completionResponse, cancelChecker);
						return completionResponse;
					}
					currentTag = scanner.getTokenText();
					break;
				case AttributeName:
					if (scanner.getTokenOffset() <= offset && offset <= scanner.getTokenEnd()) {
						collectAttributeNameSuggestions(scanner.getTokenOffset(), scanner.getTokenEnd(),
								completionRequest, completionResponse, cancelChecker);
						return completionResponse;
					}
					break;
				case DelimiterAssign:
					if (scanner.getTokenEnd() == offset) {
						collectAttributeValueSuggestions(offset, offset, completionRequest, completionResponse, cancelChecker);
						return completionResponse;
					}
					break;
				case DTDStartDoctypeTag:

					DTDDeclParameter systemId = xmlDocument.getDoctype().getSystemIdNode();
					if (systemId == null) {
						break;
					}

					if (DOMNode.isIncluded(systemId, offset)) {
						/**
						 * Completion invoked within systemId parameter ie, completion offset is at |
						 * like so: <!DOCTYPE foo SYSTEM "./|">
						 */
						collectDTDSystemIdSuggestions(systemId.getStart(), systemId.getEnd(), completionRequest,
								completionResponse, cancelChecker);
						return completionResponse;
					}
					break;
				case AttributeValue:
					if (scanner.getTokenOffset() <= offset && offset <= scanner.getTokenEnd()) {
						collectAttributeValueSuggestions(scanner.getTokenOffset(), scanner.getTokenEnd(),
								completionRequest, completionResponse, cancelChecker);
						return completionResponse;
					}
					break;
				case Whitespace:
					if (offset <= scanner.getTokenEnd()) {
						switch (scanner.getScannerState()) {
						case AfterOpeningStartTag:
							int startPos = scanner.getTokenOffset();
							int endTagPos = scanNextForEndPos(offset, scanner, TokenType.StartTag);
							collectTagSuggestions(startPos, endTagPos, completionRequest, completionResponse, cancelChecker);
							return completionResponse;
						case WithinTag:
						case AfterAttributeName:
							collectAttributeNameSuggestions(scanner.getTokenEnd(), completionRequest,
									completionResponse, cancelChecker);
							return completionResponse;
						case BeforeAttributeValue:
							collectAttributeValueSuggestions(scanner.getTokenEnd(), offset, completionRequest,
									completionResponse, cancelChecker);
							return completionResponse;
						case AfterOpeningEndTag:
							collectCloseTagSuggestions(scanner.getTokenOffset() - 1, false, offset, completionRequest,
									completionResponse, cancelChecker);
							return completionResponse;
						case WithinContent:
							collectInsideContent(completionRequest, completionResponse, cancelChecker);
							return completionResponse;
						default:
						}
					}
					break;
				case EndTagOpen:
					if (offset <= scanner.getTokenEnd()) {
						int afterOpenBracket = scanner.getTokenOffset() + 1;
						int endOffset = scanNextForEndPos(offset, scanner, TokenType.EndTag);
						collectCloseTagSuggestions(afterOpenBracket, false, endOffset, completionRequest,
								completionResponse, cancelChecker);
						return completionResponse;
					}
					break;
				case EndTag:
					if (offset <= scanner.getTokenEnd()) {
						int start = scanner.getTokenOffset() - 1;
						while (start >= 0) {
							char ch = text.charAt(start);
							if (ch == '/') {
								collectCloseTagSuggestions(start, false, scanner.getTokenEnd(), completionRequest,
										completionResponse, cancelChecker);
								return completionResponse;
							} else if (!isWhitespace(ch)) {
								break;
							}
							start--;
						}
					}
					break;
				case StartTagClose:
					if (offset <= scanner.getTokenEnd()) {
						if (currentTag != null && currentTag.length() > 0) {
							collectInsideContent(completionRequest, completionResponse, cancelChecker);
							return completionResponse;
						}
					}
					break;
				case StartTagSelfClose:
					if (offset <= scanner.getTokenEnd()) {
						if (currentTag != null && currentTag.length() > 0
								&& xmlDocument.getText().charAt(offset - 1) == '>') { // if the actual character typed
																						// was
																						// '>'
							collectInsideContent(completionRequest, completionResponse, cancelChecker);
							return completionResponse;
						}
					}
					break;
				case EndTagClose:
					if (offset <= scanner.getTokenEnd()) {
						if (currentTag != null && currentTag.length() > 0) {
							collectInsideContent(completionRequest, completionResponse, cancelChecker);
							return completionResponse;
						}
					}
					break;
				case Content:
					if (completionRequest.getXMLDocument().isDTD()
							|| completionRequest.getXMLDocument().isWithinInternalDTD(offset)) {
						if (scanner.getTokenOffset() <= offset) {
							return completionResponse;
						}
						break;
					}
					if (offset <= scanner.getTokenEnd()) {
						collectInsideContent(completionRequest, completionResponse, cancelChecker);
						return completionResponse;
					}
					break;
				// DTD
				case DTDAttlistAttributeName:
				case DTDAttlistAttributeType:
				case DTDAttlistAttributeValue:
				case DTDStartAttlist:
				case DTDStartElement:
				case DTDStartEntity:
				case DTDEndTag:
				case DTDStartInternalSubset:
				case DTDEndInternalSubset: {
					if (scanner.getTokenOffset() <= offset) {
						return completionResponse;
					}
					break;
				}

				default:
					if (offset <= scanner.getTokenEnd()) {
						return completionResponse;
					}
					break;
				}
				token = scanner.scan();
			}
			return completionResponse;
		} finally {
			collectSnippetSuggestions(completionRequest, completionResponse);
		}
	}

	/**
	 * Collect snippets suggestions.
	 *
	 * @param completionRequest  completion request.
	 * @param completionResponse completion response.
	 */
	private void collectSnippetSuggestions(CompletionRequest completionRequest, CompletionResponse completionResponse) {
		DOMDocument document = completionRequest.getXMLDocument();
		String text = document.getText();
		int endExpr = completionRequest.getOffset();
		// compute the from for search expression according to the node
		int fromSearchExpr = getExprLimitStart(completionRequest.getNode(), endExpr);
		// compute the start expression
		int startExpr = getExprStart(text, fromSearchExpr, endExpr);
		try {
			Range replaceRange = getReplaceRange(startExpr, endExpr, completionRequest);
			completionRequest.setReplaceRange(replaceRange);
			String lineDelimiter = document.lineDelimiter(replaceRange.getStart().getLine());
			List<CompletionItem> snippets = getSnippetRegistry().getCompletionItems(replaceRange, lineDelimiter,
					completionRequest.canSupportMarkupKind(MarkupKind.MARKDOWN),
					completionRequest.getSharedSettings().getCompletionSettings().isCompletionSnippetsSupported(),
					(context, model) -> {
						if (context instanceof IXMLSnippetContext) {
							return (((IXMLSnippetContext) context).isMatch(completionRequest, model));
						}
						return false;
					}, (suffix) -> {
						// Search the suffix from the right of completion offset.
						for (int i = endExpr; i < text.length(); i++) {
							char ch = text.charAt(i);
							if (Character.isWhitespace(ch)) {
								// whitespace, continue to eat character
								continue;
							} else {
								// the current character is not a whitespace, search the sufix index
								Integer eatIndex = getSuffixIndex(text, suffix, i);
								if (eatIndex != null) {
									try {
										return document.positionAt(eatIndex);
									} catch (BadLocationException e) {
										return null;
									}
								}
								return null;
							}
						}
						return null;
					});
			for (CompletionItem completionItem : snippets) {
				completionResponse.addCompletionItem(completionItem);
			}

		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "In XMLCompletions, collectSnippetSuggestions position error", e);
		}
	}

	private static Integer getSuffixIndex(String text, String suffix, final int initOffset) {
		int offset = initOffset;
		char ch = text.charAt(offset);
		// Try to search the first character which matches the suffix
		Integer suffixIndex = null;
		for (int j = 0; j < suffix.length(); j++) {
			if (suffix.charAt(j) == ch) {
				suffixIndex = j;
				break;
			}
		}
		if (suffixIndex != null) {
			// There is one of character of the suffix
			offset++;
			if (suffixIndex == suffix.length()) {
				// the suffix index is the last character of the suffix
				return offset;
			}
			// Try to eat the most characters of the suffix
			for (; offset < text.length(); offset++) {
				suffixIndex++;
				if (suffixIndex == suffix.length()) {
					// the suffix index is the last character of the suffix
					return offset;
				}
				ch = text.charAt(offset);
				if (suffix.charAt(suffixIndex) != ch) {
					return offset;
				}
			}
			return offset;
		}
		return null;
	}

	/**
	 * Returns the limit start offset of the expression according to the current
	 * node.
	 *
	 * @param currentNode the node.
	 * @param offset      the offset.
	 * @return the limit start offset of the expression according to the current
	 *         node.
	 */
	private static int getExprLimitStart(DOMNode currentNode, int offset) {
		if (currentNode == null) {
			// should never occurs
			return 0;
		}
		if (currentNode.isText()) {
			return currentNode.getStart();
		}
		if (currentNode.isComment() || currentNode.isCDATA()) {
			if (offset >= currentNode.getEnd()) {
				return currentNode.isClosed() ? currentNode.getEnd() : currentNode.getStart();
			}
			return currentNode.getStart();
		}
		if (!currentNode.isElement()) {
			if (offset >= currentNode.getEnd() && currentNode.isClosed()) {
				// <?xml ?>|
				// --> in this case the offset of '>' is returned
				return currentNode.getEnd();
			}
			// processing instruction, comments, etc
			// - <?xml | ?>
			// - <?xml |
			// --> in this case the offset of '<' is returned
			return currentNode.getStart();
		}
		DOMElement element = (DOMElement) currentNode;
		if (element.isInStartTag(offset)) {
			return element.getStartTagOpenOffset();
		}
		if (element.isInEndTag(offset)) {
			return element.getEndTagOpenOffset();
		}
		if (offset >= currentNode.getEnd()) {
			// <a />|
			return currentNode.getEnd();
		}
		return element.getStartTagCloseOffset() + 1;
	}

	private static int getExprStart(String value, int from, int to) {
		if (to == 0) {
			return to;
		}
		int index = to - 1;
		while (index > 0) {
			if (Character.isWhitespace(value.charAt(index))) {
				return index + 1;
			}
			if (index <= from) {
				return from;
			}
			index--;
		}
		return index;
	}

	/**
	 * Returns true if completion was triggered inside DTD content (internal or
	 * external DTD) and false otherwise.
	 *
	 * @param node
	 * @param xmlDocument
	 * @return true if completion was triggered inside DTD content (internal or
	 *         external DTD) and false otherwise.
	 */
	private static boolean isInsideDTDContent(DOMNode node, DOMDocument xmlDocument) {
		if (xmlDocument.isDTD()) {
			// external DTD
			return true;
		}
		// check if node belongs to internal DTD (<!ELEMENT, ....)
		return (node.getParentNode() != null && node.getParentNode().isDoctype());
	}

	private boolean isBalanced(DOMNode node) {
		if (node.isClosed() == false) {
			return false;
		}
		String name = node.getNodeName();
		DOMNode parent = node.getParentElement();
		while (parent != null && name.equals(parent.getNodeName())) {
			if (parent.isClosed() == false) {
				return false;
			}
			parent = parent.getParentElement();
		}
		return true;
	}

	public AutoCloseTagResponse doTagComplete(DOMDocument xmlDocument, Position position, XMLCompletionSettings completionSettings, CancelChecker cancelChecker) {
		int offset;
		try {
			offset = xmlDocument.offsetAt(position);
			if (offset - 2 < 0) { // There is not enough content for autoClose
				return null;
			}
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "doTagComplete failed", e);
			return null;
		}
		if (offset <= 0) {
			return null;
		}
		char c = xmlDocument.getText().charAt(offset - 1);
		char cBefore = xmlDocument.getText().charAt(offset - 2);
		String snippet = null;
		if (XMLPositionUtility.isInAttributeValue(xmlDocument, position)) {
			return null;
		}
		if (c == '>') { // Case: <a>|
			DOMNode node = xmlDocument.findNodeBefore(offset);
			if (!(node instanceof DOMElement)) {
				return null;
			}
			DOMElement element = ((DOMElement) node);
			if (node != null && node.isElement() && !element.isSelfClosed() && element.hasTagName()
					&& !isEmptyElement(((DOMElement) node).getTagName()) && node.getStart() < offset
					&& (!element.hasEndTag() || (element.getTagName().equals(node.getParentNode().getNodeName())
							&& !isBalanced(node)))) {
				snippet = "$0</" + ((DOMElement) node).getTagName() + ">";

			}
		} else if (cBefore == '<' && c == '/') { // Case: <a> </|
			DOMNode node = xmlDocument.findNodeBefore(offset);
			while ((node != null && node.isClosed()) || (node.isElement() && ((DOMElement) node).isOrphanEndTag())) {
				node = node.getParentNode();
			}
			if (node != null && node.isElement() && ((DOMElement) node).getTagName() != null) {
				snippet = ((DOMElement) node).getTagName() + ">$0";
			}
		} else {
			DOMNode node = xmlDocument.findNodeBefore(offset);
			if (node.isElement() && node.getNodeName() != null) {
				DOMElement element1 = (DOMElement) node;

				Integer slashOffset = element1.endsWith('/', offset);
				Position end = null;
				if (!element1.isInEndTag(offset) && slashOffset != null) { // The typed characted was '/'
					List<DOMAttr> attrList = element1.getAttributeNodes();
					if (attrList != null) {
						DOMAttr lastAttr = attrList.get(attrList.size() - 1);
						if (slashOffset < lastAttr.getEnd()) { // slash in attribute value
							return null;
						}
					}
					String text = xmlDocument.getText();
					// After the slash is a close bracket
					boolean closeBracketAfterSlash = offset < text.length() ? text.charAt(offset) == '>' : false;

					// Case: <a/| ...
					if (closeBracketAfterSlash == false) { // no '>' after slash
						if (element1.isStartTagClosed()) { // tag has closing '>', but slash is in incorrect area (not
															// directly before the '>')
							return null;
						}
						snippet = ">$0";
						if (element1.hasEndTag() && completionSettings.isAutoCloseRemovesContent()) { // Case: <a/| </a>
							try {
								end = xmlDocument.positionAt(element1.getEnd());
							} catch (BadLocationException e) {
								return null;
							}
						}
					} else {
						DOMNode nextSibling = node.getNextSibling();
						// If there is text in between the tags it will skip this
						if (nextSibling != null && nextSibling.isElement()) { // Case: <a/|></a>
							DOMElement element2 = (DOMElement) nextSibling;
							if (!element2.hasStartTag() && node.getNodeName().equals(element2.getNodeName())) {
								try {
									snippet = ">$0";
									end = xmlDocument.positionAt(element2.getEnd());
								} catch (BadLocationException e) {
									return null;
								}
							}
						} else if (nextSibling == null) { // Case: <a> <a/|> </a> </a>
							DOMElement parentElement = node.getParentElement();
							if (parentElement != null && node.getNodeName().equals(parentElement.getTagName())) {
								DOMNode nodeAfterParent = parentElement.getNextSibling();
								if (nodeAfterParent != null && nodeAfterParent.isElement()) {
									DOMElement elementAfterParent = (DOMElement) nodeAfterParent;
									if (parentElement.getTagName().equals(elementAfterParent.getTagName())
											&& !elementAfterParent.hasStartTag()) {
										try {
											snippet = ">$0";
											end = xmlDocument.positionAt(parentElement.getEnd());
										} catch (BadLocationException e) {
											return null;
										}
									}
								}
							}
						}
					}
					if (snippet != null && end != null) {
						return new AutoCloseTagResponse(snippet, new Range(position, end));
					}
				}
			}
		}
		if (snippet == null) {
			return null;
		}
		return new AutoCloseTagResponse(snippet);
	}

	// ---------------- Tags completion

	// Tags that look like '<'
	private void collectTagSuggestions(int tagStart, int tagEnd, CompletionRequest completionRequest,
			CompletionResponse completionResponse, CancelChecker cancelChecker) {
		collectOpenTagSuggestions(tagStart, tagEnd, completionRequest, completionResponse, cancelChecker);
		collectCloseTagSuggestions(tagStart, true, tagEnd, completionRequest, completionResponse, cancelChecker);
	}

	private void collectOpenTagSuggestions(int afterOpenBracket, int tagNameEnd, CompletionRequest completionRequest,
			CompletionResponse completionResponse, CancelChecker cancelChecker) {
		try {
			Range replaceRange = getReplaceRange(afterOpenBracket - 1, tagNameEnd, completionRequest);
			collectOpenTagSuggestions(true, replaceRange, completionRequest, completionResponse, cancelChecker);
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "While performing Completions the provided offset was a BadLocation", e);
			return;
		}
	}

	private void collectOpenTagSuggestions(boolean hasOpenBracket, Range replaceRange,
			CompletionRequest completionRequest, CompletionResponse completionResponse, CancelChecker cancelChecker) {
		try {
			DOMDocument document = completionRequest.getXMLDocument();
			String text = document.getText();
			int tagNameEnd = document.offsetAt(replaceRange.getEnd());
			int newOffset = getOffsetFollowedBy(text, tagNameEnd, ScannerState.WithinEndTag, TokenType.EndTagClose);
			if (newOffset != -1) {
				newOffset++;
				replaceRange.setEnd(document.positionAt(newOffset));
			}

		} catch (BadLocationException e) {
			// do nothing
		}
		completionRequest.setHasOpenBracket(hasOpenBracket);
		completionRequest.setReplaceRange(replaceRange);
		for (ICompletionParticipant participant : getCompletionParticipants()) {
			try {
				participant.onTagOpen(completionRequest, completionResponse, cancelChecker);
			} catch (CancellationException e) {
				throw e;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "While performing ICompletionParticipant#onTagOpen for participant '"
						+ participant.getClass().getName() + "'.", e);
			}
		}
		DOMElement parentNode = completionRequest.getParentElement();
		if (parentNode != null && !parentNode.getOwnerDocument().hasGrammar()) {
			// no grammar, collect similar tags from the parent node
			Set<String> seenElements = new HashSet<>();
			if (parentNode != null && parentNode.isElement() && parentNode.hasChildNodes()) {
				parentNode.getChildren().forEach(node -> {
					DOMElement element = node.isElement() ? (DOMElement) node : null;
					if (element == null || element.getTagName() == null
							|| seenElements.contains(element.getTagName())) {
						return;
					}
					String tag = element.getTagName();
					seenElements.add(tag);
					CompletionItem item = new CompletionItem();
					item.setLabel(tag);
					item.setKind(CompletionItemKind.Property);
					item.setFilterText(completionRequest.getFilterForStartTagName(tag));
					StringBuilder xml = new StringBuilder();
					xml.append("<");
					xml.append(tag);
					if (element.isSelfClosed()) {
						xml.append(" />");
					} else {
						xml.append(">");
						if (completionRequest.isCompletionSnippetsSupported()) {
							xml.append("$0");
						}
						if (isGenerateEndTag(completionRequest, tag)) {
							xml.append("</").append(tag).append(">");
						}
					}
					item.setTextEdit(Either.forLeft(new TextEdit(replaceRange, xml.toString())));
					item.setInsertTextFormat(InsertTextFormat.Snippet);

					completionResponse.addCompletionItem(item);
				});
			}
		}
	}

	private static boolean isGenerateEndTag(CompletionRequest completionRequest, String tagName) {
		if (!completionRequest.isAutoCloseTags()) {
			return false;
		}
		DOMNode node = completionRequest.getNode();
		if (node == null) {
			return true;
		}
		int offset = completionRequest.getOffset();
		return node.getOrphanEndElement(offset, tagName) == null;
	}

	private void collectCloseTagSuggestions(int afterOpenBracket, boolean inOpenTag, int tagNameEnd,
			CompletionRequest completionRequest, CompletionResponse completionResponse, CancelChecker cancelChecker) {
		try {
			Range range = getReplaceRange(afterOpenBracket, tagNameEnd, completionRequest);
			String text = completionRequest.getXMLDocument().getText();
			boolean hasCloseTag = isFollowedBy(text, tagNameEnd, ScannerState.WithinEndTag, TokenType.EndTagClose);
			collectCloseTagSuggestions(range, false, !hasCloseTag, inOpenTag, completionRequest, completionResponse);
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "While performing Completions the provided offset was a BadLocation", e);
		}
	}

	private void collectCloseTagSuggestions(Range range, boolean openEndTag, boolean closeEndTag, boolean inOpenTag,
			CompletionRequest completionRequest, CompletionResponse completionResponse) {
		try {
			String text = completionRequest.getXMLDocument().getText();
			DOMNode curr = completionRequest.getNode();
			if (inOpenTag) {
				curr = curr.getParentNode(); // don't suggest the own tag, it's not yet open
			}
			String closeTag = closeEndTag ? ">" : "";
			int afterOpenBracket = completionRequest.getXMLDocument().offsetAt(range.getStart());
			if (!openEndTag) {
				afterOpenBracket--;
			}
			int offset = completionRequest.getOffset();
			while (curr != null) {
				if (curr.isElement()) {
					DOMElement element = ((DOMElement) curr);
					String tag = element.getTagName();
					if (tag != null && (!element
							.isClosed() /* || element.hasEndTag() && (element.getEndTagOpenOffset() > offset) */)) {
						CompletionItem item = new CompletionItem();
						item.setLabel("End with '</" + tag + ">'");
						item.setKind(CompletionItemKind.Property);
						item.setInsertTextFormat(InsertTextFormat.PlainText);

						String startIndent = getLineIndent(element.getStart(), text);
						String endIndent = getLineIndent(afterOpenBracket, text);
						if (startIndent != null && endIndent != null && !startIndent.equals(endIndent)) {
							String insertText = startIndent + "</" + tag + closeTag;
							item.setTextEdit(Either.forLeft(new TextEdit(
									getReplaceRange(afterOpenBracket - endIndent.length(), offset, completionRequest),
									insertText)));
							item.setFilterText(endIndent + "</" + tag + closeTag);
						} else {
							String openTag = openEndTag ? "<" : "";
							String insertText = openTag + "/" + tag + closeTag;
							item.setFilterText(insertText);
							item.setTextEdit(Either.forLeft(new TextEdit(range, insertText)));
						}
						completionResponse.addCompletionItem(item);
					}
				}
				curr = curr.getParentNode();
			}
			if (inOpenTag) {
				return;
			}

		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "While performing Completions the provided offset was a BadLocation", e);
		}
	}

	private void collectInsideContent(CompletionRequest request, CompletionResponse response, CancelChecker cancelChecker) {
		Range tagNameRange = request.getXMLDocument().getElementNameRangeAt(request.getOffset());
		if (tagNameRange != null) {
			collectOpenTagSuggestions(false, tagNameRange, request, response, cancelChecker);
			collectCloseTagSuggestions(tagNameRange, true, true, false, request, response);
		}
		// Adjust the range for covering the text node.
		Range textRange = getTextRangeInsideContent(request.getNode());
		if (textRange != null) {
			request.setReplaceRange(textRange);
		}
		// Participant completion on XML content
		for (ICompletionParticipant participant : getCompletionParticipants()) {
			try {
				participant.onXMLContent(request, response, cancelChecker);
			} catch (CancellationException e) {
				throw e;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "While performing ICompletionParticipant#onXMLContent for participant '"
						+ participant.getClass().getName() + "'.", e);
			}
		}
		collectionRegionProposals(request, response);
	}

	private static Range getTextRangeInsideContent(DOMNode node) {
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
			Node firstChild = node.getFirstChild();
			if (firstChild == null) {
				// ex : <root>|</root>
				DOMElement element = (DOMElement) node;
				return XMLPositionUtility.createRange(element.getStartTagCloseOffset() + 1,
						element.getStartTagCloseOffset() + 1, element.getOwnerDocument());
			}
			if (firstChild.getNodeType() == Node.TEXT_NODE) {
				// ex : <root>abcd|</root>
				return XMLPositionUtility.selectText((DOMText) firstChild);
			}
			return null;
		case Node.TEXT_NODE:
			// ex : <root> | </root>
			return XMLPositionUtility.selectText((DOMText) node);
		}
		// should never occur
		return null;
	}

	private void collectionRegionProposals(ICompletionRequest request, ICompletionResponse response) {
		// Completion for #region
		try {
			int offset = request.getOffset();
			TextDocument document = request.getXMLDocument().getTextDocument();
			Position pos = document.positionAt(offset);
			String lineText = document.lineText(pos.getLine());
			String lineUntilPos = lineText.substring(0, pos.getCharacter());
			Matcher match = regionCompletionRegExpr.matcher(lineUntilPos);
			if (match.find()) {
				InsertTextFormat insertFormat = request.getInsertTextFormat();
				Range range = new Range(new Position(pos.getLine(), pos.getCharacter() + match.regionStart()), pos);

				String text = request.isCompletionSnippetsSupported() ? "<!-- #region $1-->" : "<!-- #region -->";
				CompletionItem beginProposal = new CompletionItem("#region");
				beginProposal.setTextEdit(Either.forLeft(new TextEdit(range, text)));
				beginProposal.setDocumentation("Insert Folding Region Start");
				beginProposal.setFilterText(match.group());
				beginProposal.setSortText("za");
				beginProposal.setKind(CompletionItemKind.Snippet);
				beginProposal.setInsertTextFormat(insertFormat);
				response.addCompletionAttribute(beginProposal);

				CompletionItem endProposal = new CompletionItem("#endregion");
				endProposal.setTextEdit(Either.forLeft(new TextEdit(range, "<!-- #endregion-->")));
				endProposal.setDocumentation("Insert Folding Region End");
				endProposal.setFilterText(match.group());
				endProposal.setSortText("zb");
				endProposal.setKind(CompletionItemKind.Snippet);
				endProposal.setInsertTextFormat(InsertTextFormat.PlainText);
				response.addCompletionAttribute(endProposal);
			}
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "While performing collectRegionCompletion", e);
		}
	}

	private void collectAttributeNameSuggestions(int nameStart, CompletionRequest completionRequest,
			CompletionResponse completionResponse, CancelChecker cancelChecker) {
		collectAttributeNameSuggestions(nameStart, completionRequest.getOffset(), completionRequest,
				completionResponse, cancelChecker);
	}

	private void collectAttributeNameSuggestions(int nameStart, int nameEnd, CompletionRequest completionRequest,
			CompletionResponse completionResponse, CancelChecker cancelChecker) {
		int replaceEnd = completionRequest.getOffset();
		String text = completionRequest.getXMLDocument().getText();
		while (replaceEnd < nameEnd && text.charAt(replaceEnd) != '<' && text.charAt(replaceEnd) != '?') { // < is a
																											// valid
																											// attribute
																											// name
																											// character,
																											// but
			// we rather assume the attribute name ends.
			// See #23236.
			replaceEnd++;
		}
		try {
			Range replaceRange = getReplaceRange(nameStart, replaceEnd, completionRequest);
			completionRequest.setReplaceRange(replaceRange);
			boolean generateValue = !isFollowedBy(text, nameEnd, ScannerState.AfterAttributeName,
					TokenType.DelimiterAssign);
			for (ICompletionParticipant participant : getCompletionParticipants()) {
				try {
					participant.onAttributeName(generateValue, completionRequest, completionResponse, cancelChecker);
				} catch (CancellationException e) {
					throw e;
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "While performing ICompletionParticipant#onAttributeName for participant '"
							+ participant.getClass().getName() + "'.", e);
				}
			}
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "While performing Completions, getReplaceRange() was given a bad Offset location",
					e);
		} catch (CancellationException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "While performing ICompletionParticipant#onAttributeName", e);
		}
	}

	private void collectAttributeValueSuggestions(int valueStart, int valueEnd, CompletionRequest completionRequest,
			CompletionResponse completionResponse, CancelChecker cancelChecker) {
		boolean addQuotes = false;
		String valuePrefix;
		int offset = completionRequest.getOffset();
		String text = completionRequest.getXMLDocument().getText();

		// Adjusts range to handle if quotations for the value exist
		if (offset > valueStart && offset <= valueEnd && StringUtils.isQuote(text.charAt(valueStart))) {
			// inside quoted attribute
			int valueContentStart = valueStart + 1;
			int valueContentEnd = valueEnd;
			// valueEnd points to the char after quote, which encloses the replace range
			if (text.charAt(valueEnd - 1) == text.charAt(valueStart)) {
				valueContentEnd--;
			}
			valuePrefix = offset >= valueContentStart && offset <= valueContentEnd
					? text.substring(valueContentStart, offset)
					: "";
			valueStart = valueContentStart;
			valueEnd = valueContentEnd;
			addQuotes = false;
		} else {
			valuePrefix = text.substring(valueStart, offset);
			addQuotes = true;
		}

		Collection<ICompletionParticipant> completionParticipants = getCompletionParticipants();
		if (completionParticipants.size() > 0) {
			try {
				Range replaceRange = getReplaceRange(valueStart, valueEnd, completionRequest);
				completionRequest.setReplaceRange(replaceRange);
				completionRequest.setAddQuotes(addQuotes);
				for (ICompletionParticipant participant : completionParticipants) {
					try {
						participant.onAttributeValue(valuePrefix, completionRequest, completionResponse, cancelChecker);
					} catch (CancellationException e) {
						throw e;
					} catch (Exception e) {
						LOGGER.log(Level.SEVERE,
								"While performing ICompletionParticipant#onAttributeValue for participant '"
										+ participant.getClass().getName() + "'.",
								e);
					}
				}
			} catch (BadLocationException e) {
				LOGGER.log(Level.SEVERE,
						"While performing Completions, getReplaceRange() was given a bad Offset location", e);
			} catch (CancellationException e) {
				throw e;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "While performing ICompletionParticipant#onAttributeValue", e);
			}
		}
	}

	/**
	 * Collect completion items for DTD systemId
	 *
	 * @param valueStart         the start offset of the systemId value, including
	 *                           quote
	 * @param valueEnd           the end offset of the systemId value, including
	 *                           quote
	 * @param completionRequest  the completion request
	 * @param completionResponse the completion response
	 */
	private void collectDTDSystemIdSuggestions(int valueStart, int valueEnd, CompletionRequest completionRequest,
			CompletionResponse completionResponse, CancelChecker cancelChecker) {
		int offset = completionRequest.getOffset();
		String text = completionRequest.getXMLDocument().getText();
		int valueContentStart = valueStart + 1;
		int valueContentEnd = valueEnd - 1;
		String valuePrefix = offset >= valueContentStart && offset <= valueContentEnd
				? text.substring(valueContentStart, offset)
				: "";
		Collection<ICompletionParticipant> completionParticipants = getCompletionParticipants();

		if (completionParticipants.size() > 0) {
			try {
				Range replaceRange = getReplaceRange(valueContentStart, valueContentEnd, completionRequest);
				completionRequest.setReplaceRange(replaceRange);
				for (ICompletionParticipant participant : completionParticipants) {
					try {
						participant.onDTDSystemId(valuePrefix, completionRequest, completionResponse, cancelChecker);
					} catch (CancellationException e) {
						throw e;
					} catch (Exception e) {
						LOGGER.log(Level.SEVERE,
								"While performing ICompletionParticipant#onDTDSystemId for participant '"
										+ participant.getClass().getName() + "'.",
								e);
					}
				}
			} catch (BadLocationException e) {
				LOGGER.log(Level.SEVERE,
						"While performing Completions, getReplaceRange() was given a bad Offset location", e);
			} catch (CancellationException e) {
				throw e;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "While performing ICompletionParticipant#onDTDSystemId", e);
			}
		}
	}

	private static int scanNextForEndPos(int offset, Scanner scanner, TokenType nextToken) {
		if (offset == scanner.getTokenEnd()) {
			TokenType token = scanner.scan();
			if (token == nextToken && scanner.getTokenOffset() == offset) {
				return scanner.getTokenEnd();
			}
		}
		return offset;
	}

	/**
	 * Returns list of {@link ICompletionParticipant}.
	 *
	 * @return list of {@link ICompletionParticipant}.
	 */
	private Collection<ICompletionParticipant> getCompletionParticipants() {
		return extensionsRegistry.getCompletionParticipants();
	}

	private static boolean isFollowedBy(String s, int offset, ScannerState intialState, TokenType expectedToken) {
		return getOffsetFollowedBy(s, offset, intialState, expectedToken) != -1;
	}

	/**
	 * Returns starting offset of 'expectedToken' if it the next non whitespace
	 * token after 'initialState'
	 *
	 * @param s
	 * @param offset
	 * @param intialState
	 * @param expectedToken
	 * @return
	 */
	public static int getOffsetFollowedBy(String s, int offset, ScannerState intialState, TokenType expectedToken) {
		Scanner scanner = XMLScanner.createScanner(s, offset, intialState);
		TokenType token = scanner.scan();
		while (token == TokenType.Whitespace) {
			token = scanner.scan();
		}
		return (token == expectedToken) ? scanner.getTokenOffset() : -1;
	}

	private static Range getReplaceRange(int replaceStart, int replaceEnd, ICompletionRequest context)
			throws BadLocationException {
		int offset = context.getOffset();
		if (replaceStart > offset) {
			replaceStart = offset;
		}
		DOMDocument document = context.getXMLDocument();
		return XMLPositionUtility.createRange(replaceStart, replaceEnd, document);
	}

	private static String getLineIndent(int offset, String text) {
		int start = offset;
		while (start > 0) {
			char ch = text.charAt(start - 1);
			if ("\n\r".indexOf(ch) >= 0) {
				return text.substring(start, offset);
			}
			if (!isWhitespace(ch)) {
				return null;
			}
			start--;
		}
		return text.substring(0, offset);
	}

	private boolean isEmptyElement(String tag) {
		return false;
	}

	private SnippetRegistry getSnippetRegistry() {
		if (snippetRegistry == null) {
			snippetRegistry = new SnippetRegistry();
		}
		return snippetRegistry;
	}
}
