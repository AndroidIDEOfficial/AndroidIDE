/************************************************************************************
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
 **************************************************************************************/
package com.itsaky.androidide.managers;

import java.io.File;
import java.util.HashMap;

public class VersionedFileManager {

  private static final HashMap<File, Integer> map = new HashMap<>();

  public static int fileOpened(File file) {
    // Initial version of file should always be 1
    map.put(file, 1);
    return 1;
  }

  public static int incrementVersion(File file) {
    if (!map.containsKey(file)) {
      map.put(file, 1);
    }

    int version = map.get(file);
    version++;
    map.put(file, version);
    return version;
  }
}
