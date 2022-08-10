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

package com.itsaky.androidide.lsp.java.models

import com.itsaky.androidide.lsp.java.compiler.CompilationTaskProcessor
import com.itsaky.androidide.lsp.java.compiler.DefaultCompilationTaskProcessor
import javax.tools.JavaFileObject

/**
 * Data sent to compiler to request a compilation.
 *
 * @author Akash Yadav
 * @param sources The source files to compile.
 * @param partialRequest Data that will be used to a partial reparse.
 */
data class CompilationRequest
@JvmOverloads
constructor(
  @JvmField val sources: Collection<JavaFileObject>,
  @JvmField val partialRequest: PartialReparseRequest? = null,
  @JvmField
  val compilationTaskProcessor: CompilationTaskProcessor = DefaultCompilationTaskProcessor()
)
