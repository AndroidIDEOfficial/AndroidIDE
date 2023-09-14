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

package com.itsaky.androidide.utils

import com.itsaky.androidide.tasks.callables.UnzipCallable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.zip.ZipFile

object ZipUtils {

  fun unzipInBackground(src: File, dest: File): Future<List<File>> {
    val executor = Executors.newSingleThreadExecutor()
    val callable = UnzipCallable(src, dest)
    return executor.submit(callable)

  }

  suspend fun unzip(srcFile: File, destDir: File) = withContext(Dispatchers.IO) {
    val zip = ZipFile(srcFile)
    val entries = zip.entries()

    while (entries.hasMoreElements()) {
      
    }
  }
}