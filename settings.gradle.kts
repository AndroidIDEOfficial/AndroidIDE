@file:Suppress("UnstableApiUsage")

import com.mooltiverse.oss.nyx.gradle.NyxExtension

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  includeBuild("build-logic")
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

plugins { id("com.mooltiverse.oss.nyx") version "2.4.5" }

extensions.configure<NyxExtension> {
  git {
    remotes.register("origin") {
      user.set("{{#environmentVariable}}GH_TOKEN{{/environmentVariable}}")
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
  ":annotation-processors",
  ":annotation-processors-ksp",
  ":annotations",
  ":actions",
  ":app",
  ":build-info",
  ":common",
  ":editor",
  ":editor-api",
  ":emulatorview",
  ":eventbus",
  ":eventbus-android",
  ":eventbus-events",
  ":gradle-plugin",
  ":idestats",
  ":lexers",
  ":logger",
  ":logsender",
  ":logsender-sample",
  ":lookup",
  ":preferences",
  ":resources",
  ":shared",
  ":templates-api",
  ":templates-impl",
  ":treeview",
  ":uidesigner",
  ":xml-inflater",
  ":lsp:api",
  ":lsp:models",
  ":lsp:java",
  ":lsp:xml",
  ":lsp:testing",
  ":subprojects:aaptcompiler",
  ":subprojects:builder-model-impl",
  ":subprojects:classfile",
  ":subprojects:flashbar",
  ":subprojects:framework-stubs",
  ":subprojects:fuzzysearch",
  ":subprojects:google-java-format",
  ":subprojects:java-compiler",
  ":subprojects:javac",
  ":subprojects:javac-services",
  ":subprojects:javapoet",
  ":subprojects:jaxp",
  ":subprojects:jdk-compiler",
  ":subprojects:jdk-jdeps",
  ":subprojects:jdt",
  ":subprojects:layoutlib-api",
  ":subprojects:projects",
  ":subprojects:tooling-api",
  ":subprojects:tooling-api-events",
  ":subprojects:tooling-api-impl",
  ":subprojects:tooling-api-model",
  ":subprojects:tooling-api-testing",
  ":subprojects:xml-dom",
  ":subprojects:xml-utils"
)