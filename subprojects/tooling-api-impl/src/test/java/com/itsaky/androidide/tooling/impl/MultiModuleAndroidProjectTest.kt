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

package com.itsaky.androidide.tooling.impl

import com.android.builder.model.v2.ide.LibraryType
import com.android.builder.model.v2.ide.ProjectType.APPLICATION
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.builder.model.IDESyncIssue
import com.itsaky.androidide.buildinfo.BuildInfo
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.IJavaProject
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.ProjectType
import com.itsaky.androidide.tooling.api.messages.TaskExecutionMessage
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult
import com.itsaky.androidide.tooling.api.models.AndroidProjectMetadata
import com.itsaky.androidide.tooling.api.models.JavaModuleExternalDependency
import com.itsaky.androidide.tooling.api.models.JavaModuleProjectDependency
import com.itsaky.androidide.tooling.api.models.JavaProjectMetadata
import com.itsaky.androidide.tooling.api.models.params.StringParameter
import com.itsaky.androidide.tooling.events.ProgressEvent
import com.itsaky.androidide.tooling.events.task.TaskStartEvent
import com.itsaky.androidide.tooling.api.util.ToolingProps
import com.itsaky.androidide.tooling.testing.ToolingApiTestLauncher
import com.itsaky.androidide.tooling.testing.ToolingApiTestLauncher.MultiVersionTestClient
import com.itsaky.androidide.utils.AndroidPluginVersion
import com.itsaky.androidide.utils.FileProvider
import com.itsaky.androidide.utils.ILogger
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

/** @author Akash Yadav */
@RunWith(JUnit4::class)
class MultiModuleAndroidProjectTest {

  @Test
  fun `test simple multi module project initialization`() {
    val (server, project, result) = ToolingApiTestLauncher().launchServer()
    assertThat(result?.isSuccessful).isTrue()
    doAssertions(project, server)
  }

  @Test
  fun `test non-existing project initialization`() {
    val (_, _, result) = ToolingApiTestLauncher().launchServer(
      projectDir = Paths.get("/directory/does/not/exist/"))
    assertThat(result?.isSuccessful).isFalse()
    assertThat(result?.failure).isEqualTo(TaskExecutionResult.Failure.PROJECT_NOT_FOUND)
  }

  @Test
  fun `test project initialization with file path`() {
    val path = FileProvider.projectRoot().resolve("build")
      .also { if (!it.exists()) it.createDirectory() }.resolve("should-be-directory.txt")
    path.deleteIfExists()
    path.createFile()

    val (_, _, result) = ToolingApiTestLauncher().launchServer(
      projectDir = path)
    assertThat(result?.isSuccessful).isFalse()
    assertThat(result?.failure).isEqualTo(TaskExecutionResult.Failure.PROJECT_NOT_DIRECTORY)
  }

  @Test
  fun `test task name should be shown if gradle configuration cache is enabled`() {

    // Issue #1173

    val taskPaths = mutableListOf<String>()

    // do not add unresolved dependency so that teh configuration cache is properly created
    val client = object : MultiVersionTestClient(excludeUnresolvedDependency = true) {

      override fun getBuildArguments(): CompletableFuture<List<String>> {
        return CompletableFuture.completedFuture(super.getBuildArguments().get().toMutableList()
          .also { it.add("--configuration-cache") })
      }

      override fun onProgressEvent(event: ProgressEvent) {
        if (event is TaskStartEvent) {
          taskPaths.add(event.descriptor.taskPath)
        }
      }
    }

    val (server, project, result) = ToolingApiTestLauncher().launchServer(client = client)
    assertThat(server).isNotNull()
    assertThat(project).isNotNull()
    assertThat(result?.isSuccessful).isTrue()

    println("Executed tasks during initialization : " + taskPaths.joinToString(
      separator = System.lineSeparator()))
    taskPaths.clear()

    val (isSuccessful, failure) = server.executeTasks(TaskExecutionMessage(
      tasks = listOf("assembleDebug")
    )).get()

    if (failure != null) {
      println("Failure: $failure")
    }

    assertThat(isSuccessful).isTrue()
    assertThat(failure).isNull()

    assertThat(taskPaths).isNotNull()
    assertThat(taskPaths.size).isGreaterThan(10)
    assertThat(taskPaths).containsAtLeastElementsIn(
      arrayOf(
        "preBuild",
        "generateDebugResources",
        "compileDebugJavaWithJavac",
        "assembleDebug"
      ).map { ":android-library:$it" }
    )
    println(
      "Executed tasks during build : " + taskPaths.joinToString(separator = System.lineSeparator())
    )
  }

