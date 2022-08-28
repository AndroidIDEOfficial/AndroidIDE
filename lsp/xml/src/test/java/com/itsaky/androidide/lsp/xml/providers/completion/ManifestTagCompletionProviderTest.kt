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

/**
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ManifestTagCompletionProviderTest : CompletionHelper by CompletionHelperImpl() {
  
  @Before
  fun setup () = XMLLSPTest.initProjectIfNeeded()
  
  @Test // prefix: 'perm'
  fun `simple tag completion test with prefix 'perm'` () {
    XMLLSPTest.apply {
      openFile("completion/ManifestPermTagTest")
      val (isIncomplete, items) = complete()
      assertThat(isIncomplete).isFalse()
      assertThat(items).containsAtLeast("permission", "permission-group", "permission-tree")
    }
  }
  
  @Test // prefix: 'uses'
  fun `simple tag completion test with prefix 'uses'` () {
    XMLLSPTest.apply {
      openFile("completion/ManifestUsesTagTest")
      val (isIncomplete, items) = complete()
      assertThat(isIncomplete).isFalse()
      assertThat(items).containsAtLeast("uses-permission", "uses-sdk", "uses-configuration", "uses-feature")
    }
  }
}