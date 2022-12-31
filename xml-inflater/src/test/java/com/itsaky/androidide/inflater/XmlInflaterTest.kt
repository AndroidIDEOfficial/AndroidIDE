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

import androidx.appcompat.app.AppCompatActivity
import com.itsaky.androidide.inflater.utils.endParse
import com.itsaky.androidide.inflater.utils.startParse
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage
import com.itsaky.androidide.tooling.testing.ToolingApiTestLauncher
import java.io.File
import org.junit.Ignore
import org.robolectric.Robolectric

@Ignore("Test utility provider")
object XmlInflaterTest {

  private var init: Boolean = false
  internal val activity by lazy { Robolectric.buildActivity(AppCompatActivity::class.java).get() }

  fun initIfNeeded() {
    if (init) {
      return
    }

    val (server, project) =
      ToolingApiTestLauncher().launchServer(implDir = "../subprojects/tooling-api-impl")
    server.initialize(InitializeProjectMessage(File("../tests/test-project").absolutePath)).get()

    Lookup.DEFAULT.register(BuildService.KEY_PROJECT_PROXY, project)
    ProjectManager.setupProject()
    init = true
  }
}

fun inflaterTest(block: (AndroidModule) -> Unit) {
  XmlInflaterTest.initIfNeeded()
  startParse(ProjectManager.app!!)
  block(ProjectManager.app!!)
  endParse()
}

fun requiresActivity(block: AppCompatActivity.() -> Unit) {
  XmlInflaterTest.activity.block()
}
