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
import com.itsaky.lsp.java.parser.ParseTask;
import com.itsaky.lsp.java.utils.EditHelper;
import com.itsaky.lsp.java.utils.FindHelper;
import com.itsaky.lsp.java.visitors.FindAnonymousTypeDeclaration;
import com.itsaky.lsp.java.visitors.FindTypeDeclarationAt;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.TextEdit;
import com.squareup.javapoet.MethodSpec;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.util.JCDiagnostic;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

public class ImplementAbstractMethods extends Rewrite {

    private static final Logger LOG = Logger.instance("main");
    private final String className;
    private final String classFile;
    private final long position;

    public ImplementAbstractMethods(@NonNull String className, String classFile) {
        if (className.startsWith("<anonymous")) {
            className = className.substring("<anonymous ".length(), className.length() - 1);
        }
        this.className = className;
        this.classFile = classFile;
        this.position = 0;
    }

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
                    Types types = task.task.getTypes();
                    Trees trees = Trees.instance(task.task);
                    TypeElement thisClass = elements.getTypeElement(this.className);
                    DeclaredType thisType = (DeclaredType) thisClass.asType();

                    ClassTree thisTree = getClassTree(task, file);
                    if (thisTree == null) {
                        thisTree = trees.getTree(thisClass);
                    }

                    int indent = EditHelper.indent(task.task, task.root(), thisTree) + 4;
                    for (Element member : elements.getAllMembers(thisClass)) {
                        if (member.getKind() == ElementKind.METHOD
                                && member.getModifiers().contains(Modifier.ABSTRACT)) {
                            ExecutableElement method = (ExecutableElement) member;
                            String text = MethodSpec.overriding(method).build().toString();
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
    private MethodTree findSource(
            @NonNull CompilerProvider compiler,
            CompileTask task,
            @NonNull ExecutableElement method) {
        TypeElement superClass = (TypeElement) method.getEnclosingElement();
        String superClassName = superClass.getQualifiedName().toString();
        String methodName = method.getSimpleName().toString();
        String[] erasedParameterTypes = FindHelper.erasedParameterTypes(task, method);
        Optional<JavaFileObject> sourceFile = compiler.findAnywhere(superClassName);
        if (!sourceFile.isPresent()) return null;
        ParseTask parse = compiler.parse(sourceFile.get());
        return FindHelper.findMethod(parse, superClassName, methodName, erasedParameterTypes);
    }

    @Nullable
    private ClassTree getClassTree(CompileTask task, Path file) {
        ClassTree thisTree = null;
        CompilationUnitTree root = task.root(file);
        if (root == null) {
            return null;
        }
        if (position != 0) {
            thisTree = new FindTypeDeclarationAt(task.task).scan(root, position);
        }
        if (thisTree == null) {
            thisTree = new FindAnonymousTypeDeclaration(task.task, root).scan(root, position);
        }
        return thisTree;
    }
}
