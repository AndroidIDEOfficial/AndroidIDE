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

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import org.jetbrains.kotlin.utils.addToStdlib.ifTrue

@Suppress("JavaPluginLanguageLevel")
plugins {
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("java-library")
  id("org.jetbrains.kotlin.jvm")
}

shadow {
  archivesName.set("tooling-api")
}

tasks.withType<Jar> {
  manifest { attributes("Main-Class" to "com.itsaky.androidide.tooling.impl.Main") }
}

tasks.register<Copy>("copyJarToAssets") {
  from(project.file("${project.buildDir}/libs/tooling-api-all.jar"))
  into(project.rootProject.file("app/src/main/assets/data/common/"))
}

tasks.register("renameJar") {
  finalizedBy("copyJarToAssets")
  
  doLast {
    val jar = project.file("${project.buildDir}/libs/tooling-api-${project.version}-all.jar")
    val destJar = jar.parentFile.resolve("tooling-api-all.jar")
    destJar.exists().ifTrue {
      destJar.delete()
    }
  
    jar.renameTo(destJar)
  }
}

project.tasks.getByName("jar") { finalizedBy("shadowJar") }

project.tasks.getByName("shadowJar") { finalizedBy("renameJar") }

dependencies {
  implementation(projects.subprojects.toolingApi)

  implementation(libs.common.jkotlin)
  implementation(libs.xml.xercesImpl)
  implementation(libs.xml.apis)
  implementation("org.gradle:gradle-tooling-api:7.6")

  testImplementation(projects.subprojects.toolingApiTesting)
  testImplementation(libs.tests.junit)
  testImplementation(libs.tests.google.truth)

  runtimeOnly("org.slf4j:slf4j-api:2.0.6")
}
