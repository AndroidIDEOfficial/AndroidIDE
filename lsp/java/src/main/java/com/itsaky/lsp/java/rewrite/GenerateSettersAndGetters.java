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

import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.utils.EditHelper;
import com.itsaky.lsp.java.utils.JavaPoetUtils;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.lsp.models.Command;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.TextEdit;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * @author Akash Yadav
 */
public class GenerateSettersAndGetters extends Rewrite {

    private final Path file;
    private final CompileTask task;
    private final List<TreePath> variables;

    public GenerateSettersAndGetters(Path file, CompileTask task, List<TreePath> variables) {
        Objects.requireNonNull(task);
        Objects.requireNonNull(variables);

        this.file = file;
        this.task = task;
        this.variables = variables;
    }

    @Override
    public Map<Path, TextEdit[]> rewrite(CompilerProvider compiler) {
        final List<TextEdit> edits = new ArrayList<>();
        final Trees trees = Trees.instance(task.task);
        for (TreePath path : variables) {
            final Element element = trees.getElement(path);
            if (element == null || element.getKind() != ElementKind.FIELD) {
                continue;
            }

            final VariableElement variable = (VariableElement) element;
            final Tree leaf = path.getLeaf();
            addGetter(compiler, variable, leaf, edits);

            if (!variable.getModifiers().contains(Modifier.FINAL)) {
                addSetter(compiler, variable, leaf, edits);
            }
        }

        return Collections.singletonMap(file, edits.toArray(new TextEdit[0]));
    }

    @Override
    protected void applyCommands(@NonNull CodeActionItem action) {
        action.setCommand(new Command("Format code", Command.FORMAT_CODE));
    }

    private void addSetter(
            CompilerProvider compiler,
            @NonNull VariableElement variable,
            Tree leaf,
            @NonNull List<TextEdit> edits) {
        final int indent = EditHelper.indent(task.task, task.root(), leaf) + 4;
        final Position insertAt = EditHelper.insertAfter(task.task, task.root(), leaf);
        final String name = variable.getSimpleName().toString();
        MethodSpec.Builder builder = MethodSpec.methodBuilder(createName(name, "set"));
        builder.addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(TypeName.get(variable.asType()), name)
                .addStatement("this.$N = $N", name, name);

        final Set<String> imports = new TreeSet<>();
        String text = JavaPoetUtils.print(builder.build(), imports);
        text = text.replace("\n", "\n".concat(EditHelper.repeatSpaces(indent)));
        text = text.concat("\n");

        edits.add(new TextEdit(new Range(insertAt, insertAt), text));
    }

    private void addGetter(
            CompilerProvider compiler,
            @NonNull VariableElement variable,
            Tree leaf,
            @NonNull List<TextEdit> edits) {
        final int indent = EditHelper.indent(task.task, task.root(), leaf) + 4;
        final Position insertAt = EditHelper.insertAfter(task.task, task.root(), leaf);
        final String name = variable.getSimpleName().toString();
        MethodSpec.Builder builder = MethodSpec.methodBuilder(createName(name, "get"));
        builder.addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(variable.asType()))
                .addStatement("return $N", name);

        final Set<String> imports = new TreeSet<>();
        String text = JavaPoetUtils.print(builder.build(), imports);
        text = text.replace("\n", "\n".concat(EditHelper.repeatSpaces(indent)));
        text = text.concat("\n");

        edits.add(new TextEdit(new Range(insertAt, insertAt), text));
    }

    @NonNull
    private String createName(String name, String prefix) {
        final StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, prefix);
        return sb.toString();
    }
}
