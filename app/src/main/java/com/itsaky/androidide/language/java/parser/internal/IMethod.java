package com.itsaky.androidide.language.java.parser.internal;

import androidx.annotation.NonNull;

public interface IMethod extends IJavaDocCommentable {
    @NonNull
    String getMethodName();

    @NonNull
    IClass getMethodReturnType();

    @NonNull
    IClass[] getMethodParameterTypes();

    int getModifiers();
}
