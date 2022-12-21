/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * This class is a copy/paste of org.eclipse.jface.text.ListLineTracker
 *******************************************************************************/
package org.eclipse.lemminx.commons;

import com.itsaky.androidide.models.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract, read-only implementation of <code>ILineTracker</code>. It lets the definition of line
 * delimiters to subclasses. Assuming that '\n' is the only line delimiter, this abstract
 * implementation defines the following line scheme:
 *
 * <ul>
 *   <li>"" -> [0,0]
 *   <li>"a" -> [0,1]
 *   <li>"\n" -> [0,1], [1,0]
 *   <li>"a\n" -> [0,2], [2,0]
 *   <li>"a\nb" -> [0,2], [2,1]
 *   <li>"a\nbc\n" -> [0,2], [2,3], [5,0]
 * </ul>
 *
 * This class must be subclassed.
 *
 * @since 3.2
 */
class ListLineTracker implements ILineTracker {

  /** The predefined delimiters of this tracker */
  public static final String[] DELIMITERS = {
    "\r", "\n", "\r\n"
  }; //$NON-NLS-3$ //$NON-NLS-1$ //$NON-NLS-2$
  /** A predefined delimiter information which is always reused as return value */
  private DelimiterInfo fDelimiterInfo = new DelimiterInfo();

  /** The line information */
  private final List<Line> fLines = new ArrayList<>();
  /** The length of the tracked text */
  private int fTextLength;

  /**
   * Combines the information of the occurrence of a line delimiter. <code>delimiterIndex</code> is
   * the index where a line delimiter starts, whereas <code>delimiterLength</code>, indicates the
   * length of the delimiter.
   */
  protected static class DelimiterInfo {
    public int delimiterIndex;
    public int delimiterLength;
    public String delimiter;
  }

  /** Creates a new line tracker. */
  protected ListLineTracker() {}

  /**
   * Binary search for the line at a given offset.
   *
   * @param offset the offset whose line should be found
   * @return the line of the offset
   */
  private int findLine(int offset) {

    if (fLines.size() == 0) {
      return -1;
    }
    int left = 0;
    int right = fLines.size();
    while (left < right - 1) {
      int mid = (right + left) >> 1;
      Line line = fLines.get(mid);
      if (line.offset <= offset) {
        left = mid;
      } else {
        right = mid;
      }
    }
    return (fLines.get(left).offset > offset) ? left - 1 : left;
  }

  @Override
  public final Position getPositionAt(int offset) throws BadLocationException {
    int lineNumber = getLineNumberOfOffset(offset);
    int lines = fLines.size();
    int character = 0;
    if (lines > 0) {
      if (lineNumber == lines) {
        Line l = fLines.get(lineNumber - 1);
        character = offset - l.offset - l.length;
      } else {
        Line l = fLines.get(lineNumber);
        character = offset - l.offset;
      }
    }
    return new Position(lineNumber, character);
  }

  /**
   * Returns the number of lines covered by the specified text range.
   *
   * @param startLine the line where the text range starts
   * @param offset the start offset of the text range
   * @param length the length of the text range
   * @return the number of lines covered by this text range
   * @exception BadLocationException if range is undefined in this tracker
   */
  private int getNumberOfLines(int startLine, int offset, int length) throws BadLocationException {

    if (length == 0) {
      return 1;
    }

    int target = offset + length;

    Line l = fLines.get(startLine);

    if (l.delimiter == null) {
      return 1;
    }

    if (l.offset + l.length > target) {
      return 1;
    }

    if (l.offset + l.length == target) {
      return 2;
    }

    return getLineNumberOfOffset(target) - startLine + 1;
  }

  @Override
  public final int getLineLength(int line) throws BadLocationException {
    int lines = fLines.size();

    if (line < 0 || line > lines) {
      throw new BadLocationException();
    }

    if (lines == 0 || lines == line) {
      return 0;
    }

    Line l = fLines.get(line);
    return l.length;
  }

