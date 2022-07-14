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
package com.itsaky.androidide.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/** Reads an input stream line by line */
public class InputStreamLineReader implements Runnable {

  private final InputStream in;
  private final OnReadListener listener;

  public InputStreamLineReader(InputStream in, OnReadListener listener) {
    this.in = in;
    this.listener = listener;
  }

  @Override
  public void run() {
    try {
      final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = reader.readLine()) != null && listener != null) {
        listener.onRead(line);
      }
    } catch (Throwable th) {
      // ignored
    }
  }

  public static interface OnReadListener {
    void onRead(String line);
  }
}
