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

/** @author Akash Yadav */
class JavaCompletionProviderTester : JavaLSPTest() {

  override fun test() {
    locals()
    members()
    staticAccess()
    lambdaVariableMemberAccess()
  }

  fun locals() {
    openFile("LocalsCompletionTest")

    val pos = cursorPosition()
    val items = completionTitles(pos)
    assertThat(items).containsAtLeast("aaString", "aaInt", "aaFloat", "aaDouble", "args")
  }

  fun members() {
    // Complete members of String
    openFile("MembersCompletionTest")

    val pos = cursorPosition()
    val items = completionTitles(pos)
    assertThat(items)
      .containsAtLeast("getClass", "toLowerCase", "toUpperCase", "substring", "charAt")
  }

  fun lambdaVariableMemberAccess() {
    // Complete members of Throwable
    openFile("LambdaMembersCompletionTest")

    val pos = cursorPosition()
    val items = completionTitles(pos)
    assertThat(items).containsAtLeast("getMessage", "getCause", "getStackTrace", "printStackTrace")
  }

  fun staticAccess() {
    // Complete static members of String
    openFile("StaticMembersCompletionTest")

    val pos = cursorPosition()
    val items = completionTitles(pos)
    assertThat(items)
      .containsAtLeast("format", "join", "valueOf", "CASE_INSENSITIVE_ORDER", "class")
  }

  override fun openFile(fileName: String) {
    super.openFile("completion/${fileName}")
  }

  private fun completionTitles(pos: com.itsaky.androidide.models.Position): List<CharSequence> {
    return server.complete(CompletionParams(pos, file!!).apply { prefix = "" }).items.map {
      it.label
    }
  }
}
