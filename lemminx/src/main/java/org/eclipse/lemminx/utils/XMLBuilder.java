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
package org.eclipse.lemminx.utils;

import static org.eclipse.lemminx.utils.StringUtils.normalizeSpace;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMComment;
import org.eclipse.lemminx.dom.DTDDeclNode;
import org.eclipse.lemminx.services.extensions.format.IFormatterParticipant;
import org.eclipse.lemminx.settings.EnforceQuoteStyle;
import org.eclipse.lemminx.settings.SharedSettings;

/**
 * XML content builder utilities.
 *
 */
public class XMLBuilder {

	private final SharedSettings sharedSettings;
	private final String lineDelimiter;
	private final StringBuilder xml;
	private final String whitespacesIndent;

	private final Collection<IFormatterParticipant> formatterParticipants;

	private static final Logger LOGGER = Logger.getLogger(XMLBuilder.class.getName());

	public XMLBuilder(SharedSettings sharedSettings, String whitespacesIndent, String lineDelimiter) {
		this(sharedSettings, whitespacesIndent, lineDelimiter, null);
	}

	public XMLBuilder(SharedSettings sharedSettings, String whitespacesIndent, String lineDelimiter,
			Collection<IFormatterParticipant> formatterParticipants) {
		this.whitespacesIndent = whitespacesIndent;
		this.sharedSettings = sharedSettings;
		this.lineDelimiter = lineDelimiter;
		this.formatterParticipants = formatterParticipants;
		this.xml = new StringBuilder();
	}

	public XMLBuilder appendSpace() {
		append(" ");
		return this;
	}

	public XMLBuilder startElement(String prefix, String name, boolean close) {
		append("<");
		if (prefix != null && !prefix.isEmpty()) {
			append(prefix);
			append(":");
		}
		append(name);
		if (close) {
			closeStartElement();
		}
		return this;
	}

	public XMLBuilder startElement(String name, boolean close) {
		return startElement(null, name, close);
	}

	public XMLBuilder endElement(String name, boolean isEndTagClosed) {
		return endElement(null, name, isEndTagClosed);
	}

	public XMLBuilder endElement(String name) {
		return endElement(null, name, true);
	}

	public XMLBuilder endElement(String prefix, String name) {
		return endElement(prefix, name, true);
	}

	public XMLBuilder endElement(String prefix, String name, boolean isEndTagClosed) {
		append("</");
		if (prefix != null && !prefix.isEmpty()) {
			append(prefix);
			append(":");
		}
		if (name != null) {
			append(name);
		}
		if (isEndTagClosed) {
			append(">");
		}
		return this;
	}

	public XMLBuilder closeStartElement() {
		append(">");
		return this;
	}

	public XMLBuilder selfCloseElement() {
		if (isSpaceBeforeEmptyCloseTag() && !isLastLineEmptyOrWhitespace()) {
			appendSpace();
		}
		append("/>");
		return this;
	}

	public XMLBuilder addSingleAttribute(DOMAttr attr) {
		return addSingleAttribute(attr, false, true);
	}

	public XMLBuilder addSingleAttribute(DOMAttr attr, boolean surroundWithQuotes, boolean prependSpace) {
		return addSingleAttribute(attr.getName(), attr.getOriginalValue(), surroundWithQuotes, prependSpace, attr);
	}

	public XMLBuilder addSingleAttribute(String name, String value, boolean surroundWithQuotes) {
		return addSingleAttribute(name, value, surroundWithQuotes, true, null);
	}

	/**
	 * Used when only one attribute is being added to a node.
	 *
	 * It will not perform any linefeeds and only basic indentation.
	 *
	 * @param name               attribute name
	 * @param value              attribute value
	 * @param surroundWithQuotes true if quotes should be added around originalValue
	 * @return this XML Builder
	 */
	private XMLBuilder addSingleAttribute(String name, String value, boolean surroundWithQuotes, boolean prependSpace,
			DOMAttr attr) {
		if (prependSpace) {
			appendSpace();
		}
		addAttributeContents(name, true, value, surroundWithQuotes, attr);
		return this;
	}

