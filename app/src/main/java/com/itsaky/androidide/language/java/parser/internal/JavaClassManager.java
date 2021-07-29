package com.itsaky.androidide.language.java.parser.internal;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.interfaces.Filter;
import com.itsaky.androidide.language.java.parser.JavacParser;
import com.itsaky.androidide.language.java.parser.model.ClassDescription;
import com.itsaky.androidide.language.java.parser.model.FieldDescription;
import com.itsaky.androidide.language.java.parser.model.MethodDescription;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.utils.Logger;
import com.sun.tools.javac.tree.JCTree;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.classfmt.FieldInfo;
import org.eclipse.jdt.internal.compiler.env.IBinaryField;
import org.eclipse.jdt.internal.compiler.env.IBinaryMethod;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import com.itsaky.androidide.language.java.parser.model.ConstructorDescription;
import com.itsaky.androidide.utils.Either;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.utils.Environment;

public class JavaClassManager implements IClassManager {
    private static final String TAG = "JavaClassReader";
    private static final String JAVA_DOT_LANG_DOT = "java.lang.";
    private static JavaClassManager INSTANCE;

    /**
     * All classes sorted by simple class name, fastest find list of classes start with prefix
     */
    private final ArrayList<Pair<String, IClass>> mSimpleNames = new ArrayList<>();
    /**
     * All classes sorted by full class name, use for faster find class name by using binary search
     */
    private final ArrayList<IClass> mFullNames = new ArrayList<>();
    /**
     * Map contains parsed class, parsed class is java class in jar file or from user
     */
    private final HashMap<String, IClass> mLoaded = new HashMap<>();

    private File mBootClasspath;

    private JavaClassManager(File bootClassPath) {
        mBootClasspath = bootClassPath;
    }

    public static JavaClassManager getInstance(File classpath) {
        if (INSTANCE == null) {
            INSTANCE = new JavaClassManager(classpath);
        }
        return INSTANCE;
    }

