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
import com.itsaky.androidide.lsp.java.compiler.CompilerProvider;
import com.itsaky.androidide.lsp.java.parser.ParseTask;
import com.itsaky.androidide.lsp.java.visitors.FindVariableAtCursor;
import com.itsaky.androidide.lsp.models.TextEdit;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import openjdk.source.tree.ExpressionTree;
import openjdk.source.tree.LineMap;
import openjdk.source.tree.Tree;
import openjdk.source.tree.VariableTree;
import openjdk.source.util.SourcePositions;
import openjdk.source.util.Trees;

public class ConvertVariableToStatement extends Rewrite {
  final Path file;
  final int position;

  public ConvertVariableToStatement(Path file, int position) {
    this.file = file;
    this.position = position;
  }

  @NonNull
  @Override
  public Map<Path, TextEdit[]> rewrite(@NonNull CompilerProvider compiler) {
    final ParseTask task = compiler.parse(file);
    final Trees trees = Trees.instance(task.task);
    final SourcePositions pos = trees.getSourcePositions();
    final LineMap lines = task.root.getLineMap();
    final VariableTree variable = findVariable(task, position);
    if (variable == null) {
      return CANCELLED;
    }
    ExpressionTree expression = variable.getInitializer();
    if (expression == null) {
      return CANCELLED;
    }
    if (!isExpressionStatement(expression)) {
      return CANCELLED;
    }
    long start = pos.getStartPosition(task.root, variable);
    long end = pos.getStartPosition(task.root, expression);
    int startLine = (int) lines.getLineNumber(start);
    int startColumn = (int) lines.getColumnNumber(start);
    Position startPos = new Position(startLine - 1, startColumn - 1);
    int endLine = (int) lines.getLineNumber(end);
    int endColumn = (int) lines.getColumnNumber(end);
    Position endPos = new Position(endLine - 1, endColumn - 1);
    Range delete = new Range(startPos, endPos);
    TextEdit edit = new TextEdit(delete, "");
    TextEdit[] edits = {edit};
    return Collections.singletonMap(file, edits);
  }

  static VariableTree findVariable(ParseTask task, int position) {
    return new FindVariableAtCursor(task.task).scan(task.root, position);
  }

  /** https://docs.oracle.com/javase/specs/jls/se13/html/jls-14.html#jls-14.8 */
  static boolean isExpressionStatement(Tree t) {
    if (t == null) return false;
    switch (t.getKind()) {
      case ASSIGNMENT:
      case PREFIX_INCREMENT:
      case PREFIX_DECREMENT:
      case POSTFIX_INCREMENT:
      case POSTFIX_DECREMENT:
      case METHOD_INVOCATION:
      case NEW_CLASS:
        return true;
      default:
        return false;
    }
  }
}
