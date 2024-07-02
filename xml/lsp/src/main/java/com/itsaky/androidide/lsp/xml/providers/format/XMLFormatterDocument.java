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

/**
 * Copyright (c) 2022 Red Hat, Inc. and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * <p>SPDX-License-Identifier: EPL-2.0
 *
 * <p>Contributors: Red Hat Inc. - initial API and implementation
 */
package com.itsaky.androidide.lsp.xml.providers.format;

import com.itsaky.androidide.lsp.models.TextEdit;
import com.itsaky.androidide.lsp.xml.models.XMLServerSettings;
import com.itsaky.androidide.lsp.xml.utils.XMLBuilder;
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
import org.eclipse.lemminx.dom.DOMParser;
import org.eclipse.lemminx.dom.DOMProcessingInstruction;
import org.eclipse.lemminx.dom.DOMText;
import org.eclipse.lemminx.dom.DTDAttlistDecl;
import org.eclipse.lemminx.dom.DTDDeclNode;
import org.eclipse.lemminx.dom.DTDDeclParameter;
import org.eclipse.lemminx.dom.builder.EmptyElements;

import java.util.ArrayList;
import java.util.List;

/**
 * Default XML formatter which generates one text edit by rewriting the DOM node which must be
 * formatted.
 *
 * @author Angelo ZERR
 */
public class XMLFormatterDocument {

  private final TextDocument textDocument;
  private final Range range;
  private final XMLServerSettings sharedSettings = XMLServerSettings.INSTANCE;
  private final EmptyElements emptyElements;

  private int startOffset;
  private int endOffset;
  private DOMDocument fullDomDocument;
  private DOMDocument rangeDomDocument;
  private XMLBuilder xmlBuilder;
  private int indentLevel;
  private boolean linefeedOnNextWrite;
  private boolean withinDTDContent;

  /**
   * XML formatter document.
   */
  public XMLFormatterDocument(TextDocument textDocument, Range range) {
    this.textDocument = textDocument;
    this.range = range;
    this.emptyElements = sharedSettings.getFormattingOptions().getEmptyElementsBehavior();
    this.linefeedOnNextWrite = false;
  }

  /**
   * Returns a List containing a single TextEdit, containing the newly formatted changes of
   * this.textDocument
   *
   * @return List containing a single TextEdit
   * @throws BadLocationException
   */
  public List<? extends TextEdit> format() throws BadLocationException {
    this.fullDomDocument =
        DOMParser.getInstance().parse(textDocument.getText(), textDocument.getUri(), null, false);
    if (isRangeFormatting()) {
      setupRangeFormatting(range);
    } else {
      setupFullFormatting(range);
    }

    this.indentLevel = getStartingIndentLevel();
    format(this.rangeDomDocument);

    List<? extends TextEdit> textEdits = getFormatTextEdit();
    return textEdits;
  }

  private boolean isRangeFormatting() {
    return this.range != null;
  }

  private void setupRangeFormatting(Range range) throws BadLocationException {
    int startOffset = this.textDocument.offsetAt(range.getStart());
    int endOffset = this.textDocument.offsetAt(range.getEnd());

    Position startPosition = this.textDocument.positionAt(startOffset);
    Position endPosition = this.textDocument.positionAt(endOffset);
    enlargePositionToGutters(startPosition, endPosition);

    this.startOffset = this.textDocument.offsetAt(startPosition);
    this.endOffset = this.textDocument.offsetAt(endPosition);

    String fullText = this.textDocument.getText();
    String rangeText = fullText.substring(this.startOffset, this.endOffset);

    withinDTDContent = this.fullDomDocument.isWithinInternalDTD(startOffset);
    String uri = this.textDocument.getUri();
    if (withinDTDContent) {
      uri += ".dtd";
    }
    this.rangeDomDocument = DOMParser.getInstance().parse(rangeText, uri, null, false);

    if (containsTextWithinStartTag()) {
      adjustOffsetToStartTag();
      rangeText = fullText.substring(this.startOffset, this.endOffset);
      this.rangeDomDocument = DOMParser.getInstance().parse(rangeText, uri, null, false);
    }

    this.xmlBuilder = new XMLBuilder("", textDocument.lineDelimiter(startPosition.getLine()));
  }

