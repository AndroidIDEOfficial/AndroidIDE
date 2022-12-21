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

package com.itsaky.androidide.projects.models

import com.itsaky.androidide.models.Range
import com.itsaky.androidide.utils.DocumentUtils
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.nio.file.Path
import java.time.Instant

/**
 * A document that is opened in the editor.
 *
 * @author Akash Yadav
 */
open class ActiveDocument(
  val file: Path,
  val content: String,
  val changeRange: Range,
  var changDelta: Int,
  val version: Int,
  val modified: Instant
) {

  companion object {

    @JvmStatic
    fun create(
      file: Path,
      content: String,
      changeRange: Range,
      changDelta: Int,
      version: Int,
      modified: Instant
    ): ActiveDocument {
      if (DocumentUtils.isJavaFile(file)) {
        return ActiveJavaDocument(file, content, changeRange, changDelta, version, modified)
      }

      return ActiveDocument(file, content, changeRange, changDelta, version, modified)
    }
  }

  fun inputStream(): BufferedInputStream {
    return content.byteInputStream().buffered()
  }

  fun reader(): BufferedReader {
    return content.reader().buffered()
  }
}
