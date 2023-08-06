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

/**
 * Compiler settings for [JavaModule].
 *
 * @author Akash Yadav
 */
class JavaModuleCompilerSettings(
  override val javaSourceVersion: String,
  override val javaBytecodeVersion: String
) : IJavaCompilerSettings(), java.io.Serializable {

  // IMPORTANT
  // Do not use javax.lang.model.SourceVersion reference here
  // When running on Android, this class is preset as jdkx.lang.model.SourceVersion
  // Using the 'javax' reference will result a 'ClassNotFoundException' while deserializing the
  // JSONRpc data received from the Tooling API server
  constructor() : this("RELEASE_11", "RELEASE_11")
}