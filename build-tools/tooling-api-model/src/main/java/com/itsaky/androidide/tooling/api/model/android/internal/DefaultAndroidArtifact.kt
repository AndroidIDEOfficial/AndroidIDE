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

package com.itsaky.androidide.tooling.api.model.android.internal

import com.android.builder.model.v2.ModelSyncFile
import com.android.builder.model.v2.ide.AndroidArtifact
import com.android.builder.model.v2.ide.ApiVersion
import com.android.builder.model.v2.ide.BundleInfo
import com.android.builder.model.v2.ide.CodeShrinker
import com.android.builder.model.v2.ide.SourceProvider
import com.android.builder.model.v2.ide.TestInfo
import java.io.File

/** @author Akash Yadav */
class DefaultAndroidArtifact(
    override val abiFilters: Set<String>?,
    override val assembleTaskOutputListingFile: File?,
    override val bundleInfo: BundleInfo?,
    override val codeShrinker: CodeShrinker?,
    override val generatedResourceFolders: Collection<File>,
    override val isSigned: Boolean,
    override val maxSdkVersion: Int?,
    override val minSdkVersion: ApiVersion,
    override val signingConfigName: String?,
    override val sourceGenTaskName: String,
    override val testInfo: TestInfo?,
    override val assembleTaskName: String,
    override val classesFolders: Set<File>,
    override val compileTaskName: String,
    override val generatedSourceFolders: Collection<File>,
    override val ideSetupTaskNames: Set<String>,
    override val modelSyncFiles: Collection<ModelSyncFile>,
    override val multiFlavorSourceProvider: SourceProvider?,
    override val variantSourceProvider: SourceProvider?
) : AndroidArtifact
