/**
 * Copyright (c) 2018 Angelo ZERR. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v20.html
 *
 * <p>SPDX-License-Identifier: EPL-2.0
 *
 * <p>Contributors: Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.commons;

import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextDocument {

  private static String DEFAULT_DELIMTER = System.lineSeparator();

  private ILineTracker lineTracker;

  private String text;
  private String uri;

  public TextDocument(String text, String uri) {
    setUri(uri);
    setText(text);
  }

  public void setUri(final String uri) {
    this.uri = uri;
  }

  public String getUri() {
    return uri;
  }

  public void setText(final String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public Position positionAt(int position) throws BadLocationException {
    ILineTracker lineTracker = getLineTracker();
    return lineTracker.getPositionAt(position);
  }

  public int offsetAt(Position position) throws BadLocationException {
    ILineTracker lineTracker = getLineTracker();
    return lineTracker.getOffsetAt(position);
  }

  public String lineText(int lineNumber) throws BadLocationException {
    ILineTracker lineTracker = getLineTracker();
    Line line = lineTracker.getLineInformation(lineNumber);
    String text = getText();
    return text.substring(line.offset, line.offset + line.length);
  }

  public int lineOffsetAt(int position) throws BadLocationException {
    ILineTracker lineTracker = getLineTracker();
    Line line = lineTracker.getLineInformationOfOffset(position);
    return line.offset;
  }

  public String lineDelimiter(int lineNumber) throws BadLocationException {
    ILineTracker lineTracker = getLineTracker();
    String lineDelimiter = lineTracker.getLineDelimiter(lineNumber);
    if (lineDelimiter == null) {
      if (lineTracker.getNumberOfLines() > 0) {
        lineDelimiter = lineTracker.getLineInformation(0).delimiter;
      }
    }
    if (lineDelimiter == null) {
      lineDelimiter = DEFAULT_DELIMTER;
    }
    return lineDelimiter;
  }

  public Range getWordRangeAt(int textOffset, Pattern wordDefinition) {
    try {
      Position pos = positionAt(textOffset);
      ILineTracker lineTracker = getLineTracker();
      Line line = lineTracker.getLineInformation(pos.getLine());
      String text = getText();
      String lineText = text.substring(line.offset, textOffset);
      int position = lineText.length();
      Matcher m = wordDefinition.matcher(lineText);
      int currentPosition = 0;
      while (currentPosition != position) {
        if (m.find()) {
          currentPosition = m.end();
          if (currentPosition == position) {
            return new Range(new Position(pos.getLine(), m.start()), pos);
          }
        } else {
          currentPosition++;
        }
        m.region(currentPosition, position);
      }
      return new Range(pos, pos);
    } catch (BadLocationException e) {
      return null;
    }
  }

  private ILineTracker getLineTracker() {
    if (lineTracker == null) {
      lineTracker = createLineTracker();
    }
    return lineTracker;
  }

  private synchronized ILineTracker createLineTracker() {
    if (lineTracker != null) {
      return lineTracker;
    }
    ILineTracker lineTracker = new ListLineTracker();
    lineTracker.set(getText());
    return lineTracker;
  }
}
