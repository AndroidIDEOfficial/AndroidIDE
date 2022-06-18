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
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.visitors.FindBiggerRange;
import com.itsaky.lsp.models.ExpandSelectionParams;
import com.itsaky.lsp.models.Range;
import com.sun.source.tree.CompilationUnitTree;

/**
 * Selection provider implementation for Java.
 *
 * @author Akash Yadav
 */
public class JavaSelectionProvider {

  private static final ILogger LOG = ILogger.newInstance("JavaSelectionProvider");
  private final CompilerProvider compiler;

  public JavaSelectionProvider(CompilerProvider compiler) {
    this.compiler = compiler;
  }

  @NonNull
  public Range expandSelection(@NonNull ExpandSelectionParams params) {
    return compiler
        .compile(params.getFile())
        .get(
            task -> {
              final CompilationUnitTree root = task.root(params.getFile());
              final FindBiggerRange rangeFinder = new FindBiggerRange(task.task, root);
              final Range range = rangeFinder.scan(root, params.getSelection());

              if (range != null) {
                LOG.verbose("Expanding selection to range", range);
                return range;
              }

              LOG.debug("Unable to expand selection");
              return params.getSelection();
            });
  }
}
