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

import com.itsaky.androidide.lsp.models.DiagnosticSeverity.ERROR
import com.itsaky.androidide.lsp.models.DiagnosticSeverity.HINT
import com.itsaky.androidide.lsp.models.DiagnosticSeverity.INFO
import com.itsaky.androidide.lsp.models.DiagnosticSeverity.WARNING
import com.itsaky.androidide.models.Range
import io.github.rosemoe.sora.lang.diagnostic.DiagnosticRegion
import io.github.rosemoe.sora.lang.diagnostic.DiagnosticRegion.SEVERITY_ERROR
import io.github.rosemoe.sora.lang.diagnostic.DiagnosticRegion.SEVERITY_NONE
import io.github.rosemoe.sora.lang.diagnostic.DiagnosticRegion.SEVERITY_TYPO
import io.github.rosemoe.sora.lang.diagnostic.DiagnosticRegion.SEVERITY_WARNING
import java.nio.file.Path
import java.nio.file.Paths

data class DiagnosticItem(
  var message: String,
  var code: String,
  var range: Range,
  var source: String,
  var severity: DiagnosticSeverity
) {

  var extra: Any = Any()

  companion object {
    @JvmField
    val START_COMPARATOR: Comparator<in DiagnosticItem> =
      Comparator.comparing(DiagnosticItem::range)

    private fun mapSeverity(severity: DiagnosticSeverity): Short {
      return when (severity) {
        ERROR -> SEVERITY_ERROR
        WARNING -> SEVERITY_WARNING
        INFO -> SEVERITY_NONE
        HINT -> SEVERITY_TYPO
      }
    }
  }

  fun asDiagnosticRegion(): DiagnosticRegion =
    DiagnosticRegion(range.start.requireIndex(), range.end.requireIndex(), mapSeverity(severity))
}

data class DiagnosticResult(var file: Path, var diagnostics: List<DiagnosticItem>) {
  companion object {
    @JvmField val NO_UPDATE = DiagnosticResult(Paths.get(""), emptyList())
  }
}

enum class DiagnosticSeverity {
  ERROR,
  WARNING,
  INFO,
  HINT
}
