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

package com.itsaky.lsp.models

import java.net.URI
import java.nio.file.Path

/**
 * Base class for files that accept files as parameters.
 */
open class DocumentEvent (var file: Path) {
    fun asUri () : URI {
        return file.toUri()
    }
}

data class DocumentOpenEvent (var openedFile: Path,
                              var text: String,
                              var version: Int) : DocumentEvent (openedFile)

data class DocumentCloseEvent (var closedFile: Path) : DocumentEvent (closedFile)

data class DocumentChangeEvent (var changedFile: Path,
                                var newText: CharSequence,
                                var version: Int) : DocumentEvent (changedFile)

data class DocumentSaveEvent (var savedFile: Path) : DocumentEvent (savedFile)

data class ShowDocumentParams (var file: Path, var selection: Range)

data class ShowDocumentResult (var success: Boolean)