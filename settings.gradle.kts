@file:Suppress("UnstableApiUsage")

import com.mooltiverse.oss.nyx.gradle.NyxExtension
import com.mooltiverse.oss.nyx.services.github.GitHub

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  includeBuild("build-logic")
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

plugins { id("com.mooltiverse.oss.nyx") version "1.3.0" }

extensions.configure<NyxExtension> {
  git {
    remotes.register("origin") {
      user.set("{{#environment.variable}}GH_TOKEN{{/environment.variable}}")
      password.set("")
    }
  }
  configurationFile.set(".nyx.yml")
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://repo.gradle.org/gradle/libs-releases/") }
    maven { url = uri("https://repo.eclipse.org/content/repositories/lemminx-snapshots/") }
  }
}

rootProject.name = "AndroidIDE"

include(
  ":annotation-ksp",
  ":annotations",
  ":actions",
  ":app",
  ":common",
  ":editor",
  ":emulatorview",
  ":eventbus",
  ":eventbus-android",
  ":eventbus-events",
  ":lexers",
  ":logger",
  ":lookup",
  ":preferences",
  ":resources",
  ":shared",
  ":treeview",
  ":uidesigner",
  ":xml-inflater",
  ":gradle-plugin",
  ":lsp:api",
  ":lsp:models",
  ":lsp:java",
  ":lsp:xml",
  ":lsp:testing",
  ":subprojects:aaptcompiler",
  ":subprojects:builder-model-impl",
  ":subprojects:classfile",
  ":subprojects:framework-stubs",
  ":subprojects:fuzzysearch",
  ":subprojects:google-java-format",
  ":subprojects:javac",
  ":subprojects:javac-services",
  ":subprojects:jaxp",
  ":subprojects:jdt",
  ":subprojects:jsonrpc",
  ":subprojects:layoutlib-api",
  ":subprojects:projects",
  ":subprojects:tooling-api",
  ":subprojects:tooling-api-events",
  ":subprojects:tooling-api-impl",
  ":subprojects:tooling-api-model",
  ":subprojects:tooling-api-testing",
  ":subprojects:xml-dom",
  ":subprojects:xml-formatter",
  ":subprojects:xml-utils"
)

/**
 * Information about the CI build.
 *
 * @author Akash Yadav
 */
object CI {

  /** The short commit hash. */
  val commitHash by lazy {
    val sha = System.getenv("GITHUB_SHA") ?: return@lazy ""
    shortSha(sha)
  }

  /** Name of the current branch. */
  val branchName by lazy {
    System.getenv("GITHUB_REF_NAME") ?: "main" // by default, 'main'
  }

  /** Whether the current build is a CI build. */
  val isCiBuild by lazy { "true" == System.getenv("CI") }

  private fun shortSha(sha: String): String {
    return ProcessBuilder("git", "rev-parse", "--short", sha)
      .directory(File("."))
      .redirectErrorStream(true)
      .start()
      .inputStream
      .bufferedReader()
      .readText()
      .trim()
  }
}
