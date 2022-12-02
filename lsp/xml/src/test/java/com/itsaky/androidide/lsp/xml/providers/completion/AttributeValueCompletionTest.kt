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
import com.itsaky.androidide.lsp.xml.utils.dimensionUnits
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

  @Test // prefix: '@string/ap'       // for attribute 'android:text'
  // TestAttrValueWithQualifiedRefPrefix
  fun `test value completion with unqualified reference prefix`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrValueWithRefPrefix")
      val (incomplete, items) = complete()
      assertThat(incomplete).isFalse()

      assertThat(items)
        .containsAtLeast(
          "@string/app_name",
          "@string/appbar_scrolling_view_behavior",
          "@android:string/app_category_audio",
          "@android:string/app_category_game",
          "@android:string/app_category_image",
          "@android:string/app_category_maps",
          "@android:string/app_category_news",
          "@android:string/app_category_social",
          "@android:string/app_category_video"
        )
    }
  }

  @Test // prefix: '@android:string/ap'       // for attribute 'android:text'
  fun `test value completion with qualified reference prefix`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrValueWithQualifiedRefPrefix")
      val (incomplete, items) = complete()
      assertThat(incomplete).isFalse()

      assertThat(items).doesNotContain("@string/app_name")
      assertThat(items).doesNotContain("@string/appbar_scrolling_view_behavior")

      assertThat(items)
        .containsAtLeast(
          "@android:string/app_category_audio",
          "@android:string/app_category_game",
          "@android:string/app_category_image",
          "@android:string/app_category_maps",
          "@android:string/app_category_news",
          "@android:string/app_category_social",
          "@android:string/app_category_video"
        )
    }
  }

  @Test // prefix: '@com.itsaky.test.app:string/ap'       // for attribute 'android:text'
  fun `test value completion with qualified reference prefix (test with custom package not android)`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrValueWithQualifiedRefPrefix2")
      val (incomplete, items) = complete()
      assertThat(incomplete).isFalse()

      assertThat(items).contains("@string/app_name")
      assertThat(items).doesNotContain("@string/appbar_scrolling_view_behavior")

      arrayOf(
          "@android:string/app_category_audio",
          "@android:string/app_category_game",
          "@android:string/app_category_image",
          "@android:string/app_category_maps",
          "@android:string/app_category_news",
          "@android:string/app_category_social",
          "@android:string/app_category_video"
        )
        .forEach { assertThat(items).doesNotContain(it) }
    }
  }

  @Test // prefix: '@android:s'       // for attribute 'android:text'
  fun `test value completion with qualified reference prefix with incomplete type`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrValueWithQualifiedRefWithIncompleteType")
      val (incomplete, items) = complete()
      assertThat(incomplete).isFalse()
      assertThat(items).containsAtLeast("string", "style", "styleable")
    }
  }

  @Test // prefix: '@c'       // for attribute 'android:text'
  fun `test value completion with qualified reference prefix with incomplete type or package`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrValueWithQualifiedRefWithIncompleteTypeOrPck")
      val (incomplete, items) = complete()
      assertThat(incomplete).isFalse()
      assertThat(items)
        .containsAtLeast(
          /*resource type*/ "color",
          /*pck*/ "com.google.android.material",
          /*pck*/ "com.itsaky.test.app"
        )
    }
  }

  @Test // prefix: '@com.'       // for attribute 'android:text'
  fun `test value completion with qualified reference prefix with incomplete package`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TestAttrValueWithQualifiedRefWithIncompletePck")
      val (incomplete, items) = complete()
      assertThat(incomplete).isFalse()
      assertThat(items).doesNotContain("color")
      assertThat(items)
        .containsAtLeast(/*pck*/ "com.google.android.material", /*pck*/ "com.itsaky.test.app")
    }
  }
  
  @Test
  fun `test constant dimension values completion`() {
    XMLLSPTest.apply {
      openFile("../res/layout/ConstantDimensionTest")
      val (incomplete, items) = complete()
      assertThat(incomplete).isFalse()
      
      val expected = dimensionUnits.map { "4${it}" }
      assertThat(items).containsAtLeastElementsIn(expected)
    }
  }
}
