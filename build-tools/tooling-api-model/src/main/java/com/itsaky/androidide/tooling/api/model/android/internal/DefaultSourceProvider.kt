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
class DefaultSourceProvider() : SourceProvider {

    override var aidlDirectories: Collection<File>? = null
    override var assetsDirectories: Collection<File>? = null
    override var javaDirectories: Collection<File> = emptyList()
    override var jniLibsDirectories: Collection<File> = emptyList()
    override var kotlinDirectories: Collection<File> = emptyList()
    override var manifestFile: File = File(".")
    override var mlModelsDirectories: Collection<File>? = null
    override var name: String = ""
    override var renderscriptDirectories: Collection<File>? = null
    override var resDirectories: Collection<File>? = null
    override var resourcesDirectories: Collection<File> = emptyList()
    override var shadersDirectories: Collection<File>? = null

    override fun toString(): String {
        return "DefaultSourceProvider(aidlDirectories=$aidlDirectories, assetsDirectories=$assetsDirectories, javaDirectories=$javaDirectories, jniLibsDirectories=$jniLibsDirectories, kotlinDirectories=$kotlinDirectories, manifestFile=$manifestFile, mlModelsDirectories=$mlModelsDirectories, name='$name', renderscriptDirectories=$renderscriptDirectories, resDirectories=$resDirectories, resourcesDirectories=$resourcesDirectories, shadersDirectories=$shadersDirectories)"
    }
}
