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
import com.itsaky.androidide.lsp.java.compiler.SynchronizedTask;
import com.itsaky.androidide.lsp.java.utils.FindHelper;
import com.itsaky.androidide.lsp.models.TextEdit;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.preferences.utils.EditorUtilKt;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import openjdk.source.util.Trees;

public class AddSuppressWarningAnnotation extends Rewrite {

  final String className, methodName;
  final String[] erasedParameterTypes;

  public AddSuppressWarningAnnotation(
      String className, String methodName, String[] erasedParameterTypes) {
    this.className = className;
    this.methodName = methodName;
    this.erasedParameterTypes = erasedParameterTypes;
  }

  @NonNull
  @Override
  public Map<Path, TextEdit[]> rewrite(@NonNull CompilerProvider compiler) {
    Path file = compiler.findTypeDeclaration(className);
    if (file == CompilerProvider.NOT_FOUND) {
      return CANCELLED;
    }
    SynchronizedTask synchronizedTask = compiler.compile(file);
    return synchronizedTask.get(
        task -> {
          final var trees = Trees.instance(task.task);
          final var methodElement =
              FindHelper.findMethod(task, className, methodName, erasedParameterTypes);
          if (methodElement == null) {
            return CANCELLED;
          }
          final var methodTree = trees.getTree(methodElement);
          if (methodTree == null) {
            return CANCELLED;
          }
          final var startMethod = (int) trees.getSourcePositions()
              .getStartPosition(task.root(), methodTree);
          final var lines = task.root().getLineMap();
          final var line = (int) lines.getLineNumber(startMethod);
          final var column = (int) lines.getColumnNumber(startMethod);
          final var startLine = (int) lines.getStartPosition(line);
          final var indent = EditorUtilKt.indentationString(startMethod - startLine);
          final var insertText = "@SuppressWarnings(\"unchecked\")\n" + indent;
          final var insertPoint = new Position(line - 1, column - 1);
          final var edits = new TextEdit[]{
              new TextEdit(new Range(insertPoint, insertPoint), insertText)};
          return Collections.singletonMap(file, edits);
        });
  }
}