  private fun doAssertions(project: IProject, server: IToolingApiServer) {
    assertThat(project).isNotNull()

    // As the returned project is just a proxy,
    // project instanceOf IdeGradleProject will always return false
    val isInitialized = server.isServerInitialized().get()
    assertThat(isInitialized).isTrue()

    var selectionResult = project.selectProject(StringParameter("")).get()
    assertThat(selectionResult.isSuccessful).isTrue()
    assertThat(project.getType().get()).isEqualTo(ProjectType.Gradle)

    selectionResult = project.selectProject(StringParameter(":app")).get()
    assertThat(selectionResult).isNotNull()
    assertThat(selectionResult.isSuccessful).isTrue()

    val app = project.asAndroidProject()
    assertThat(app).isNotNull()
    assertThat(app).isInstanceOf(IAndroidProject::class.java)

    var metadata = app.getMetadata().get() as AndroidProjectMetadata
    assertThat(metadata).isNotNull()
    assertThat(metadata.javaCompileOptions.sourceCompatibility).isEqualTo("11")
    assertThat(metadata.javaCompileOptions.targetCompatibility).isEqualTo("11")
    assertThat(metadata.javaCompileOptions.javaSourceVersion).isEqualTo("11")
    assertThat(metadata.javaCompileOptions.javaBytecodeVersion).isEqualTo("11")
    assertThat(metadata.javaCompileOptions.isCoreLibraryDesugaringEnabled).isFalse()
    assertThat(metadata.androidType).isEqualTo(APPLICATION)
    assertThat(metadata.packageName).isEqualTo("com.itsaky.test.app")
    assertThat(metadata.viewBindingOptions).isNotNull()
    assertThat(metadata.viewBindingOptions.isEnabled).isTrue()

    // There are always more than 100 tasks in an android module
    // Also, the tasks must contain the user defined tasks
    val tasks = app.getTasks().get()
    assertThat(tasks.size).isAtLeast(100)
    assertThat(tasks.first { it.path == "${metadata.projectPath}:thisIsATestTask" }).isNotNull()

    var libraries = app.getLibraryMap().get()
    // At least one project library
    assertThat(libraries.values.filter { it.type == LibraryType.PROJECT }).isNotEmpty()
    // :app module includes :java-library as a dependency. But it is not transitive
    assertThat(
      libraries.values.firstOrNull {
        it.type == LibraryType.PROJECT &&
            it.projectInfo!!.projectPath == ":another-java-library" &&
            it.projectInfo!!.attributes["org.gradle.usage"] == "java-api"
      }
    )
      .isNull()

    selectionResult = project.selectProject(StringParameter(":android-library")).get()
    assertThat(selectionResult.isSuccessful).isTrue()

    val androidLib = project.asAndroidProject()
    assertThat(androidLib).isNotNull()
    assertThat(androidLib).isInstanceOf(IAndroidProject::class.java)
    // Make sure that transitive dependencies are included here because :android-library includes
    // :java-library project which further includes :another-java-libraries project with 'api'
    // configuration
    libraries = androidLib.getLibraryMap().get()!!
    assertThat(
      libraries.values.firstOrNull {
        it.type == LibraryType.PROJECT &&
            it.projectInfo!!.projectPath == ":another-java-library" &&
            it.projectInfo!!.attributes["org.gradle.usage"] == "java-api"
      }
    )
      .isNotNull()

    metadata = androidLib.getMetadata().get() as AndroidProjectMetadata
    assertThat(metadata.javaCompileOptions.javaSourceVersion).isEqualTo("11")
    assertThat(metadata.javaCompileOptions.javaBytecodeVersion).isEqualTo("11")

    selectionResult = project.selectProject(StringParameter(":java-library")).get()
    assertThat(selectionResult.isSuccessful).isTrue()

    val javaLibrary = project.asJavaProject()
    assertThat(javaLibrary).isNotNull()
    assertThat(javaLibrary).isInstanceOf(IJavaProject::class.java)

    var javaMetadata = javaLibrary.getMetadata().get() as JavaProjectMetadata
    assertThat(javaMetadata.compilerSettings.javaSourceVersion).isEqualTo("11")
    assertThat(javaMetadata.compilerSettings.javaBytecodeVersion).isEqualTo("11")

    val javaDependencies = javaLibrary.getDependencies().get()
    assertThat(
      javaDependencies.firstOrNull {
        it is JavaModuleExternalDependency &&
            it.gradleArtifact != null &&
            it.run {
              gradleArtifact!!.group == "io.github.itsaky" &&
                  gradleArtifact!!.name == "nb-javac-android" &&
                  gradleArtifact!!.version == "17.0.0.0"
            }
      }
    )
      .isNotNull()
    assertThat(
      javaDependencies.firstOrNull {
        it is JavaModuleProjectDependency && it.moduleName == "another-java-library"
      }
    )
      .isNotNull()
    // In case we have multiple dependencies with same name but different path
    val nested =
      javaDependencies
        .filterIsInstance(JavaModuleProjectDependency::class.java)
        .filter { it.moduleName.endsWith("nested-java-library") }
    assertThat(nested).hasSize(2)
    assertThat(nested[0].projectPath).isNotEqualTo(nested[1].projectPath)
    assertThat(
      project.selectProject(StringParameter(":does-not-exist")).get().isSuccessful).isFalse()

    selectionResult = project.selectProject(StringParameter(":another-java-library")).get()
    assertThat(selectionResult.isSuccessful).isTrue()

    val anotherJavaLib = project.asJavaProject()
    assertThat(anotherJavaLib).isNotNull()
    assertThat(anotherJavaLib).isInstanceOf(IJavaProject::class.java)

    javaMetadata = anotherJavaLib.getMetadata().get() as JavaProjectMetadata
    assertThat(javaMetadata.compilerSettings.javaSourceVersion).isEqualTo("1.8")
    assertThat(javaMetadata.compilerSettings.javaBytecodeVersion).isEqualTo("1.8")
  }

