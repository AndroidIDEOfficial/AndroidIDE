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

package com.itsaky.androidide.lexers.log;

import java.util.Objects;

/**
 * Tokens in a log line.
 *
 * @author Akash Yadav
 */
public class LogToken {

  public static final int UNKNOWN = -1;
  public static final int WS = 0;
  public static final int DATE = 1;
  public static final int TIME = 2;
  public static final int PID = 3;
  public static final int TID = 4;
  public static final int TAG = 5;
  public static final int PRIORITY = 6;
  public static final int MESSAGE = 7;

  /** Text of this token. */
  public final String text;

  public final int startIndex;
  public final int endIndex;
  public final int type;

  public LogToken(final String text, final int startIndex, final int endIndex, final int type) {
    this.text = text;
    this.startIndex = startIndex;
    this.endIndex = endIndex;
    this.type = type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, startIndex, endIndex, type);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LogToken)) {
      return false;
    }
    final LogToken logToken = (LogToken) o;
    return startIndex == logToken.startIndex
        && endIndex == logToken.endIndex
        && type == logToken.type
        && Objects.equals(text, logToken.text);
  }

  @Override
  public String toString() {
    return "LogToken{"
        + "text='"
        + text
        + '\''
        + ", startIndex="
        + startIndex
        + ", endIndex="
        + endIndex
        + ", type="
        + type
        + '}';
  }
}
