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

import com.android.builder.model.v2.ide.LibraryType.PROJECT
import com.android.builder.model.v2.ide.ProjectType.APPLICATION
import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage
import com.itsaky.androidide.tooling.api.messages.result.BuildResult
import com.itsaky.androidide.tooling.api.messages.result.GradleWrapperCheckResult
import com.itsaky.androidide.tooling.api.model.AndroidModule
import com.itsaky.androidide.tooling.api.model.JavaModule
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher
import com.itsaky.androidide.tooling.events.ProgressEvent
import com.itsaky.androidide.utils.ILogger
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.*

/** @author Akash Yadav */
@RunWith(JUnit4::class)
class ToolingApiImplTest {

  private val log = ILogger.newInstance(javaClass.simpleName)

  @Test
  fun testProjectInit() {
    val client = TestClient()
    val (server, project) = launchServer(client)

    server.initialize(InitializeProjectMessage(getTestProject().absolutePath)).get()

    verifyProjectProps(project, server)
  }

  private fun verifyProjectProps(project: IProject, server: IToolingApiServer) {
    assertThat(project).isNotNull()
    assertThat(project.type.get()).isEqualTo(IProject.Type.Gradle)
    // As the returned project is just a proxy,
    // project instanceOf IdeGradleProject will always return false

    val isInitialized = server.isServerInitialized().get()
    assertThat(isInitialized).isTrue()

    val app = project.findByPath(":app").get()
    assertThat(app).isNotNull()
    assertThat(app).isInstanceOf(AndroidModule::class.java)

    assertThat((app as AndroidModule).javaCompileOptions).isNotNull()
    assertThat(app.javaCompileOptions.sourceCompatibility).isEqualTo("11")
    assertThat(app.javaCompileOptions.targetCompatibility).isEqualTo("11")
    assertThat(app.javaCompileOptions.isCoreLibraryDesugaringEnabled).isFalse()

    assertThat(app.projectType).isEqualTo(APPLICATION)
    assertThat(app.packageName).isEqualTo("com.itsaky.test.app")

    assertThat(app.viewBindingOptions).isNotNull()
    assertThat(app.viewBindingOptions!!.isEnabled).isTrue()

    // There are always more than 100 tasks in an android module
    // Also, the tasks must contain the user defined tasks
    assertThat(app.tasks.size).isAtLeast(100)
    assertThat(app.tasks.first { it.path == "${app.projectPath}:thisIsATestTask" }).isNotNull()

    // This is not expected to be filled. Instead debugLibraries must be filled
    @Suppress("DEPRECATION") assertThat(app.variantDependencies).isEmpty()

    assertThat(app.debugLibraries).isNotEmpty()
    // At least one project library
    assertThat(app.debugLibraries.filter { it.type == PROJECT }).isNotEmpty()

    val javaLibrary = project.findByPath(":java-library").get()
    assertThat(javaLibrary).isNotNull()
    assertThat(javaLibrary).isInstanceOf(JavaModule::class.java)

    assertThat(project.findByPath(":does-not-exist").get()).isNull()
  }

  private fun launchServer(client: IToolingApiClient): Pair<IToolingApiServer, IProject> {
    val builder =
      ProcessBuilder(
        "java",
        "--add-opens",
        "java.base/java.lang=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.util=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.io=ALL-UNNAMED",
        "-jar",
        "./build/libs/tooling-api-all.jar"
      )
    val androidHome = findAndroidHome()
    println("ANDROID_HOME=$androidHome")
    builder.environment()["ANDROID_SDK_ROOT"] = androidHome
    builder.environment()["ANDROID_HOME"] = androidHome
    val proc = builder.start()

    Thread(Reader(proc.errorStream)).start()
    val launcher = ToolingApiLauncher.newClientLauncher(client, proc.inputStream, proc.outputStream)

    launcher.startListening()

    return launcher.remoteProxy as IToolingApiServer to launcher.remoteProxy as IProject
  }

  private fun findAndroidHome(): String {
    var androidHome = System.getenv("ANDROID_HOME")
    if (androidHome != null && androidHome.isNotBlank()) {
      return androidHome
    }

    androidHome = System.getenv("ANDROID_SDK_ROOT")
    if (androidHome != null && androidHome.isNotBlank()) {
      return androidHome
    }

    val os = System.getProperty("os.name")
    val home = System.getProperty("user.home")
    return if (os.contains("Linux")) {
      "$home/Android/Sdk"
    } else {
      "$home\\AppData\\Local\\Android\\Sdk"
    }
  }

  private class TestClient : IToolingApiClient {
    private val log = ILogger.newInstance(javaClass.simpleName)
    override fun logMessage(line: LogLine) {
      log.log(line.priority, line.formattedTagAndMessage())
    }

    override fun logOutput(line: String) {
      log.debug(line.trim())
    }

    override fun prepareBuild() {}
    override fun onBuildSuccessful(result: BuildResult) {}
    override fun onBuildFailed(result: BuildResult) {}

    override fun onProgressEvent(event: ProgressEvent) {}

    override fun getBuildArguments(): CompletableFuture<List<String>> {
      return CompletableFuture.completedFuture(listOf("--stacktrace", "--info"))
    }

    override fun checkGradleWrapperAvailability(): CompletableFuture<GradleWrapperCheckResult> =
      CompletableFuture.completedFuture(GradleWrapperCheckResult(true))
  }

  private class Reader(val input: InputStream) : Runnable {
    private val log = ILogger.newInstance(javaClass.simpleName)
    override fun run() {
      try {
        val reader = BufferedReader(InputStreamReader(input))
        var line = reader.readLine()
        while (line != null) {
          log.debug(line)
          line = reader.readLine()
        }
      } catch (error: Throwable) {
        log.error(error)
      }
    }
  }

  fun Any.getTestProject(): File = File("./src/test/test-project")

  fun Any.asJson(): String {
    val builder = GsonBuilder()
    ToolingApiLauncher.configureGson(builder)
    return builder.create().toJson(this)
  }
}
