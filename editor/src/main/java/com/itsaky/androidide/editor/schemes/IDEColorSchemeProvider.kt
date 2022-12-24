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

import com.itsaky.androidide.editor.schemes.internal.parser.SchemeParser
import com.itsaky.androidide.tasks.executeAsyncProvideError
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.util.function.Consumer

/** @author Akash Yadav */
object IDEColorSchemeProvider {

  private val log = ILogger.newInstance("IDEColorSchemeProvider")

  val scheme: IDEColorScheme by lazy {
    val baseSchemePath = "editor/schemes/default"
    SchemeParser { File(Environment.ANDROIDIDE_UI, "$baseSchemePath/$it") }
      .parse(File(Environment.ANDROIDIDE_UI, "$baseSchemePath/default.json"))
  }
  
  @JvmStatic
  fun init() {
    scheme
  }

  fun readScheme(schemeConsumer: Consumer<IDEColorScheme>) {
    readScheme(schemeConsumer::accept)
  }
  
  fun readScheme(consume: (IDEColorScheme) -> Unit) {
    executeAsyncProvideError({ this.scheme }) { scheme, error ->
      if (scheme == null || error != null) {
        log.error("Failed to read color scheme", error)
        return@executeAsyncProvideError
      }
      consume(scheme)
    }
  }
}
