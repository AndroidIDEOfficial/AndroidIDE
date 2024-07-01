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

package com.itsaky.androidide.lsp.java

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.projects.ModuleProject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_VALUE_STRING)
class JavaCompilerProviderTest {

  @Before
  fun setup() {
    JavaLSPTest.setup()
  }

  @Test
  fun `test module specific compilers`() {
    val workspace = IProjectManager.getInstance().getWorkspace()!!
    val appModule = workspace.getProject(":app") as ModuleProject
    val androidLib = workspace.getProject(":android-library") as ModuleProject
    val anotherAndroidLib = workspace.getProject(":another-android-library") as ModuleProject
    val javaLib = workspace.getProject(":java-library") as ModuleProject
    val anotherJavaLib = workspace.getProject(":another-java-library") as ModuleProject

    val compilers = mutableSetOf<JavaCompilerService>()
    for (module in listOf(appModule, androidLib, anotherAndroidLib, javaLib, anotherJavaLib)) {
      compilers.add(JavaCompilerProvider.get(module))
    }

    assertThat(compilers).hasSize(5)

    val appCompiler = JavaCompilerProvider.get(appModule)
    compilers.add(appCompiler)
    compilers.add(JavaCompilerProvider.get(javaLib))

    assertThat(compilers).hasSize(5)

    compilers.clear()
    JavaCompilerProvider.getInstance().destroy()

    assertThat(JavaCompilerProvider.get(appModule)).isNotEqualTo(appCompiler)
  }
}
