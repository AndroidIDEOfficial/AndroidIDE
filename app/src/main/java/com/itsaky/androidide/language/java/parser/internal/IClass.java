package com.itsaky.androidide.language.java.parser.internal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.itsaky.androidide.language.java.parser.model.ConstructorDescription;
import java.util.ArrayList;
import java.util.List;

public interface IClass extends IJavaDocCommentable {
    int getModifiers();

    @Nullable
    String getFullClassName();

    @Nullable
    String getSimpleName();

    boolean isInterface();

    boolean isEnum();

    boolean isPrimitive();

    boolean isAnnotation();
	
	boolean isMemberClass();

    @Nullable
    IMethod getMethod(@NonNull String methodName, @Nullable IClass[] argsType);

    @Nullable
    IField getField(@NonNull String name);

    List<IMethod> getMethods();

    @Nullable
    IClass getSuperclass();

    ArrayList<IField> getFields();

    List<SuggestItem> getMember(String prefix);

    List<ConstructorDescription> getConstructors();
}
