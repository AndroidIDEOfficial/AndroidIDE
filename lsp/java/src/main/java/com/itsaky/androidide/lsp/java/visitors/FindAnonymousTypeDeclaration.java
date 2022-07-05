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

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

/**
 * @author Akash Yadav
 */
public class FindAnonymousTypeDeclaration extends TreePathScanner<ClassTree, Long> {

  private final SourcePositions pos;
  private final CompilationUnitTree root;
  private TreePath stored;

  public FindAnonymousTypeDeclaration(JavacTask task, CompilationUnitTree root) {
    this.pos = Trees.instance(task).getSourcePositions();
    this.root = root;
  }

  @Override
  public ClassTree reduce(ClassTree a, ClassTree b) {
    if (a != null) return a;
    return b;
  }

  @Override
  public ClassTree visitNewClass(NewClassTree t, Long find) {

    if (pos == null) {
      return null;
    }

    ClassTree smaller = super.visitNewClass(t, find);
    if (smaller != null) {
      return smaller;
    }

    if (pos.getStartPosition(root, t.getClassBody()) <= find
        && find < pos.getEndPosition(root, t.getClassBody())) {
      stored = getCurrentPath();
      return t.getClassBody();
    }

    return null;
  }

  public TreePath getStoredPath() {
    return stored;
  }
}
