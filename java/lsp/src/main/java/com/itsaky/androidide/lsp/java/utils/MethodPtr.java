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

package com.itsaky.androidide.lsp.java.utils;

import androidx.annotation.NonNull;
import com.squareup.javapoet.ImportCollectingCodeWriter;
import com.squareup.javapoet.TypeName;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import jdkx.lang.model.element.ExecutableElement;
import jdkx.lang.model.element.TypeElement;
import jdkx.lang.model.element.VariableElement;
import jdkx.lang.model.type.TypeKind;
import jdkx.lang.model.type.TypeMirror;
import jdkx.lang.model.util.Types;
import openjdk.source.util.JavacTask;

/**
 * @author Akash Yadav
 */
public class MethodPtr {

  public String className, methodName;
  public String[] erasedParameterTypes;
  public String[] simplifiedErasedParameterTypes;

  public MethodPtr(@NonNull JavacTask task, @NonNull ExecutableElement method) {
    final Types types = task.getTypes();
    final TypeElement parent = (TypeElement) method.getEnclosingElement();
    className = parent.getQualifiedName().toString();
    methodName = method.getSimpleName().toString();
    erasedParameterTypes = new String[method.getParameters().size()];
    simplifiedErasedParameterTypes = new String[erasedParameterTypes.length];

    for (int i = 0; i < erasedParameterTypes.length; i++) {
      final VariableElement param = method.getParameters().get(i);
      final TypeMirror type = param.asType();
      final TypeMirror erased = types.erasure(type);
      erasedParameterTypes[i] = erased.toString();
      simplifiedErasedParameterTypes[i] = simplify(erased);
    }
  }

  private String simplify(@NonNull TypeMirror type) {

    if (type.getKind() == TypeKind.NULL) {
      return type.toString();
    }

    final TypeName name = TypeName.get(type);
    try {
      return getSimpleName(name);
    } catch (IOException e) {
      return type.toString();
    }
  }

  @NonNull
  private String getSimpleName(TypeName name) throws IOException {
    final StringBuilder sb = new StringBuilder();
    final ImportCollectingCodeWriter writer = new ImportCollectingCodeWriter(sb);
    writer.setPrintQualifiedNames(false);
    writer.emit(name);
    return sb.toString();
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(className, methodName);
    result = 31 * result + Arrays.hashCode(erasedParameterTypes);
    result = 31 * result + Arrays.hashCode(simplifiedErasedParameterTypes);
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MethodPtr)) {
      return false;
    }
    MethodPtr methodPtr = (MethodPtr) o;
    return Objects.equals(className, methodPtr.className)
        && Objects.equals(methodName, methodPtr.methodName)
        && Arrays.equals(erasedParameterTypes, methodPtr.erasedParameterTypes)
        && Arrays.equals(simplifiedErasedParameterTypes, methodPtr.simplifiedErasedParameterTypes);
  }
}
