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
import com.itsaky.androidide.lsp.java.utils.EditHelper;
import com.itsaky.androidide.lsp.java.utils.FindHelper;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.lsp.models.TextEdit;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.Trees;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;

public class AddSuppressWarningAnnotation extends Rewrite {
  final String className, methodName;
  final String[] erasedParameterTypes;

  public AddSuppressWarningAnnotation(
      String className, String methodName, String[] erasedParameterTypes) {
    this.className = className;
    this.methodName = methodName;
    this.erasedParameterTypes = erasedParameterTypes;
  }

  @Override
  public Map<Path, TextEdit[]> rewrite(@NonNull CompilerProvider compiler) {
    Path file = compiler.findTypeDeclaration(className);
    if (file == CompilerProvider.NOT_FOUND) {
      return CANCELLED;
    }
    SynchronizedTask synchronizedTask = compiler.compile(file);
    return synchronizedTask.get(
        task -> {
          Trees trees = Trees.instance(task.task);
          ExecutableElement methodElement =
              FindHelper.findMethod(task, className, methodName, erasedParameterTypes);
          MethodTree methodTree = trees.getTree(methodElement);
          SourcePositions pos = trees.getSourcePositions();
          int startMethod = (int) pos.getStartPosition(task.root(), methodTree);
          LineMap lines = task.root().getLineMap();
          int line = (int) lines.getLineNumber(startMethod);
          int column = (int) lines.getColumnNumber(startMethod);
          int startLine = (int) lines.getStartPosition(line);
          String indent = EditHelper.repeatSpaces(startMethod - startLine);
          String insertText = "@SuppressWarnings(\"unchecked\")\n" + indent;
          Position insertPoint = new Position(line - 1, column - 1);
          TextEdit insert = new TextEdit(new Range(insertPoint, insertPoint), insertText);
          TextEdit[] edits = {insert};
          return Collections.singletonMap(file, edits);
        });
  }
}
