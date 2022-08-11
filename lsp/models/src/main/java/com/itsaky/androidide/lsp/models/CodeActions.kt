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

package com.itsaky.androidide.lsp.models

import com.itsaky.androidide.lsp.models.CodeActionKind.None
import com.itsaky.androidide.models.Range
import java.nio.file.Path

data class CodeActionParams(
  var file: Path,
  var range: Range,
  var diagnostics: List<DiagnosticItem>
)

data class CodeActionResult(var actions: List<CodeActionItem>) {
  constructor() : this(ArrayList())
}

data class CodeActionItem(
  var title: String,
  var changes: List<DocumentChange>,
  var kind: CodeActionKind,
  var command: Command
) {
  constructor() : this("", ArrayList(), None, Command("", ""))
}

enum class CodeActionKind {
  QuickFix,
  None
}

data class DocumentChange(var file: Path?, var edits: List<TextEdit>) {
  constructor() : this(null, ArrayList())
}
