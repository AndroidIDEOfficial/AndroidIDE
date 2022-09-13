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

package com.itsaky.androidide.lsp.xml.providers.completion

import com.android.aaptcompiler.AaptResourceType.BOOL
import com.android.aaptcompiler.AaptResourceType.DIMEN
import com.android.aaptcompiler.AaptResourceType.INTEGER
import com.android.aaptcompiler.AaptResourceType.MENU
import com.android.aaptcompiler.AaptResourceType.STRING
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lsp.xml.CompletionHelper
import com.itsaky.androidide.lsp.xml.CompletionHelperImpl
import com.itsaky.androidide.lsp.xml.XMLLSPTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class AttributeValueCompletionTest : CompletionHelper by CompletionHelperImpl() {

  @Before
  fun setup() {
    XMLLSPTest.initProjectIfNeeded()
  }

  @Test // prefix: '' (empty)
  fun `values must be completed according to the attribute format`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrsValue")

      val (isIncomplete, items) = complete()

      assertThat(isIncomplete).isTrue()
      assertThat(items).isNotEmpty()

      // Check if all the expected color values are present
      assertThat(items)
        .containsAtLeast(
          "@color/abc_background_cache_hint_selector_material_dark",
          "@color/abc_background_cache_hint_selector_material_light",
          "@color/abc_btn_colored_borderless_text_material",
          "@color/abc_btn_colored_text_material",
          "@color/abc_color_highlight_material"
        )

      // Check that we do not have any other type of values
      for (type in setOf(STRING, INTEGER, BOOL, DIMEN, INTEGER, MENU)) {
        assertThat(items.firstOrNull { it.contains(type.tagName) }).isNull()
      }
    }
  }

  @Test // prefix: '' (empty)        // for attribute 'material:layout_constraintStart_toStartOf'
  fun `values must be completed from defined namespaces as well`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrValueFromNamespace")

      val (isIncomplete, items) = complete()

      assertThat(isIncomplete).isTrue()
      assertThat(items).isNotEmpty()

      assertThat(items).contains("parent")
    }
  }

  @Test // prefix: '' (empty)        // for attribute 'material:layout_constraintStart_toStartOf'
  fun `values must be completed from defined auto namespace as well`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrValueFromNamespaceAuto")

      val (isIncomplete, items) = complete()

      assertThat(isIncomplete).isTrue()
      assertThat(items).isNotEmpty()

      assertThat(items).contains("parent")
    }
  }
}
