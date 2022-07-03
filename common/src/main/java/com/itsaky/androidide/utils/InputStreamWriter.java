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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/** Writes an InputStream to a file */
public class InputStreamWriter implements Runnable {

  private final InputStream in;
  private final File out;

  private final BufferedOutputStream os;

  public InputStreamWriter(InputStream in, File out) throws FileNotFoundException {
    this.in = in;
    this.out = out;

    this.os = new BufferedOutputStream(new FileOutputStream(out, true));
  }

  @Override
  public void run() {
    try {
      final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      while (true) {
        final String data = reader.readLine();
        if (data == null) {
          break;
        }

        os.write(data.concat("\n").getBytes());
        os.flush();
      }
    } catch (Throwable th) {
      // ignored
    }
  }
}
