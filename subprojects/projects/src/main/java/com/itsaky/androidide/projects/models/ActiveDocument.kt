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

import com.itsaky.androidide.eventbus.events.editor.ChangeType
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.utils.ILogger
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.Closeable
import java.nio.file.Path
import java.time.Instant

/**
 * A document that is opened in the editor.
 *
 * @author Akash Yadav
 */
open class ActiveDocument(
  val file: Path,
  val version: Int,
  val modified: Instant,
  content: String = ""
) : Closeable {

  private val _content = StringBuilder(content)
  private val log = ILogger.newInstance("ActiveDocument")

  val content: String
    get() = _content.toString()

  fun patch(event: DocumentChangeEvent) {
    val text = event.changedText
    val start = event.changeRange.start.requireIndex()
    val end = event.changeRange.end.requireIndex()

    when (event.changeType) {
      ChangeType.DELETE -> _content.delete(start, end)
      ChangeType.INSERT -> _content.insert(start, text)
      else -> {
        _content.clear()
        _content.append(text)
      }
    }
  }

  override fun close() {
    _content.clear()
  }

  fun inputStream(): BufferedInputStream {
    return content.byteInputStream().buffered()
  }

  fun reader(): BufferedReader {
    return content.reader().buffered()
  }
}
