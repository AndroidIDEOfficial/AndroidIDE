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

package com.itsaky.androidide.lsp.java.visitors;

import static android.text.TextUtils.substring;

import androidx.annotation.NonNull;

import com.itsaky.androidide.lsp.java.compiler.CompileTask;
import com.itsaky.androidide.lsp.models.HighlightToken;
import com.itsaky.androidide.lsp.models.HighlightTokenKind;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.projects.FileManager;
import com.itsaky.androidide.utils.ILogger;
import openjdk.source.tree.ClassTree;
import openjdk.source.tree.CompilationUnitTree;
import openjdk.source.tree.ExpressionTree;
import openjdk.source.tree.IdentifierTree;
import openjdk.source.tree.LineMap;
import openjdk.source.tree.MemberSelectTree;
import openjdk.source.tree.MethodInvocationTree;
import openjdk.source.tree.MethodTree;
import openjdk.source.tree.Tree;
import openjdk.source.tree.VariableTree;
import openjdk.source.util.SourcePositions;
import openjdk.source.util.TreePath;
import openjdk.source.util.TreePathScanner;
import openjdk.source.util.Trees;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jdkx.lang.model.element.Element;
import jdkx.lang.model.element.ElementKind;
import jdkx.lang.model.element.Modifier;
import jdkx.lang.model.element.Name;

public class SemanticHighlighter extends TreePathScanner<Void, List<HighlightToken>> {

  private static final ILogger LOG = ILogger.newInstance("JavaSemanticHighlighter");
  private final Trees trees;
  private final CompileTask task;

  public SemanticHighlighter(CompileTask task) {
    this.task = task;
    this.trees = Trees.instance(task.task);
  }

  @Override
  public Void visitClass(ClassTree t, List<HighlightToken> colors) {
    putSemantics(t.getSimpleName(), colors);
    return super.visitClass(t, colors);
  }

  @Override
  public Void visitMethod(MethodTree tree, List<HighlightToken> colors) {
    putSemantics(tree.getName(), colors);
    return super.visitMethod(tree, colors);
  }

  @Override
  public Void visitVariable(VariableTree t, List<HighlightToken> colors) {
    putSemantics(t.getName(), colors);
    return super.visitVariable(t, colors);
  }

  @Override
  public Void visitMethodInvocation(MethodInvocationTree tree, List<HighlightToken> colors) {
    Name name = null;
    ExpressionTree select = tree.getMethodSelect();
    if (select instanceof MemberSelectTree) {
      name = ((MemberSelectTree) select).getIdentifier();
    } else if (select instanceof IdentifierTree) {
      name = ((IdentifierTree) select).getName();
    }

    if (name != null) {
      Range range = find(getCurrentPath(), name);
      if (range != null) {
        colors.add(new HighlightToken(range, HighlightTokenKind.METHOD_INVOCATION));
      }
    }
    return super.visitMethodInvocation(tree, colors);
  }

  @Override
  public Void visitMemberSelect(MemberSelectTree t, List<HighlightToken> colors) {
    putSemantics(t.getIdentifier(), colors);
    return super.visitMemberSelect(t, colors);
  }

  @Override
  public Void visitIdentifier(IdentifierTree t, List<HighlightToken> colors) {
    putSemantics(t.getName(), colors);
    return super.visitIdentifier(t, colors);
  }

  private void putSemantics(Name name, List<HighlightToken> tokens) {
    if (name.contentEquals("this") || name.contentEquals("super") || name.contentEquals("class")) {
      return;
    }
    final TreePath fromPath = getCurrentPath();

    // Cancel the visit in case a task was cancelled/closed
    if (task == null || task.task == null) {
      throw new RuntimeException("Task is null");
    }

    Element toEl = trees.getElement(fromPath);
    if (toEl == null) {
      return;
    }

    Range range = find(fromPath, name);
    if (range == Range.NONE) return;

    ElementKind kind = toEl.getKind();
    HighlightTokenKind tokenKind = HighlightTokenKind.TEXT_NORMAL;

    switch (kind) {
      case PACKAGE:
        tokenKind = HighlightTokenKind.PACKAGE_NAME;
        break;
      case ENUM:
        tokenKind = HighlightTokenKind.ENUM_TYPE;
        break;
      case CLASS:
        tokenKind = HighlightTokenKind.TYPE_NAME;
        break;
      case ANNOTATION_TYPE:
        tokenKind = HighlightTokenKind.ANNOTATION;
        break;
      case INTERFACE:
        tokenKind = HighlightTokenKind.INTERFACE;
        break;
      case ENUM_CONSTANT:
        tokenKind = HighlightTokenKind.ENUM;
        break;
      case FIELD:
        if (toEl.getModifiers().contains(Modifier.STATIC)) {
          tokenKind = HighlightTokenKind.STATIC_FIELD;
        } else {
          tokenKind = HighlightTokenKind.FIELD;
        }
        break;
      case METHOD:
        tokenKind = HighlightTokenKind.METHOD_DECLARATION;
        break;
      case PARAMETER:
        tokenKind = HighlightTokenKind.PARAMETER;
        break;
      case LOCAL_VARIABLE:
        tokenKind = HighlightTokenKind.LOCAL_VARIABLE;
        break;
      case EXCEPTION_PARAMETER:
        tokenKind = HighlightTokenKind.EXCEPTION_PARAMETER;
        break;
      case CONSTRUCTOR:
        tokenKind = HighlightTokenKind.CONSTRUCTOR;
        break;
      case STATIC_INIT:
        tokenKind = HighlightTokenKind.STATIC_INIT;
        break;
      case INSTANCE_INIT:
        tokenKind = HighlightTokenKind.INSTANCE_INIT;
        break;
      case TYPE_PARAMETER:
        tokenKind = HighlightTokenKind.TYPE_PARAMETER;
        break;
      case RESOURCE_VARIABLE:
        tokenKind = HighlightTokenKind.RESOURCE_VARIABLE;
        break;
    }

    tokens.add(new HighlightToken(range, tokenKind));
  }

  private Range find(TreePath path, Name name) {
    // Find region containing name
    SourcePositions pos = trees.getSourcePositions();
    CompilationUnitTree root = path.getCompilationUnit();
    Tree leaf = path.getLeaf();
    int start = (int) pos.getStartPosition(root, leaf);
    int end = (int) pos.getEndPosition(root, leaf);
    // Adjust start to remove LHS of declarations and member selections
    if (leaf instanceof MemberSelectTree) {
      MemberSelectTree select = (MemberSelectTree) leaf;
      start = (int) pos.getEndPosition(root, select.getExpression());
    } else if (leaf instanceof VariableTree) {
      VariableTree declaration = (VariableTree) leaf;
      start = (int) pos.getEndPosition(root, declaration.getType());
    }
    // If no position, give up
    if (start == -1 || end == -1) {
      return Range.NONE;
    }
    // Find name inside expression
    Path file = Paths.get(root.getSourceFile().toUri());
    CharSequence contents = FileManager.INSTANCE.getDocumentContents(file);
    String region = substring(contents, start, end);
    start += region.indexOf(name.toString());
    end = start + name.length();
    return new Range(getPosition(start, root.getLineMap()), getPosition(end, root.getLineMap()));
  }

  @NonNull
  private static Position getPosition(long position, @NonNull LineMap lines) {
    // decrement the numbers
    // to convert 1-based indexes to 0-based
    final int line = (int) lines.getLineNumber(position) - 1;
    final int column = (int) lines.getColumnNumber(position) - 1;
    return new Position(line, column);
  }
}
