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

import com.google.googlejavaformat.java.Formatter
import com.google.googlejavaformat.java.JavaFormatterOptions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Collections
import java.util.stream.Collectors

/**
 * Plugin for formatting the Java source files using google-java-format.
 *
 * @author Akash Yadav
 */
class GoogleJavaFormatPlugin : Plugin<Project> {

  companion object {
    private val excludeProjects =
      listOf(
        ":subprojects:aaptcompiler",
        ":subprojects:google-java-format",
        ":subprojects:jaxp",
        ":subprojects:jdt",
        ":subprojects:xml-dom",
        ":subprojects:xml-formatter",
        ":lexers"
      )
  }

  override fun apply(target: Project) {
    target.tasks.register("formatJavaSources") {
      doLast {
        val jarName = "google-java-format-1.15.0-all-deps"
        val jar = target.rootProject.file(".tools/${jarName}.jar")

        if (!jar.exists()) {
          logger.info("google-java-format JAR file not found. Skipping format task...")
          return@doLast
        }

        var formattingChanges = false
        target.rootProject.subprojects.forEach { sub ->
          
          if (sub.path in excludeProjects) {
            logger.warn("Project '${sub.path}' will not be formatted.")
            return@doLast
          }
          
          val sources = walkForSources(sub.file("src/main/java").toPath(), logger).toMutableSet()
          sources.addAll(walkForSources(sub.file("src/test/java").toPath(), logger))

          val formatter =
            Formatter(
              JavaFormatterOptions.builder()
                .style(JavaFormatterOptions.Style.GOOGLE)
                .formatJavadoc(true)
                .build()
            )

          for (path in sources) {
            if (doJavaFormat(formatter, path, logger)) {
              formattingChanges = true
            }
          }

          if (!formattingChanges) {
            logger.info("No formatting changes.")
            return@doLast
          }

          // ---------------- Stage Changes -------------------
          var out = ByteArrayOutputStream()
          var exit =
            target.rootProject.exec {
              isIgnoreExitValue = true
              standardOutput = out
              errorOutput = standardOutput
              commandLine = listOf("git", "add", ".")
            }

          if (exit.exitValue != 0) {
            logger.error(
              "Unable to stage changes. Process terminated with exit code ${exit.exitValue}"
            )
            logger.error(out.toString())
            return@doLast
          }

          // ----------------- Commit Changes --------------------
          out = ByteArrayOutputStream()
          exit =
            target.rootProject.exec {
              isIgnoreExitValue = true
              standardOutput = out
              errorOutput = standardOutput
              commandLine = listOf("git", "commit", "-m", "[Gradle] Format java source code")
            }

          if (exit.exitValue == 0) {
            logger.info("Changes committed successfully.")
          } else {
            logger.error(
              "Failed to commit changes. Process terminated with exit code ${exit.exitValue}"
            )
            logger.error(out.toString())
          }
        }
      }
    }
  }

  private fun doJavaFormat(formatter: Formatter, path: String, logger: Logger): Boolean {
    try {
      val file = Paths.get(path)
      val content = Files.readAllBytes(file)
      val utf8Decoded = String(content, StandardCharsets.UTF_8)
      val formatted = formatter.formatSource(utf8Decoded)
      if (utf8Decoded == formatted) {
        logger.info("{}: UP-TO-DATE", file)
        return true
      }

      val utf8Encoded = formatted.toByteArray(StandardCharsets.UTF_8)
      Files.write(file, utf8Encoded)
      println("${path}: Formatted successfully!")
      return true
    } catch (th: Throwable) {
      logger.error("Failed to format $path", th)
      return false
    }
  }

  private fun walkForSources(dir: Path, logger: Logger): Collection<String> {
    try {
      if (!Files.exists(dir)) {
        return Collections.emptySet()
      }

      return Files.walk(dir)
        .filter { !Files.isDirectory(it) && Files.isReadable(it) && Files.isWritable(it) }
        .map { it.toFile().absolutePath }
        .filter { it.endsWith(".java") && !it.endsWith("_template.java") }
        .collect(Collectors.toSet())
    } catch (error: Throwable) {
      logger.error("Could not walk directory for java sources: ${dir.toString ()}", error)
      return Collections.emptySet()
    }
  }
}
