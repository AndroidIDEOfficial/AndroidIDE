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

package com.itsaky.androidide.javac.services.visitors;

import openjdk.source.tree.CompilationUnitTree;
import openjdk.source.tree.MethodTree;
import openjdk.source.tree.Tree;
import openjdk.source.tree.VariableTree;
import openjdk.tools.javac.tree.EndPosTable;
import openjdk.tools.javac.tree.JCTree;
import openjdk.tools.javac.tree.JCTree.JCVariableDecl;

/** Helper visitor for partial reparse. Updates tree positions by the given delta. */
public class TranslateMethodPositionsVisitor extends ErrorAwareTreeScanner<Void, Void> {

  private final MethodTree changedMethod;
  private final EndPosTable endPos;
  private final int delta;
  boolean active;
  boolean inMethod;

  public TranslateMethodPositionsVisitor(
      final MethodTree changedMethod, final EndPosTable endPos, final int delta) {
    assert endPos != null;
    this.changedMethod = changedMethod;
    this.endPos = endPos;
    this.delta = delta;

    active = changedMethod == null;
  }

  @Override
  public Void scan(Tree node, Void p) {
    if (active && node != null) {
      if (((JCTree) node).pos >= 0) {
        ((JCTree) node).pos += delta;
      }
    }
    Void result = super.scan(node, p);
    if (inMethod && node != null) {
      endPos.replaceTree((JCTree) node, null);
    }
    if (active && node != null) {
      int pos = endPos.replaceTree((JCTree) node, null);
      int newPos;
      if (pos < 0) {
        newPos = pos;
      } else {
        newPos = pos + delta;
      }
      endPos.storeEnd((JCTree) node, newPos);
    }
    return result;
  }

  @Override
  public Void visitCompilationUnit(CompilationUnitTree node, Void p) {
    return scan(node.getTypeDecls(), p);
  }

  @Override
  public Void visitMethod(MethodTree node, Void p) {
    if (active || inMethod) {
      scan(node.getModifiers(), p);
      scan(node.getReturnType(), p);
      scan(node.getTypeParameters(), p);
      scan(node.getParameters(), p);
      scan(node.getThrows(), p);
    }
    if (node == changedMethod) {
      inMethod = true;
    }
    if (active || inMethod) {
      scan(node.getBody(), p);
    }
    if (inMethod) {
      active = true;
      inMethod = false;
    }
    if (active) {
      scan(node.getDefaultValue(), p);
    }
    return null;
  }

  @Override
  public Void visitVariable(VariableTree node, Void p) {
    JCVariableDecl varDecl = (JCVariableDecl) node;
    if (varDecl.sym != null && active && varDecl.sym.pos >= 0) {
      varDecl.sym.pos += delta;
    }
    return super.visitVariable(node, p);
  }
}
