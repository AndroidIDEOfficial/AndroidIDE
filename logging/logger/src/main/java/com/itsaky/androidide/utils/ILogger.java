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

import com.itsaky.androidide.buildinfo.BuildInfo;
import java.util.Map;
import java.util.WeakHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logger for the IDE.
 *
 * <p>If a {@link Throwable} is passed to any of the logging methods, whole stack trace of the
 * throwable is printed. Any modifications to this class will affect every log message in the IDE.
 *
 * @author Akash Yadav
 */
public abstract class ILogger {

  public static final Logger ROOT = LoggerFactory.getLogger(BuildInfo.PACKAGE_NAME);

  public static final String MSG_SEPARATOR = " "; // Separate messages with a space.

  protected final String TAG;

  protected boolean isEnabled = true;

  protected ILogger(String tag) {
    TAG = preProcessLogTag(tag);
  }

  /**
   * Logging level.
   */
  public enum Level {

    DEBUG('D'),
    WARNING('W'),
    ERROR('E'),
    INFO('I'),
    VERBOSE('V');

    public final char levelChar;

    Level(char levelChar) {
      this.levelChar = levelChar;
    }

    public static Level forChar(char c) {
      c = Character.toUpperCase(c);
      if (c == 'T') {
        // trace
        return VERBOSE;
      }

      for (Level value : values()) {
        if (value.levelChar == c) {
          return value;
        }
      }

      throw new IllegalArgumentException("Invalid level char " + c);
    }
  }

  /**
   * A listener which can be used to listen to log events.
   */
  public interface LogListener {

    void log(Level level, String tag, String message);
  }
}
