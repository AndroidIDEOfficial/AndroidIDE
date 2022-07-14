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

package com.itsaky.androidide.lsp.java.rewrite;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.itsaky.androidide.lsp.java.compiler.CompilerProvider;
import com.itsaky.androidide.lsp.java.parser.ParseTask;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.lsp.models.TextEdit;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.Trees;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AddImport extends Rewrite {

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  public final String className;

  final Path file;

  public AddImport(Path file, String className) {
    this.file = file;
    this.className = className;
  }

  @Override
  public Map<Path, TextEdit[]> rewrite(@NonNull CompilerProvider compiler) {
    final ParseTask task = compiler.parse(file);
    Position point = insertPosition(task);
    String text = "import " + className + ";\n";
    return Collections.singletonMap(
        file, new TextEdit[] {new TextEdit(new Range(point, point), text)});
  }

  @SuppressWarnings("Since15")
  private Position insertPosition(@NonNull ParseTask task) {
    List<? extends ImportTree> imports = task.root.getImports();
    for (ImportTree i : imports) {
      String next = i.getQualifiedIdentifier().toString();
      if (className.compareTo(next) < 0) {
        return insertBefore(task, i);
      }
    }
    if (!imports.isEmpty()) {
      Tree last = imports.get(imports.size() - 1);
      return insertAfter(task, last);
    }
    if (task.root.getPackage() != null) {
      return insertAfter(task, task.root.getPackage());
    }
    return new Position(0, 0);
  }

  private Position insertBefore(ParseTask task, Tree i) {
    SourcePositions pos = Trees.instance(task.task).getSourcePositions();
    long offset = pos.getStartPosition(task.root, i);
    int line = (int) task.root.getLineMap().getLineNumber(offset);
    return new Position(line - 1, 0);
  }

  private Position insertAfter(ParseTask task, Tree i) {
    SourcePositions pos = Trees.instance(task.task).getSourcePositions();
    long offset = pos.getStartPosition(task.root, i);
    int line = (int) task.root.getLineMap().getLineNumber(offset);
    return new Position(line, 0);
  }
}
