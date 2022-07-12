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

import com.google.common.collect.ImmutableSet
import com.google.common.reflect.ClassPath
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Path

/**
 * Lists all classes from classpath(s).
 *
 * @author Akash Yadav
 */
class ClasspathReader {
  companion object {

    private val log = ILogger.newInstance(ClasspathReader::class.java.simpleName)

    @Suppress("UnstableApiUsage")
    @JvmStatic
    fun listClasses(paths: Collection<Path>): ImmutableSet<ClassPath.ClassInfo> {
      val urls = paths.map { toUrl(it) }.toTypedArray()
      val classLoader = URLClassLoader(urls, null)

      val scanner: ClassPath
      try {
        scanner = ClassPath.from(classLoader)
      } catch (e: IOException) {
        log.warn("Unable to read classpaths for project:", paths)
        throw RuntimeException(e)
      }

      return scanner.allClasses
    }

    private fun toUrl(path: Path): URL {
      try {
        return path.toUri().toURL()
      } catch (e: MalformedURLException) {
        throw RuntimeException(e)
      }
    }
  }
}
