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

package com.squareup.javapoet;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.jetbrains.annotations.NotNull;

/**
 * A code writer implementation which is used by {@link TypeName} to provide list of classes that
 * need to be implemented.
 *
 * @author Akash Yadav
 */
public class ImportCollectingCodeWriter extends CodeWriter {
  private final Set<String> importClasses = new TreeSet<>();
  private boolean printQualifiedNames = true;

  public ImportCollectingCodeWriter(Appendable out) {
    super(out);
  }

  public ImportCollectingCodeWriter(
      Appendable out, String indent, Set<String> staticImports, Set<String> alwaysQualify) {
    super(out, indent, staticImports, alwaysQualify);
  }

  public ImportCollectingCodeWriter(
      Appendable out,
      String indent,
      Map<String, ClassName> importedTypes,
      Set<String> staticImports,
      Set<String> alwaysQualify) {
    super(out, indent, importedTypes, staticImports, alwaysQualify);
  }

  public boolean isPrintQualifiedNames() {
    return printQualifiedNames;
  }

  public void setPrintQualifiedNames(boolean printQualifiedNames) {
    this.printQualifiedNames = printQualifiedNames;
  }

  public void addImport(String qualifiedName) {
    this.importClasses.add(qualifiedName);
  }

  @NotNull
  public Set<String> getImportClasses() {
    return importClasses;
  }

  public void emit(AnnotationSpec annotationSpec) throws IOException {
    emit("$L", annotationSpec);
  }

  public void emit(@NotNull ArrayTypeName arrayTypeName) throws IOException {
    arrayTypeName.emit(this);
  }

  public void emit(@NotNull ClassName className) throws IOException {
    className.emit(this);
  }

  public void emit(@NotNull FieldSpec field) throws IOException {
    field.emit(this, Collections.emptySet());
  }

  public void emit(@NotNull MethodSpec methodSpec) throws IOException {
    methodSpec.emit(this, "Constructor", Collections.emptySet());
  }

  public void emit(@NotNull ParameterizedTypeName parameterizedTypeName) throws IOException {
    parameterizedTypeName.emit(this);
  }

  public void emit(@NotNull ParameterSpec parameterSpec) throws IOException {
    parameterSpec.emit(this, false);
  }

  public void emit(@NotNull TypeName typeName) throws IOException {
    typeName.emit(this);
  }

  public void emit(@NotNull TypeSpec typeSpec) throws IOException {
    typeSpec.emit(this, null, Collections.emptySet());
  }

  public void emit(@NotNull TypeVariableName typeVariableName) throws IOException {
    typeVariableName.emit(this);
  }

  public void emit(@NotNull WildcardTypeName wildcardTypeName) throws IOException {
    wildcardTypeName.emit(this);
  }
}
