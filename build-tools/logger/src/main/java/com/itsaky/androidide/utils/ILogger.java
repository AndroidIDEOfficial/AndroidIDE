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
package com.itsaky.androidide.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Logger for the IDE.
 *
 * <p>If a {@link Throwable} is passed to any of the logging methods, whole stack trace of the
 * throwable is printed. Any modifications to this class will affect every log message in the IDE.
 *
 * @author Akash Yadav
 */
public abstract class ILogger {

  public static final int DEBUG = 0;
  public static final int WARNING = 1;
  public static final int ERROR = 2;
  public static final int INFO = 3;
  public static final int VERBOSE = 4;
  private static final String MSG_SEPARATOR = " "; // Separate messages with a space.
  private static final List<LogListener> logListeners = new ArrayList<>();
  private static ILogger instance;
  protected final String TAG;

  protected ILogger(String tag) {
    TAG = tag;
  }

  public static ILogger instance() {
    return instance == null ? instance = createInstance("AndroidIDE") : instance;
  }

  private static ILogger createInstance(String tag) {
    return newPlatformDependentLogger(tag);
  }

  private static ILogger newPlatformDependentLogger(String tag) {
    return LogUtils.isJvm() ? new JvmLogger(tag) : new AndroidLogger(tag);
  }

  public static void addLogListener(LogListener listener) {
    logListeners.add(Objects.requireNonNull(listener));
  }

  public static void removeLogListener(LogListener listener) {
    logListeners.remove(Objects.requireNonNull(listener));
  }

  public static ILogger newInstance(String tag) {
    return createInstance(tag);
  }

  public static String priorityText(int priority) {
    switch (priority) {
      case DEBUG:
        return "DEBUG";
      case INFO:
        return "INFO";
      case VERBOSE:
        return "VERBOSE";
      case ERROR:
        return "ERROR";
      case WARNING:
        return "WARNING";
      default:
        return "<UNKNOWN>";
    }
  }

  public static char priorityChar(int priority) {
    switch (priority) {
      case INFO:
        return 'I';
      case VERBOSE:
        return 'V';
      case ERROR:
        return 'E';
      case WARNING:
        return 'W';
      default:
        return 'D';
    }
  }

  public static int priority(char priorityChar) {
    switch (priorityChar) {
      case 'I':
        return INFO;
      case 'V':
        return VERBOSE;
      case 'E':
        return ERROR;
      case 'W':
        return WARNING;
      case 'D':
      default:
        return DEBUG;
    }
  }

  /**
   * Log error messages.
   *
   * @param messages The messages to log.
   * @return This logger instance.
   */
  public ILogger error(Object... messages) {
    return log(ERROR, messages);
  }

  /**
   * Log messages with the given priority.
   *
   * @param priority The priority of the log messages.
   * @param messages The messages to log.
   * @return This logger instance.
   */
  public ILogger log(int priority, Object... messages) {
    logAndNotify(priority, generateMessage(messages));
    return this;
  }

  private void logAndNotify(int priority, String msg) {
    doLog(priority, msg);
    for (final var listener : logListeners) {
      listener.log(priority, TAG, msg);
    }
  }

  protected String generateMessage(Object... messages) {
    StringBuilder sb = new StringBuilder();
    if (messages == null) {
      return "null";
    }

    for (Object msg : messages) {
      sb.append(msg instanceof Throwable ? "\n" : MSG_SEPARATOR);
      sb.append(msg instanceof Throwable ? LogUtils.getFullStackTrace(((Throwable) msg)) : msg);
      sb.append(msg instanceof Throwable ? "\n" : MSG_SEPARATOR);
    }

    return sb.toString();
  }

  /**
   * Log the message to an appropriate stream where the user can see the log messages.
   *
   * @param priority The priority for this log message.
   * @param message The full generated message for this log. Might contain new lines.
   * @see ILogger#DEBUG
   * @see ILogger#ERROR
   * @see ILogger#WARNING
   * @see ILogger#VERBOSE
   * @see ILogger#INFO
   */
  protected abstract void doLog(int priority, String message);

  /**
   * Log warning messages.
   *
   * @param messages The messages to log.
   * @return This logger instance.
   */
  public ILogger warn(Object... messages) {
    return log(WARNING, messages);
  }

  /**
   * Log verbose messages.
   *
   * @param messages The messages to log.
   * @return This logger instance.
   */
  public ILogger verbose(Object... messages) {
    return log(VERBOSE, messages);
  }

  /**
   * Log information messages.
   *
   * @param messages The messages to log.
   * @return This logger instance.
   */
  public ILogger info(Object... messages) {
    return log(INFO, messages);
  }

  /** Logs the name of method and class which calls this method. */
  public void logThis() {
    debug(getCallerClassDescription());
  }

  /**
   * Log debug messages.
   *
   * @param messages The messages to log.
   * @return This logger instance.
   */
  public ILogger debug(Object... messages) {
    return log(DEBUG, messages);
  }

  protected String getCallerClassDescription() {
    final var elements = Thread.currentThread().getStackTrace();
    for (int i = 1, elementsLength = elements.length; i < elementsLength; i++) {
      final var element = elements[i];
      final var klass = element.getClassName();
      final var method = element.getMethodName();
      if (ILogger.class.getName().equals(klass) || klass.contains("java.lang.Thread")) {
        continue;
      }

      return String.format("%s [%s]", method, klass);
    }

    return "<Logger> <Cannot get caller information>";
  }

  /** A listener which can be used to listen to log events. */
  public interface LogListener {
    void log(int priority, String tag, String message);
  }
}
