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
import com.itsaky.androidide.tooling.api.messages.result.BuildInfo
import com.itsaky.androidide.tooling.api.messages.result.BuildResult
import com.itsaky.androidide.tooling.api.messages.result.GradleWrapperCheckResult
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher
import com.itsaky.androidide.tooling.events.ProgressEvent
import com.itsaky.androidide.utils.FileProvider
import com.itsaky.androidide.utils.ILogger
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Path
import java.util.Collections
import java.util.concurrent.CompletableFuture
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter
import kotlin.io.path.deleteIfExists
import kotlin.io.path.pathString

/**
 * Launches the Tooling API server in a separate JVM process.
 *
 * ***For testing purposes only!***
 *
 * @author Akash Yadav
 */
class ToolingApiTestLauncher {

  private val opens =
    mutableMapOf("java.base" to "java.lang", "java.base" to "java.util",
      "java.base" to "java.io")

  private val exports =
    mutableMapOf("jdk.compiler" to "com.sun.tools.javac.api",
      "jdk.compiler" to "com.sun.tools.javac.file",
      "jdk.compiler" to "com.sun.tools.javac.parser",
      "jdk.compiler" to "com.sun.tools.javac.tree",
      "jdk.compiler" to "com.sun.tools.javac.util")

  fun launchServer(
    client: IToolingApiClient = MultiVersionTestClient(),
  ): Pair<IToolingApiServer, IProject> {
    val builder = ProcessBuilder(createProcessCmd(FileProvider.implModule()
      .resolve("build/libs/tooling-api-all.jar").pathString))
    val androidHome = findAndroidHome()
    println("ANDROID_HOME=$androidHome")
    builder.environment()["ANDROID_SDK_ROOT"] = androidHome
    builder.environment()["ANDROID_HOME"] = androidHome
    val proc = builder.start()

    Thread(Reader(proc.errorStream)).start()
    val launcher =
      ToolingApiLauncher.newClientLauncher(client, proc.inputStream,
        proc.outputStream)

    launcher.startListening()

    return launcher.remoteProxy as IToolingApiServer to launcher.remoteProxy as IProject
  }

  private fun createProcessCmd(jar: String): List<String> {
    val cmd = mutableListOf("java")
    System.getenv("JAVA_HOME")?.let {
      val java = File(it, "bin/java")
      if (java.exists() && java.isFile) {
        if (!java.canExecute()) {
          java.setExecutable(true)
        }

        cmd[0] = java.absolutePath
      }
    }
    for (open in opens) {
      cmd.add("--add-opens=${open.key}/${open.value}=ALL-UNNAMED")
    }

    for (export in exports) {
      cmd.add("--add-exports=${export.key}/${export.value}=ALL-UNNAMED")
    }

    Collections.addAll(cmd, "-jar", jar)

    println(
      "[ToolingApiTestLauncher] Java cmd: " + cmd.joinToString(separator = " "))

    return cmd
  }

  class MultiVersionTestClient(
    private val projectDir: Path = FileProvider.testProjectRoot(),
    var agpVersion: String = DEFAULT_AGP_VERSION,
    var gradleVersion: String = DEFAULT_GRADLE_VERSION
  ) : IToolingApiClient {

    companion object {

      const val buildFile = "build.gradle"
      const val buildFileIn = "$buildFile.in"

      const val gradlewProps = "gradle/wrapper/gradle-wrapper.properties"
      const val gradlewPropsIn = "$gradlewProps.in"

      const val DEFAULT_AGP_VERSION = "7.2.0"
      const val DEFAULT_GRADLE_VERSION = "7.5.1"

      const val GENERATED_FILE_WARNING =
        "DO NOT EDIT - Automatically generated file"

      private val log = ILogger.newInstance("MultiVersionTestClient")
    }

    override fun logMessage(line: LogLine) {
      log.log(line.priority, line.formattedTagAndMessage())
    }

    override fun logOutput(line: String) {
      log.debug(line.trim())
    }

    override fun prepareBuild(buildInfo: BuildInfo) {
      log.debug("---------- PREPARE BUILD ----------")
      log.debug("AGP Version : ${this.agpVersion}")
      log.debug("Gradle Version : ${this.gradleVersion}")
      log.debug("-----------------------------------")

      projectDir.resolve(buildFileIn)
        .replaceContents(dest = projectDir.resolve(buildFile),
          candidate = "@@TOOLING_API_TEST_AGP_VERSION@@" to this.agpVersion)

      projectDir.resolve(gradlewPropsIn)
        .replaceContents(comment = "#", dest = projectDir.resolve(gradlewProps),
          candidate = "@@GRADLE_VERSION@@" to this.gradleVersion)
    }

    override fun onBuildSuccessful(result: BuildResult) {}
    override fun onBuildFailed(result: BuildResult) {}

    override fun onProgressEvent(event: ProgressEvent) {}

    override fun getBuildArguments(): CompletableFuture<List<String>> {
      return CompletableFuture.completedFuture(listOf("--stacktrace", "--info"))
    }

    override fun checkGradleWrapperAvailability(): CompletableFuture<GradleWrapperCheckResult> =
      CompletableFuture.completedFuture(GradleWrapperCheckResult(true))

    private fun Path.replaceContents(dest: Path, comment: String = "//",
      candidate: Pair<String, String>
    ) = replaceContents(dest, comment, *arrayOf(candidate))

    private fun Path.replaceContents(dest: Path, comment: String = "//",
      vararg candidates: Pair<String, String>
    ) {
      val contents = StringBuilder().append(comment)
        .append(" ")
        .append(GENERATED_FILE_WARNING)
        .append(System.getProperty("line.separator").repeat(2))

      bufferedReader().use { reader ->
        reader.readText().also { text ->
          var t = text
          for ((old, new) in candidates) {
            t = t.replace(old, new)
          }
          contents.append(t)
        }
      }

      dest.deleteIfExists()

      dest.bufferedWriter().use { writer ->
        writer.write(contents.toString())
        writer.flush()
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