  private boolean containsTextWithinStartTag() {

    if (this.rangeDomDocument.getChildren().size() < 1) {
      return false;
    }

    DOMNode firstChild = this.rangeDomDocument.getChild(0);
    if (!firstChild.isText()) {
      return false;
    }

    int tagContentOffset = firstChild.getStart();
    int fullDocOffset = getFullOffsetFromRangeOffset(tagContentOffset);
    DOMNode fullNode = this.fullDomDocument.findNodeAt(fullDocOffset);

    if (!fullNode.isElement()) {
      return false;
    }
    return ((DOMElement) fullNode).isInStartTag(fullDocOffset);
  }

  private void adjustOffsetToStartTag() throws BadLocationException {
    int tagContentOffset = this.rangeDomDocument.getChild(0).getStart();
    int fullDocOffset = getFullOffsetFromRangeOffset(tagContentOffset);
    DOMNode fullNode = this.fullDomDocument.findNodeAt(fullDocOffset);
    Position nodePosition = this.textDocument.positionAt(fullNode.getStart());
    nodePosition.setColumn(0);
    this.startOffset = this.textDocument.offsetAt(nodePosition);
  }

  private void setupFullFormatting(Range range) throws BadLocationException {
    this.startOffset = 0;
    this.endOffset = textDocument.getText().length();
    this.rangeDomDocument = this.fullDomDocument;

    Position startPosition = textDocument.positionAt(startOffset);
    this.xmlBuilder = new XMLBuilder("", textDocument.lineDelimiter(startPosition.getLine()));
  }

  private void enlargePositionToGutters(Position start, Position end) throws BadLocationException {
    start.setColumn(0);

    if (end.getColumn() == 0 && end.getLine() > 0) {
      end.setLine(end.getLine() - 1);
    }

    end.setColumn(this.textDocument.lineText(end.getLine()).length());
  }

  private int getStartingIndentLevel() throws BadLocationException {
    if (withinDTDContent) {
      return 1;
    }
    DOMNode startNode = this.fullDomDocument.findNodeAt(this.startOffset);
    if (startNode.isOwnerDocument()) {
      return 0;
    }

    DOMNode startNodeParent = startNode.getParentNode();

    if (startNodeParent.isOwnerDocument()) {
      return 0;
    }

    // the starting indent level is the parent's indent level + 1
    int startNodeIndentLevel = getNodeIndentLevel(startNodeParent) + 1;
    return startNodeIndentLevel;
  }

  private int getNodeIndentLevel(DOMNode node) throws BadLocationException {

    Position nodePosition = this.textDocument.positionAt(node.getStart());
    String textBeforeNode =
        this.textDocument
            .lineText(nodePosition.getLine())
            .substring(0, nodePosition.getColumn() + 1);

    int spaceOrTab = getSpaceOrTabStartOfString(textBeforeNode);

    if (EditorPreferences.INSTANCE.getUseSoftTab()) {
      return (spaceOrTab / EditorPreferences.INSTANCE.getTabSize());
    }
    return spaceOrTab;
  }

  private int getSpaceOrTabStartOfString(String string) {
    int i = 0;
    int spaceOrTab = 0;
    while (i < string.length() && (string.charAt(i) == ' ' || string.charAt(i) == '\t')) {
      spaceOrTab++;
      i++;
    }
    return spaceOrTab;
  }

