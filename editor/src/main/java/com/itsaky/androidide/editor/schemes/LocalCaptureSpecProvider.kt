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

package com.itsaky.androidide.editor.schemes

import io.github.rosemoe.sora.editor.ts.LocalsCaptureSpec

/**
 * Provides local
 *
 * @author Akash Yadav */
object LocalCaptureSpecProvider {

  @JvmStatic
  fun newLocalCaptureSpec(type: String): LocalsCaptureSpec {
    val scheme = IDEColorSchemeProvider.scheme
    val lang = scheme.languages[type] ?: return LocalsCaptureSpec.DEFAULT
    return object : LocalsCaptureSpec() {

      override fun isDefinitionCapture(captureName: String): Boolean {
        return lang.isLocalDef(captureName)
      }

      override fun isReferenceCapture(captureName: String): Boolean {
        return lang.isLocalRef(captureName)
      }

      override fun isScopeCapture(captureName: String): Boolean {
        return lang.isLocalScope(captureName)
      }
    }
  }
}
