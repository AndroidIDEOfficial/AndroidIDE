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

package com.itsaky.androidide.lsp.java.providers;

import androidx.annotation.NonNull;

import com.itsaky.androidide.lsp.java.compiler.CompileTask;
import com.itsaky.androidide.lsp.java.compiler.CompilerProvider;
import com.itsaky.androidide.lsp.java.compiler.SynchronizedTask;
import com.itsaky.androidide.lsp.java.utils.FindHelper;
import com.itsaky.androidide.lsp.java.utils.NavigationHelper;
import com.itsaky.androidide.lsp.java.visitors.FindReferences;
import com.itsaky.androidide.lsp.models.ReferenceParams;
import com.itsaky.androidide.lsp.models.ReferenceResult;
import com.itsaky.androidide.models.Location;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class ReferenceProvider {

  public static final List<Location> NOT_SUPPORTED = Collections.emptyList();
  private final CompilerProvider compiler;
  private Path file;
  private int line, column;

  public ReferenceProvider(CompilerProvider compiler) {
    this.compiler = compiler;
  }

  @NonNull
  public ReferenceResult findReferences(@NonNull ReferenceParams params) {
    this.file = params.getFile();

    // 1-based line and column indexes
    this.line = params.getPosition().getLine() + 1;
    this.column = params.getPosition().getColumn() + 1;
    return new ReferenceResult(find());
  }

  public List<Location> find() {
    final SynchronizedTask synchronizedTask = compiler.compile(file);
    return synchronizedTask.get(
        task -> {
          Element element = NavigationHelper.findElement(task, file, line, column);
          if (element == null) return NOT_SUPPORTED;
          if (NavigationHelper.isLocal(element)) {
            return findReferences(task);
          }
          if (NavigationHelper.isType(element)) {
            TypeElement type = (TypeElement) element;
            String className = type.getQualifiedName().toString();
            task.close();
            return findTypeReferences(className);
          }
          if (NavigationHelper.isMember(element)) {
            TypeElement parentClass = (TypeElement) element.getEnclosingElement();
            String className = parentClass.getQualifiedName().toString();
            String memberName = element.getSimpleName().toString();
            if (memberName.equals("<init>")) {
              memberName = parentClass.getSimpleName().toString();
            }
            task.close();
            return findMemberReferences(className, memberName);
          }
          return NOT_SUPPORTED;
        });
  }

  private List<Location> findTypeReferences(String className) {
    Path[] files = compiler.findTypeReferences(className);
    if (files.length == 0) return Collections.emptyList();
    return compiler.compile(files).get(this::findReferences);
  }

  private List<Location> findMemberReferences(String className, String memberName) {
    Path[] files = compiler.findMemberReferences(className, memberName);
    if (files.length == 0) return Collections.emptyList();
    return compiler.compile(files).get(this::findReferences);
  }

  private List<Location> findReferences(CompileTask task) {
    Element element = NavigationHelper.findElement(task, file, line, column);
    List<TreePath> paths = new ArrayList<>();
    for (CompilationUnitTree root : task.roots) {
      new FindReferences(task.task, element).scan(root, paths);
    }
    List<Location> locations = new ArrayList<>();
    for (TreePath p : paths) {
      locations.add(FindHelper.location(task, p));
    }
    return locations;
  }
}
