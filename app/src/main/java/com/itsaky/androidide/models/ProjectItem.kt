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

package com.itsaky.androidide.models

import android.content.Context
import android.graphics.drawable.Drawable
import com.itsaky.androidide.utils.getIcon
import java.io.File
import java.nio.file.Path

data class ProjectItem(val path: Path, val name: String, val iconPath: String){
}
fun getProjects(path: String = "/storage/emulated/0/AndroidIDEProjects", context: Context): List<ProjectItem> {
  val projects = mutableListOf<ProjectItem>()

  fun searchProjectsRecursively(dir: File) {
    val subDirectories = dir.listFiles()
    if (subDirectories != null) {
      for (subDir in subDirectories) {
        if (subDir.isDirectory) {
          val androidIdeFolder = File(subDir, ".androidide")
          if (androidIdeFolder.exists() && androidIdeFolder.isDirectory) {
            val projectItem = ProjectItem(
              path = subDir.toPath(),
              name = subDir.name,
              iconPath = "${subDir.absolutePath}" +
                  "" +
                  "in/res/mipmap-hdpi/ic_launcher.webp")
            projects.add(projectItem)
          }

        }
      }
    }
  }

  val rootDirectory = File(path)
  if (rootDirectory.exists() && rootDirectory.isDirectory) {
    searchProjectsRecursively(rootDirectory)
  }

  return projects
}
