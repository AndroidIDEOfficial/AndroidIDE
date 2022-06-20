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

package com.itsaky.androidide.utils;

import com.itsaky.androidide.models.LogLine;

/**
 * {@link ILogger} implementation for the JVM.
 *
 * @author Akash Yadav
 */
public class JvmLogger extends ILogger {

  public static LogInterceptor interceptor;

  protected JvmLogger(String tag) {
    super(tag);
  }

  @Override
  protected void doLog(Priority priority, String message) {
    final var log = new LogLine(priority, TAG, message);
    if (interceptor != null) {
      interceptor.onLog(log);
    } else {
      System.err.println(log.toSimpleString());
    }
  }

  public interface LogInterceptor {
    void onLog(LogLine line);
  }
}
