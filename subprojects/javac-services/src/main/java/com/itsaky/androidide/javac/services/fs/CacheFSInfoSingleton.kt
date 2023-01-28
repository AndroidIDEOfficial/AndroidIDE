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

import com.itsaky.androidide.utils.VMUtils
import openjdk.tools.javac.file.CacheFSInfo
import java.nio.file.Path

/**
 * Singleton class for [CacheFSInfo] to avoid reading attributes of same file multiple times.
 *
 * @author Akash Yadav
 */
object CacheFSInfoSingleton : CacheFSInfo() {
  
  /**
   * Caches information about the given [Path].
   */
  @JvmOverloads
  fun cache(file: Path, cacheJarClasspath: Boolean = true) {
    
    if (VMUtils.isJvm()) {
      return
    }
    
    // Cache canonical path
    getCanonicalFile(file)
    
    // Cache attributes
    getAttributes(file)
    
    // Cache jar classpath if requested
    if (cacheJarClasspath) {
      getJarClassPath(file)
    }
  }
}