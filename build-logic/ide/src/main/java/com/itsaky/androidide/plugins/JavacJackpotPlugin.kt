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

import com.itsaky.androidide.plugins.tasks.javac.JavacExtractClassesTask
import com.itsaky.androidide.plugins.tasks.javac.JavacFinalizeTask
import com.itsaky.androidide.plugins.tasks.javac.JavacJackpotTask
import com.itsaky.androidide.plugins.tasks.javac.JavacPropertiesParserTask
import com.itsaky.androidide.plugins.tasks.javac.JavacRenamePackageReferencesTask
import com.itsaky.androidide.plugins.tasks.javac.JdkSourceDownloadTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile

/**
 * Downloads JDK source, extracts source files for the Java compiler and patches them so that
 * they can be used on Android.
 *
 * @author Akash Yadav
 */
class JavacJackpotPlugin : Plugin<Project> {

  companion object {

    /**
     * Files to copy as-is from JDK source. The keys are the JDK module names and the values are
     * glob paths to the files in <module>/share/classes directory, minus the '.java' extension.
     *
     * IMPORTANT: Package names of all the classes are prefixed with `com.itsaky.androidide` in order
     * to avoid conflicts with the classes defined in the JDK.
     */
    private val copySourceFiles = listOf(
      "java.base" to "jdk/internal/misc/PreviewFeatures",
      "java.base" to "jdk/internal/misc/MainMethodFinder",
      "java.base" to "jdk/internal/javac/*",
    )
  }

  override fun apply(target: Project) {
    target.run {
      val javacAndroid = layout.buildDirectory.dir("javac-android")
      val jdkSource = javacAndroid.get().dir("jdk")

      val downloadTask = tasks.register("downloadJdkSource", JdkSourceDownloadTask::class.java)

      val deleteExtractedSourceTask = tasks.register("deleteExtractedJdkSource",
        Delete::class.java) {
        dependsOn(downloadTask)
        delete(jdkSource)
      }

      val extractJdkSourceTask = tasks.register("extractJdkSource", Copy::class.java) {
        dependsOn(deleteExtractedSourceTask)

        from(tarTree(downloadTask.get().outputFile))
        into(jdkSource)
      }

      val jackpotTaskName = "javacJackpot"
      val transformedJavacSourceDir = javacAndroid.get().dir(jackpotTaskName)

      val deleteTransformedSourcesTask = tasks.register(
        "deleteTransformedJavacSources",
        Delete::class.java
      ) {
        dependsOn(extractJdkSourceTask)
        delete(transformedJavacSourceDir)
      }

      val copiedSourceFilesDir = javacAndroid.get().dir("copiedSources")
      val copySourcesTask = tasks.register("copySourceFiles", Copy::class.java) {
        dependsOn(deleteTransformedSourcesTask)

        copySourceFiles.forEach { (module, files) ->
          from(
            jdkSource.dir(downloadTask.get().jdkSourceDirName()).dir("src/${module}/share/classes")
          )
          include("${files}.java")
        }

        into(copiedSourceFilesDir.dir("com/itsaky/androidide"))
      }

      val jackpotTask = tasks.register(jackpotTaskName, JavacJackpotTask::class.java) {
        dependsOn(copySourcesTask)
        source(project.fileTree("src/main/resources").matching { include("**/*.hint") })
        jdkSourceDirectory.set(jdkSource.dir(downloadTask.get().jdkSourceDirName()))
        sourceOutDirectory.set(transformedJavacSourceDir)
      }

      val deleteInfoFilesTask = tasks.register("deleteInfoFiles", Delete::class.java) {
        delete(project.fileTree(transformedJavacSourceDir).also {
          dependsOn(jackpotTask)
          it.include("**/module-info.java", "**/package-info.java")
        })
      }

      val renamePackageReferencesTask = tasks.register("renamePackageReferences",
        JavacRenamePackageReferencesTask::class.java) {
        dependsOn(deleteInfoFilesTask)
        source(transformedJavacSourceDir.asFileTree.matching { include("**/*.java") })
        source(copiedSourceFilesDir)
      }

      val compilePropertiesTask = target.tasks.register("compileProperties",
        JavacPropertiesParserTask::class.java) {
        dependsOn(renamePackageReferencesTask)
        sourceDir.set(transformedJavacSourceDir.dir("jdk.compiler"))
        outputs.dir(getPropsDir())
      }

      val finalTask = tasks.register("finalizeJavacTask", JavacFinalizeTask::class.java) {
        dependsOn(compilePropertiesTask)
      }

      tasks.withType(JavaCompile::class.java) {
        dependsOn(finalTask)

        options.compilerArgs.add("--add-exports=java.base/sun.reflect.annotation=ALL-UNNAMED")
      }

      extensions.getByType(JavaPluginExtension::class.java).run {
        sourceSets.getByName("main").java.srcDirs(
          JavacJackpotTask.getTransformedSourceDirectories(transformedJavacSourceDir.asFile),
          copiedSourceFilesDir,
          compilePropertiesTask.get().getPropsDir()
        )
      }
    }
  }
}