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
// Forked from JavacTaskImpl
package com.itsaky.androidide.javac.services.compiler

import com.itsaky.androidide.javac.services.CancelService
import jdkx.tools.DiagnosticListener
import jdkx.tools.JavaFileManager
import jdkx.tools.JavaFileObject
import openjdk.tools.javac.api.JavacTaskImpl
import openjdk.tools.javac.api.JavacTool

/**
 * A pool of reusable JavacTasks. When a task is no valid anymore, it is returned to the pool, and
 * its Context may be reused for future processing in some cases. The reuse is achieved by replacing
 * some components (most notably JavaCompiler and Log) with reusable counterparts, and by cleaning
 * up leftovers from previous compilation.
 *
 * For each combination of options, a separate task/context is created and kept, as most option
 * values are cached inside components themselves.
 *
 * When the compilation redefines sensitive classes (e.g. classes in the the java.* packages), the
 * task/context is not reused.
 *
 * When the task is reused, then packages that were already listed won't be listed again.
 *
 * Care must be taken to only return tasks that won't be used by the original caller.
 *
 * Care must also be taken when custom components are installed, as those are not cleaned when the
 * task/context is reused, and subsequent getTask may return a task based on a context with these
 * custom components.
 *
 * **This is NOT part of any supported API. If you write code that depends on this, you do so at
 * your own risk. This code and its internal interfaces are subject to change or deletion without
 * notice.**
 */
open class ReusableCompiler {
  private val systemProvider = JavacTool.create()
  private val currentOptions = mutableListOf<String>()
  @JvmField var currentContext: ReusableContext? = null

  internal var checkedOut = false

  /**
   * Creates a new task as if by [jdkx.tools.JavaCompiler.getTask] and runs the provided worker with
   * it. The task is only valid while the worker is running. The internal structures may be reused
   * from some previous compilation.
   *
   * @param fileManager a file manager; if `null` use the compiler's standard filemanager
   * @param diagnosticListener a diagnostic listener; if `null` use the compiler's default method
   *   for reporting diagnostics
   * @param options compiler options, `null` means no options
   * @param classes names of classes to be processed by annotation processing, `null` means no class
   *   names
   * @param compilationUnits the compilation units to compile, `null` means no compilation units
   * @return an object representing the compilation
   * @throws RuntimeException if an unrecoverable error occurred in a user supplied component. The
   *   [cause][Throwable.cause] will be the error in user code.
   * @throws IllegalArgumentException if any of the options are invalid, or if any of the given
   *   compilation units are of other kind than [source][JavaFileObject.Kind.SOURCE]
   */
  open fun getTask(
    fileManager: JavaFileManager?,
    diagnosticListener: DiagnosticListener<in JavaFileObject?>?,
    options: Iterable<String>,
    classes: Iterable<String>,
    compilationUnits: Iterable<JavaFileObject?>?
  ): ReusableBorrow {

    if (checkedOut) {
      throw RuntimeException("Compiler is already in-use!")
    }

    checkedOut = true
    val opts = options.toList()
    if (opts != currentOptions) {
      currentOptions.clear()
      currentOptions.addAll(opts)
      currentContext = onCreateContext()
    }

    val task =
      systemProvider.getTask(
        null,
        fileManager,
        diagnosticListener,
        opts,
        classes,
        compilationUnits,
        currentContext
      ) as JavacTaskImpl

    task.addTaskListener(currentContext)

    return onCreateBorrow(task)
  }

  protected open fun onCreateContext(): ReusableContext {
    return ReusableContext(cancelService)
  }

  protected open fun onCreateBorrow(task: JavacTaskImpl): ReusableBorrow {
    return ReusableBorrow(this, task)
  }

  companion object {
    @JvmStatic
    private val cancelService: CancelService = CancelServiceImpl()
  }
}
