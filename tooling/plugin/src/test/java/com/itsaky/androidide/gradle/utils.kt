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

package com.itsaky.androidide.gradle

import com.itsaky.androidide.buildinfo.BuildInfo
import com.itsaky.androidide.tooling.api.LogSenderConfig._PROPERTY_IS_TEST_ENV
import com.itsaky.androidide.tooling.api.LogSenderConfig._PROPERTY_MAVEN_LOCAL_REPOSITORY
import com.itsaky.androidide.utils.FileProvider
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.internal.PluginUnderTestMetadataReading
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

internal fun buildProject(
  agpVersion: String = BuildInfo.AGP_VERSION_LATEST,
  gradleVersion: String = BuildInfo.AGP_VERSION_GRADLE_LATEST,
  useApplyPluginGroovySyntax: Boolean = false,
  configureArgs: (MutableList<String>) -> Unit = {},
  vararg plugins: String
): BuildResult {
  val projectRoot = openProject(agpVersion, useApplyPluginGroovySyntax, *plugins)
  val initScript = FileProvider.testHomeDir().resolve(".androidide/init/androidide.init.gradle")
  val mavenLocal = Paths.get("build/maven-local/repos.txt").toFile()

  if (!(mavenLocal.exists() && mavenLocal.isFile)) {
    throw FileNotFoundException("repos.txt file not found")
  }

  val repositories = mavenLocal.readText()

  for (repo in repositories.split(':')) {
    val file = File(repo)
    if (!(file.exists() && file.isDirectory)) {
      throw FileNotFoundException("Maven local repository does not exist : $repo")
    }
  }

  val args = mutableListOf(
    ":app:tasks", // run any task, as long as it applies the plugins
    "--init-script", initScript.pathString,
    "-P$_PROPERTY_IS_TEST_ENV=true", // plugins should be published to maven local first
    "-P$_PROPERTY_MAVEN_LOCAL_REPOSITORY=$repositories",
    "--stacktrace"
  )

  configureArgs(args)

  val runner = GradleRunner.create()
    .withProjectDir(projectRoot.toFile())
    .withGradleVersion(gradleVersion)
    .withArguments(
      *args.toTypedArray()
    )

  writeInitScript(
    initScript.toFile(),
    PluginUnderTestMetadataReading.readImplementationClasspath()
  )

  return runner.build()
}

internal fun writeInitScript(file: File, deps: List<File>) {
  file.parentFile.mkdirs()

  val root = FileProvider.projectRoot().pathString
  val depsString = deps.filter { it.absolutePath.startsWith(root) }
    .joinToString(separator = System.lineSeparator()) {
      "classpath files(\"${it}\")"
    }

  file.bufferedWriter().use {
    it.write(
      """
      initscript {
        dependencies {
          // make sure the init script plugin is in classpath
          $depsString
        }
      }
      
      apply plugin: com.itsaky.androidide.gradle.AndroidIDEInitScriptPlugin
    """.trimIndent()
    )
  }
}

internal fun openProject(
  agpVersion: String = BuildInfo.AGP_VERSION_LATEST,
  useApplyPluginGroovySyntax: Boolean = false,
  vararg plugins: String
): Path {
  val projectRoot = Paths.get("src/test/resources/sample-project")

  run {
    projectRoot.resolve("build.gradle.kts").toFile()
      .replaceAllPlaceholders(mapOf("AGP_VERSION" to agpVersion))
  }

  run {
    // remove existing build scripts
    projectRoot.resolve("app")
      .toFile()
      .listFiles()!!
      .filter { it.name.startsWith("build.gradle") && !it.name.endsWith(".in") }
      .forEach { it.delete() }

    val pluginsText = if (!useApplyPluginGroovySyntax) {
      plugins.joinToString(separator = "\n") { "id(\"$it\")" }
    } else {
      plugins.joinToString(separator = "\n") { "apply plugin: \"$it\"" }
    }

    projectRoot.resolve("app/build.gradle" + if (useApplyPluginGroovySyntax) "" else ".kts")
      .toFile()
      .replaceAllPlaceholders(mapOf("PLUGINS" to pluginsText))
  }

  return projectRoot
}

private fun File.replaceAllPlaceholders(entries: Map<String, String>) {
  val sb = StringBuilder(parentFile.resolve("${name}.in").readText())
  for ((placeholder, value) in entries) {
    val regex = Regex.escape("@@${placeholder}@@").toRegex()
    val result = regex.findAll(sb)
    for (matchResult in result) {
      sb.replace(matchResult.range.first, matchResult.range.last + 1, value)
    }
  }
  writeText(sb.toString())
}