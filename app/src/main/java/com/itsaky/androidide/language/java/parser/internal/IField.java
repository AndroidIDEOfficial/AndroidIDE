package com.itsaky.androidide.language.java.parser.internal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IField extends IJavaDocCommentable {
    @NonNull
    String getFieldName();

    @NonNull
    IClass getFieldType();

    int getFieldModifiers();

    @Nullable
    Object getFieldValue();
}
