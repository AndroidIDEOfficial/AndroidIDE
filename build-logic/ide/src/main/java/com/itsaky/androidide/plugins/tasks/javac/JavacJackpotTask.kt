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

package com.itsaky.androidide.plugins.tasks.javac

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Task to transform Java compiler sources to make it compatible with the Android Runtime.
 *
 * @author Akash Yadav
 */
abstract class JavacJackpotTask : SourceTask() {

  /**
   * The source directory for the JDK.
   */
  @get:InputDirectory
  abstract val jdkSourceDirectory: DirectoryProperty

  /**
   * The output directory where all the transformed sources will be stored.
   */
  @get:OutputDirectory
  @get:Optional
  abstract val sourceOutDirectory: DirectoryProperty

  init {
    run {
      sourceOutDirectory.convention(project.layout.buildDirectory.dir(name))
    }
  }

  companion object {

    private val javaPckRegex = """^[a-zA-Z_]\w*(\.[a-zA-Z_]\w*)*${'$'}""".toRegex()
    private val packageTransformExcludes = arrayOf(
      "sun.reflect"
    )

    fun getTransformedSourceDirectories(sourceOutDir: File): List<File> {
      return compilerModules().map { resolveTransformedSrcDir(sourceOutDir, it, false) }
    }

    private fun compilerModules(): Array<String> {
      return arrayOf("java.compiler", "jdk.compiler")
    }

    private fun resolveTransformedSrcDir(srcOut: File, module: String, transformPck: Boolean) =
      srcOut.resolve(module).let {
        if (transformPck) {
          File(it, "com/itsaky/androidide")
        } else it
      }

    private fun resolveJdkModuleJavaSrc(jdkSrc: File, module: String) =
      jdkSrc.resolve("src/${module}/share/classes")
  }

  @TaskAction
  fun jackpot() {
    val projectJavaSrc = project.file("src/main/java")
    val jdkSrc = jdkSourceDirectory.get().asFile
    val srcOut = sourceOutDirectory.get().asFile
    val modules = compilerModules()

    for (module in modules) {
      resolveJdkModuleJavaSrc(jdkSrc, module).copyRecursively(
        resolveTransformedSrcDir(srcOut, module, true))
    }

    val sourcePaths = mutableListOf(projectJavaSrc.absolutePath).let { paths ->
      paths.addAll(
        modules.map { module -> resolveJdkModuleJavaSrc(jdkSrc, module).absolutePath }
      )
      paths.joinToString(separator = File.pathSeparator)
    }

    // sorted
    val finalHintFiles = source.files.map { it.absolutePath }.toMutableList()
    finalHintFiles.add(writePckTransformationsHintFile(srcOut))

    finalHintFiles.sortByDescending { it.substringAfterLast(File.separatorChar).substring(0, 2) }

    for (hintFile in finalHintFiles) {
      val cmdline = createCmdlineForHintFile(
        hintFile,
        sourcePaths,
        modules,
        srcOut
      )

      logger.lifecycle("Running: ${cmdline.joinToString(separator = " ")}")
      org.netbeans.modules.jackpot30.cmdline.Main.compile(*cmdline.toTypedArray())
    }
  }

  private fun createCmdlineForHintFile(hintFilePath: String, sourcePaths: String,
    modules: Array<String>,
    srcOut: File): MutableList<String> {
    val cmdline = mutableListOf(
      "--hint-file",
      hintFilePath,
      "--sourcepath",
      sourcePaths,
      "--source", "8",
      "--apply"
    )

    cmdline.addAll(modules.map { module ->
      resolveTransformedSrcDir(srcOut, module, false).also { it.mkdirs() }.absolutePath
    })
    return cmdline
  }

  private fun writePckTransformationsHintFile(srcOut: File): String {
    val hintFileOut = sourceOutDirectory.file("ac_nb-javac-android.hint").get().asFile
    val packgeTransformations = compilerModules().flatMap { module ->
      val moduleDir = resolveTransformedSrcDir(srcOut, module, true)

      // list all classes and change the refactor to change their package names
      // refactoring the package names directly causes strange issues and in some cases,
      // a few classes are left out
      // as a result, we refactor each class individually and change the package/import declarations
      // manually using find & replace
    moduleDir.walkBottomUp()
      .filter { it.isFile && it.extension == "java" }
      .mapNotNull {
        it.absolutePath.substring(moduleDir.absolutePath.length)
          .takeIf { path -> path.isNotBlank() }
      }
      .map { it.substring(1).replace(File.separatorChar, '.') }
      .map { it.substringBeforeLast(".java") }
      .distinct()

      // to change the package names directly,
      // use the following
//      moduleDir.walkBottomUp()
//        .filter { it.isDirectory }
//        .mapNotNull {
//          it.absolutePath.substring(moduleDir.absolutePath.length)
//            .takeIf { path -> path.isNotBlank() }
//        }
//        .map { it.substring(1).replace(File.separatorChar, '.') }
//        .distinct()
//        .filter { pck -> javaPckRegex.matches(pck) && packageTransformExcludes.none { it == pck || it.startsWith(pck) } }
//    }.joinToString(separator = System.lineSeparator()) { pck ->
//      "$pck => com.itsaky.androidide.${pck} ;;"
//    }
  }.joinToString(separator = System.lineSeparator()) { cls ->
    "$cls => com.itsaky.androidide.${cls} ;;"
  }

    hintFileOut.writeText(packgeTransformations)

    return hintFileOut.absolutePath
  }
}