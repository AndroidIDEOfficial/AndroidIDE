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
class ManifestAttrValueCompletionProviderTest : CompletionHelper by CompletionHelperImpl() {

  @Before fun setup() = XMLLSPTest.initProjectIfNeeded()

  @Test
  fun `simple value completion test`() {
    XMLLSPTest.apply {
      openFile("completion/ManifestAttrValueTest")
      val (incomplete, items) = complete()
      assertThat(incomplete).isFalse()
      assertThat(items)
        .containsAtLeast(
          "game",
          "audio",
          "video",
          "image",
          "social",
          "news",
          "maps",
          "productivity",
          "accessibility"
        )
    }
  }

  @Test // prefix: 'M'
  fun `activity action value completion test`() {
    XMLLSPTest.apply {
      assertHasSingleLineEntries(
        "ManifestActivityActionCompletionTest",
        arrayOf(
          "android.intent.action.MAIN",
          "android.intent.action.MANAGE_APP_PERMISSION",
          "android.intent.action.MANAGE_APP_PERMISSIONS"
        )
      )
    }
  }

  @Test // prefix: 'M'
  fun `receiver action value completion test`() {
    XMLLSPTest.apply {
      assertHasSingleLineEntries(
        "ManifestReceiverActionCompletionTest",
        arrayOf(
          "android.intent.action.MANAGE_PACKAGE_STORAGE",
          "android.intent.action.MASTER_CLEAR_NOTIFICATION",
          "android.intent.action.MEDIA_BAD_REMOVAL",
          "android.intent.action.MEDIA_BUTTON",
          "android.intent.action.MEDIA_CHECKING",
          "android.intent.action.MEDIA_EJECT",
          "android.intent.action.MEDIA_MOUNTED",
          "android.intent.action.MEDIA_NOFS",
          "android.intent.action.MEDIA_REMOVED",
          "android.intent.action.MEDIA_SCANNER_FINISHED",
          "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
          "android.intent.action.MEDIA_SCANNER_STARTED",
          "android.intent.action.MEDIA_SHARED",
          "android.intent.action.MEDIA_UNMOUNTABLE",
          "android.intent.action.MEDIA_UNMOUNTED",
          "android.intent.action.MY_PACKAGE_REPLACED",
          "android.intent.action.MY_PACKAGE_SUSPENDED"
        )
      )
    }
  }

  @Test // prefix: 'M'
  fun `service action value completion test`() {
    XMLLSPTest.apply {
      assertHasSingleLineEntries(
        "ManifestServiceActionCompletionTest",
        arrayOf(
          "android.media.MediaRoute2ProviderService",
          "android.media.browse.MediaBrowserService"
        )
      )
    }
  }

  @Test // prefix: 'A'
  fun `category value completion test`() {
    XMLLSPTest.apply {
      assertHasSingleLineEntries(
        "ManifestCategoryCompletionTest",
        arrayOf(
          "android.intent.category.ACCESSIBILITY_SHORTCUT_TARGET",
          "android.intent.category.ALTERNATIVE",
          "android.intent.category.APP_BROWSER",
          "android.intent.category.APP_CALCULATOR",
          "android.intent.category.APP_CALENDAR",
          "android.intent.category.APP_CONTACTS",
          "android.intent.category.APP_EMAIL",
          "android.intent.category.APP_FILES",
          "android.intent.category.APP_GALLERY",
          "android.intent.category.APP_MAPS",
          "android.intent.category.APP_MARKET",
          "android.intent.category.APP_MESSAGING",
          "android.intent.category.APP_MUSIC"
        )
      )
    }
  }

  @Test // prefix: 'f'
  fun `feature value completion test`() {
    XMLLSPTest.apply {
      assertHasSingleLineEntries(
        "ManifestFeatureCompletionTest",
        arrayOf(
          "android.hardware.faketouch",
          "android.hardware.fingerprint",
          "android.software.file_based_encryption",
          "android.software.freeform_window_management"
        )
      )
    }
  }
  
  @Test
  fun `permission value completion test`() {
    XMLLSPTest.apply {
      assertHasSingleLineEntries(
        "ManifestPermissionCompletionTest",
        arrayOf(
          "WRITE_EXTERNAL_STORAGE",
          "WAKE_LOCK",
          "WRITE_CONTACTS",
          "WRITE_SETTINGS",
          "WRITE_SECURE_SETTINGS",
          "WRITE_VOICEMAIL"
        )
      )
    }
  }
  
  @Test // prefix: 'ic_l'
  fun `resource references value completion test`() {
    XMLLSPTest.apply {
      assertHasSingleLineEntries(
        "ManifestResourceReferenceCompletionTest",
        arrayOf(
          "@drawable/ic_launcher_background",
          "@drawable/ic_launcher_foreground"
        )
      )
    }
  }
  
  private fun XMLLSPTest.assertHasSingleLineEntries(file: String, expect: Array<String>) {
    openFile("completion/$file")
    val (_, items) = complete()
    assertThat(items).containsAtLeastElementsIn(expect)
  }
}
