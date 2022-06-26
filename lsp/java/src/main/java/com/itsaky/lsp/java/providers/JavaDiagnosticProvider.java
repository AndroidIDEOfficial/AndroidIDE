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

package com.itsaky.lsp.java.providers;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.java.CompilationCancellationException;
import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.compiler.JavaCompilerService;
import com.itsaky.lsp.java.compiler.SynchronizedTask;
import com.itsaky.lsp.models.DiagnosticItem;

import org.netbeans.lib.nbjavac.services.CancelAbort;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Code analyzer for java source code.
 *
 * @author Akash Yadav
 */
public class JavaDiagnosticProvider {

  private final JavaCompilerService compiler;
  private static final ILogger LOG = ILogger.newInstance("JavaDiagnosticProvider");

  public JavaDiagnosticProvider(JavaCompilerService compiler) {
    this.compiler = compiler;
  }

  @NonNull
  public List<DiagnosticItem> analyze(@NonNull Path file) {
    try {
      final SynchronizedTask synchronizedTask = compiler.compile(file);
      return synchronizedTask.get(
          task -> {
            if (!isTaskValid(task)) {
              // Do not use Collections.emptyList ()
              return new ArrayList<>();
            }

            return DiagnosticsProvider.findDiagnostics(task, file);
          });
    } catch (Throwable err) {
      if (CompilationCancellationException.isCancelled(err) && !CancelAbort.isCancelled(err)) {
        throw err;
      }

      LOG.warn("Unable to analyze file", err);
      compiler.destroy();

      return new ArrayList<>();
    }
  }

  private static boolean isTaskValid(CompileTask task) {
    return task != null && task.task != null && task.roots != null && task.roots.size() > 0;
  }
}
