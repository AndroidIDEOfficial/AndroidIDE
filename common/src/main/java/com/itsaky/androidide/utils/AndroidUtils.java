package com.itsaky.androidide.utils;

import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.resources.R;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AndroidUtils {
  /**
   * The package is used to create a directory (eg:
   * MyApplication/app/src/main/java/src/my/package/name) A windows directory path cannot be longer
   * than 250 chars On unix/mac a directory name cannot be longer than 250 chars On all platforms,
   * aapt fails with really cryptic errors if the package name is longer that ~200 chars Having a
   * sane length for the package also seems a good thing
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
   * Validates a potential package name and returns null if the package name is valid, and otherwise
   * returns a description for why it is not valid.
   *
   * <p>Note that Android package names are more restrictive than general Java package names; we
   * require at least two segments, limit the character set to [a-zA-Z0-9_] (Java allows any {@link
   * Character#isLetter(char)} and require that each segment start with a letter (Java allows
   * underscores at the beginning).
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
      return BaseApplication.getBaseInstance().getString(R.string.msg_empty_package);
    }

    String packageManagerCheck = validateName(name);
    if (packageManagerCheck != null) {
      return packageManagerCheck;
    }
    if (!name.matches("^[a-z][a-z0-9_]*(\\.[a-z0-9_]+)+[0-9a-z_]$")) {
      return BaseApplication.getBaseInstance().getString(R.string.msg_package_is_not_valid);
    }
    return null;
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
            return BaseApplication.getBaseInstance()
                .getString(R.string.msg_package_not_valid_char_1);
          } else {
            return BaseApplication.getBaseInstance()
                .getString(R.string.msg_package_not_use_digit_first);
          }
        }
      }
      if (c == '.') {
        hasSep = true;
        front = true;
        continue;
      }
      return String.format(
          BaseApplication.getBaseInstance().getString(R.string.msg_package_not_valid_char_2), c);
    }
    return hasSep
        ? null
        : BaseApplication.getBaseInstance().getString(R.string.msg_package_mus_have_dot);
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

  public static boolean isIdentifier(@NotNull String candidate) {
    return StringUtil.isJavaIdentifier(candidate);
  }

  public static boolean validateNameChecker(@NotNull String name) {
    return name.matches("^([a-zA-Z0-9_]*)$");
  }

  @Nullable
  public static String validatePackageName(@Nullable String packageName) {
    packageName = (packageName == null) ? "" : packageName;
    if (packageName.length() >= PACKAGE_LENGTH_LIMIT) {
      return BaseApplication.getBaseInstance().getString(R.string.msg_package_is_to_long);
    }
    return AndroidUtils.validateAndroidPackageName(packageName);
  }

  /**
   * Converts a CamelCase word into an underlined_word
   *
   * @param string the CamelCase version of the word
   * @return the underlined version of the word
   */
  @NotNull
  public static String camelCaseToUnderlines(@NotNull String string) {
    if (string.isEmpty()) {
      return string;
    }

    StringBuilder sb = new StringBuilder(2 * string.length());
    int n = string.length();
    boolean lastWasUpperCase = Character.isUpperCase(string.charAt(0));
    for (int i = 0; i < n; i++) {
      char c = string.charAt(i);
      boolean isUpperCase = Character.isUpperCase(c);
      if (isUpperCase && !lastWasUpperCase) {
        sb.append('_');
      }
      lastWasUpperCase = isUpperCase;
      c = Character.toLowerCase(c);
      sb.append(c);
    }

    return sb.toString();
  }

  /**
   * Converts an underlined_word into a CamelCase word
   *
   * @param string the underlined word to convert
   * @return the CamelCase version of the word
   */
  @NotNull
  public static String underlinesToCamelCase(@NotNull String string) {
    StringBuilder sb = new StringBuilder(string.length());
    int n = string.length();

    int i = 0;
    @SuppressWarnings("SpellCheckingInspection")
    boolean upcaseNext = true;
    for (; i < n; i++) {
      char c = string.charAt(i);
      if (c == '_') {
        upcaseNext = true;
      } else {
        if (upcaseNext) {
          c = Character.toUpperCase(c);
        }
        upcaseNext = false;
        sb.append(c);
      }
    }

    return sb.toString();
  }

  public static String appNameToPackageName(String appName, String packageName) {
    String newAppName = trimWhiteSpace(appName).toLowerCase(Locale.getDefault());
    return getPackageDomain(packageName) + "." + newAppName;
  }

  public static String trimWhiteSpace(String string) {
    return StringUtils.deleteWhitespace(string);
  }

  public static String getPackageDomain(String packageName) {
    int pos = packageName.lastIndexOf(".");
    String domain;
    if (pos != -1) {
      domain = packageName.substring(0, pos);
    } else {
      domain = "com.example";
    }
    return domain;
  }
}
