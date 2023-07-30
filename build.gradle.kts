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
import com.vanniktech.maven.publish.AndroidMultiVariantLibrary
import com.vanniktech.maven.publish.GradlePlugin
import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost.Companion.S01
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
  }
}

val flavorsAbis = arrayOf("arm64-v8a", "armeabi-v7a")

fun Project.configureBaseExtension() {
  extensions.findByType(BaseExtension::class)?.run {
    compileSdkVersion(BuildConfig.compileSdk)
    buildToolsVersion = BuildConfig.buildTools

    defaultConfig {
      minSdk = BuildConfig.minSdk
      targetSdk = BuildConfig.targetSdk
      versionCode = projectVersionCode
      versionName = rootProject.version.toString()

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
      sourceCompatibility = BuildConfig.javaVersion
      targetCompatibility = BuildConfig.javaVersion
    }

    if (":app" == project.path) {
      flavorDimensions("default")

      productFlavors {
        flavorsAbis.forEach(this::create)

        forEach {
          val name = it.name
          defaultConfig.buildConfigField("String",
            "FLAVOR_${name.replace('-', '_').uppercase()}",
            "\"${name}\"")
        }
      }
    }

    // configure split APKs for ':app' module only
    if (this@configureBaseExtension == rootProject.findProject(":app")) {
      splits {
        abi {
          reset()

          isEnable = true
          isUniversalApk = false

          // TODO: Find a way to enable split APKs in product flavors. If this is possible, we can configure
          //       each flavor to include only a single ABI. For example, for the 'arm64-v8a' flavor,
          //       we can configure it to generate APK only for 'arm64-v8a'.
          //
          //  See the contribution guidelines for more information.
          @Suppress("ChromeOsAbiSupport")
          include(*flavorsAbis)
        }
      }
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
  afterEvaluate {
    apply { plugin(AndroidIDEPlugin::class.java) }
  }

  project.group = BuildConfig.packageName
  project.version = rootProject.version
  plugins.withId("com.android.application") { configureBaseExtension() }
  plugins.withId("com.android.library") { configureBaseExtension() }

  plugins.withId("java-library") {
    configure<JavaPluginExtension> {
      sourceCompatibility = BuildConfig.javaVersion
      targetCompatibility = BuildConfig.javaVersion
    }
  }

  project.afterEvaluate {
    if (project.plugins.hasPlugin("com.vanniktech.maven.publish.base") && project.description.isNullOrBlank()) {
      throw GradleException("Project ${project.path} must have a description")
    }
  }

  plugins.withId("com.vanniktech.maven.publish.base") {
    configure<MavenPublishBaseExtension> {

      pom {
        name.set(project.name)
        description.set(project.description)
        inceptionYear.set("2021")
        url.set(ProjectConfig.REPO_URL)
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
          url.set(ProjectConfig.REPO_URL)
          connection.set(ProjectConfig.SCM_GIT)
          developerConnection.set(ProjectConfig.SCM_SSH)
        }
      }

      coordinates(project.group.toString(), project.name, project.publishingVersion)
      publishToMavenCentral(host = S01)
      signAllPublications()

      if (plugins.hasPlugin("com.android.library")) {
        configure(AndroidMultiVariantLibrary())
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

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = BuildConfig.javaVersion.toString()
  }
}

tasks.register<Delete>("clean") { delete(rootProject.buildDir) }
