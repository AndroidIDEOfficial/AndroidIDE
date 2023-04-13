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

import org.jetbrains.kotlin.utils.addToStdlib.ifTrue

plugins {
  id("java-gradle-plugin")
  id("org.jetbrains.kotlin.jvm")
  id("com.vanniktech.maven.publish.base")
}

tasks.create("generateBuildInfo") {

  val buildInfoFile = "src/main/java/AndroidIDEBuildInfo.kt"
  val buildInfoFileIn = "${buildInfoFile}.in"

  project
    .file(buildInfoFileIn)
    .replaceContents(
      dest = project.file(buildInfoFile),
      candidates = arrayOf("@@BUILD_VERSION@@" to project.publishingVersion)
    )
}

val generatedWarning = "DO NOT EDIT - Automatically generated file"
fun File.replaceContents(
  dest: File,
  comment: String = "//",
  vararg candidates: Pair<String, String>
) {
  val contents =
    StringBuilder()
      .append(comment)
      .append(" ")
      .append(generatedWarning)
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

  dest.exists().ifTrue { dest.delete() }

  dest.bufferedWriter().use { writer ->
    writer.write(contents.toString())
    writer.flush()
  }
}

gradlePlugin {
  website.set(ProjectConfig.GITHUB_URL)
  vcsUrl.set(ProjectConfig.GITHUB_URL)

  plugins {
    create("gradlePlugin") {
      id = "com.itsaky.androidide.gradle"
      implementationClass = "com.itsaky.androidide.gradle.AndroidIDEGradlePlugin"
      displayName = "AndroidIDE Gradle Plugin"
      description = "Gradle plugin for projects that are built with AndroidIDE"
      tags.set(setOf("androidide"))
    }
  }
}
