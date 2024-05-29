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

package com.itsaky.androidide.lsp.java.compiler

import com.itsaky.androidide.models.Position
import jdkx.tools.JavaFileObject
import openjdk.tools.javac.util.Context

/**
 * Configuration for the [JavaCompilerImpl].
 *
 * @property files The main files that are being compiled/parsed.
 * @property completionInfo Information about the completion
 * @author Akash Yadav
 */
class JavaCompilerConfig(context: Context) {
  init {
    context.put(compilerConfigKey, this)
  }

  var files: Collection<JavaFileObject>? = null
  var completionInfo: CompletionInfo? = null

  companion object {

    @JvmField val compilerConfigKey = Context.Key<JavaCompilerConfig>()

    @JvmStatic
    fun instance(context: Context): JavaCompilerConfig {
      var instance = context.get(compilerConfigKey)
      if (instance == null) {
        instance = JavaCompilerConfig(context)
      }
      return instance
    }
  }
}

/**
 * Information about the completion request initiated by the Java completion provider.
 *
 * @property cursor The cursor position for the completion.
 * @author Akash Yadav
 */
data class CompletionInfo(val cursor: Position)
