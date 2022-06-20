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

import android.util.Log;

/**
 * {@link ILogger} implementation for the Android Runtime (ART).
 *
 * @author Akash Yadav
 */
public class AndroidLogger extends ILogger {

  protected AndroidLogger(String tag) {
    super(tag);
  }

  @Override
  protected void doLog(Priority priority, String message) {
    switch (priority) {
      case ERROR:
        Log.e(TAG, message);
        break;
      case WARNING:
        Log.w(TAG, message);
        break;
      case VERBOSE:
        Log.v(TAG, message);
        break;
      case INFO:
        Log.i(TAG, message);
        break;
      case DEBUG:
      default:
        Log.d(TAG, message);
        break;
    }
  }
}
