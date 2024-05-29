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

import com.itsaky.androidide.builder.model.IJavaCompilerSettings
import com.itsaky.androidide.tooling.api.ProjectType
import java.io.File

/**
 * Metadata about a Java project.
 *
 * @property compilerSettings Compiler settings for this project.
 * @author Akash Yadav
 */
class JavaProjectMetadata(
  name: String?,
  path: String,
  projectDir: File,
  buildDir: File,
  description: String?,
  buildScript: File,
  type: ProjectType,
  val compilerSettings: IJavaCompilerSettings,
  override val classesJar: File?
) : ProjectMetadata(name, path, projectDir, buildDir, description, buildScript, type),
  IModuleMetadata {

  constructor(base: ProjectMetadata, compilerSettings: IJavaCompilerSettings, classesJar: File?) :
      this(
        base.name,
        base.projectPath,
        base.projectDir,
        base.buildDir,
        base.description,
        base.buildScript,
        base.type,
        compilerSettings,
        classesJar
      )
}