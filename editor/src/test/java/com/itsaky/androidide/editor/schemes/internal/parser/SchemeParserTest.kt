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

package com.itsaky.androidide.editor.schemes.internal.parser

import android.graphics.Color
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.editor.schemes.IDEColorScheme
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class SchemeParserTest {

  @Test
  fun `test simple parsing`() {
    val scheme = parseSyntaxJson()
    assertThat(scheme.isDarkScheme).isFalse()
    assertThat(scheme.definitions).hasSize(12)
    assertThat(scheme.colorIds[scheme.definitions["black"] ?: -1]).isEqualTo(Color.BLACK)
    assertThat(scheme.colorIds[scheme.definitions["white"] ?: -1]).isEqualTo(Color.WHITE)
    assertThat(scheme.colorIds[scheme.definitions["9e"] ?: -1]).isEqualTo(
      Color.parseColor("#9e9e9e"))

    assertThat(scheme.editorScheme.size).isEqualTo(27)
    assertThat(scheme.editorScheme[EditorColorScheme.WHOLE_BACKGROUND])
      .isEqualTo(Color.parseColor("#212121"))
  }

  @Test
  fun `test color scheme definitions as objects and color values`() {
    val scheme = parseSyntaxJson()
    scheme.getLanguageScheme("@log").apply {
      assertThat(this).isNotNull()
      assertThat(this!!.styles).isNotEmpty()

      this.styles["text.error"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.parseColor("#f44336"))
      }
      this.styles["text.warning"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.parseColor("#ffeb3b"))
      }
      this.styles["text.debug"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.parseColor("#f5f5f5"))
        assertThat(scheme.colorIds[this.fg]).isEqualTo(
          scheme.colorIds[scheme.definitions["f5"] ?: -1])
      }
      this.styles["priority.debug"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.WHITE)
        assertThat(scheme.colorIds[this.fg]).isEqualTo(
          scheme.colorIds[scheme.definitions["white"] ?: -1])
        assertThat(scheme.colorIds[this.bg]).isEqualTo(Color.parseColor("#9e9d24"))
        assertThat(this.bold || this.italic || this.strikeThrough).isFalse()
        assertThat(this.completion).isTrue()
      }
      this.styles["priority.error"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.BLACK)
        assertThat(scheme.colorIds[this.fg]).isEqualTo(
          scheme.colorIds[scheme.definitions["black"] ?: -1])
        assertThat(scheme.colorIds[this.bg]).isEqualTo(Color.parseColor("#f44336"))
        assertThat(this.bold || this.italic || this.strikeThrough).isFalse()
        assertThat(this.completion).isFalse()
      }
    }
  }

  @Test
  fun `test local scopes, defs and refs`() {
    val scheme = parseSyntaxJson()
    scheme.getLanguageScheme("java").apply {
      assertThat(this).isNotNull()
      assertThat(this!!.localDefs).containsExactly("definition.var", "definition.field")
      assertThat(this.localRefs).containsExactly("reference")
      assertThat(this.localScopes).containsExactly("scope")
      assertThat(this.localDefVals).isEmpty()
    }
  }

  @Test
  fun `test language file references`() {
    val scheme = parseSyntaxJson("lang-refs.json")
    scheme.getLanguageScheme("@log").apply {
      assertThat(this).isNotNull()
      assertThat(this!!.styles).isNotEmpty()

      this.styles["text.error"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.parseColor("#f44336"))
      }
      this.styles["text.warning"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.parseColor("#ffeb3b"))
      }
      this.styles["text.debug"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.parseColor("#f5f5f5"))
        assertThat(scheme.colorIds[this.fg]).isEqualTo(
          scheme.colorIds[scheme.definitions["f5"] ?: -1])
      }
      this.styles["priority.debug"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.WHITE)
        assertThat(scheme.colorIds[this.fg]).isEqualTo(
          scheme.colorIds[scheme.definitions["white"] ?: -1])
        assertThat(scheme.colorIds[this.bg]).isEqualTo(Color.parseColor("#9e9d24"))
        assertThat(this.bold || this.italic || this.strikeThrough).isFalse()
        assertThat(this.completion).isTrue()
      }
      this.styles["priority.error"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.BLACK)
        assertThat(scheme.colorIds[this.fg]).isEqualTo(
          scheme.colorIds[scheme.definitions["black"] ?: -1])
        assertThat(scheme.colorIds[this.bg]).isEqualTo(Color.parseColor("#f44336"))
        assertThat(this.bold || this.italic || this.strikeThrough).isFalse()
        assertThat(this.completion).isFalse()
      }
    }
    scheme.getLanguageScheme("java").apply {
      assertThat(this).isNotNull()
      assertThat(this!!.localDefs).containsExactly("definition.var", "definition.field")
      assertThat(this.localRefs).containsExactly("reference")
      assertThat(this.localScopes).containsExactly("scope")
      assertThat(this.localDefVals).isEmpty()
    }
  }

  @Test
  fun `test lang ref with lang object`() {
    val scheme = parseSyntaxJson("lang-ref-with-object.json")
    scheme.getLanguageScheme("@log").apply {
      assertThat(this).isNotNull()
      assertThat(this!!.styles).isNotEmpty()

      this.styles["text.error"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.parseColor("#f44336"))
      }
      this.styles["text.warning"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.parseColor("#ffeb3b"))
      }
      this.styles["text.debug"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.parseColor("#f5f5f5"))
        assertThat(scheme.colorIds[this.fg]).isEqualTo(
          scheme.colorIds[scheme.definitions["f5"] ?: -1])
      }
      this.styles["priority.debug"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.WHITE)
        assertThat(scheme.colorIds[this.fg]).isEqualTo(
          scheme.colorIds[scheme.definitions["white"] ?: -1])
        assertThat(scheme.colorIds[this.bg]).isEqualTo(Color.parseColor("#9e9d24"))
        assertThat(this.bold || this.italic || this.strikeThrough).isFalse()
        assertThat(this.completion).isTrue()
      }
      this.styles["priority.error"].apply {
        assertThat(this).isNotNull()
        assertThat(scheme.colorIds[this!!.fg]).isEqualTo(Color.BLACK)
        assertThat(scheme.colorIds[this.fg]).isEqualTo(
          scheme.colorIds[scheme.definitions["black"] ?: -1])
        assertThat(scheme.colorIds[this.bg]).isEqualTo(Color.parseColor("#f44336"))
        assertThat(this.bold || this.italic || this.strikeThrough).isFalse()
        assertThat(this.completion).isFalse()
      }
    }
    scheme.getLanguageScheme("java").apply {
      assertThat(this).isNotNull()
      assertThat(this!!.localDefs).containsExactly("definition.var", "definition.field")
      assertThat(this.localRefs).containsExactly("reference")
      assertThat(this.localScopes).containsExactly("scope")
      assertThat(this.localDefVals).isEmpty()
    }
  }

  @Test
  fun `test editor file ref parsing`() {
    val scheme = parseSyntaxJson("editor-ref.json")
    assertThat(scheme.isDarkScheme).isFalse()
    assertThat(scheme.definitions).hasSize(12)
    assertThat(scheme.colorIds[scheme.definitions["black"] ?: -1]).isEqualTo(Color.BLACK)
    assertThat(scheme.colorIds[scheme.definitions["white"] ?: -1]).isEqualTo(Color.WHITE)
    assertThat(scheme.colorIds[scheme.definitions["9e"] ?: -1]).isEqualTo(
      Color.parseColor("#9e9e9e"))

    assertThat(scheme.editorScheme.size).isEqualTo(27)
    assertThat(scheme.editorScheme[EditorColorScheme.WHOLE_BACKGROUND])
      .isEqualTo(Color.parseColor("#212121"))
  }

  private fun parseSyntaxJson(name: String = "syntax.json"): IDEColorScheme {
    val basePath = "./src/test/resources"
    val parser = SchemeParser { File("$basePath/$it") }
    val scheme = parser.parse(File("$basePath/$name"), "AndroidIDE Test Scheme", false)
    assertThat(scheme).isNotNull()
    return scheme
  }
}