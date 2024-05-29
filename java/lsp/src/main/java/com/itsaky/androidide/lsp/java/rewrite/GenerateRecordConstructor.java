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
import com.itsaky.androidide.lsp.java.compiler.CompileTask;
import com.itsaky.androidide.lsp.java.compiler.CompilerProvider;
import com.itsaky.androidide.lsp.java.compiler.SynchronizedTask;
import com.itsaky.androidide.lsp.java.utils.EditHelper;
import com.itsaky.androidide.lsp.models.TextEdit;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.preferences.internal.EditorPreferences;
import com.itsaky.androidide.preferences.utils.EditorUtilKt;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import jdkx.lang.model.element.Modifier;
import jdkx.lang.model.element.TypeElement;
import openjdk.source.tree.ClassTree;
import openjdk.source.tree.MethodTree;
import openjdk.source.tree.Tree;
import openjdk.source.tree.VariableTree;
import openjdk.source.util.SourcePositions;
import openjdk.source.util.Trees;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateRecordConstructor extends Rewrite {

  private static final Logger LOG = LoggerFactory.getLogger(GenerateRecordConstructor.class);
  final String className;

  public GenerateRecordConstructor(String className) {
    this.className = className;
  }

  @NonNull
  @Override
  public Map<Path, TextEdit[]> rewrite(@NonNull CompilerProvider compiler) {
    LOG.info("Generate default constructor for {}...", className);
    // TODO this needs to fall back on looking for inner classes and package-private classes
    Path file = compiler.findTypeDeclaration(className);

    if (file == CompilerProvider.NOT_FOUND) {
      LOG.warn("Unable to find source file for class: {}", this.className);
      return CANCELLED;
    }

    SynchronizedTask synchronizedTask = compiler.compile(file);
    return synchronizedTask.get(
        task -> {
          TypeElement typeElement = task.task.getElements().getTypeElement(className);
          ClassTree typeTree = Trees.instance(task.task).getTree(typeElement);
          List<VariableTree> fields = fieldsNeedingInitialization(typeTree);
          String parameters = generateParameters(task, fields);
          String initializers = generateInitializers(fields);
          StringBuilder buf = new StringBuilder();
          buf.append("\n");
          if (typeTree.getModifiers().getFlags().contains(Modifier.PUBLIC)) {
            buf.append("public ");
          }

          buf.append(simpleName(className))
              .append("(")
              .append(parameters)
              .append(") {\n    ")
              .append(initializers)
              .append("\n}");
          String string = buf.toString();
          int indent = EditHelper.indent(task.task, task.root(), typeTree)
              + EditorPreferences.INSTANCE.getTabSize();
          string = string.replaceAll("\n", "\n" + EditorUtilKt.indentationString(indent));
          string = string + "\n\n";
          Position insert = insertPoint(task, typeTree);
          TextEdit[] edits = {new TextEdit(new Range(insert, insert), string)};
          return Collections.singletonMap(file, edits);
        });
  }

  private List<VariableTree> fieldsNeedingInitialization(ClassTree typeTree) {
    List<VariableTree> fields = new ArrayList<>();
    for (Tree member : typeTree.getMembers()) {
      if (!(member instanceof VariableTree)) {
        continue;
      }
      VariableTree field = (VariableTree) member;
      if (field.getInitializer() != null) {
        continue;
      }
      Set<Modifier> flags = field.getModifiers().getFlags();
      if (flags.contains(Modifier.STATIC)) {
        continue;
      }
      if (!flags.contains(Modifier.FINAL)) {
        continue;
      }
      fields.add(field);
    }

    return fields;
  }

  private String generateParameters(CompileTask task, List<VariableTree> fields) {
    StringJoiner join = new StringJoiner(", ");
    for (VariableTree f : fields) {
      join.add(extract(task, f.getType()) + " " + f.getName());
    }
    return join.toString();
  }

  private CharSequence extract(CompileTask task, Tree typeTree) {
    try {
      CharSequence contents = task.root().getSourceFile().getCharContent(true);
      SourcePositions pos = Trees.instance(task.task).getSourcePositions();
      int start = (int) pos.getStartPosition(task.root(), typeTree);
      int end = (int) pos.getEndPosition(task.root(), typeTree);
      return contents.subSequence(start, end);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String generateInitializers(List<VariableTree> fields) {
    StringJoiner join = new StringJoiner("\n    ");
    for (VariableTree f : fields) {
      join.add("this." + f.getName() + " = " + f.getName() + ";");
    }
    return join.toString();
  }

  private String simpleName(String className) {
    int dot = className.lastIndexOf('.');
    if (dot != -1) {
      return className.substring(dot + 1);
    }
    return className;
  }

  private Position insertPoint(CompileTask task, ClassTree typeTree) {
    for (Tree member : typeTree.getMembers()) {
      if (member.getKind() == Tree.Kind.METHOD) {
        MethodTree method = (MethodTree) member;
        if (method.getReturnType() == null) {
          continue;
        }
        LOG.info("...insert constructor before {}", method.getName());
        return EditHelper.insertBefore(task.task, task.root(), method);
      }
    }
    LOG.info("...insert constructor at end of class");
    return EditHelper.insertAtEndOfClass(task.task, task.root(), typeTree);
  }
}
