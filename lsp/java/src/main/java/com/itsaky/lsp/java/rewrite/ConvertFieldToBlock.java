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

package com.itsaky.lsp.java.rewrite;

import static com.itsaky.lsp.java.rewrite.ConvertVariableToStatement.findVariable;
import static com.itsaky.lsp.java.rewrite.ConvertVariableToStatement.isExpressionStatement;

import androidx.annotation.NonNull;

import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.parser.ParseTask;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.TextEdit;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.Trees;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import javax.lang.model.element.Modifier;

public class ConvertFieldToBlock extends Rewrite {
  final Path file;
  final int position;

  public ConvertFieldToBlock(Path file, int position) {
    this.file = file;
    this.position = position;
  }

  @Override
  public Map<Path, TextEdit[]> rewrite(@NonNull CompilerProvider compiler) {
    ParseTask task = compiler.parse(file);
    Trees trees = Trees.instance(task.task);
    SourcePositions pos = trees.getSourcePositions();
    LineMap lines = task.root.getLineMap();
    VariableTree variable = findVariable(task, position);
    if (variable == null) {
      return CANCELLED;
    }
    ExpressionTree expression = variable.getInitializer();
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
    Range deleteLhs = new Range(startPos, endPos);
    TextEdit fixLhs = new TextEdit(deleteLhs, "{ ");
    if (variable.getModifiers().getFlags().contains(Modifier.STATIC)) {
      fixLhs.setNewText("static { ");
    }
    long right = pos.getEndPosition(task.root, variable);
    int rightLine = (int) lines.getLineNumber(right);
    int rightColumn = (int) lines.getColumnNumber(right);
    Position rightPos = new Position(rightLine - 1, rightColumn - 1);
    Range insertRight = new Range(rightPos, rightPos);
    TextEdit fixRhs = new TextEdit(insertRight, " }");
    TextEdit[] edits = {fixLhs, fixRhs};
    return Collections.singletonMap(file, edits);
  }
}
