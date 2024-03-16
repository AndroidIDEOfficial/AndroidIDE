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

import com.blankj.utilcode.util.ZipUtils;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

public final class UnzipCallable implements Callable<List<File>> {

  private File src;
  private File dest;

  public UnzipCallable(File src, File dest) {
    this.src = src;
    this.dest = dest;
  }

  @Override
  public List<File> call() throws Exception {
    return ZipUtils.unzipFile(src, dest);
  }
}
