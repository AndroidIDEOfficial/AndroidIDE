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

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

public class FindTypeDeclarationAt extends TreePathScanner<ClassTree, Long> {
  private final SourcePositions pos;
  private CompilationUnitTree root;

  private TreePath path;

  public FindTypeDeclarationAt(JavacTask task) {
    pos = Trees.instance(task).getSourcePositions();
  }

  @Override
  public ClassTree visitCompilationUnit(CompilationUnitTree t, Long find) {
    root = t;
    return super.visitCompilationUnit(t, find);
  }

  @Override
  public ClassTree visitClass(ClassTree t, Long find) {
    ClassTree smaller = super.visitClass(t, find);
    if (smaller != null) {
      return smaller;
    }
    if (pos.getStartPosition(root, t) <= find && find < pos.getEndPosition(root, t)) {
      this.path = getCurrentPath();
      return t;
    }
    return null;
  }

  @Override
  public ClassTree reduce(ClassTree a, ClassTree b) {
    if (a != null) return a;
    return b;
  }

  public TreePath getPath() {
    return path;
  }
}
