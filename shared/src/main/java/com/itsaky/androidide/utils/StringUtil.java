package com.itsaky.androidide.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StringUtil {

  @Contract(pure = true)
  public static boolean isJavaIdentifier(@NotNull String text) {
    int len = text.length();
    if (len == 0) return false;

    if (!isJavaIdentifierStart(text.charAt(0))) return false;

    for (int i = 1; i < len; i++) {
      if (!isJavaIdentifierPart(text.charAt(i))) return false;
    }
    return true;
  }

  @Contract(pure = true)
  public static boolean isJavaIdentifierStart(char c) {
    return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || Character.isJavaIdentifierStart(c);
  }

  @Contract(pure = true)
  public static boolean isJavaIdentifierPart(char c) {
    return c >= '0' && c <= '9'
        || c >= 'a' && c <= 'z'
        || c >= 'A' && c <= 'Z'
        || Character.isJavaIdentifierPart(c);
  }
}
