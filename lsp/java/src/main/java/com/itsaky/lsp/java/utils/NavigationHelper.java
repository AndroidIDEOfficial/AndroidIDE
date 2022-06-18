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

package com.itsaky.lsp.java.utils;

import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.visitors.FindNameAt;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import java.nio.file.Path;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class NavigationHelper {

  public static Element findElement(CompileTask task, Path file, int line, int column) {
    Trees trees = Trees.instance(task.task);
    for (CompilationUnitTree root : task.roots) {
      if (root.getSourceFile().toUri().equals(file.toUri())) {
        long cursor = root.getLineMap().getPosition(line, column);
        TreePath path = new FindNameAt(task).scan(root, cursor);
        if (path == null) {
          return null;
        }
        return trees.getElement(path);
      }
    }
    throw new RuntimeException("file not found");
  }

  public static boolean isLocal(Element element) {
    if (element.getModifiers().contains(Modifier.PRIVATE)) {
      return true;
    }
    switch (element.getKind()) {
      case EXCEPTION_PARAMETER:
      case LOCAL_VARIABLE:
      case PARAMETER:
      case TYPE_PARAMETER:
        return true;
      default:
        return false;
    }
  }

  public static boolean isMember(Element element) {
    switch (element.getKind()) {
      case ENUM_CONSTANT:
      case FIELD:
      case METHOD:
      case CONSTRUCTOR:
        return true;
      default:
        return false;
    }
  }

  public static boolean isType(Element element) {
    switch (element.getKind()) {
      case ANNOTATION_TYPE:
      case CLASS:
      case ENUM:
      case INTERFACE:
        return true;
      default:
        return false;
    }
  }
}