  private DOMElement getFullDocElemFromRangeElem(DOMElement elemFromRangeDoc) {
    int fullOffset = -1;

    if (elemFromRangeDoc.hasStartTag()) {
      fullOffset = getFullOffsetFromRangeOffset(elemFromRangeDoc.getStartTagOpenOffset()) + 1;
      // +1 because offset must be here: <|root
      // for DOMNode.findNodeAt() to find the correct element
    } else if (elemFromRangeDoc.hasEndTag()) {
      fullOffset = getFullOffsetFromRangeOffset(elemFromRangeDoc.getEndTagOpenOffset()) + 1;
      // +1 because offset must be here: <|/root
      // for DOMNode.findNodeAt() to find the correct element
    } else {
      return null;
    }

    DOMElement elemFromFullDoc = (DOMElement) this.fullDomDocument.findNodeAt(fullOffset);
    return elemFromFullDoc;
  }

  private int getFullOffsetFromRangeOffset(int rangeOffset) {
    return rangeOffset + this.startOffset;
  }

  private boolean startTagExistsInRangeDocument(DOMNode node) {
    if (!node.isElement()) {
      return false;
    }

    return ((DOMElement) node).hasStartTag();
  }

  private boolean startTagExistsInFullDocument(DOMNode node) {
    if (!node.isElement()) {
      return false;
    }

    DOMElement elemFromFullDoc = getFullDocElemFromRangeElem((DOMElement) node);

    if (elemFromFullDoc == null) {
      return false;
    }

    return elemFromFullDoc.hasStartTag();
  }

  private void format(DOMNode node) throws BadLocationException {

    if (linefeedOnNextWrite && (!node.isText() || !((DOMText) node).isWhitespace())) {
      this.xmlBuilder.linefeed();
      linefeedOnNextWrite = false;
    }

    if (node.getNodeType() != DOMNode.DOCUMENT_NODE) {
      boolean doLineFeed =
          !node.getOwnerDocument().isDTD()
              && !(node.isComment() && ((DOMComment) node).isCommentSameLineEndTag())
              && (!node.isText()
              || (!((DOMText) node).isWhitespace() && ((DOMText) node).hasSiblings()));

      if (this.indentLevel > 0 && doLineFeed) {
        // add new line + indent
        if (!node.isChildOfOwnerDocument() || node.getPreviousNonTextSibling() != null) {
          this.xmlBuilder.linefeed();
        }

        if (!startTagExistsInRangeDocument(node) && startTagExistsInFullDocument(node)) {
          DOMNode startNode = getFullDocElemFromRangeElem((DOMElement) node);
          int currentIndentLevel = getNodeIndentLevel(startNode);
          this.xmlBuilder.indent(currentIndentLevel);
          this.indentLevel = currentIndentLevel;
        } else {
          this.xmlBuilder.indent(this.indentLevel);
        }
      }
      if (node.isElement()) {
        // Format Element
        formatElement((DOMElement) node);
      } else if (node.isCDATA()) {
        // Format CDATA
        formatCDATA((DOMCDATASection) node);
      } else if (node.isComment()) {
        // Format comment
        formatComment((DOMComment) node);
      } else if (node.isProcessingInstruction()) {
        // Format processing instruction
        formatProcessingInstruction(node);
      } else if (node.isProlog()) {
        // Format prolog
        formatProlog(node);
      } else if (node.isText()) {
        // Format Text
        formatText((DOMText) node);
      } else if (node.isDoctype()) {
        // Format document type
        formatDocumentType((DOMDocumentType) node);
      }
    } else if (node.hasChildNodes()) {
      // Other nodes kind like root
      for (DOMNode child : node.getChildren()) {
        format(child);
      }
    }
  }

  /**
   * Format the given DOM prolog
   *
   * @param node the DOM prolog to format.
   */
  private void formatProlog(DOMNode node) {
    addPrologToXMLBuilder(node, this.xmlBuilder);
    linefeedOnNextWrite = true;
  }

