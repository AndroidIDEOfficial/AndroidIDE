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
package org.eclipse.lemminx.extensions.contentmodel.participants;

import static org.eclipse.lemminx.utils.StringUtils.getString;
import static org.eclipse.lemminx.utils.XMLPositionUtility.selectCurrentTagOffset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLLocator;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMDocumentType;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.ETagRequiredCodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.ETagUnterminatedCodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.ElementUnterminatedCodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.EqRequiredInAttributeCodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.MarkupEntityMismatchCodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.NoGrammarConstraintsCodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.OpenQuoteExpectedCodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.RootElementTypeMustMatchDoctypedeclCodeAction;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.diagnostics.IXMLErrorCode;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lemminx.utils.XMLPositionUtility.EntityReferenceRange;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ResourceOperationKind;

/**
 * XML error code.
 *
 * @see https://wiki.xmldation.com/Support/Validator
 *
 */
public enum XMLSyntaxErrorCode implements IXMLErrorCode {

	AttributeNotUnique, // https://wiki.xmldation.com/Support/Validator/AttributeNotUnique
	AttributeNSNotUnique, // https://wiki.xmldation.com/Support/Validator/AttributeNSNotUnique
	AttributePrefixUnbound, //
	ContentIllegalInProlog, // https://wiki.xmldation.com/Support/Validator/ContentIllegalInProlog
	DashDashInComment, // https://wiki.xmldation.com/Support/Validator/DashDashInComment
	ElementUnterminated, // https://wiki.xmldation.com/Support/Validator/ElementUnterminated
	ElementPrefixUnbound, // https://wiki.xmldation.com/Support/Validator/ElementPrefixUnbound
	EmptyPrefixedAttName, // https://wiki.xmldation.com/Support/Validator/EmptyPrefixedAttName
	EncodingDeclRequired, // https://wiki.xmldation.com/Support/Validator/EncodingDeclRequired
	ETagRequired, // https://wiki.xmldation.com/Support/Validator/ETagRequired
	ETagUnterminated, // https://wiki.xmldation.com/Support/Validator/ETagUnterminated
	EqRequiredInAttribute, // https://wiki.xmldation.com/Support/Validator/EqRequiredInAttribute
	EqRequiredInXMLDecl, //
	IllegalQName, //
	InvalidCommentStart, //
	LessthanInAttValue, //
	MarkupEntityMismatch, //
	MarkupNotRecognizedInContent, //
	NameRequiredInReference, //
	OpenQuoteExpected, //
	PITargetRequired, //
	PseudoAttrNameExpected, //
	QuoteRequiredInXMLDecl, //
	RootElementTypeMustMatchDoctypedecl, //
	SDDeclInvalid, //
	SemicolonRequiredInReference, //
	SpaceRequiredBeforeEncodingInXMLDecl, //
	SpaceRequiredBeforeStandalone, //
	SpaceRequiredInPI, //
	VersionInfoRequired, //
	VersionNotSupported, //
	XMLDeclUnterminated, //
	CustomETag, //
	PrematureEOF, //
	DoctypeNotAllowed, //
	NoMorePseudoAttributes, //
	NoGrammarConstraints;

	private final String code;

	private XMLSyntaxErrorCode() {
		this(null);
	}

	private XMLSyntaxErrorCode(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		if (code == null) {
			return name();
		}
		return code;
	}

	private final static Map<String, XMLSyntaxErrorCode> codes;

	static {
		codes = new HashMap<>();
		for (XMLSyntaxErrorCode errorCode : values()) {
			codes.put(errorCode.getCode(), errorCode);
		}
	}

	public static XMLSyntaxErrorCode get(String name) {
		return codes.get(name);
	}

