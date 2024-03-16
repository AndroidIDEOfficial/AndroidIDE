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

package com.itsaky.androidide.xml.widgets

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.xml.findAndroidJar
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class WidgetTableRegistryTest {

  @Test
  fun `test multiple calls should returns same instance`() {
    val (platformDir, registry, table) = createTable()
    assertThat(registry.forPlatformDir(platformDir)).isEqualTo(table)
  }

  @Test
  fun `test simple layout retrieval`() {
    val (_, _, table) = createTable()
    table.getWidget("android.widget.FrameLayout").apply {
      assertThat(this).isNotNull()
      assertThat(this!!.qualifiedName).isEqualTo("android.widget.FrameLayout")
      assertThat(this.simpleName).isEqualTo("FrameLayout")
      assertThat(this.type).isEqualTo(WidgetType.LAYOUT)
    }
  }

  @Test
  fun `test table contains layout params`() {
    val (_, _, table) = createTable()
    table.getWidget("android.widget.FrameLayout.LayoutParams").apply {
      assertThat(this).isNotNull()
      assertThat(this!!.qualifiedName).isEqualTo("android.widget.FrameLayout.LayoutParams")
      assertThat(this.simpleName).isEqualTo("LayoutParams")
      assertThat(this.type).isEqualTo(WidgetType.LAYOUT_PARAM)
    }
  }

  @Test
  fun `test find with simple name`() {
    val (_, _, table) = createTable()
    table.findWidgetWithSimpleName("TextView").apply {
      assertThat(this).isNotNull()
      assertThat(this!!.simpleName).isEqualTo("TextView")
      assertThat(this.qualifiedName).isEqualTo("android.widget.TextView")
      assertThat(this.superclasses).containsExactly("android.view.View", "java.lang.Object")
    }
  }

  @Test
  fun `test get all widgets list`() {
    val (_, _, table) = createTable()
    assertThat(table.getAllWidgets().map(Widget::simpleName))
      .containsAtLeast(
        "TextView",
        "ImageView",
        "Button",
        "LinearLayout",
        "RelativeLayout",
        "FrameLayout",
        "GestureOverlayView",
        "CalendarView"
      )
  }

  private fun createTable(): Triple<File, WidgetTableRegistry, WidgetTable> {
    val platformDir = findAndroidJar().parentFile!!
    val registry = WidgetTableRegistry.getInstance()
    val table = registry.forPlatformDir(platformDir)

    assertThat(table).isNotNull()
    return Triple(platformDir, registry, table!!)
  }
}
