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
package com.itsaky.androidide.builder.model

import com.android.builder.model.v2.ide.AndroidArtifact
import com.android.builder.model.v2.ide.BytecodeTransformation
import com.android.builder.model.v2.ide.CodeShrinker
import com.android.builder.model.v2.ide.PrivacySandboxSdkInfo
import java.io.File
import java.io.Serializable

/** @author Akash Yadav */
class DefaultAndroidArtifact : AndroidArtifact, Serializable {

  private val serialVersionUID = 1L
  override var applicationId: String? = ""
  override var resGenTaskName: String? = null
  override var abiFilters: Set<String>? = null
  override var assembleTaskOutputListingFile: File? = null
  override var bundleInfo: DefaultBundleInfo? = null
  override var codeShrinker: CodeShrinker? = null
  override var generatedResourceFolders: Collection<File> = emptyList()
  override var isSigned: Boolean = false
  override var maxSdkVersion: Int? = null
  override var minSdkVersion: DefaultApiVersion = DefaultApiVersion()
  override var signingConfigName: String? = null
  override var sourceGenTaskName: String = ""
  override var testInfo: DefaultTestInfo? = null
  override var assembleTaskName: String = ""
  override var classesFolders: Set<File> = emptySet()
  override var compileTaskName: String = ""
  override var generatedSourceFolders: Collection<File> = emptyList()
  override var ideSetupTaskNames: Set<String> = emptySet()
  override var targetSdkVersionOverride: DefaultApiVersion? = null
  override var modelSyncFiles: Collection<Void> = emptyList()
  override var privacySandboxSdkInfo: PrivacySandboxSdkInfo? = null
  override var desugaredMethodsFiles: Collection<File> = emptyList()
  override val generatedClassPaths: Map<String, File> = emptyMap()
  override val bytecodeTransformations: Collection<BytecodeTransformation> = emptyList()
}
