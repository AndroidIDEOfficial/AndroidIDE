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
import com.itsaky.androidide.eventbus.events.file.FileCreationEvent
import com.itsaky.androidide.eventbus.events.file.FileDeletionEvent
import com.itsaky.androidide.eventbus.events.file.FileRenameEvent
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage
import com.itsaky.androidide.tooling.testing.ToolingApiTestLauncher
import com.itsaky.androidide.utils.FileProvider
import com.itsaky.androidide.utils.SourceClassTrie.SourceNode
import java.io.File
import java.nio.file.Files
import kotlin.io.path.pathString
import kotlin.io.path.writeText
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_VALUE_STRING)
class ModuleProjectTest {

  @Test
  fun test() {
    val (server, project) = ToolingApiTestLauncher().launchServer()
    server.initialize(InitializeProjectMessage(FileProvider.testProjectRoot().pathString, "debug")).get()

    Lookup.getDefault().register(BuildService.KEY_PROJECT_PROXY, project)

    verifyProjectManagerAPIs()

    assertThat(project).isNotNull()

    val rootProject = ProjectManager.rootProject
    assertThat(rootProject).isNotNull()

    val root = rootProject!!.rootProject
    assertThat(root).isNotNull()
    assertThat(root.name).isEqualTo("TestApp")

    val rootDir = root.projectDir
    assertThat(rootDir).isNotNull()
    assertThat(Files.isSameFile(rootDir.toPath(), FileProvider.testProjectRoot())).isTrue()

    val app = rootProject.findByPath(":app")
    assertThat(app).isNotNull()
    assertThat(app).isInstanceOf(AndroidModule::class.java)

    val appSourceDirs = (app as AndroidModule).getSourceDirectories()
    assertThat(appSourceDirs).isNotEmpty()
    assertThat(appSourceDirs)
      .containsAtLeast(File(rootDir, "app/src/main/java"), File(rootDir, "app/src/main/kotlin"))
    assertThat(appSourceDirs).doesNotContain(File(rootDir, "other-java-library/src/main/java"))
    assertThat(app.compilerSettings.javaSourceVersion).isEqualTo("11")
    assertThat(app.compilerSettings.javaBytecodeVersion).isEqualTo("11")
    assertThat(app.compilerSettings.javaSourceVersion)
      .isEqualTo(app.compilerSettings.sourceCompatibility)
    assertThat(app.compilerSettings.javaBytecodeVersion)
      .isEqualTo(app.compilerSettings.targetCompatibility)

    val anotherAndroidLib = rootProject.findByPath(":another-android-library")
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
          "build/${IAndroidProject.FD_INTERMEDIATES}/compile_library_classes_jar/debug/classes.jar"
        ),
        File(
          anotherAndroidLibDir,
          "build/${IAndroidProject.FD_INTERMEDIATES}/compile_r_class_jar/debug/R.jar"
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

    assertThat(anotherAndroidLib.compilerSettings.javaSourceVersion).isEqualTo("11")
    assertThat(anotherAndroidLib.compilerSettings.javaBytecodeVersion).isEqualTo("11")

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
            "build/${IAndroidProject.FD_INTERMEDIATES}/compile_library_classes_jar/debug/classes.jar"
          ),
          File(
            app.projectDir,
            "build/${IAndroidProject.FD_INTERMEDIATES}/compile_and_runtime_not_namespaced_r_class_jar/debug/R.jar"
          ),
          File(
            anotherAndroidLibDir,
            "build/${IAndroidProject.FD_INTERMEDIATES}/compile_r_class_jar/debug/R.jar"
          )
        )
      assertThat(
          filter {
            it.absolutePath.endsWith("appcompat-1.5.1/jars/classes.jar") ||
              it.absolutePath.endsWith("material-1.8.0-alpha01/jars/classes.jar") ||
              it.absolutePath.endsWith("coordinatorlayout-1.2.0/jars/classes.jar")
          }
        )
        .hasSize(3)
    }

    val javaLibrary = rootProject.findByPath(":java-library")
    assertThat(javaLibrary).isNotNull()
    assertThat(javaLibrary).isInstanceOf(JavaModule::class.java)

    val javaLibSourceDirs = (javaLibrary as JavaModule).getSourceDirectories()
    assertThat(javaLibSourceDirs).isNotEmpty()
    assertThat(javaLibSourceDirs).contains(File(rootDir, "java-library/src/main/java"))

    app.indexSourcesAndClasspaths()

    val classes = app.compileJavaSourceClasses.findInPackage("com.itsaky")
    assertThat(classes).isNotNull()
    assertThat(classes).isNotEmpty()
    assertThat(classes.map { it.qualifiedName })
      .containsExactly(
        "com.itsaky.androidide.tooling.test.Main",
        "com.itsaky.test.app.MainActivity"
      )
    assertThat(classes.map { it.isClass }).containsExactly(true, true)
    assertThat(classes.map { it::class.java })
      .containsExactly(SourceNode::class.java, SourceNode::class.java)

    rootProject.findByPath(":another-java-library").run {
      assertThat(this).isInstanceOf(JavaModule::class.java)
      assertThat((this as JavaModule).compilerSettings).isNotNull()
      assertThat(compilerSettings.javaSourceVersion).isEqualTo("1.8")
      assertThat(compilerSettings.javaBytecodeVersion).isEqualTo("1.8")
    }
  }

  private fun verifyProjectManagerAPIs() {

    val testCls =
      FileProvider.testProjectRoot()
        .resolve("app/src/main/java/com/itsaky/test/app/Test.java")
        .toAbsolutePath()
        .normalize()
    val testCls_renamed = testCls.parent!!.resolve("TestRenamed.java")

    if (Files.exists(testCls)) {
      Files.delete(testCls)
    }

    if (Files.exists(testCls_renamed)) {
      Files.delete(testCls_renamed)
    }

    ProjectManager.setupProject()

    val rootProject = ProjectManager.rootProject
    assertThat(rootProject).isNotNull()

    val root = rootProject?.rootProject
    assertThat(root).isNotNull()
    assertThat(root!!.path).isEqualTo(":")

    val source =
      root.projectDir.toPath().resolve("app/src/main/java/com/itsaky/test/app/MainActivity.java")
    val module = rootProject.findModuleForFile(source)
    assertThat(module).isNotNull()
    assertThat(module!!.path).isEqualTo(":app")
    assertThat(module.projectDir.path).isEqualTo(File(root.projectDir, "app").path)

    val sourceNode = module.compileJavaSourceClasses.findSource(source)
    assertThat(sourceNode).isNotNull()
    assertThat(sourceNode!!.isClass).isTrue()
    assertThat(sourceNode.name).isEqualTo("MainActivity")
    assertThat(sourceNode.packageName).isEqualTo("com.itsaky.test.app")
    assertThat(sourceNode.qualifiedName).isEqualTo("com.itsaky.test.app.MainActivity")
    assertThat(sourceNode.children).isEmpty()
    assertThat(Files.isSameFile(source, sourceNode.file)).isTrue()

    // make sure the files are not indexed
    assertThat(ProjectManager.containsSourceFile(testCls)).isFalse()
    assertThat(ProjectManager.containsSourceFile(testCls_renamed)).isFalse()

    testCls.writeText("public class Test {  }")
    ProjectManager.onFileCreated(FileCreationEvent(testCls.toFile()))
    assertThat(ProjectManager.containsSourceFile(testCls)).isTrue()
    assertThat(ProjectManager.containsSourceFile(testCls_renamed)).isFalse()
    assertThat(ProjectManager.findModuleForFile(testCls, true)).isEqualTo(module)
    assertThat(ProjectManager.findModuleForFile(testCls_renamed, true)).isNull()

    Files.move(testCls, testCls_renamed)
    ProjectManager.onFileRenamed(FileRenameEvent(testCls.toFile(), testCls_renamed.toFile()))
    assertThat(ProjectManager.containsSourceFile(testCls)).isFalse()
    assertThat(ProjectManager.containsSourceFile(testCls_renamed)).isTrue()
    assertThat(ProjectManager.findModuleForFile(testCls, true)).isNull()
    assertThat(ProjectManager.findModuleForFile(testCls_renamed, true)).isEqualTo(module)

    Files.delete(testCls_renamed)
    ProjectManager.onFileDeleted(FileDeletionEvent(testCls_renamed.toFile()))
    assertThat(ProjectManager.containsSourceFile(testCls)).isFalse()
    assertThat(ProjectManager.containsSourceFile(testCls_renamed)).isFalse()
    assertThat(ProjectManager.findModuleForFile(testCls, true)).isNull()
    assertThat(ProjectManager.findModuleForFile(testCls_renamed, true)).isNull()
  }
}
