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

package com.itsaky.androidide.editor.language.kotlin

import android.content.Context
import com.itsaky.androidide.editor.language.treesitter.TreeSitterLanguage
import com.itsaky.androidide.treesitter.kotlin.TSLanguageKotlin

/**
 * [TreeSitterLanguage] implementation for Kotlin.
 *
 * @author Akash Yadav
 */
open class KotlinLanguage(context: Context) :
  TreeSitterLanguage(context, TSLanguageKotlin.newInstance(), TS_TYPE_KT) {

  companion object {
    val FACTORY = Factory { KotlinLanguage(it) }
    const val TS_TYPE_KT = "kt"
    const val TS_TYPE_KTS = "kts"
  }

  override fun getInterruptionLevel(): Int {
    return INTERRUPTION_LEVEL_STRONG
  }
}
