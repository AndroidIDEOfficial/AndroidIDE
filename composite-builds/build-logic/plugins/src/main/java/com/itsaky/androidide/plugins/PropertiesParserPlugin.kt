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

package com.itsaky.androidide.plugins

import com.itsaky.androidide.build.propparser.PropertiesParser
import com.itsaky.androidide.build.propparser.gen.ClassGenerator
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile
import java.io.File

/**
 * Translates a .properties file into a .java file containing an enum-like Java class which defines
 * static factory methods for all resource keys in a given resource file.
 *
 * @author Akash Yadav
 */
class PropertiesParserPlugin : Plugin<Project> {

  override fun apply(target: Project) {

    target.extensions.configure(JavaPluginExtension::class.java) {
      sourceSets.getByName("main").java { srcDir(target.propsDir) }
    }

    target.tasks.register("compileProperties") {
      doLast {
        val options = target.createParserOptions()
        if (options.isEmpty()) {
          return@doLast
        }

        val parser = PropertiesParser { target.logger.lifecycle(it) }

        logger.info(
          "Running PropertiesParser with arguments: ${options.joinToString(separator = " ")}"
        )

        val ok = parser.run(options.toTypedArray())
        if (!ok) {
          throw GradleException("Failed to parse property files")
        }
      }

      outputs.dir(target.propsDir)
    }

    target.tasks.register("cleanCompiledProperties", Delete::class.java) { delete(target.propsDir) }

    target.tasks.withType(JavaCompile::class.java) { dependsOn("compileProperties") }
    target.tasks.getByName("clean").dependsOn("cleanCompiledProperties")
  }
}

// When changing this path, make sure to change the same in the method below as well as in the
// ClassGenerator.packageName(...) method.
private val Project.resDir: File
  get() = file("src/main/resources")

private val Project.propsDir: File
  get() = layout.buildDirectory.dir( "generated/properties").get().asFile

private fun Project.createParserOptions(): List<String> {
  val options = mutableListOf<String>()
  resDir.walkTopDown().forEach {
    if (!(it.isFile && it.extension == "properties")) {
      return@forEach
    }

    val destPath =
      it.path.substringAfter("src/main/resources".replace('/', File.separatorChar))
        .substringBeforeLast(File.separatorChar) +
          File.separator +
          ClassGenerator.toplevelName(it) +
          ".java"
    val destFile = File(propsDir, destPath)

    if (destFile.exists() && destFile.lastModified() >= it.lastModified()) {
      logger.info("${it.name} is up-to-date. Skipping.")
      return@forEach
    }

    destFile.parentFile.mkdirs()
    options.add("-compile")
    options.add(it.absolutePath)
    options.add(destFile.parentFile.absolutePath)
  }
  return options
}
