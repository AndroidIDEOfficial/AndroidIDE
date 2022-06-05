package com.itsaky.androidide.utils;

import org.jetbrains.annotations.*;

public class AndroidUtils {
    /**
     * The package is used to create a directory (eg:
     * MyApplication/app/src/main/java/src/my/package/name) A windows directory path cannot be
     * longer than 250 chars On unix/mac a directory name cannot be longer than 250 chars On all
     * platforms, aapt fails with really cryptic errors if the package name is longer that ~200
     * chars Having a sane length for the package also seems a good thing
     */
    private static final int PACKAGE_LENGTH_LIMIT = 100;

    /**
     * Checks if the given name is a valid Android application package (which has additional
     * requirements beyond a normal Java package)
     *
     * @see #validateAndroidPackageName(String)
     */
    public static boolean isValidAndroidPackageName(@NotNull String name) {
        return validateAndroidPackageName(name) == null;
    }

    /**
     * Checks if the given name is a valid general Java package name.
     *
     * <p>If validating the Android package name, use {@link #validateAndroidPackageName(String)}
     * instead!
     */
    public static boolean isValidJavaPackageName(@NotNull String name) {
        int index = 0;
        while (true) {
            int index1 = name.indexOf('.', index);
            if (index1 < 0) index1 = name.length();
            if (!isIdentifier(name.substring(index, index1))) return false;
            if (index1 == name.length()) return true;
            index = index1 + 1;
        }
    }

    /**
     * Validates a potential package name and returns null if the package name is valid, and
     * otherwise returns a description for why it is not valid.
     *
     * <p>Note that Android package names are more restrictive than general Java package names; we
     * require at least two segments, limit the character set to [a-zA-Z0-9_] (Java allows any
     * {@link Character#isLetter(char)} and require that each segment start with a letter (Java
     * allows underscores at the beginning).
     *
     * <p>For details, see core/java/android/content/pm/PackageParser.java#validateName
     *
     * @param name the package name
     * @return null if the package is valid as an Android package name, and otherwise a description
     *     for why not
     */
    @Nullable
    public static String validateAndroidPackageName(@NotNull String name) {
        if (name.isEmpty()) {
            return "Package name is missing";
        }

        String packageManagerCheck = validateName(name);
        if (packageManagerCheck != null) {
            return packageManagerCheck;
        }
        if (!name.matches("^[a-z][a-z0-9_]*(\\.[a-z0-9_]+)+[0-9a-z_]$")) {
            return "Package name is not a valid";
        }
        return null;
    }

    @Nullable
    public static String validatePackageName(@Nullable String packageName) {
        packageName = (packageName == null) ? "" : packageName;
        if (packageName.length() >= PACKAGE_LENGTH_LIMIT) {
            return "Package name is to long";
        }
        return AndroidUtils.validateAndroidPackageName(packageName);
    }

    // This method is a copy of android.content.pm.PackageParser#validateName with the
    // error messages tweaked
    @Nullable
    private static String validateName(String name) {
        int N = name.length();
        boolean hasSep = false;
        boolean front = true;
        for (int i = 0; i < N; i++) {
            char c = name.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                front = false;
                continue;
            }
            if ((c >= '0' && c <= '9') || c == '_') {
                if (!front) {
                    continue;
                } else {
                    if (c == '_') {
                        return "The character '_' cannot be the first character in a package segment";
                    } else {
                        return "A digit cannot be the first character in a package segment";
                    }
                }
            }
            if (c == '.') {
                hasSep = true;
                front = true;
                continue;
            }
            return "The character '" + c + "' is not allowed in Android application package names";
        }
        return hasSep ? null : "The package must have at least one '.' separator";
    }

    public static boolean isIdentifier(@NotNull String candidate) {
        return StringUtil.isJavaIdentifier(candidate);
    }
}
