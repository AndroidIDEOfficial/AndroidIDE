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

package com.itsaky.androidide.xml.versions

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.xml.findAndroidJar
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class ApiVersionsRegistryTest {

  @Test
  fun test() {
    val androidJar = findAndroidJar()
    val registry = ApiVersionsRegistry.getInstance()
    val versions = registry.forPlatformDir(androidJar.parentFile!!)

    assertThat(versions).isNotNull()
    versions!!.getClass("android.Manifest\$permission").apply {
      assertThat(this).isNotNull()
      this!!.getField("WRITE_EXTERNAL_STORAGE").apply {
        assertThat(this).isNotNull()
        assertThat(this!!.since).isEqualTo(4)
        assertThat(this.removed).isEqualTo(-1)
        assertThat(this.deprecated).isEqualTo(-1)
      }

      this.getField("USE_BIOMETRIC").apply {
        assertThat(this).isNotNull()
        assertThat(this!!.since).isEqualTo(28)
        assertThat(this.removed).isEqualTo(-1)
        assertThat(this.deprecated).isEqualTo(-1)
      }

      this.getField("PERSISTENT_ACTIVITY").apply {
        assertThat(this).isNotNull()
        assertThat(this!!.since).isEqualTo(-1)
        assertThat(this.removed).isEqualTo(-1)
        assertThat(this.deprecated).isEqualTo(15)
      }

      this.getMethod("nonExistentMethod", "i.do.not.exist").apply { assertThat(this).isNull() }
      this.getField("nonExistentField").apply { assertThat(this).isNull() }
    }

    versions.getClass("android.view.View").apply {
      assertThat(this).isNotNull()
      this!!.getField("SYSTEM_UI_FLAG_FULLSCREEN").apply {
        assertThat(this).isNotNull()
        assertThat(this!!.since).isEqualTo(16)
        assertThat(this.removed).isEqualTo(-1)
        assertThat(this.deprecated).isEqualTo(30)
      }

      this.getMethod(
          "setOnSystemUiVisibilityChangeListener",
          "android.view.View\$OnSystemUiVisibilityChangeListener"
        )
        .apply {
          assertThat(this).isNotNull()
          assertThat(this!!.since).isEqualTo(11)
          assertThat(this.removed).isEqualTo(-1)
          assertThat(this.deprecated).isEqualTo(30)
        }

      this.getMethod("nonExistentMethod", "i.do.not.exist").apply { assertThat(this).isNull() }
    }
  }
}
