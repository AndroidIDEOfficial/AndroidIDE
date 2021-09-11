package com.itsaky.androidide;


import com.github.megatronking.stringfog.xor.StringFogImpl;

/**
 * Generated code from StringFog gradle plugin. Do not modify!
 */
public final class StringFog {
  private static final StringFogImpl IMPL = new StringFogImpl();

  public static String encrypt(String value) {
    return IMPL.encrypt(value, "WVV0aGMwaEFZVzVFVW05SlJFbGtaUT09");
  }

  public static String decrypt(String value) {
    return IMPL.decrypt(value, "WVV0aGMwaEFZVzVFVW05SlJFbGtaUT09");
  }

  public static boolean overflow(String value) {
    return IMPL.overflow(value, "WVV0aGMwaEFZVzVFVW05SlJFbGtaUT09");
  }

}
