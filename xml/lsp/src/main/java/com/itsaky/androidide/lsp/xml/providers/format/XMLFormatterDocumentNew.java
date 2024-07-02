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

/*******************************************************************************
 * Copyright (c) 2022 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package com.itsaky.androidide.lsp.xml.providers.format;

import com.itsaky.androidide.lsp.models.TextEdit;
import com.itsaky.androidide.lsp.xml.models.XMLFormattingOptions;
import com.itsaky.androidide.lsp.xml.models.XMLServerSettings;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.preferences.internal.EditorPreferences;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.TextDocument;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMCDATASection;
import org.eclipse.lemminx.dom.DOMComment;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMDocumentType;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DOMProcessingInstruction;
import org.eclipse.lemminx.dom.DOMText;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Experimental XML formatter which generates several text edit to remove, add, update spaces /
 * indent.
 *
 * @author Angelo ZERR
 */
public class XMLFormatterDocumentNew {

  private static final Logger LOGGER = Logger.getLogger(XMLFormatterDocumentNew.class.getName());

  private static final String XML_SPACE_ATTR = "xml:space";

  private static final String XML_SPACE_ATTR_DEFAULT = "default";

  private static final String XML_SPACE_ATTR_PRESERVE = "preserve";

  private final DOMDocument xmlDocument;
  private final TextDocument textDocument;
  private final String lineDelimiter;
  private final XMLServerSettings sharedSettings = XMLServerSettings.INSTANCE;

  private final DOMProcessingInstructionFormatter processingInstructionFormatter;

  private final DOMDocTypeFormatter docTypeFormatter;

  private final DOMElementFormatter elementFormatter;

  private final DOMAttributeFormatter attributeFormatter;

  private final DOMTextFormatter textFormatter;

  private final DOMCommentFormatter commentFormatter;

  private final DOMCDATAFormatter cDATAFormatter;

  private int startOffset = -1;
  private int endOffset = -1;

  private CancelChecker cancelChecker;

  /**
   * XML formatter document.
   */
  public XMLFormatterDocumentNew(DOMDocument xmlDocument, Range range) {
    this.xmlDocument = xmlDocument;
    this.textDocument = xmlDocument.getTextDocument();
    this.lineDelimiter = computeLineDelimiter(textDocument);
    if (range != null) {
      try {
        startOffset = textDocument.offsetAt(range.getStart());
        endOffset = textDocument.offsetAt(range.getEnd());
      } catch (BadLocationException e) {
        LOGGER.log(Level.SEVERE, e.getMessage(), e);
      }
    }
    this.docTypeFormatter = new DOMDocTypeFormatter(this);
    this.attributeFormatter = new DOMAttributeFormatter(this);
    this.elementFormatter = new DOMElementFormatter(this, attributeFormatter);
    this.processingInstructionFormatter =
        new DOMProcessingInstructionFormatter(this, attributeFormatter);
    this.textFormatter = new DOMTextFormatter(this);
    this.commentFormatter = new DOMCommentFormatter(this);
    this.cDATAFormatter = new DOMCDATAFormatter(this);
  }