    public static JavaClassManager getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("JavaClassManager not init");
        }
        return INSTANCE;
    }

    @NonNull
    public ArrayList<IClass> getAllClasses() {
        return mFullNames;
    }

    public void loadFromProject(AndroidProject project) {
        try {
            try {
                loadClasspaths(project);
            } catch (Throwable e) {}
            JavacParser parser = new JavacParser();
            List<String> javaSrcDirs = project.getSourcePaths();
            for (String dir : javaSrcDirs) {
				final File javaSrcDir = new File(dir);
                Collection<File> javaFiles = FileUtils.listFilesInDirWithFilter(javaSrcDir, (f -> f.isFile() && f.getName().endsWith(".java")), true);
                for (File javaFile : javaFiles) {
                    String content = FileIOUtils.readFile2String(javaFile);
                    JCTree.JCCompilationUnit ast = parser.parse(content);
                    List<IClass> parseClasses = parser.parseClasses(ast);
                    for (IClass aClass : parseClasses) {
                        update(aClass);
                    }
                }
            }
        } catch (Throwable e) {}
	}

    /**
     * @param fullName - full class name
     */
    @Nullable
    public IClass getParsedClass(String fullName) {
        IClass cache = mLoaded.get(fullName);
        if (cache != null) {
            return cache;
        }
        return mLoaded.get(JAVA_DOT_LANG_DOT + fullName);
    }

    @NonNull
    public IClass getClassWrapper(@NonNull Class clazz) {
        IClass cache = mLoaded.get(clazz.getName());
        if (cache != null) {
            return cache;
        }
        ClassDescription wrapper = new ClassDescription(clazz);
        update(wrapper);
        wrapper.initMembers(clazz);
        return wrapper;
    }

    @Override
    public void update(IClass value) {
        String fullClassName = value.getFullClassName();
        if (mLoaded.containsKey(fullClassName)) {
            mLoaded.put(fullClassName, value);
            return;
        }
        mLoaded.put(fullClassName, value);

        if (mFullNames.size() == 0) {
            mFullNames.add(value);
        } else {
            boolean found = false;
            for (int i = 0; i < mFullNames.size(); i++) {
                IClass iClass = mFullNames.get(i);
                if (fullClassName.compareTo(iClass.getFullClassName()) <= 0) {
                    mFullNames.add(i, value);
                    found = true;
                    break;
                }
            }
            if (!found) {
                mFullNames.add(value);
            }
        }
        String simpleName = value.getSimpleName();
        Pair<String, IClass> simplePair = new Pair<String, IClass>(simpleName, value);
        if (mSimpleNames.size() == 0) {
            mSimpleNames.add(simplePair);
        } else {
            boolean found = false;
            for (int i = 0; i < mSimpleNames.size(); i++) {
                Pair<String, IClass> pair = mSimpleNames.get(i);
                if (simpleName.compareTo(pair.first) <= 0) {
                    mSimpleNames.add(i, simplePair);
                    found = true;
                    break;
                }
            }
            if (!found) {
                mSimpleNames.add(simplePair);
            }
        }
    }

    @Override
    public void remove(String fullClassName) {
        IClass remove = mLoaded.remove(fullClassName);
        int index = Collections.binarySearch(mFullNames, remove,
			new Comparator<IClass>() {
				@Override
				public int compare(IClass o1, IClass o2) {
					return o1.getFullClassName().compareTo(o2.getFullClassName());
				}
			});
        if (index >= 0) {
            mFullNames.remove(index);
        }
        Pair<String, IClass> key = new Pair<String, IClass>(remove.getSimpleName(), remove);
        index = Collections.binarySearch(mSimpleNames, key,
			new Comparator<Pair<String, IClass>>() {
				@Override
				public int compare(Pair<String, IClass> o1,
								   Pair<String, IClass> o2) {
					return o1.first.compareTo(o2.first);
				}
			});
        if (index >= 0) {
            mSimpleNames.remove(index);
        }
    }

    /**
     * search class with binary search
     *
     * @param classes - sorted classes by name
     * @param prefix  - prefix if class name
     */
    @Nullable
    private List<Pair<String, IClass>> binarySearch(
		@NonNull List<Pair<String, IClass>> classes,
		@NonNull String prefix) {
        int start = -1, end = -1;
        int left = 0;
        int right = classes.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            String name = classes.get(mid).first;
			String midValue = name.substring(0, Math.min(name.length(), prefix.length()));
            if (midValue.trim().toLowerCase(Locale.US).compareTo(prefix.toLowerCase(Locale.US)) < 0) {
                left = mid + 1;
			} else if (midValue.trim().toLowerCase(Locale.US).compareTo(prefix.toLowerCase(Locale.US)) > 0) {
                right = mid - 1;
            } else {
                start = mid;
                end = mid;
                while (start >= 0 &&
					   classes.get(start)
					   .first.trim().toLowerCase(Locale.US)
					   .startsWith(prefix.toLowerCase(Locale.US))) {
                    start--;
                }
                while (end < classes.size() &&
					   classes.get(end)
					   .first.trim().toLowerCase(Locale.US)
					   .startsWith(prefix.toLowerCase(Locale.US))) {
                    end++;
                }
                start++;
                end--;
                break;
            }
        }
        if (end >= 0 && start >= 0 && end - start + 1 /*length*/ >= 1) {
            return classes.subList(start, end + 1);
        }
        return null;
    }

    @Override
    @NonNull
    public List<IClass> find(@NonNull String simpleNamePrefix, @Nullable Filter<IClass> filter) {
        ArrayList<IClass> result = new ArrayList<>();
        List<Pair<String, IClass>> simple = binarySearch(mSimpleNames, simpleNamePrefix);
        if (simple != null) {
            for (Pair<String, IClass> c : simple) {
                if (filter != null) {
                    if (!filter.accept(c.second)) {
                        continue;
                    }
                }
                result.add(c.second);
            }
        }
        return result;
    }
    
    private void loadClasspaths(AndroidProject project) throws IOException, ClassFormatException, ClassNotFoundException {
        final List<String> classpaths = project.getClassPaths();
        if(!classpaths.contains(Environment.BOOTCLASSPATH))
            classpaths.add(Environment.BOOTCLASSPATH.getAbsolutePath());
        for(int i=0;i<classpaths.size();i++) {
            final File cp = new File(classpaths.get(i));
            if(!(cp.exists() && cp.getName().endsWith(".jar"))) continue;
            final ZipFile zip = new ZipFile(cp);
            final Enumeration<? extends ZipEntry> entries = zip.entries();

            ZipEntry entry = null;
            while(entries.hasMoreElements() && ((entry = entries.nextElement()) != null)) {
                if(!entry.getName().endsWith(".class"))
                    continue;
                try {
                    ClassFileReader reader = ClassFileReader.read(zip, entry.getName());
                    describe(zip, reader);
                } catch (Throwable th) {}
            }
        }
    }

    private ClassDescription describe(ZipFile zip, IBinaryType type) throws IOException, ClassFormatException, ClassNotFoundException {
        if(type == null)
            return null;
        final char[] name = type.getName();
        final char[] superClassName = type.getSuperclassName();
        if(name == null) return null;
        final int mods = type.getModifiers();
        final boolean isPremitive = false;
        final boolean isAnnotation = false;
        final boolean isEnum = false;
        final boolean isMember = type.getEnclosingTypeName() != null;
        final IBinaryField[] fields = type.getFields();
        final IBinaryMethod[] methods = type.getMethods();
        final char[][] interfaces = type.getInterfaceNames();
        ClassDescription clazz = new ClassDescription(bytecodeToSourceType(new String(name)), mods, isPremitive, isAnnotation, isEnum, isMember);
        
        int index = 0;
        while(fields != null && index < fields.length) {
            IBinaryField f = fields[index];
            if(f == null) continue;
            FieldDescription field = describe(zip, f);
            if(field == null) continue;
            clazz.addField(field);
            ++index;
        }
        
        index = 0;
        while(methods != null && index < methods.length) {
            IBinaryMethod m = methods[index];
            if(m == null) continue;
            Either<MethodDescription, ConstructorDescription> result = describe(zip, m);
            if(result == null) continue;
            if(result.isLeft())
                clazz.addMethod(result.getLeft());
            else clazz.addConstructor(result.getRight());
            ++index;
        }
        
        // TODO: Add interfaces
        
        clazz.setSuperclass(superClassName == null ? ClassDescription.ofObject() : findType(zip, new String(superClassName)));
        
        update(clazz);
        return clazz;
    }

    private Either<MethodDescription, ConstructorDescription> describe(ZipFile zip, IBinaryMethod m) throws IOException, IllegalArgumentException, ClassFormatException, ClassNotFoundException {
        if(zip == null || m == null) return null;
        final char[] descriptor = m.getMethodDescriptor();
        final char[] signature = m.getGenericSignature();
        final char[] name = m.getSelector();
        final int mods = m.getModifiers();
        if(name == null || descriptor == null) return null;
        IClass returns = findType(zip, Signature.getReturnType(new String(descriptor)));
        String[] paramTypes = Signature.getParameterTypes(new String(descriptor));
        ArrayList<String> params = new ArrayList<>();
        for(String type : paramTypes) {
            params.add(bytecodeToSourceType(type));
        }
        return m.isConstructor() ? Either.forRight(new ConstructorDescription(new String(name).replace("/", "."), params)) : Either.forLeft(new MethodDescription(new String(name).replace("/", "."), returns, mods, params));
    }

    private FieldDescription describe(ZipFile zip, IBinaryField field) throws IOException, ClassFormatException, ClassNotFoundException {
        if(field == null)
            return null;
        final char[] n = field.getName();
        final char[] t = field.getTypeName();
        if(n == null || t == null) return null;
        final String name = new String(n);
        final String type = new String(t);
        final int mods = field.getModifiers();
        final String value = field instanceof FieldInfo && ((FieldInfo) field).hasConstant() ? field.getConstant().stringValue() : "";
        IClass typeClass = findType(zip, type);
        if(typeClass == null)
            typeClass = new ClassDescription(Object.class);
        FieldDescription f = new FieldDescription(mods, typeClass, name.replace("/", "."), value);
        return f;
    }

    private IClass findType(ZipFile zip, String name) throws IOException, ClassFormatException, ClassNotFoundException {
        if(zip == null || name == null)
            return null;
        final int arrayCount = Signature.getArrayCount(name);
        if(arrayCount == 0) {
            if(name.length() == 1) {
                // A primitive type
                final Class load = primitiveType(name.charAt(0));
                if(load != null) {
                    ClassDescription desc = new ClassDescription(load);
                    desc.initMembers(load);
                    update(desc);
                    return desc;
                }
            }
            IClass found = getParsedClass(bytecodeToSourceType(name));
            if(found != null) {
                return found;
            } else {
                ClassFileReader reader = ClassFileReader.read(zip, name);
                if(reader == null)
                    return null;
                else {
                   // This class will be updated in describe() method
                   return describe(zip, reader); 
                }
            }
        } else {
            if(Signature.getElementType(name).length() == 1) {
                // This type may be an array of primitive
                Class load = Class.forName(name);
                if(load != null) {
                    ClassDescription desc = new ClassDescription(load);
                    desc.initMembers(load);
                    update(desc);
                    return desc;
                }
            }
            // TODO: Load array type classes of objects
            // Return without loading members of this class
            return new ClassDescription(Void.TYPE);
        }
    }
    
    private String bytecodeToSourceType(String type) {
        type = Signature.getElementType(type);
        if(type.startsWith("L") && type.endsWith(";")) {
            type = type.substring(1, type.lastIndexOf(";"));
        }
        return type.replace("/", ".");
    }

    public Class primitiveType(char typeId) {
        switch (typeId) {
            case 'I' : return int.class;
            case 'B' : return byte.class;
            case 'S' : return short.class;
            case 'C' : return char.class;
            case 'F' : return float.class;
            case 'D' : return double.class;
            case 'Z' : return boolean.class;
            case 'J' : return long.class;
            default : return null;
        }
    }
    
    
    /**
     * Kept as a backup if loading jar files fails for some reasons
     */
    private ArrayList<Class> loadFromDexes(AndroidProject project) {
        ArrayList<Class> classes = new ArrayList<>();
        try {
            final List<String> dexes = ConstantsBridge.PROJECT_DEXES;
            final PathClassLoader loader = new PathClassLoader(TextUtils.join(File.pathSeparator, dexes), ClassLoader.getSystemClassLoader());
            if (dexes == null || dexes.size() <= 0) return classes;
            for (String dex : dexes) {
                final DexFile d = new DexFile(new File(dex));
                Enumeration<String> entries = d.entries();
                String name = null;
                while (entries.hasMoreElements() && (name = entries.nextElement()) != null) {
                    while(name.startsWith(File.separator))
                        name = name.substring(1);
                    if(name.contains(File.separator))
                        name = name.replace(File.separator, ".");
                    Class<?> load = tryLoad(loader, name);
                    if (load != null)
                        classes.add(load);
                }
                d.close();
            }

            ConstantsBridge.CLASS_LOAD_SUCCESS = true;
            return classes;
        } catch (Throwable th) {
            ConstantsBridge.CLASS_LOAD_SUCCESS = false;
            return classes;
        }
    }

    private Class<?> tryLoad(ClassLoader loader, String name) {
        try {
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
	}

}
