/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.utils;

import static com.android.SdkConstants.DOT_9PNG;
import static com.android.SdkConstants.DOT_BMP;
import static com.android.SdkConstants.DOT_GIF;
import static com.android.SdkConstants.DOT_JPEG;
import static com.android.SdkConstants.DOT_JPG;
import static com.android.SdkConstants.DOT_PNG;
import static com.android.SdkConstants.DOT_WEBP;
import static com.android.SdkConstants.DOT_XML;

import com.google.common.base.CaseFormat;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
  /** Prefix in comments which mark the source locations for merge results */
  public static final String FILENAME_PREFIX = "From: ";

  public static final List<String> IMAGE_EXTENSIONS =
      ImmutableList.of(DOT_PNG, DOT_9PNG, DOT_GIF, DOT_JPEG, DOT_JPG, DOT_BMP, DOT_WEBP);
  /** For use by {@link #getLineSeparator()} */
  private static String sLineSeparator;

  /**
   * Returns true if the given sequence ends with the given suffix (case sensitive).
   *
   * @param sequence the character sequence to be checked
   * @param suffix the suffix to look for
   * @return true if the given sequence ends with the given suffix
   */
  public static boolean endsWith(@NotNull CharSequence sequence, @NotNull CharSequence suffix) {
    return endsWith(sequence, sequence.length(), suffix);
  }

  /**
   * Returns true if the given sequence ends at the given offset with the given suffix (case
   * sensitive)
   *
   * @param sequence the character sequence to be checked
   * @param endOffset the offset at which the sequence is considered to end
   * @param suffix the suffix to look for
   * @return true if the given sequence ends with the given suffix
   */
  public static boolean endsWith(
      @NotNull CharSequence sequence, int endOffset, @NotNull CharSequence suffix) {
    if (endOffset < suffix.length()) {
      return false;
    }

    for (int i = endOffset - 1, j = suffix.length() - 1; j >= 0; i--, j--) {
      if (sequence.charAt(i) != suffix.charAt(j)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns true if the given string starts with the given prefix, using a case-insensitive
   * comparison.
   *
   * @param string the full string to be checked
   * @param prefix the prefix to be checked for
   * @return true if the string case-insensitively starts with the given prefix
   */
  public static boolean startsWithIgnoreCase(@NotNull String string, @NotNull String prefix) {
    return string.regionMatches(true /* ignoreCase */, 0, prefix, 0, prefix.length());
  }

  /**
   * Returns true if the given string starts at the given offset with the given prefix, case
   * insensitively.
   *
   * @param string the full string to be checked
   * @param offset the offset in the string to start looking
   * @param prefix the prefix to be checked for
   * @return true if the string case-insensitively starts at the given offset with the given prefix
   */
  public static boolean startsWith(@NotNull String string, int offset, @NotNull String prefix) {
    return string.regionMatches(true /* ignoreCase */, offset, prefix, 0, prefix.length());
  }

  /**
   * Strips the whitespace from the given string
   *
   * @param string the string to be cleaned up
   * @return the string, without whitespace
   */
  public static String stripWhitespace(@NotNull String string) {
    StringBuilder sb = new StringBuilder(string.length());
    for (int i = 0, n = string.length(); i < n; i++) {
      char c = string.charAt(i);
      if (!Character.isWhitespace(c)) {
        sb.append(c);
      }
    }

    return sb.toString();
  }

  /**
   * Returns true if the given string has an upper case character.
   *
   * @param s the string to check
   * @return true if it contains uppercase characters
   */
  public static boolean hasUpperCaseCharacter(@NotNull String s) {
    for (int i = 0; i < s.length(); i++) {
      if (Character.isUpperCase(s.charAt(i))) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the default line separator to use.
   *
   * <p>NOTE: If you have an associated IDocument (Eclipse), it is better to call
   * TextUtilities#getDefaultLineDelimiter(IDocument) since that will allow (for example) editing a
   * \r\n-delimited document on a \n-delimited platform and keep a consistent usage of delimiters in
   * the file.
   *
   * @return the delimiter string to use
   */
  @NotNull
  public static String getLineSeparator() {
    if (sLineSeparator == null) {
      // This is guaranteed to exist:
      sLineSeparator = System.getProperty("line.separator"); // $NON-NLS-1$
    }

    return sLineSeparator;
  }

  /**
   * Wraps the given text at the given line width, with an optional hanging indent.
   *
   * @param text the text to be wrapped
   * @param lineWidth the number of characters to wrap the text to
   * @param hangingIndent the hanging indent (to be used for the second and subsequent lines in each
   *     paragraph, or null if not known
   * @return the string, wrapped
   */
  @NotNull
  public static String wrap(@NotNull String text, int lineWidth, @Nullable String hangingIndent) {
    if (hangingIndent == null) {
      hangingIndent = "";
    }
    int explanationLength = text.length();
    StringBuilder sb = new StringBuilder(explanationLength * 2);
    int index = 0;

    while (index < explanationLength) {
      int lineEnd = text.indexOf('\n', index);
      int next;

      if (lineEnd != -1 && (lineEnd - index) < lineWidth) {
        next = lineEnd + 1;
      } else {
        // Line is longer than available width; grab as much as we can
        lineEnd = Math.min(index + lineWidth, explanationLength);
        if (lineEnd - index < lineWidth) {
          next = explanationLength;
        } else {
          // then back up to the last space
          int lastSpace = text.lastIndexOf(' ', lineEnd);
          if (lastSpace > index) {
            lineEnd = lastSpace;
            next = lastSpace + 1;
          } else {
            // No space anywhere on the line: it contains something wider than
            // can fit (like a long URL) so just hard break it
            next = lineEnd + 1;
          }
        }
      }

      if (sb.length() > 0) {
        sb.append(hangingIndent);
      } else {
        lineWidth -= hangingIndent.length();
      }

      sb.append(text.substring(index, lineEnd));
      sb.append('\n');
      index = next;
    }

    return sb.toString();
  }

  /**
   * Returns the given localized string as an int. For example, in the US locale, "1,000", will
   * return 1000. In the French locale, "1.000" will return 1000. If the format is invalid, returns
   * the supplied default value instead.
   *
   * @param string the string to be parsed
   * @param defaultValue the value to be returned if there is a parsing error
   * @return the integer value
   */
  public static int parseLocalizedInt(@NotNull String string, int defaultValue) {
    try {
      return parseLocalizedInt(string);
    } catch (ParseException e) {
      return defaultValue;
    }
  }

  /**
   * Returns the given localized string as an int. For example, in the US locale, "1,000", will
   * return 1000. In the French locale, "1.000" will return 1000. It will return 0 for empty
   * strings.
   *
   * <p>To parse a string without catching parser exceptions, call {@link #parseLocalizedInt(String,
   * int)} instead, passing the default value to be returned if the format is invalid.
   *
   * @param string the string to be parsed
   * @return the integer value
   * @throws ParseException if the format is not correct
   */
  public static int parseLocalizedInt(@NotNull String string) throws ParseException {
    if (string.isEmpty()) {
      return 0;
    }
    return NumberFormat.getIntegerInstance().parse(string).intValue();
  }

  /**
   * Returns the given localized string as a double. For example, in the US locale, "3.14", will
   * return 3.14. In the French locale, "3,14" will return 3.14. If the format is invalid, returns
   * the supplied default value instead.
   *
   * @param string the string to be parsed
   * @param defaultValue the value to be returned if there is a parsing error
   * @return the double value
   */
  public static double parseLocalizedDouble(@NotNull String string, double defaultValue) {
    try {
      return parseLocalizedDouble(string);
    } catch (ParseException e) {
      return defaultValue;
    }
  }

  /**
   * Returns the given localized string as a double. For example, in the US locale, "3.14", will
   * return 3.14. In the French locale, "3,14" will return 3.14. It will return 0 for empty strings.
   *
   * <p>To parse a string without catching parser exceptions, call {@link
   * #parseLocalizedDouble(String, double)} instead, passing the default value to be returned if the
   * format is invalid.
   *
   * @param string the string to be parsed
   * @return the double value
   * @throws ParseException if the format is not correct
   */
  public static double parseLocalizedDouble(@NotNull String string) throws ParseException {
    if (string.isEmpty()) {
      return 0.0;
    }
    return NumberFormat.getNumberInstance().parse(string).doubleValue();
  }

  /**
   * Returns the corresponding {@link File} for the given file:// url
   *
   * @param url the URL string, e.g. file://foo/bar
   * @return the corresponding {@link File} (which may or may not exist)
   * @throws MalformedURLException if the URL string is malformed or is not a file: URL
   */
  @NotNull
  public static File urlToFile(@NotNull String url) throws MalformedURLException {
    return urlToFile(new URL(url));
  }

  @NotNull
  public static File urlToFile(@NotNull URL url) throws MalformedURLException {
    try {
      return new File(url.toURI());
    } catch (IllegalArgumentException e) {
      MalformedURLException ex = new MalformedURLException(e.getLocalizedMessage());
      ex.initCause(e);
      throw ex;
    } catch (URISyntaxException e) {
      return new File(url.getPath());
    }
  }

  /**
   * Copies the given XML file to the given new path. It also inserts a comment at the end of the
   * file which points to the original source location. This is intended for use with error parsers
   * which can rewrite for example AAPT error messages in say layout or manifest files, which occur
   * in the merged (copied) output, and present it as an error pointing to one of the user's
   * original source files.
   */
  public static void copyXmlWithSourceReference(@NotNull File from, @NotNull File to)
      throws IOException {
    copyXmlWithComment(from, to, createPathComment(from, true));
  }

  /**
   * Creates the path comment XML string. Note that it does not escape characters such as &amp; and
   * &lt;; those are expected to be escaped by the caller (for example, handled by a call to {@link
   * org.w3c.dom.Document#createComment(String)})
   *
   * @param file the file to create a path comment for
   * @param includePadding whether to include padding. The final comment recognized by error
   *     recognizers expect padding between the {@code <!--} and the start marker (From:); you can
   *     disable padding if the caller already is in a context where the padding has been added.
   * @return the corresponding XML contents of the string
   */
  public static String createPathComment(@NotNull File file, boolean includePadding)
      throws MalformedURLException {
    String url = fileToUrlString(file);
    int dashes = url.indexOf("--");
    if (dashes != -1) { // Not allowed inside XML comments - for SGML compatibility. Sigh.
      url = url.replace("--", "%2D%2D");
    }

    if (includePadding) {
      return ' ' + FILENAME_PREFIX + url + ' ';
    } else {
      return FILENAME_PREFIX + url;
    }
  }

  /**
   * Returns the corresponding URL string for the given {@link File}
   *
   * @param file the file to look up the URL for
   * @return the corresponding URL
   * @throws MalformedURLException in very unexpected cases
   */
  public static String fileToUrlString(@NotNull File file) throws MalformedURLException {
    return fileToUrl(file).toExternalForm();
  }

  /**
   * Returns the corresponding URL for the given {@link File}
   *
   * @param file the file to look up the URL for
   * @return the corresponding URL
   * @throws MalformedURLException in very unexpected cases
   */
  public static URL fileToUrl(@NotNull File file) throws MalformedURLException {
    return file.toURI().toURL();
  }

  /** Copies a given XML file, and appends a given comment to the end */
  private static void copyXmlWithComment(
      @NotNull File from, @NotNull File to, @Nullable String comment) throws IOException {
    assert endsWithIgnoreCase(from.getPath(), DOT_XML) : from;

    int successfulOps = 0;
    InputStream in = new FileInputStream(from);
    try {
      FileOutputStream out = new FileOutputStream(to, false);
      try {
        ByteStreams.copy(in, out);
        successfulOps++;
        if (comment != null) {
          String commentText = "<!--" + XmlUtils.toXmlTextValue(comment) + "-->";
          byte[] suffix = commentText.getBytes(Charsets.UTF_8);
          out.write(suffix);
        }
      } finally {
        Closeables.close(out, successfulOps < 1);
        successfulOps++;
      }
    } finally {
      Closeables.close(in, successfulOps < 2);
    }
  }

  /**
   * Returns true if the given string ends with the given suffix, using a case-insensitive
   * comparison.
   *
   * @param string the full string to be checked
   * @param suffix the suffix to be checked for
   * @return true if the string case-insensitively ends with the given suffix
   */
  public static boolean endsWithIgnoreCase(@NotNull String string, @NotNull String suffix) {
    return string.regionMatches(
        true /* ignoreCase */, string.length() - suffix.length(), suffix, 0, suffix.length());
  }

  /**
   * Translates an XML name (e.g. xml-name) into a Java / C++ constant name (e.g. XML_NAME)
   *
   * @param xmlName the hyphen separated lower case xml name.
   * @return the equivalent constant name.
   */
  public static String xmlNameToConstantName(String xmlName) {
    return CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, xmlName);
  }

  /**
   * Translates a camel case name (e.g. xmlName) into a Java / C++ constant name (e.g. XML_NAME)
   *
   * @param camelCaseName the camel case name.
   * @return the equivalent constant name.
   */
  public static String camelCaseToConstantName(String camelCaseName) {
    return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, camelCaseName);
  }

  /**
   * Translates a Java / C++ constant name (e.g. XML_NAME) into camel case name (e.g. xmlName)
   *
   * @param constantName the constant name.
   * @return the equivalent camel case name.
   */
  public static String constantNameToCamelCase(String constantName) {
    return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, constantName);
  }

  /**
   * Translates a Java / C++ constant name (e.g. XML_NAME) into a XML case name (e.g. xml-name)
   *
   * @param constantName the constant name.
   * @return the equivalent XML name.
   */
  public static String constantNameToXmlName(String constantName) {
    return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, constantName);
  }

  /**
   * Get the R field name from a resource name, since AAPT will flatten the namespace, turning dots,
   * dashes and colons into _
   *
   * @param resourceName the name to convert
   * @return the corresponding R field name
   */
  @NotNull
  public static String getResourceFieldName(@NotNull String resourceName) {
    // AAPT will flatten the namespace, turning dots, dashes and colons into _
    for (int i = 0, n = resourceName.length(); i < n; i++) {
      char c = resourceName.charAt(i);
      if (c == '.' || c == ':' || c == '-') {
        return resourceName.replace('.', '_').replace('-', '_').replace(':', '_');
      }
    }

    return resourceName;
  }

  /**
   * Returns true if the given file path points to an image file recognized by Android. See
   * http://developer.android.com/guide/appendix/media-formats.html for details.
   *
   * @param path the filename to be tested
   * @return true if the file represents an image file
   */
  public static boolean hasImageExtension(String path) {
    for (String ext : IMAGE_EXTENSIONS) {
      if (endsWithIgnoreCase(path, ext)) {
        return true;
      }
    }
    return false;
  }
}
