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
import com.itsaky.androidide.lsp.java.JavaLSPTest
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.progress.ICancelChecker
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class JavaCompletionProviderTest {

  @Before
  fun setup() {
    JavaLSPTest.setup()
  }

  @Test
  fun locals() {
    JavaLSPTest.apply {
      openFile("completion/LocalsCompletionTest")

      val pos = cursorPosition()
      val items = completionTitles(pos)
      assertThat(items).containsAtLeast("aaString", "aaInt", "aaFloat", "aaDouble", "args")
    }
  }

  fun members() {
    JavaLSPTest.apply {
      // Complete members of String
      openFile("completion/MembersCompletionTest")

      val pos = cursorPosition()
      val items = completionTitles(pos)
      assertThat(items)
        .containsAtLeast("getClass", "toLowerCase", "toUpperCase", "substring", "charAt")
    }
  }

  @Test
  fun lambdaVariableMemberAccess() {
    JavaLSPTest.apply {
      // Complete members of Throwable
      openFile("completion/LambdaMembersCompletionTest")

      val pos = cursorPosition()
      val items = completionTitles(pos)
      assertThat(items)
        .containsAtLeast("getMessage", "getCause", "getStackTrace", "printStackTrace")
    }
  }

  @Test
  fun staticAccess() {
    JavaLSPTest.apply {
      // Complete static members of String
      openFile("completion/StaticMembersCompletionTest")

      val pos = cursorPosition()
      val items = completionTitles(pos)
      assertThat(items)
        .containsAtLeast("format", "join", "valueOf", "CASE_INSENSITIVE_ORDER", "class")
    }
  }

  private fun completionTitles(pos: Position): List<CharSequence> {
    return JavaLSPTest.server
      .complete(
        CompletionParams(pos, JavaLSPTest.file!!, ICancelChecker.NOOP).apply { prefix = "" })
      .items
      .map { it.ideLabel }
  }
}
