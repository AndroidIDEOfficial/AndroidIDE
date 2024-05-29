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

package com.itsaky.androidide.projects.classpath

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.utils.FileProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_VALUE_STRING)
class ZipFileClasspathReaderTest {

  @Test
  fun testListClasses() {
    val classes =
      ZipFileClasspathReader()
        .listClasses(
          listOf(
            FileProvider.testProjectRoot().resolve("app/src/main/resources/android.jar").toFile()
          )
        )

    val context = classes.firstOrNull { it.name == "android.content.Context" }
    assertThat(context).isNotNull()
    assertThat(context!!.isTopLevel).isTrue()
    assertThat(context.isAnonymous).isFalse()
    assertThat(context.isLocal).isFalse()
    assertThat(context.isInner).isFalse()
    assertThat(context.simpleName).isEqualTo("Context")
    assertThat(context.packageName).isEqualTo("android.content")

    val clickListener = classes.firstOrNull { it.name == "android.view.View\$OnClickListener" }
    assertThat(clickListener).isNotNull()
    assertThat(clickListener!!.isTopLevel).isFalse()
    assertThat(clickListener.isAnonymous).isFalse()
    assertThat(clickListener.isLocal).isFalse()
    assertThat(clickListener.isInner).isTrue()
    assertThat(clickListener.simpleName).isEqualTo("OnClickListener")
    assertThat(clickListener.packageName).isEqualTo("android.view")
  }
}
