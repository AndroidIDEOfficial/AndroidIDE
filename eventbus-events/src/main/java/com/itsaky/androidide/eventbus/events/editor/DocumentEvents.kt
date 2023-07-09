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

package com.itsaky.androidide.eventbus.events.editor

import com.itsaky.androidide.models.Range
import java.net.URI
import java.nio.file.Path

/** Base class for files that accept files as parameters. */
open class DocumentEvent(var file: Path) {
  fun asUri(): URI {
    return file.toUri()
  }
}

/** Dispatched when an editor is opened for the given file. */
data class DocumentOpenEvent(var openedFile: Path, var text: String, var version: Int) :
  DocumentEvent(openedFile)

/** Dispatched when the given file is closed. Always dispatched after [DocumentOpenEvent]. */
data class DocumentCloseEvent
@JvmOverloads
constructor(var closedFile: Path, val selectionRange: Range = Range.NONE) :
  DocumentEvent(closedFile)

/**
 * Dispatched when the content of the given opened document changes. The change can be either
 * performed by the user or the IDE itself.
 */
data class DocumentChangeEvent(
  var changedFile: Path,
  var changedText: String,
  var newText: String? = null,
  var version: Int,
  var changeType: ChangeType,
  var changeDelta: Int,
  var changeRange: Range
) : DocumentEvent(changedFile)

/** Dispatched when the given document is saved to disk. */
data class DocumentSaveEvent(var savedFile: Path) : DocumentEvent(savedFile)

/** Dispatched when the given opened document is selected and it is visible to the user. */
data class DocumentSelectedEvent(var selectedFile: Path) : DocumentEvent(selectedFile)

/** The type of change in a [DocumentChangeEvent]. */
enum class ChangeType {
  INSERT,
  DELETE,
  NEW_TEXT
}
