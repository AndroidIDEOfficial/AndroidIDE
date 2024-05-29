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

package com.itsaky.androidide.editor.ui

import com.itsaky.androidide.editor.api.IEditor
import com.itsaky.androidide.editor.ui.IDEEditor.Companion.log
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.models.Range
import io.github.rosemoe.sora.widget.SelectionMovement
import java.io.File

/**
 * Handler which implements various features in [IEditor].
 *
 * @author Akash Yadav
 */
class EditorFeatures(
  var editor: IDEEditor? = null
) : IEditor {

  override fun getFile(): File? = withEditor { _file }

  override fun isModified(): Boolean = withEditor { this.isModified } ?: false

  override fun setSelection(position: Position) {
    withEditor {
      setSelection(position.line, position.column)
    }
  }

  override fun setSelection(start: Position, end: Position) {
    withEditor {
      if (!isValidPosition(start, true) || !isValidPosition(end, true)) {
        log.warn("Invalid selection range: start={} end={}", start, end)
        return@withEditor
      }

      setSelectionRegion(start.line, start.column, end.line, end.column)
    }
  }

  override fun setSelectionAround(line: Int, column: Int) {
    withEditor {
      if (line < lineCount) {
        val columnCount = text.getColumnCount(line)
        setSelection(line, if (column > columnCount) columnCount else column)
      } else {
        setSelection(lineCount - 1, text.getColumnCount(lineCount - 1))
      }
    }
  }

  override fun getCursorLSPRange(): Range = withEditor {
    val end = cursor.right().let {
      Position(line = it.line, column = it.column, index = it.index)
    }
    return@withEditor Range(cursorLSPPosition, end)
  } ?: Range.NONE

  override fun getCursorLSPPosition(): Position = withEditor {
    return@withEditor cursor.left().let {
      Position(line = it.line, column = it.column, index = it.index)
    }
  } ?: Position.NONE

  override fun validateRange(range: Range) {
    withEditor {
      val start = range.start
      val end = range.end
      val text = text
      val lineCount = text.lineCount

      start.line = 0.coerceAtLeast(start.line).coerceAtMost(lineCount - 1)
      start.column = 0.coerceAtLeast(start.column).coerceAtMost(text.getColumnCount(start.line))

      end.line = 0.coerceAtLeast(end.line).coerceAtMost(lineCount - 1)
      end.column = 0.coerceAtLeast(end.column).coerceAtMost(text.getColumnCount(end.line))
    }
  }

  override fun isValidRange(range: Range?, allowColumnEqual: Boolean): Boolean = withEditor {
    if (range == null) {
      return@withEditor false
    }
    val start = range.start
    val end = range.end
    return@withEditor isValidPosition(start, allowColumnEqual)
        // make sure start position is before end position
        && isValidPosition(end, allowColumnEqual) && start < end
  } ?: false

  override fun isValidPosition(position: Position?, allowColumnEqual: Boolean): Boolean =
    withEditor {
      return@withEditor if (position == null) {
        false
      } else isValidLine(position.line) &&
          isValidColumn(position.line, position.column, allowColumnEqual)
    } ?: false

  override fun isValidLine(line: Int): Boolean =
    withEditor { line >= 0 && line < text.lineCount } ?: false

  override fun isValidColumn(line: Int, column: Int, allowColumnEqual: Boolean): Boolean =
    withEditor {
      val columnCount = text.getColumnCount(line)
      return@withEditor column >= 0 && (column < columnCount || allowColumnEqual && column == columnCount)
    } ?: false

  override fun append(text: CharSequence?): Int = withEditor {
    val content = getText()
    if (lineCount <= 0) {
      return@withEditor 0
    }

    val line = lineCount - 1
    var col = content.getColumnCount(line)
    if (col < 0) {
      col = 0
    }
    content.insert(line, col, text)
    return@withEditor line
  } ?: -1

  override fun replaceContent(newContent: CharSequence?) {
    withEditor {
      val lastLine = text.lineCount - 1
      val lastColumn = text.getColumnCount(lastLine)
      text.replace(0, 0, lastLine, lastColumn, newContent ?: "")
    }
  }

  override fun goToEnd() {
    withEditor {
      moveSelection(SelectionMovement.TEXT_END)
    }
  }

  private inline fun <T> withEditor(crossinline action: IDEEditor.() -> T): T? {
    return this.editor?.run {
      if (isReleased) {
        null
      } else action()
    }
  }
}