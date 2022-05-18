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

import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.compiler.SynchronizedTask;
import com.itsaky.lsp.models.DiagnosticItem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Code analyzer for java source code.
 *
 * @author Akash Yadav
 */
public class JavaDiagnosticProvider {

  private final CompilerProvider compiler;

  public JavaDiagnosticProvider(CompilerProvider compiler) {
    this.compiler = compiler;
  }

  private static boolean isTaskValid(CompileTask task) {
    return task != null && task.task != null && task.roots != null && task.roots.size() > 0;
  }

  @NonNull
  public List<DiagnosticItem> analyze(@NonNull Path file) {
    final SynchronizedTask synchronizedTask = compiler.compile(file);
    return synchronizedTask.get(
        task -> {
          if (!isTaskValid(task)) {
            // Do not use Collections.emptyList ()
            return new ArrayList<>();
          }

          return DiagnosticsProvider.findDiagnostics(task, file);
        });
  }
}
