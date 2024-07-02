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

import org.eclipse.lemminx.dom.DOMDocumentType;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DTDAttlistDecl;
import org.eclipse.lemminx.dom.DTDDeclNode;
import org.eclipse.lemminx.dom.DTDDeclParameter;
import org.eclipse.lemminx.utils.StringUtils;
import org.w3c.dom.Node;

import java.util.List;

/**
 * DOM docType formatter.
 *
 * @author Angelo ZERR
 */
public class DOMDocTypeFormatter {

  private final XMLFormatterDocumentNew formatterDocument;

  public DOMDocTypeFormatter(XMLFormatterDocumentNew formatterDocument) {
    this.formatterDocument = formatterDocument;
  }

  public void formatDocType(
      DOMDocumentType docType,
      XMLFormattingConstraints parentConstraints,
      int start,
      int end,
      List<TextEdit> edits) {
    boolean isDTD = docType.getOwnerDocument().isDTD();
    if (isDTD) {
      formatDTD(docType, parentConstraints, start, end, edits);
    } else {
      List<DTDDeclParameter> parameters = docType.getParameters();
      if (!parameters.isEmpty()) {
        for (DTDDeclParameter parameter : parameters) {
          replaceLeftSpacesWithOneSpace(docType.getStart(), parameter.getStart(), edits);
          if (docType.isInternalSubset(parameter)) {
            // level + 1 since the 'level' value is the doctype tag's level
            XMLFormattingConstraints constraints = new XMLFormattingConstraints();
            constraints.copyConstraints(parentConstraints);
            constraints.setIndentLevel(constraints.getIndentLevel() + 1);
            formatDTD(docType, constraints, start, end, edits);
          }
        }
        int quoteStart = getDocTypeIdStart(docType);
        int quoteEnd = getDocTypeIdEnd(docType);

        if (quoteStart != -1 && quoteEnd != -1) {
          // replace current quote with preferred quote in the case:
          // <!DOCTYPE note SYSTEM "note.dtd">
          formatterDocument.replaceQuoteWithPreferred(quoteStart, quoteStart + 1, edits);
          formatterDocument.replaceQuoteWithPreferred(quoteEnd - 1, quoteEnd, edits);
        }
      }
    }
    DTDDeclParameter internalSubset = docType.getInternalSubsetNode();
    if (internalSubset == null) {
      if (docType.isClosed()) {
        // Remove space between content and end bracket in case of no internal subset
        // Example: <!DOCTYPE note SYSTEM "note.dtd"|>
        int startDocType = docType.getStart();
        int endDocType = docType.getEnd() - 1;
        removeLeftSpaces(startDocType, endDocType, edits);
      }
    } else {
      // Add new line at end of internal subset
      // <!DOCTYPE person [...
      // <!ENTITY AUTHOR \"John Doe\">|]>
      int startDocType = internalSubset.getStart();
      int endDocType = internalSubset.getEnd() - 1;
      String lineDelimiter = formatterDocument.getLineDelimiter();
      replaceLeftSpacesWith(startDocType, endDocType, lineDelimiter, edits);
    }
  }

  private void formatDTD(
      DOMDocumentType docType,
      XMLFormattingConstraints parentConstraints,
      int start,
      int end,
      List<TextEdit> edits) {
    boolean addLineSeparator = !docType.getOwnerDocument().isDTD();
    for (DOMNode child : docType.getChildren()) {
      switch (child.getNodeType()) {
        case DOMNode.DTD_ELEMENT_DECL_NODE:
        case DOMNode.DTD_ATT_LIST_NODE:
        case Node.ENTITY_NODE:
        case DOMNode.DTD_NOTATION_DECL:
          // Format DTD node declaration, for example:
          // <!ENTITY AUTHOR "John Doe">
          DTDDeclNode nodeDecl = (DTDDeclNode) child;
          formatDTDNodeDecl(nodeDecl, parentConstraints, addLineSeparator, edits);
          addLineSeparator = true;
          break;

        default:
          // unknown, so just leave alone for now but make sure to update
          // available line width
          int width = updateLineWidthWithLastLine(child, parentConstraints.getAvailableLineWidth());
          parentConstraints.setAvailableLineWidth(width);
      }
    }
  }

  private int updateLineWidthWithLastLine(DOMNode child, int availableLineWidth) {
    return formatterDocument.updateLineWidthWithLastLine(child, availableLineWidth);
  }