  /**
   * Format the given DOM text node.
   *
   * @param textNode the DOM text node to format.
   */
  private void formatText(DOMText textNode) {
    String content = textNode.getData();
    if (textNode.equals(this.fullDomDocument.getLastChild())) {
      xmlBuilder.addContent(content);
    } else {
      xmlBuilder.addContent(
          content, textNode.isWhitespace(), textNode.hasSiblings(), textNode.getDelimiter());
    }
  }

  /**
   * Format the given DOM document type.
   *
   * @param documentType the DOM document type to format.
   */
  private void formatDocumentType(DOMDocumentType documentType) {
    boolean isDTD = documentType.getOwnerDocument().isDTD();
    if (!isDTD) {
      this.xmlBuilder.startDoctype();
      List<DTDDeclParameter> params = documentType.getParameters();

      for (DTDDeclParameter param : params) {
        if (!documentType.isInternalSubset(param)) {
          xmlBuilder.addParameter(param.getParameter());
        } else {
          xmlBuilder.startDoctypeInternalSubset();
          xmlBuilder.linefeed();
          // level + 1 since the 'level' value is the doctype tag's level
          formatDTD(documentType, this.indentLevel + 1, this.endOffset, this.xmlBuilder);
          xmlBuilder.linefeed();
          xmlBuilder.endDoctypeInternalSubset();
        }
      }
      if (documentType.isClosed()) {
        xmlBuilder.endDoctype();
      }
      linefeedOnNextWrite = true;

    } else {
      formatDTD(documentType, this.indentLevel, this.endOffset, this.xmlBuilder);
    }
  }

  /**
   * Format the given DOM ProcessingIntsruction.
   */
  private void formatProcessingInstruction(DOMNode node) {
    addPIToXMLBuilder(node, this.xmlBuilder);
    if (this.indentLevel == 0) {
      this.xmlBuilder.linefeed();
    }
  }

  /**
   * Format the given DOM Comment
   */
  private void formatComment(DOMComment comment) {
    this.xmlBuilder.startComment(comment);
    this.xmlBuilder.addContentComment(comment.getData());
    if (comment.isClosed()) {
      // Generate --> only if comment is closed.
      this.xmlBuilder.endComment();
    }
    if (this.indentLevel == 0) {
      linefeedOnNextWrite = true;
    }
  }

  /**
   * Format the given DOM CDATA
   */
  private void formatCDATA(DOMCDATASection cdata) {
    this.xmlBuilder.startCDATA();
    this.xmlBuilder.addContentCDATA(cdata.getData());
    if (cdata.isClosed()) {
      // Generate ]> only if CDATA is closed.
      this.xmlBuilder.endCDATA();
    }
  }

  /**
   * Format the given DOM element
   *
   * @param element the DOM element to format.
   * @throws BadLocationException
   */
  private void formatElement(DOMElement element) throws BadLocationException {
    String tag = element.getTagName();
    if (element.hasEndTag() && !element.hasStartTag()) {
      // bad element without start tag (ex: <\root>)
      xmlBuilder.endElement(tag, element.isEndTagClosed());
    } else {
      // generate start element
      xmlBuilder.startElement(tag, false);
      if (element.hasAttributes()) {
        formatAttributes(element);
      }

      EmptyElements emptyElements = getEmptyElements(element);
      switch (emptyElements) {
        case Expand:
          // Expand empty element: <example /> -> <example></example>
          xmlBuilder.closeStartElement();
          // end tag element is done, only if the element is closed
          // the format, doesn't fix the close tag
          this.xmlBuilder.endElement(tag, true);
          break;
        case Collapse:
          // Collapse empty element: <example></example> -> <example />
          formatElementStartTagSelfCloseBracket(element);
          break;
        default:
          if (element.isStartTagClosed()) {
            formatElementStartTagCloseBracket(element);
          }
          boolean hasElements = false;
          if (element.hasChildNodes()) {
            // element has body

            this.indentLevel++;
            for (DOMNode child : element.getChildren()) {
              hasElements = hasElements || !child.isText();
              format(child);
            }
            this.indentLevel--;
          }
          if (element.hasEndTag()) {
            if (hasElements) {
              this.xmlBuilder.linefeed();
              this.xmlBuilder.indent(this.indentLevel);
            }
            // end tag element is done, only if the element is closed
            // the format, doesn't fix the close tag
            if (element.hasEndTag() && element.getEndTagOpenOffset() <= this.endOffset) {
              this.xmlBuilder.endElement(tag, element.isEndTagClosed());
            } else {
              formatElementStartTagSelfCloseBracket(element);
            }
          } else if (element.isSelfClosed()) {
            formatElementStartTagSelfCloseBracket(element);
          }
      }
    }
  }

