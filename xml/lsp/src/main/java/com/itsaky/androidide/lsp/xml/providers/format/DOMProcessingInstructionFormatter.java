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
import org.eclipse.lemminx.dom.DOMProcessingInstruction;

import java.util.List;

/**
 * DOM processing instruction formatter.
 *
 * @author Angelo ZERR
 */
public class DOMProcessingInstructionFormatter {

  private final XMLFormatterDocumentNew formatterDocument;

  private final DOMAttributeFormatter attributeFormatter;

  public DOMProcessingInstructionFormatter(
      XMLFormatterDocumentNew formatterDocument, DOMAttributeFormatter attributeFormatter) {
    this.formatterDocument = formatterDocument;
    this.attributeFormatter = attributeFormatter;
  }

  public void formatProcessingInstruction(
      DOMProcessingInstruction processingInstruction,
      XMLFormattingConstraints parentConstraints,
      List<TextEdit> edits) {
    int prevOffset = processingInstruction.getStartContent();
    // 1. format attributes : attributes must be in a same line separate with only
    // one space
    if (processingInstruction.hasAttributes()) {
      // --- <?xml version = \"1.0\" encoding = \"UTF-8\"?>
      // --> <?xml version=\"1.0\" encoding=\"UTF-8\"?>
      List<DOMAttr> attributes = processingInstruction.getAttributeNodes();
      boolean singleAttribute = attributes.size() == 1;
      for (DOMAttr attr : attributes) {
        attributeFormatter.formatAttribute(
            attr, prevOffset, singleAttribute, false, parentConstraints, edits);
        prevOffset = attr.getEnd();
      }
    }
    // 2. format end of processing instruction : remove extra space between the last
    // attribute value and the end of processing instruction
    // --- <?xml version=\"1.0\" encoding=\"UTF-8\" ?>
    // --> <?xml version=\"1.0\" encoding=\"UTF-8\"?>
    if (processingInstruction.isClosed()) {
      // it ends with ?>
      int endPIOffset = processingInstruction.getEnd() - 2;
      if (prevOffset != endPIOffset) {
        replaceLeftSpacesWith(prevOffset, endPIOffset, "", edits);
      }
    }
  }

  private void replaceLeftSpacesWith(
      int leftLimit, int to, String replacement, List<TextEdit> edits) {
    formatterDocument.replaceLeftSpacesWith(leftLimit, to, replacement, edits);
  }
}
