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

package com.itsaky.lsp.java.visitors;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;

public class FindVariableAtCursor extends TreeScanner<VariableTree, Integer> {
  private final SourcePositions pos;
  private CompilationUnitTree root;

  public FindVariableAtCursor(JavacTask task) {
    pos = Trees.instance(task).getSourcePositions();
  }

  @Override
  public VariableTree visitCompilationUnit(CompilationUnitTree t, Integer find) {
    root = t;
    return super.visitCompilationUnit(t, find);
  }

  @Override
  public VariableTree visitVariable(VariableTree t, Integer find) {
    VariableTree smaller = super.visitVariable(t, find);
    if (smaller != null) {
      return smaller;
    }
    if (pos.getStartPosition(root, t) <= find && find < pos.getEndPosition(root, t)) {
      return t;
    }
    return null;
  }

  @Override
  public VariableTree reduce(VariableTree r1, VariableTree r2) {
    if (r1 != null) return r1;
    return r2;
  }
}
