/**
 *  Copyright (c) 2018 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Initial code from https://github.com/Microsoft/vscode-html-languageservice
 * Initial copyright Copyright (C) Microsoft Corporation. All rights reserved.
 * Initial license: MIT
 *
 * Contributors:
 *  - Microsoft Corporation: Initial code, written in TypeScript, licensed under MIT license
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.lemminx.dom;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.TextDocument;
import org.eclipse.lemminx.dom.parser.Scanner;
import org.eclipse.lemminx.dom.parser.TokenType;
import org.eclipse.lemminx.dom.parser.XMLScanner;
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Tolerant XML parser.
 *
 */
public class DOMParser {

	private static final Logger LOGGER = Logger.getLogger(DOMParser.class.getName());

	private static final DOMParser INSTANCE = new DOMParser();

	public static DOMParser getInstance() {
		return INSTANCE;
	}

	private DOMParser() {

	}

	public DOMDocument parse(String text, String uri, URIResolverExtensionManager resolverExtensionManager) {
		return parse(new TextDocument(text, uri), resolverExtensionManager);
	}

	public DOMDocument parse(String text, String uri, URIResolverExtensionManager resolverExtensionManager,
			boolean ignoreWhitespaceContent) {
		return parse(new TextDocument(text, uri), resolverExtensionManager, ignoreWhitespaceContent);
	}

	public DOMDocument parse(TextDocument document, URIResolverExtensionManager resolverExtensionManager) {
		return parse(document, resolverExtensionManager, true);
	}

	public DOMDocument parse(TextDocument document, URIResolverExtensionManager resolverExtensionManager,
			boolean ignoreWhitespaceContent) {
		return parse(document, resolverExtensionManager, ignoreWhitespaceContent, null);
	}