	/**
	 * Create the LSP range from the SAX error.
	 *
	 * @param location
	 * @param key
	 * @param arguments
	 * @param document
	 * @return the LSP range from the SAX error.
	 */
	public static Range toLSPRange(XMLLocator location, XMLSyntaxErrorCode code, Object[] arguments,
			DOMDocument document) {
		int offset = location.getCharacterOffset() - 1;
		// adjust positions
		switch (code) {
		case SpaceRequiredBeforeStandalone:
		case SpaceRequiredBeforeEncodingInXMLDecl:
		case VersionInfoRequired:
		case ElementPrefixUnbound:
		case RootElementTypeMustMatchDoctypedecl:
			return XMLPositionUtility.selectStartTagName(offset, document);
		case EqRequiredInAttribute: {
			String attrName = getString(arguments[1]);
			return XMLPositionUtility.selectAttributeNameFromGivenNameAt(attrName, offset, document);
		}
		case NoMorePseudoAttributes:
		case EncodingDeclRequired:
		case EqRequiredInXMLDecl:
			return XMLPositionUtility.selectAttributeNameAt(offset, document);
		case AttributeNSNotUnique: {
			String attrName = getString(arguments[1]);
			Range xmlns = XMLPositionUtility.selectAttributeNameFromGivenNameAt("xmlns:" + attrName, offset, document);
			if (xmlns != null) {
				return xmlns;
			}
			return XMLPositionUtility.selectAttributeNameFromGivenNameAt(attrName, offset, document);
		}
		case AttributeNotUnique: {
			String attrName = getString(arguments[1]);
			return XMLPositionUtility.selectAttributeNameFromGivenNameAt(attrName, offset, document);
		}
		case AttributePrefixUnbound: {
			return XMLPositionUtility.selectAttributePrefixFromGivenNameAt(getString(arguments[1]), offset, document);
		}
		case LessthanInAttValue: {
			String attrName = getString(arguments[1]);
			return XMLPositionUtility.selectAttributeValueAt(attrName, offset, document);
		}
		case QuoteRequiredInXMLDecl: {
			String attrName = getString(arguments[0]);
			return XMLPositionUtility.selectAttributeValueAt(attrName, offset, document);
		}
		case EmptyPrefixedAttName: {
			QName qName = (QName) arguments[0];
			return XMLPositionUtility.selectAttributeValueAt(qName.rawname, offset, document);
		}
		case SDDeclInvalid:
		case VersionNotSupported: {
			String attrValue = getString(arguments[0]);
			return XMLPositionUtility.selectAttributeValueByGivenValueAt(attrValue, offset, document);
		}
		case ETagUnterminated: {
			/**
			 * Cases:
			 *
			 * <a> </b>
			 *
			 * <a> <b> </b> </c>
			 *
			 * <a> <a> </a> </b
			 */
			int endOffset = removeLeftSpaces(offset, document.getText());
			return XMLPositionUtility.selectEndTagName(endOffset, document);
		}
		case CustomETag:
			return XMLPositionUtility.selectEndTagName(offset, document);
		case ElementUnterminated: {
			// Cases
			// - <foo><bar <
			// - <foo><bar /<
			DOMNode node = document.findNodeAt(offset);
			if (node != null && node.isElement()) {
				DOMElement tagElement = (DOMElement) node;
				int endOffset = offset;
				if (tagElement.hasChildNodes()) {
					for (DOMNode child : tagElement.getChildren()) {
						if (child.isElement()) {
							endOffset = child.getStart() - 1;
							break;
						}
					}
				}
				return getRangeFromStartNodeToOffset(tagElement, endOffset, document);
			}
			// Should never occurs
			return null;
		}
		case MarkupEntityMismatch: {
			// Cases
			// - <foo
			// - <foo /
			DOMElement documentElement = document.getDocumentElement();
			if (documentElement != null && documentElement.hasEndTag()) {
				return XMLPositionUtility.selectEndTagName(documentElement);
			}
			return XMLPositionUtility.selectRootStartTag(document);
		}
		case ETagRequired: {
			String tag = getString(arguments[0]);
			DOMNode node = document.findNodeAt(offset);
			DOMElement startTagElement = null;
			if (node != null && node.isElement()) {
				DOMElement element = (DOMElement) node;
				if (element.isOrphanEndTag()) {
					// <foo></
					startTagElement = element.getParentElement();
				} else if (element.isInEndTag(offset)) {
					// search the element inside this element
					// <foo>
					// <bar>
					// </foo>
					startTagElement = findChildTag(tag, element);

				}
				return XMLPositionUtility.selectStartTagName(startTagElement);
			}
			// Should never occurs
			return null;
		}
		case SemicolonRequiredInReference: {
			EntityReferenceRange range = XMLPositionUtility.selectEntityReference(offset + 1, document, false);
			return range != null ? range.getRange() : null;
		}
		case ContentIllegalInProlog: {
			int startOffset = offset + 1;
			int endOffset = 0;
			int errorOffset = offset + 1;
			String text = document.getText();
			int startPrologOffset = text.indexOf("<");
			if (errorOffset < startPrologOffset) {
				// Invalid content given before prolog. Prolog should be the first thing in the
				// file if given.
				startOffset = errorOffset;
				endOffset = startPrologOffset;
			} else {
				// Invalid content given after prolog. Either root tag or comment should be
				// present
				int firstStartTagOffset = text.indexOf("<", errorOffset);
				startOffset = errorOffset;
				endOffset = firstStartTagOffset != -1 ? firstStartTagOffset : text.length();
			}
			return XMLPositionUtility.createRange(startOffset, endOffset, document);
		}
		case DashDashInComment: {
			int endOffset = offset + 1;
			int startOffset = offset - 1;
			return XMLPositionUtility.createRange(startOffset, endOffset, document);
		}
		case IllegalQName:
		case InvalidCommentStart:
		case MarkupNotRecognizedInContent:
			return XMLPositionUtility.createRange(offset, offset + 1, document);
		case NameRequiredInReference:
			break;
		case OpenQuoteExpected: {
			return XMLPositionUtility.selectAttributeNameAt(offset - 1, document);
		}
		case DoctypeNotAllowed:
			DOMDocumentType docType = document.getDoctype();
			return XMLPositionUtility.createRange(docType);
		case PITargetRequired:
			// Working
			break;
		case PseudoAttrNameExpected:
			// Working
			// Add better message
			break;
		case SpaceRequiredInPI:
			int start = selectCurrentTagOffset(offset, document) + 1;
			int end = offset + 1;
			return XMLPositionUtility.createRange(start, end, document);
		case PrematureEOF:
		case XMLDeclUnterminated:
			break;
		default:
		}

		return null;

	}

