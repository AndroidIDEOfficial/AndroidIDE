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

package com.itsaky.androidide.tooling.testing

import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.messages.result.BuildResult
import com.itsaky.androidide.tooling.api.messages.result.GradleWrapperCheckResult
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher
import com.itsaky.androidide.tooling.events.ProgressEvent
import com.itsaky.androidide.utils.ILogger
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.*

/**
 * Launches the Tooling API server in a separate JVM process.
 *
 * ***For testing purposes only!***
 *
 * ***This must be used only from modules that are located in the 'build-tools' directory!***
 *
 * @author Akash Yadav
 */
class ToolingApiTestLauncher {

  fun launchServer(
    client: IToolingApiClient = TestClient(),
    implDir: String = "../tooling-api-impl"
  ): Pair<IToolingApiServer, IProject> {
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
        "$implDir/build/libs/tooling-api-all.jar"
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

  open class TestClient : IToolingApiClient {
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
  
  class MultiVersionTestClient(var version: String = "7.2.0") :
    ToolingApiTestLauncher.TestClient() {
    
    companion object {
      val buildTemplateFile = File("../../tests/test-project/build.gradle.in")
      val buildFile = File(buildTemplateFile.parentFile, "build.gradle")
    }
    
    override fun prepareBuild() {
      super.prepareBuild()
      var contents = buildTemplateFile.bufferedReader().readText()
      contents = contents.replace("@@TOOLING_API_TEST_AGP_VERSION@@", this.version)
      contents = "/* DO NOT EDIT - Automatically generated file */\n${contents.trim()}"
      val writer = buildFile.bufferedWriter()
      writer.use {
        it.write(contents)
        it.flush()
      }
    }
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
}
