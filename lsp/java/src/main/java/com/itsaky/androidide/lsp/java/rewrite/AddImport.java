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
import androidx.annotation.RestrictTo;
import com.itsaky.androidide.lsp.java.compiler.CompilerProvider;
import com.itsaky.androidide.lsp.java.parser.ParseTask;
import com.itsaky.androidide.lsp.java.utils.InsertUtilsKt;
import com.itsaky.androidide.lsp.models.TextEdit;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public class AddImport extends Rewrite {

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  public final String className;

  final Path file;

  public AddImport(Path file, String className) {
    this.file = file;
    this.className = className;
  }

  @NonNull
  @Override
  public Map<Path, TextEdit[]> rewrite(@NonNull CompilerProvider compiler) {
    final ParseTask task = compiler.parse(file);
    Position point = InsertUtilsKt.positionForImports(className, task);
    String text = "import " + className + ";\n";
    return Collections.singletonMap(
        file, new TextEdit[] {new TextEdit(new Range(point, point), text)});
  }
}
