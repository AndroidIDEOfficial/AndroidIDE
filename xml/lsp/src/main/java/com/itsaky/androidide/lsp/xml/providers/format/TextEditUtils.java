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
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.TextDocument;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Utilities for {@link TextEdit}.
 *
 * @author Angelo ZERR
 */
public class TextEditUtils {

  private static final Logger LOGGER = Logger.getLogger(TextEditUtils.class.getName());

  /**
   * Returns the {@link TextEdit} to insert the given expected content from the given range (from,
   * to) of the given text document and null otherwise.
   *
   * @param from the range from.
   * @param to the range to.
   * @param expectedContent the expected content.
   * @param textDocument the text document.
   * @return the {@link TextEdit} to insert the given expected content from the given range (from,
   *     to) of the given text document and null otherwise.
   */
  public static TextEdit createTextEditIfNeeded(
      int from, int to, String expectedContent, TextDocument textDocument) {
    String text = textDocument.getText();

    // Check if content from the range [from, to] is the same than expected content
    if (isMatchExpectedContent(from, to, expectedContent, text)) {
      // The expected content exists, no need to create a TextEdit
      return null;
    }

    // Insert the expected content.
    try {
      Position endPos = textDocument.positionAt(to);
      Position startPos = to == from ? endPos : textDocument.positionAt(from);
      Range range = new Range(startPos, endPos);
      return new TextEdit(range, expectedContent);
    } catch (BadLocationException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    return null;
  }

  /**
   * Returns true if the given content from the range [from, to] of the given text is the same than
   * expected content and false otherwise.
   *
   * @param from the from range.
   * @param to the to range.
   * @param expectedContent the expected content.
   * @param text the text document.
   * @return true if the given content from the range [from, to] of the given text is the same than
   *     expected content and false otherwise.
   */
  private static boolean isMatchExpectedContent(
      int from, int to, String expectedContent, String text) {
    if (expectedContent.length() == to - from) {
      int j = 0;
      for (int i = from; i < to; i++) {
        char c = text.charAt(i);
        if (expectedContent.charAt(j) != c) {
          return false;
        }
        j++;
      }
    } else {
      return false;
    }
    return true;
  }

  public static String applyEdits(TextDocument document, List<? extends TextEdit> edits)
      throws BadLocationException {
    String text = document.getText();
    List<? extends TextEdit> sortedEdits =
        mergeSort(
            edits /* .map(getWellformedEdit) */,
            (a, b) -> {
              int diff = a.getRange().getStart().getLine() - b.getRange().getStart().getLine();
              if (diff == 0) {
                return a.getRange().getStart().getColumn()
                    - b.getRange().getStart().getColumn();
              }
              return diff;
            });
    int lastModifiedOffset = 0;
    List<String> spans = new ArrayList<>();
    for (TextEdit e : sortedEdits) {
      int startOffset = document.offsetAt(e.getRange().getStart());
      if (startOffset < lastModifiedOffset) {
        throw new Error("Overlapping edit");
      } else if (startOffset > lastModifiedOffset) {
        spans.add(text.substring(lastModifiedOffset, startOffset));
      }
      if (e.getNewText() != null) {
        spans.add(e.getNewText());
      }
      lastModifiedOffset = document.offsetAt(e.getRange().getEnd());
    }
    spans.add(text.substring(lastModifiedOffset));
    return spans.stream() //
        .collect(Collectors.joining());
  }

  private static <T> List<T> mergeSort(List<T> data, Comparator<T> comparator) {
    if (data.size() <= 1) {
      // sorted
      return data;
    }
    int p = (data.size() / 2) | 0;
    List<T> left = data.subList(0, p);
    List<T> right = data.subList(p, data.size());

    mergeSort(left, comparator);
    mergeSort(right, comparator);

    int leftIdx = 0;
    int rightIdx = 0;
    int i = 0;
    while (leftIdx < left.size() && rightIdx < right.size()) {
      int ret = comparator.compare(left.get(leftIdx), right.get(rightIdx));
      if (ret <= 0) {
        // smaller_equal -> take left to preserve order
        data.set(i++, left.get(leftIdx++));
      } else {
        // greater -> take right
        data.set(i++, right.get(rightIdx++));
      }
    }
    while (leftIdx < left.size()) {
      data.set(i++, left.get(leftIdx++));
    }
    while (rightIdx < right.size()) {
      data.set(i++, right.get(rightIdx++));
    }
    return data;
  }

  /**
   * Returns the offset of the first whitespace that's found in the given range [leftLimit,to] from
   * the left of the to, and leftLimit otherwise.
   *
   * @param leftLimit the left limit range.
   * @param to the to range.
   * @return the offset of the first whitespace that's found in the given range [leftLimit,to] from
   *     the left of the to, and leftLimit otherwise.
   */
  public static int adjustOffsetWithLeftWhitespaces(int leftLimit, int to, String text) {
    if (to == 0) {
      return -1;
    }
    for (int i = to - 1; i >= leftLimit; i--) {
      char c = text.charAt(i);
      if (!Character.isWhitespace(c)) {
        // The current character is not a whitespace, return the offset of the character
        return i + 1;
      }
    }
    return leftLimit;
  }
}
