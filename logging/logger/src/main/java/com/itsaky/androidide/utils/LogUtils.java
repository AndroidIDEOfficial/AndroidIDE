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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Utility class for {@link ILogger}.
 *
 * @author Akash Yadav
 */
public class LogUtils {
  private static final String LINE_SEP = System.getProperty("line.separator");

  /**
   * @return <code>true</code> if the current platform is JVM, <code>false</code> otherwise.
   */
  public static boolean isJvm() {
    try {
      // If we're in a testing environment
      Class.forName("org.junit.runners.JUnit4");
      return true;
    } catch (ClassNotFoundException e) {
      // ignored
    }

    try {
      Class.forName("android.content.Context");
      return false;
    } catch (ClassNotFoundException e) {
      return true;
    }
  }

  public static String preProcessLogTag(String tag) {
    final var regex = "[^a-z-A-Z0-9_.]";
    final var matcher = Pattern.compile(regex).matcher(tag);
    if (matcher.find()) {
      tag = tag.replaceAll(regex, "_");
    }

    return tag;
  }

  public static String getFullStackTrace(Throwable throwable) {
    final List<Throwable> throwableList = new ArrayList<>();
    while (throwable != null && !throwableList.contains(throwable)) {
      throwableList.add(throwable);
      throwable = throwable.getCause();
    }
    final int size = throwableList.size();
    final List<String> frames = new ArrayList<>();
    List<String> nextTrace = getStackFrameList(throwableList.get(size - 1));
    for (int i = size; --i >= 0; ) {
      final List<String> trace = nextTrace;
      if (i != 0) {
        nextTrace = getStackFrameList(throwableList.get(i - 1));
        removeCommonFrames(trace, nextTrace);
      }
      if (i == size - 1) {
        frames.add(throwableList.get(i).toString());
      } else {
        frames.add(" Caused by: " + throwableList.get(i).toString());
      }
      frames.addAll(trace);
    }
    StringBuilder sb = new StringBuilder();
    for (final String element : frames) {
      sb.append(element).append(LINE_SEP);
    }
    return sb.toString();
  }

  private static List<String> getStackFrameList(final Throwable throwable) {
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw, true);
    throwable.printStackTrace(pw);
    final String stackTrace = sw.toString();
    final StringTokenizer frames = new StringTokenizer(stackTrace, LINE_SEP);
    final List<String> list = new ArrayList<>();
    boolean traceStarted = false;
    while (frames.hasMoreTokens()) {
      final String token = frames.nextToken();
      // Determine if the line starts with <whitespace>at
      final int at = token.indexOf("at");
      if (at != -1 && token.substring(0, at).trim().isEmpty()) {
        traceStarted = true;
        list.add(token);
      } else if (traceStarted) {
        break;
      }
    }
    return list;
  }

  private static void removeCommonFrames(
      final List<String> causeFrames, final List<String> wrapperFrames) {
    int causeFrameIndex = causeFrames.size() - 1;
    int wrapperFrameIndex = wrapperFrames.size() - 1;
    while (causeFrameIndex >= 0 && wrapperFrameIndex >= 0) {
      // Remove the frame from the cause trace if it is the same
      // as in the wrapper trace
      final String causeFrame = causeFrames.get(causeFrameIndex);
      final String wrapperFrame = wrapperFrames.get(wrapperFrameIndex);
      if (causeFrame.equals(wrapperFrame)) {
        causeFrames.remove(causeFrameIndex);
      }
      causeFrameIndex--;
      wrapperFrameIndex--;
    }
  }
}
