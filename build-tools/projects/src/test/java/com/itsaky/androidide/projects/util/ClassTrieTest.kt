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

package com.itsaky.androidide.projects.util

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.utils.ClassTrie
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ClassTrieTest {

  @Test
  fun testAppend() {
    val trie = ClassTrie()
    trie.append("java.lang.String")
    trie.append("java.lang.String.Inner")
    trie.append("java.lang.Object")
    trie.append("java.util.List")
    trie.append("java.io.File")
    trie.append("java.nio.file.Path")

    assertThat(trie.findClassNames("java.lang"))
      .containsExactly("java.lang.String", "java.lang.String.Inner", "java.lang.Object")
    assertThat(trie.findInPackage("java.lang").map { it.isClass }).containsExactly(true, true, true)
    assertThat(trie.findClassNames("java.util")).containsExactly("java.util.List")
    assertThat(trie.findClassNames("java.nio")).containsExactly("java.nio.file.Path")
  }

  @Test
  fun testRemoval() {
    val trie = ClassTrie()
    trie.append("java.lang.String")
    trie.append("java.lang.Object")
    trie.append("java.util.List")
    trie.append("java.io.File")
    trie.append("java.nio.file.Path")

    assertThat(trie.allClassNames())
      .containsExactly(
        "java.lang.String",
        "java.lang.Object",
        "java.util.List",
        "java.io.File",
        "java.nio.file.Path"
      )

    assertThat(trie.contains("java.io.File")).isTrue()
    assertThat(trie.contains("java.nio.file.Path")).isTrue()

    trie.remove("java.lang.String")
    trie.remove("java.nio.file.Path")

    assertThat(trie.allClassNames())
      .containsExactly("java.lang.Object", "java.util.List", "java.io.File")

    assertThat(trie.contains("java.io.File")).isTrue()
    assertThat(trie.contains("java.nio.file.Path")).isFalse()
    assertThat(trie.contains("java.lang.String")).isFalse()

    assertThat(trie.findClassNames("java.lang")).containsExactly("java.lang.Object")
    assertThat(trie.findClassNames("java.util")).containsExactly("java.util.List")
    assertThat(trie.findClassNames("java.nio")).isEmpty()
  }
}
