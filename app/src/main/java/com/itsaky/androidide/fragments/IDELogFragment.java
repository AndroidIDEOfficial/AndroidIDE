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

package com.itsaky.androidide.fragments;

import androidx.annotation.NonNull;

import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.utils.ILogger;

public class IDELogFragment extends LogViewFragment {

  private final ILogger.LogListener listener = this::log;

  public IDELogFragment() {
    ILogger.addLogListener(listener);
  }

  @Override
  protected String onCreateLogString(@NonNull LogLine line) {
    return line.toSimpleString();
  }

  private void log(int priority, String tag, @NonNull String message) {
    if (message.contains("\n")) {
      final var split = message.split("\n");
      for (var line : split) {
        logLine(priority, tag, line);
      }
    } else {
      logLine(priority, tag, message);
    }
  }

  private void logLine(int priority, String tag, String message) {
    final var line = new LogLine(ILogger.priorityChar(priority), tag, message);
    appendLog(line);
  }
}
