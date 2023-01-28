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

import openjdk.source.tree.CaseTree;
import openjdk.source.tree.CompilationUnitTree;
import openjdk.source.tree.ErroneousTree;
import openjdk.source.tree.IdentifierTree;
import openjdk.source.tree.ImportTree;
import openjdk.source.tree.MemberReferenceTree;
import openjdk.source.tree.MemberSelectTree;
import openjdk.source.tree.Tree;
import openjdk.source.util.JavacTask;
import openjdk.source.util.SourcePositions;
import openjdk.source.util.TreePath;
import openjdk.source.util.TreePathScanner;
import openjdk.source.util.Trees;

public class FindCompletionsAt extends TreePathScanner<TreePath, Long> {

  //  private static final ILogger LOG = ILogger.newInstance("FindCompletionsAt");
  private final JavacTask task;
  private CompilationUnitTree root;

  public FindCompletionsAt(JavacTask task) {
    this.task = task;
  }

  @Override
  public TreePath visitCompilationUnit(CompilationUnitTree t, Long find) {
    root = t;
    return reduce(super.visitCompilationUnit(t, find), getCurrentPath());
  }

  @Override
  public TreePath visitIdentifier(IdentifierTree t, Long find) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    long start = pos.getStartPosition(root, t);
    long end = pos.getEndPosition(root, t);
    if (start <= find && find <= end) {
      return getCurrentPath();
    }
    return super.visitIdentifier(t, find);
  }

  @Override
  public TreePath visitMemberSelect(MemberSelectTree t, Long find) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    long start = pos.getEndPosition(root, t.getExpression()) + 1;
    long end = pos.getEndPosition(root, t);
    if (start <= find && find <= end) {
      return getCurrentPath();
    }
    return super.visitMemberSelect(t, find);
  }

  @Override
  public TreePath visitMemberReference(MemberReferenceTree t, Long find) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    long start = pos.getEndPosition(root, t.getQualifierExpression()) + 2;
    long end = pos.getEndPosition(root, t);
    if (start <= find && find <= end) {
      return getCurrentPath();
    }
    return super.visitMemberReference(t, find);
  }

  @Override
  public TreePath visitCase(CaseTree t, Long find) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();

    // check if the cursor is in the case expression
    // default statements have null expression
    // In case of an identifier tree, we have to check for both, variables and switch constants
    // in
    // CompletionProvider
    if (t.getExpression() != null && !(t.getExpression() instanceof IdentifierTree)) {
      long start = pos.getStartPosition(root, t.getExpression());
      long end = pos.getEndPosition(root, t.getExpression());
      if (start <= find && find <= end) {
        return new TreePath(getCurrentPath(), t.getExpression());
      }
    }

    long start = pos.getStartPosition(root, t) + "case".length();
    long end = pos.getEndPosition(root, t.getExpression());
    if (start <= find && find <= end) {
      return getCurrentPath().getParentPath();
    }

    return super.visitCase(t, find);
  }

  @Override
  public TreePath visitImport(ImportTree t, Long find) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    long start = pos.getStartPosition(root, t.getQualifiedIdentifier());
    long end = pos.getEndPosition(root, t.getQualifiedIdentifier());
    if (start <= find && find <= end) {
      return getCurrentPath();
    }
    return super.visitImport(t, find);
  }

  @Override
  public TreePath visitErroneous(ErroneousTree t, Long find) {
    if (t.getErrorTrees() == null) {
      return null;
    }
    for (Tree e : t.getErrorTrees()) {
      TreePath found = scan(e, find);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  @Override
  public TreePath reduce(TreePath a, TreePath b) {
    if (a != null) {
      return a;
    }
    return b;
  }
}
