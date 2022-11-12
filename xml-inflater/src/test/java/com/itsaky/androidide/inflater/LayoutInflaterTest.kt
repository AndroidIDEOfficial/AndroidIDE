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
import android.widget.LinearLayout
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.inflater.internal.AttributeImpl
import com.itsaky.androidide.inflater.internal.LayoutInflaterImpl
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.inflater.internal.utils.IDTable
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
      requiresActivity { activity ->
        val file = layoutFile("singleView")
        val parent = LinearLayout(activity)
        val inflater = ILayoutInflater.newInflater()
        val inflated = inflater.inflate(file, parent)
        inflated.apply {
          assertThat(this).isNotNull()
          assertThat(this).isInstanceOf(View::class.java)
        }
      }
    }
  }

  @Test
  fun `test inlined layout`() {
    inflaterTest {
      requiresActivity { activity ->
        val file = layoutFile("inlined")
        val parent = LinearLayout(activity)
        val inflater = ILayoutInflater.newInflater()
        val inflated = inflater.inflate(file, parent)
        inflated.apply {
          assertThat(this).isNotNull()
          assertThat(this).isInstanceOf(LinearLayout::class.java)
        }
      }
    }
  }

  @Test
  fun `test inflated view is of expected type`() {
    inflaterTest { module ->
      requiresActivity { activity ->
        val parent = LinearLayout(activity)
        val inflater = ILayoutInflater.newInflater() as LayoutInflaterImpl
        viewToAdapter.keys.forEach { view ->
          parent.removeAllViews()
          module.createLayoutFile(view.simpleName!!) { file ->
            file.writeText(viewDeclTemplate(view.simpleName!!))
            val inflated = inflater.inflate(file, parent)
            assertThat(inflated!!.view).isInstanceOf(view.java)
            assertThat(inflated.view.id)
              .isEqualTo(IDTable.get(view.simpleName!!, "template_view"))

            (inflated as ViewImpl).attributes.apply {
              assertThat(this)
                .contains(AttributeImpl(INamespace.ANDROID, "id", "@+id/template_view"))
              assertThat(this)
                .contains(AttributeImpl(INamespace.ANDROID, "layout_height", "match_parent"))
              assertThat(this)
                .contains(AttributeImpl(INamespace.ANDROID, "layout_width", "match_parent"))
            }
          }
        }
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
