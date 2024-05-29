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

import java.io.IOException;
import openjdk.source.tree.CompilationUnitTree;
import openjdk.source.tree.MethodTree;
import openjdk.source.util.JavacTask;
import openjdk.source.util.SourcePositions;
import openjdk.source.util.TreeScanner;
import openjdk.source.util.Trees;

public class PruneMethodBodies extends TreeScanner<StringBuilder, Long> {
  private final JavacTask task;
  private final StringBuilder buf = new StringBuilder();
  private CompilationUnitTree root;

  public PruneMethodBodies(JavacTask task) {
    this.task = task;
  }

  @Override
  public StringBuilder reduce(StringBuilder a, StringBuilder b) {
    return buf;
  }

  @Override
  public StringBuilder visitCompilationUnit(CompilationUnitTree t, Long find) {
    root = t;
    try {
      CharSequence contents = t.getSourceFile().getCharContent(true);
      buf.setLength(0);
      buf.append(contents);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    super.visitCompilationUnit(t, find);
    return buf;
  }

  @Override
  public StringBuilder visitMethod(MethodTree t, Long find) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    if (t.getBody() == null) {
      return buf;
    }
    long start = pos.getStartPosition(root, t.getBody());
    long end = pos.getEndPosition(root, t.getBody());
    if (!(start <= find && find < end)) {
      for (int i = (int) start + 1; i < end - 1; i++) {
        if (!Character.isWhitespace(buf.charAt(i))) {
          buf.setCharAt(i, ' ');
        }
      }
      return buf;
    }
    super.visitMethod(t, find);
    return buf;
  }
}
