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

package com.itsaky.androidide.lsp.xml.providers

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.xml.XMLLSPTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class XMLCompletionProviderTest {

  @Before
  fun setup() {
    XMLLSPTest.initProjectIfNeeded()
  }

  @Test
  fun `test simple tag completion`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TagCompletion")

      val (isIncomplete, items) = complete()

      assertThat(isIncomplete).isFalse()
      assertThat(items).isNotEmpty()

      assertThat(items).containsAtLeast("ImageView", "ImageButton")
    }
  }

  @Test
  fun `test simple attribute value completion`() {
    XMLLSPTest.apply {
      openFile("../res/layout/AttributeValueCompletion")

      val (isIncomplete, items) = complete()

      assertThat(isIncomplete).isFalse()
      assertThat(items).isNotEmpty()

      assertThat(items).containsAtLeast("center", "fitCenter", "fitXY", "matrix")
    }
  }

  @Test
  fun `test simple attribute completion`() {
    XMLLSPTest.apply {
      openFile("../res/layout/AttributeCompletion")

      val (isIncomplete, items) = complete()

      assertThat(isIncomplete).isFalse()
      assertThat(items).isNotEmpty()

      assertThat(items).containsAtLeast("text", "textColor", "textAlignment", "textAllCaps")
    }
  }

  private fun complete(): Pair<Boolean, List<String>> {
    return XMLLSPTest.run {
      val createCompletionParams = createCompletionParams()
      val result = server.complete(createCompletionParams)
      result.isIncomplete to
        result.items
          .filter { it.label != null }
          .map { it.label.toString() }
          .filter { it.isNotBlank() }
          .toList()
    }
  }

  private fun createCompletionParams(): CompletionParams {
    return XMLLSPTest.run {
      val cursor = cursorPosition(true)
      val completionParams = CompletionParams(cursor, file!!)
      completionParams.position.index = this.cursor
      completionParams.content = contents
      completionParams
    }
  }
}
