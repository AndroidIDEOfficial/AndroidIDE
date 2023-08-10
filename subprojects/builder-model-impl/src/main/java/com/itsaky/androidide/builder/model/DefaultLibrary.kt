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

import com.android.builder.model.v2.ide.Library
import com.android.builder.model.v2.ide.LibraryType
import com.android.builder.model.v2.ide.LibraryType.ANDROID_LIBRARY
import java.io.File
import java.io.Serializable

/** @author Akash Yadav */
class DefaultLibrary : Library, Serializable {
  private val serialVersionUID = 1L
  override var androidLibraryData: DefaultAndroidLibraryData? = null
  override var artifact: File? = null
  override var srcJar: File? = null
  override var docJar: File? = null
  override var samplesJar: File? = null
  override var key: String = ""
  override var libraryInfo: DefaultLibraryInfo? = null
  override var lintJar: File? = null
  override var projectInfo: DefaultProjectInfo? = null
  override var type: LibraryType = ANDROID_LIBRARY

  /** Dependencies of this library. */
  val dependencies = mutableSetOf<String>()

  /**
   * Whether an attempt should be made to lookup this library's package name.
   *
   * FOR INTERNAL USE ONLY!
   */
  var lookupPackage: Boolean = true

  /**
   * The package name of this library. MUST NOT be accesed directly. Use
   * `DefaultLibrary.findPackageName()` method defined in the `:subprojects:tooling-api-models`
   * module.
   */
  var packageName: String = ""
}
