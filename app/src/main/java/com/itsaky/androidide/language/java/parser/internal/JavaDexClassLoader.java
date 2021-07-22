package com.itsaky.androidide.language.java.parser.internal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.itsaky.androidide.interfaces.Filter;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.models.AndroidProject;
import java.io.File;
import java.util.List;

/**
 * Created by Duy on 20-Jul-17.
 */

public class JavaDexClassLoader {
    private static final Filter<IClass> mClassFilter = new Filter<IClass>() {
        @Override
        public boolean accept(IClass aClass) {
            return !(aClass.isAnnotation() || aClass.isInterface() || aClass.isEnum() || aClass.isMemberClass());
        }
    };
    private static final Filter<IClass> mEnumFilter = new Filter<IClass>() {
        @Override
        public boolean accept(IClass aClass) {
            return aClass.isEnum();
        }
    };
    private static final Filter<IClass> mInterfaceFilter = new Filter<IClass>() {
        @Override
        public boolean accept(IClass aClass) {
            return aClass.isInterface();
        }
    };
    private static final Filter<IClass> mAnnotationFilter = new Filter<IClass>() {
        @Override
        public boolean accept(IClass aClass) {
            return aClass.isAnnotation();
        }
    };

    private JavaClassManager mClassReader;

    public JavaDexClassLoader(File classpath) {
        mClassReader = JavaClassManager.getInstance(classpath);
    }

    public JavaClassManager getClassReader() {
        return mClassReader;
    }

    @NonNull
    public List<IClass> findAllWithPrefix(String simpleNamePrefix) {
        return mClassReader.find(simpleNamePrefix, null);
    }

    @NonNull
    public List<IClass> findAllWithPrefix(@NonNull String simpleNamePrefix,
                                          @Nullable Filter<IClass> filter) {
        return mClassReader.find(simpleNamePrefix, filter);
    }

    public List<IClass> findClasses(String simpleNamePrefix) {
        return mClassReader.find(simpleNamePrefix, mClassFilter);
    }

    public List<IClass> findInterfaces(String simpleNamePrefix) {
        return mClassReader.find(simpleNamePrefix, mInterfaceFilter);
    }

    public List<IClass> findEnums(String simpleNamePrefix) {
        return mClassReader.find(simpleNamePrefix, mEnumFilter);

    }

    public List<IClass> findAnnotations(String simpleNamePrefix) {
        return mClassReader.find(simpleNamePrefix, mAnnotationFilter);
    }


    public void loadAllClasses(AndroidProject projectFile) {
        mClassReader.loadFromProject(projectFile);
    }

    public void updateClasses(List<IClass> classes) {
        for (IClass aClass : classes) {
            mClassReader.update(aClass);
        }
    }
	
	public void updateClass(IClass... classes) {
		for (IClass aClass : classes) {
            mClassReader.update(aClass);
        }
	}
}
