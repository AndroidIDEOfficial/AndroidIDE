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

tasks.register<Copy>("copyJarToAssets") {
  from(project.file("${project.buildDir}/libs/tooling-api-all.jar"))
  into(project.rootProject.file("app/src/main/assets/data/common/"))
}

tasks.register("deleteExistingJarFiles") { delete { delete(project.buildDir.resolve("libs")) } }

tasks.register("copyJar") {
  finalizedBy("copyJarToAssets")
  doLast {
    val libsDir = project.buildDir.resolve("libs")

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

project.tasks.getByName("shadowJar") { finalizedBy("copyJar") }

dependencies {
  implementation(projects.shared)
  implementation(projects.subprojects.toolingApi)

  implementation(libs.common.jkotlin)
  implementation(libs.xml.xercesImpl)
  implementation(libs.xml.apis)
  implementation(libs.tooling.gradleApi)

  testImplementation(projects.subprojects.toolingApiTesting)
  testImplementation(projects.shared)
  testImplementation(libs.tests.junit)
  testImplementation(libs.tests.google.truth)

  runtimeOnly(libs.tooling.slf4j)
}
