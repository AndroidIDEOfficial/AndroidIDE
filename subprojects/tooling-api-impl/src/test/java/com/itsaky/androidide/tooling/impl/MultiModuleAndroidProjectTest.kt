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

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.testing.tooling.ToolingApiTestLauncher
import com.itsaky.androidide.testing.tooling.ToolingApiTestLauncher.MultiVersionTestClient
import com.itsaky.androidide.tooling.api.messages.TaskExecutionMessage
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult
import com.itsaky.androidide.tooling.events.ProgressEvent
import com.itsaky.androidide.tooling.events.task.TaskStartEvent
import com.itsaky.androidide.utils.FileProvider
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

/** @author Akash Yadav */
@RunWith(JUnit4::class)
@FixMethodOrder(MethodSorters.JVM)
class MultiModuleAndroidProjectTest {

  @Test
  fun `test simple multi module project initialization`() {
    ToolingApiTestLauncher.launchServer {
      assertThat(result?.isSuccessful).isTrue()
      performBasicProjectAssertions(project, server)
    }
  }

  @Test
  fun `test non-existing project initialization`() {
    ToolingApiTestLauncher.launchServer(projectDir = Paths.get("/directory/does/not/exist/")) {
      assertThat(result?.isSuccessful).isFalse()
      assertThat(result?.failure).isEqualTo(TaskExecutionResult.Failure.PROJECT_NOT_FOUND)
    }
  }

  @Test
  fun `test project initialization with file path`() {
    val path = FileProvider.projectRoot().resolve("build")
      .also { if (!it.exists()) it.createDirectory() }.resolve("should-be-directory.txt")
    path.deleteIfExists()
    path.createFile()

    ToolingApiTestLauncher.launchServer(projectDir = path) {
      assertThat(result?.isSuccessful).isFalse()
      assertThat(result?.failure).isEqualTo(TaskExecutionResult.Failure.PROJECT_NOT_DIRECTORY)
    }
  }

  @Test
  fun `test task name should be shown if gradle configuration cache is enabled`() {

    // Issue #1173

    val taskPaths = mutableListOf<String>()

    // do not add unresolved dependency so that the configuration cache is properly created
    val client = object : MultiVersionTestClient(excludeUnresolvedDependency = true, gradleVersion = "8.5") {

      override fun getBuildArguments(): CompletableFuture<List<String>> {
        return CompletableFuture.completedFuture(
          super.getBuildArguments().get().toMutableList().also { it.add("--configuration-cache") })
      }

      override fun onProgressEvent(event: ProgressEvent) {
        if (event is TaskStartEvent) {
          taskPaths.add(event.descriptor.taskPath)
        }
      }
    }

    ToolingApiTestLauncher.launchServer(
      projectDir = FileProvider.sampleProjectRoot(),
      client = client
    ) {
      assertThat(server).isNotNull()
      assertThat(project).isNotNull()
      assertThat(result?.isSuccessful).isTrue()

      println("Executed tasks during initialization : " + taskPaths.joinToString(
        separator = System.lineSeparator()))
      taskPaths.clear()

      val (isSuccessful, failure) = server.executeTasks(
        TaskExecutionMessage(tasks = listOf("assembleDebug"))
      ).get()

      if (failure != null) {
        println("Failure: $failure")
      }

      assertThat(isSuccessful).isTrue()
      assertThat(failure).isNull()

      assertThat(taskPaths).isNotNull()
      assertThat(taskPaths.size).isGreaterThan(10)
      assertThat(taskPaths).containsAtLeastElementsIn(
        arrayOf("preBuild", "generateDebugResources", "compileDebugJavaWithJavac",
          "assembleDebug").map { ":app:$it" })
      println(
        "Executed tasks during build : " + taskPaths.joinToString(separator = System.lineSeparator()))
    }
  }
}
