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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.compiler.SynchronizedTask;
import com.itsaky.lsp.java.utils.EditHelper;
import com.itsaky.lsp.java.visitors.FindAnonymousTypeDeclaration;
import com.itsaky.lsp.java.visitors.FindTypeDeclarationAt;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.lsp.models.Command;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.TextEdit;
import com.squareup.javapoet.MethodSpec;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.util.JCDiagnostic;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.StringJoiner;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class ImplementAbstractMethods extends Rewrite {

    private static final Logger LOG = Logger.newInstance("main");
    private final String className;
    private final String classFile;
    private final long position;

    public ImplementAbstractMethods(@NonNull JCDiagnostic diagnostic) {
        Object[] args = diagnostic.getArgs();
        String className = args[0].toString();

        if (!className.contains("<anonymous")) {
            this.className = className;
            this.classFile = className;
            this.position = 0;
        } else {
            className = className.substring("<anonymous ".length(), className.length() - 1);
            className = className.substring(0, className.indexOf('$'));
            this.classFile = className;
            this.className = args[2].toString();
            this.position = diagnostic.getStartPosition();
        }
    }

    @Override
    public Map<Path, TextEdit[]> rewrite(@NonNull CompilerProvider compiler) {
        final Path file = compiler.findTypeDeclaration(this.classFile);
        final SynchronizedTask synchronizedTask = compiler.compile(file);
        return synchronizedTask.get(
                task -> {
                    StringJoiner insertText = new StringJoiner("\n");
                    Elements elements = task.task.getElements();
                    Trees trees = Trees.instance(task.task);
                    TypeElement thisClass = elements.getTypeElement(this.className);

                    ClassTree thisTree = getClassTree(task, file);
                    if (thisTree == null) {
                        thisTree = trees.getTree(thisClass);
                    }

                    int indent = EditHelper.indent(task.task, task.root(), thisTree) + 4;
                    for (Element member : elements.getAllMembers(thisClass)) {
                        if (member.getKind() == ElementKind.METHOD
                                && member.getModifiers().contains(Modifier.ABSTRACT)) {
                            ExecutableElement method = (ExecutableElement) member;
                            final MethodSpec methodSpec = MethodSpec.overriding(method).build();
                            String text = "\n" + methodSpec;
                            text = text.replaceAll("\n", "\n" + EditHelper.repeatSpaces(indent));
                            insertText.add(text);
                        }
                    }

                    Position insert =
                            EditHelper.insertAtEndOfClass(task.task, task.root(), thisTree);
                    TextEdit[] edits = {new TextEdit(new Range(insert, insert), insertText + "\n")};
                    return Collections.singletonMap(file, edits);
                });
    }

    @Nullable
    private ClassTree getClassTree(CompileTask task, Path file) {
        ClassTree thisTree = null;
        CompilationUnitTree root = task.root(file);
        if (root == null) {
            return null;
        }

        if (position != 0) {
            final FindTypeDeclarationAt scanner = new FindTypeDeclarationAt(task.task);
            thisTree = scanner.scan(root, position);
        }

        if (thisTree == null) {
            final FindAnonymousTypeDeclaration scanner =
                    new FindAnonymousTypeDeclaration(task.task, root);
            thisTree = scanner.scan(root, position);
        }

        return thisTree;
    }

    @Override
    protected void applyCommands(@NonNull CodeActionItem action) {
        action.setCommand(new Command("Format code", Command.FORMAT_CODE));
    }
}
