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

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class XMLCompletionProviderTest : CompletionHelper by CompletionHelperImpl() {

  @Before
  fun setup() {
    XMLLSPTest.initProjectIfNeeded()
  }

  @Test
  fun `test simple tag completion`() {
    XMLLSPTest.apply {
      openFile("../res/layout/TagCompletion")

      val (isIncomplete, items) = complete()

      assertThat(isIncomplete).isTrue()
      assertThat(items).isNotEmpty()

      assertThat(items).containsAtLeast("ImageView", "ImageButton")
    }
  }
  
  @Test
  fun `test empty tag completion`() {
    XMLLSPTest.apply {
      openFile("../res/layout/EmptyTagNameCompletion")
      
      val (isIncomplete, items) = complete()
      
      assertThat(isIncomplete).isTrue()
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

      assertThat(items).containsAtLeast("android:text", "android:textColor", "android:textAlignment", "android:textAllCaps")
    }
  }
}
