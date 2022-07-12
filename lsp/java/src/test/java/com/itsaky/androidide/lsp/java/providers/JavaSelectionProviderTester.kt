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
package com.itsaky.androidide.lsp.java.providers

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.eventbus.events.editor.ChangeType.NEW_TEXT
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.lsp.java.JavaLSPTest
import com.itsaky.androidide.lsp.models.ExpandSelectionParams
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.models.Range
import io.github.rosemoe.sora.text.Content

/** @author Akash Yadav */
class JavaSelectionProviderTester : JavaLSPTest() {

  override fun test() {
    testSimpleSelectionExpansion()
    testMethodSelection()
    testTryCatchSelection()
    testTryFinallySelection()
  }

  private fun testSimpleSelectionExpansion() {
    openFile("SimpleSelectionExpansionTest")
    cursor = requireCursor()
    deleteCursorText()
    dispatchEvent(DocumentChangeEvent(file!!, contents.toString(), 1, NEW_TEXT, 0, Range.NONE))

    val range = findRange()
    val expanded = server.expandSelection(ExpandSelectionParams(file!!, range))

    assertThat(expanded).isEqualTo(Range(Position(4, 27), Position(4, 41)))
  }

  private fun testMethodSelection() {
    openFile("MethodBodySelectionExpansionTest")

    val start = Position(3, 43)
    val end = Position(5, 5)
    val range = Range(start, end)

    val expanded = server.expandSelection(ExpandSelectionParams(file!!, range))
    assertThat(expanded).isEqualTo(Range(Position(3, 4), end))
  }

  private fun testTryCatchSelection() {
    openFile("TrySelectionExpansionTest")

    // Test expand selection if catch block is selected
    val start = Position(7, 10)
    val end = Position(8, 9)
    val range = Range(start, end)

    val expanded = server.expandSelection(ExpandSelectionParams(file!!, range))
    assertThat(expanded).isEqualTo(Range(Position(4, 8), Position(10, 9)))
  }

  private fun testTryFinallySelection() {
    openFile("TrySelectionExpansionTest")

    // Test expand selection if catch block is selected
    val start = Position(8, 18)
    val end = Position(10, 9)
    val range = Range(start, end)

    val expanded = server.expandSelection(ExpandSelectionParams(file!!, range))
    assertThat(expanded).isEqualTo(Range(Position(4, 8), Position(10, 9)))
  }

  private fun findRange(): Range {
    val pos = Content(contents!!).indexer.getCharPosition(cursor)
    val position = Position(pos.line, pos.column, pos.index)
    return Range(position, position)
  }

  override fun openFile(fileName: String) {
    super.openFile("selection/${fileName}")
  }
}
