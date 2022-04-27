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

import com.android.builder.model.v2.ide.AndroidArtifact
import com.android.builder.model.v2.ide.JavaArtifact
import com.android.builder.model.v2.ide.TestedTargetVariant
import com.android.builder.model.v2.ide.Variant

/** @author Akash Yadav */
class DefaultVariant(
    override val androidTestArtifact: AndroidArtifact?,
    override val buildType: String?,
    override val desugaredMethods: List<String>,
    override val displayName: String,
    override val isInstantAppCompatible: Boolean,
    override val mainArtifact: AndroidArtifact,
    override val name: String,
    override val productFlavors: List<String>,
    override val testFixturesArtifact: AndroidArtifact?,
    override val testedTargetVariant: TestedTargetVariant?,
    override val unitTestArtifact: JavaArtifact?
) : Variant
