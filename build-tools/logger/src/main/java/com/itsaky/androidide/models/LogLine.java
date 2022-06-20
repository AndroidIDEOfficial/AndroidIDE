/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.models;

import com.itsaky.androidide.utils.ILogger;

import java.util.Objects;

public class LogLine {

  public String unformatted;
  public String date;
  public String time;
  public String pid;
  public String tid;
  public String tag;
  public String message;
  public ILogger.Priority priority;
  public boolean formatted;

  // For JSONRpc
  @SuppressWarnings("unused")
  protected LogLine() {}

  public LogLine(ILogger.Priority priority, String tag, String message) {
    this(priority, "", "", "", "", tag, message);
  }

  public LogLine(
      ILogger.Priority priority,
      String date,
      String time,
      String pid,
      String tid,
      String tag,
      String message) {
    this(priority, date, time, pid, tid, tag, message, true);
  }

  public LogLine(
      ILogger.Priority priority,
      String date,
      String time,
      String pid,
      String tid,
      String tag,
      String message,
      boolean formatted) {
    this.date = date;
    this.time = time;
    this.pid = pid;
    this.tid = tid;
    this.tag = tag;
    this.message = message;
    this.priority = priority;
    this.formatted = formatted;
  }

  public LogLine(String unformatted) {
    this.unformatted = unformatted;
    this.formatted = false;
  }

  private static ILogger.Priority parsePriority(char s) {
    return ILogger.priority(Character.toUpperCase(s));
  }

  public static LogLine forLogString(final String log) {
    try {
      final var split = log.split("\\s", 7);
      return new LogLine(
          parsePriority(split[4].charAt(0)),
          split[0], // date
          split[1], // time
          split[2], // process id
          split[3], // thread id
          // priority
          split[5], // tag
          split[6] // message
          );
    } catch (Throwable th) {
      return new LogLine(log);
    }
  }

  @Override
  public String toString() {
    return this.formatted
        ? String.format(
            "%s %s %s/%s %s/%s %s",
            date, time, pid, tid, ILogger.priorityChar(priority), tag, message)
        : this.unformatted;
  }

  private String trimIfNeeded(String tag, int maxLength) {
    final var sb = new StringBuilder(tag);
    final var length = tag.length();
    if (length > maxLength) {
      final var start = length - maxLength;
      sb.delete(0, start);

      // When the tag is long enough, prefix the tag with '..'
      sb.setCharAt(0, '.');
      sb.setCharAt(1, '.');
    }

    return sb.toString();
  }

  public String toSimpleString() {
    return this.formatted
        ? String.format(
            "%-25s %-2s %s", trimIfNeeded(tag, 25), ILogger.priorityChar(priority), message)
        : this.unformatted;
  }

  public String formattedTagAndMessage() {
    return this.formatted
        ? String.format("%-25s %s", trimIfNeeded(tag, 25), message)
        : this.unformatted;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final LogLine logLine = (LogLine) o;
    return formatted == logLine.formatted
        && Objects.equals(unformatted, logLine.unformatted)
        && Objects.equals(date, logLine.date)
        && Objects.equals(time, logLine.time)
        && Objects.equals(pid, logLine.pid)
        && Objects.equals(tid, logLine.tid)
        && Objects.equals(tag, logLine.tag)
        && Objects.equals(message, logLine.message)
        && priority == logLine.priority;
  }

  @Override
  public int hashCode() {
    return Objects.hash(unformatted, date, time, pid, tid, tag, message, priority, formatted);
  }
}
