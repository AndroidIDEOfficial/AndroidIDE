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

import com.itsaky.androidide.zipfs2.JarFileSystemProvider
import com.itsaky.androidide.zipfs2.ZipFileSystem
import java.nio.file.FileSystem
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.pathString

/**
 * An implementation of [JarFileSystemProvider] that caches the created [CachedJarFileSystem] so
 * that it can be (re)used in multiple compilations.
 *
 * @author Akash Yadav
 */
object CachingJarFileSystemProvider : JarFileSystemProvider() {
  private val cachedFs = ConcurrentHashMap<String, CachedJarFileSystem>()

  override fun createFs(path: Path, env: MutableMap<String, *>?): ZipFileSystem {
    val cached = cachedFs[path.normalize().pathString]
    if (cached != null) {
      return cached
    }
    return createAndCache(path, env)
  }

  fun newFileSystem(path: Path): FileSystem? {
    return newFileSystem(path, mutableMapOf<String, Any>())
  }

  fun clearCache() {
    cachedFs.values.forEach { it.doClose() }
    cachedFs.clear()
  }

  private fun createAndCache(path: Path, env: MutableMap<String, *>?): CachedJarFileSystem {
    val fs = CachedJarFileSystem(this, path, env)
    cachedFs[path.normalize().pathString] = fs
    return fs
  }
}
