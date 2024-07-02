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

package com.itsaky.androidide.lsp.xml.providers.format;

import com.itsaky.androidide.lsp.models.TextEdit;
import com.itsaky.androidide.models.Range;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * XML formatter support.
 */
public class XMLFormatter {

  private static final Logger LOG = LoggerFactory.getLogger(XMLFormatter.class);

  /**
   * Returns a List containing a single TextEdit, containing the newly formatted changes of the
   * document.
   *
   * @param range specified range in which formatting will be done
   * @return List containing a TextEdit with formatting changes
   */
  public List<? extends TextEdit> format(DOMDocument xmlDocument, Range range) {
    try {
      XMLFormatterDocument formatterDocument =
          new XMLFormatterDocument(xmlDocument.getTextDocument(), range);
      return formatterDocument.format();
    } catch (BadLocationException e) {
      LOG.error("Formatting failed due to BadLocation", e);
    }
    return null;
  }
}
