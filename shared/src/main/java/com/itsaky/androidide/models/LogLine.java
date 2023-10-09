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
package com.itsaky.androidide.models;

import static com.itsaky.androidide.utils.LogTagUtils.trimTagIfNeeded;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.ILogger.Priority;
import com.itsaky.androidide.utils.RecyclableObjectPool;
import com.itsaky.androidide.utils.RecyclableObjectPool.Recyclable;
import java.util.Objects;

public class LogLine implements Recyclable {

  private static final RecyclableObjectPool<LogLine> logLinePool = new RecyclableObjectPool<>(
      RecyclableObjectPool.CAPACITY_DEFAULT, LogLine.class, LogLine::new);

  public String unformatted;
  public String date;
  public String time;
  public String pid;
  public String tid;
  public String tag;
  public String message;
  public ILogger.Priority priority;
  public boolean formatted;

  public static LogLine obtain(Priority priority, String tag, String message) {
    return obtain(priority, tag, message, true);
  }

  public static LogLine obtain(Priority priority, String tag, String message, boolean formatted) {
    final var logLine = logLinePool.obtain();
    logLine.priority = priority;
    logLine.tag = tag;
    logLine.message = message;
    logLine.formatted = formatted;
    return logLine;
  }

  public static LogLine forLogString(final String log) {
    if (log == null) {
      return null;
    }
    final var logLine = logLinePool.obtain();
    try {
      final var split = log.split("\\s", 7);
      logLine.priority = ILogger.priority(Character.toUpperCase(split[4].charAt(0)));
      logLine.date = split[0];
      logLine.time = split[1]; // time
      logLine.pid = split[2]; // process id
      logLine.tid = split[3]; // thread id
      logLine.tag = split[5]; // tag
      logLine.message = split[6]; // message
      logLine.formatted = true;
    } catch (Throwable th) { // do not log the exception with ILogger
      logLine.unformatted = log;
      logLine.formatted = false;
    }

    return logLine;
  }

  // For JSONRpc and Recyclable
  protected LogLine() {
    resetToDefault();
  }

  public String toSimpleString() {
    return this.formatted
        ? String.format(
        "%-25s %-2s %s", trimTagIfNeeded(tag, 25), ILogger.priorityChar(priority), message)
        : this.unformatted;
  }


  public String formattedTagAndMessage() {
    return this.formatted
        ? String.format("%-25s %-2s", trimTagIfNeeded(tag, 25), message)
        : this.unformatted;
  }

  protected void resetToDefault() {
    unformatted = date = time = pid = tid = tag = message = null;
    priority = Priority.DEBUG;
    formatted = false;
  }

  @Override
  public void recycle() {
    resetToDefault();
    logLinePool.recycle(this);
  }

  @Override
  public int hashCode() {
    return Objects.hash(unformatted, date, time, pid, tid, tag, message, priority, formatted);
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
  public String toString() {
    return this.formatted
        ? String.format(
        "%s %s %s %s %-2s %-25s %s",
        date, time, pid, tid, ILogger.priorityChar(priority), trimTagIfNeeded(tag, 25), message)
        : this.unformatted;
  }
}
