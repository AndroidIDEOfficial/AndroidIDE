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
import com.itsaky.androidide.lsp.java.utils.CancelChecker;
import com.itsaky.androidide.lsp.java.utils.FindHelper;
import com.itsaky.androidide.lsp.java.utils.NavigationHelper;
import com.itsaky.androidide.lsp.java.visitors.FindReferences;
import com.itsaky.androidide.lsp.models.ReferenceParams;
import com.itsaky.androidide.lsp.models.ReferenceResult;
import com.itsaky.androidide.models.Location;
import com.itsaky.androidide.progress.ICancelChecker;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import jdkx.lang.model.element.Element;
import jdkx.lang.model.element.TypeElement;
import openjdk.source.tree.CompilationUnitTree;
import openjdk.source.util.TreePath;

public class ReferenceProvider extends CancelableServiceProvider {

  public static final List<Location> NOT_SUPPORTED = Collections.emptyList();
  private final CompilerProvider compiler;
  private Path file;
  private int line, column;

  public ReferenceProvider(CompilerProvider compiler, ICancelChecker checker) {
    super(checker);
    this.compiler = compiler;
  }

  @NonNull
  public ReferenceResult findReferences(@NonNull ReferenceParams params) {
    this.file = params.getFile();

    // 1-based line and column indexes
    this.line = params.getPosition().getLine() + 1;
    this.column = params.getPosition().getColumn() + 1;

    List<Location> locations;
    try {
      locations = find();
    } catch (Exception err) {
      if (!CancelChecker.isCancelled(err)) {
        throw err;
      }
      locations = new ArrayList<>();
    }

    return new ReferenceResult(locations);
  }

  public List<Location> find() {
    abortIfCancelled();
    final SynchronizedTask synchronizedTask = compiler.compile(file);

    // findTypeReferences and findMemberReferences initiate another compilation task
    // However, initiating a compilation task while another compilation is in progress will result in a deadlock
    // Therefore, we return a supplier from the current synchronized task and
    final Supplier<List<Location>> result = synchronizedTask.get(
        task -> {
          abortIfCancelled();
          Element element = NavigationHelper.findElement(task, file, line, column, this);
          if (element == null) {
            return () -> NOT_SUPPORTED;
          }

          if (NavigationHelper.isLocal(element)) {
            // findReferences method here uses the compilation task object
            // however, finding the references lazily using supplier will leak this task
            final var references = findReferences(task);
            return () -> references;
          }

          if (NavigationHelper.isType(element)) {
            TypeElement type = (TypeElement) element;
            String className = type.getQualifiedName().toString();
            return () -> findTypeReferences(className);
          }

          if (NavigationHelper.isMember(element)) {
            final var parentClass = (TypeElement) element.getEnclosingElement();
            final var className = parentClass.getQualifiedName().toString();

            var memberName = element.getSimpleName().toString();
            if (memberName.equals("<init>")) {
              memberName = parentClass.getSimpleName().toString();
            }

            String finalMemberName = memberName;
            return () -> findMemberReferences(className, finalMemberName);
          }

          return () -> NOT_SUPPORTED;
        });

    return result.get();
  }

  private List<Location> findTypeReferences(String className) {
    abortIfCancelled();
    Path[] files = compiler.findTypeReferences(className);
    if (files.length == 0) {
      return Collections.emptyList();
    }

    abortIfCancelled();
    return compiler.compile(files).get(this::findReferences);
  }

  private List<Location> findMemberReferences(String className, String memberName) {
    abortIfCancelled();
    final var files = compiler.findMemberReferences(className, memberName);
    if (files.length == 0) {
      return Collections.emptyList();
    }

    abortIfCancelled();
    return compiler.compile(files).get(this::findReferences);
  }

  private List<Location> findReferences(CompileTask task) {
    abortIfCancelled();
    Element element = NavigationHelper.findElement(task, file, line, column, this);
    List<TreePath> paths = new ArrayList<>();
    for (CompilationUnitTree root : task.roots) {
      abortIfCancelled();
      new FindReferences(task.task, element).scan(root, paths);
    }
    List<Location> locations = new ArrayList<>();
    for (TreePath p : paths) {
      abortIfCancelled();
      locations.add(FindHelper.location(task, p));
    }
    return locations;
  }
}