  /**
   * Formats the start tag's closing bracket (>) according to
   * {@code XMLFormattingOptions#isPreserveAttrLineBreaks()}
   *
   * <p>{@code XMLFormattingOptions#isPreserveAttrLineBreaks()}: If true, must add a newline +
   * indent before the closing bracket if the last attribute of the element and the closing bracket
   * are in different lines.
   *
   * @param element
   * @throws BadLocationException
   */
  private void formatElementStartTagCloseBracket(DOMElement element) throws BadLocationException {
    if (this.sharedSettings.getFormattingOptions().isPreserveAttributeLineBreaks()
        && element.hasAttributes()
        && !isSameLine(getLastAttribute(element).getEnd(), element.getStartTagCloseOffset())) {
      xmlBuilder.linefeed();
      this.xmlBuilder.indent(this.indentLevel);
    }
    xmlBuilder.closeStartElement();
  }

  /**
   * Formats the self-closing tag (/>) according to
   * {@code XMLFormattingOptions#isPreserveAttrLineBreaks()}
   *
   * <p>{@code XMLFormattingOptions#isPreserveAttrLineBreaks()}: If true, must add a newline +
   * indent before the self-closing tag if the last attribute of the element and the closing bracket
   * are in different lines.
   *
   * @param element
   * @throws BadLocationException
   */
  private void formatElementStartTagSelfCloseBracket(DOMElement element)
      throws BadLocationException {
    if (this.sharedSettings.getFormattingOptions().isPreserveAttributeLineBreaks()
        && element.hasAttributes()) {
      int elementEndOffset = element.getEnd();
      if (element.isStartTagClosed()) {
        elementEndOffset = element.getStartTagCloseOffset();
      }
      if (!isSameLine(getLastAttribute(element).getEnd(), elementEndOffset)) {
        this.xmlBuilder.linefeed();
        this.xmlBuilder.indent(this.indentLevel);
      }
    }

    this.xmlBuilder.selfCloseElement();
  }

  private void formatAttributes(DOMElement element) throws BadLocationException {
    List<DOMAttr> attributes = element.getAttributeNodes();
    boolean isSingleAttribute = hasSingleAttributeInFullDoc(element);
    int prevOffset = element.getStart();
    for (DOMAttr attr : attributes) {
      formatAttribute(attr, isSingleAttribute, prevOffset);
      prevOffset = attr.getEnd();
    }
    if ((this.sharedSettings.getFormattingOptions().isClosingBracketNewLine()
        && this.sharedSettings.getFormattingOptions().isSplitAttributes())
        && !isSingleAttribute) {
      xmlBuilder.linefeed();
      // Indent by tag + splitAttributesIndentSize to match with attribute indent
      // level
      int totalIndent =
          this.indentLevel
              + this.sharedSettings.getFormattingOptions().getSplitAttributesIndentSize();
      xmlBuilder.indent(totalIndent);
    }
  }

  private void formatAttribute(DOMAttr attr, boolean isSingleAttribute, int prevOffset)
      throws BadLocationException {
    if (this.sharedSettings.getFormattingOptions().isPreserveAttributeLineBreaks()
        && !isSameLine(prevOffset, attr.getStart())) {
      xmlBuilder.linefeed();
      xmlBuilder.indent(this.indentLevel + 1);
      xmlBuilder.addSingleAttribute(attr, false, false);
    } else if (isSingleAttribute) {
      xmlBuilder.addSingleAttribute(attr);
    } else {
      xmlBuilder.addAttribute(attr, this.indentLevel);
    }
  }

