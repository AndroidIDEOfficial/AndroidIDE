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

package com.itsaky.androidide.templates

import com.itsaky.androidide.utils.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * [RecipeExecutor] implementation for tests.
 *
 * @author Akash Yadav
 */
class TestRecipeExecutor : RecipeExecutor {

  // Modules whose assets are queried from the templates API
  // order of the element matters!
  private val modulesWithAssets by lazy {
    arrayOf(
      "utilities/templates-api",
      "utilities/templates-impl",
      "core/app"
    )
  }

  override fun copy(source: File, dest: File) {
    source.copyTo(dest)
  }

  override fun save(source: String, dest: File) {
    dest.parentFile?.mkdirs()
    dest.writeText(source)
  }

  override fun openAsset(path: String): InputStream {
    return findAsset(path).inputStream().buffered()
  }

  override fun copyAsset(path: String, dest: File) {
    openAsset(path).use {
      it.copyTo(dest.outputStream())
    }
  }

  override fun copyAssetsRecursively(path: String, destDir: File) {
    findAsset(path, true).copyRecursively(destDir, true)
  }

  private fun findAsset(path: String, isDir: Boolean = false) : File {
    for (module in modulesWithAssets) {
      val moduleDir = File(FileProvider.projectRoot().toFile(), module)
      if (!moduleDir.exists()) {
        throw FileNotFoundException("Module dir '$moduleDir' does not exist")
      }

      var assetDir = File(moduleDir, "src/main/assets")

      // Look for the asset in the static assets directory
      var assetFile = File(assetDir, path)
      if (assetFile.exists() && ((isDir && assetFile.isDirectory) || assetFile.isFile)) {
        return assetFile
      }

      // If not found, then look for it in the generated assets directory
      assetDir = File(moduleDir, "build/generated/assets")
      if (assetDir.exists()) {

        // look in all generated asset directories
        for (dir in assetDir.listFiles()!!) {
          assetFile = File(dir, path)
          if (assetFile.exists() && ((isDir && assetFile.isDirectory) || assetFile.isFile)) {
            return assetFile
          }
        }
      }
    }

    throw FileNotFoundException("Asset with path '$path' not found!")
  }
}