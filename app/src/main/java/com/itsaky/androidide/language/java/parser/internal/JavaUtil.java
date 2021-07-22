package com.itsaky.androidide.language.java.parser.internal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.blankj.utilcode.util.FileUtils;
import com.sun.tools.javac.tree.JCTree;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Modifier;

/**
 * Created by Duy on 20-Jul-17.
 */

public class JavaUtil {
    public static String getSimpleName(String className) {
        if (className.contains(".")) {
            className = className.substring(className.lastIndexOf(".") + 1);
        }
		
		return getArrayTypeIfNeeded(className);
    }

    @NonNull
    public static String getPackageName(String classname) {
        if (classname.contains(".")) {
            return classname.substring(0, classname.lastIndexOf("."));
        } else {
            return "";
        }
    }

    public static String getInverseName(String className) {
        String[] split = className.split(".");
        String result = "";
        for (String s : split) {
            result = s + result;
        }
        return result;
    }


    public static boolean isValidClassName(@Nullable String name) {
        return name != null && name.matches("[A-Za-z_][A-Za-z0-9_]*");
    }

    @Nullable
    public static String getClassName(File javaSrc, String filePath) {
        if (filePath.startsWith(javaSrc.getPath())) {
            //hello/src/main/java
            //hello ->
            String filename = filePath.substring(filePath.indexOf(javaSrc.getPath()) + javaSrc.getPath().length() + 1);
            filename = filename.replace(File.separator, ".");
            if (filename.endsWith(".java")) {
                filename = filename.substring(0, filename.lastIndexOf(".java"));
            }
            return filename;
        } else {
            return null;
        }
    }

    public static int toJavaModifiers(Set<Modifier> modifierSet) {
        int modifiers = 0;
        for (Modifier modifier : modifierSet) {
            switch (modifier) {
                case FINAL:
                    modifiers |= java.lang.reflect.Modifier.FINAL;
                    break;
                case PROTECTED:
                    modifiers |= java.lang.reflect.Modifier.PROTECTED;

                    break;
                case PRIVATE:
                    modifiers |= java.lang.reflect.Modifier.PRIVATE;

                    break;
                case ABSTRACT:
                    modifiers |= java.lang.reflect.Modifier.ABSTRACT;

                    break;
                case NATIVE:
                    modifiers |= java.lang.reflect.Modifier.NATIVE;

                case PUBLIC:
                    modifiers |= java.lang.reflect.Modifier.PUBLIC;

                case STATIC:
                    modifiers |= java.lang.reflect.Modifier.STATIC;

                case TRANSIENT:
                    modifiers |= java.lang.reflect.Modifier.TRANSIENT;

                    break;
                case VOLATILE:
                    modifiers |= java.lang.reflect.Modifier.VOLATILE;

                    break;
                case SYNCHRONIZED:
                    modifiers |= java.lang.reflect.Modifier.SYNCHRONIZED;

                    break;
                case STRICTFP:
                    modifiers |= java.lang.reflect.Modifier.STRICT;

                    break;
            }
        }
        return modifiers;
    }

    @NonNull
    public static String findImportedClassName(@NonNull JCTree.JCCompilationUnit mUnit,
                                               @NonNull String className) {
        List<JCTree.JCImport> imports = mUnit.getImports();
        for (JCTree.JCImport jcImport : imports) {
            String fullName = jcImport.getQualifiedIdentifier().toString();
            if (fullName.equals(className) || fullName.endsWith("." + className)) {
                return fullName;
            }
        }
        return className;
    }

    @Nullable
    public static IClass jcTypeToClass(JCTree.JCCompilationUnit unit, JCTree type) {
        if (type == null) {
            return null;
        }
        String className;
        if (type instanceof JCTree.JCTypeApply) {
            return jcTypeToClass(unit, ((JCTree.JCTypeApply) type).getType());
        } else {
            className = type.toString();
        }
		
        className = findImportedClassName(unit, className);
        return JavaClassManager.getInstance().getParsedClass(className);
    }
    public static ArrayList<String> listClassName(File src) {
        if (!src.exists()) return new ArrayList<String>();
		
        Collection<File> files = FileUtils.listFilesInDirWithFilter(src, (f -> f.isFile() && f.getName().endsWith(".java")), true);

        ArrayList<String> classes = new ArrayList<>();
        String srcPath = src.getPath();
        for (File file : files) {
            String javaPath = file.getPath();
            javaPath = javaPath.substring(srcPath.length() + 1, javaPath.length() - 5); //.java
            javaPath = javaPath.replace(File.separator, ".");
            classes.add(javaPath);
        }
        return classes;
    }
	
	public static String getArrayTypeIfNeeded(String currentType) {
		if(currentType == null)
			return "";
		String type = new String(currentType);
		if(type.startsWith("[")) {
			type = type.substring(1).trim();
			if (type.equals("Z")) {
				type = "boolean[]";
			} else if (type.equals("B")) {
				type = "byte[]";
			} else if (type.equals("C")) {
				type = "char[]";
			} else if (type.equals("S")) {
				type = "short[]";
			} else if (type.equals("I")) {
				type = "int[]";
			} else if (type.equals("J")) {
				type = "long[]";
			} else if (type.equals("F")) {
				type = "float[]";
			} else if (type.equals("D")) {
				type = "double[]";
			} else {
				if(type.endsWith(";"))
					type = type.substring(0, type.length());
				type = type + "[]";
			}
		}
		return type;
	}


}
