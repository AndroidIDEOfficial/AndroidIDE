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

public class AddException extends Rewrite {
  final String className, methodName;
  final String[] erasedParameterTypes;
  final String exceptionType;

  public AddException(
      String className, String methodName, String[] erasedParameterTypes, String exceptionType) {
    this.className = className;
    this.methodName = methodName;
    this.erasedParameterTypes = erasedParameterTypes;
    this.exceptionType = exceptionType;
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
          LineMap lines = task.root().getLineMap();
          long startBody = pos.getStartPosition(task.root(), methodTree.getBody());
          int line = (int) lines.getLineNumber(startBody);
          int column = (int) lines.getColumnNumber(startBody);
          Position insertPos = new Position(line - 1, column - 1);
          String simpleName = exceptionType;
          int lastDot = simpleName.lastIndexOf('.');
          if (lastDot != -1) {
            simpleName = exceptionType.substring(lastDot + 1);
          }

          String insertText;
          if (methodTree.getThrows().isEmpty()) {
            insertText = "throws " + simpleName + " ";
          } else {
            insertText = ", " + simpleName + " ";
          }
          TextEdit insertThrows = new TextEdit(new Range(insertPos, insertPos), insertText);
          // TODO add import if needed
          TextEdit[] edits = {insertThrows};
          return Collections.singletonMap(file, edits);
        });
  }
}
