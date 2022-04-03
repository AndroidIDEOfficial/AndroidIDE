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

package com.itsaky.lsp.java.utils;

import androidx.annotation.NonNull;
import com.sun.source.util.JavacTask;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * @author Akash Yadav
 */
public class MethodPtr {

    public String className, methodName;
    public String[] erasedParameterTypes;

    public MethodPtr(@NonNull JavacTask task, @NonNull ExecutableElement method) {
        final Types types = task.getTypes();
        final TypeElement parent = (TypeElement) method.getEnclosingElement();
        className = parent.getQualifiedName().toString();
        methodName = method.getSimpleName().toString();
        erasedParameterTypes = new String[method.getParameters().size()];
        for (int i = 0; i < erasedParameterTypes.length; i++) {
            final VariableElement param = method.getParameters().get(i);
            final TypeMirror type = param.asType();
            final TypeMirror erased = types.erasure(type);
            erasedParameterTypes[i] = erased.toString();
        }
    }
}
