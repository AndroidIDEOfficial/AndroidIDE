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

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import java.util.List;

import javax.lang.model.element.Element;

public class FindReferences extends TreePathScanner<Void, List<TreePath>> {

  final JavacTask task;
  final Element find;

  public FindReferences(JavacTask task, Element find) {
    this.task = task;
    this.find = find;
  }

  @Override
  public Void visitNewClass(NewClassTree t, List<TreePath> list) {
    if (check()) {
      list.add(getCurrentPath());
    }
    return super.visitNewClass(t, list);
  }

  @Override
  public Void visitMemberSelect(MemberSelectTree t, List<TreePath> list) {
    if (check()) {
      list.add(getCurrentPath());
    }
    return super.visitMemberSelect(t, list);
  }

  @Override
  public Void visitMemberReference(MemberReferenceTree t, List<TreePath> list) {
    if (check()) {
      list.add(getCurrentPath());
    }
    return super.visitMemberReference(t, list);
  }

  @Override
  public Void visitIdentifier(IdentifierTree t, List<TreePath> list) {
    if (check()) {
      list.add(getCurrentPath());
    }
    return super.visitIdentifier(t, list);
  }

  private boolean check() {
    Element candidate = Trees.instance(task).getElement(getCurrentPath());
    return find.equals(candidate);
  }
}
