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

package com.itsaky.androidide.lsp.java.visitors;

import androidx.annotation.NonNull;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds variables between the given start and end indexes.
 *
 * @author Akash Yadav
 */
public class FindVariablesBetween extends TreePathScanner<Void, Void> {

  private final long start;
  private final long end;
  private final SourcePositions positions;
  private final List<TreePath> paths = new ArrayList<>();
  private CompilationUnitTree root;

  public FindVariablesBetween(@NonNull JavacTask task, long start, long end) {
    Trees trees = Trees.instance(task);
    this.positions = trees.getSourcePositions();
    this.start = start;
    this.end = end;
  }

  @Override
  public Void visitCompilationUnit(CompilationUnitTree node, Void unused) {
    this.root = node;
    return super.visitCompilationUnit(node, unused);
  }

  @Override
  public Void visitVariable(VariableTree node, Void unused) {
    if (isInRange(node)) {
      this.paths.add(getCurrentPath());
    }

    return super.visitVariable(node, unused);
  }

  private boolean isInRange(VariableTree node) {
    final long start = this.positions.getStartPosition(root, node);
    final long end = this.positions.getEndPosition(root, node);
    return (this.start <= start && end <= this.end) || (start <= this.start && end >= this.end);
  }

  @NonNull
  public List<TreePath> getPaths() {
    return paths;
  }
}
