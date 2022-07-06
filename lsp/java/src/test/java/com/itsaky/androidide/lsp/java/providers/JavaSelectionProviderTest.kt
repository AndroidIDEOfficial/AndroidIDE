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
import com.itsaky.androidide.lsp.java.BaseJavaTest
import com.itsaky.androidide.lsp.models.ChangeType.NEW_TEXT
import com.itsaky.androidide.lsp.models.DocumentChangeEvent
import com.itsaky.androidide.lsp.models.ExpandSelectionParams
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.models.Range
import io.github.rosemoe.sora.text.Content
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class JavaSelectionProviderTest : BaseJavaTest() {

  @Before
  fun init() {
    LOG.debug("Initializing JavaSelectionProviderTest")
  }

  @Test
  fun testSimpleSelectionExpansion() {
    openFile("SimpleSelectionExpansionTest")
    cursor = requireCursor()
    deleteCursorText()
    mServer.documentHandler.onContentChange(
      com.itsaky.androidide.lsp.models.DocumentChangeEvent(file!!, contents.toString(), 1, NEW_TEXT, 0, com.itsaky.androidide.models.Range.NONE)
    )

    val range = findRange()
    val expanded = mServer.expandSelection(com.itsaky.androidide.lsp.models.ExpandSelectionParams(file!!, range))

    assertThat(expanded).isEqualTo(com.itsaky.androidide.models.Range(com.itsaky.androidide.models.Position(4, 27), com.itsaky.androidide.models.Position(4, 41)))
  }

  @Test
  fun testMethodSelection() {
    openFile("MethodBodySelectionExpansionTest")

    val start = com.itsaky.androidide.models.Position(3, 43)
    val end = com.itsaky.androidide.models.Position(5, 5)
    val range = com.itsaky.androidide.models.Range(start, end)

    val expanded = mServer.expandSelection(com.itsaky.androidide.lsp.models.ExpandSelectionParams(file!!, range))
    assertThat(expanded).isEqualTo(com.itsaky.androidide.models.Range(com.itsaky.androidide.models.Position(3, 4), end))
  }

  @Test
  fun testTryCatchSelection() {
    openFile("TrySelectionExpansionTest")

    // Test expand selection if catch block is selected
    val start = com.itsaky.androidide.models.Position(7, 10)
    val end = com.itsaky.androidide.models.Position(8, 9)
    val range = com.itsaky.androidide.models.Range(start, end)

    val expanded = mServer.expandSelection(com.itsaky.androidide.lsp.models.ExpandSelectionParams(file!!, range))
    assertThat(expanded).isEqualTo(com.itsaky.androidide.models.Range(com.itsaky.androidide.models.Position(4, 8), com.itsaky.androidide.models.Position(10, 9)))
  }

  @Test
  fun testTryFinallySelection() {
    openFile("TrySelectionExpansionTest")

    // Test expand selection if catch block is selected
    val start = com.itsaky.androidide.models.Position(8, 18)
    val end = com.itsaky.androidide.models.Position(10, 9)
    val range = com.itsaky.androidide.models.Range(start, end)

    val expanded = mServer.expandSelection(com.itsaky.androidide.lsp.models.ExpandSelectionParams(file!!, range))
    assertThat(expanded).isEqualTo(com.itsaky.androidide.models.Range(com.itsaky.androidide.models.Position(4, 8), com.itsaky.androidide.models.Position(10, 9)))
  }

  private fun findRange(): com.itsaky.androidide.models.Range {
    val pos = Content(contents!!).indexer.getCharPosition(cursor)
    val position = com.itsaky.androidide.models.Position(pos.line, pos.column, pos.index)
    return com.itsaky.androidide.models.Range(position, position)
  }

  override fun openFile(fileName: String) {
    super.openFile("selection/${fileName}")
  }
}
