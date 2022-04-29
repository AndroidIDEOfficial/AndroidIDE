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
package com.itsaky.androidide.tooling.api.model.internal

import com.android.builder.model.v2.ide.AndroidArtifact
import com.android.builder.model.v2.ide.CodeShrinker
import java.io.File

/** @author Akash Yadav */
class DefaultAndroidArtifact : AndroidArtifact {

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
    override var multiFlavorSourceProvider: DefaultSourceProvider? = null
    override var variantSourceProvider: DefaultSourceProvider? = null
    override var targetSdkVersionOverride: DefaultApiVersion? = null
    override val modelSyncFiles: Collection<DefaultModelSyncFile> = emptyList()

    override fun toString(): String {
        return "DefaultAndroidArtifact(abiFilters=$abiFilters, assembleTaskOutputListingFile=$assembleTaskOutputListingFile, bundleInfo=$bundleInfo, codeShrinker=$codeShrinker, generatedResourceFolders=$generatedResourceFolders, isSigned=$isSigned, maxSdkVersion=$maxSdkVersion, minSdkVersion=$minSdkVersion, signingConfigName=$signingConfigName, sourceGenTaskName='$sourceGenTaskName', testInfo=$testInfo, assembleTaskName='$assembleTaskName', classesFolders=$classesFolders, compileTaskName='$compileTaskName', generatedSourceFolders=$generatedSourceFolders, ideSetupTaskNames=$ideSetupTaskNames, multiFlavorSourceProvider=$multiFlavorSourceProvider, variantSourceProvider=$variantSourceProvider, targetSdkVersionOverride=$targetSdkVersionOverride, modelSyncFiles=$modelSyncFiles)"
    }
}
