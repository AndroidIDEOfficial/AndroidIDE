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
import org.slf4j.LoggerFactory
import java.nio.file.Path

/**
 * Singleton class for [CacheFSInfo] to avoid reading attributes of same file multiple times.
 *
 * @author Akash Yadav
 */
object CacheFSInfoSingleton : CacheFSInfo() {

  const val TEST_PROP_ENABLED_ON_JVM = "ide.testing.javac.fsCache.isEnabledOnJVM"
  private val log = LoggerFactory.getLogger(CacheFSInfoSingleton::class.java)

  /**
   * Caches information about the given [Path].
   */
  @JvmOverloads
  fun cache(file: Path, cacheJarClasspath: Boolean = true) {

    if (System.getProperty(TEST_PROP_ENABLED_ON_JVM, null) != "true") {
      if (VMUtils.isJvm()) {
        return
      }
    }

    try {
      // Cache canonical path
      getCanonicalFile(file)

      // Cache attributes
      getAttributes(file)

      // Cache jar classpath if requested
      if (cacheJarClasspath) {
        getJarClassPath(file)
      }
    } catch (err: Throwable) {
      log.warn("Failed to cache jar file: {}", file, err)
    }
  }
}