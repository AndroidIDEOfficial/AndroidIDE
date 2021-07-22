package com.itsaky.androidide.language.java.parser.internal;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import com.itsaky.androidide.interfaces.Filter;
import java.util.List;

public interface IClassManager {
    void update(IClass clazz);

    void remove(String fullClassName);

    @NonNull
    List<IClass> find(@NonNull String simpleNamePrefix,
                      @Nullable Filter<IClass> filter);
}