  private static String computeLineDelimiter(TextDocument textDocument) {
    try {
      return textDocument.lineDelimiter(0);
    } catch (BadLocationException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    return System.lineSeparator();
  }

  /**
   * Returns a List containing multiple TextEdit, containing the newly formatted changes of an XML
   * document.
   *
   * @return List containing multiple TextEdit of an XML document.
   * @throws BadLocationException
   */
  public List<? extends TextEdit> format() throws BadLocationException {
    return format(xmlDocument, startOffset, endOffset);
  }

  public List<? extends TextEdit> format(DOMDocument document, int start, int end) {
    List<TextEdit> edits = new ArrayList<>();

    // get initial document region
    DOMNode currentDOMNode = getDOMNodeToFormat(document, start, end);

    if (currentDOMNode != null) {
      int startOffset = currentDOMNode.getStart();

      XMLFormattingConstraints parentConstraints = getNodeConstraints(currentDOMNode);

      // initialize available line width
      int lineWidth = getMaxLineWidth();

      try {
        int lineOffset = textDocument.lineOffsetAt(startOffset);
        lineWidth = lineWidth - (startOffset - lineOffset);
      } catch (BadLocationException e) {
        LOGGER.log(Level.SEVERE, e.getMessage(), e);
      }
      parentConstraints.setAvailableLineWidth(lineWidth);

      // format all siblings (and their children) as long they
      // overlap with start/end offset
      if (currentDOMNode.isElement()) {
        parentConstraints.setFormatElementCategory(
            getFormatElementCategory((DOMElement) currentDOMNode, null));
      } else {
        parentConstraints.setFormatElementCategory(FormatElementCategory.IgnoreSpace);
      }
      formatSiblings(edits, currentDOMNode, parentConstraints, start, end);
    }

    boolean insertFinalNewline = isInsertFinalNewline();
    if (isTrimFinalNewlines()) {
      trimFinalNewlines(insertFinalNewline, edits);
    }
    if (insertFinalNewline) {
      String xml = textDocument.getText();
      int endDocument = xml.length() - 1;
      if (endDocument >= 0) {
        char c = xml.charAt(endDocument);
        if (c != '\n') {
          try {
            Position pos = textDocument.positionAt(endDocument);
            pos.setColumn(pos.getColumn() + 1);
            Range range = new Range(pos, pos);
            edits.add(new TextEdit(range, lineDelimiter));
          } catch (BadLocationException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
          }
        }
      }
    }
    return edits;
  }

  /**
   * Returns the DOM node to format according to the given range and the DOM document otherwise.
   *
   * @param document the DOM document.
   * @param start    the start range offset and -1 otherwise.
   * @param end      the end range offset and -1 otherwise.
   * @return the DOM node to format according to the given range and the DOM document otherwise.
   */
  private static DOMNode getDOMNodeToFormat(DOMDocument document, int start, int end) {
    if (start != -1 && end != -1) {
      DOMNode startNode = document.findNodeAt(start);
      DOMNode endNode = document.findNodeBefore(end);

      if (endNode.getStart() == start) {
        // ex :
        // <div>
        // |<img />|
        // </div>
        return endNode;
      }

      if (isCoverNode(startNode, endNode)) {
        return startNode;
      } else if (isCoverNode(endNode, startNode)) {
        return endNode;
      } else {
        DOMNode startParent = startNode.getParentNode();
        DOMNode endParent = endNode.getParentNode();
        while (startParent != null && endParent != null) {
          if (isCoverNode(startParent, endParent)) {
            return startParent;
          } else if (isCoverNode(endParent, startParent)) {
            return endParent;
          }
          startParent = startParent.getParentNode();
          endParent = endParent.getParentNode();
        }
      }
    }
    return document;
  }

  private static boolean isCoverNode(DOMNode startNode, DOMNode endNode) {
    return (startNode.getStart() < endNode.getStart() && startNode.getEnd() > endNode.getEnd())
        || startNode == endNode;
  }

  /**
   * Returns the DOM node constraints of the given DOM node.
   *
   * @param node the DOM node.
   * @return the DOM node constraints of the given DOM node.
   */
  private XMLFormattingConstraints getNodeConstraints(DOMNode node) {
    XMLFormattingConstraints result = new XMLFormattingConstraints();
    // Compute the indent level according to the parent node.
    int indentLevel = 0;
    while (node != null) {
      node = node.getParentElement();
      if (node != null) {
        indentLevel++;
      }
    }
    result.setIndentLevel(indentLevel);
    return result;
  }

  private void formatSiblings(
      List<TextEdit> edits,
      DOMNode domNode,
      XMLFormattingConstraints parentConstraints,
      int start,
      int end) {
    DOMNode currentDOMNode = domNode;
    while (currentDOMNode != null) {
      if (cancelChecker != null) {
        cancelChecker.checkCanceled();
      }
      format(currentDOMNode, parentConstraints, start, end, edits);
      currentDOMNode = currentDOMNode.getNextSibling();
    }
  }

  private void format(
      DOMNode child,
      XMLFormattingConstraints parentConstraints,
      int start,
      int end,
      List<TextEdit> edits) {

    switch (child.getNodeType()) {
      case Node.DOCUMENT_TYPE_NODE:
        DOMDocumentType docType = (DOMDocumentType) child;
        docTypeFormatter.formatDocType(docType, parentConstraints, start, end, edits);
        break;

      case Node.DOCUMENT_NODE:
        DOMDocument document = (DOMDocument) child;
        formatChildren(document, parentConstraints, start, end, edits);
        break;

      case DOMNode.PROCESSING_INSTRUCTION_NODE:
        DOMProcessingInstruction processingInstruction = (DOMProcessingInstruction) child;
        processingInstructionFormatter.formatProcessingInstruction(
            processingInstruction, parentConstraints, edits);
        break;

      case Node.ELEMENT_NODE:
        DOMElement element = (DOMElement) child;
        elementFormatter.formatElement(element, parentConstraints, start, end, edits);
        break;

      case Node.TEXT_NODE:
        DOMText textNode = (DOMText) child;
        textFormatter.formatText(textNode, parentConstraints, edits);
        break;

      case Node.COMMENT_NODE:
        DOMComment commentNode = (DOMComment) child;
        commentFormatter.formatComment(commentNode, parentConstraints, edits);
        break;

      case Node.CDATA_SECTION_NODE:
        DOMCDATASection cDATANode = (DOMCDATASection) child;
        cDATAFormatter.formatCDATASection(cDATANode, parentConstraints, edits);
        break;

      default:
        // unknown, so just leave alone for now but make sure to update
        // available line width
        int width = updateLineWidthWithLastLine(child, parentConstraints.getAvailableLineWidth());
        parentConstraints.setAvailableLineWidth(width);
    }
  }

  public void formatChildren(
      DOMNode currentDOMNode,
      XMLFormattingConstraints parentConstraints,
      int start,
      int end,
      List<TextEdit> edits) {
    for (DOMNode child : currentDOMNode.getChildren()) {
      format(child, parentConstraints, start, end, edits);
    }
  }

  public void formatAttributeValue(DOMAttr attr, int indentLevel, List<TextEdit> edits) {
  }

  public void removeLeftSpaces(int leftLimit, int to, List<TextEdit> edits) {
    replaceLeftSpacesWith(leftLimit, to, "", edits);
  }

  public void replaceLeftSpacesWithOneSpace(int leftLimit, int to, List<TextEdit> edits) {
    replaceLeftSpacesWith(leftLimit, to, " ", edits);
  }

  void replaceLeftSpacesWith(int leftLimit, int to, String replacement, List<TextEdit> edits) {
    int from = adjustOffsetWithLeftWhitespaces(leftLimit, to);
    if (from >= 0) {
      createTextEditIfNeeded(from, to, replacement, edits);
    }
  }

  void replaceQuoteWithPreferred(int from, int to, List<TextEdit> edits) {
    createTextEditIfNeeded(from, to, getQuotationAsString(), edits);
  }

  public int adjustOffsetWithLeftWhitespaces(int leftLimit, int to) {
    return TextEditUtils.adjustOffsetWithLeftWhitespaces(leftLimit, to, textDocument.getText());
  }

  public int replaceLeftSpacesWithIndentation(
      int indentLevel, int leftLimit, int to, boolean addLineSeparator, List<TextEdit> edits) {
    int from = adjustOffsetWithLeftWhitespaces(leftLimit, to);
    if (from >= 0) {
      String expectedSpaces = getIndentSpaces(indentLevel, addLineSeparator);
      createTextEditIfNeeded(from, to, expectedSpaces, edits);
      return expectedSpaces.length();
    }
    return 0;
  }

  public int replaceLeftSpacesWithIndentationWithMultiNewLines(
      int indentLevel, int leftLimit, int offset, int newLineCount, List<TextEdit> edits) {
    int from = adjustOffsetWithLeftWhitespaces(leftLimit, offset);
    if (from >= 0) {
      String expectedSpaces = getIndentSpacesWithMultiNewLines(indentLevel, newLineCount);
      createTextEditIfNeeded(from, offset, expectedSpaces, edits);
      return expectedSpaces.length();
    }
    return 0;
  }

  public int replaceLeftSpacesWithIndentationWithOffsetSpaces(
      int indentSpace, int leftLimit, int to, boolean addLineSeparator, List<TextEdit> edits) {
    int from = adjustOffsetWithLeftWhitespaces(leftLimit, to);
    if (from >= 0) {
      String expectedSpaces = getIndentSpacesWithOffsetSpaces(indentSpace, addLineSeparator);
      createTextEditIfNeeded(from, to, expectedSpaces, edits);
      return expectedSpaces.length();
    }
    return 0;
  }

  boolean hasLineBreak(int from, int to) {
    String text = textDocument.getText();
    for (int i = from; i < to; i++) {
      char c = text.charAt(i);
      if (isLineSeparator(c)) {
        return true;
      }
    }
    return false;
  }

  public int getNormalizedLength(int from, int to) {
    String text = textDocument.getText();
    int contentOffset = 0;
    for (int i = from; i < to; i++) {
      if (Character.isWhitespace(text.charAt(i)) && !Character.isWhitespace(text.charAt(i + 1))) {
        to -= contentOffset;
        contentOffset = 0;
      } else if (Character.isWhitespace(text.charAt(i))) {
        contentOffset++;
      }
    }
    return to;
  }

  public int getOffsetWithPreserveLineBreaks(
      int from, int to, int tabSize, boolean isInsertSpaces) {
    int initialTo = to;
    String text = textDocument.getText();
    for (int i = to; i > from; i--) {
      if (text.charAt(i) == '\t') {
        to -= tabSize;
      } else if (isLineSeparator(text.charAt(i))) {
        int prevIndent = 0;
        for (int j = i + 1; j < initialTo; j++) {
          if (text.charAt(j) == '\t' && !isInsertSpaces) {
            prevIndent += tabSize;
          } else if (Character.isWhitespace(text.charAt(j))) {
            prevIndent++;
          } else {
            to += (prevIndent - tabSize);
            return to;
          }
        }
      } else if (text.charAt(i) == ' ' && StringUtils.isQuote(text.charAt(i - 1))) {
        int j = 1;
        while (text.charAt(i + j) == ' ') {
          to++;
          j++;
        }
        to--;
      } else {
        to--;
      }
    }
    return to;
  }

  // DTD formatting

  // ------- Utilities method

  int updateLineWidthWithLastLine(DOMNode child, int availableLineWidth) {
    String text = textDocument.getText();
    int lineWidth = availableLineWidth;
    int end = child.getEnd();
    // Check if next char after the end of the DOM node is a new line feed.
    if (end < text.length()) {
      char c = text.charAt(end);
      if (isLineSeparator(c)) {
        // ex: <?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n
        return getMaxLineWidth();
      }
    }
    for (int i = end - 1; i > child.getStart(); i--) {
      char c = text.charAt(i);
      if (isLineSeparator(c)) {
        return lineWidth;
      } else {
        lineWidth--;
      }
    }
    return lineWidth;
  }

  private static boolean isLineSeparator(char c) {
    return c == '\r' || c == '\n';
  }

  public int getLineBreakOffset(int startAttr, int start) {
    String text = textDocument.getText();
    for (int i = startAttr; i < start; i++) {
      char c = text.charAt(i);
      if (isLineSeparator(c)) {
        return i;
      }
    }
    return -1;
  }

  void insertLineBreak(int start, int end, List<TextEdit> edits) {
    createTextEditIfNeeded(start, end, lineDelimiter, edits);
  }

  void replaceSpacesWithOneSpace(int spaceStart, int spaceEnd, List<TextEdit> edits) {
    if (spaceStart >= 0) {
      spaceEnd = spaceEnd == -1 ? spaceStart + 1 : spaceEnd + 1;
      // Replace several spaces with one space
      // <foo>a[space][space][space]b</foo>
      // --> <foo>a[space]b</foo>
      replaceLeftSpacesWithOneSpace(spaceStart, spaceEnd, edits);
    }
  }

  /**
   * Returns the format element category of the given DOM element.
   *
   * @param element           the DOM element.
   * @param parentConstraints the parent constraints.
   * @return the format element category of the given DOM element.
   */
  public FormatElementCategory getFormatElementCategory(
      DOMElement element, XMLFormattingConstraints parentConstraints) {
    if (!element.isClosed()) {
      return parentConstraints.getFormatElementCategory();
    }

    // Get the category from the settings
    FormatElementCategory fromSettings = getFormattingSettings().getFormatElementCategory(element);
    if (fromSettings != null) {
      return fromSettings;
    }

    if (XML_SPACE_ATTR_PRESERVE.equals(element.getAttribute(XML_SPACE_ATTR))) {
      return FormatElementCategory.PreserveSpace;
    }

    if (parentConstraints != null) {
      if (parentConstraints.getFormatElementCategory() == FormatElementCategory.PreserveSpace) {
        if (!XML_SPACE_ATTR_DEFAULT.equals(element.getAttribute(XML_SPACE_ATTR))) {
          return FormatElementCategory.PreserveSpace;
        }
      }
    }

    boolean hasElement = false;
    boolean hasText = false;
    boolean onlySpaces = true;
    for (DOMNode child : element.getChildren()) {
      if (child.isElement()) {
        hasElement = true;
      } else if (child.isText()) {
        onlySpaces = ((Text) child).isElementContentWhitespace();
        if (!onlySpaces) {
          hasText = true;
        }
      }
      if (hasElement && hasText) {
        return FormatElementCategory.MixedContent;
      }
    }
    if (hasElement && onlySpaces) {
      return FormatElementCategory.IgnoreSpace;
    }
    return FormatElementCategory.NormalizeSpace;
  }

  void createTextEditIfNeeded(int from, int to, String expectedContent, List<TextEdit> edits) {
    TextEdit edit = TextEditUtils.createTextEditIfNeeded(from, to, expectedContent, textDocument);
    if (edit != null) {
      edits.add(edit);
    }
  }

  public String getIndentSpaces(int level, boolean addLineSeparator) {
    StringBuilder spaces = new StringBuilder();
    if (addLineSeparator) {
      spaces.append(lineDelimiter);
    }

    for (int i = 0; i < level; i++) {
      if (isInsertSpaces()) {
        for (int j = 0; j < getTabSize(); j++) {
          spaces.append(" ");
        }
      } else {
        spaces.append("\t");
      }
    }
    return spaces.toString();
  }

  /**
   * Return the expected indent spaces and new lines with the specified number of new lines.
   *
   * @param level        the indent level.
   * @param newLineCount the number of new lines to be added.
   * @return the expected indent spaces and new lines with the specified number of new lines.
   */
  private String getIndentSpacesWithMultiNewLines(int level, int newLineCount) {
    StringBuilder spaces = new StringBuilder();
    while (newLineCount != 0) {
      spaces.append(lineDelimiter);
      newLineCount--;
    }

    for (int i = 0; i < level; i++) {
      if (isInsertSpaces()) {
        for (int j = 0; j < getTabSize(); j++) {
          spaces.append(" ");
        }
      } else {
        spaces.append("\t");
      }
    }
    return spaces.toString();
  }

  private String getIndentSpacesWithOffsetSpaces(int spaceCount, boolean addLineSeparator) {
    StringBuilder spaces = new StringBuilder();
    if (addLineSeparator) {
      spaces.append(lineDelimiter);
    }
    int spaceOffset = spaceCount % getTabSize();

    for (int i = 0; i < spaceCount / getTabSize(); i++) {
      if (isInsertSpaces()) {
        for (int j = 0; j < getTabSize(); j++) {
          spaces.append(" ");
        }
      } else {
        spaces.append("\t");
      }
    }

    for (int i = 0; i < spaceOffset; i++) {
      spaces.append(" ");
    }

    return spaces.toString();
  }

  private void trimFinalNewlines(boolean insertFinalNewline, List<TextEdit> edits) {
    String xml = textDocument.getText();
    int end = xml.length() - 1;
    int i = end;
    while (i >= 0 && isLineSeparator(xml.charAt(i))) {
      i--;
    }
    if (end > i) {
      if (insertFinalNewline) {
        // re-adjust offset to keep insert final new line
        i++;
        if (xml.charAt(end - 1) == '\r') {
          i++;
        }
      }
      if (end > i) {
        try {
          Position endPos = textDocument.positionAt(end + 1);
          Position startPos = textDocument.positionAt(i + 1);
          Range range = new Range(startPos, endPos);
          edits.add(new TextEdit(range, ""));
        } catch (BadLocationException e) {
          LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
      }
    }
  }

  int getMaxLineWidth() {
    return getFormattingSettings().getMaxLineWidth();
  }

  private int getTabSize() {
    return EditorPreferences.INSTANCE.getTabSize();
  }

  private boolean isInsertSpaces() {
    return EditorPreferences.INSTANCE.getUseSoftTab();
  }

  private boolean isTrimFinalNewlines() {
    return getFormattingSettings().isTrimFinalNewLine();
  }

  private boolean isInsertFinalNewline() {
    return getFormattingSettings().isInsertFinalNewLine();
  }

  private String getQuotationAsString() {
    return "\"";
  }

  private XMLFormattingOptions getFormattingSettings() {
    return getSharedSettings().getFormattingOptions();
  }

  XMLServerSettings getSharedSettings() {
    return sharedSettings;
  }

  String getLineDelimiter() {
    return lineDelimiter;
  }

  String getText() {
    return textDocument.getText();
  }

  public int getLineAtOffset(int offset) {
    try {
      return textDocument.lineOffsetAt(offset);
    } catch (BadLocationException e) {
      return -1;
    }
  }
}
