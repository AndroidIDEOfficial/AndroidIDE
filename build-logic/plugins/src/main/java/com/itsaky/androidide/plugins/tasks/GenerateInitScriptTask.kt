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

package com.itsaky.androidide.plugins.tasks

import com.itsaky.androidide.build.config.VersionUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Generates the Gradle init script for AndroidIDE.
 */
abstract class GenerateInitScriptTask : DefaultTask() {

  @get:Input
  abstract val downloadVersion: Property<String>

  @get:Input
  abstract val mavenGroupId: Property<String>

  @get:OutputDirectory
  abstract val outputDir: DirectoryProperty

  @TaskAction
  fun generate() {

    val outFile = this.outputDir.file("data/common/androidide.init.gradle")
      .also {
        it.get().asFile.parentFile.mkdirs()
      }

    outFile.get().asFile.bufferedWriter().use {

      it.write(
        """
      initscript {
          repositories {
              
              // Always specify the snapshots repository first
              maven {
                  // Add snapshots repository for AndroidIDE CI builds
                  url "${VersionUtils.SONATYPE_SNAPSHOTS_REPO}"
              }
              
              maven {
                  // Add public repository for AndroidIDE release builds
                  url "${VersionUtils.SONATYPE_PUBLIC_REPO}"
              }
              
              mavenCentral()
              google()
          }

          dependencies {
              classpath('${mavenGroupId.get()}:gradle-plugin:${downloadVersion.get()}') {
                  setChanging(false)
              }
          }
      }
      
      apply plugin: com.itsaky.androidide.gradle.AndroidIDEInitScriptPlugin
    """
          .trimIndent()
      )
    }
  }

}