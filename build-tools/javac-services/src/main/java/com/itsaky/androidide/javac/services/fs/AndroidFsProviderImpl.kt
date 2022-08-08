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

import com.itsaky.androidide.zipfs2.AndroidFsProvider
import java.nio.file.spi.FileSystemProvider

/** @author Akash Yadav */
object AndroidFsProviderImpl : AndroidFsProvider() {

  init {
    INSTANCE = this
  }

  fun init() {
    // Used to instantiate this class and override the AndroidFsProvider instance
  }

  override fun zipFsProvider(): FileSystemProvider {
    return CachingJarFileSystemProvider
  }
}
