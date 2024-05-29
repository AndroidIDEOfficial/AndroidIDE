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

import com.itsaky.androidide.zipfs2.JarPackageProvider
import openjdk.tools.javac.file.RelativePath.RelativeDirectory
import java.nio.file.Path

/**
 * Provides cached packages from arhives.
 *
 * @author Akash Yadav
 */
object JarPackageProviderImpl : JarPackageProvider {
  override fun getPackages(archivePath: Path): MutableMap<RelativeDirectory, Path> {
    val fs = CachingJarFileSystemProvider.newFileSystem(archivePath) as CachedJarFileSystem
    return fs.packages
  }
}
