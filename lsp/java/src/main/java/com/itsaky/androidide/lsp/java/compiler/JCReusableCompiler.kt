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

import com.itsaky.androidide.javac.services.compiler.ReusableCompiler
import com.itsaky.androidide.javac.services.compiler.ReusableContext

/**
 * Implementation of the [ReusableCompiler] which replaces necessary components from the
 * [ReusableContext].
 *
 * @author Akash Yadav
 */
class JCReusableCompiler : ReusableCompiler() {

  override fun onCreateContext(): ReusableContext {
    return super.onCreateContext().also {
      JavaCompilerImpl.preRegister(context = it, replace = true)
    }
  }
}
