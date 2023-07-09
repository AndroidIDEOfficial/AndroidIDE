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

package com.itsaky.androidide.utils

import com.blankj.utilcode.util.ResourceUtils
import com.itsaky.androidide.app.IDEApplication
import com.itsaky.androidide.templates.RecipeExecutor
import java.io.File
import java.io.InputStream

/**
 * [RecipeExecutor] implementation used for creating projects.
 *
 * @author Akash Yadav
 */
class TemplateRecipeExecutor : RecipeExecutor {

  private val application: IDEApplication
    get() = IDEApplication.instance

  override fun copy(source: File, dest: File) {
    source.copyTo(dest)
  }

  override fun save(source: String, dest: File) {
    dest.parentFile?.mkdirs()
    dest.writeText(source)
  }

  override fun openAsset(path: String): InputStream {
    try {
      return application.assets.open(path)
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }

  override fun copyAsset(path: String, dest: File) {
    openAsset(path).use {
      it.copyTo(dest.outputStream())
    }
  }

  override fun copyAssetsRecursively(path: String, destDir: File) {
    ResourceUtils.copyFileFromAssets(path, destDir.absolutePath)
  }
}