	public DOMDocument parse(TextDocument document, URIResolverExtensionManager resolverExtensionManager,
			boolean ignoreWhitespaceContent, CancelChecker monitor) {
		boolean isDTD = DOMUtils.isDTD(document.getUri());
		boolean inDTDInternalSubset = false;
		String text = document.getText();
		Scanner scanner = XMLScanner.createScanner(text, 0, isDTD);
		DOMDocument xmlDocument = new DOMDocument(document, resolverExtensionManager);
		xmlDocument.setCancelChecker(monitor);

		DOMNode curr = isDTD ? new DOMDocumentType(0, text.length()) : xmlDocument;
		if (isDTD) {
			xmlDocument.addChild(curr);

			// This DOMDocumentType object is hidden, and just represents the DTD file
			// nothing should affect it's closed status
			curr.closed = true;
		}
		DOMNode lastClosed = curr;
		DOMAttr attr = null;
		int endTagOpenOffset = -1;
		DOMNode tempWhitespaceContent = null;
		boolean isInitialDeclaration = true; // A declaration can have multiple internal declarations
		boolean previousTokenWasEndTagOpen = false;
		TokenType token = scanner.scan();
		while (token != TokenType.EOS) {
			if (monitor != null) {
				monitor.checkCanceled();
			}
			if (tempWhitespaceContent != null && token != TokenType.EndTagOpen) {
				tempWhitespaceContent = null;
			}
			if (previousTokenWasEndTagOpen) {
				previousTokenWasEndTagOpen = false;
				if (token != TokenType.EndTag) {
					// The excepted token is not an EndTag, create a fake end tag element
					DOMElement element = xmlDocument.createElement(endTagOpenOffset, endTagOpenOffset + 2);
					element.endTagOpenOffset = endTagOpenOffset;
					curr.addChild(element);
				}
			}
			switch (token) {
			case StartTagOpen: {
				if (!curr.isClosed() && curr.parent != null) {
					// The next node's parent (curr) is not closed at this point
					// so the node's parent (curr) will have its end position updated
					// to a newer end position.
					curr.end = scanner.getTokenOffset();
				}
				if ((curr.isClosed()) || curr.isDoctype()) {
					// The next node being considered is a child of 'curr'
					// and if 'curr' is already closed then 'curr' was not updated properly.
					// Or if we get a Doctype node then we know it was not closed and 'curr'
					// wasn't updated properly.
					curr = curr.parent;
					inDTDInternalSubset = false; // In case it was previously in the internal subset
				}
				DOMElement child = xmlDocument.createElement(scanner.getTokenOffset(), scanner.getTokenEnd());
				child.startTagOpenOffset = scanner.getTokenOffset();
				curr.addChild(child);
				curr = child;
				break;
			}

			case StartTag: {
				DOMElement element = (DOMElement) curr;
				element.tag = scanner.getTokenText();
				curr.end = scanner.getTokenEnd();
				break;
			}

			case StartTagClose:
				if (curr.isElement()) {
					DOMElement element = (DOMElement) curr;
					curr.end = scanner.getTokenEnd(); // might be later set to end tag position
					element.startTagCloseOffset = scanner.getTokenOffset();

					// never enters isEmptyElement() is always false
					if (element.hasTagName() && isEmptyElement(element.getTagName()) && curr.parent != null) {
						curr.closed = true;
						curr = curr.parent;
					}
				} else if (curr.isProcessingInstruction() || curr.isProlog()) {
					DOMProcessingInstruction element = (DOMProcessingInstruction) curr;
					curr.end = scanner.getTokenEnd(); // might be later set to end tag position
					element.startTagClose = true;
					if (element.getTarget() != null && isEmptyElement(element.getTarget()) && curr.parent != null) {
						curr.closed = true;
						curr = curr.parent;
					}
				}
				curr.end = scanner.getTokenEnd();
				break;

			case EndTagOpen:
				if (tempWhitespaceContent != null) {
					curr.addChild(tempWhitespaceContent);
					tempWhitespaceContent = null;
				}
				endTagOpenOffset = scanner.getTokenOffset();
				curr.end = scanner.getTokenOffset();
				previousTokenWasEndTagOpen = true;
				break;

			case EndTag:
				// end tag (ex: </root>)
				String closeTag = scanner.getTokenText();
				DOMNode current = curr;

				/**
				 * eg: <a><b><c></d> will set a,b,c end position to the start of |</d>
				 */
				while (!(curr.isElement() && ((DOMElement) curr).isSameTag(closeTag)) && curr.parent != null) {
					curr.end = endTagOpenOffset;
					curr = curr.parent;
				}
				if (curr != xmlDocument) {
					curr.closed = true;
					if (curr.isElement()) {
						((DOMElement) curr).endTagOpenOffset = endTagOpenOffset;
					} else if (curr.isProcessingInstruction() || curr.isProlog()) {
						((DOMProcessingInstruction) curr).endTagOpenOffset = endTagOpenOffset;
					}
					curr.end = scanner.getTokenEnd();
				} else {
					// element open tag not found (ex: <root>) add a fake element which only has an
					// end tag (no start tag).
					DOMElement element = xmlDocument.createElement(scanner.getTokenOffset() - 2, scanner.getTokenEnd());
					element.endTagOpenOffset = endTagOpenOffset;
					element.tag = closeTag;
					current.addChild(element);
					curr = element;
				}
				break;

			case StartTagSelfClose:
				if (curr.parent != null) {
					curr.closed = true;
					((DOMElement) curr).selfClosed = true;
					curr.end = scanner.getTokenEnd();
					lastClosed = curr;
					curr = curr.parent;
				}
				break;

			case EndTagClose:
				if (curr.parent != null) {
					curr.end = scanner.getTokenEnd();
					lastClosed = curr;
					if (lastClosed.isElement()) {
						((DOMElement) curr).endTagCloseOffset = scanner.getTokenOffset();
					}
					if (curr.isDoctype()) {
						curr.closed = true;
					}
					curr = curr.parent;

				}
				break;

			case AttributeName: {
				attr = new DOMAttr(null, scanner.getTokenOffset(),
						scanner.getTokenEnd(), curr);
				curr.setAttributeNode(attr);
				curr.end = scanner.getTokenEnd();
				break;
			}

			case DelimiterAssign: {
				if (attr != null) {
					// Sets the value to the '=' position in case there is no AttributeValue
					attr.setDelimiter(scanner.getTokenOffset());
				}
				break;
			}

			case AttributeValue: {
				if (curr.hasAttributes() && attr != null) {
					attr.setValue(null, scanner.getTokenOffset(), scanner.getTokenEnd());
				}
				attr = null;
				curr.end = scanner.getTokenEnd();
				break;
			}

			case CDATATagOpen: {
				DOMCDATASection cdataNode = xmlDocument.createCDataSection(scanner.getTokenOffset(), text.length());
				curr.addChild(cdataNode);
				curr = cdataNode;
				break;
			}

			case CDATAContent: {
				DOMCDATASection cdataNode = (DOMCDATASection) curr;
				cdataNode.startContent = scanner.getTokenOffset();
				cdataNode.endContent = scanner.getTokenEnd();
				curr.end = scanner.getTokenEnd();
				break;
			}

			case CDATATagClose: {
				curr.end = scanner.getTokenEnd();
				curr.closed = true;
				curr = curr.parent;
				break;
			}

			case StartPrologOrPI: {
				DOMProcessingInstruction prologOrPINode = xmlDocument
						.createProcessingInstruction(scanner.getTokenOffset(), text.length());
				curr.addChild(prologOrPINode);
				curr = prologOrPINode;
				break;
			}

			case PIName: {
				DOMProcessingInstruction processingInstruction = ((DOMProcessingInstruction) curr);
				processingInstruction.target = scanner.getTokenText();
				processingInstruction.processingInstruction = true;
				break;
			}

			case PrologName: {
				DOMProcessingInstruction processingInstruction = ((DOMProcessingInstruction) curr);
				processingInstruction.target = scanner.getTokenText();
				processingInstruction.prolog = true;
				break;
			}

			case PIContent: {
				DOMProcessingInstruction processingInstruction = (DOMProcessingInstruction) curr;
				processingInstruction.startContent = scanner.getTokenOffset();
				processingInstruction.endContent = scanner.getTokenEnd();
				break;
			}

			case PIEnd:
			case PrologEnd: {
				curr.end = scanner.getTokenEnd();
				curr.closed = true;
				curr = curr.parent;
				break;
			}

			case StartCommentTag: {
				// Incase the tag before the comment tag (curr) was not properly closed
				// curr should be set to the root node.
				if (xmlDocument.isDTD() || inDTDInternalSubset) {
					while (!curr.isDoctype()) {
						curr = curr.parent;
					}
				} else if ((curr.isClosed())) {
					curr = curr.parent;
				}
				DOMComment comment = xmlDocument.createComment(scanner.getTokenOffset(), text.length());
				curr.addChild(comment);
				curr = comment;
				try {
					int endLine = document.positionAt(lastClosed.end).getLine();
					int startLine = document.positionAt(curr.start).getLine();
					if (endLine == startLine && lastClosed.end <= curr.start) {
						comment.commentSameLineEndTag = true;
					}
				} catch (BadLocationException e) {
					LOGGER.log(Level.SEVERE, "XMLParser StartCommentTag bad offset in document", e);
				}
				break;
			}

			case Comment: {
				DOMComment comment = (DOMComment) curr;
				comment.startContent = scanner.getTokenOffset();
				comment.endContent = scanner.getTokenEnd();
				break;
			}

			case EndCommentTag: {
				curr.end = scanner.getTokenEnd();
				curr.closed = true;
				curr = curr.parent;
				break;
			}

			case Content: {
				boolean currIsDeclNode = curr instanceof DTDDeclNode;
				if (currIsDeclNode) {
					curr.end = scanner.getTokenOffset() - 1;
					while (!curr.isDoctype()) {
						curr = curr.getParentNode();
					}
				}
				int start = scanner.getTokenOffset();
				int end = scanner.getTokenEnd();
				DOMText textNode = xmlDocument.createText(start, end);
				textNode.closed = true;

				if (scanner.isTokenTextBlank()) {
					if (ignoreWhitespaceContent) {
						if (curr.hasChildNodes()) {
							break;
						}

						tempWhitespaceContent = textNode;
						break;

					} else if (!currIsDeclNode) {
						textNode.setWhitespace(true);
					} else {
						break;
					}

				}

				curr.addChild(textNode);
				break;
			}

			// DTD

			case DTDStartDoctypeTag: {
				DOMDocumentType doctype = xmlDocument.createDocumentType(scanner.getTokenOffset(), text.length());
				curr.addChild(doctype);
				doctype.parent = curr;
				curr = doctype;
				break;
			}

			case DTDDoctypeName: {
				DOMDocumentType doctype = (DOMDocumentType) curr;
				doctype.setName(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDDocTypeKindPUBLIC: {
				DOMDocumentType doctype = (DOMDocumentType) curr;
				doctype.setKind(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDDocTypeKindSYSTEM: {
				DOMDocumentType doctype = (DOMDocumentType) curr;
				doctype.setKind(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDDoctypePublicId: {
				DOMDocumentType doctype = (DOMDocumentType) curr;
				doctype.setPublicId(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDDoctypeSystemId: {
				DOMDocumentType doctype = (DOMDocumentType) curr;
				doctype.setSystemId(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDStartInternalSubset: {
				DOMDocumentType doctype = (DOMDocumentType) curr;
				doctype.setStartInternalSubset(scanner.getTokenOffset());
				inDTDInternalSubset = true;
				break;
			}

			case DTDEndInternalSubset: {
				while (!curr.isDoctype()) {
					curr.end = scanner.getTokenOffset() - 1;
					curr = curr.getParentNode();
				}
				inDTDInternalSubset = false;
				DOMDocumentType doctype = (DOMDocumentType) curr;
				doctype.setEndInternalSubset(scanner.getTokenEnd());
				break;
			}

			case DTDStartElement: {
				// If previous 'curr' was an unclosed DTD Declaration
				while (!curr.isDoctype()) {
					curr.end = scanner.getTokenOffset();
					curr = curr.getParentNode();
				}

				DTDElementDecl child = new DTDElementDecl(scanner.getTokenOffset(), text.length());
				curr.addChild(child);
				curr = child;
				break;
			}

			case DTDElementDeclName: {
				DTDElementDecl element = (DTDElementDecl) curr;
				element.setName(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDElementCategory: {
				DTDElementDecl element = (DTDElementDecl) curr;
				element.setCategory(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDStartElementContent: {
				DTDElementDecl element = (DTDElementDecl) curr;
				element.setContent(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDElementContent: {
				DTDElementDecl element = (DTDElementDecl) curr;
				element.updateLastParameterEnd(scanner.getTokenEnd());
				break;
			}

			case DTDEndElementContent: {
				DTDElementDecl element = (DTDElementDecl) curr;
				element.updateLastParameterEnd(scanner.getTokenEnd());
				break;
			}

			case DTDStartAttlist: {
				while (!curr.isDoctype()) { // If previous DTD Decl was unclosed
					curr.end = scanner.getTokenOffset();
					curr = curr.getParentNode();
				}
				DTDAttlistDecl child = new DTDAttlistDecl(scanner.getTokenOffset(), text.length());

				isInitialDeclaration = true;
				curr.addChild(child);
				curr = child;
				break;
			}

			case DTDAttlistElementName: {
				DTDAttlistDecl attribute = (DTDAttlistDecl) curr;
				attribute.setName(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDAttlistAttributeName: {
				DTDAttlistDecl attribute = (DTDAttlistDecl) curr;
				if (isInitialDeclaration == false) {
					// All additional declarations are created as new DTDAttlistDecl's
					DTDAttlistDecl child = new DTDAttlistDecl(attribute.getStart(), attribute.getEnd());
					attribute.addAdditionalAttDecl(child);
					child.parent = attribute;

					attribute = child;
					curr = child;
				}
				attribute.setAttributeName(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDAttlistAttributeType: {
				DTDAttlistDecl attribute = (DTDAttlistDecl) curr;
				attribute.setAttributeType(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDAttlistAttributeValue: {
				DTDAttlistDecl attribute = (DTDAttlistDecl) curr;
				attribute.setAttributeValue(scanner.getTokenOffset(), scanner.getTokenEnd());

				if (attribute.parent.isDTDAttListDecl()) { // Is not the root/main ATTLIST node
					curr = attribute.parent;
				} else {
					isInitialDeclaration = false;
				}
				break;
			}

			case DTDStartEntity: {
				while (!curr.isDoctype()) { // If previous DTD Decl was unclosed
					curr.end = scanner.getTokenOffset();
					curr = curr.getParentNode();
				}
				DTDEntityDecl child = new DTDEntityDecl(scanner.getTokenOffset(), text.length());
				curr.addChild(child);
				curr = child;
				break;
			}

			case DTDEntityPercent: {
				DTDEntityDecl entity = (DTDEntityDecl) curr;
				entity.setPercent(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDEntityName: {
				DTDEntityDecl entity = (DTDEntityDecl) curr;
				entity.setName(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDEntityValue: {
				DTDEntityDecl entity = (DTDEntityDecl) curr;
				entity.setValue(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDEntityKindPUBLIC:
			case DTDEntityKindSYSTEM: {
				DTDEntityDecl entity = (DTDEntityDecl) curr;
				entity.setKind(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDEntityPublicId: {
				DTDEntityDecl entity = (DTDEntityDecl) curr;
				entity.setPublicId(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDEntitySystemId: {
				DTDEntityDecl entity = (DTDEntityDecl) curr;
				entity.setSystemId(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDStartNotation: {
				while (!curr.isDoctype()) { // If previous DTD Decl was unclosed
					curr.end = scanner.getTokenOffset();
					curr = curr.getParentNode();
				}
				DTDNotationDecl child = new DTDNotationDecl(scanner.getTokenOffset(), text.length());
				curr.addChild(child);
				curr = child;
				isInitialDeclaration = true;
				break;
			}

			case DTDNotationName: {
				DTDNotationDecl notation = (DTDNotationDecl) curr;
				notation.setName(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDNotationKindPUBLIC: {
				DTDNotationDecl notation = (DTDNotationDecl) curr;
				notation.setKind(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDNotationKindSYSTEM: {
				DTDNotationDecl notation = (DTDNotationDecl) curr;
				notation.setKind(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDNotationPublicId: {
				DTDNotationDecl notation = (DTDNotationDecl) curr;
				notation.setPublicId(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDNotationSystemId: {
				DTDNotationDecl notation = (DTDNotationDecl) curr;
				notation.setSystemId(scanner.getTokenOffset(), scanner.getTokenEnd());
				break;
			}

			case DTDEndTag: {
				if ((curr.isDTDElementDecl() || curr.isDTDAttListDecl() || curr.isDTDEntityDecl()
						|| curr.isDTDNotationDecl())) {
					while (curr.parent != null && !curr.parent.isDoctype()) {
						curr = curr.parent;
					}
					curr.end = scanner.getTokenEnd();
					curr.closed = true;
					curr = curr.parent;
				}
				break;
			}

			case DTDEndDoctypeTag: {
				((DOMDocumentType) curr).end = scanner.getTokenEnd();
				curr.closed = true;
				curr = curr.parent;
				break;
			}

			case DTDUnrecognizedParameters: {
				DTDDeclNode node = (DTDDeclNode) curr;
				node.setUnrecognized(scanner.getTokenOffset(), ((XMLScanner) scanner).getLastNonWhitespaceOffset());
				break;
			}

			default:
			}
			token = scanner.scan();
		}
		if (previousTokenWasEndTagOpen) {
			previousTokenWasEndTagOpen = false;
			if (token != TokenType.EndTag) {
				// The excepted token is not an EndTag, create a fake end tag element
				DOMElement element = xmlDocument.createElement(endTagOpenOffset, endTagOpenOffset + 2);
				element.endTagOpenOffset = endTagOpenOffset;
				curr.addChild(element);
			}
		}
		while (curr.parent != null) {
			curr.end = text.length();
			curr = curr.parent;
		}
		return xmlDocument;
	}

	private static boolean isEmptyElement(String tag) {
		return false;
	}

}