	/**
	 * Remove the offset of the first character from the left offset which is not a
	 * whitespace.
	 *
	 * @param initialOffset the initial offset.
	 *
	 * @param text          the XML content.
	 * @return the offset of the first character from the left offset which is not a
	 *         whitespace.
	 */
	private static int removeLeftSpaces(final int initialOffset, String text) {
		int offset = initialOffset;
		if (offset >= text.length()) {
			return text.length();
		}
		char ch = text.charAt(offset);
		while (Character.isWhitespace(ch)) {
			offset--;
			ch = text.charAt(offset);
		}
		boolean enclosed = false;
		if (ch == '/') {
			// Usecases :
			// - <foo> <
			// - <foo> </
			char previous = text.charAt(offset - 1);
			if (previous == '<') {
				enclosed = false;
				offset--;
			} else {
				enclosed = true;
			}
		} else if (ch != '<') {
			enclosed = true;
		}
		if (enclosed) {
			return offset + 1;
		}
		return offset;
	}

	/**
	 * Returns the proper range from the given node to the given offset.
	 *
	 * @param fromNode the from node.
	 * @param toOffset the to offset.
	 * @param document the DOM document.
	 *
	 * @return the proper range from the given node to the given offset.
	 */
	private static Range getRangeFromStartNodeToOffset(DOMNode fromNode, int toOffset, DOMDocument document) {
		int endOffset = removeLeftSpaces(toOffset, document.getText());
		int startOffset = fromNode.getStart();
		if (fromNode.isElement()) {
			// The from node is a DOM element, adjust end and start offset
			DOMElement fromElement = (DOMElement) fromNode;
			if (fromElement.hasTagName()) {
				startOffset = fromElement.getStartTagOpenOffset() + 1;
				endOffset = Math.max(endOffset,
						fromElement.getStartTagOpenOffset() + fromElement.getTagName().length());
			}
		}
		return XMLPositionUtility.createRange(startOffset, endOffset, document);
	}

	/**
	 * Returns the first child element of the given element which matches the given
	 * tag name and null otherwise.
	 *
	 * @param tagName the tag name.
	 * @param element the DOM element.
	 * @return the first child element of the given element which matches the given
	 *         tag name and null otherwise.
	 */
	private static DOMElement findChildTag(String tagName, DOMElement element) {
		List<DOMNode> children = element.getChildren();
		for (int i = children.size() - 1; i >= 0; i--) {
			DOMNode child = children.get(i);
			if (child.isElement()) {
				DOMElement childElement = ((DOMElement) child);
				if (childElement.isSameTag(tagName)) {
					return childElement;
				} else {
					DOMElement tagElement = findChildTag(tagName, childElement);
					if (tagElement != null) {
						return tagElement;
					}
				}
			}
		}
		return null;
	}

	public static void registerCodeActionParticipants(Map<String, ICodeActionParticipant> codeActions,
			SharedSettings sharedSettings) {
		codeActions.put(ETagUnterminated.getCode(), new ETagUnterminatedCodeAction());
		codeActions.put(ElementUnterminated.getCode(), new ElementUnterminatedCodeAction());
		codeActions.put(EqRequiredInAttribute.getCode(), new EqRequiredInAttributeCodeAction());
		codeActions.put(OpenQuoteExpected.getCode(), new OpenQuoteExpectedCodeAction());
		codeActions.put(MarkupEntityMismatch.getCode(), new MarkupEntityMismatchCodeAction());
		codeActions.put(ETagRequired.getCode(), new ETagRequiredCodeAction());
		codeActions.put(RootElementTypeMustMatchDoctypedecl.getCode(),
				new RootElementTypeMustMatchDoctypedeclCodeAction());
		if (sharedSettings != null
				&& sharedSettings.getWorkspaceSettings().isResourceOperationSupported(ResourceOperationKind.Create)) {
			codeActions.put(NoGrammarConstraints.getCode(), new NoGrammarConstraintsCodeAction());
		}
	}
}
