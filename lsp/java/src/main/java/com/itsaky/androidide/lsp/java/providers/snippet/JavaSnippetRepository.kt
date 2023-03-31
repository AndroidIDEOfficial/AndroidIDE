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

package com.itsaky.androidide.lsp.java.providers.snippet

/**
 * Repository to store various snippets for Java.
 *
 * @author Akash Yadav
 */
object JavaSnippetRepository {

  val snippets: Map<JavaSnippetScope, List<JavaSnippet>> by lazy { createSnippets() }

  private fun createSnippets(): Map<JavaSnippetScope, List<JavaSnippet>> {
    return mutableMapOf<JavaSnippetScope, List<JavaSnippet>>().apply {
      this[JavaSnippetScope.GLOBAL] = createGlobalSnippets()
      this[JavaSnippetScope.TOP_LEVEL] = createTopLevelSnippets()
      this[JavaSnippetScope.MEMBER] = createMemberSnippets()
      this[JavaSnippetScope.LOCAL] = createLocalSnippets()
    }
  }

  private fun createGlobalSnippets(): List<JavaSnippet> {
    return listOf()
  }

  private fun createTopLevelSnippets(): List<JavaSnippet> {
    return listOf()
  }

  private fun createMemberSnippets(): List<JavaSnippet> {
    return listOf()
  }

  private fun createLocalSnippets(): List<JavaSnippet> {
    return listOf(
      JavaSnippet("for", "Indexed for loop") {
        arrayOf("for (int \${1:i} = 0; \$1 < \${2:count}; ++\$1) {", "\t\$0", "}")
      },
      JavaSnippet("forr", "Reverse-indexed for loop") {
        arrayOf("for (int \${1:i} = \${2:count} - 1; \$1 >= 0; --\$1) {", "\t\$0", "}")
      }
    )
  }
}
