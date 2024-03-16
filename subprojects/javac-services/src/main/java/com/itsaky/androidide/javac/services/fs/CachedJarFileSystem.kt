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

package com.itsaky.androidide.javac.services.fs

import com.itsaky.androidide.zipfs2.ZipFileSystem
import com.itsaky.androidide.zipfs2.ZipFileSystemProvider
import jdkx.lang.model.SourceVersion
import openjdk.tools.javac.file.RelativePath.RelativeDirectory
import java.io.IOException
import java.nio.file.Path

/**
 * A cached file system for JAR files.
 *
 * @author Akash Yadav
 */
class CachedJarFileSystem(
  provider: ZipFileSystemProvider?,
  zfpath: Path?,
  env: MutableMap<String, *>?
) : ZipFileSystem(provider, zfpath, env) {
  
  internal val packages = mutableMapOf<RelativeDirectory, Path>()

  override fun close() {
    // Do nothing
    // This is called manually by the Java LSP
  }

  @Throws(IOException::class)
  fun doClose() {
    super.close()
  }

  fun storeJARPackageDir(dir: Path?): Boolean {
    if (isValid(dir?.fileName)) {
      packages[RelativeDirectory(rootDir.relativize(dir!!).toString())] = dir
      return true
    }

    return false
  }

  private fun isValid(fileName: Path?): Boolean {
    return if (fileName == null) {
      true
    } else {
      var name = fileName.toString()
      if (name.endsWith("/")) {
        name = name.substring(0, name.length - 1)
      }
      SourceVersion.isIdentifier(name)
    }
  }
}
