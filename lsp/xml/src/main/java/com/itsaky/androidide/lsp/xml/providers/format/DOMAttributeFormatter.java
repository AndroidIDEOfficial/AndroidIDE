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

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.utils.StringUtils;

import java.util.List;

/**
 * DOM attribute formatter.
 *
 * @author Angelo ZERR
 */
public class DOMAttributeFormatter {

  private final XMLFormatterDocumentNew formatterDocument;

  public DOMAttributeFormatter(XMLFormatterDocumentNew formatterDocument) {
    this.formatterDocument = formatterDocument;
  }

  public void formatAttribute(
      DOMAttr attr,
      int prevOffset,
      boolean singleAttribute,
      boolean useSettings,
      XMLFormattingConstraints parentConstraints,
      List<TextEdit> edits) {
    // 1) format before attribute name : indent left of the attribute name
    // ex : <foo[space][space]attr=""
    // --> <foo[space]attr=""
    boolean alreadyIndented = false;
    if (useSettings) {
      int indentLevel = parentConstraints.getIndentLevel();
      if (isPreserveAttributeLineBreaks() && hasLineBreak(prevOffset, attr.getStart())) {
        replaceLeftSpacesWithIndentation(indentLevel + 1, prevOffset, attr.getStart(), true, edits);
        alreadyIndented = true;
      } else if (isSplitAttributes() && !singleAttribute) {
        // move the attribute to a new line and indent it.
        replaceLeftSpacesWithIndentation(
            indentLevel + getSplitAttributesIndentSize(), prevOffset, attr.getStart(), true, edits);
        alreadyIndented = true;
      }
    }
    if (!alreadyIndented) {
      // // remove extra whitespaces between previous attribute
      // attr0='name'[space][space][space]attr1='name' -->
      // attr0='name'[space]attr1='name'

      // Adjust the startAttr to avoid ignoring invalid content
      // ex : <asdf |""`=asdf />
      // must be adjusted with <asdf ""|`=asdf /> to keep the invalid content ""
      int from = prevOffset;
      int to = attr.getStart();
      replaceLeftSpacesWithOneSpace(from, to, edits);
    }

    // 2) format delimiter : remove whitespaces between '='
    // ex : <foo attr = ""
    // --> <foo attr=""
    if (attr.hasDelimiter()) {
      int delimiterOffset = attr.getDelimiterOffset(); // <foo attr =| ""

      // 2.1 Remove extra spaces between end of attribute name and delimiter
      int attrNameEnd = attr.getNodeAttrName().getEnd(); // <foo attr| = ""
      removeLeftSpaces(attrNameEnd, delimiterOffset, edits);

      if (attr.getNodeAttrValue() != null) {
        // 2.2 Remove extra spaces between delimiter and start of attribute value
        int attrValueStart = attr.getNodeAttrValue().getStart(); // <foo attr = |""
        removeLeftSpaces(delimiterOffset, attrValueStart, edits);
      }
      formatAttributeValue(attr, parentConstraints.getIndentLevel(), edits);
    }

    // replace current quote with preferred quote in case of attribute value
    // ex: if preferred quote is single quote (')
    // <a name="value"> </a>
    // --> <a name='value'> </a>
    String originalValue = attr.getOriginalValue();
    if (originalValue != null) {
      if (originalValue.charAt(0) != getQuotationAsChar()
          && StringUtils.isQuote(originalValue.charAt(0))) {
        replaceQuoteWithPreferred(
            attr.getNodeAttrValue().getStart(), attr.getNodeAttrValue().getStart() + 1, edits);
      }
      if (originalValue.charAt(originalValue.length() - 1) != getQuotationAsChar()
          && StringUtils.isQuote(originalValue.charAt(originalValue.length() - 1))) {
        replaceQuoteWithPreferred(
            attr.getNodeAttrValue().getEnd() - 1, attr.getNodeAttrValue().getEnd(), edits);
      }
    }
  }

  private void formatAttributeValue(DOMAttr attr, int indentLevel, List<TextEdit> edits) {
    formatterDocument.formatAttributeValue(attr, indentLevel, edits);
  }

  private void replaceQuoteWithPreferred(int from, int to, List<TextEdit> edits) {
    formatterDocument.replaceQuoteWithPreferred(from, to, edits);
  }

  private void replaceLeftSpacesWithOneSpace(int from, int to, List<TextEdit> edits) {
    formatterDocument.replaceLeftSpacesWithOneSpace(from, to, edits);
  }

  private void replaceLeftSpacesWithIndentation(
      int indentLevel, int leftLimit, int to, boolean addLineSeparator, List<TextEdit> edits) {
    formatterDocument.replaceLeftSpacesWithIndentation(
        indentLevel, leftLimit, to, addLineSeparator, edits);
  }

  private void removeLeftSpaces(int from, int to, List<TextEdit> edits) {
    formatterDocument.removeLeftSpaces(from, to, edits);
  }

  private boolean isSplitAttributes() {
    return formatterDocument.getSharedSettings().getFormattingOptions().isSplitAttributes();
  }

  private int getSplitAttributesIndentSize() {
    return formatterDocument
        .getSharedSettings()
        .getFormattingOptions()
        .getSplitAttributesIndentSize();
  }

  boolean isPreserveAttributeLineBreaks() {
    return formatterDocument
        .getSharedSettings()
        .getFormattingOptions()
        .isPreserveAttributeLineBreaks();
  }

  private boolean hasLineBreak(int prevOffset, int start) {
    return formatterDocument.hasLineBreak(prevOffset, start);
  }

  private char getQuotationAsChar() {
    return '"';
  }
}