	/**
	 * Add prolog attribute
	 *
	 * It will not perform any linefeeds and only basic indentation.
	 *
	 * @param attr attribute
	 * @return this XML Builder
	 */
	public XMLBuilder addPrologAttribute(DOMAttr attr) {
		appendSpace();
		addAttributeContents(attr.getName(), attr.hasDelimiter(), attr.getOriginalValue(), false, attr);
		return this;
	}

	/**
	 * Used when you are knowingly adding multiple attributes.
	 *
	 * Does linefeeds and indentation.
	 *
	 * @param name
	 * @param value
	 * @param level
	 * @return
	 */
	public XMLBuilder addAttribute(String name, String value, int level, boolean surroundWithQuotes) {
		if (isSplitAttributes()) {
			linefeed();
			indent(level + sharedSettings.getFormattingSettings().getSplitAttributesIndentSize());
		} else {
			appendSpace();
		}

		addAttributeContents(name, true, value, surroundWithQuotes, null);
		return this;
	}

	public XMLBuilder addAttribute(DOMAttr attr, int level) {
		return addAttribute(attr, level, false);
	}

	private XMLBuilder addAttribute(DOMAttr attr, int level, boolean surroundWithQuotes) {
		if (isSplitAttributes()) {
			linefeed();
			indent(level + sharedSettings.getFormattingSettings().getSplitAttributesIndentSize());
		} else {
			appendSpace();
		}

		addAttributeContents(attr.getName(), attr.hasDelimiter(), attr.getOriginalValue(), surroundWithQuotes, attr);
		return this;
	}

	/**
	 * Builds the attribute {name, '=', and value}.
	 *
	 * Never puts quotes around unquoted values unless indicated to by
	 * 'surroundWithQuotes'
	 *
	 * @param name               name of the attribute
	 * @param equalsSign         true if equals sign exists, false otherwise
	 * @param originalValue      value of the attribute
	 * @param surroundWithQuotes true if quotes should be added around
	 *                           originalValue, false otherwise
	 */
	private void addAttributeContents(String name, boolean equalsSign, String originalValue, boolean surroundWithQuotes,
			DOMAttr attr) {
		if (name != null) {
			append(name);
		}
		if (equalsSign) {
			append("=");
		}
		if (originalValue != null) {
			char preferredQuote = getQuotationAsChar();
			Character quote = null;
			String valueWithoutQuote = originalValue;
			if (StringUtils.isQuoted(originalValue)) {
				if (getEnforceQuoteStyle() == EnforceQuoteStyle.preferred
						&& originalValue.charAt(0) != preferredQuote) {
					quote = preferredQuote;
				} else {
					quote = originalValue.charAt(0);
				}
				valueWithoutQuote = StringUtils.convertToQuotelessValue(originalValue);
			} else if (surroundWithQuotes) {
				quote = preferredQuote;
			}
			formatAttributeValue(name, valueWithoutQuote, quote, attr);
		}
	}

