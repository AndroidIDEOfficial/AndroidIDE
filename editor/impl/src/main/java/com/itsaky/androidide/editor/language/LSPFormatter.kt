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

package com.itsaky.androidide.editor.language

import com.itsaky.androidide.lsp.api.ILanguageServer
import com.itsaky.androidide.lsp.models.CodeFormatResult
import com.itsaky.androidide.lsp.models.FormatCodeParams
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.models.Range
import io.github.rosemoe.sora.lang.format.AsyncFormatter
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.text.TextRange

/**
 * An [AsyncFormatter] implementation which uses the LSP implementation to format code.
 *
 * @author Akash Yadav
 */
class LSPFormatter(val server: ILanguageServer? = null) : AsyncFormatter() {
  
  override fun formatAsync(text: Content, cursorRange: TextRange): TextRange {
    return doFormat(text, cursorRange)
  }

  override fun formatRegionAsync(
    text: Content,
    rangeToFormat: TextRange,
    cursorRange: TextRange
  ): TextRange {
    return doFormat(text, cursorRange, rangeToFormat)
  }

  private fun doFormat(
    text: Content,
    cursorRange: TextRange,
    rangeToFormat: TextRange? = null
  ): TextRange {
    if (server == null) {
      return cursorRange
    }

    val range =
      (rangeToFormat?.asRange() ?: text.wholeRange()).apply {
        start.apply {
          index = (if (line == 0 && column == 0) 0 else text.getCharIndex(line, column))
        }
        end.apply { index = (if (line == 0 && column == 0) 0 else text.getCharIndex(line, column)) }
      }
    val result = server.formatCode(FormatCodeParams(text, range))

    if (!result.hasEdits() ) {
      // Deselect the selected content
      return TextRange(cursorRange.start, cursorRange.start)
    }

    if (result.isIndexed) {
      result.indexedTextEdits.forEach { text.replace(it.start, it.end, it.newText) }
    } else {
      result.edits.forEach {
        text.replace(
          it.range.start.line,
          it.range.start.column,
          it.range.end.line,
          it.range.end.column,
          it.newText
        )
      }
    }
    // Deselect the selected content
    return TextRange(cursorRange.start, cursorRange.start)
  }
}

private fun CodeFormatResult.hasEdits() =
  this.indexedTextEdits.isNotEmpty() || this.edits.isNotEmpty()

private fun TextRange.asRange(): Range {
  return Range().also {
    it.start = this.start.asPosition()
    it.end = this.end.asPosition()
  }
}

private fun Content.wholeRange(): Range {
  return Range(Position(0, 0), Position(lineCount - 1, getColumnCount(lineCount - 1)))
}

private fun CharPosition.asPosition(): Position {
  return Position(this.line, this.column, this.index)
}

private fun Range.asTextRange(): TextRange {
  return TextRange(this.start.asCharPosition(), this.end.asCharPosition())
}

private fun Position.asCharPosition(): CharPosition {
  return CharPosition().also {
    it.line = this.line
    it.column = this.column
    it.index = this.index
  }
}