  private void formatDTDNodeDecl(
      DTDDeclNode nodeDecl,
      XMLFormattingConstraints parentConstraints,
      boolean addLineSeparator,
      List<TextEdit> edits) {
    // 1) indent the DTD element, entity, notation declaration
    // before formatting : [space][space]<!ELEMENT>
    // after formatting : <!ELEMENT>
    replaceLeftSpacesWithIndentation(
        parentConstraints.getIndentLevel(),
        nodeDecl.getParentNode().getStart(),
        nodeDecl.getStart(),
        addLineSeparator,
        edits);

    // 2 separate each parameters with one space
    // before formatting : <!ELEMENT[space][space]note>
    // after formatting : <!ELEMENT[space]note>
    DTDAttlistDecl attlist = nodeDecl.isDTDAttListDecl() ? (DTDAttlistDecl) nodeDecl : null;
    if (attlist != null) {
      int indentLevel = nodeDecl.getOwnerDocument().isDTD() ? 1 : 2;
      List<DTDAttlistDecl> internalDecls = attlist.getInternalChildren();
      if (internalDecls == null) {
        int previousOffset = attlist.getStart();
        for (DTDDeclParameter parameter : attlist.getParameters()) {
          // Normalize space at the start of parameter to a single space for ATTLIST, for
          // example:
          // <!ATTLIST |E |WIDTH |CDATA |"0">
          replaceLeftSpacesWithOneSpace(previousOffset, parameter.getStart(), edits);
          // replace current quote with preferred quote in the case:
          // <!ATTLIST E WIDTH CDATA "0">
          replaceQuoteWithPreferred(nodeDecl, parameter, edits);
          previousOffset = parameter.getEnd();
        }
      } else {
        boolean multipleInternalAttlistDecls = false;
        List<DTDDeclParameter> params = attlist.getParameters();
        DTDDeclParameter parameter;
        int previousOffset = attlist.getStart();
        for (int i = 0; i < params.size(); i++) {
          parameter = params.get(i);
          if (attlist.getNameParameter().equals(parameter)) {
            replaceLeftSpacesWithOneSpace(previousOffset, parameter.getStart(), edits);
            if (attlist.getParameters().size() > 1) { // has parameters after elementName
              multipleInternalAttlistDecls = true;
            }
          } else {
            if (multipleInternalAttlistDecls && i == 1) {
              replaceLeftSpacesWithIndentation(
                  indentLevel, previousOffset, parameter.getStart(), true, edits);
            } else {
              replaceLeftSpacesWithOneSpace(previousOffset, parameter.getStart(), edits);
            }
          }
          previousOffset = parameter.getEnd();
        }

        for (DTDAttlistDecl attlistDecl : internalDecls) {
          params = attlistDecl.getParameters();
          previousOffset = attlistDecl.getStart();
          for (int i = 0; i < params.size(); i++) {
            parameter = params.get(i);
            if (i == 0) {
              replaceLeftSpacesWithIndentation(
                  indentLevel, previousOffset, parameter.getStart(), true, edits);
            } else {
              replaceLeftSpacesWithOneSpace(previousOffset, parameter.getStart(), edits);
            }
            previousOffset = parameter.getEnd();
          }
        }
      }
    } else {
      List<DTDDeclParameter> parameters = nodeDecl.getParameters();
      if (!parameters.isEmpty()) {
        int previousOffset = nodeDecl.getStart();
        for (DTDDeclParameter parameter : parameters) {
          // Normalize space at the start of parameter to a single space for non-ATTLIST,
          // for example:
          // <!ENTITY |AUTHOR |"John Doe">
          replaceLeftSpacesWithOneSpace(previousOffset, parameter.getStart(), edits);
          // replace current quote with preferred quote in the case:
          // <!ENTITY AUTHOR "John Doe">
          replaceQuoteWithPreferred(nodeDecl, parameter, edits);
          previousOffset = parameter.getEnd();
        }
      }
    }
  }

  private void replaceLeftSpacesWith(int from, int to, String replacement, List<TextEdit> edits) {
    formatterDocument.replaceLeftSpacesWith(from, to, replacement, edits);
  }

  private void replaceLeftSpacesWithOneSpace(int from, int to, List<TextEdit> edits) {
    formatterDocument.replaceLeftSpacesWithOneSpace(from, to, edits);
  }

  private int replaceLeftSpacesWithIndentation(
      int indentLevel, int from, int to, boolean addLineSeparator, List<TextEdit> edits) {
    return formatterDocument.replaceLeftSpacesWithIndentation(
        indentLevel, from, to, addLineSeparator, edits);
  }

  private void removeLeftSpaces(int from, int to, List<TextEdit> edits) {
    formatterDocument.removeLeftSpaces(from, to, edits);
  }

  private static int getDocTypeIdStart(DOMDocumentType docType) {
    if (docType.getPublicIdNode() != null) {
      return docType.getPublicIdNode().getStart();
    } else if (docType.getSystemIdNode() != null) {
      return docType.getSystemIdNode().getStart();
    } else return -1;
  }

  private static int getDocTypeIdEnd(DOMDocumentType docType) {
    if (docType.getPublicIdNode() != null) {
      return docType.getPublicIdNode().getEnd();
    } else if (docType.getSystemIdNode() != null) {
      return docType.getSystemIdNode().getEnd();
    } else return -1;
  }

  private void replaceQuoteWithPreferred(
      DTDDeclNode nodeDecl, DTDDeclParameter parameter, List<TextEdit> edits) {
    int paramStart = parameter.getStart();
    int paramEnd = parameter.getEnd();
    if (StringUtils.isQuote(nodeDecl.getOwnerDocument().getText().charAt(paramStart))
        && StringUtils.isQuote(nodeDecl.getOwnerDocument().getText().charAt(paramEnd - 1))) {
      formatterDocument.replaceQuoteWithPreferred(paramStart, paramStart + 1, edits);
      formatterDocument.replaceQuoteWithPreferred(paramEnd - 1, paramEnd, edits);
    }
  }
}
