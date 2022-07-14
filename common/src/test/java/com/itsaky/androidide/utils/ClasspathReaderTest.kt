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

package com.itsaky.androidide.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.File
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_VALUE_STRING)
class ClasspathReaderTest {

  @Test
  fun testListClasses() {
    val classes =
      com.itsaky.androidide.utils.ClasspathReader.listClasses(
        listOf(File("../tests/test-project/app/src/main/resources/android.jar"))
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
