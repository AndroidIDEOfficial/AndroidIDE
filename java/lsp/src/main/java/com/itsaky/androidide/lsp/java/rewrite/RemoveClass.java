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
import com.itsaky.androidide.lsp.java.compiler.CompilerProvider;
import com.itsaky.androidide.lsp.java.parser.ParseTask;
import com.itsaky.androidide.lsp.java.utils.EditHelper;
import com.itsaky.androidide.lsp.java.visitors.FindTypeDeclarationAt;
import com.itsaky.androidide.lsp.models.TextEdit;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import openjdk.source.tree.ClassTree;

public class RemoveClass extends Rewrite {
  final Path file;
  final int position;

  public RemoveClass(Path file, int position) {
    this.file = file;
    this.position = position;
  }

  @NonNull
  @Override
  public Map<Path, TextEdit[]> rewrite(@NonNull CompilerProvider compiler) {
    ParseTask task = compiler.parse(file);
    final ClassTree type = new FindTypeDeclarationAt(task.task).scan(task.root, (long) position);
    TextEdit[] edits = {EditHelper.removeTree(task.task, task.root, type)};
    return Collections.singletonMap(file, edits);
  }
}
