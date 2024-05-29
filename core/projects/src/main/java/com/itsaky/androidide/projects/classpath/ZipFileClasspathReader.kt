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

package com.itsaky.androidide.projects.classpath

import com.google.common.collect.ImmutableSet
import java.io.File
import java.util.zip.ZipFile

/**
 * Lists all classes from classpath(s).
 *
 * @author Akash Yadav
 */
class ZipFileClasspathReader : IClasspathReader {
  
  override fun listClasses(files: Collection<File>): ImmutableSet<ClassInfo> {
    val classes = ImmutableSet.builder<ClassInfo>()
    files.forEach {
      if (!it.exists()) {
        return@forEach
      }
    
      ZipFile(it).use { zipFile ->
        for (entry in zipFile.entries()) {
          if (!entry.name.endsWith(".class")) {
            continue
          }
        
          var name = entry.name.substringBeforeLast(".class")
          if (name.length <= 1) {
            continue
          }
        
          if (name.startsWith('/')) {
            name = name.substring(1)
          }
        
          if (name.contains('/')) {
            name = name.replace('/', '.')
          }

          ClassInfo.create(name)?.also { classInfo ->
            classes.add(classInfo)
          }
        }
      }
    }
  
    return classes.build()
  }
}
