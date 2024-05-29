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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import openjdk.source.tree.ClassTree;
import openjdk.source.tree.CompilationUnitTree;
import openjdk.source.util.TreeScanner;

public class FindTypeDeclarationNamed extends TreeScanner<ClassTree, String> {
  private List<CharSequence> qualifiedName = new ArrayList<>();

  @Override
  public ClassTree reduce(ClassTree a, ClassTree b) {
    if (a != null) return a;
    return b;
  }

  @Override
  public ClassTree visitCompilationUnit(CompilationUnitTree t, String find) {
    String name = Objects.toString(t.getPackageName(), "");
    qualifiedName.add(name);
    return super.visitCompilationUnit(t, find);
  }

  @Override
  public ClassTree visitClass(ClassTree t, String find) {
    qualifiedName.add(t.getSimpleName());
    if (String.join(".", qualifiedName).equals(find)) {
      return t;
    }
    ClassTree recurse = super.visitClass(t, find);
    qualifiedName.remove(qualifiedName.size() - 1);
    return recurse;
  }
}
