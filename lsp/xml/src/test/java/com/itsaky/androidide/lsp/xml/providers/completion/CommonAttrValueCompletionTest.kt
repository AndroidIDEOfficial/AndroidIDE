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

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lsp.xml.CompletionHelper
import com.itsaky.androidide.lsp.xml.CompletionHelperImpl
import com.itsaky.androidide.lsp.xml.XMLLSPTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CommonAttrValueCompletionTest : CompletionHelper by CompletionHelperImpl() {

  @Before fun setup() = XMLLSPTest.initProjectIfNeeded()

  @Test
  fun `test simple drawable attr value completion`() {
    XMLLSPTest.apply {
      openFile("../res/drawable/TestDrawableAttrValue")
      val (incomplete, items) = complete()
      assertThat(incomplete).isFalse()
      assertThat(items).containsAtLeast("rectangle", "oval", "line", "ring")
    }
  }

  @Test // prefix: '' in a <set> tag
  fun `test simple anim attributes`() {
    XMLLSPTest.apply {
      openFile("../res/anim/TestSimpleAttrValue")
      val (_, items) = complete()
      // Only integer values
      assertThat(items.filter { it.startsWith("@integer/") || it.startsWith("@android:integer/") })
        .hasSize(items.size)
    }
  }

  @Test // prefix: '' in a <arcMotion> tag for android:duration attr
  fun `test simple transition attributes`() {
    XMLLSPTest.apply {
      openFile("../res/transition/TestSimpleAttrValue")
      val (_, items) = complete()
      // Only integer values
      assertThat(items.filter { it.startsWith("@integer/") || it.startsWith("@android:integer/") })
        .hasSize(items.size)
    }
  }
}
