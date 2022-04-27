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

import com.android.builder.model.v2.ide.SourceProvider
import java.io.File

/** @author Akash Yadav */
class DefaultSourceProvider(
    override val aidlDirectories: Collection<File>?,
    override val assetsDirectories: Collection<File>?,
    override val javaDirectories: Collection<File>,
    override val jniLibsDirectories: Collection<File>,
    override val kotlinDirectories: Collection<File>,
    override val manifestFile: File,
    override val mlModelsDirectories: Collection<File>?,
    override val name: String,
    override val renderscriptDirectories: Collection<File>?,
    override val resDirectories: Collection<File>?,
    override val resourcesDirectories: Collection<File>,
    override val shadersDirectories: Collection<File>?
) : SourceProvider {
    constructor() :
        this(
            null,
            null,
            emptyList(),
            emptyList(),
            emptyList(),
            File("."),
            null,
            "",
            null,
            null,
            emptyList(),
            null)

    override fun toString(): String {
        return "DefaultSourceProvider(aidlDirectories=$aidlDirectories, assetsDirectories=$assetsDirectories, javaDirectories=$javaDirectories, jniLibsDirectories=$jniLibsDirectories, kotlinDirectories=$kotlinDirectories, manifestFile=$manifestFile, mlModelsDirectories=$mlModelsDirectories, name='$name', renderscriptDirectories=$renderscriptDirectories, resDirectories=$resDirectories, resourcesDirectories=$resourcesDirectories, shadersDirectories=$shadersDirectories)"
    }
}