  @Override
  public final int getLineNumberOfOffset(int position) throws BadLocationException {
    if (position < 0) {
      throw new BadLocationException("Negative offset : " + position); // $NON-NLS-1$
    } else if (position > fTextLength) {
      throw new BadLocationException(
          "Offset > length: " + position + " > " + fTextLength); // $NON-NLS-1$//$NON-NLS-2$
    }

    if (position == fTextLength) {

      int lastLine = fLines.size() - 1;
      if (lastLine < 0) {
        return 0;
      }

      Line l = fLines.get(lastLine);
      return (l.delimiter != null ? lastLine + 1 : lastLine);
    }

    return findLine(position);
  }

  @Override
  public int getOffsetAt(Position position) throws BadLocationException {
    int line = position.getLine();
    int lines = fLines.size();

    if (line < 0 || line > lines) {
      throw new BadLocationException("The line value, {" + line + "}, is out of bounds.");
    }

    int lineOffset = -1;
    int lineLength = -1;
    if (lines == 0) {
      lineOffset = 0;
      lineLength = 0;
    } else {
      if (line == lines) {
        Line l = fLines.get(line - 1);
        lineOffset = l.offset + l.length;
        lineLength = 0;
      } else {
        Line l = fLines.get(line);
        lineOffset = l.offset;
        lineLength = l.delimiter != null ? l.length - l.delimiter.length() : l.length;
      }
    }
    int character = position.getColumn();
    int offset = lineOffset + character;
    int endLineOffset = lineOffset + lineLength;
    if (offset > endLineOffset) {
      throw new BadLocationException(
          "The character value, {" + character + "} of the line" + line + "}, is out of bounds.");
    }
    return offset;
  }

  @Override
  public final Line getLineInformationOfOffset(int position) throws BadLocationException {
    if (position > fTextLength) {
      throw new BadLocationException(
          "Offset > length: " + position + " > " + fTextLength); // $NON-NLS-1$//$NON-NLS-2$
    }

    if (position == fTextLength) {
      int size = fLines.size();
      if (size == 0) {
        return new Line(0, 0);
      }
      Line l = fLines.get(size - 1);

      return (l.delimiter != null
          ? new Line(fTextLength, 0)
          : new Line(fTextLength - l.length, l.length));
    }

    return getLineInformation(findLine(position));
  }

  @Override
  public final Line getLineInformation(int line) throws BadLocationException {
    int lines = fLines.size();

    if (line < 0 || line > lines) {
      throw new BadLocationException("The line value, {" + line + "}, is out of bounds.");
    }

    if (lines == 0) {
      return new Line(0, 0);
    }

    if (line == lines) {
      Line l = fLines.get(line - 1);
      return new Line(l.offset + l.length, 0);
    }

    Line l = fLines.get(line);
    return (l.delimiter != null ? new Line(l.offset, l.length - l.delimiter.length()) : l);
  }

  @Override
  public final int getLineOffset(int line) throws BadLocationException {
    int lines = fLines.size();

    if (line < 0 || line > lines) {
      throw new BadLocationException("The line value, {" + line + "}, is out of bounds.");
    }

    if (lines == 0) {
      return 0;
    }

    if (line == lines) {
      Line l = fLines.get(line - 1);
      if (l.delimiter != null) {
        return l.offset + l.length;
      }
      throw new BadLocationException();
    }

    Line l = fLines.get(line);
    return l.offset;
  }

  @Override
  public final int getNumberOfLines() {
    int lines = fLines.size();

    if (lines == 0) {
      return 1;
    }

    Line l = fLines.get(lines - 1);
    return (l.delimiter != null ? lines + 1 : lines);
  }

  @Override
  public final int getNumberOfLines(int position, int length) throws BadLocationException {

    if (position < 0 || position + length > fTextLength) {
      throw new BadLocationException();
    }

    if (length == 0) {
      return 1;
    }

    return getNumberOfLines(getLineNumberOfOffset(position), position, length);
  }

