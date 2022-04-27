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
class DefaultVariant : Variant {
    override var androidTestArtifact: AndroidArtifact? = null
    override var buildType: String? = null
    override var desugaredMethods: List<String> = emptyList()
    override var displayName: String = ""
    override var isInstantAppCompatible: Boolean = false
    override var mainArtifact: AndroidArtifact = DefaultAndroidArtifact()
    override var name: String = ""
    override var productFlavors: List<String> = emptyList()
    override var testFixturesArtifact: AndroidArtifact? = null
    override var testedTargetVariant: TestedTargetVariant? = null
    override var unitTestArtifact: JavaArtifact? = null

    override fun toString(): String {
        return "DefaultVariant(androidTestArtifact=$androidTestArtifact, buildType=$buildType, desugaredMethods=$desugaredMethods, displayName='$displayName', isInstantAppCompatible=$isInstantAppCompatible, mainArtifact=$mainArtifact, name='$name', productFlavors=$productFlavors, testFixturesArtifact=$testFixturesArtifact, testedTargetVariant=$testedTargetVariant, unitTestArtifact=$unitTestArtifact)"
    }
}
