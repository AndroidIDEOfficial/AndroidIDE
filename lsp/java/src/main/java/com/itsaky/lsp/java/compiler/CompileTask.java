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

package com.itsaky.lsp.java.compiler;

import androidx.annotation.NonNull;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTaskImpl;

import java.nio.file.Path;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompileTask implements AutoCloseable {

  public final JavacTaskImpl task;
  public final List<CompilationUnitTree> roots;
  public final List<Diagnostic<? extends JavaFileObject>> diagnostics;

  private final CompileBatch compileBatch;

  public CompileTask(
      @NonNull CompileBatch compileBatch, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
    this.compileBatch = compileBatch;
    this.task = compileBatch.task;
    this.roots = compileBatch.roots;
    this.diagnostics = diagnostics;
  }

  public CompilationUnitTree root() {
    if (roots.size() != 1) {
      throw new RuntimeException("No compilation units found. Roots: " + roots.size());
    }
    return roots.get(0);
  }

  public CompilationUnitTree root(Path file) {
    for (CompilationUnitTree root : roots) {
      if (root.getSourceFile().toUri().equals(file.toUri())) {
        return root;
      }
    }
    throw new RuntimeException("Compilation unit not found");
  }

  public CompilationUnitTree root(JavaFileObject file) {
    for (CompilationUnitTree root : roots) {
      if (root.getSourceFile().toUri().equals(file.toUri())) {
        return root;
      }
    }
    throw new RuntimeException("Compilation unit not found");
  }

  @Override
  public void close() {
    compileBatch.close();
  }
}