	private void formatAttributeValue(String name, String valueWithoutQuote, Character quote, DOMAttr attr) {
		if (formatterParticipants != null) {
			for (IFormatterParticipant formatterParticipant : formatterParticipants) {
				try {
					if (formatterParticipant.formatAttributeValue(name, valueWithoutQuote, quote, attr, this)) {
						return;
					}
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE,
						"Error while processing format attributes for the participant '" + formatterParticipant.getClass().getName() + "'.", e);
				}
			}
		}
		if (quote != null) {
			append(quote);
		}
		append(valueWithoutQuote);
		if (quote != null) {
			append(quote);
		}
	}

	public void append(String str) {
		xml.append(str);
	}

	public void append(char c) {
		xml.append(c);
	}

	public XMLBuilder linefeed() {
		append(lineDelimiter);
		if (whitespacesIndent != null) {
			append(whitespacesIndent);
		}
		return this;
	}

	/**
	 * Returns this XMLBuilder with <code>text</code> added
	 *
	 * @param text the text to add
	 * @return this XMLBuilder with <code>text</code> added
	 */
	public XMLBuilder addContent(String text) {
		return addContent(text, false, false, null);
	}

	/**
	 * Returns this XMLBuilder with <code>text</code> added depending on
	 * <code>isWhitespaceContent</code>, <code>hasSiblings</code> and
	 * <code>delimiter</code>
	 *
	 * @param text                the proposed text to add
	 * @param isWhitespaceContent whether or not the text contains only whitespace
	 *                            content
	 * @param hasSiblings         whether or not the corresponding text node has
	 *                            siblings
	 * @param delimiter           line delimiter
	 * @return this XMLBuilder with <code>text</code> added depending on
	 *         <code>isWhitespaceContent</code>, <code>hasSiblings</code> and
	 *         <code>delimiter</code>
	 */
	public XMLBuilder addContent(String text, boolean isWhitespaceContent, boolean hasSiblings, String delimiter) {
		if (!isWhitespaceContent) {
			if (isJoinContentLines()) {
				text = StringUtils.normalizeSpace(text);
			} else if (hasSiblings) {
				text = text.trim();
			}
			if (isTrimTrailingWhitespace()) {
				text = trimTrailingSpacesEachLine(text);
			}
			append(text);
		} else if (!hasSiblings && isPreserveEmptyContent()) {
			append(text);
		} else if (hasSiblings) {
			int preservedNewLines = getPreservedNewlines();
			if (preservedNewLines > 0) {
				int newLineCount = StringUtils.getNumberOfNewLines(text, isWhitespaceContent, delimiter,
						preservedNewLines);
				for (int i = 0; i < newLineCount - 1; i++) { // - 1 because the node after will insert a delimiter
					append(delimiter);
				}
			}
		}
		return this;
	}

	public XMLBuilder indent(int level) {
		for (int i = 0; i < level; i++) {
			if (isInsertSpaces()) {
				for (int j = 0; j < getTabSize(); j++) {
					appendSpace();
				}
			} else {
				append("\t");
			}
		}
		return this;
	}

	public XMLBuilder startPrologOrPI(String tagName) {
		append("<?");
		append(tagName);
		return this;
	}

	public XMLBuilder addContentPI(String content) {
		appendSpace();
		append(content);
		return this;
	}

	public XMLBuilder endPrologOrPI() {
		append("?>");
		return this;
	}

	@Override
	public String toString() {
		return xml.toString();
	}

	/**
	 * Trims the trailing newlines for the current XML StringBuilder
	 */
	public void trimFinalNewlines() {
		int i = xml.length() - 1;
		while (i >= 0 && (xml.charAt(i) == '\r' || xml.charAt(i) == '\n')) {
			xml.deleteCharAt(i--);
		}
	}

	/**
	 * Returns <code>str</code> with the trailing spaces from each line removed
	 *
	 * @param str the String
	 * @return <code>str</code> with the trailing spaces from each line removed
	 */
	private static String trimTrailingSpacesEachLine(String str) {
		StringBuilder sb = new StringBuilder(str);
		int i = str.length() - 1;
		boolean removeSpaces = true;
		while (i >= 0) {
			char curr = sb.charAt(i);
			if (curr == '\n' || curr == '\r') {
				removeSpaces = true;
			} else if (removeSpaces && Character.isWhitespace(curr)) {
				sb.deleteCharAt(i);
			} else {
				removeSpaces = false;
			}
			i--;
		}
		return sb.toString();
	}

	public XMLBuilder startCDATA() {
		append("<![CDATA[");
		return this;
	}

	public XMLBuilder addContentCDATA(String content) {
		if (isJoinCDATALines()) {
			content = normalizeSpace(content);
		}
		append(content);
		return this;
	}

	public XMLBuilder endCDATA() {
		append("]]>");
		return this;
	}

	public XMLBuilder startComment(DOMComment comment) {
		if (comment.isCommentSameLineEndTag()) {
			appendSpace();
		}
		append("<!--");
		return this;
	}

	public XMLBuilder addContentComment(String content) {
		if (isJoinCommentLines()) {
			appendSpace();
			append(normalizeSpace(content));
			appendSpace();
		} else {
			append(content);
		}
		return this;
	}

	public XMLBuilder addDeclTagStart(DTDDeclNode tag) {
		append("<!" + tag.getDeclType());
		return this;
	}

	public XMLBuilder addDeclTagStart(String declTagName) {
		append("<!" + declTagName);
		return this;
	}

	public XMLBuilder startDoctype() {
		append("<!DOCTYPE");
		return this;
	}

	public XMLBuilder startDTDElementDecl() {
		append("<!ELEMENT");
		return this;
	}

	public XMLBuilder startDTDAttlistDecl() {
		append("<!ATTLIST");
		return this;
	}

	public XMLBuilder addParameter(String parameter) {
		return addUnindentedParameter(" " + replaceQuotesIfNeeded(parameter));
	}

	public XMLBuilder addUnindentedParameter(String parameter) {
		append(replaceQuotesIfNeeded(parameter));
		return this;
	}

	public XMLBuilder startDoctypeInternalSubset() {
		append(" [");
		return this;
	}

	public XMLBuilder startUnindentedDoctypeInternalSubset() {
		append("[");
		return this;
	}

	public XMLBuilder endDoctypeInternalSubset() {
		append("]");
		return this;
	}

	public XMLBuilder endComment() {
		append("-->");
		return this;
	}

	public XMLBuilder endDoctype() {
		append(">");
		return this;
	}

	public boolean isLastLineEmptyOrWhitespace() {
		if (this.xml.length() == 0) {
			return true;
		}
		int i = this.xml.length() - 1;
		while (i > 0 && Character.isSpaceChar(this.xml.charAt(i))) {
			i--;
		}
		return i > 0 && (this.xml.charAt(i) == '\r' || this.xml.charAt(i) == '\n');
	}

	private String replaceQuotesIfNeeded(String str) {
		if (getEnforceQuoteStyle() != EnforceQuoteStyle.preferred) {
			return str;
		}
		if (StringUtils.isQuoted(str)) {
			String quote = getQuotationAsString();
			return quote + StringUtils.convertToQuotelessValue(str) + quote;
		}
		return str;
	}

	private EnforceQuoteStyle getEnforceQuoteStyle() {
		return this.sharedSettings.getFormattingSettings().getEnforceQuoteStyle();
	}

	private String getQuotationAsString() {
		return this.sharedSettings.getPreferences().getQuotationAsString();
	}

	private boolean isJoinCommentLines() {
		return sharedSettings.getFormattingSettings().isJoinCommentLines();
	}

	private boolean isJoinCDATALines() {
		return sharedSettings.getFormattingSettings().isJoinCDATALines();
	}

	private boolean isSplitAttributes() {
		return sharedSettings.getFormattingSettings().isSplitAttributes();
	}

	private boolean isInsertSpaces() {
		return sharedSettings.getFormattingSettings().isInsertSpaces();
	}

	private int getTabSize() {
		return sharedSettings.getFormattingSettings().getTabSize();
	}

	private boolean isJoinContentLines() {
		return sharedSettings.getFormattingSettings().isJoinContentLines();
	}

	private boolean isPreserveEmptyContent() {
		return sharedSettings.getFormattingSettings().isPreserveEmptyContent();
	}

	private boolean isTrimTrailingWhitespace() {
		return sharedSettings.getFormattingSettings().isTrimTrailingWhitespace();
	}

	private int getPreservedNewlines() {
		return sharedSettings.getFormattingSettings().getPreservedNewlines();
	}

	private boolean isSpaceBeforeEmptyCloseTag() {
		return sharedSettings.getFormattingSettings().isSpaceBeforeEmptyCloseTag();
	}

	private char getQuotationAsChar() {
		return sharedSettings.getPreferences().getQuotationAsChar();
	}

	public int length() {
		return xml.length();
	}

	public char charAt(int index) {
		return xml.charAt(index);
	}

	public SharedSettings getSharedSettings() {
		return sharedSettings;
	}

}