  /**
   * Tests the functionality of the tooling API implementation against multiple versions of the
   * Android Gradle Plugin. This test runs only in the CI environment.
   */
  @Test
  @Throws(CIOnlyException::class)
  fun `test CI-only simple multi module project initialization with multiple AGP versions`() {
    ciOnlyTest {
      // Test the minimum supported and the latest AGP version
      val versions =
        listOf(
          // AGP to Gradle
          BuildInfo.AGP_VERSION_MININUM to "7.3.3",
          BuildInfo.AGP_VERSION_LATEST to "8.2"
        )

      val client = MultiVersionTestClient()
      for ((agpVersion, gradleVersion) in versions) {
        client.agpVersion = agpVersion
        client.gradleVersion = gradleVersion
        val (server, project, result) = ToolingApiTestLauncher().launchServer(client = client)
        assertThat(result?.isSuccessful).isTrue()
        doAssertions(project = project, server = server)
      }
    }
  }

  @Test
  @Throws(CIOnlyException::class)
  fun `test CI-only latest tested AGP version warning`() {
    ciOnlyTest {
      val log = CollectingLogger()
      val agpVersion = AndroidPluginVersion.parse(BuildInfo.AGP_VERSION_LATEST)

      val client = MultiVersionTestClient(agpVersion = agpVersion.toStringSimple(),
        gradleVersion = "${agpVersion.major}.${agpVersion.minor}", log = log)
      val (_, project, result) = ToolingApiTestLauncher().launchServer(
        client = client,
        log = log,
        sysProps = mapOf(
          ToolingProps.TESTING_LATEST_AGP_VERSION to MultiVersionTestClient.DEFAULT_AGP_VERSION)
      )
      val output = log.toString()

      if (result?.isSuccessful != true) {
        // print the output if the initialization fails
        println(output)
      }

      assertThat(result?.isSuccessful).isTrue()

      val syncIssues = project.getProjectSyncIssues().get()
      assertThat(syncIssues).isNotNull()
      assertThat(syncIssues.syncIssues).isNotEmpty()
      assertThat(
        syncIssues.syncIssues.find { it.type == IDESyncIssue.TYPE_AGP_VERSION_TOO_NEW }
      ).isNotNull()
    }
  }

  @Test
  @Throws(CIOnlyException::class)
  fun `test CI-only minimum AGP version failure`() {
    ciOnlyTest {
      val log = CollectingLogger()
      val agpVersion = "7.1.0"
      val client = MultiVersionTestClient(agpVersion = agpVersion, gradleVersion = "7.2", log = log)
      val (_, _, result) = ToolingApiTestLauncher().launchServer(client = client, log = log)
      assertThat(result?.isSuccessful).isFalse()

      val output = log.toString()
      assertThat(output).contains(
        "Android Gradle Plugin version $agpVersion is not supported by AndroidIDE.")
    }
  }

  private fun ciOnlyTest(test: () -> Unit) {
    try {
      assertIsCI()
      test()
    } catch (err: CIOnlyException) {
      if (shouldTestMultipleVersions()) {
        throw err
      }
    }
  }

  private fun assertIsCI() {
    if (!shouldTestMultipleVersions()) {
      throw CIOnlyException()
    }
  }

  private fun shouldTestMultipleVersions(): Boolean {
    return System.getenv("TEST_TOOLING_API_IMPL").let { it == "true" }
  }

  internal class CollectingLogger : ILogger(CollectingLogger::class.simpleName) {

    private val string = StringBuilder()

    override fun doLog(level: Level?, message: String?) {
      string.append("${message?.trim()}${System.lineSeparator()}")
    }

    override fun toString(): String {
      return string.toString()
    }
  }

  private class CIOnlyException : IllegalStateException()
}
