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

import com.android.build.gradle.BaseExtension
import com.itsaky.androidide.plugins.AndroidIDEPlugin
import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.GradlePlugin
import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost.Companion.S01
import org.gradle.api.Project

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id("build-logic.root-project")
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.maven.publish) apply false
  alias(libs.plugins.gradle.publish) apply false
}

buildscript {
  dependencies {
    classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
  }
}

val Project.simpleVersionName: String by lazy {
  val version = rootProject.version.toString()
  val regex = Regex("^v\\d+\\.?\\d+\\.?\\d+-\\w+")

  return@lazy regex.find(version)?.value?.substring(1)?.also {
    logger.warn("Simple version name is '$it' (from version $version)")
  }
    ?: run {
      if (CI.isTestEnv) {
        return@run "1.0.0-beta"
      }

      throw IllegalStateException(
        "Cannot extract simple version name. Invalid version string '$version'. Version names must be SEMVER with 'v' prefix"
      )
    }
}

val Project.projectVersionCode: Int by lazy {
  val version = simpleVersionName
  val regex = Regex("^\\d+\\.?\\d+\\.?\\d+")

  return@lazy regex.find(version)?.value?.replace(".", "")?.toInt()?.also {
    logger.warn("Version code is '$it' (from version ${version}).")
  }
    ?: throw IllegalStateException(
      "Cannot extract version code. Invalid version string '$version'. Version names must be SEMVER with 'v' prefix"
    )
}

val Project.publishingVersion by lazy {
  var version = simpleVersionName
  if (CI.isCiBuild && CI.branchName != "main") {
    version += "-${CI.commitHash}-SNAPSHOT"
  }
  return@lazy version
}

fun Project.configureBaseExtension() {
  extensions.findByType(BaseExtension::class)?.run {
    compileSdkVersion(BuildConfig.compileSdk)
    buildToolsVersion = BuildConfig.buildTools

    defaultConfig {
      minSdk = BuildConfig.minSdk
      targetSdk = BuildConfig.targetSdk
      versionCode = projectVersionCode
      versionName = rootProject.version.toString()
    }

    compileOptions {
      sourceCompatibility = BuildConfig.javaVersion
      targetCompatibility = BuildConfig.javaVersion
    }

    buildTypes.getByName("debug") { isMinifyEnabled = false }
    buildTypes.getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    testOptions { unitTests.isIncludeAndroidResources = true }

    buildFeatures.viewBinding = true
    buildFeatures.buildConfig = true
  }
}

subprojects {
  apply { plugin(AndroidIDEPlugin::class.java) }

  project.group = "com.itsaky.androidide"
  project.version = rootProject.version
  plugins.withId("com.android.application") { configureBaseExtension() }
  plugins.withId("com.android.library") { configureBaseExtension() }

  plugins.withId("java-library") {
    configure<JavaPluginExtension> {
      sourceCompatibility = BuildConfig.javaVersion
      targetCompatibility = BuildConfig.javaVersion
    }
  }

  plugins.withId("com.vanniktech.maven.publish.base") {
    configure<MavenPublishBaseExtension> {

      pom {
        name.set(project.name)
        description.set(project.description)
        inceptionYear.set("2021")
        url.set(ProjectConfig.GITHUB_URL)
        licenses {
          license {
            name.set("The GNU General Public License, v3.0")
            url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
            distribution.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
          }
        }

        developers {
          developer {
            id.set("androidide")
            name.set("AndroidIDE")
            url.set(ProjectConfig.PROJECT_SITE)
          }
        }

        scm {
          url.set(ProjectConfig.GITHUB_URL)
          connection.set(ProjectConfig.SCM_GIT)
          developerConnection.set(ProjectConfig.SCM_SSH)
        }
      }

      coordinates(project.group.toString(), project.name, project.publishingVersion)
      publishToMavenCentral(host = S01)
      signAllPublications()

      if (plugins.hasPlugin("com.android.library")) {
        configure(AndroidSingleVariantLibrary())
      } else if (plugins.hasPlugin("java-gradle-plugin")) {
        configure(GradlePlugin(javadocJar = JavadocJar.Javadoc()))
      } else if (plugins.hasPlugin("java-library")) {
        configure(JavaLibrary(javadocJar = JavadocJar.Javadoc()))
      }
    }
  }

  plugins.withId("com.gradle.plugin-publish") {
    configure<GradlePluginDevelopmentExtension> {
      version = project.publishingVersion
    }
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = BuildConfig.javaVersion.toString()
  }
}

tasks.register<Delete>("clean") { delete(rootProject.buildDir) }
