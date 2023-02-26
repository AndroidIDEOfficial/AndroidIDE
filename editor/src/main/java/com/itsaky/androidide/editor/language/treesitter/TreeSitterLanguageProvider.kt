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
import com.itsaky.androidide.editor.language.java.JavaLanguage
import com.itsaky.androidide.editor.language.json.JsonLanguage
import com.itsaky.androidide.editor.language.xml.XMLLanguage
import com.itsaky.androidide.editor.schemes.LanguageSpecProvider.BASE_SPEC_PATH
import java.io.File
import java.io.IOException

/**
 * Provides instance of [TreeSitterLanguage] implementations.
 *
 * @author Akash Yadav
 */
object TreeSitterLanguageProvider {

  fun forFile(file: File, context: Context): TreeSitterLanguage? {
    val type = file.extension
  
    try {
      // check if there is at least highlights.scm file for this file type
      context.assets.open("${BASE_SPEC_PATH}/${type}/highlights.scm").close()
    } catch (e: IOException) {
      return null
    }
  
    return when (type) {
      "java" -> JavaLanguage(context)
      "json" -> JsonLanguage(context)
      "xml" -> XMLLanguage(context)
      else -> return null
    }
  }
}
