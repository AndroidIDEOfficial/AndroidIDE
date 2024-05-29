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

package com.itsaky.androidide.javac.services.compiler

import com.itsaky.androidide.javac.services.NBJavaCompiler
import openjdk.tools.javac.main.JavaCompiler
import openjdk.tools.javac.util.Context

/**
 * Reusable JavaCompiler; exposes a method to clean up the component from leftovers associated
 * with previous compilations.
 */
open class ReusableJavaCompiler(context: Context?) : NBJavaCompiler(context) {
  
  companion object {
    val factory = Context.Factory<JavaCompiler> { ReusableJavaCompiler(it) }
  }
  
  override fun checkReusable() {
    // Do nothing
  }
  
  override fun close() {
    // Do nothing
  }
  
  fun clear()   {
    newRound()
  }
}