  @Override
  public final int computeNumberOfLines(String text) {
    int count = 0;
    int start = 0;
    DelimiterInfo delimiterInfo = nextDelimiterInfo(text, start);
    while (delimiterInfo != null && delimiterInfo.delimiterIndex > -1) {
      ++count;
      start = delimiterInfo.delimiterIndex + delimiterInfo.delimiterLength;
      delimiterInfo = nextDelimiterInfo(text, start);
    }
    return count;
  }

  public final String getLineDelimiter(int line) throws BadLocationException {
    int lines = fLines.size();

    if (line < 0 || line > lines) {
      throw new BadLocationException("The line value, {" + line + "}, is out of bounds.");
    }

    if (lines == 0) {
      return null;
    }

    if (line == lines) {
      return null;
    }

    Line l = fLines.get(line);
    return l.delimiter;
  }

  /**
   * Returns the information about the first delimiter found in the given text starting at the given
   * offset.
   *
   * @param text the text to be searched
   * @param offset the offset in the given text
   * @return the information of the first found delimiter or <code>null</code>
   */
  protected DelimiterInfo nextDelimiterInfo(String text, int offset) {
    char ch;
    int length = text.length();
    for (int i = offset; i < length; i++) {

      ch = text.charAt(i);
      if (ch == '\r') {

        if (i + 1 < length) {
          if (text.charAt(i + 1) == '\n') {
            fDelimiterInfo.delimiter = DELIMITERS[2];
            fDelimiterInfo.delimiterIndex = i;
            fDelimiterInfo.delimiterLength = 2;
            return fDelimiterInfo;
          }
        }

        fDelimiterInfo.delimiter = DELIMITERS[0];
        fDelimiterInfo.delimiterIndex = i;
        fDelimiterInfo.delimiterLength = 1;
        return fDelimiterInfo;

      } else if (ch == '\n') {

        fDelimiterInfo.delimiter = DELIMITERS[1];
        fDelimiterInfo.delimiterIndex = i;
        fDelimiterInfo.delimiterLength = 1;
        return fDelimiterInfo;
      }
    }

    return null;
  }

  /**
   * Creates the line structure for the given text. Newly created lines are inserted into the line
   * structure starting at the given position. Returns the number of newly created lines.
   *
   * @param text the text for which to create a line structure
   * @param insertPosition the position at which the newly created lines are inserted into the
   *     tracker's line structure
   * @param offset the offset of all newly created lines
   * @return the number of newly created lines
   */
  private int createLines(String text, int insertPosition, int offset) {

    int count = 0;
    int start = 0;
    DelimiterInfo delimiterInfo = nextDelimiterInfo(text, 0);

    while (delimiterInfo != null && delimiterInfo.delimiterIndex > -1) {

      int index = delimiterInfo.delimiterIndex + (delimiterInfo.delimiterLength - 1);

      if (insertPosition + count >= fLines.size()) {
        fLines.add(new Line(offset + start, offset + index, delimiterInfo.delimiter));
      } else {
        fLines.add(
            insertPosition + count,
            new Line(offset + start, offset + index, delimiterInfo.delimiter));
      }

      ++count;
      start = index + 1;
      delimiterInfo = nextDelimiterInfo(text, start);
    }

    if (start < text.length()) {
      if (insertPosition + count < fLines.size()) {
        // there is a line below the current
        Line l = fLines.get(insertPosition + count);
        int delta = text.length() - start;
        l.offset -= delta;
        l.length += delta;
      } else {
        fLines.add(new Line(offset + start, offset + text.length() - 1, null));
        ++count;
      }
    }

    return count;
  }

  @Override
  public final void replace(int position, int length, String text) throws BadLocationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public final void set(String text) {
    fLines.clear();
    if (text != null) {
      fTextLength = text.length();
      createLines(text, 0, 0);
    }
  }

  /**
   * Returns the internal data structure, a {@link List} of {@link Line}s.
   *
   * @return the internal list of lines.
   */
  final List<Line> getLines() {
    return fLines;
  }
}
