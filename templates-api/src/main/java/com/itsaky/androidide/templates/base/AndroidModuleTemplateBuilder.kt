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

package com.itsaky.androidide.templates.base

import com.android.SdkConstants.ANDROID_MANIFEST_XML
import com.itsaky.androidide.templates.ModuleType.AndroidLibrary
import com.itsaky.androidide.templates.RecipeExecutor
import com.itsaky.androidide.templates.SrcSet
import com.itsaky.androidide.templates.base.util.AndroidManifestBuilder
import com.itsaky.androidide.templates.base.modules.android.buildGradleSrc
import java.io.File

class AndroidModuleTemplateBuilder : ModuleTemplateBuilder() {

  val manifestBuilder = AndroidManifestBuilder()

  /**
   * Return the file path to `AndroidManifest.xml`.
   */
  fun manifestFile(): File {
    return File(srcFolder(SrcSet.Main), ANDROID_MANIFEST_XML).also { it.parentFile!!.mkdirs() }
  }

  /**
   * Configure the properties for `AndroidManifest.xml` file.
   */
  fun manifest(block: AndroidManifestBuilder.() -> Unit) {
    manifestBuilder.apply(block)
  }

  override fun RecipeExecutor.preConfig() {
    manifestBuilder.apply {
      packageName = data.packageName
      isLibrary = data.type == AndroidLibrary
    }
  }

  override fun RecipeExecutor.postConfig() {
    manifestBuilder.apply {
      generate(manifestFile())
    }
  }

  override fun RecipeExecutor.buildGradle() {
    save(buildGradleSrc(), buildGradleFile())
  }
}