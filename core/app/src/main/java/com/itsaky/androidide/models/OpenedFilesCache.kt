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

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import org.slf4j.LoggerFactory
import java.io.Reader

/**
 * @author Akash Yadav
 */
data class OpenedFilesCache(@SerializedName(KEY_SELECTED_FILE) val selectedFile: String,
  @SerializedName(KEY_ALL_FILES) val allFiles: List<OpenedFile>) {

  companion object {

    private const val KEY_SELECTED_FILE = "selectedFile"
    private const val KEY_ALL_FILES = "allFiles"
    private val log = LoggerFactory.getLogger(OpenedFilesCache::class.java)

    @JvmStatic
    fun parse(contentReader: Reader): OpenedFilesCache? {
      return try {
        JsonReader(contentReader).use { reader ->
          if (!reader.hasNext()) {
            return@use null
          }

          reader.beginObject()
          var selectedFile = ""
          var allFiles = emptyList<OpenedFile>()
          while (reader.hasNext()) {
            val name = reader.nextName()

            if (name == KEY_SELECTED_FILE) {
              selectedFile = reader.nextString()
            } else if (name == KEY_ALL_FILES) {
              allFiles = Gson().fromJson(reader, object : TypeToken<List<OpenedFile>>() {})
            }
          }
          reader.endObject()

          return@use OpenedFilesCache(selectedFile, allFiles)
        }
      } catch (err: Exception) {
        log.error("Failed to parse opened files cache", err)
        null
      }
    }
  }
}