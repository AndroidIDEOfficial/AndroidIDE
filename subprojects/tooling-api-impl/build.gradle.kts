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

@Suppress("JavaPluginLanguageLevel")
plugins {
  id("com.github.johnrengelman.shadow") version "8.1.1"
  id("java-library")
  id("org.jetbrains.kotlin.jvm")
}

tasks.withType<Jar> {
  manifest { attributes("Main-Class" to "${BuildConfig.packageName}.tooling.impl.Main") }
}

tasks.register("deleteExistingJarFiles") {
  delete {
    delete(project.layout.buildDirectory.dir("libs"))
  }
}

tasks.register("copyJar") {
  doLast {
    val libsDir = project.layout.buildDirectory.dir("libs")

    copy {
      from(libsDir)
      into(libsDir)
      include("*-all.jar")
      rename { "tooling-api-all.jar" }
    }
  }
}

project.tasks.getByName("jar") {
  dependsOn("deleteExistingJarFiles")
  finalizedBy("shadowJar")
}

project.tasks.getByName("shadowJar") {
  finalizedBy("copyJar")
}

dependencies {
  api(projects.subprojects.toolingApi)

  implementation(projects.buildInfo)
  implementation(projects.shared)

  implementation(libs.common.jkotlin)
  implementation(libs.xml.xercesImpl)
  implementation(libs.xml.apis)
  implementation(libs.tooling.gradleApi)

  testImplementation(projects.testing.tooling)

  runtimeOnly(libs.tooling.slf4j)
}
