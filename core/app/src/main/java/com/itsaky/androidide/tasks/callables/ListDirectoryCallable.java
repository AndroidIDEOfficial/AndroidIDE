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

package com.itsaky.androidide.tasks.callables;

import android.text.TextUtils;
import com.blankj.utilcode.util.FileUtils;
import java.io.File;
import java.io.FileFilter;

public class ListDirectoryCallable implements java.util.concurrent.Callable<String> {

  private final FileFilter ARCHIVE_FILTER =
      new FileFilter() {

        @Override
        public boolean accept(File p1) {
          return p1.isFile() && (p1.getName().endsWith(".tar.xz") || p1.getName().endsWith(".zip"));
        }
      };
  private File file;

  public ListDirectoryCallable(File file) {
    this.file = file;
  }

  @Override
  public String call() throws Exception {
    return TextUtils.join("\n", FileUtils.listFilesInDirWithFilter(file, ARCHIVE_FILTER, false));
  }
}
