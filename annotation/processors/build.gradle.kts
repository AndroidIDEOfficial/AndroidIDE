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


import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.itsaky.androidide.build.config.BuildConfig

plugins {
  kotlin("jvm")
}



dependencies {
  implementation(kotlin("stdlib"))

  implementation(projects.annotation.annotations)
  implementation(projects.event.eventbus)

  implementation(libs.androidx.annotation)
  implementation(libs.common.javapoet)

  implementation("de.greenrobot:java-common:2.3.1")

  implementation(libs.google.auto.service.annotations)
  annotationProcessor(libs.google.auto.service)

  // Generates the required META-INF descriptor to make the processor incremental.
  val incap = "1.0.0"
  compileOnly("net.ltgt.gradle.incap:incap:$incap")
  annotationProcessor("net.ltgt.gradle.incap:incap-processor:$incap")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}