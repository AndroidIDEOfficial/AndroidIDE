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
import java.io.File
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ProjectItem(val path: Path, val name: String, val iconPath: String, val cache: ProjectInfoCache){


  companion object {

    val projects = mutableListOf<ProjectItem>()
    private fun searchProjects(dir: File) {
      if (projects.size > 11) return

      val subDirectories = dir.listFiles() ?: return

      for (subDir in subDirectories) {
        if (!subDir.isDirectory) continue

        val androidIdeFolder = File(subDir, ".androidide")
        if (androidIdeFolder.exists() && androidIdeFolder.isDirectory) {
          val file = File(androidIdeFolder.path, "editor/projectInfo.json")

          ProjectInfoCache.read(file) { cache ->
            if (cache != null) {

              val iconPath = "${subDir.absolutePath}/res/mipmap-hdpi/ic_launcher.webp"
              val projectItem = ProjectItem(
                path = subDir.toPath(),
                name = subDir.name,
                iconPath = iconPath,
                cache = cache
              )
              projects.add(projectItem)
            }
          }
        }
      }
    }
    fun getProjects(path: String = "/storage/emulated/0/AndroidIDEProjects", context: Context): List<ProjectItem> {
      val rootDirectory = File(path)
      if (rootDirectory.exists()) {
        searchProjects(rootDirectory)
      }

      val sortedProjects = projects.sortedBy { projectItem ->
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val openTime = LocalDateTime.parse(projectItem.cache.lastOpened, formatter)
        openTime
      }

      return sortedProjects
    }

  }
}
