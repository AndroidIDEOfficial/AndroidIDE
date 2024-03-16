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

import java.util.Arrays;
import org.slf4j.LoggerFactory;

/**
 * Static methods for logging messages.
 *
 * @author Akash Yadav
 */
public class Logger {

  private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Logger.class);

  public static void error(Object... messages) {
    if (messages == null || messages.length == 0) {
      return;
    }
    if (messages.length == 1) {
      LOG.error(String.valueOf(messages[0]));
      return;
    }

    if (messages.length == 2) {
      LOG.error(String.valueOf(messages[0]), messages[1]);
      return;
    }

    LOG.error(String.valueOf(messages[0]), Arrays.copyOfRange(messages, 1, messages.length));
  }

  public static void warn(Object... messages) {
    if (messages == null || messages.length == 0) {
      return;
    }
    if (messages.length == 1) {
      LOG.warn(String.valueOf(messages[0]));
      return;
    }

    if (messages.length == 2) {
      LOG.warn(String.valueOf(messages[0]), messages[1]);
      return;
    }

    LOG.warn(String.valueOf(messages[0]), Arrays.copyOfRange(messages, 1, messages.length));
  }

  public static void info(Object... messages) {
    if (messages == null || messages.length == 0) {
      return;
    }
    if (messages.length == 1) {
      LOG.info(String.valueOf(messages[0]));
      return;
    }

    if (messages.length == 2) {
      LOG.info(String.valueOf(messages[0]), messages[1]);
      return;
    }

    LOG.info(String.valueOf(messages[0]), Arrays.copyOfRange(messages, 1, messages.length));
  }

  public static void debug(Object... messages) {
    if (messages == null || messages.length == 0) {
      return;
    }
    if (messages.length == 1) {
      LOG.debug(String.valueOf(messages[0]));
      return;
    }

    if (messages.length == 2) {
      LOG.debug(String.valueOf(messages[0]), messages[1]);
      return;
    }

    LOG.debug(String.valueOf(messages[0]), Arrays.copyOfRange(messages, 1, messages.length));
  }
}
