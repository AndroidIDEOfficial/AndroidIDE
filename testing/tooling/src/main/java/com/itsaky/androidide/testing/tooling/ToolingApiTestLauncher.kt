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

package com.itsaky.androidide.testing.tooling

import ch.qos.logback.core.CoreConstants
import com.itsaky.androidide.testing.tooling.models.ToolingApiTestLauncherParams
import com.itsaky.androidide.testing.tooling.models.ToolingApiTestScope
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.messages.GradleDistributionParams
import com.itsaky.androidide.tooling.api.messages.InitializeProjectParams
import com.itsaky.androidide.tooling.api.messages.LogMessageParams
import com.itsaky.androidide.tooling.api.messages.result.BuildInfo
import com.itsaky.androidide.tooling.api.messages.result.BuildResult
import com.itsaky.androidide.tooling.api.messages.result.GradleWrapperCheckResult
import com.itsaky.androidide.tooling.api.messages.toLogLine
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher
import com.itsaky.androidide.tooling.api.util.ToolingProps
import com.itsaky.androidide.tooling.events.ProgressEvent
import com.itsaky.androidide.utils.FileProvider
import com.itsaky.androidide.utils.ILogger
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
object ToolingApiTestLauncher {

  private val opens =
    mutableMapOf("java.base" to "java.lang", "java.base" to "java.util",
      "java.base" to "java.io")

  private val exports =
    mutableMapOf("jdk.compiler" to "com.sun.tools.javac.api",
      "jdk.compiler" to "com.sun.tools.javac.file",
      "jdk.compiler" to "com.sun.tools.javac.parser",
      "jdk.compiler" to "com.sun.tools.javac.tree",
      "jdk.compiler" to "com.sun.tools.javac.util")

  @JvmOverloads
  @JvmStatic
  fun launchServer(
    projectDir: Path = FileProvider.testProjectRoot(),
    client: MultiVersionTestClient = MultiVersionTestClient(),
    initParams: InitializeProjectParams = InitializeProjectParams(
      projectDir.pathString,
      client.gradleDistParams
    ),
    log: Logger = LoggerFactory.getLogger("BuildOutputLogger"),
    sysProps: Map<String, String> = emptyMap(),
    sysEnvs: Map<String, String> = emptyMap(),
    action: ToolingApiTestScope.() -> Unit,
  ) = launchServer(
    ToolingApiTestLauncherParams(projectDir, client, initParams, log, sysProps, sysEnvs), action)

  @JvmOverloads
  @JvmStatic
  @JvmName("launchServerWithParams")
  fun launchServer(
    params: ToolingApiTestLauncherParams = ToolingApiTestLauncherParams(),
    action: ToolingApiTestScope.() -> Unit
  ) {
    return launchServerAsync(params) {
      // wait for project initialization
      initializeResult.get()

      action()
    }
  }

  @JvmOverloads
  @JvmStatic
  fun launchServerAsync(
    params: ToolingApiTestLauncherParams = ToolingApiTestLauncherParams(),
    action: ToolingApiTestScope.() -> Unit
  ) {
    val cmdLine = createProcessCmd(
      FileProvider.implModule()
        .resolve("build/libs/tooling-api-all.jar").pathString,
      params.sysProps
    )

    val builder = ProcessBuilder(cmdLine)
    val androidHome = com.itsaky.androidide.testing.common.utils.findAndroidHome()

    builder.environment()["ANDROID_SDK_ROOT"] = androidHome
    builder.environment()["ANDROID_HOME"] = androidHome
    builder.environment().putAll(params.sysEnvs)

    val proc = builder.start()

    proc.onExit().whenComplete { process, error ->
      if (process != null) {
        println(
          "[ToolingApiTestLauncher] Tooling API server process finished with exit code: ${process.exitValue()}"
        )
      }
      if (error != null) {
        println("[ToolingApiTestLauncher] Tooling API server process error")
        error.printStackTrace()
      }
    }

    Thread(Reader(proc.errorStream, params.log)).start()
    val launcher =
      ToolingApiLauncher.newClientLauncher(
        params.client, proc.inputStream,
        proc.outputStream
      )

    launcher.startListening()

    val server = launcher.remoteProxy as IToolingApiServer
    val project = launcher.remoteProxy as IProject
    val result = server.initialize(params.initParams)

    try {
      // perform the action
      ToolingApiTestScope(server, project, result).action()
    } finally {
      server.cancelCurrentBuild().get()
      server.shutdown().get()
    }
  }

