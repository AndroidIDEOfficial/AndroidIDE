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

package com.itsaky.androidide.logging;

import com.itsaky.androidide.logging.utils.LogUtils;

/**
 * @author Akash Yadav
 */
public class JvmStdErrAppender extends StdErrAppender {

  public static final String PROP_JVM_STDERR_APPENDER_ENABLED = "ide.logging.jvmStdErrAppenderEnabled";

  private boolean isJvm = false; // JvmStdErrAppender is disabled by default

  @Override
  public void start() {
    this.isJvm = LogUtils.isJvm();
    super.start();
  }

  @Override
  public boolean isStarted() {
    return super.isStarted()
        && isJvm
        && Boolean.parseBoolean(System.getProperty(PROP_JVM_STDERR_APPENDER_ENABLED, "true"));
  }
}
