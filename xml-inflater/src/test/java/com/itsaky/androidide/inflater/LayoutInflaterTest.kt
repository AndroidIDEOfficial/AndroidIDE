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

package com.itsaky.androidide.inflater

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.inflater.internal.LayoutInflaterImpl
import com.itsaky.androidide.inflater.internal.ViewGroupImpl
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.inflater.internal.utils.IDTable
import com.itsaky.androidide.inflater.utils.newAttribute
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.projects.api.AndroidModule
import java.io.File
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class LayoutInflaterTest {

  @Test
  fun `test functionality`() {
    inflaterTest {
      requiresActivity {
        val file = layoutFile("singleView")
        val parent = LinearLayout(this)
        val inflater = ILayoutInflater.newInflater()
        val inflated = inflater.inflate(file, parent)
        inflated.apply {
          assertThat(this).isNotEmpty()
          assertThat(this).hasSize(1)
          forEach { assertThat(it.view).isInstanceOf(View::class.java) }
        }
      }
    }
  }

  @Test
  fun `test inflated view is of expected type`() {
    inflaterTest { module ->
      requiresActivity {
        val parent = LinearLayout(this)
        val inflater = ILayoutInflater.newInflater() as LayoutInflaterImpl
        viewToAdapter.keys.forEach { view ->
          println("Testing layout inflater and adapter for ${view.qualifiedName}")
          parent.removeAllViews()
          module.createLayoutFile(view.simpleName!!) { file ->
            file.writeText(viewDeclTemplate(view.simpleName!!))
            val inflated = inflater.inflate(file, parent)[0]
            assertThat(inflated.view).isInstanceOf(view.java)
            assertThat(inflated.view.id).isEqualTo(IDTable.get(view.simpleName!!, "template_view"))

            (inflated as ViewImpl).attributes.apply {
              assertThat(this)
                .contains(newAttribute(inflated, INamespace.ANDROID, "id", "@+id/template_view"))
              assertThat(this)
                .contains(
                  newAttribute(inflated, INamespace.ANDROID, "layout_height", "match_parent")
                )
              assertThat(this)
                .contains(
                  newAttribute(inflated, INamespace.ANDROID, "layout_width", "match_parent")
                )
            }
          }
        }
      }
    }
  }

  @Test
  fun `verify included view hierarchy`() {
    inflaterTest { module ->
      requiresActivity {
        val parent = LinearLayout(this)
        val inflater = ILayoutInflater.newInflater()
        val inflated = inflater.inflate(layoutFile("include"), parent)
        assertThat(inflated).hasSize(1)

        val view = inflated[0] as ViewGroupImpl
        assertThat(view.printHierarchy())
          .isEqualTo(
            "android.widget.LinearLayout\n" +
              "    android.widget.RelativeLayout\n" +
              "        android.widget.TextView\n" +
              "        android.widget.TextView\n"
          )
        val included = view[0]
        included.findAttribute("layout_height", INamespace.ANDROID.uri).apply {
          assertThat(this).isNotNull()
          assertThat(this!!.value).isEqualTo("48dp")
        }
        included.findAttribute("layout_width", INamespace.ANDROID.uri).apply {
          assertThat(this).isNotNull()
          assertThat(this!!.value).isEqualTo("48dp")
        }
        assertThat(included.hasAttribute("gravity", INamespace.ANDROID.uri)).isTrue()
        assertThat(included.hasAttribute("id", INamespace.ANDROID.uri)).isTrue()
      }
    }
  }

  @Test
  fun `verify merged view hierarchy`() {
    inflaterTest { module ->
      requiresActivity {
        val parent = LinearLayout(this)
        val inflater = ILayoutInflater.newInflater(module)
        val inflated = inflater.inflate(layoutFile("merge"), parent)
        assertThat(inflated).hasSize(1)

        val view = inflated[0] as ViewGroupImpl
        assertThat(view.printHierarchy())
          .isEqualTo(
            "android.widget.LinearLayout\n" +
              "    android.widget.TextView\n" +
              "    android.widget.TextView\n"
          )

        assertThat(view.childCount).isEqualTo(2)

        // attributes on <include> tag must be ignored
        for (i in 0 until view.childCount) {
          assertThat(view[i].hasAttribute("clickable", INamespace.ANDROID.uri)).isFalse()
        }
      }
    }
  }

  @Test
  fun `test unsupported views`() {
    inflaterTest {
      requiresActivity {
        val parent = LinearLayout(this)
        val inflater = ILayoutInflater.newInflater(it)
        val inflated = inflater.inflate(layoutFile("unsupported_views"), parent)

        assertThat(inflated).hasSize(1)

        val root = inflated[0] as ViewGroupImpl

        // verity the inflated layout hierarchy
        // this takes care of verifying that the generated XML elements will have proper XML tagsl
        assertThat(root.printHierarchy())
          .isEqualTo(
            "android.widget.LinearLayout\n" +
              "    com.itsaky.androidide.inflater.unsupported.UnsupportedView\n" +
              "    com.itsaky.androidide.inflater.unsupported.UnsupportedLayout\n" +
              "    com.itsaky.androidide.inflater.unsupported.UnsupportedLayout\n" +
              "        android.widget.ImageView\n" +
              "        android.widget.ImageView\n" +
              "    com.itsaky.androidide.inflater.unsupported.UnsupportedLayout\n" +
              "        com.itsaky.androidide.inflater.unsupported.UnsupportedView\n" +
              "        com.itsaky.androidide.inflater.unsupported.UnsupportedView\n"
          )

        // TextView is used to inflate unsupported views
        assertThat(root[0].view).isInstanceOf(TextView::class.java)

        // TextView is also used for layouts which do not have any child viewsl
        assertThat(root[1].view).isInstanceOf(TextView::class.java)

        // FrameLayout is used to inflate unsupported layouts with child views
        assertThat(root[2].view).isInstanceOf(FrameLayout::class.java)
        assertThat(root[3].view).isInstanceOf(FrameLayout::class.java)

        assertThat((root[2] as ViewGroupImpl)[0].view).isInstanceOf(ImageView::class.java)
        assertThat((root[2] as ViewGroupImpl)[1].view).isInstanceOf(ImageView::class.java)

        // Unsupported child views
        assertThat((root[3] as ViewGroupImpl)[0].view).isInstanceOf(TextView::class.java)
        assertThat((root[3] as ViewGroupImpl)[1].view).isInstanceOf(TextView::class.java)
      }
    }
  }

  private fun layoutFile(name: String): File {
    val app = ProjectManager.app ?: throw IllegalStateException("Project is not initialized")
    return File(app.projectDir, "src/main/res/layout/$name.xml")
  }

  private fun AndroidModule.createLayoutFile(name: String, block: (File) -> Unit = {}): File {
    return File(projectDir, "src/main/res/layout/$name.xml").apply {
      if (exists()) {
        delete()
      }
      block(this)
    }
  }
}
