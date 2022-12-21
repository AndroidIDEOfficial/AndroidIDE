package com.itsaky.androidide.fuzzysearch.algorithms;

import com.itsaky.androidide.fuzzysearch.ToStringFunction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultStringFunction implements ToStringFunction<String> {

  private static final String pattern = "(?ui)\\W";
  private static final Pattern r = compilePattern();

  private static Pattern compilePattern() {

    Pattern p;

    try {
      p = Pattern.compile(pattern, Pattern.UNICODE_CHARACTER_CLASS);
    } catch (IllegalArgumentException e) {
      // Even though Android supports the unicode pattern class
      // for some reason it throws an IllegalArgumentException
      // if we pass the flag like on standard Java runtime
      //
      // We catch this and recompile without the flag (unicode should still work)
      p = Pattern.compile(pattern);
    }

    return p;
  }

  /**
   * Performs the default string processing on the input string
   *
   * @param in Input string
   * @return The processed string
   */
  @Override
  public String apply(String in) {

    in = subNonAlphaNumeric(in, " ");
    in = in.toLowerCase();
    in = in.trim();

    return in;
  }

  /**
   * Substitute non alphanumeric characters.
   *
   * @param in The input string
   * @param sub The string to substitute with
   * @return The replaced string
   */
  public static String subNonAlphaNumeric(String in, String sub) {

    Matcher m = r.matcher(in);

    if (m.find()) {
      return m.replaceAll(sub);
    } else {
      return in;
    }
  }
}
