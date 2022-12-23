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

package com.itsaky.androidide.language.incremental

import android.graphics.Color
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.editor.schemes.internal.parser.SchemeParser
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme
import java.io.File
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class SchemeParserTest {

  @Test
  fun `test simple parsing`() {
    val parser = SchemeParser()
    val scheme =
      parser.parse(File("./src/main/java/com/itsaky/androidide/editor/schemes/syntax.json"))
    assertThat(scheme).isNotNull()
    assertThat(scheme.isDarkScheme).isFalse()
    assertThat(scheme.definitions).hasSize(12)
    assertThat(scheme.definitions["black"]).isEqualTo(Color.BLACK)
    assertThat(scheme.definitions["white"]).isEqualTo(Color.WHITE)
    assertThat(scheme.definitions["9e"]).isEqualTo(Color.parseColor("#9e9e9e"))

    assertThat(scheme.editorScheme).hasSize(27)
    assertThat(scheme.editorScheme[EditorColorScheme.WHOLE_BACKGROUND]).isEqualTo(Color.parseColor("#212121"))
  }
}
