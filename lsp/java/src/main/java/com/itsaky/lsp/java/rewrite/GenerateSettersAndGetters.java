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
    private final List<TreePath> variables;

    public GenerateSettersAndGetters(Path file, List<TreePath> variables) {
        Objects.requireNonNull(variables);

        this.file = file;
        this.variables = variables;
    }

    @Override
    public Map<Path, TextEdit[]> rewrite(CompilerProvider compiler) {
        final List<TextEdit> edits = new ArrayList<>();
        return compiler.compile(file)
                .get(
                        task -> {
                            final Trees trees = Trees.instance(task.task);
                            

                            return Collections.singletonMap(file, edits.toArray(new TextEdit[0]));
                        });
    }

    @Override
    protected void applyCommands(@NonNull CodeActionItem action) {
        action.setCommand(new Command("Format code", Command.FORMAT_CODE));
    }
}
