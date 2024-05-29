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

import com.android.builder.model.v2.ide.SourceProvider
import java.io.File
import java.io.Serializable

/** @author Akash Yadav */
class DefaultSourceProvider() : SourceProvider, Serializable {
  private val serialVersionUID = 1L
  override var aidlDirectories: Collection<File>? = null
  override var assetsDirectories: Collection<File>? = null
  override var customDirectories: Collection<DefaultCustomSourceDirectory>? = null
  override var javaDirectories: Collection<File> = emptyList()
  override var jniLibsDirectories: Collection<File> = emptyList()
  override var kotlinDirectories: Collection<File> = emptyList()
  override var manifestFile: File = NoFile
  override var mlModelsDirectories: Collection<File>? = null
  override var name: String = ""
  override var renderscriptDirectories: Collection<File>? = null
  override var resDirectories: Collection<File>? = null
  override var resourcesDirectories: Collection<File> = emptyList()
  override var shadersDirectories: Collection<File>? = null
  override var baselineProfileDirectories: Collection<File>? = null

  companion object {
    @JvmStatic val NoFile = File("<does-not-exist>")
  }
}
