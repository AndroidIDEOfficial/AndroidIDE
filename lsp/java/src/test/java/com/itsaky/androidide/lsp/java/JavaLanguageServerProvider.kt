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

package com.itsaky.androidide.lsp.java

import com.itsaky.androidide.lsp.api.ILanguageServer
import com.itsaky.androidide.lsp.api.LanguageServerProvider
import com.itsaky.androidide.lsp.java.JavaLanguageServer

class JavaLanguageServerProvider : LanguageServerProvider() {
  private val server: JavaLanguageServer = JavaLanguageServer()
  override fun getServer(): ILanguageServer {
    return server
  }

  companion object {
    @JvmStatic val INSTANCE: LanguageServerProvider = JavaLanguageServerProvider()
  }
}
