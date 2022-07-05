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

package com.itsaky.lsp.java.compiler;

import com.itsaky.lsp.java.models.CompilationRequest;
import com.itsaky.lsp.java.parser.ParseTask;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.tools.JavaFileObject;

public interface CompilerProvider {
  Path NOT_FOUND = Paths.get("");

  Set<String> imports();

  List<String> publicTopLevelTypes();

  List<String> packagePrivateTopLevelTypes(String packageName);

  Iterable<Path> search(String query);

  Optional<JavaFileObject> findAnywhere(String className);

  Path findTypeDeclaration(String className);

  Path[] findTypeReferences(String className);

  Path[] findMemberReferences(String className, String memberName);

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
