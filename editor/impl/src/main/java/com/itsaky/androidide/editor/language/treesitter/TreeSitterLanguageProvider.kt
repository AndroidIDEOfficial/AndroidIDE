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

package com.itsaky.androidide.editor.language.treesitter

import android.content.Context
import java.io.File

/**
 * Provides instance of [TreeSitterLanguage] implementations.
 *
 * @author Akash Yadav
 */
object TreeSitterLanguageProvider {

  fun hasTsLanguage(file: File) : Boolean {
    return TSLanguageRegistry.instance.hasLanguage(file.extension)
  }

  fun forFile(file: File, context: Context): TreeSitterLanguage? {
    if (!hasTsLanguage(file)) {
      return null
    }

    return forType(file.extension, context)
  }

  fun forType(type: String, context: Context): TreeSitterLanguage? {
    return try {
      TSLanguageRegistry.instance.getFactory<TreeSitterLanguage>(type).create(context)
    } catch (e: TSLanguageRegistry.NotRegisteredException) {
      null
    }
  }
}
