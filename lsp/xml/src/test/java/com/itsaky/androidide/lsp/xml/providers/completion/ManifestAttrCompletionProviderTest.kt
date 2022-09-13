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
class ManifestAttrCompletionProviderTest : CompletionHelper by CompletionHelperImpl() {

  @Before fun setup() = XMLLSPTest.initProjectIfNeeded()

  @Test // prefix: 'version'
  fun `test manifest tag attributes`() {
    XMLLSPTest.apply {
      openFile("completion/ManifestAttrTest")
      val (incomplete, items) = complete()
      assertThat(incomplete).isFalse()
      assertThat(items)
        .containsAtLeast("android:versionName", "android:versionCode", "android:versionCodeMajor")
    }
  }

  @Test // prefix: 'allow'
  fun `test manifest application tag attributes`() {
    XMLLSPTest.apply {
      openFile("completion/ManifestApplicationAttrTest")
      val (incomplete, items) = complete()
      assertThat(incomplete).isFalse()
      assertThat(items)
        .containsAtLeast(
          "android:allowClearUserData",
          "android:allowClearUserDataOnFailedRestore",
          "android:allowAudioPlaybackCapture",
          "android:allowNativeHeapPointerTagging",
          "android:allowAutoRevokePermissionsExemption"
        )
    }
  }
}
