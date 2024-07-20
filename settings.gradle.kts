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

@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  includeBuild("composite-builds/build-logic") {
    name = "build-logic"
  }

  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  val dependencySubstitutions = mapOf(
    "build-deps" to arrayOf(
      "appintro",
      "fuzzysearch",
      "google-java-format",
      "java-compiler",
      "javac",
      "javapoet",
      "jaxp",
      "jdk-compiler",
      "jdk-jdeps",
      "jdt",
      "layoutlib-api",
      "logback-core"
    ),

    "build-deps-common" to arrayOf(
      "desugaring-core"
    )
  )

  for ((build, modules) in dependencySubstitutions) {
    includeBuild("composite-builds/${build}") {
      this.name = build
      dependencySubstitution {
        for (module in modules) {
          substitute(module("com.itsaky.androidide.build:${module}"))
            .using(project(":${module}"))
        }
      }
    }
  }

  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://s01.oss.sonatype.org/content/groups/public/") }
    maven { url = uri("https://jitpack.io") }
  }
}

buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath("com.mooltiverse.oss.nyx:gradle:2.5.2")
  }
}

val isGitRepo by lazy {
  cmdOutput("git", "rev-parse", "--is-inside-work-tree").trim() == "true"
}

private fun cmdOutput(vararg args: String): String {
  return ProcessBuilder(*args)
    .directory(File("."))
    .redirectErrorStream(true)
    .start()
    .inputStream
    .bufferedReader()
    .readText()
    .trim()
}

FDroidConfig.load(rootDir)

if (FDroidConfig.hasRead && FDroidConfig.isFDroidBuild) {
  gradle.rootProject {
    val regex = Regex("^v\\d+\\.?\\d+\\.?\\d+-\\w+")

    val simpleVersion = regex.find(FDroidConfig.fDroidVersionName!!)?.value
      ?: throw IllegalArgumentException("Invalid version '${FDroidConfig.fDroidVersionName}. Version name must have semantic version format.'")

    project.setProperty("version", simpleVersion)
  }
} else if(isGitRepo) {
  apply {
    plugin("com.mooltiverse.oss.nyx")
  }
}

rootProject.name = "AndroidIDE"

// keep this sorted alphabetically
include(
  ":annotation:annotations",
  ":annotation:processors",
  ":annotation:processors-ksp",
  ":core:actions",
  ":core:app",
  ":core:common",
  ":core:indexing-api",
  ":core:indexing-core",
  ":core:lsp-api",
  ":core:lsp-models",
  ":core:projects",
  ":core:resources",
  ":editor:api",
  ":editor:impl",
  ":editor:lexers",
  ":editor:treesitter",
  ":event:eventbus",
  ":event:eventbus-android",
  ":event:eventbus-events",
  ":java:javac-services",
  ":java:lsp",
  ":logging:idestats",
  ":logging:logger",
  ":logging:logsender",
  ":logging:logsender-sample",
  ":termux:application",
  ":termux:emulator",
  ":termux:shared",
  ":termux:view",
  ":testing:androidTest",
  ":testing:benchmarks",
  ":testing:commonTest",
  ":testing:gradleToolingTest",
  ":testing:lspTest",
  ":testing:unitTest",
  ":tooling:api",
  ":tooling:builder-model-impl",
  ":tooling:events",
  ":tooling:impl",
  ":tooling:model",
  ":tooling:plugin",
  ":tooling:plugin-config",
  ":utilities:build-info",
  ":utilities:flashbar",
  ":utilities:framework-stubs",
  ":utilities:lookup",
  ":utilities:preferences",
  ":utilities:shared",
  ":utilities:templates-api",
  ":utilities:templates-impl",
  ":utilities:treeview",
  ":utilities:uidesigner",
  ":utilities:xml-inflater",
  ":xml:aaptcompiler",
  ":xml:dom",
  ":xml:lsp",
  ":xml:resources-api",
  ":xml:utils",
)

object FDroidConfig {

  var hasRead: Boolean = false
    private set

  var isFDroidBuild: Boolean = false
    private set

  var fDroidVersionName: String? = null
    private set

  var fDroidVersionCode: Int? = null
    private set

  const val PROP_FDROID_BUILD = "ide.build.fdroid"
  const val PROP_FDROID_BUILD_VERSION = "ide.build.fdroid.version"
  const val PROP_FDROID_BUILD_VERCODE = "ide.build.fdroid.vercode"

  fun load(rootDir: File) {
    val propsFile = File(rootDir, "fdroid.properties")
    if (!propsFile.exists() || !propsFile.isFile) {
      hasRead = true
      isFDroidBuild = false
      return
    }

    val properties = propsFile.let { props ->
      java.util.Properties().also {
        it.load(props.reader())
      }
    }

    hasRead = true
    isFDroidBuild = properties.getProperty(PROP_FDROID_BUILD, null).toBoolean()

    fDroidVersionName = properties.getProperty(PROP_FDROID_BUILD_VERSION, null)
    fDroidVersionCode =  properties.getProperty(PROP_FDROID_BUILD_VERCODE, null)?.toInt()
  }
}
