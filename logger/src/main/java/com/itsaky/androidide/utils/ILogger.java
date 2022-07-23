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

import static com.itsaky.androidide.utils.LogUtils.preProcessLogTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * Logger for the IDE.
 *
 * <p>If a {@link Throwable} is passed to any of the logging methods, whole stack trace of the
 * throwable is printed. Any modifications to this class will affect every log message in the IDE.
 *
 * @author Akash Yadav
 */
public abstract class ILogger {

  public static final String DEFAULT_TAG = "AndroidIDE";
  public static final String MSG_SEPARATOR = " "; // Separate messages with a space.

  private static final List<LogListener> logListeners = new ArrayList<>();
  private static final Map<String, ILogger> cachedLoggers = new WeakHashMap<>();

  private static ILogger instance;
  protected final String TAG;

  protected ILogger(String tag) {
    TAG = preProcessLogTag(tag);
  }

  public static ILogger instance() {
    return cachedLoggers.computeIfAbsent(DEFAULT_TAG, ILogger::createInstance);
  }

  private static ILogger createInstance(String tag) {
    return cachedLoggers.computeIfAbsent(tag, ILogger::newPlatformDependentLogger);
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

  public static Priority priority(char priorityChar) {
    for (var priority : Priority.values()) {
      if (priorityChar(priority) == priorityChar) {
        return priority;
      }
    }
    throw new IllegalArgumentException("Invalid priority character: " + priorityChar);
  }

  public static char priorityChar(Priority priority) {
    return Character.toUpperCase(priorityText(priority).charAt(0));
  }

  public static String priorityText(Priority priority) {
    return priority.name();
  }

  /**
   * Log error messages.
   *
   * @param messages The messages to log.
   * @return This logger instance.
   */
  public ILogger error(Object... messages) {
    return log(Priority.ERROR, messages);
  }

  /**
   * Log messages with the given priority.
   *
   * @param priority The priority of the log messages.
   * @param messages The messages to log.
   * @return This logger instance.
   */
  public ILogger log(Priority priority, Object... messages) {
    logAndNotify(priority, generateMessage(messages));
    return this;
  }

  private void logAndNotify(Priority priority, String msg) {
    doLog(priority, msg);
    for (final var listener : logListeners) {
      listener.log(priority, TAG, msg);
    }
  }

  /**
   * Log the message to an appropriate stream where the user can see the log messages.
   *
   * @param priority The priority for this log message.
   * @param message The full generated message for this log. Might contain new lines.
   * @see ILogger.Priority#DEBUG
   * @see ILogger.Priority#ERROR
   * @see ILogger.Priority#WARNING
   * @see ILogger.Priority#VERBOSE
   * @see ILogger.Priority#INFO
   */
  protected abstract void doLog(Priority priority, String message);

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
   * Log warning messages.
   *
   * @param messages The messages to log.
   * @return This logger instance.
   */
  public ILogger warn(Object... messages) {
    return log(Priority.WARNING, messages);
  }

  /**
   * Log verbose messages.
   *
   * @param messages The messages to log.
   * @return This logger instance.
   */
  public ILogger verbose(Object... messages) {
    return log(Priority.VERBOSE, messages);
  }

  /**
   * Log information messages.
   *
   * @param messages The messages to log.
   * @return This logger instance.
   */
  public ILogger info(Object... messages) {
    return log(Priority.INFO, messages);
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
    return log(Priority.DEBUG, messages);
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

  /** Logging priority. */
  public enum Priority {
    DEBUG,
    WARNING,
    ERROR,
    INFO,
    VERBOSE
  }

  /** A listener which can be used to listen to log events. */
  public interface LogListener {
    void log(Priority priority, String tag, String message);
  }
}
