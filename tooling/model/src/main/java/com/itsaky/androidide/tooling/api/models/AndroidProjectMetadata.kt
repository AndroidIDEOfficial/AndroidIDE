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

package com.itsaky.androidide.tooling.api.models

import com.itsaky.androidide.builder.model.DefaultAndroidGradlePluginProjectFlags
import com.itsaky.androidide.builder.model.DefaultJavaCompileOptions
import com.itsaky.androidide.builder.model.DefaultViewBindingOptions
import com.itsaky.androidide.tooling.api.ProjectType
import java.io.File

/**
 * Metadata about an Android project.
 *
 * @author Akash Yadav
 */
class AndroidProjectMetadata(
  name: String?,
  path: String,
  projectDir: File,
  buildDir: File,
  description: String?,
  buildScript: File,
  type: ProjectType,
  val androidType: AndroidProjectType,
  val flags: DefaultAndroidGradlePluginProjectFlags,
  val javaCompileOptions: DefaultJavaCompileOptions,
  val viewBindingOptions: DefaultViewBindingOptions,
  val resourcePrefix: String?,
  val namespace: String?,
  val androidTestNamespace: String?,
  val testFixtureNamespace: String?,
  override val classesJar: File?
) : ProjectMetadata(name, path, projectDir, buildDir, description, buildScript, type),
  IModuleMetadata {

  constructor(
    base: ProjectMetadata,
    androidType: AndroidProjectType,
    flags: DefaultAndroidGradlePluginProjectFlags,
    javaCompileOptiosn: DefaultJavaCompileOptions,
    viewBindingOptions: DefaultViewBindingOptions,
    resourcePrefix: String?,
    namespace: String?,
    androidTestNamespace: String?,
    testFixtureNamespace: String?,
    classesJar: File?) :

      this(
        base.name,
        base.projectPath,
        base.projectDir,
        base.buildDir,
        base.description,
        base.buildScript,
        base.type,
        androidType,
        flags,
        javaCompileOptiosn,
        viewBindingOptions,
        resourcePrefix,
        namespace,
        androidTestNamespace,
        testFixtureNamespace,
        classesJar
      )
}

typealias AndroidProjectType = com.android.builder.model.v2.ide.ProjectType