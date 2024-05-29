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
import com.itsaky.androidide.templates.base.modules.android.androidGitignoreSrc
import com.itsaky.androidide.templates.base.modules.android.buildGradleSrc
import com.itsaky.androidide.templates.base.modules.android.proguardRules
import com.itsaky.androidide.templates.base.util.AndroidManifestBuilder
import com.itsaky.androidide.templates.base.util.AndroidModuleResManager
import com.itsaky.androidide.templates.base.util.stringRes
import com.squareup.javapoet.TypeSpec
import java.io.File

class AndroidModuleTemplateBuilder : ModuleTemplateBuilder() {

  /**
   * Set whether this Android module is a Jetpack Compose module or not.
   *
   * If this is set to `true`, then compose-specific configurations will be
   * added to the `build.gradle[.kts]` file.
   */
  var isComposeModule = false

  val manifest = AndroidManifestBuilder()
  val res = AndroidModuleResManager()

  /**
   * Return the file path to `AndroidManifest.xml`.
   */
  fun manifestFile(): File {
    return File(srcFolder(SrcSet.Main),
      ANDROID_MANIFEST_XML).also { it.parentFile!!.mkdirs() }
  }

  /**
   * Configure the properties for `AndroidManifest.xml` file.
   */
  inline fun manifest(crossinline block: AndroidManifestBuilder.() -> Unit) {
    manifest.apply(block)
  }

  /**
   * Get the Android `res` directory for [main][SrcSet.Main] source set in this module.
   *
   * @return The `res` directory for the [main][SrcSet.Main] source set.
   */
  fun mainResDir(): File {
    return resDir(SrcSet.Main)
  }

  /**
   * Get the Android `res` directory for the given [source set][srcSet] in this module.
   *
   * @return The `res` directory.
   */
  fun resDir(srcSet: SrcSet): File {
    return File(srcFolder(srcSet), "res").also { it.mkdirs() }
  }

  /**
   * Configure the resources in the module.
   *
   * @param configure Function to configure the resources.
   */
  inline fun RecipeExecutor.res(crossinline configure: AndroidModuleResManager.() -> Unit) {
    res.apply(configure)
  }

  /**
   * Copy the default resources (without `values` directory) to this module.
   */
  fun RecipeExecutor.copyDefaultRes() {
    copyAssetsRecursively(baseAsset("res"), mainResDir())
  }

  /**
   * Creates a new activity class in the application/library package.
   *
   * @param name The name of the class.
   */
  inline fun RecipeExecutor.createActivity(name: String = "MainActivity",
                                    crossinline configure: TypeSpec.Builder.() -> Unit
  ) {
    sources {
      createClass(data.packageName, name, configure)
    }
  }

  override fun baseAsset(path: String): String {
    return super.baseAsset("android/${path}")
  }

  override fun RecipeExecutor.preConfig() {
    manifest.apply {
      packageName = data.packageName
      isLibrary = data.type == AndroidLibrary
    }

    // Copy the proguard-rules.pro file
    proguardRules()
  }

  override fun RecipeExecutor.postConfig() {

    // Write .gitignore
    gitignore()

    manifest.apply {
      generate(manifestFile())
    }

    res {
      data.appName?.let { putStringRes(manifest.appLabelRes, it) }

      if (strings.isNotEmpty()) {
        createValuesResource("strings") {
          linefeed()
          strings.forEach { (name, value) ->
            stringRes(name, value)
          }
          linefeed()
        }
      }
    }
  }

  override fun RecipeExecutor.buildGradle() {
    save(buildGradleSrc(isComposeModule), buildGradleFile())
  }

  /**
   * Writes the `.gitignore` file in the mdoule directory.
   */
  fun RecipeExecutor.gitignore() {
    val gitignore = File(data.projectDir, ".gitignore")
    save(androidGitignoreSrc(), gitignore)
  }
}