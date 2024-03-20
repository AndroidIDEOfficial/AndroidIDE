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

package com.itsaky.androidide.tooling.impl

import com.android.builder.model.v2.ide.LibraryType
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.IJavaProject
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.ProjectType
import com.itsaky.androidide.tooling.api.models.AndroidProjectMetadata
import com.itsaky.androidide.tooling.api.models.JavaModuleExternalDependency
import com.itsaky.androidide.tooling.api.models.JavaModuleProjectDependency
import com.itsaky.androidide.tooling.api.models.JavaProjectMetadata
import com.itsaky.androidide.tooling.api.models.params.StringParameter

internal fun performBasicProjectAssertions(project: IProject, server: IToolingApiServer) {
  assertThat(project).isNotNull()

  // As the returned project is just a proxy,
  // project instanceOf IdeGradleProject will always return false
  val isInitialized = server.isServerInitialized().get()
  assertThat(isInitialized).isTrue()

  var selectionResult = project.selectProject(StringParameter("")).get()
  assertThat(selectionResult.isSuccessful).isTrue()
  assertThat(project.getType().get()).isEqualTo(ProjectType.Gradle)

  selectionResult = project.selectProject(StringParameter(":app")).get()
  assertThat(selectionResult).isNotNull()
  assertThat(selectionResult.isSuccessful).isTrue()

  val app = project.asAndroidProject()
  assertThat(app).isNotNull()
  assertThat(app).isInstanceOf(IAndroidProject::class.java)

  var metadata = app.getMetadata().get() as AndroidProjectMetadata
  assertThat(metadata).isNotNull()
  assertThat(metadata.javaCompileOptions.sourceCompatibility).isEqualTo("11")
  assertThat(metadata.javaCompileOptions.targetCompatibility).isEqualTo("11")
  assertThat(metadata.javaCompileOptions.javaSourceVersion).isEqualTo("11")
  assertThat(metadata.javaCompileOptions.javaBytecodeVersion).isEqualTo("11")
  assertThat(metadata.javaCompileOptions.isCoreLibraryDesugaringEnabled).isFalse()
  assertThat(metadata.androidType)
    .isEqualTo(com.android.builder.model.v2.ide.ProjectType.APPLICATION)
  assertThat(metadata.namespace).isEqualTo("com.itsaky.test.app")
  assertThat(metadata.viewBindingOptions).isNotNull()
  assertThat(metadata.viewBindingOptions.isEnabled).isTrue()

  // There are always more than 100 tasks in an android module
  // Also, the tasks must contain the user defined tasks
  val tasks = app.getTasks().get()
  assertThat(tasks.size).isAtLeast(100)
  assertThat(tasks.first { it.path == "${metadata.projectPath}:thisIsATestTask" }).isNotNull()

  var libraries = app.getLibraryMap().get()
  // At least one project library
  assertThat(libraries.values.filter { it.type == LibraryType.PROJECT }).isNotEmpty()
  // :app module includes :java-library as a dependency. But it is not transitive
  assertThat(
    libraries.values.firstOrNull {
      it.type == LibraryType.PROJECT &&
          it.projectInfo!!.projectPath == ":another-java-library" &&
          it.projectInfo!!.attributes["org.gradle.usage"] == "java-api"
    }
  )
    .isNull()

  selectionResult = project.selectProject(StringParameter(":android-library")).get()
  assertThat(selectionResult.isSuccessful).isTrue()

  val androidLib = project.asAndroidProject()
  assertThat(androidLib).isNotNull()
  assertThat(androidLib).isInstanceOf(IAndroidProject::class.java)
  // Make sure that transitive dependencies are included here because :android-library includes
  // :java-library project which further includes :another-java-libraries project with 'api'
  // configuration
  libraries = androidLib.getLibraryMap().get()!!
  assertThat(
    libraries.values.firstOrNull {
      it.type == LibraryType.PROJECT &&
          it.projectInfo!!.projectPath == ":another-java-library" &&
          it.projectInfo!!.attributes["org.gradle.usage"] == "java-api"
    }
  )
    .isNotNull()

  metadata = androidLib.getMetadata().get() as AndroidProjectMetadata
  assertThat(metadata.javaCompileOptions.javaSourceVersion).isEqualTo("11")
  assertThat(metadata.javaCompileOptions.javaBytecodeVersion).isEqualTo("11")

  selectionResult = project.selectProject(StringParameter(":java-library")).get()
  assertThat(selectionResult.isSuccessful).isTrue()

  val javaLibrary = project.asJavaProject()
  assertThat(javaLibrary).isNotNull()
  assertThat(javaLibrary).isInstanceOf(IJavaProject::class.java)

  var javaMetadata = javaLibrary.getMetadata().get() as JavaProjectMetadata
  assertThat(javaMetadata.compilerSettings.javaSourceVersion).isEqualTo("11")
  assertThat(javaMetadata.compilerSettings.javaBytecodeVersion).isEqualTo("11")

  val javaDependencies = javaLibrary.getDependencies().get()
  assertThat(
    javaDependencies.firstOrNull {
      it is JavaModuleExternalDependency &&
          it.gradleArtifact != null &&
          it.run {
            gradleArtifact!!.group == "io.github.itsaky" &&
                gradleArtifact!!.name == "nb-javac-android" &&
                gradleArtifact!!.version == "17.0.0.0"
          }
    }
  )
    .isNotNull()
  assertThat(
    javaDependencies.firstOrNull {
      it is JavaModuleProjectDependency && it.moduleName == "another-java-library"
    }
  )
    .isNotNull()
  // In case we have multiple dependencies with same name but different path
  val nested =
    javaDependencies
      .filterIsInstance(JavaModuleProjectDependency::class.java)
      .filter { it.moduleName.endsWith("nested-java-library") }
  assertThat(nested).hasSize(2)
  assertThat(nested[0].projectPath).isNotEqualTo(nested[1].projectPath)
  assertThat(
    project.selectProject(StringParameter(":does-not-exist")).get().isSuccessful).isFalse()

  selectionResult = project.selectProject(StringParameter(":another-java-library")).get()
  assertThat(selectionResult.isSuccessful).isTrue()

  val anotherJavaLib = project.asJavaProject()
  assertThat(anotherJavaLib).isNotNull()
  assertThat(anotherJavaLib).isInstanceOf(IJavaProject::class.java)

  javaMetadata = anotherJavaLib.getMetadata().get() as JavaProjectMetadata
  assertThat(javaMetadata.compilerSettings.javaSourceVersion).isEqualTo("1.8")
  assertThat(javaMetadata.compilerSettings.javaBytecodeVersion).isEqualTo("1.8")
}
