/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
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
 **************************************************************************************/

package com.itsaky.androidide.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.utils.ILogger;

public class LogReceiver extends BroadcastReceiver {

  public static final String APPEND_LOG = "com.itsaky.androidide.logs.APPEND_LOG";
  public static final String EXTRA_LINE = "log_line";
  private final ILogger LOG = ILogger.newInstance("LogReceiver");
  private LogListener listener;

  public LogReceiver setLogListener(LogListener listener) {
    this.listener = listener;
    return this;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals(APPEND_LOG) && intent.hasExtra(EXTRA_LINE)) {
      String line = intent.getStringExtra(EXTRA_LINE);
      if (line == null) return;
      try {
        sendLogLine(line);
      } catch (Throwable th) {
        LOG.error("Unable to parse log line from app.", th);
      }
    }
  }

  private void sendLogLine(String line) {
    final var log = LogLine.forLogString(line);
    if (listener != null) {
      listener.appendLogLine(log);
    }
  }

  public interface LogListener {
    void appendLogLine(LogLine line);
  }
}
