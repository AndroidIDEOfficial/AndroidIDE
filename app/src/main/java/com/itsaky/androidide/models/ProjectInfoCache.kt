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

import com.blankj.utilcode.util.FileUtils
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.io.Reader

data class ProjectInfoCache(@SerializedName(KEY_LAST_OPENED) val lastOpened: String) {

  companion object {

    private const val KEY_LAST_OPENED = "lastOpened"
    private val log = ILogger.newInstance("ProjectInfoCache")

    @JvmStatic
    fun parse(contentReader: Reader): ProjectInfoCache? {
      return try {
        JsonReader(contentReader).use { reader ->
          if (!reader.hasNext()) {
            return@use null
          }

          reader.beginObject()
          var lastOpened = ""
          while (reader.hasNext()) {
            val name = reader.nextName()

            if (name == KEY_LAST_OPENED) {
              lastOpened = reader.nextString()
            }
          }
          reader.endObject()

          return@use ProjectInfoCache(lastOpened)
        }
      } catch (err: Exception) {
        log.error("Failed to parse project info cache", err)
        null
      }

    }


    fun getProjectInfoCache(forWrite: Boolean = false): File {
      var file = Environment.getProjectCacheDir(IProjectManager.getInstance().projectDir)
      file = File(file, "editor/projectInfo.json")

      if (file.exists() && forWrite) {
        FileUtils.rename(file, "${file.name}.bak")
      }
      if (file.parentFile?.exists() == false) {
        file.parentFile?.mkdirs()
      }
      file.createNewFile()

      return file
    }

    fun read(file: File,result: (ProjectInfoCache?) -> Unit) {
      executeAsync({
        if (file.length() == 0L) {
          return@executeAsync null
        }
        return@executeAsync file.reader().buffered().use { reader ->
          parse(reader)
        }

      }) {

        result(it)
      }
    }

  }

}
