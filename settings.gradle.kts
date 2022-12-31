@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  includeBuild("build-logic")
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    
    maven {
      url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
    
    maven {
      url = uri("https://jitpack.io")
    }
  
    maven {
      url = uri("https://repo.gradle.org/gradle/libs-releases/")
    }
  
    maven {
      url = uri("https://repo.eclipse.org/content/repositories/lemminx-snapshots/")
    }
  }
}

rootProject.name = "AndroidIDE"

include(
  ":annotation-processors",
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
