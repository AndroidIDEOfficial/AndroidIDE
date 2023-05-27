/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.eclipse.lemminx.dom.builder;

import static org.eclipse.lemminx.utils.StringUtils.normalizeSpace;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMComment;
import org.eclipse.lemminx.dom.DTDDeclNode;
import org.eclipse.lemminx.utils.StringUtils;

/**
 * XML content builder utilities.
 */
public class XmlBuilder {

  private final BaseXmlFormattingOptions formattingOptions;
  private final String lineDelimiter;
  private final StringBuilder xml;
  private final String whitespacesIndent;

  public XmlBuilder() {
    this("", System.lineSeparator(), new BaseXmlFormattingOptions());
  }

  public XmlBuilder(String whitespacesIndent, String lineDelimiter, BaseXmlFormattingOptions options
  ) {
    this.whitespacesIndent = whitespacesIndent;
    this.lineDelimiter = lineDelimiter;
    this.formattingOptions = options;
    this.xml = new StringBuilder();
  }

  public XmlBuilder appendSpace() {
    append(" ");
    return this;
  }

  public XmlBuilder startElement(String prefix, String name, boolean close) {
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

  public XmlBuilder startElement(String name, boolean close) {
    return startElement(null, name, close);
  }

  public XmlBuilder endElement(String name, boolean isEndTagClosed) {
    return endElement(null, name, isEndTagClosed);
  }

  public XmlBuilder endElement(String name) {
    return endElement(null, name, true);
  }

  public XmlBuilder endElement(String prefix, String name) {
    return endElement(prefix, name, true);
  }

  public XmlBuilder endElement(String prefix, String name, boolean isEndTagClosed) {
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

  public XmlBuilder closeStartElement() {
    append(">");
    return this;
  }

  public XmlBuilder selfCloseElement() {
    if (isSpaceBeforeEmptyCloseTag() && !isLastLineEmptyOrWhitespace()) {
      appendSpace();
    }
    append("/>");
    return this;
  }

  public XmlBuilder androidAttribute(String name, String value) {
    return addSingleAttribute("android:" + name, value, true);
  }

  public XmlBuilder addSingleAttribute(DOMAttr attr) {
    return addSingleAttribute(attr, false, true);
  }

  public XmlBuilder addSingleAttribute(DOMAttr attr, boolean surroundWithQuotes,
                                       boolean prependSpace
  ) {
    return addSingleAttribute(attr.getName(), attr.getOriginalValue(), surroundWithQuotes,
      prependSpace);
  }

  public XmlBuilder addSingleAttribute(String name, String value) {
    return addSingleAttribute(name, value, true);
  }

  public XmlBuilder addSingleAttribute(String name, String value, boolean surroundWithQuotes) {
    return addSingleAttribute(name, value, surroundWithQuotes, true);
  }

  /**
   * Used when only one attribute is being added to a node.
   *
   * <p>It will not perform any linefeeds and only basic indentation.
   *
   * @param name               attribute name
   * @param value              attribute value
   * @param surroundWithQuotes true if quotes should be added around originalValue
   * @return this XML Builder
   */
  private XmlBuilder addSingleAttribute(String name, String value, boolean surroundWithQuotes,
                                        boolean prependSpace
  ) {
    if (prependSpace) {
      appendSpace();
    }
    addAttributeContents(name, true, value, surroundWithQuotes);
    return this;
  }

  /**
   * Add prolog attribute
   *
   * <p>It will not perform any linefeeds and only basic indentation.
   *
   * @param attr attribute
   * @return this XML Builder
   */
  public XmlBuilder addPrologAttribute(DOMAttr attr) {
    appendSpace();
    addAttributeContents(attr.getName(), attr.hasDelimiter(), attr.getOriginalValue(), false);
    return this;
  }

  /**
   * Used when you are knowingly adding multiple attributes.
   *
   * <p>Does linefeeds and indentation.
   *
   * @param name
   * @param value
   * @param level
   * @return
   */
  public XmlBuilder addAttribute(String name, String value, int level, boolean surroundWithQuotes) {
    if (isSplitAttributes()) {
      linefeed();
      indent(level + formattingOptions.getSplitAttributesIndentSize());
    } else {
      appendSpace();
    }

    addAttributeContents(name, true, value, surroundWithQuotes);
    return this;
  }

  public XmlBuilder addAttribute(DOMAttr attr, int level) {
    return addAttribute(attr, level, false);
  }

  private XmlBuilder addAttribute(DOMAttr attr, int level, boolean surroundWithQuotes) {
    if (isSplitAttributes()) {
      linefeed();
      indent(level + formattingOptions.getSplitAttributesIndentSize());
    } else {
      appendSpace();
    }

    addAttributeContents(attr.getName(), attr.hasDelimiter(), attr.getOriginalValue(),
      surroundWithQuotes);
    return this;
  }

  /**
   * Builds the attribute {name, '=', and value}.
   *
   * <p>Never puts quotes around unquoted values unless indicated to by 'surroundWithQuotes'
   *
   * @param name               name of the attribute
   * @param equalsSign         true if equals sign exists, false otherwise
   * @param originalValue      value of the attribute
   * @param surroundWithQuotes true if quotes should be added around originalValue, false otherwise
   */
  protected void addAttributeContents(String name, boolean equalsSign, String originalValue,
                                    boolean surroundWithQuotes
  ) {
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
        if (originalValue.charAt(0) != preferredQuote) {
          quote = preferredQuote;
        } else {
          quote = originalValue.charAt(0);
        }
        valueWithoutQuote = StringUtils.convertToQuotelessValue(originalValue);
      } else if (surroundWithQuotes) {
        quote = preferredQuote;
      }
      formatAttributeValue(valueWithoutQuote, quote);
    }
  }

