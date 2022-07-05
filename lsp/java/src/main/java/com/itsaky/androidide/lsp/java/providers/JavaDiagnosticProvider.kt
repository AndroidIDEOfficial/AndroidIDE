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
package com.itsaky.androidide.lsp.java.providers

import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.lsp.java.CompilationCancellationException.Companion.isCancelled
import com.itsaky.androidide.lsp.java.FileStore
import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.java.providers.DiagnosticsProvider.findDiagnostics
import com.itsaky.androidide.lsp.models.DiagnosticResult
import java.nio.file.Path
import java.time.Instant
import java.util.concurrent.atomic.*
import java.util.function.*
import com.itsaky.androidide.javac.services.CancelAbort

/**
 * Code analyzer for java source code.
 *
 * @author Akash Yadav
 */
class JavaDiagnosticProvider(private val completionChecker: Supplier<Boolean>) {

  private val log = ILogger.newInstance(javaClass.simpleName)
  private val analyzeTimestamps = mutableMapOf<Path, Instant>()
  private var cachedDiagnostics = DiagnosticResult.NO_UPDATE
  private var analyzing = AtomicBoolean(false)

  fun analyze(compiler: JavaCompilerService, file: Path): DiagnosticResult {

    log.debug("Analyzing:", file)

    if (completionChecker.get()) {
      log.warn("Completion is in progress. Skipping analyze...")
      return DiagnosticResult.NO_UPDATE
    }

    val modifiedAt = FileStore.modified(file)
    val analyzedAt = analyzeTimestamps[file]

    if (analyzedAt?.isAfter(modifiedAt) == true) {
      log.debug("Using cached analyze results...")
      return cachedDiagnostics
    }

    if (analyzing.get()) {
      log.warn("Another analyze process is in progress...")
      return DiagnosticResult.NO_UPDATE
    }

    analyzing.set(true)

    return try {
        compiler.compile(file).get { task -> doAnalyze(file, task) }
      } catch (err: Throwable) {

        if (isCancelled(err) || com.itsaky.androidide.javac.services.CancelAbort.isCancelled(err)) {
          throw err
        }

        log.warn("Unable to analyze file", err)

        compiler.destroy()
        DiagnosticResult.NO_UPDATE
      }
      .also {
        cachedDiagnostics = it
        analyzeTimestamps[file] = Instant.now()
        analyzing.set(false)
      }
  }

  private fun doAnalyze(file: Path, task: CompileTask): DiagnosticResult {
    return if (!isTaskValid(task)) {
      // Do not use Collections.emptyList ()
      // The returned list is accessed and the list returned by Collections.emptyList()
      // throws exception when trying to access.
      cachedDiagnostics
    } else DiagnosticResult(file, findDiagnostics(task, file))
  }

  companion object {
    private fun isTaskValid(task: CompileTask?): Boolean {
      return task?.task != null && task.roots != null && task.roots.size > 0
    }
  }
}
