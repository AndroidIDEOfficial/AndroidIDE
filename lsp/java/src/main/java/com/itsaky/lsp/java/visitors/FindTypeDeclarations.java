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
import com.sun.source.util.TreeScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FindTypeDeclarations extends TreeScanner<Void, List<String>> {
  private List<CharSequence> qualifiedName = new ArrayList<>();

  @Override
  public Void visitCompilationUnit(CompilationUnitTree root, List<String> found) {
    String name = Objects.toString(root.getPackageName(), "");
    qualifiedName.add(name);
    return super.visitCompilationUnit(root, found);
  }

  @Override
  public Void visitClass(ClassTree type, List<String> found) {
    qualifiedName.add(type.getSimpleName());
    found.add(String.join(".", qualifiedName));
    super.visitClass(type, found);
    qualifiedName.remove(qualifiedName.size() - 1);
    return null;
  }
}
