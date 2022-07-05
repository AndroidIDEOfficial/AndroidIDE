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

package com.itsaky.androidide.projects.api

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage
import com.itsaky.androidide.tooling.testing.ToolingApiTestLauncher
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.nio.file.Files
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ModuleProjectTest {

  private val log = ILogger.newInstance(javaClass.simpleName)

  @Test
  fun test() {
    val (server, project) = ToolingApiTestLauncher().launchServer()
    server
      .initialize(
        InitializeProjectMessage(File("../tooling-api-impl/src/test/test-project").absolutePath)
      )
      .get()
    ProjectManager.setupProject(project)

    val root = ProjectManager.rootProject
    assertThat(root).isNotNull()
    assertThat(root!!.name).isEqualTo("TestApp")

    val rootDir = root.projectDir
    assertThat(rootDir).isNotNull()
    assertThat(
        Files.isSameFile(
          rootDir.toPath(),
          File("../tooling-api-impl/src/test/test-project").toPath()
        )
      )
      .isTrue()

    val app = root.findByPath(":app")
    assertThat(app).isNotNull()
    assertThat(app).isInstanceOf(AndroidModule::class.java)

    val appSourceDirs = (app as AndroidModule).getSourceDirectories()
    assertThat(appSourceDirs).isNotEmpty()
    assertThat(appSourceDirs)
      .containsAtLeast(File(rootDir, "app/src/main/java"), File(rootDir, "app/src/main/kotlin"))
    assertThat(appSourceDirs).doesNotContain(File(rootDir, "other-java-library/src/main/java"))

    val anotherAndroidLib = root.findByPath(":another-android-library")
    assertThat(anotherAndroidLib).isNotNull()
    assertThat(anotherAndroidLib).isInstanceOf(AndroidModule::class.java)

    val anotherAndroidLibDir = anotherAndroidLib!!.projectDir

    val androidLibSourceDirs = (anotherAndroidLib as AndroidModule).getSourceDirectories()
    assertThat(androidLibSourceDirs).isNotEmpty()
    assertThat(androidLibSourceDirs)
      .contains(File(rootDir, "another-android-library/src/main/java"))

    assertThat(anotherAndroidLib.getCompileClasspaths())
      .containsAtLeast(
        File(
          anotherAndroidLibDir,
          "build/${com.itsaky.androidide.tooling.api.model.AndroidModule.FD_INTERMEDIATES}/compile_library_classes_jar/debug/classes.jar"
        ),
        File(
          anotherAndroidLibDir,
          "build/${com.itsaky.androidide.tooling.api.model.AndroidModule.FD_INTERMEDIATES}/compile_r_class_jar/debug/R.jar"
        )
      )
    assertThat(
        anotherAndroidLib.getCompileClasspaths().firstOrNull {
          it.name == "nb-javac-android-17.0.0.0.jar"
        }
      )
      .isNotNull()

    assertThat(anotherAndroidLib.getCompileSourceDirectories())
      .containsAtLeast(
        File(anotherAndroidLibDir, "src/main/java"),
        File(anotherAndroidLibDir, "src/main/kotlin"),
        File(rootDir, "other-java-library/src/main/java")
      )

    val appCompileSourceDirs = app.getCompileSourceDirectories()
    assertThat(appCompileSourceDirs).isNotEmpty()
    assertThat(appCompileSourceDirs)
      .containsAtLeast(
        File(rootDir, "app/src/main/java"),
        File(rootDir, "app/src/main/kotlin"),
        File(anotherAndroidLibDir, "src/main/java")
      )

    app.getCompileClasspaths().apply {
      assertThat(this).isNotEmpty()
      assertThat(this)
        .containsAtLeast(
          File(
            anotherAndroidLibDir,
            "build/${com.itsaky.androidide.tooling.api.model.AndroidModule.FD_INTERMEDIATES}/compile_library_classes_jar/debug/classes.jar"
          ),
          File(
            app.projectDir,
            "build/${com.itsaky.androidide.tooling.api.model.AndroidModule.FD_INTERMEDIATES}/compile_and_runtime_not_namespaced_r_class_jar/debug/R.jar"
          ),
          File(
            anotherAndroidLibDir,
            "build/${com.itsaky.androidide.tooling.api.model.AndroidModule.FD_INTERMEDIATES}/compile_r_class_jar/debug/R.jar"
          )
        )
      assertThat(
          filter {
            it.absolutePath.endsWith("appcompat-1.3.1/jars/classes.jar") ||
              it.absolutePath.endsWith("material-1.4.0/jars/classes.jar") ||
              it.absolutePath.endsWith("coordinatorlayout-1.2.0/jars/classes.jar")
          }
        )
        .hasSize(3)
    }

    val javaLibrary = root.findByPath(":java-library")
    assertThat(javaLibrary).isNotNull()
    assertThat(javaLibrary).isInstanceOf(JavaModule::class.java)

    val javaLibSourceDirs = (javaLibrary as JavaModule).getSourceDirectories()
    assertThat(javaLibSourceDirs).isNotEmpty()
    assertThat(javaLibSourceDirs).contains(File(rootDir, "java-library/src/main/java"))

    app.indexSources()

    val classNames = app.compileSourceClasses.findClassNames("com.itsaky")
    assertThat(classNames).isNotNull()
    assertThat(classNames).isNotEmpty()
    assertThat(classNames)
      .containsExactly(
        "com.itsaky.androidide.tooling.test.Main",
        "com.itsaky.test.app.MainActivity"
      )
  }
}
