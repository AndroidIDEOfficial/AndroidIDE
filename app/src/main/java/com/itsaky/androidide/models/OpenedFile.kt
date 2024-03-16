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
import com.google.gson.stream.JsonReader
import org.slf4j.LoggerFactory

/**
 * A file that is opened in editor.
 *
 * @author Akash Yadav
 */
data class OpenedFile(
  @SerializedName(KEY_FILE) val filePath: String,
  @SerializedName(KEY_SELECTION) var selection: Range
) {

  companion object {

    private const val KEY_FILE = "file"
    private const val KEY_SELECTION = "selection"
    private val log = LoggerFactory.getLogger(OpenedFile::class.java)

    fun readFrom(reader: JsonReader): OpenedFile? {
      return try {
//        reader.beginObject()
//        var path = ""
//        var selection = Range.NONE
//        while(reader.hasNext()) {
//          val name = reader.nextName()
//          if (name == KEY_FILE) {
//            path = reader.nextString()
//          } else if (name == KEY_SELECTION) {
//            selection = Gson().fromJson(reader, Range::class.java)
//          }
//        }
//        reader.endObject()
//
//        OpenedFile(path, selection)
        Gson().fromJson(reader, OpenedFile::class.java)
      } catch (err: Exception) {
        log.error("Failed to read opened file", err)
        null
      }
    }
  }
}
