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

import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.compiler.SynchronizedTask;
import com.itsaky.lsp.java.parser.ParseTask;
import com.itsaky.lsp.java.utils.EditHelper;
import com.itsaky.lsp.java.utils.FindHelper;
import com.itsaky.lsp.java.visitors.FindTypeDeclarationAt;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.TextEdit;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

public class OverrideInheritedMethod extends Rewrite {
    final String superClassName, methodName;
    final String[] erasedParameterTypes;
    final Path file;
    final int insertPosition;

    public OverrideInheritedMethod(
            String superClassName,
            String methodName,
            String[] erasedParameterTypes,
            Path file,
            int insertPosition) {
        this.superClassName = superClassName;
        this.methodName = methodName;
        this.erasedParameterTypes = erasedParameterTypes;
        this.file = file;
        this.insertPosition = insertPosition;
    }

    @Override
    public Map<Path, TextEdit[]> rewrite(CompilerProvider compiler) {
        Position insertPoint = insertNearCursor(compiler);
        String insertText = insertText(compiler);
        TextEdit[] edits = {new TextEdit(new Range(insertPoint, insertPoint), insertText)};
        return Collections.singletonMap(file, edits);
    }

    private String insertText(CompilerProvider compiler) {
        final SynchronizedTask synchronizedTask = compiler.compile(file);
        return synchronizedTask.get(
                task -> {
                    final Types types = task.task.getTypes();
                    final Trees trees = Trees.instance(task.task);
                    final ExecutableElement superMethod =
                            FindHelper.findMethod(
                                    task, superClassName, methodName, erasedParameterTypes);
                    final ClassTree thisTree =
                            new FindTypeDeclarationAt(task.task)
                                    .scan(task.root(), (long) insertPosition);
                    final TreePath thisPath = trees.getPath(task.root(), thisTree);
                    final TypeElement thisClass = (TypeElement) trees.getElement(thisPath);
                    final ExecutableType parameterizedType =
                            (ExecutableType)
                                    types.asMemberOf(
                                            (DeclaredType) thisClass.asType(), superMethod);
                    final int indent = EditHelper.indent(task.task, task.root(), thisTree) + 4;
                    final Optional<JavaFileObject> sourceFile =
                            compiler.findAnywhere(superClassName);
                    if (!sourceFile.isPresent()) return "";
                    final ParseTask parse = compiler.parse(sourceFile.get());
                    final MethodTree source =
                            FindHelper.findMethod(
                                    parse, superClassName, methodName, erasedParameterTypes);
                    String text = EditHelper.printMethod(superMethod, parameterizedType, source);
                    text = text.replaceAll("\n", "\n" + EditHelper.repeatSpaces(indent));
                    text = text + "\n\n";
                    return text;
                });
    }

    private Position insertNearCursor(CompilerProvider compiler) {
        final ParseTask task = compiler.parse(file);
        final ClassTree parent =
                new FindTypeDeclarationAt(task.task).scan(task.root, (long) insertPosition);
        final Position next = nextMember(task, parent);
        if (next != Position.NONE) {
            return next;
        }
        return EditHelper.insertAtEndOfClass(task.task, task.root, parent);
    }

    private Position nextMember(ParseTask task, ClassTree parent) {
        final SourcePositions pos = Trees.instance(task.task).getSourcePositions();
        for (Tree member : parent.getMembers()) {
            long start = pos.getStartPosition(task.root, member);
            if (start > insertPosition) {
                int line = (int) task.root.getLineMap().getLineNumber(start);
                return new Position(line - 1, 0);
            }
        }
        return Position.NONE;
    }
}