  /**
   * Returns true if first offset and second offset belong in the same line of the document
   *
   * <p>If current formatting is range formatting, the provided offsets must be ranged offsets
   * (offsets relative to the formatting range)
   *
   * @param first  the first offset
   * @param second the second offset
   * @return true if first offset and second offset belong in the same line of the document
   * @throws BadLocationException
   */
  private boolean isSameLine(int first, int second) throws BadLocationException {
    if (isRangeFormatting()) {
      // adjust range offsets so that they are relative to the full document
      first = getFullOffsetFromRangeOffset(first);
      second = getFullOffsetFromRangeOffset(second);
    }
    return getLineNumber(first) == getLineNumber(second);
  }

  private int getLineNumber(int offset) throws BadLocationException {
    return this.textDocument.positionAt(offset).getLine();
  }

  private DOMAttr getLastAttribute(DOMElement element) {
    if (!element.hasAttributes()) {
      return null;
    }
    List<DOMAttr> attributes = element.getAttributeNodes();
    return attributes.get(attributes.size() - 1);
  }

  /**
   * Returns true if the provided element has one attribute in the fullDomDocument (not the
   * rangeDomDocument)
   *
   * @param element
   * @return true if the provided element has one attribute in the fullDomDocument (not the
   * rangeDomDocument)
   */
  private boolean hasSingleAttributeInFullDoc(DOMElement element) {
    DOMElement fullElement = getFullDocElemFromRangeElem(element);
    return fullElement.getAttributeNodes().size() == 1;
  }

  /**
   * Return the option to use to generate empty elements.
   *
   * @param element the DOM element
   * @return the option to use to generate empty elements.
   */
  private EmptyElements getEmptyElements(DOMElement element) {
    if (this.emptyElements != EmptyElements.Ignore) {
      if (element.isClosed() && element.isEmpty()) {
        // Element is empty and closed
        switch (this.emptyElements) {
          case Expand:
          case Collapse: {
            if (this.sharedSettings.getFormattingOptions().isPreserveEmptyContent()) {
              // preserve content
              if (element.hasChildNodes()) {
                // The element is empty and contains somes spaces which must be preserved
                return EmptyElements.Ignore;
              }
            }
            return this.emptyElements;
          }
          default:
            return this.emptyElements;
        }
      }
    }
    return EmptyElements.Ignore;
  }