  private fun createProcessCmd(
    jar: String,
    sysProps: Map<String, String> = emptyMap()
  ): List<String> {
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

    cmd.add("-D${ToolingProps.TESTING_IS_TEST_ENV}=true")

    sysProps.forEach { (key, value) ->
      cmd.add("-D$key=$value")
    }

    cmd.add(
      "-D${CoreConstants.STATUS_LISTENER_CLASS_KEY}=com.itsaky.androidide.tooling.impl.util.LogbackStatusListener")

    Collections.addAll(cmd, "-jar", jar)

    println(
      "[ToolingApiTestLauncher] Java cmd: " + cmd.joinToString(separator = " "))

    return cmd
  }

  open class MultiVersionTestClient(
    private val projectDir: Path = FileProvider.testProjectRoot(),
    var agpVersion: String = DEFAULT_AGP_VERSION,
    var gradleVersion: String = DEFAULT_GRADLE_VERSION,
    private val androidBlockConfig: String = "",
    private val log: Logger = LoggerFactory.getLogger(MultiVersionTestClient::class.java),
    private val extraArgs: List<String> = emptyList(),
    private var excludeUnresolvedDependency: Boolean = false,
    var outputValidator: (String) -> Boolean = { true },
  ) : IToolingApiClient {

    var isOutputValid = false
      private set

    val gradleDistParams: GradleDistributionParams
      get() = GradleDistributionParams.forVersion(this.gradleVersion)

    @Suppress("ConstPropertyName")
    companion object {

      const val buildFile = "build.gradle"
      const val buildFileIn = "$buildFile.in"

      const val appBuildFile = "app/build.gradle"
      const val appBuildFileIn = "$appBuildFile.in"

      const val DEFAULT_AGP_VERSION = "7.2.0"
      const val DEFAULT_GRADLE_VERSION = "7.3.3"

      const val GENERATED_FILE_WARNING =
        "DO NOT EDIT - Automatically generated file"
    }

    override fun logMessage(params: LogMessageParams) {
      val line = params.toLogLine()
      val message = line.message
      val logger = LoggerFactory.getLogger(line.tag)
      when (line.level) {
        ILogger.Level.DEBUG -> logger.debug(message)
        ILogger.Level.WARNING -> logger.warn(message)
        ILogger.Level.ERROR -> logger.error(message)
        ILogger.Level.INFO -> logger.info(message)

        else -> logger.trace(message)
      }
    }

    override fun logOutput(line: String) {
      val trimmed = line.trim()
      val curr = this.isOutputValid;
      this.isOutputValid = this.isOutputValid || outputValidator(trimmed)

      if (!curr && this.isOutputValid) {
        log.debug("Output validation succeeded")
      }

      log.debug(trimmed)
    }

    override fun prepareBuild(buildInfo: BuildInfo) {
      log.debug("---------- PREPARE BUILD ----------")
      log.debug("AGP Version : ${this.agpVersion}")
      log.debug("Gradle Version : ${this.gradleVersion}")
      log.debug("-----------------------------------")

      projectDir.resolve(buildFileIn)
        .replaceContents(dest = projectDir.resolve(buildFile),
          candidate = "@@TOOLING_API_TEST_AGP_VERSION@@" to this.agpVersion)

      projectDir.resolve(appBuildFileIn)
        .replaceContents(
          projectDir.resolve(appBuildFile),
          "//",
          "@@ANDROID_BLOCK_CONFIG@@" to androidBlockConfig,
          "@@UNRESOLVED_DEPENDENCY@@" to if (!excludeUnresolvedDependency) "implementation 'unresolved:unresolved:unresolved'" else "")
    }

    override fun onBuildSuccessful(result: BuildResult) {
      onBuildResult(result)
    }

    override fun onBuildFailed(result: BuildResult) {
      onBuildResult(result)
    }

    private fun onBuildResult(@Suppress("UNUSED_PARAMETER") result: BuildResult) {
      projectDir.resolve(buildFile).deleteIfExists()
      projectDir.resolve(appBuildFile).deleteIfExists()
    }

    override fun onProgressEvent(event: ProgressEvent) {}

    override fun getBuildArguments(): CompletableFuture<List<String>> {
      return CompletableFuture.completedFuture(
        mutableListOf("--stacktrace", "--info").also { it.addAll(extraArgs) })
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
        .append(System.lineSeparator().repeat(2))

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

  private class Reader(val input: InputStream, val log: Logger) : Runnable {

    override fun run() {
      try {
        val reader = BufferedReader(InputStreamReader(input))
        var line = reader.readLine()
        while (line != null) {
          log.debug(line)
          line = reader.readLine()
        }
      } catch (error: Throwable) {
        log.error("An error occurred reading from tooling API", error)
      }
    }
  }
}
