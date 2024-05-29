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

import com.itsaky.androidide.javac.services.NBLog
import jdkx.tools.Diagnostic
import jdkx.tools.DiagnosticListener
import jdkx.tools.JavaFileObject
import openjdk.tools.javac.util.Context
import openjdk.tools.javac.util.DefinedBy
import openjdk.tools.javac.util.DefinedBy.Api.COMPILER
import openjdk.tools.javac.util.Log
import java.io.PrintWriter

/**
 * Reusable Log; exposes a method to clean up the component from leftovers associated with
 * previous compilations.
 */
internal class ReusableLog(var context: Context) : NBLog(context, PrintWriter(System.err)) {
  fun clear() {
    recorded.clear()
    sourceMap.clear()
    nerrors = 0
    nwarnings = 0
    
    // Set a fake listener that will lazily lookup the context for the 'real' listener.
    // Since
    // this field is never updated when a new task is created, we cannot simply reset
    // the field
    // or keep old value. This is a hack to workaround the limitations in the current
    // infrastructure.
    diagListener = object : DiagnosticListener<JavaFileObject?> {
      
      var cachedListener: DiagnosticListener<JavaFileObject>? = null
      
      @Suppress("UNCHECKED_CAST")
      @DefinedBy(COMPILER)
      override fun report(diagnostic: Diagnostic<out JavaFileObject>) {
        if (cachedListener == null) {
          cachedListener = context.get(DiagnosticListener::class.java) as DiagnosticListener<JavaFileObject>?
        }
        cachedListener!!.report(diagnostic)
      }
    }
  }
  
  companion object {
    val factory = Context.Factory<Log> { context: Context -> ReusableLog(context) }
  }
}