  private static boolean formatDTD(
      DOMDocumentType doctype, int level, int end, XMLBuilder xmlBuilder) {
    DOMNode previous = null;
    for (DOMNode node : doctype.getChildren()) {
      if (previous != null) {
        xmlBuilder.linefeed();
      }

      xmlBuilder.indent(level);

      if (node.isText()) {
        xmlBuilder.addContent(((DOMText) node).getData().trim());
      } else if (node.isComment()) {
        DOMComment comment = (DOMComment) node;
        xmlBuilder.startComment(comment);
        xmlBuilder.addContentComment(comment.getData());
        xmlBuilder.endComment();
      } else if (node.isProcessingInstruction()) {
        addPIToXMLBuilder(node, xmlBuilder);
      } else if (node.isProlog()) {
        addPrologToXMLBuilder(node, xmlBuilder);
      } else {
        boolean setEndBracketOnNewLine = false;
        DTDDeclNode decl = (DTDDeclNode) node;
        xmlBuilder.addDeclTagStart(decl);

        if (decl.isDTDAttListDecl()) {
          DTDAttlistDecl attlist = (DTDAttlistDecl) decl;
          List<DTDAttlistDecl> internalDecls = attlist.getInternalChildren();

          if (internalDecls == null) {
            for (DTDDeclParameter param : decl.getParameters()) {
              xmlBuilder.addParameter(param.getParameter());
            }
          } else {
            boolean multipleInternalAttlistDecls = false;
            List<DTDDeclParameter> params = attlist.getParameters();
            DTDDeclParameter param;
            for (int i = 0; i < params.size(); i++) {
              param = params.get(i);
              if (attlist.getNameParameter().equals(param)) {
                xmlBuilder.addParameter(param.getParameter());
                if (attlist.getParameters().size() > 1) { // has parameters after elementName
                  xmlBuilder.linefeed();
                  xmlBuilder.indent(level + 1);
                  setEndBracketOnNewLine = true;
                  multipleInternalAttlistDecls = true;
                }
              } else {
                if (multipleInternalAttlistDecls && i == 1) {
                  xmlBuilder.addUnindentedParameter(param.getParameter());
                } else {
                  xmlBuilder.addParameter(param.getParameter());
                }
              }
            }

            for (DTDAttlistDecl attlistDecl : internalDecls) {
              xmlBuilder.linefeed();
              xmlBuilder.indent(level + 1);
              params = attlistDecl.getParameters();
              for (int i = 0; i < params.size(); i++) {
                param = params.get(i);

                if (i == 0) {
                  xmlBuilder.addUnindentedParameter(param.getParameter());
                } else {
                  xmlBuilder.addParameter(param.getParameter());
                }
              }
            }
          }
        } else {
          for (DTDDeclParameter param : decl.getParameters()) {
            xmlBuilder.addParameter(param.getParameter());
          }
        }
        if (setEndBracketOnNewLine) {
          xmlBuilder.linefeed();
          xmlBuilder.indent(level);
        }
        if (decl.isClosed()) {
          xmlBuilder.closeStartElement();
        }
      }
      previous = node;
    }
    return true;
  }

  private List<? extends TextEdit> getFormatTextEdit() throws BadLocationException {
    Position startPosition = this.textDocument.positionAt(this.startOffset);
    Position endPosition = this.textDocument.positionAt(this.endOffset);
    Range r = new Range(startPosition, endPosition);
    List<TextEdit> edits = new ArrayList<>();

    // check if format range reaches the end of the document
    if (this.endOffset == this.textDocument.getText().length()) {

      if (this.sharedSettings.getFormattingOptions().isTrimFinalNewLine()) {
        this.xmlBuilder.trimFinalNewlines();
      }

      if (this.sharedSettings.getFormattingOptions().isInsertFinalNewLine()
          && !this.xmlBuilder.isLastLineEmptyOrWhitespace()) {
        this.xmlBuilder.linefeed();
      }
    }

    edits.add(new TextEdit(r, this.xmlBuilder.toString()));
    return edits;
  }

  private static void addPIToXMLBuilder(DOMNode node, XMLBuilder xml) {
    DOMProcessingInstruction processingInstruction = (DOMProcessingInstruction) node;
    xml.startPrologOrPI(processingInstruction.getTarget());

    String content = processingInstruction.getData();
    if (content.length() > 0) {
      xml.addContentPI(content);
    } else {
      xml.addContent(" ");
    }

    xml.endPrologOrPI();
  }

  private static void addPrologToXMLBuilder(DOMNode node, XMLBuilder xml) {
    DOMProcessingInstruction processingInstruction = (DOMProcessingInstruction) node;
    xml.startPrologOrPI(processingInstruction.getTarget());
    if (node.hasAttributes()) {
      addPrologAttributes(node, xml);
    }
    xml.endPrologOrPI();
  }

  /**
   * Will add all attributes, to the given builder, on a single line
   */
  private static void addPrologAttributes(DOMNode node, XMLBuilder xmlBuilder) {
    List<DOMAttr> attrs = node.getAttributeNodes();
    if (attrs == null) {
      return;
    }
    for (DOMAttr attr : attrs) {
      xmlBuilder.addPrologAttribute(attr);
    }
  }
}
