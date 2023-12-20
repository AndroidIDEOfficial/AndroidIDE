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

package com.itsaky.androidide.logsender.utils;

import com.itsaky.androidide.utils.ILogger;

/**
 * Static methods for logging messages.
 *
 * @author Akash Yadav
 */
public class Logger {
  public static final ILogger LOG = ILogger.newInstance("LogSender");

  public static void error(Object... messages) {
    LOG.error(messages);
  }

  public static void warn(Object... messages) {
    LOG.warn(messages);
  }

  public static void info(Object... messages) {
    LOG.info(messages);
  }

  public static void debug(Object... messages) {
    LOG.debug(messages);
  }
}