  private void formatAttributeValue(String valueWithoutQuote, Character quote) {
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

  public XmlBuilder linefeed() {
    append(lineDelimiter);
    if (whitespacesIndent != null) {
      append(whitespacesIndent);
    }
    return this;
  }

  /**
   * Returns this XmlBuilder with <code>text</code> added
   *
   * @param text the text to add
   * @return this XmlBuilder with <code>text</code> added
   */
  public XmlBuilder addContent(String text) {
    return addContent(text, false, false, null);
  }

  /**
   * Returns this XmlBuilder with <code>text</code> added depending on <code>isWhitespaceContent
   * </code>, <code>hasSiblings</code> and <code>delimiter</code>
   *
   * @param text                the proposed text to add
   * @param isWhitespaceContent whether or not the text contains only whitespace content
   * @param hasSiblings         whether or not the corresponding text node has siblings
   * @param delimiter           line delimiter
   * @return this XmlBuilder with <code>text</code> added depending on <code>isWhitespaceContent
   * </code>, <code>hasSiblings</code> and <code>delimiter</code>
   */
  public XmlBuilder addContent(String text, boolean isWhitespaceContent, boolean hasSiblings,
                               String delimiter
  ) {
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
        for (int i = 0; i < newLineCount - 1;
          i++) { // - 1 because the node after will insert a delimiter
          append(delimiter);
        }
      }
    }
    return this;
  }

  public XmlBuilder indent(int level) {
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

  public XmlBuilder startPrologOrPI(String tagName) {
    append("<?");
    append(tagName);
    return this;
  }

  public XmlBuilder addContentPI(String content) {
    appendSpace();
    append(content);
    return this;
  }

  public XmlBuilder endPrologOrPI() {
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

  public XmlBuilder startCDATA() {
    append("<![CDATA[");
    return this;
  }

  public XmlBuilder addContentCDATA(String content) {
    if (isJoinCDATALines()) {
      content = normalizeSpace(content);
    }
    append(content);
    return this;
  }

  public XmlBuilder endCDATA() {
    append("]]>");
    return this;
  }

  public XmlBuilder startComment(DOMComment comment) {
    if (comment.isCommentSameLineEndTag()) {
      appendSpace();
    }
    append("<!--");
    return this;
  }

  public XmlBuilder addContentComment(String content) {
    if (isJoinCommentLines()) {
      appendSpace();
      append(normalizeSpace(content));
      appendSpace();
    } else {
      append(content);
    }
    return this;
  }

  public XmlBuilder addDeclTagStart(DTDDeclNode tag) {
    append("<!" + tag.getDeclType());
    return this;
  }

  public XmlBuilder addDeclTagStart(String declTagName) {
    append("<!" + declTagName);
    return this;
  }

  public XmlBuilder startDoctype() {
    append("<!DOCTYPE");
    return this;
  }

  public XmlBuilder startDTDElementDecl() {
    append("<!ELEMENT");
    return this;
  }

  public XmlBuilder startDTDAttlistDecl() {
    append("<!ATTLIST");
    return this;
  }

  public XmlBuilder addParameter(String parameter) {
    return addUnindentedParameter(" " + replaceQuotesIfNeeded(parameter));
  }

  public XmlBuilder addUnindentedParameter(String parameter) {
    append(replaceQuotesIfNeeded(parameter));
    return this;
  }

  public XmlBuilder startDoctypeInternalSubset() {
    append(" [");
    return this;
  }

  public XmlBuilder startUnindentedDoctypeInternalSubset() {
    append("[");
    return this;
  }

  public XmlBuilder endDoctypeInternalSubset() {
    append("]");
    return this;
  }

  public XmlBuilder endComment() {
    append("-->");
    return this;
  }

  public XmlBuilder endDoctype() {
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
    if (StringUtils.isQuoted(str)) {
      String quote = getQuotationAsString();
      return quote + StringUtils.convertToQuotelessValue(str) + quote;
    }
    return str;
  }

  private String getQuotationAsString() {
    return "\"";
  }

  private boolean isJoinCommentLines() {
    return formattingOptions.isJoinCommentLines();
  }

  private boolean isJoinCDATALines() {
    return formattingOptions.isJoinCDataLines();
  }

  private boolean isSplitAttributes() {
    return formattingOptions.isSplitAttributes();
  }

  private boolean isInsertSpaces() {
    return formattingOptions.isUseSoftTab();
  }

  private int getTabSize() {
    return formattingOptions.getTabSize();
  }

  private boolean isJoinContentLines() {
    return formattingOptions.isJoinContentLines();
  }

  private boolean isPreserveEmptyContent() {
    return formattingOptions.isPreserveEmptyContent();
  }

  private boolean isTrimTrailingWhitespace() {
    return formattingOptions.isTrimTrailingWhitespace();
  }

  private int getPreservedNewlines() {
    return formattingOptions.getPreservedNewLines();
  }

  private boolean isSpaceBeforeEmptyCloseTag() {
    return formattingOptions.isSpaceBeforeEmptyCloseTag();
  }

  private char getQuotationAsChar() {
    return '"';
  }

  public int length() {
    return xml.length();
  }

  public char charAt(int index) {
    return xml.charAt(index);
  }
}
