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

package com.itsaky.androidide.lsp.java.compiler;

import com.itsaky.androidide.lsp.java.models.CompilationRequest;
import com.itsaky.androidide.lsp.java.parser.ParseTask;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import jdkx.tools.JavaFileObject;

public interface CompilerProvider {
  Path NOT_FOUND = Paths.get("");

  TreeSet<String> publicTopLevelTypes();

  TreeSet<String> packagePrivateTopLevelTypes(String packageName);

  Optional<JavaFileObject> findAnywhere(String className);

  Path findTypeDeclaration(String className);

  Path[] findTypeReferences(String className);

  Path[] findMemberReferences(String className, String memberName);

  default List<String> findQualifiedNames(String simpleName) {
    return findQualifiedNames(simpleName, false);
  }

  List<String> findQualifiedNames(String simpleName, boolean onlyOne);

  ParseTask parse(Path file);

  ParseTask parse(JavaFileObject file);

  default SynchronizedTask compile(Path... files) {
    return compile(Arrays.stream(files).map(SourceFileObject::new).collect(Collectors.toList()));
  }

  default SynchronizedTask compile(Collection<? extends JavaFileObject> sources) {
    return compile(new CompilationRequest(sources));
  }

  SynchronizedTask compile(CompilationRequest request);
}
