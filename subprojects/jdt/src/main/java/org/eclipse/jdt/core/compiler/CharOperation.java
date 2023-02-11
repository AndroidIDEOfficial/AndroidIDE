/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/
/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Luiz-Otavio Zorzella <zorzella at gmail dot com> - Improve CamelCase algorithm
 *     Gabor Kovesdan - Contribution for Bug 350000 - [content assist] Include non-prefix matches in auto-complete suggestions
 *     Stefan Xenos <sxenos@gmail.com> (Google) - Bug 501283 - Lots of hash collisions during indexing
 *******************************************************************************/
package org.eclipse.jdt.core.compiler;

import org.eclipse.jdt.internal.compiler.parser.ScannerHelper;

import java.util.Arrays;
import java.util.List;

/**
 * This class is a collection of helper methods to manipulate char arrays.
 *
 * @since 2.1
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public final class CharOperation {

  /** Constant for an empty char array */
  public static final char[] NO_CHAR = new char[0];

  /** Constant for an empty char array with two dimensions. */
  public static final char[][] NO_CHAR_CHAR = new char[0][];

  /**
   * Constant for an empty String array.
   *
   * @since 3.1
   */
  public static final String[] NO_STRINGS = new String[0];

  /**
   * Constant for all Prefix
   *
   * @since 3.14
   */
  public static final char[] ALL_PREFIX = new char[] {'*'};

  /**
   * Constant for comma
   *
   * @since 3.14
   */
  public static final char[] COMMA_SEPARATOR = new char[] {','};

  private static final int[] EMPTY_REGIONS = new int[0];

  /**
   * Answers a new array with appending the suffix character at the end of the array. <br>
   * <br>
   * For example:<br>
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { 'a', 'b' }
   *    suffix = 'c'
   *    => result = { 'a', 'b' , 'c' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = null
   *    suffix = 'c'
   *    => result = { 'c' }
   * </pre>
   * </ol>
   *
   * @param array the array that is concatenated with the suffix character
   * @param suffix the suffix character
   * @return the new array
   */
  public static final char[] append(char[] array, char suffix) {
    if (array == null) return new char[] {suffix};
    int length = array.length;
    System.arraycopy(array, 0, array = new char[length + 1], 0, length);
    array[length] = suffix;
    return array;
  }

  /**
   * Answers a new array with appending the sub-array at the end of the array. <br>
   * <br>
   * For example:<br>
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { 'a', 'b' }
   *    suffix = { 'c', 'd' }
   *    => result = { 'a', 'b' , 'c' , d' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = null
   *    suffix = { 'c' }
   *    => result = { 'c' }
   * </pre>
   * </ol>
   *
   * @param target the array that is concatenated with the suffix array.
   * @param suffix the array that will be concatenated to the target
   * @return the new array
   * @throws NullPointerException if the target array is null
   * @since 3.11
   */
  public static final char[] append(char[] target, char[] suffix) {
    if (suffix == null || suffix.length == 0) return target;
    int targetLength = target.length;
    int subLength = suffix.length;
    int newTargetLength = targetLength + subLength;
    if (newTargetLength > targetLength) {
      System.arraycopy(target, 0, target = new char[newTargetLength], 0, targetLength);
    }
    System.arraycopy(suffix, 0, target, targetLength, subLength);
    return target;
  }

  /**
   * Append the given sub-array to the target array starting at the given index in the target array.
   * The start of the sub-array is inclusive, the end is exclusive. Answers a new target array if it
   * needs to grow, otherwise answers the same target array. <br>
   * For example:<br>
   *
   * <ol>
   *   <li>
   *       <pre>
   *    target = { 'a', 'b', '0' }
   *    index = 2
   *    array = { 'c', 'd' }
   *    start = 0
   *    end = 1
   *    => result = { 'a', 'b' , 'c' }
   * </pre>
   *   <li>
   *       <pre>
   *    target = { 'a', 'b' }
   *    index = 2
   *    array = { 'c', 'd' }
   *    start = 0
   *    end = 1
   *    => result = { 'a', 'b' , 'c', '0', '0' , '0' } (new array)
   * </pre>
   *   <li>
   *       <pre>
   *    target = { 'a', 'b', 'c' }
   *    index = 1
   *    array = { 'c', 'd', 'e', 'f' }
   *    start = 1
   *    end = 4
   *    => result = { 'a', 'd' , 'e', 'f', '0', '0', '0', '0' } (new array)
   * </pre>
   * </ol>
   *
   * @param target the given target
   * @param index the given index
   * @param array the given array
   * @param start the given start index
   * @param end the given end index
   * @return the new array
   * @throws NullPointerException if the target array is null
   */
  public static final char[] append(char[] target, int index, char[] array, int start, int end) {
    int targetLength = target.length;
    int subLength = end - start;
    int newTargetLength = subLength + index;
    if (newTargetLength > targetLength) {
      System.arraycopy(target, 0, target = new char[newTargetLength * 2], 0, index);
    }
    System.arraycopy(array, start, target, index, subLength);
    return target;
  }

  /**
   * Answers a new array with prepending the prefix character at the start of the array. <br>
   * <br>
   * For example:<br>
   *
   * <ol>
   *   <li>
   *       <pre>
   *    prefix = 'c'
   *    array = { 'a', 'b' }
   *    => result = { 'c' , 'a', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    prefix = 'c'
   *    array = null
   *    => result = { 'c' }
   * </pre>
   * </ol>
   *
   * @param array the array that is concatenated with the prefix character
   * @param prefix the prefix character
   * @return the new array
   * @since 3.14
   */
  public static final char[] prepend(char prefix, char[] array) {
    if (array == null) return new char[] {prefix};
    int length = array.length;
    System.arraycopy(array, 0, array = new char[length + 1], 1, length);
    array[0] = prefix;
    return array;
  }

  /**
   * Answers the concatenation of the two arrays. It answers null if the two arrays are null. If the
   * first array is null, then the second array is returned. If the second array is null, then the
   * first array is returned. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = null
   *    => result = null
   * </pre>
   *   <li>
   *       <pre>
   *    first = { { ' a' } }
   *    second = null
   *    => result = { { ' a' } }
   * </pre>
   *   <li>
   *       <pre>
   *    first = null
   *    second = { { ' a' } }
   *    => result = { { ' a' } }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { { ' b' } }
   *    second = { { ' a' } }
   *    => result = { { ' b' }, { ' a' } }
   * </pre>
   * </ol>
   *
   * @param first the first array to concatenate
   * @param second the second array to concatenate
   * @return the concatenation of the two arrays, or null if the two arrays are null.
   */
  public static final char[][] arrayConcat(char[][] first, char[][] second) {
    if (first == null) return second;
    if (second == null) return first;

    int length1 = first.length;
    int length2 = second.length;
    char[][] result = new char[length1 + length2][];
    System.arraycopy(first, 0, result, 0, length1);
    System.arraycopy(second, 0, result, length1, length2);
    return result;
  }

  /**
   * Answers true if the pattern matches the given name using CamelCase rules, or false otherwise.
   * char[] CamelCase matching does NOT accept explicit wild-cards '*' and '?' and is inherently
   * case sensitive.
   *
   * <p>CamelCase denotes the convention of writing compound names without spaces, and capitalizing
   * every term. This function recognizes both upper and lower CamelCase, depending whether the
   * leading character is capitalized or not. The leading part of an upper CamelCase pattern is
   * assumed to contain a sequence of capitals which are appearing in the matching name; e.g. 'NPE'
   * will match 'NullPointerException', but not 'NewPerfData'. A lower CamelCase pattern uses a
   * lowercase first character. In Java, type names follow the upper CamelCase convention, whereas
   * method or field names follow the lower CamelCase convention.
   *
   * <p>The pattern may contain lowercase characters, which will be matched in a case sensitive way.
   * These characters must appear in sequence in the name. For instance, 'NPExcep' will match
   * 'NullPointerException', but not 'NullPointerExCEPTION' or 'NuPoEx' will match
   * 'NullPointerException', but not 'NoPointerException'.
   *
   * <p>Digit characters are treated in a special way. They can be used in the pattern but are not
   * always considered as leading character. For instance, both 'UTF16DSS' and 'UTFDSS' patterns
   * will match 'UTF16DocumentScannerSupport'.
   *
   * <p>Using this method allows matching names to have more parts than the specified pattern (see
   * {@link #camelCaseMatch(char[], char[], boolean)}).<br>
   * For instance, 'HM' , 'HaMa' and 'HMap' patterns will match 'HashMap', 'HatMapper' <b>and
   * also</b> 'HashMapEntry'.
   *
   * <p>Examples:
   *
   * <ol>
   *   <li>pattern = "NPE".toCharArray() name = "NullPointerException".toCharArray() result => true
   *   <li>pattern = "NPE".toCharArray() name = "NoPermissionException".toCharArray() result => true
   *   <li>pattern = "NuPoEx".toCharArray() name = "NullPointerException".toCharArray() result =>
   *       true
   *   <li>pattern = "NuPoEx".toCharArray() name = "NoPermissionException".toCharArray() result =>
   *       false
   *   <li>pattern = "npe".toCharArray() name = "NullPointerException".toCharArray() result => false
   *   <li>pattern = "IPL3".toCharArray() name = "IPerspectiveListener3".toCharArray() result =>
   *       true
   *   <li>pattern = "HM".toCharArray() name = "HashMapEntry".toCharArray() result => true
   * </ol>
   *
   * @param pattern the given pattern
   * @param name the given name
   * @return true if the pattern matches the given name, false otherwise
   * @since 3.2
   */
  public static final boolean camelCaseMatch(char[] pattern, char[] name) {
    if (pattern == null) return true; // null pattern is equivalent to '*'
    if (name == null) return false; // null name cannot match

    return camelCaseMatch(
        pattern, 0, pattern.length, name, 0, name.length, false /*not the same count of parts*/);
  }

  /**
   * Answers true if a sub-pattern matches the sub-part of the given name using CamelCase rules, or
   * false otherwise. char[] CamelCase matching does NOT accept explicit wild-cards '*' and '?' and
   * is inherently case sensitive. Can match only subset of name/pattern, considering end positions
   * as non-inclusive. The sub-pattern is defined by the patternStart and patternEnd positions.
   *
   * <p>CamelCase denotes the convention of writing compound names without spaces, and capitalizing
   * every term. This function recognizes both upper and lower CamelCase, depending whether the
   * leading character is capitalized or not. The leading part of an upper CamelCase pattern is
   * assumed to contain a sequence of capitals which are appearing in the matching name; e.g. 'NPE'
   * will match 'NullPointerException', but not 'NewPerfData'. A lower CamelCase pattern uses a
   * lowercase first character. In Java, type names follow the upper CamelCase convention, whereas
   * method or field names follow the lower CamelCase convention.
   *
   * <p>The pattern may contain lowercase characters, which will be matched in a case sensitive way.
   * These characters must appear in sequence in the name. For instance, 'NPExcep' will match
   * 'NullPointerException', but not 'NullPointerExCEPTION' or 'NuPoEx' will match
   * 'NullPointerException', but not 'NoPointerException'.
   *
   * <p>Digit characters are treated in a special way. They can be used in the pattern but are not
   * always considered as leading character. For instance, both 'UTF16DSS' and 'UTFDSS' patterns
   * will match 'UTF16DocumentScannerSupport'.
   *
   * <p>CamelCase can be restricted to match only the same count of parts. When this restriction is
   * specified the given pattern and the given name must have <b>exactly</b> the same number of
   * parts (i.e. the same number of uppercase characters).<br>
   * For instance, 'HM' , 'HaMa' and 'HMap' patterns will match 'HashMap' and 'HatMapper' <b>but
   * not</b> 'HashMapEntry'.
   *
   * <p>Examples:
   *
   * <ol>
   *   <li>pattern = "NPE".toCharArray() patternStart = 0 patternEnd = 3 name =
   *       "NullPointerException".toCharArray() nameStart = 0 nameEnd = 20 result => true
   *   <li>pattern = "NPE".toCharArray() patternStart = 0 patternEnd = 3 name =
   *       "NoPermissionException".toCharArray() nameStart = 0 nameEnd = 21 result => true
   *   <li>pattern = "NuPoEx".toCharArray() patternStart = 0 patternEnd = 6 name =
   *       "NullPointerException".toCharArray() nameStart = 0 nameEnd = 20 result => true
   *   <li>pattern = "NuPoEx".toCharArray() patternStart = 0 patternEnd = 6 name =
   *       "NoPermissionException".toCharArray() nameStart = 0 nameEnd = 21 result => false
   *   <li>pattern = "npe".toCharArray() patternStart = 0 patternEnd = 3 name =
   *       "NullPointerException".toCharArray() nameStart = 0 nameEnd = 20 result => false
   *   <li>pattern = "IPL3".toCharArray() patternStart = 0 patternEnd = 4 name =
   *       "IPerspectiveListener3".toCharArray() nameStart = 0 nameEnd = 21 result => true
   *   <li>pattern = "HM".toCharArray() patternStart = 0 patternEnd = 2 name =
   *       "HashMapEntry".toCharArray() nameStart = 0 nameEnd = 12 result => (samePartCount ==
   *       false)
   * </ol>
   *
   * @param pattern the given pattern
   * @param patternStart the start index of the pattern, inclusive
   * @param patternEnd the end index of the pattern, exclusive
   * @param name the given name
   * @param nameStart the start index of the name, inclusive
   * @param nameEnd the end index of the name, exclusive
   * @param samePartCount flag telling whether the pattern and the name should have the same count
   *     of parts or not.<br>
   *     &nbsp;&nbsp;For example:
   *     <ul>
   *       <li>'HM' type string pattern will match 'HashMap' and 'HtmlMapper' types, but not
   *           'HashMapEntry'
   *       <li>'HMap' type string pattern will still match previous 'HashMap' and 'HtmlMapper'
   *           types, but not 'HighMagnitude'
   *     </ul>
   *
   * @return true if a sub-pattern matches the sub-part of the given name, false otherwise
   * @since 3.4
   */
  public static final boolean camelCaseMatch(
      char[] pattern,
      int patternStart,
      int patternEnd,
      char[] name,
      int nameStart,
      int nameEnd,
      boolean samePartCount) {

    /* !!!!!!!!!! WARNING !!!!!!!!!!
     * The algorithm implemented in this method has been heavily used in
     * StringOperation#getCamelCaseMatchingRegions(String, int, int, String, int, int, boolean)
     * method.
     *
     * So, if any change needs to be applied in the current algorithm,
     * do NOT forget to also apply the same change in the StringOperation method!
     */

    if (name == null) return false; // null name cannot match
    if (pattern == null) return true; // null pattern is equivalent to '*'
    if (patternEnd < 0) patternEnd = pattern.length;
    if (nameEnd < 0) nameEnd = name.length;

    if (patternEnd <= patternStart) return nameEnd <= nameStart;
    if (nameEnd <= nameStart) return false;
    // check first pattern char
    if (name[nameStart] != pattern[patternStart]) {
      // first char must strictly match (upper/lower)
      return false;
    }

    char patternChar, nameChar;
    int iPattern = patternStart;
    int iName = nameStart;

    // Main loop is on pattern characters
    while (true) {

      iPattern++;
      iName++;

      if (iPattern == patternEnd) { // we have exhausted pattern...
        // it's a match if the name can have additional parts (i.e. uppercase characters) or
        // is also
        // exhausted
        if (!samePartCount || iName == nameEnd) return true;

        // otherwise it's a match only if the name has no more uppercase characters
        while (true) {
          if (iName == nameEnd) {
            // we have exhausted the name, so it's a match
            return true;
          }
          nameChar = name[iName];
          // test if the name character is uppercase
          if (nameChar < ScannerHelper.MAX_OBVIOUS) {
            if ((ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[nameChar] & ScannerHelper.C_UPPER_LETTER)
                != 0) {
              return false;
            }
          } else if (!Character.isJavaIdentifierPart(nameChar) || Character.isUpperCase(nameChar)) {
            return false;
          }
          iName++;
        }
      }

      if (iName == nameEnd) {
        // We have exhausted the name (and not the pattern), so it's not a match
        return false;
      }

      // For as long as we're exactly matching, bring it on (even if it's a lower case
      // character)
      if ((patternChar = pattern[iPattern]) == name[iName]) {
        continue;
      }

      // If characters are not equals, then it's not a match if patternChar is lowercase
      if (patternChar < ScannerHelper.MAX_OBVIOUS) {
        if ((ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[patternChar]
                & (ScannerHelper.C_UPPER_LETTER | ScannerHelper.C_DIGIT))
            == 0) {
          return false;
        }
      } else if (Character.isJavaIdentifierPart(patternChar)
          && !Character.isUpperCase(patternChar)
          && !Character.isDigit(patternChar)) {
        return false;
      }

      // patternChar is uppercase, so let's find the next uppercase in name
      while (true) {
        if (iName == nameEnd) {
          //	We have exhausted name (and not pattern), so it's not a match
          return false;
        }

        nameChar = name[iName];
        if (nameChar < ScannerHelper.MAX_OBVIOUS) {
          int charNature = ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[nameChar];
          if ((charNature & (ScannerHelper.C_LOWER_LETTER | ScannerHelper.C_SPECIAL)) != 0) {
            // nameChar is lowercase
            iName++;
          } else if ((charNature & ScannerHelper.C_DIGIT) != 0) {
            // nameChar is digit => break if the digit is current pattern character
            // otherwise
            // consume it
            if (patternChar == nameChar) break;
            iName++;
            // nameChar is uppercase...
          } else if (patternChar != nameChar) {
            // .. and it does not match patternChar, so it's not a match
            return false;
          } else {
            // .. and it matched patternChar. Back to the big loop
            break;
          }
        }
        // Same tests for non-obvious characters
        else if (Character.isJavaIdentifierPart(nameChar) && !Character.isUpperCase(nameChar)) {
          iName++;
        } else if (Character.isDigit(nameChar)) {
          if (patternChar == nameChar) break;
          iName++;
        } else if (patternChar != nameChar) {
          return false;
        } else {
          break;
        }
      }
      // At this point, either name has been exhausted, or it is at an uppercase letter.
      // Since pattern is also at an uppercase letter
    }
  }

  /**
   * Answers true if the pattern matches the given name using CamelCase rules, or false otherwise.
   * char[] CamelCase matching does NOT accept explicit wild-cards '*' and '?' and is inherently
   * case sensitive.
   *
   * <p>CamelCase denotes the convention of writing compound names without spaces, and capitalizing
   * every term. This function recognizes both upper and lower CamelCase, depending whether the
   * leading character is capitalized or not. The leading part of an upper CamelCase pattern is
   * assumed to contain a sequence of capitals which are appearing in the matching name; e.g. 'NPE'
   * will match 'NullPointerException', but not 'NewPerfData'. A lower CamelCase pattern uses a
   * lowercase first character. In Java, type names follow the upper CamelCase convention, whereas
   * method or field names follow the lower CamelCase convention.
   *
   * <p>The pattern may contain lowercase characters, which will be matched in a case sensitive way.
   * These characters must appear in sequence in the name. For instance, 'NPExcep' will match
   * 'NullPointerException', but not 'NullPointerExCEPTION' or 'NuPoEx' will match
   * 'NullPointerException', but not 'NoPointerException'.
   *
   * <p>Digit characters are treated in a special way. They can be used in the pattern but are not
   * always considered as leading character. For instance, both 'UTF16DSS' and 'UTFDSS' patterns
   * will match 'UTF16DocumentScannerSupport'.
   *
   * <p>CamelCase can be restricted to match only the same count of parts. When this restriction is
   * specified the given pattern and the given name must have <b>exactly</b> the same number of
   * parts (i.e. the same number of uppercase characters).<br>
   * For instance, 'HM' , 'HaMa' and 'HMap' patterns will match 'HashMap' and 'HatMapper' <b>but
   * not</b> 'HashMapEntry'.
   *
   * <p>Examples:
   *
   * <ol>
   *   <li>pattern = "NPE".toCharArray() name = "NullPointerException".toCharArray() result => true
   *   <li>pattern = "NPE".toCharArray() name = "NoPermissionException".toCharArray() result => true
   *   <li>pattern = "NuPoEx".toCharArray() name = "NullPointerException".toCharArray() result =>
   *       true
   *   <li>pattern = "NuPoEx".toCharArray() name = "NoPermissionException".toCharArray() result =>
   *       false
   *   <li>pattern = "npe".toCharArray() name = "NullPointerException".toCharArray() result => false
   *   <li>pattern = "IPL3".toCharArray() name = "IPerspectiveListener3".toCharArray() result =>
   *       true
   *   <li>pattern = "HM".toCharArray() name = "HashMapEntry".toCharArray() result => (samePartCount
   *       == false)
   * </ol>
   *
   * @param pattern the given pattern
   * @param name the given name
   * @param samePartCount flag telling whether the pattern and the name should have the same count
   *     of parts or not.<br>
   *     &nbsp;&nbsp;For example:
   *     <ul>
   *       <li>'HM' type string pattern will match 'HashMap' and 'HtmlMapper' types, but not
   *           'HashMapEntry'
   *       <li>'HMap' type string pattern will still match previous 'HashMap' and 'HtmlMapper'
   *           types, but not 'HighMagnitude'
   *     </ul>
   *
   * @return true if the pattern matches the given name, false otherwise
   * @since 3.4
   */
  public static final boolean camelCaseMatch(char[] pattern, char[] name, boolean samePartCount) {
    if (pattern == null) return true; // null pattern is equivalent to '*'
    if (name == null) return false; // null name cannot match

    return camelCaseMatch(pattern, 0, pattern.length, name, 0, name.length, samePartCount);
  }

  /**
   * Answers true if a sub-pattern matches the sub-part of the given name using CamelCase rules, or
   * false otherwise. char[] CamelCase matching does NOT accept explicit wild-cards '*' and '?' and
   * is inherently case sensitive. Can match only subset of name/pattern, considering end positions
   * as non-inclusive. The sub-pattern is defined by the patternStart and patternEnd positions.
   *
   * <p>CamelCase denotes the convention of writing compound names without spaces, and capitalizing
   * every term. This function recognizes both upper and lower CamelCase, depending whether the
   * leading character is capitalized or not. The leading part of an upper CamelCase pattern is
   * assumed to contain a sequence of capitals which are appearing in the matching name; e.g. 'NPE'
   * will match 'NullPointerException', but not 'NewPerfData'. A lower CamelCase pattern uses a
   * lowercase first character. In Java, type names follow the upper CamelCase convention, whereas
   * method or field names follow the lower CamelCase convention.
   *
   * <p>The pattern may contain lowercase characters, which will be matched in a case sensitive way.
   * These characters must appear in sequence in the name. For instance, 'NPExcep' will match
   * 'NullPointerException', but not 'NullPointerExCEPTION' or 'NuPoEx' will match
   * 'NullPointerException', but not 'NoPointerException'.
   *
   * <p>Digit characters are treated in a special way. They can be used in the pattern but are not
   * always considered as leading character. For instance, both 'UTF16DSS' and 'UTFDSS' patterns
   * will match 'UTF16DocumentScannerSupport'.
   *
   * <p>Digit characters are treated in a special way. They can be used in the pattern but are not
   * always considered as leading character. For instance, both 'UTF16DSS' and 'UTFDSS' patterns
   * will match 'UTF16DocumentScannerSupport'.
   *
   * <p>Using this method allows matching names to have more parts than the specified pattern (see
   * {@link #camelCaseMatch(char[], int, int, char[], int, int, boolean)}).<br>
   * For instance, 'HM' , 'HaMa' and 'HMap' patterns will match 'HashMap', 'HatMapper' <b>and
   * also</b> 'HashMapEntry'.
   *
   * <p>Examples:
   *
   * <ol>
   *   <li>pattern = "NPE".toCharArray() patternStart = 0 patternEnd = 3 name =
   *       "NullPointerException".toCharArray() nameStart = 0 nameEnd = 20 result => true
   *   <li>pattern = "NPE".toCharArray() patternStart = 0 patternEnd = 3 name =
   *       "NoPermissionException".toCharArray() nameStart = 0 nameEnd = 21 result => true
   *   <li>pattern = "NuPoEx".toCharArray() patternStart = 0 patternEnd = 6 name =
   *       "NullPointerException".toCharArray() nameStart = 0 nameEnd = 20 result => true
   *   <li>pattern = "NuPoEx".toCharArray() patternStart = 0 patternEnd = 6 name =
   *       "NoPermissionException".toCharArray() nameStart = 0 nameEnd = 21 result => false
   *   <li>pattern = "npe".toCharArray() patternStart = 0 patternEnd = 3 name =
   *       "NullPointerException".toCharArray() nameStart = 0 nameEnd = 20 result => false
   *   <li>pattern = "IPL3".toCharArray() patternStart = 0 patternEnd = 4 name =
   *       "IPerspectiveListener3".toCharArray() nameStart = 0 nameEnd = 21 result => true
   *   <li>pattern = "HM".toCharArray() patternStart = 0 patternEnd = 2 name =
   *       "HashMapEntry".toCharArray() nameStart = 0 nameEnd = 12 result => true
   * </ol>
   *
   * @param pattern the given pattern
   * @param patternStart the start index of the pattern, inclusive
   * @param patternEnd the end index of the pattern, exclusive
   * @param name the given name
   * @param nameStart the start index of the name, inclusive
   * @param nameEnd the end index of the name, exclusive
   * @return true if a sub-pattern matches the sub-part of the given name, false otherwise
   * @since 3.2
   */
  public static final boolean camelCaseMatch(
      char[] pattern, int patternStart, int patternEnd, char[] name, int nameStart, int nameEnd) {
    return camelCaseMatch(
        pattern,
        patternStart,
        patternEnd,
        name,
        nameStart,
        nameEnd,
        false /*not the same count of parts*/);
  }

  /**
   * Answers true if the characters of the pattern are contained in the name as a substring, in a
   * case-insensitive way.
   *
   * @param pattern the given pattern
   * @param name the given name
   * @return true if the pattern matches the given name, false otherwise
   * @since 3.12
   */
  public static final boolean substringMatch(String pattern, String name) {
    if (pattern == null || pattern.length() == 0) {
      return true;
    }
    if (name == null) {
      return false;
    }
    return checkSubstringMatch(pattern.toCharArray(), name.toCharArray());
  }

  /**
   * Internal substring matching method; called after the null and length checks are performed.
   *
   * @param pattern the given pattern
   * @param name the given name
   * @return true if the pattern matches the given name, false otherwise
   * @see CharOperation#substringMatch(char[], char[])
   */
  private static final boolean checkSubstringMatch(char[] pattern, char[] name) {

    /* XXX: to be revised/enabled

    	// allow non-consecutive occurrence of pattern characters
    	if (pattern.length >= 3) {
    		int pidx = 0;

    		for (int nidx = 0; nidx < name.length; nidx++) {
    			if (Character.toLowerCase(name[nidx]) ==
    					Character.toLowerCase(pattern[pidx]))
    				pidx++;
    			if (pidx == pattern.length)
    				return true;
    		}

    	// for short patterns only allow consecutive occurrence
    	} else {
    */
    // outer loop iterates on the characters of the name; trying to
    // match at any possible position
    outer:
    for (int nidx = 0; nidx < name.length - pattern.length + 1; nidx++) {
      // inner loop iterates on pattern characters
      for (int pidx = 0; pidx < pattern.length; pidx++) {
        if (Character.toLowerCase(name[nidx + pidx]) != Character.toLowerCase(pattern[pidx])) {
          // no match until parameter list; do not match parameter list
          if ((name[nidx + pidx] == '(') || (name[nidx + pidx] == ':')) return false;
          continue outer;
        }
        if (pidx == pattern.length - 1) return true;
      }
    }
    // XXX: }

    return false;
  }

  /**
   * Answers true if the characters of the pattern are contained in the name as a substring, in a
   * case-insensitive way.
   *
   * @param pattern the given pattern
   * @param name the given name
   * @return true if the pattern matches the given name, false otherwise
   * @since 3.12
   */
  public static final boolean substringMatch(char[] pattern, char[] name) {
    if (pattern == null || pattern.length == 0) {
      return true;
    }
    if (name == null) {
      return false;
    }
    return checkSubstringMatch(pattern, name);
  }

  /**
   * Returns the char arrays as an array of Strings
   *
   * @param charArrays the char array to convert
   * @return the char arrays as an array of Strings or null if the given char arrays is null.
   * @since 3.0
   */
  public static String[] charArrayToStringArray(char[][] charArrays) {
    if (charArrays == null) return null;
    int length = charArrays.length;
    if (length == 0) return NO_STRINGS;
    String[] strings = new String[length];
    for (int i = 0; i < length; i++) strings[i] = new String(charArrays[i]);
    return strings;
  }

  /**
   * Returns the char array as a String
   *
   * @param charArray the char array to convert
   * @return the char array as a String or null if the given char array is null.
   * @since 3.0
   */
  public static String charToString(char[] charArray) {
    if (charArray == null) return null;
    return new String(charArray);
  }

  /**
   * Converts the given list of strings to an array of equal size, containing the individual strings
   * converted to char[] each.
   *
   * @param stringList
   * @return an array of char[], representing the elements in the input list, or {@code null} if the
   *     list was {@code null}.
   * @since 3.14
   */
  public static char[][] toCharArrays(List<String> stringList) {
    if (stringList == null) return null;
    char[][] result = new char[stringList.size()][];
    for (int i = 0; i < result.length; i++) result[i] = stringList.get(i).toCharArray();
    return result;
  }
  /**
   * Answers a new array adding the second array at the end of first array. It answers null if the
   * first and second are null. If the first array is null, then a new array char[][] is created
   * with second. If the second array is null, then the first array is returned. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = { 'a' }
   *    => result = { { ' a' } }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { { ' a' } }
   *    second = null
   *    => result = { { ' a' } }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { { ' a' } }
   *    second = { ' b' }
   *    => result = { { ' a' } , { ' b' } }
   * </pre>
   * </ol>
   *
   * @param first the first array to concatenate
   * @param second the array to add at the end of the first array
   * @return a new array adding the second array at the end of first array, or null if the two
   *     arrays are null.
   */
  public static final char[][] arrayConcat(char[][] first, char[] second) {
    if (second == null) return first;
    if (first == null) return new char[][] {second};

    int length = first.length;
    char[][] result = new char[length + 1][];
    System.arraycopy(first, 0, result, 0, length);
    result[length] = second;
    return result;
  }
  /**
   * Compares the two char arrays lexicographically.
   *
   * <p>Returns a negative integer if array1 lexicographically precedes the array2, a positive
   * integer if this array1 lexicographically follows the array2, or zero if both arrays are equal.
   *
   * @param array1 the first given array
   * @param array2 the second given array
   * @return the returned value of the comparison between array1 and array2
   * @throws NullPointerException if one of the arrays is null
   * @since 3.3
   */
  public static final int compareTo(char[] array1, char[] array2) {
    int length1 = array1.length;
    int length2 = array2.length;
    int min = Math.min(length1, length2);
    for (int i = 0; i < min; i++) {
      if (array1[i] != array2[i]) {
        return array1[i] - array2[i];
      }
    }
    return length1 - length2;
  }
  /**
   * Compares the two char arrays lexicographically between the given start and end positions.
   *
   * <p>Returns a negative integer if array1 lexicographically precedes the array2, a positive
   * integer if this array1 lexicographically follows the array2, or zero if both arrays are equal.
   *
   * <p>The comparison is done between start and end positions.
   *
   * @param array1 the first given array
   * @param array2 the second given array
   * @param start the starting position to compare (inclusive)
   * @param end the ending position to compare (exclusive)
   * @return the returned value of the comparison between array1 and array2
   * @throws NullPointerException if one of the arrays is null
   * @since 3.7.1
   */
  public static final int compareTo(char[] array1, char[] array2, int start, int end) {
    int length1 = array1.length;
    int length2 = array2.length;
    int min = Math.min(length1, length2);
    min = Math.min(min, end);
    for (int i = start; i < min; i++) {
      if (array1[i] != array2[i]) {
        return array1[i] - array2[i];
      }
    }
    return length1 - length2;
  }
  /**
   * Compares the contents of the two arrays array and prefix. Returns
   *
   * <ul>
   *   <li>zero if the array starts with the prefix contents
   *   <li>the difference between the first two characters that are not equal
   *   <li>one if array length is lower than the prefix length and that the prefix starts with the
   *       array contents.
   * </ul>
   *
   * <p>For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = null
   *    prefix = null
   *    => result = NullPointerException
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'a', 'b', 'c', 'd', 'e' }
   *    prefix = { 'a', 'b', 'c'}
   *    => result = 0
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'a', 'b', 'c', 'd', 'e' }
   *    prefix = { 'a', 'B', 'c'}
   *    => result = 32
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'd', 'b', 'c', 'd', 'e' }
   *    prefix = { 'a', 'b', 'c'}
   *    => result = 3
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'a', 'b', 'c', 'd', 'e' }
   *    prefix = { 'd', 'b', 'c'}
   *    => result = -3
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'a', 'a', 'c', 'd', 'e' }
   *    prefix = { 'a', 'e', 'c'}
   *    => result = -4
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param prefix the given prefix
   * @return the result of the comparison (>=0 if array>prefix)
   * @throws NullPointerException if either array or prefix is null
   */
  public static final int compareWith(char[] array, char[] prefix) {
    int arrayLength = array.length;
    int prefixLength = prefix.length;
    int min = Math.min(arrayLength, prefixLength);
    int i = 0;
    while (min-- != 0) {
      char c1 = array[i];
      char c2 = prefix[i++];
      if (c1 != c2) return c1 - c2;
    }
    if (prefixLength == i) return 0;
    return -1; // array is shorter than prefix (e.g. array:'ab' < prefix:'abc').
  }

  /**
   * Answers the concatenation of the three arrays. It answers null if the three arrays are null. If
   * first is null, it answers the concatenation of second and third. If second is null, it answers
   * the concatenation of first and third. If third is null, it answers the concatenation of first
   * and second. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = { 'a' }
   *    third = { 'b' }
   *    => result = { ' a', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    second = null
   *    third = { 'b' }
   *    => result = { ' a', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    second = { 'b' }
   *    third = null
   *    => result = { ' a', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = null
   *    second = null
   *    third = null
   *    => result = null
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    second = { 'b' }
   *    third = { 'c' }
   *    => result = { 'a', 'b', 'c' }
   * </pre>
   * </ol>
   *
   * @param first the first array to concatenate
   * @param second the second array to concatenate
   * @param third the third array to concatenate
   * @return the concatenation of the three arrays, or null if the three arrays are null.
   */
  public static final char[] concat(char[] first, char[] second, char[] third) {
    if (first == null) return concat(second, third);
    if (second == null) return concat(first, third);
    if (third == null) return concat(first, second);

    int length1 = first.length;
    int length2 = second.length;
    int length3 = third.length;
    char[] result = new char[length1 + length2 + length3];
    System.arraycopy(first, 0, result, 0, length1);
    System.arraycopy(second, 0, result, length1, length2);
    System.arraycopy(third, 0, result, length1 + length2, length3);
    return result;
  }

  /**
   * Answers the concatenation of the two arrays. It answers null if the two arrays are null. If the
   * first array is null, then the second array is returned. If the second array is null, then the
   * first array is returned. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = { 'a' }
   *    => result = { ' a' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { ' a' }
   *    second = null
   *    => result = { ' a' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { ' a' }
   *    second = { ' b' }
   *    => result = { ' a' , ' b' }
   * </pre>
   * </ol>
   *
   * @param first the first array to concatenate
   * @param second the second array to concatenate
   * @return the concatenation of the two arrays, or null if the two arrays are null.
   */
  public static final char[] concat(char[] first, char[] second) {
    if (first == null) return second;
    if (second == null) return first;

    int length1 = first.length;
    int length2 = second.length;
    char[] result = new char[length1 + length2];
    System.arraycopy(first, 0, result, 0, length1);
    System.arraycopy(second, 0, result, length1, length2);
    return result;
  }

  /**
   * Answers the concatenation of the two arrays inserting the separator character between the two
   * arrays. Differs from {@link CharOperation#contains(char, char[])} in case second array is a
   * zero length array. It answers null if the two arrays are null. If the first array is null, then
   * the second array is returned. If the second array is null, then the first array is returned. if
   * the second array is zero length array, the separator is appended. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = { 'a' }
   *    separator = '/'
   *    => result = { ' a' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { ' a' }
   *    second = null
   *    separator = '/'
   *    => result = { ' a' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { ' a' }
   *    second = { ' b' }
   *    separator = '/'
   *    => result = { ' a' , '/', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { ' a' }
   *    second = { '' }
   *    separator = '.'
   *    => result = { ' a' , '.', }
   * </pre>
   * </ol>
   *
   * @param first the first array to concatenate
   * @param second the second array to concatenate
   * @param separator the character to insert
   * @return the concatenation of the two arrays inserting the separator character between the two
   *     arrays , or null if the two arrays are null. If second array is of zero length, the
   *     separator is appended to the first array and returned.
   * @since 3.14
   */
  public static final char[] concatAll(char[] first, char[] second, char separator) {
    if (first == null) return second;
    if (second == null) return first;

    int length1 = first.length;
    if (length1 == 0) return second;
    int length2 = second.length;

    char[] result = new char[length1 + length2 + 1];
    System.arraycopy(first, 0, result, 0, length1);
    result[length1] = separator;
    if (length2 > 0) System.arraycopy(second, 0, result, length1 + 1, length2);
    return result;
  }

  /**
   * Answers the concatenation of the three arrays inserting the sep1 character between the first
   * two arrays and sep2 between the last two. It answers null if the three arrays are null. If the
   * first array is null or empty, then it answers the concatenation of second and third inserting
   * the sep2 character between them. If the second array is null or empty, then it answers the
   * concatenation of first and third inserting the sep1 character between them. If the third array
   * is null or empty, then it answers the concatenation of first and second inserting the sep1
   * character between them. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    sep1 = '/'
   *    second = { 'a' }
   *    sep2 = ':'
   *    third = { 'b' }
   *    => result = { ' a' , ':', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    sep1 = '/'
   *    second = null
   *    sep2 = ':'
   *    third = { 'b' }
   *    => result = { ' a' , '/', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    sep1 = '/'
   *    second = { 'b' }
   *    sep2 = ':'
   *    third = null
   *    => result = { ' a' , '/', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    sep1 = '/'
   *    second = { 'b' }
   *    sep2 = ':'
   *    third = { 'c' }
   *    => result = { ' a' , '/', 'b' , ':', 'c' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    sep1 = '/'
   *    second = { }
   *    sep2 = ':'
   *    third = { 'c' }
   *    => result = { ' a', ':', 'c' }
   * </pre>
   * </ol>
   *
   * @param first the first array to concatenate
   * @param sep1 the character to insert
   * @param second the second array to concatenate
   * @param sep2 the character to insert
   * @param third the second array to concatenate
   * @return the concatenation of the three arrays inserting the sep1 character between the two
   *     arrays and sep2 between the last two.
   * @since 3.12
   */
  public static final char[] concatNonEmpty(
      char[] first, char sep1, char[] second, char sep2, char[] third) {
    if (first == null || first.length == 0) return concatNonEmpty(second, third, sep2);
    if (second == null || second.length == 0) return concatNonEmpty(first, third, sep1);
    if (third == null || third.length == 0) return concatNonEmpty(first, second, sep1);

    return concat(first, sep1, second, sep2, third);
  }

  /**
   * Answers the concatenation of the three arrays inserting the sep1 character between the first
   * two arrays and sep2 between the last two. It answers null if the three arrays are null. If the
   * first array is null, then it answers the concatenation of second and third inserting the sep2
   * character between them. If the second array is null, then it answers the concatenation of first
   * and third inserting the sep1 character between them. If the third array is null, then it
   * answers the concatenation of first and second inserting the sep1 character between them. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    sep1 = '/'
   *    second = { 'a' }
   *    sep2 = ':'
   *    third = { 'b' }
   *    => result = { ' a' , ':', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    sep1 = '/'
   *    second = null
   *    sep2 = ':'
   *    third = { 'b' }
   *    => result = { ' a' , '/', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    sep1 = '/'
   *    second = { 'b' }
   *    sep2 = ':'
   *    third = null
   *    => result = { ' a' , '/', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    sep1 = '/'
   *    second = { 'b' }
   *    sep2 = ':'
   *    third = { 'c' }
   *    => result = { ' a' , '/', 'b' , ':', 'c' }
   * </pre>
   * </ol>
   *
   * @param first the first array to concatenate
   * @param sep1 the character to insert
   * @param second the second array to concatenate
   * @param sep2 the character to insert
   * @param third the second array to concatenate
   * @return the concatenation of the three arrays inserting the sep1 character between the two
   *     arrays and sep2 between the last two.
   */
  public static final char[] concat(
      char[] first, char sep1, char[] second, char sep2, char[] third) {
    if (first == null) return concat(second, third, sep2);
    if (second == null) return concat(first, third, sep1);
    if (third == null) return concat(first, second, sep1);

    int length1 = first.length;
    int length2 = second.length;
    int length3 = third.length;
    char[] result = new char[length1 + length2 + length3 + 2];
    System.arraycopy(first, 0, result, 0, length1);
    result[length1] = sep1;
    System.arraycopy(second, 0, result, length1 + 1, length2);
    result[length1 + length2 + 1] = sep2;
    System.arraycopy(third, 0, result, length1 + length2 + 2, length3);
    return result;
  }
  /**
   * Answers the concatenation of the two arrays inserting the separator character between the two
   * arrays. It answers null if the two arrays are null. If the first array is null or is empty,
   * then the second array is returned. If the second array is null or is empty, then the first
   * array is returned. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = { 'a' }
   *    separator = '/'
   *    => result = { ' a' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { ' a' }
   *    second = null
   *    separator = '/'
   *    => result = { ' a' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { ' a' }
   *    second = { ' b' }
   *    separator = '/'
   *    => result = { ' a' , '/', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { ' a' }
   *    second = {  }
   *    separator = '/'
   *    => result = { ' a'}
   * </pre>
   * </ol>
   *
   * @param first the first array to concatenate
   * @param second the second array to concatenate
   * @param separator the character to insert
   * @return the concatenation of the two arrays inserting the separator character between the two
   *     arrays , or null if the two arrays are null.
   * @since 3.12
   */
  public static final char[] concatNonEmpty(char[] first, char[] second, char separator) {
    if (first == null || first.length == 0) return second;
    if (second == null || second.length == 0) return first;
    return concat(first, second, separator);
  }

  /**
   * Answers the concatenation of the two arrays inserting the separator character between the two
   * arrays. It answers null if the two arrays are null. If the first array is null, then the second
   * array is returned. If the second array is null, then the first array is returned. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = { 'a' }
   *    separator = '/'
   *    => result = { ' a' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { ' a' }
   *    second = null
   *    separator = '/'
   *    => result = { ' a' }
   * </pre>
   *   <li>
   *       <pre>
   *    first = { ' a' }
   *    second = { ' b' }
   *    separator = '/'
   *    => result = { ' a' , '/', 'b' }
   * </pre>
   * </ol>
   *
   * @param first the first array to concatenate
   * @param second the second array to concatenate
   * @param separator the character to insert
   * @return the concatenation of the two arrays inserting the separator character between the two
   *     arrays , or null if the two arrays are null.
   */
  public static final char[] concat(char[] first, char[] second, char separator) {
    if (first == null) return second;
    if (second == null) return first;

    int length1 = first.length;
    if (length1 == 0) return second;
    int length2 = second.length;
    if (length2 == 0) return first;

    char[] result = new char[length1 + length2 + 1];
    System.arraycopy(first, 0, result, 0, length1);
    result[length1] = separator;
    System.arraycopy(second, 0, result, length1 + 1, length2);
    return result;
  }

  /**
   * Answers a new array with prepending the prefix character and appending the suffix character at
   * the end of the array. If array is null, it answers a new array containing the prefix and the
   * suffix characters. <br>
   * <br>
   * For example:<br>
   *
   * <ol>
   *   <li>
   *       <pre>
   *    prefix = 'a'
   *    array = { 'b' }
   *    suffix = 'c'
   *    => result = { 'a', 'b' , 'c' }
   * </pre>
   *   <li>
   *       <pre>
   *    prefix = 'a'
   *    array = null
   *    suffix = 'c'
   *    => result = { 'a', 'c' }
   * </pre>
   * </ol>
   *
   * @param prefix the prefix character
   * @param array the array that is concatenated with the prefix and suffix characters
   * @param suffix the suffix character
   * @return the new array
   */
  public static final char[] concat(char prefix, char[] array, char suffix) {
    if (array == null) return new char[] {prefix, suffix};

    int length = array.length;
    char[] result = new char[length + 2];
    result[0] = prefix;
    System.arraycopy(array, 0, result, 1, length);
    result[length + 1] = suffix;
    return result;
  }

  /**
   * Answers the concatenation of the given array parts using the given separator between each part
   * and prepending the given name at the beginning. <br>
   * <br>
   * For example:<br>
   *
   * <ol>
   *   <li>
   *       <pre>
   *    name = { 'c' }
   *    array = { { 'a' }, { 'b' } }
   *    separator = '.'
   *    => result = { 'a', '.', 'b' , '.', 'c' }
   * </pre>
   *   <li>
   *       <pre>
   *    name = null
   *    array = { { 'a' }, { 'b' } }
   *    separator = '.'
   *    => result = { 'a', '.', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    name = { ' c' }
   *    array = null
   *    separator = '.'
   *    => result = { 'c' }
   * </pre>
   * </ol>
   *
   * @param name the given name
   * @param array the given array
   * @param separator the given separator
   * @return the concatenation of the given array parts using the given separator between each part
   *     and prepending the given name at the beginning
   */
  public static final char[] concatWith(char[] name, char[][] array, char separator) {
    int nameLength = name == null ? 0 : name.length;
    if (nameLength == 0) return concatWith(array, separator);

    int length = array == null ? 0 : array.length;
    if (length == 0) return name;

    int size = nameLength;
    int index = length;
    while (--index >= 0) if (array[index].length > 0) size += array[index].length + 1;
    char[] result = new char[size];
    index = size;
    for (int i = length - 1; i >= 0; i--) {
      int subLength = array[i].length;
      if (subLength > 0) {
        index -= subLength;
        System.arraycopy(array[i], 0, result, index, subLength);
        result[--index] = separator;
      }
    }
    System.arraycopy(name, 0, result, 0, nameLength);
    return result;
  }

  /**
   * Answers the concatenation of the given array parts using the given separator between each part.
   * <br>
   * <br>
   * For example:<br>
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { { 'a' }, { 'b' } }
   *    separator = '.'
   *    => result = { 'a', '.', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = null
   *    separator = '.'
   *    => result = { }
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param separator the given separator
   * @return the concatenation of the given array parts using the given separator between each part
   */
  public static final char[] concatWith(char[][] array, char separator) {
    int length = array == null ? 0 : array.length;
    if (length == 0) return CharOperation.NO_CHAR;

    int size = length - 1;
    int index = length;
    while (--index >= 0) {
      if (array[index].length == 0) size--;
      else size += array[index].length;
    }
    if (size <= 0) return CharOperation.NO_CHAR;
    char[] result = new char[size];
    index = length;
    while (--index >= 0) {
      length = array[index].length;
      if (length > 0) {
        System.arraycopy(array[index], 0, result, (size -= length), length);
        if (--size >= 0) result[size] = separator;
      }
    }
    return result;
  }

  /**
   * Answers the concatenation of the given array parts using the given separator between each part
   * and appending the given name at the end. <br>
   * <br>
   * For example:<br>
   *
   * <ol>
   *   <li>
   *       <pre>
   *    name = { 'c' }
   *    array = { { 'a' }, { 'b' } }
   *    separator = '.'
   *    => result = { 'a', '.', 'b' , '.', 'c' }
   * </pre>
   *   <li>
   *       <pre>
   *    name = null
   *    array = { { 'a' }, { 'b' } }
   *    separator = '.'
   *    => result = { 'a', '.', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    name = { ' c' }
   *    array = null
   *    separator = '.'
   *    => result = { 'c' }
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param name the given name
   * @param separator the given separator
   * @return the concatenation of the given array parts using the given separator between each part
   *     and appending the given name at the end
   */
  public static final char[] concatWith(char[][] array, char[] name, char separator) {
    int nameLength = name == null ? 0 : name.length;
    if (nameLength == 0) return concatWith(array, separator);

    int length = array == null ? 0 : array.length;
    if (length == 0) return name;

    int size = nameLength;
    int index = length;
    while (--index >= 0) if (array[index].length > 0) size += array[index].length + 1;
    char[] result = new char[size];
    index = 0;
    for (int i = 0; i < length; i++) {
      int subLength = array[i].length;
      if (subLength > 0) {
        System.arraycopy(array[i], 0, result, index, subLength);
        index += subLength;
        result[index++] = separator;
      }
    }
    System.arraycopy(name, 0, result, index, nameLength);
    return result;
  }

  /**
   * Answers the concatenation of the given array parts using the given separator between each part
   * irrespective of whether an element is a zero length array or not. <br>
   * <br>
   * For example:<br>
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { { 'a' }, {}, { 'b' } }
   *    separator = ''
   *    => result = { 'a', '/', '/', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = { { 'a' }, { 'b' } }
   *    separator = '.'
   *    => result = { 'a', '.', 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = null
   *    separator = '.'
   *    => result = { }
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param separator the given separator
   * @return the concatenation of the given array parts using the given separator between each part
   * @since 3.12
   */
  public static final char[] concatWithAll(char[][] array, char separator) {
    int length = array == null ? 0 : array.length;
    if (length == 0) return CharOperation.NO_CHAR;

    int size = length - 1;
    int index = length;
    while (--index >= 0) {
      size += array[index].length;
    }
    char[] result = new char[size];
    index = length;
    while (--index >= 0) {
      length = array[index].length;
      if (length > 0) {
        System.arraycopy(array[index], 0, result, (size -= length), length);
      }
      if (--size >= 0) result[size] = separator;
    }
    return result;
  }

  /**
   * Answers true if the array contains an occurrence of character, false otherwise. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    character = 'c'
   *    array = { { ' a' }, { ' b' } }
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    character = 'a'
   *    array = { { ' a' }, { ' b' } }
   *    result => true
   * </pre>
   * </ol>
   *
   * @param character the character to search
   * @param array the array in which the search is done
   * @return true if the array contains an occurrence of character, false otherwise.
   * @throws NullPointerException if array is null.
   */
  public static final boolean contains(char character, char[][] array) {
    for (int i = array.length; --i >= 0; ) {
      char[] subarray = array[i];
      for (int j = subarray.length; --j >= 0; ) if (subarray[j] == character) return true;
    }
    return false;
  }

  /**
   * Answers true if the array contains an occurrence of character, false otherwise. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    character = 'c'
   *    array = { ' b'  }
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    character = 'a'
   *    array = { ' a' , ' b' }
   *    result => true
   * </pre>
   * </ol>
   *
   * @param character the character to search
   * @param array the array in which the search is done
   * @return true if the array contains an occurrence of character, false otherwise.
   * @throws NullPointerException if array is null.
   */
  public static final boolean contains(char character, char[] array) {
    for (int i = array.length; --i >= 0; ) if (array[i] == character) return true;
    return false;
  }

  /**
   * Answers true if the array contains an occurrence of one of the characters, false otherwise.
   * <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    characters = { 'c', 'd' }
   *    array = { 'a', ' b'  }
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    characters = { 'c', 'd' }
   *    array = { 'a', ' b', 'c'  }
   *    result => true
   * </pre>
   * </ol>
   *
   * @param characters the characters to search
   * @param array the array in which the search is done
   * @return true if the array contains an occurrence of one of the characters, false otherwise.
   * @throws NullPointerException if array is null.
   * @since 3.1
   */
  public static final boolean contains(char[] characters, char[] array) {
    for (int i = array.length; --i >= 0; )
      for (int j = characters.length; --j >= 0; ) if (array[i] == characters[j]) return true;
    return false;
  }

  /**
   * Does the given array contain a char sequence that is equal to the give sequence?
   *
   * @param array
   * @param sequence
   * @return true if sequence is equal to an element in array
   * @since 3.14
   */
  public static boolean containsEqual(char[][] array, char[] sequence) {
    for (int i = 0; i < array.length; i++) {
      if (equals(array[i], sequence)) return true;
    }
    return false;
  }

  /**
   * Answers true if the two arrays are identical character by character, otherwise false. The
   * equality is case sensitive. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = null
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    first = { }
   *    second = null
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    second = { 'a' }
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    second = { 'A' }
   *    result => false
   * </pre>
   * </ol>
   *
   * @param first the first array
   * @param second the second array
   * @return true if the two arrays are identical character by character, otherwise false
   */
  public static final boolean equals(char[] first, char[] second) {
    return Arrays.equals(first, second);
  }

  /**
   * Answers a deep copy of the toCopy array.
   *
   * @param toCopy the array to copy
   * @return a deep copy of the toCopy array.
   */
  public static final char[][] deepCopy(char[][] toCopy) {
    int toCopyLength = toCopy.length;
    char[][] result = new char[toCopyLength][];
    for (int i = 0; i < toCopyLength; i++) {
      char[] toElement = toCopy[i];
      int toElementLength = toElement.length;
      char[] resultElement = new char[toElementLength];
      System.arraycopy(toElement, 0, resultElement, 0, toElementLength);
      result[i] = resultElement;
    }
    return result;
  }

  /**
   * Return true if array ends with the sequence of characters contained in toBeFound, otherwise
   * false. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { 'a', 'b', 'c', 'd' }
   *    toBeFound = { 'b', 'c' }
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'a', 'b', 'c' }
   *    toBeFound = { 'b', 'c' }
   *    result => true
   * </pre>
   * </ol>
   *
   * @param array the array to check
   * @param toBeFound the array to find
   * @return true if array ends with the sequence of characters contained in toBeFound, otherwise
   *     false.
   * @throws NullPointerException if array is null or toBeFound is null
   */
  public static final boolean endsWith(char[] array, char[] toBeFound) {
    int i = toBeFound.length;
    int j = array.length - i;

    if (j < 0) return false;
    while (--i >= 0) if (toBeFound[i] != array[i + j]) return false;
    return true;
  }

  /**
   * If isCaseSensite is true, answers true if the two arrays are identical character by character,
   * otherwise false. If it is false, answers true if the two arrays are identical character by
   * character without checking the case, otherwise false. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = null
   *    isCaseSensitive = true
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    first = { { } }
   *    second = null
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    first = { { 'A' } }
   *    second = { { 'a' } }
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    first = { { 'A' } }
   *    second = { { 'a' } }
   *    isCaseSensitive = false
   *    result => true
   * </pre>
   * </ol>
   *
   * @param first the first array
   * @param second the second array
   * @param isCaseSensitive check whether or not the equality should be case sensitive
   * @return true if the two arrays are identical character by character according to the value of
   *     isCaseSensitive, otherwise false
   */
  public static final boolean equals(char[][] first, char[][] second, boolean isCaseSensitive) {

    if (isCaseSensitive) {
      return equals(first, second);
    }
    if (first == second) return true;
    if (first == null || second == null) return false;
    if (first.length != second.length) return false;

    for (int i = first.length; --i >= 0; ) if (!equals(first[i], second[i], false)) return false;
    return true;
  }

  /**
   * Answers true if the two arrays are identical character by character, otherwise false. The
   * equality is case sensitive. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = null
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    first = { { } }
   *    second = null
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    first = { { 'a' } }
   *    second = { { 'a' } }
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    first = { { 'A' } }
   *    second = { { 'a' } }
   *    result => false
   * </pre>
   * </ol>
   *
   * @param first the first array
   * @param second the second array
   * @return true if the two arrays are identical character by character, otherwise false
   */
  public static final boolean equals(char[][] first, char[][] second) {
    if (first == second) return true;
    if (first == null || second == null) return false;
    if (first.length != second.length) return false;

    for (int i = first.length; --i >= 0; ) if (!equals(first[i], second[i])) return false;
    return true;
  }

  /**
   * If isCaseSensite is true, answers true if the two arrays are identical character by character,
   * otherwise false. If it is false, answers true if the two arrays are identical character by
   * character without checking the case, otherwise false. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = null
   *    isCaseSensitive = true
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    first = { }
   *    second = null
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'A' }
   *    second = { 'a' }
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'A' }
   *    second = { 'a' }
   *    isCaseSensitive = false
   *    result => true
   * </pre>
   * </ol>
   *
   * @param first the first array
   * @param second the second array
   * @param isCaseSensitive check whether or not the equality should be case sensitive
   * @return true if the two arrays are identical character by character according to the value of
   *     isCaseSensitive, otherwise false
   */
  public static final boolean equals(char[] first, char[] second, boolean isCaseSensitive) {

    if (isCaseSensitive) {
      return equals(first, second);
    }
    if (first == second) return true;
    if (first == null || second == null) return false;
    if (first.length != second.length) return false;

    for (int i = first.length; --i >= 0; )
      if (ScannerHelper.toLowerCase(first[i]) != ScannerHelper.toLowerCase(second[i])) return false;
    return true;
  }

  /**
   * Answers true if the first array is identical character by character to a portion of the second
   * array delimited from position secondStart (inclusive) to secondEnd(exclusive), otherwise false.
   * The equality is case sensitive. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = null
   *    secondStart = 0
   *    secondEnd = 0
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    first = { }
   *    second = null
   *    secondStart = 0
   *    secondEnd = 0
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    second = { 'a' }
   *    secondStart = 0
   *    secondEnd = 1
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    second = { 'A' }
   *    secondStart = 0
   *    secondEnd = 1
   *    result => false
   * </pre>
   * </ol>
   *
   * @param first the first array
   * @param second the second array
   * @param secondStart inclusive start position in the second array to compare
   * @param secondEnd exclusive end position in the second array to compare
   * @return true if the first array is identical character by character to fragment of second array
   *     ranging from secondStart to secondEnd-1, otherwise false
   * @since 3.0
   */
  public static final boolean equals(char[] first, char[] second, int secondStart, int secondEnd) {
    return equals(first, second, secondStart, secondEnd, true);
  }

  /**
   * Answers true if the first array is identical character by character to a portion of the second
   * array delimited from position secondStart (inclusive) to secondEnd(exclusive), otherwise false.
   * The equality could be either case sensitive or case insensitive according to the value of the
   * <code>isCaseSensitive</code> parameter.
   *
   * <p>For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    first = null
   *    second = null
   *    secondStart = 0
   *    secondEnd = 0
   *    isCaseSensitive = false
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    first = { }
   *    second = null
   *    secondStart = 0
   *    secondEnd = 0
   *    isCaseSensitive = false
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    second = { 'a' }
   *    secondStart = 0
   *    secondEnd = 1
   *    isCaseSensitive = true
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    second = { 'A' }
   *    secondStart = 0
   *    secondEnd = 1
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    first = { 'a' }
   *    second = { 'A' }
   *    secondStart = 0
   *    secondEnd = 1
   *    isCaseSensitive = false
   *    result => true
   * </pre>
   * </ol>
   *
   * @param first the first array
   * @param second the second array
   * @param secondStart inclusive start position in the second array to compare
   * @param secondEnd exclusive end position in the second array to compare
   * @param isCaseSensitive check whether or not the equality should be case sensitive
   * @return true if the first array is identical character by character to fragment of second array
   *     ranging from secondStart to secondEnd-1, otherwise false
   * @since 3.2
   */
  public static final boolean equals(
      char[] first, char[] second, int secondStart, int secondEnd, boolean isCaseSensitive) {
    if (first == second) return true;
    if (first == null || second == null) return false;
    if (first.length != secondEnd - secondStart) return false;
    if (isCaseSensitive) {
      for (int i = first.length; --i >= 0; ) if (first[i] != second[i + secondStart]) return false;
    } else {
      for (int i = first.length; --i >= 0; )
        if (ScannerHelper.toLowerCase(first[i])
            != ScannerHelper.toLowerCase(second[i + secondStart])) return false;
    }
    return true;
  }

  /**
   * If isCaseSensite is true, the equality is case sensitive, otherwise it is case insensitive.
   *
   * <p>Answers true if the name contains the fragment at the starting index startIndex, otherwise
   * false. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    fragment = { 'b', 'c' , 'd' }
   *    name = { 'a', 'b', 'c' , 'd' }
   *    startIndex = 1
   *    isCaseSensitive = true
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    fragment = { 'b', 'c' , 'd' }
   *    name = { 'a', 'b', 'C' , 'd' }
   *    startIndex = 1
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    fragment = { 'b', 'c' , 'd' }
   *    name = { 'a', 'b', 'C' , 'd' }
   *    startIndex = 0
   *    isCaseSensitive = false
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    fragment = { 'b', 'c' , 'd' }
   *    name = { 'a', 'b'}
   *    startIndex = 0
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   * </ol>
   *
   * @param fragment the fragment to check
   * @param name the array to check
   * @param startIndex the starting index
   * @param isCaseSensitive check whether or not the equality should be case sensitive
   * @return true if the name contains the fragment at the starting index startIndex according to
   *     the value of isCaseSensitive, otherwise false.
   * @throws NullPointerException if fragment or name is null.
   */
  public static final boolean fragmentEquals(
      char[] fragment, char[] name, int startIndex, boolean isCaseSensitive) {

    int max = fragment.length;
    if (name.length < max + startIndex) return false;
    if (isCaseSensitive) {
      for (int i = max; --i >= 0; ) {
        // assumes the prefix is not larger than the name
        if (fragment[i] != name[i + startIndex]) {
          return false;
        }
      }
      return true;
    }
    for (int i = max; --i >= 0; ) {
      // assumes the prefix is not larger than the name
      if (ScannerHelper.toLowerCase(fragment[i])
          != ScannerHelper.toLowerCase(name[i + startIndex])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Answers a hashcode for the array
   *
   * @param array the array for which a hashcode is required
   * @return the hashcode
   */
  public static final int hashCode(char[] array) {
    int hash = Arrays.hashCode(array);
    return hash & 0x7FFFFFFF;
  }

  /**
   * Answers true if c is a whitespace according to the JLS (&#92;u0009, &#92;u000a, &#92;u000c,
   * &#92;u000d, &#92;u0020), otherwise false. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    c = ' '
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    c = '&#92;u3000'
   *    result => false
   * </pre>
   * </ol>
   *
   * @param c the character to check
   * @return true if c is a whitespace according to the JLS, otherwise false.
   */
  public static boolean isWhitespace(char c) {
    return c < ScannerHelper.MAX_OBVIOUS
        && ((ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_JLS_SPACE) != 0);
  }

  /**
   * Answers the first index in the array for which the corresponding character is equal to
   * toBeFound. Answers -1 if no occurrence of this character is found. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { ' a', 'b', 'c', 'd' }
   *    result => 2
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'e'
   *    array = { ' a', 'b', 'c', 'd' }
   *    result => -1
   * </pre>
   * </ol>
   *
   * @param toBeFound the character to search
   * @param array the array to be searched
   * @return the first index in the array for which the corresponding character is equal to
   *     toBeFound, -1 otherwise
   * @throws NullPointerException if array is null
   */
  public static final int indexOf(char toBeFound, char[] array) {
    return indexOf(toBeFound, array, 0);
  }

  /**
   * Answers the first index in the array for which the corresponding character is equal to
   * toBeFound starting the search at index start. Answers -1 if no occurrence of this character is
   * found. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { ' a', 'b', 'c', 'd' }
   *    start = 2
   *    result => 2
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { ' a', 'b', 'c', 'd' }
   *    start = 3
   *    result => -1
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'e'
   *    array = { ' a', 'b', 'c', 'd' }
   *    start = 1
   *    result => -1
   * </pre>
   * </ol>
   *
   * @param toBeFound the character to search
   * @param array the array to be searched
   * @param start the starting index
   * @return the first index in the array for which the corresponding character is equal to
   *     toBeFound, -1 otherwise
   * @throws NullPointerException if array is null
   * @throws ArrayIndexOutOfBoundsException if start is lower than 0
   */
  public static final int indexOf(char toBeFound, char[] array, int start) {
    for (int i = start; i < array.length; i++) if (toBeFound == array[i]) return i;
    return -1;
  }

  /**
   * Answers the first index in the array for which the toBeFound array is a matching subarray
   * following the case rule. Answers -1 if no match is found. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    toBeFound = { 'c' }
   *    array = { ' a', 'b', 'c', 'd' }
   *    result => 2
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = { 'e' }
   *    array = { ' a', 'b', 'c', 'd' }
   *    result => -1
   * </pre>
   * </ol>
   *
   * @param toBeFound the subarray to search
   * @param array the array to be searched
   * @param isCaseSensitive flag to know if the matching should be case sensitive
   * @return the first index in the array for which the toBeFound array is a matching subarray
   *     following the case rule, -1 otherwise
   * @throws NullPointerException if array is null or toBeFound is null
   * @since 3.2
   */
  public static final int indexOf(char[] toBeFound, char[] array, boolean isCaseSensitive) {
    return indexOf(toBeFound, array, isCaseSensitive, 0);
  }

  /**
   * Answers the first index in the array for which the toBeFound array is a matching subarray
   * following the case rule starting at the index start. Answers -1 if no match is found. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    toBeFound = { 'c' }
   *    array = { ' a', 'b', 'c', 'd' }
   *    result => 2
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = { 'e' }
   *    array = { ' a', 'b', 'c', 'd' }
   *    result => -1
   * </pre>
   * </ol>
   *
   * @param toBeFound the subarray to search
   * @param array the array to be searched
   * @param isCaseSensitive flag to know if the matching should be case sensitive
   * @param start the starting index
   * @return the first index in the array for which the toBeFound array is a matching subarray
   *     following the case rule starting at the index start, -1 otherwise
   * @throws NullPointerException if array is null or toBeFound is null
   * @since 3.2
   */
  public static final int indexOf(
      final char[] toBeFound, final char[] array, final boolean isCaseSensitive, final int start) {
    return indexOf(toBeFound, array, isCaseSensitive, start, array.length);
  }

  /**
   * Answers the first index in the array for which the toBeFound array is a matching subarray
   * following the case rule starting at the index start. Answers -1 if no match is found. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    toBeFound = { 'c' }
   *    array = { ' a', 'b', 'c', 'd' }
   *    result => 2
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = { 'e' }
   *    array = { ' a', 'b', 'c', 'd' }
   *    result => -1
   * </pre>
   * </ol>
   *
   * @param toBeFound the subarray to search
   * @param array the array to be searched
   * @param isCaseSensitive flag to know if the matching should be case sensitive
   * @param start the starting index (inclusive)
   * @param end the end index (exclusive)
   * @return the first index in the array for which the toBeFound array is a matching subarray
   *     following the case rule starting at the index start, -1 otherwise
   * @throws NullPointerException if array is null or toBeFound is null
   * @since 3.2
   */
  public static final int indexOf(
      final char[] toBeFound,
      final char[] array,
      final boolean isCaseSensitive,
      final int start,
      final int end) {
    final int arrayLength = end;
    final int toBeFoundLength = toBeFound.length;
    if (toBeFoundLength > arrayLength || start < 0) return -1;
    if (toBeFoundLength == 0) return 0;
    if (toBeFoundLength == arrayLength) {
      if (isCaseSensitive) {
        for (int i = start; i < arrayLength; i++) {
          if (array[i] != toBeFound[i]) return -1;
        }
        return 0;
      } else {
        for (int i = start; i < arrayLength; i++) {
          if (ScannerHelper.toLowerCase(array[i]) != ScannerHelper.toLowerCase(toBeFound[i]))
            return -1;
        }
        return 0;
      }
    }
    if (isCaseSensitive) {
      arrayLoop:
      for (int i = start, max = arrayLength - toBeFoundLength + 1; i < max; i++) {
        if (array[i] == toBeFound[0]) {
          for (int j = 1; j < toBeFoundLength; j++) {
            if (array[i + j] != toBeFound[j]) continue arrayLoop;
          }
          return i;
        }
      }
    } else {
      arrayLoop:
      for (int i = start, max = arrayLength - toBeFoundLength + 1; i < max; i++) {
        if (ScannerHelper.toLowerCase(array[i]) == ScannerHelper.toLowerCase(toBeFound[0])) {
          for (int j = 1; j < toBeFoundLength; j++) {
            if (ScannerHelper.toLowerCase(array[i + j]) != ScannerHelper.toLowerCase(toBeFound[j]))
              continue arrayLoop;
          }
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Answers the first index in the array for which the corresponding character is equal to
   * toBeFound starting the search at index start and before the ending index. Answers -1 if no
   * occurrence of this character is found. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { ' a', 'b', 'c', 'd' }
   *    start = 2
   *    result => 2
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { ' a', 'b', 'c', 'd' }
   *    start = 3
   *    result => -1
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'e'
   *    array = { ' a', 'b', 'c', 'd' }
   *    start = 1
   *    result => -1
   * </pre>
   * </ol>
   *
   * @param toBeFound the character to search
   * @param array the array to be searched
   * @param start the starting index (inclusive)
   * @param end the ending index (exclusive)
   * @return the first index in the array for which the corresponding character is equal to
   *     toBeFound, -1 otherwise
   * @throws NullPointerException if array is null
   * @throws ArrayIndexOutOfBoundsException if start is lower than 0 or ending greater than array
   *     length
   * @since 3.2
   */
  public static final int indexOf(char toBeFound, char[] array, int start, int end) {
    for (int i = start; i < end; i++) if (toBeFound == array[i]) return i;
    return -1;
  }

  /**
   * Answers the last index in the array for which the corresponding character is equal to toBeFound
   * stopping at the index startIndex. Answers -1 if no occurrence of this character is found. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { ' a', 'b', 'c', 'd' }
   *    startIndex = 2
   *    result => 2
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { ' a', 'b', 'c', 'd', 'e' }
   *    startIndex = 3
   *    result => -1
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'e'
   *    array = { ' a', 'b', 'c', 'd' }
   *    startIndex = 0
   *    result => -1
   * </pre>
   * </ol>
   *
   * @param toBeFound the character to search
   * @param array the array to be searched
   * @param startIndex the stopping index
   * @return the last index in the array for which the corresponding character is equal to toBeFound
   *     stopping at the index startIndex, -1 otherwise
   * @throws NullPointerException if array is null
   * @throws ArrayIndexOutOfBoundsException if startIndex is lower than 0
   */
  public static final int lastIndexOf(char toBeFound, char[] array, int startIndex) {
    for (int i = array.length; --i >= startIndex; ) if (toBeFound == array[i]) return i;
    return -1;
  }

  /**
   * Answers the last index in the array for which the corresponding character is equal to toBeFound
   * starting from endIndex to startIndex. Answers -1 if no occurrence of this character is found.
   * <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { ' a', 'b', 'c', 'd' }
   *    startIndex = 2
   *    endIndex = 2
   *    result => 2
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { ' a', 'b', 'c', 'd', 'e' }
   *    startIndex = 3
   *    endIndex = 4
   *    result => -1
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'e'
   *    array = { ' a', 'b', 'c', 'd' }
   *    startIndex = 0
   *    endIndex = 3
   *    result => -1
   * </pre>
   * </ol>
   *
   * @param toBeFound the character to search
   * @param array the array to be searched
   * @param startIndex the stopping index
   * @param endIndex the starting index
   * @return the last index in the array for which the corresponding character is equal to toBeFound
   *     starting from endIndex to startIndex, -1 otherwise
   * @throws NullPointerException if array is null
   * @throws ArrayIndexOutOfBoundsException if endIndex is greater or equals to array length or
   *     starting is lower than 0
   */
  public static final int lastIndexOf(char toBeFound, char[] array, int startIndex, int endIndex) {
    for (int i = endIndex; --i >= startIndex; ) if (toBeFound == array[i]) return i;
    return -1;
  }

  /**
   * Answers the last portion of a name given a separator. <br>
   * <br>
   * For example,
   *
   * <pre>
   * 	lastSegment("java.lang.Object".toCharArray(),'.') --> Object
   * </pre>
   *
   * @param array the array
   * @param separator the given separator
   * @return the last portion of a name given a separator
   * @throws NullPointerException if array is null
   */
  public static final char[] lastSegment(char[] array, char separator) {
    int pos = lastIndexOf(separator, array);
    if (pos < 0) return array;
    return subarray(array, pos + 1, array.length);
  }

  /**
   * Answers the last index in the array for which the corresponding character is equal to toBeFound
   * starting from the end of the array. Answers -1 if no occurrence of this character is found.
   * <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { ' a', 'b', 'c', 'd' , 'c', 'e' }
   *    result => 4
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'e'
   *    array = { ' a', 'b', 'c', 'd' }
   *    result => -1
   * </pre>
   * </ol>
   *
   * @param toBeFound the character to search
   * @param array the array to be searched
   * @return the last index in the array for which the corresponding character is equal to toBeFound
   *     starting from the end of the array, -1 otherwise
   * @throws NullPointerException if array is null
   */
  public static final int lastIndexOf(char toBeFound, char[] array) {
    for (int i = array.length; --i >= 0; ) if (toBeFound == array[i]) return i;
    return -1;
  }

  /**
   * Answers a new array which is a copy of the given array starting at the given start and ending
   * at the given end. The given start is inclusive and the given end is exclusive. Answers null if
   * start is greater than end, if start is lower than 0 or if end is greater than the length of the
   * given array. If end equals -1, it is converted to the array length. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { 'a' , 'b' }
   *    start = 0
   *    end = 1
   *    result => { 'a' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'a', 'b' }
   *    start = 0
   *    end = -1
   *    result => { 'a' , 'b' }
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param start the given starting index
   * @param end the given ending index
   * @return a new array which is a copy of the given array starting at the given start and ending
   *     at the given end
   * @throws NullPointerException if the given array is null
   */
  public static final char[] subarray(char[] array, int start, int end) {
    if (end == -1) end = array.length;
    if (start > end) return null;
    if (start < 0) return null;
    if (end > array.length) return null;

    char[] result = new char[end - start];
    System.arraycopy(array, start, result, 0, end - start);
    return result;
  }

  /**
   * Answers true if the pattern matches the given name, false otherwise. This char[] pattern
   * matching accepts wild-cards '*' and '?'.
   *
   * <p>When not case sensitive, the pattern is assumed to already be lowercased, the name will be
   * lowercased character per character as comparing.<br>
   * If name is null, the answer is false.<br>
   * If pattern is null, the answer is true if name is not null. For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    pattern = { '?', 'b', '*' }
   *    name = { 'a', 'b', 'c' , 'd' }
   *    isCaseSensitive = true
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    pattern = { '?', 'b', '?' }
   *    name = { 'a', 'b', 'c' , 'd' }
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   *   <li>
   *       <pre>
   *    pattern = { 'b', '*' }
   *    name = { 'a', 'b', 'c' , 'd' }
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   * </ol>
   *
   * @param pattern the given pattern
   * @param name the given name
   * @param isCaseSensitive flag to know whether or not the matching should be case sensitive
   * @return true if the pattern matches the given name, false otherwise
   */
  public static final boolean match(char[] pattern, char[] name, boolean isCaseSensitive) {

    if (name == null) return false; // null name cannot match
    if (pattern == null) return true; // null pattern is equivalent to '*'

    return match(pattern, 0, pattern.length, name, 0, name.length, isCaseSensitive);
  }

  /**
   * Answers true if a sub-pattern matches the subpart of the given name, false otherwise. char[]
   * pattern matching, accepting wild-cards '*' and '?'. Can match only subset of name/pattern. end
   * positions are non-inclusive. The subpattern is defined by the patternStart and pattternEnd
   * positions. When not case sensitive, the pattern is assumed to already be lowercased, the name
   * will be lowercased character per character as comparing. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    pattern = { '?', 'b', '*' }
   *    patternStart = 1
   *    patternEnd = 3
   *    name = { 'a', 'b', 'c' , 'd' }
   *    nameStart = 1
   *    nameEnd = 4
   *    isCaseSensitive = true
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    pattern = { '?', 'b', '*' }
   *    patternStart = 1
   *    patternEnd = 2
   *    name = { 'a', 'b', 'c' , 'd' }
   *    nameStart = 1
   *    nameEnd = 4
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   * </ol>
   *
   * @param pattern the given pattern
   * @param patternStart the given pattern start
   * @param patternEnd the given pattern end
   * @param name the given name
   * @param nameStart the given name start
   * @param nameEnd the given name end
   * @param isCaseSensitive flag to know if the matching should be case sensitive
   * @return true if a sub-pattern matches the subpart of the given name, false otherwise
   */
  public static final boolean match(
      char[] pattern,
      int patternStart,
      int patternEnd,
      char[] name,
      int nameStart,
      int nameEnd,
      boolean isCaseSensitive) {

    if (name == null) return false; // null name cannot match
    if (pattern == null) return true; // null pattern is equivalent to '*'
    int iPattern = patternStart;
    int iName = nameStart;

    if (patternEnd < 0) patternEnd = pattern.length;
    if (nameEnd < 0) nameEnd = name.length;

    /* check first segment */
    char patternChar = 0;
    while (true) {
      if (iPattern == patternEnd) {
        if (iName == nameEnd) return true; // the chars match
        return false; // pattern has ended but not the name, no match
      }
      if ((patternChar = pattern[iPattern]) == '*') {
        break;
      }
      if (iName == nameEnd) {
        return false; // name has ended but not the pattern
      }
      if (patternChar != (isCaseSensitive ? name[iName] : ScannerHelper.toLowerCase(name[iName]))
          && patternChar != '?') {
        return false;
      }
      iName++;
      iPattern++;
    }
    /* check sequence of star+segment */
    int segmentStart;
    if (patternChar == '*') {
      segmentStart = ++iPattern; // skip star
    } else {
      segmentStart = 0; // force iName check
    }
    int prefixStart = iName;
    checkSegment:
    while (iName < nameEnd) {
      if (iPattern == patternEnd) {
        iPattern = segmentStart; // mismatch - restart current segment
        iName = ++prefixStart;
        continue checkSegment;
      }
      /* segment is ending */
      if ((patternChar = pattern[iPattern]) == '*') {
        segmentStart = ++iPattern; // skip start
        if (segmentStart == patternEnd) {
          return true;
        }
        prefixStart = iName;
        continue checkSegment;
      }
      /* check current name character */
      if ((isCaseSensitive ? name[iName] : ScannerHelper.toLowerCase(name[iName])) != patternChar
          && patternChar != '?') {
        iPattern = segmentStart; // mismatch - restart current segment
        iName = ++prefixStart;
        continue checkSegment;
      }
      iName++;
      iPattern++;
    }

    return (segmentStart == patternEnd)
        || (iName == nameEnd && iPattern == patternEnd)
        || (iPattern == patternEnd - 1 && pattern[iPattern] == '*');
  }

  /**
   * Answers true if the pattern matches the filepath using the pathSepatator, false otherwise.
   *
   * <p>Path char[] pattern matching, accepting wild-cards '**', '*' and '?' (using Ant directory
   * tasks conventions, also see
   * "http://jakarta.apache.org/ant/manual/dirtasks.html#defaultexcludes"). Path pattern matching is
   * enhancing regular pattern matching in supporting extra rule where '**' represent any folder
   * combination. Special rule: - foo\ is equivalent to foo\** When not case sensitive, the pattern
   * is assumed to already be lowercased, the name will be lowercased character per character as
   * comparing.
   *
   * @param pattern the given pattern
   * @param filepath the given path
   * @param isCaseSensitive to find out whether or not the matching should be case sensitive
   * @param pathSeparator the given path separator
   * @return true if the pattern matches the filepath using the pathSepatator, false otherwise
   */
  public static final boolean pathMatch(
      char[] pattern, char[] filepath, boolean isCaseSensitive, char pathSeparator) {

    if (filepath == null) return false; // null name cannot match
    if (pattern == null) return true; // null pattern is equivalent to '*'

    // offsets inside pattern
    int pSegmentStart = pattern[0] == pathSeparator ? 1 : 0;
    int pLength = pattern.length;
    int pSegmentEnd = CharOperation.indexOf(pathSeparator, pattern, pSegmentStart + 1);
    if (pSegmentEnd < 0) pSegmentEnd = pLength;

    // special case: pattern foo\ is equivalent to foo\**
    boolean freeTrailingDoubleStar = pattern[pLength - 1] == pathSeparator;

    // offsets inside filepath
    int fSegmentStart, fLength = filepath.length;
    if (filepath[0] != pathSeparator) {
      fSegmentStart = 0;
    } else {
      fSegmentStart = 1;
    }
    if (fSegmentStart != pSegmentStart) {
      return false; // both must start with a separator or none.
    }
    int fSegmentEnd = CharOperation.indexOf(pathSeparator, filepath, fSegmentStart + 1);
    if (fSegmentEnd < 0) fSegmentEnd = fLength;

    // first segments
    while (pSegmentStart < pLength
        && !(pSegmentEnd == pLength && freeTrailingDoubleStar
            || (pSegmentEnd == pSegmentStart + 2
                && pattern[pSegmentStart] == '*'
                && pattern[pSegmentStart + 1] == '*'))) {

      if (fSegmentStart >= fLength) return false;
      if (!CharOperation.match(
          pattern,
          pSegmentStart,
          pSegmentEnd,
          filepath,
          fSegmentStart,
          fSegmentEnd,
          isCaseSensitive)) {
        return false;
      }

      // jump to next segment
      pSegmentEnd = CharOperation.indexOf(pathSeparator, pattern, pSegmentStart = pSegmentEnd + 1);
      // skip separator
      if (pSegmentEnd < 0) pSegmentEnd = pLength;

      fSegmentEnd = CharOperation.indexOf(pathSeparator, filepath, fSegmentStart = fSegmentEnd + 1);
      // skip separator
      if (fSegmentEnd < 0) fSegmentEnd = fLength;
    }

    /* check sequence of doubleStar+segment */
    int pSegmentRestart;
    if ((pSegmentStart >= pLength && freeTrailingDoubleStar)
        || (pSegmentEnd == pSegmentStart + 2
            && pattern[pSegmentStart] == '*'
            && pattern[pSegmentStart + 1] == '*')) {
      pSegmentEnd = CharOperation.indexOf(pathSeparator, pattern, pSegmentStart = pSegmentEnd + 1);
      // skip separator
      if (pSegmentEnd < 0) pSegmentEnd = pLength;
      pSegmentRestart = pSegmentStart;
    } else {
      if (pSegmentStart >= pLength)
        return fSegmentStart >= fLength; // true if filepath is done too.
      pSegmentRestart = 0; // force fSegmentStart check
    }
    int fSegmentRestart = fSegmentStart;
    checkSegment:
    while (fSegmentStart < fLength) {

      if (pSegmentStart >= pLength) {
        if (freeTrailingDoubleStar) return true;
        // mismatch - restart current path segment
        pSegmentEnd =
            CharOperation.indexOf(pathSeparator, pattern, pSegmentStart = pSegmentRestart);
        if (pSegmentEnd < 0) pSegmentEnd = pLength;

        fSegmentRestart = CharOperation.indexOf(pathSeparator, filepath, fSegmentRestart + 1);
        // skip separator
        if (fSegmentRestart < 0) {
          fSegmentRestart = fLength;
        } else {
          fSegmentRestart++;
        }
        fSegmentEnd =
            CharOperation.indexOf(pathSeparator, filepath, fSegmentStart = fSegmentRestart);
        if (fSegmentEnd < 0) fSegmentEnd = fLength;
        continue checkSegment;
      }

      /* path segment is ending */
      if (pSegmentEnd == pSegmentStart + 2
          && pattern[pSegmentStart] == '*'
          && pattern[pSegmentStart + 1] == '*') {
        pSegmentEnd =
            CharOperation.indexOf(pathSeparator, pattern, pSegmentStart = pSegmentEnd + 1);
        // skip separator
        if (pSegmentEnd < 0) pSegmentEnd = pLength;
        pSegmentRestart = pSegmentStart;
        fSegmentRestart = fSegmentStart;
        if (pSegmentStart >= pLength) return true;
        continue checkSegment;
      }
      /* chech current path segment */
      if (!CharOperation.match(
          pattern,
          pSegmentStart,
          pSegmentEnd,
          filepath,
          fSegmentStart,
          fSegmentEnd,
          isCaseSensitive)) {
        // mismatch - restart current path segment
        pSegmentEnd =
            CharOperation.indexOf(pathSeparator, pattern, pSegmentStart = pSegmentRestart);
        if (pSegmentEnd < 0) pSegmentEnd = pLength;

        fSegmentRestart = CharOperation.indexOf(pathSeparator, filepath, fSegmentRestart + 1);
        // skip separator
        if (fSegmentRestart < 0) {
          fSegmentRestart = fLength;
        } else {
          fSegmentRestart++;
        }
        fSegmentEnd =
            CharOperation.indexOf(pathSeparator, filepath, fSegmentStart = fSegmentRestart);
        if (fSegmentEnd < 0) fSegmentEnd = fLength;
        continue checkSegment;
      }
      // jump to next segment
      pSegmentEnd = CharOperation.indexOf(pathSeparator, pattern, pSegmentStart = pSegmentEnd + 1);
      // skip separator
      if (pSegmentEnd < 0) pSegmentEnd = pLength;

      fSegmentEnd = CharOperation.indexOf(pathSeparator, filepath, fSegmentStart = fSegmentEnd + 1);
      // skip separator
      if (fSegmentEnd < 0) fSegmentEnd = fLength;
    }

    return (pSegmentRestart >= pSegmentEnd)
        || (fSegmentStart >= fLength && pSegmentStart >= pLength)
        || (pSegmentStart == pLength - 2
            && pattern[pSegmentStart] == '*'
            && pattern[pSegmentStart + 1] == '*')
        || (pSegmentStart == pLength && freeTrailingDoubleStar);
  }

  /**
   * Answers the number of occurrences of the given character in the given array, 0 if any. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    toBeFound = 'b'
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    result => 3
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    result => 0
   * </pre>
   * </ol>
   *
   * @param toBeFound the given character
   * @param array the given array
   * @return the number of occurrences of the given character in the given array, 0 if any
   * @throws NullPointerException if array is null
   */
  public static final int occurencesOf(char toBeFound, char[] array) {
    int count = 0;
    for (int i = 0; i < array.length; i++) if (toBeFound == array[i]) count++;
    return count;
  }

  /**
   * Answers the number of occurrences of the given character in the given array starting at the
   * given index, 0 if any. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    toBeFound = 'b'
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    start = 2
   *    result => 2
   * </pre>
   *   <li>
   *       <pre>
   *    toBeFound = 'c'
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    start = 0
   *    result => 0
   * </pre>
   * </ol>
   *
   * @param toBeFound the given character
   * @param array the given array
   * @param start the given index
   * @return the number of occurrences of the given character in the given array, 0 if any
   * @throws NullPointerException if array is null
   * @throws ArrayIndexOutOfBoundsException if start is lower than 0
   */
  public static final int occurencesOf(char toBeFound, char[] array, int start) {
    int count = 0;
    for (int i = start; i < array.length; i++) if (toBeFound == array[i]) count++;
    return count;
  }

  /**
   * Return the int value represented by the designated subpart of array. The calculation of the
   * result for single-digit positive integers is optimized in time.
   *
   * @param array the array within which the int value is to be parsed
   * @param start first character of the int value in array
   * @param length length of the int value in array
   * @return the int value of a subpart of array
   * @throws NumberFormatException if the designated subpart of array does not parse to an int
   * @since 3.4
   */
  public static final int parseInt(char[] array, int start, int length)
      throws NumberFormatException {
    if (length == 1) {
      int result = array[start] - '0';
      if (result < 0 || result > 9) {
        throw new NumberFormatException("invalid digit"); // $NON-NLS-1$
      }
      return result;
    } else {
      return Integer.parseInt(new String(array, start, length));
    }
  }

  /**
   * Answers true if the given name starts with the given prefix, false otherwise. The comparison is
   * case sensitive. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    prefix = { 'a' , 'b' }
   *    name = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    prefix = { 'a' , 'c' }
   *    name = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    result => false
   * </pre>
   * </ol>
   *
   * @param prefix the given prefix
   * @param name the given name
   * @return true if the given name starts with the given prefix, false otherwise
   * @throws NullPointerException if the given name is null or if the given prefix is null
   */
  public static final boolean prefixEquals(char[] prefix, char[] name) {

    int max = prefix.length;
    if (name.length < max) {
      return false;
    }
    for (int i = max; --i >= 0; ) {
      // assumes the prefix is not larger than the name
      if (prefix[i] != name[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * Answers true if the given name starts with the given prefix, false otherwise. isCaseSensitive
   * is used to find out whether or not the comparison should be case sensitive. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    prefix = { 'a' , 'B' }
   *    name = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    isCaseSensitive = false
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    prefix = { 'a' , 'B' }
   *    name = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   * </ol>
   *
   * @param prefix the given prefix
   * @param name the given name
   * @param isCaseSensitive to find out whether or not the comparison should be case sensitive
   * @return true if the given name starts with the given prefix, false otherwise
   * @throws NullPointerException if the given name is null or if the given prefix is null
   */
  public static final boolean prefixEquals(char[] prefix, char[] name, boolean isCaseSensitive) {
    return prefixEquals(prefix, name, isCaseSensitive, 0);
  }

  /**
   * Answers true if the given name, starting from the given index, starts with the given prefix,
   * false otherwise. isCaseSensitive is used to find out whether or not the comparison should be
   * case sensitive. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    prefix = { 'a' , 'B' }
   *    name = { 'c', 'd', 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    startIndex = 2
   *    isCaseSensitive = false
   *    result => true
   * </pre>
   *   <li>
   *       <pre>
   *    prefix = { 'a' , 'B' }
   *    name = { 'c', 'd', 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    startIndex = 2
   *    isCaseSensitive = true
   *    result => false
   * </pre>
   * </ol>
   *
   * @param prefix the given prefix
   * @param name the given name
   * @param isCaseSensitive to find out whether or not the comparison should be case sensitive
   * @param startIndex index from which the prefix should be searched in the name
   * @return true if the given name starts with the given prefix, false otherwise
   * @throws NullPointerException if the given name is null or if the given prefix is null
   * @since 3.7
   */
  public static final boolean prefixEquals(
      char[] prefix, char[] name, boolean isCaseSensitive, int startIndex) {

    int max = prefix.length;
    if (name.length - startIndex < max) return false;
    if (isCaseSensitive) {
      for (int i = max; --i >= 0; ) {
        // assumes the prefix is not larger than the name
        if (prefix[i] != name[startIndex + i]) {
          return false;
        }
      }
      return true;
    }

    for (int i = max; --i >= 0; ) {
      // assumes the prefix is not larger than the name
      if (ScannerHelper.toLowerCase(prefix[i]) != ScannerHelper.toLowerCase(name[startIndex + i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Answers a new array removing a given character. Answers the given array if there is no
   * occurrence of the character to remove. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { 'a' , 'b', 'b', 'c', 'b', 'a' }
   *    toBeRemoved = 'b'
   *    return { 'a' , 'c', 'a' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    toBeRemoved = 'c'
   *    return array
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param toBeRemoved the character to be removed
   * @return a new array removing given character
   * @since 3.2
   */
  public static final char[] remove(char[] array, char toBeRemoved) {

    if (array == null) return null;
    int length = array.length;
    if (length == 0) return array;
    char[] result = null;
    int count = 0;
    for (int i = 0; i < length; i++) {
      char c = array[i];
      if (c == toBeRemoved) {
        if (result == null) {
          result = new char[length];
          System.arraycopy(array, 0, result, 0, i);
          count = i;
        }
      } else if (result != null) {
        result[count++] = c;
      }
    }
    if (result == null) return array;
    System.arraycopy(result, 0, result = new char[count], 0, count);
    return result;
  }

  /**
   * Replace all occurrence of the character to be replaced with the replacement character in the
   * given array. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    toBeReplaced = 'b'
   *    replacementChar = 'a'
   *    result => No returned value, but array is now equals to { 'a' , 'a', 'a', 'a', 'a', 'a' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    toBeReplaced = 'c'
   *    replacementChar = 'a'
   *    result => No returned value, but array is now equals to { 'a' , 'b', 'b', 'a', 'b', 'a' }
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param toBeReplaced the character to be replaced
   * @param replacementChar the replacement character
   * @throws NullPointerException if the given array is null
   */
  public static final void replace(char[] array, char toBeReplaced, char replacementChar) {
    if (toBeReplaced != replacementChar) {
      for (int i = 0, max = array.length; i < max; i++) {
        if (array[i] == toBeReplaced) array[i] = replacementChar;
      }
    }
  }

  /**
   * Replace all occurrences of characters to be replaced with the replacement character in the
   * given array. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { 'a' , 'b', 'b', 'c', 'a', 'b', 'c', 'a' }
   *    toBeReplaced = { 'b', 'c' }
   *    replacementChar = 'a'
   *    result => No returned value, but array is now equals to { 'a' , 'a', 'a', 'a', 'a', 'a', 'a', 'a' }
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param toBeReplaced characters to be replaced
   * @param replacementChar the replacement character
   * @throws NullPointerException if arrays are null.
   * @since 3.1
   */
  public static final void replace(char[] array, char[] toBeReplaced, char replacementChar) {
    replace(array, toBeReplaced, replacementChar, 0, array.length);
  }

  /**
   * Replace all occurrences of characters to be replaced with the replacement character in the
   * given array from the start position (inclusive) to the end position (exclusive). <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { 'a' , 'b', 'b', 'c', 'a', 'b', 'c', 'a' }
   *    toBeReplaced = { 'b', 'c' }
   *    replacementChar = 'a'
   *    start = 4
   *    end = 8
   *    result => No returned value, but array is now equals to { 'a' , 'b', 'b', 'c', 'a', 'a', 'a', 'a' }
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param toBeReplaced characters to be replaced
   * @param replacementChar the replacement character
   * @param start the given start position (inclusive)
   * @param end the given end position (exclusive)
   * @throws NullPointerException if arrays are null.
   * @since 3.2
   */
  public static final void replace(
      char[] array, char[] toBeReplaced, char replacementChar, int start, int end) {
    for (int i = end; --i >= start; )
      for (int j = toBeReplaced.length; --j >= 0; )
        if (array[i] == toBeReplaced[j]) array[i] = replacementChar;
  }

  /**
   * Answers a new array of characters with substitutions. No side-effect is operated on the
   * original array, in case no substitution happened, then the result is the same as the original
   * one. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    toBeReplaced = { 'b' }
   *    replacementChar = { 'a', 'a' }
   *    result => { 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    toBeReplaced = { 'c' }
   *    replacementChar = { 'a' }
   *    result => { 'a' , 'b', 'b', 'a', 'b', 'a' }
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param toBeReplaced characters to be replaced
   * @param replacementChars the replacement characters
   * @return a new array of characters with substitutions or the given array if none
   * @throws NullPointerException if the given array is null
   */
  public static final char[] replace(char[] array, char[] toBeReplaced, char[] replacementChars) {

    int max = array.length;
    int replacedLength = toBeReplaced.length;
    int replacementLength = replacementChars.length;

    int[] starts = new int[5];
    int occurrenceCount = 0;

    if (!equals(toBeReplaced, replacementChars)) {

      next:
      for (int i = 0; i < max; ) {
        int index = indexOf(toBeReplaced, array, true, i);
        if (index == -1) {
          i++;
          continue next;
        }
        if (occurrenceCount == starts.length) {
          System.arraycopy(starts, 0, starts = new int[occurrenceCount * 2], 0, occurrenceCount);
        }
        starts[occurrenceCount++] = index;
        i = index + replacedLength;
      }
    }
    if (occurrenceCount == 0) return array;
    char[] result = new char[max + occurrenceCount * (replacementLength - replacedLength)];
    int inStart = 0, outStart = 0;
    for (int i = 0; i < occurrenceCount; i++) {
      int offset = starts[i] - inStart;
      System.arraycopy(array, inStart, result, outStart, offset);
      inStart += offset;
      outStart += offset;
      System.arraycopy(replacementChars, 0, result, outStart, replacementLength);
      inStart += replacedLength;
      outStart += replacementLength;
    }
    System.arraycopy(array, inStart, result, outStart, max - inStart);
    return result;
  }

  /**
   * Replace all occurrence of the character to be replaced with the replacement character in a copy
   * of the given array. Returns the given array if no occurrences of the character to be replaced
   * are found. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    toBeReplaced = 'b'
   *    replacementChar = 'a'
   *    result => A new array that is equals to { 'a' , 'a', 'a', 'a', 'a', 'a' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    toBeReplaced = 'c'
   *    replacementChar = 'a'
   *    result => The original array that remains unchanged.
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param toBeReplaced the character to be replaced
   * @param replacementChar the replacement character
   * @throws NullPointerException if the given array is null
   * @since 3.1
   */
  public static final char[] replaceOnCopy(char[] array, char toBeReplaced, char replacementChar) {

    char[] result = null;
    for (int i = 0, length = array.length; i < length; i++) {
      char c = array[i];
      if (c == toBeReplaced) {
        if (result == null) {
          result = new char[length];
          System.arraycopy(array, 0, result, 0, i);
        }
        result[i] = replacementChar;
      } else if (result != null) {
        result[i] = c;
      }
    }
    if (result == null) return array;
    return result;
  }

  /**
   * Return a new array which is the split of the given array using the given divider and trimming
   * each subarray to remove whitespaces equals to ' '. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    divider = 'b'
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    result => { { 'a' }, {  }, { 'a' }, { 'a' } }
   * </pre>
   *   <li>
   *       <pre>
   *    divider = 'c'
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    result => { { 'a', 'b', 'b', 'a', 'b', 'a' } }
   * </pre>
   *   <li>
   *       <pre>
   *    divider = 'b'
   *    array = { 'a' , ' ', 'b', 'b', 'a', 'b', 'a' }
   *    result => { { 'a' }, {  }, { 'a' }, { 'a' } }
   * </pre>
   *   <li>
   *       <pre>
   *    divider = 'c'
   *    array = { ' ', ' ', 'a' , 'b', 'b', 'a', 'b', 'a', ' ' }
   *    result => { { 'a', 'b', 'b', 'a', 'b', 'a' } }
   * </pre>
   * </ol>
   *
   * @param divider the given divider
   * @param array the given array
   * @return a new array which is the split of the given array using the given divider and trimming
   *     each subarray to remove whitespaces equals to ' '
   */
  public static final char[][] splitAndTrimOn(char divider, char[] array) {
    int length = array == null ? 0 : array.length;
    if (length == 0) return NO_CHAR_CHAR;

    int wordCount = 1;
    for (int i = 0; i < length; i++) if (array[i] == divider) wordCount++;
    char[][] split = new char[wordCount][];
    int last = 0, currentWord = 0;
    for (int i = 0; i < length; i++) {
      if (array[i] == divider) {
        int start = last, end = i - 1;
        while (start < i && array[start] == ' ') start++;
        while (end > start && array[end] == ' ') end--;
        split[currentWord] = new char[end - start + 1];
        System.arraycopy(array, start, split[currentWord++], 0, end - start + 1);
        last = i + 1;
      }
    }
    int start = last, end = length - 1;
    while (start < length && array[start] == ' ') start++;
    while (end > start && array[end] == ' ') end--;
    split[currentWord] = new char[end - start + 1];
    System.arraycopy(array, start, split[currentWord++], 0, end - start + 1);
    return split;
  }

  /**
   * Return a new array which is the split of the given array using the given divider. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    divider = 'b'
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    result => { { 'a' }, {  }, { 'a' }, { 'a' } }
   * </pre>
   *   <li>
   *       <pre>
   *    divider = 'c'
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    result => { { 'a', 'b', 'b', 'a', 'b', 'a' } }
   * </pre>
   *   <li>
   *       <pre>
   *    divider = 'c'
   *    array = { ' ', ' ', 'a' , 'b', 'b', 'a', 'b', 'a', ' ' }
   *    result => { { ' ', 'a', 'b', 'b', 'a', 'b', 'a', ' ' } }
   * </pre>
   * </ol>
   *
   * @param divider the given divider
   * @param array the given array
   * @return a new array which is the split of the given array using the given divider
   */
  public static final char[][] splitOn(char divider, char[] array) {
    int length = array == null ? 0 : array.length;
    if (length == 0) return NO_CHAR_CHAR;

    int wordCount = 1;
    for (int i = 0; i < length; i++) if (array[i] == divider) wordCount++;
    char[][] split = new char[wordCount][];
    int last = 0, currentWord = 0;
    for (int i = 0; i < length; i++) {
      if (array[i] == divider) {
        split[currentWord] = new char[i - last];
        System.arraycopy(array, last, split[currentWord++], 0, i - last);
        last = i + 1;
      }
    }
    split[currentWord] = new char[length - last];
    System.arraycopy(array, last, split[currentWord], 0, length - last);
    return split;
  }

  /**
   * Return a new array which is the split of the given array using the given divider ignoring the
   * text between (possibly nested) openEncl and closingEncl. If there are no openEncl in the code
   * this is identical to {@link CharOperation#splitOn(char, char[], int, int)}. The given end is
   * exclusive and the given start is inclusive. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    divider = ','
   *    array = { 'A' , '<', 'B', ',', 'C', '>', ',', 'D' }
   *    start = 0
   *    end = 8
   *    result => { {  'A' , '<', 'B', ',', 'C', '>'}, { 'D' }}
   * </pre>
   * </ol>
   *
   * @param divider the given divider
   * @param openEncl the opening enclosure
   * @param closeEncl the closing enclosure
   * @param array the given array
   * @param start the given starting index
   * @param end the given ending index
   * @return a new array which is the split of the given array using the given divider
   * @throws ArrayIndexOutOfBoundsException if start is lower than 0 or end is greater than the
   *     array length
   * @since 3.12
   */
  public static final char[][] splitOnWithEnclosures(
      char divider, char openEncl, char closeEncl, char[] array, int start, int end) {
    int length = array == null ? 0 : array.length;
    if (length == 0 || start > end) return NO_CHAR_CHAR;

    int wordCount = 1;
    int enclCount = 0;
    for (int i = start; i < end; i++) {
      if (array[i] == openEncl) enclCount++;
      else if (array[i] == divider) wordCount++;
    }
    if (enclCount == 0) return CharOperation.splitOn(divider, array, start, end);

    int nesting = 0;
    if (openEncl == divider || closeEncl == divider) {
      // divider should be distinct
      return CharOperation.NO_CHAR_CHAR;
    }

    int[][] splitOffsets = new int[wordCount][2]; // maximum
    int last = start, currentWord = 0, prevOffset = start;
    for (int i = start; i < end; i++) {
      if (array[i] == openEncl) {
        ++nesting;
        continue;
      }
      if (array[i] == closeEncl) {
        if (nesting > 0) --nesting;
        continue;
      }
      if (array[i] == divider && nesting == 0) {
        splitOffsets[currentWord][0] = prevOffset;
        last = splitOffsets[currentWord++][1] = i;
        prevOffset = last + 1;
      }
    }
    if (last < end - 1) {
      splitOffsets[currentWord][0] = prevOffset;
      splitOffsets[currentWord++][1] = end;
    }
    char[][] split = new char[currentWord][];
    for (int i = 0; i < currentWord; ++i) {
      int sStart = splitOffsets[i][0];
      int sEnd = splitOffsets[i][1];
      int size = sEnd - sStart;
      split[i] = new char[size];
      System.arraycopy(array, sStart, split[i], 0, size);
    }
    return split;
  }

  /**
   * Return a new array which is the split of the given array using the given divider. The given end
   * is exclusive and the given start is inclusive. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    divider = 'b'
   *    array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
   *    start = 2
   *    end = 5
   *    result => { {  }, { 'a' }, {  } }
   * </pre>
   * </ol>
   *
   * @param divider the given divider
   * @param array the given array
   * @param start the given starting index
   * @param end the given ending index
   * @return a new array which is the split of the given array using the given divider
   * @throws ArrayIndexOutOfBoundsException if start is lower than 0 or end is greater than the
   *     array length
   */
  public static final char[][] splitOn(char divider, char[] array, int start, int end) {
    int length = array == null ? 0 : array.length;
    if (length == 0 || start > end) return NO_CHAR_CHAR;

    int wordCount = 1;
    for (int i = start; i < end; i++) if (array[i] == divider) wordCount++;
    char[][] split = new char[wordCount][];
    int last = start, currentWord = 0;
    for (int i = start; i < end; i++) {
      if (array[i] == divider) {
        split[currentWord] = new char[i - last];
        System.arraycopy(array, last, split[currentWord++], 0, i - last);
        last = i + 1;
      }
    }
    split[currentWord] = new char[end - last];
    System.arraycopy(array, last, split[currentWord], 0, end - last);
    return split;
  }

  /**
   * Answers a new array which is a copy of the given array starting at the given start and ending
   * at the given end. The given start is inclusive and the given end is exclusive. Answers null if
   * start is greater than end, if start is lower than 0 or if end is greater than the length of the
   * given array. If end equals -1, it is converted to the array length. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { { 'a' } , { 'b' } }
   *    start = 0
   *    end = 1
   *    result => { { 'a' } }
   * </pre>
   *   <li>
   *       <pre>
   *    array = { { 'a' } , { 'b' } }
   *    start = 0
   *    end = -1
   *    result => { { 'a' }, { 'b' } }
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @param start the given starting index
   * @param end the given ending index
   * @return a new array which is a copy of the given array starting at the given start and ending
   *     at the given end
   * @throws NullPointerException if the given array is null
   */
  public static final char[][] subarray(char[][] array, int start, int end) {
    if (end == -1) end = array.length;
    if (start > end) return null;
    if (start < 0) return null;
    if (end > array.length) return null;

    char[][] result = new char[end - start][];
    System.arraycopy(array, start, result, 0, end - start);
    return result;
  }

  /**
   * Answers the result of a char[] conversion to lowercase. Answers null if the given chars array
   * is null. <br>
   * NOTE: If no conversion was necessary, then answers back the argument one. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    chars = { 'a' , 'b' }
   *    result => { 'a' , 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'A', 'b' }
   *    result => { 'a' , 'b' }
   * </pre>
   * </ol>
   *
   * @param chars the chars to convert
   * @return the result of a char[] conversion to lowercase
   */
  public static final char[] toLowerCase(char[] chars) {
    if (chars == null) return null;
    int length = chars.length;
    char[] lowerChars = null;
    for (int i = 0; i < length; i++) {
      char c = chars[i];
      char lc = ScannerHelper.toLowerCase(c);
      if ((c != lc) || (lowerChars != null)) {
        if (lowerChars == null) {
          System.arraycopy(chars, 0, lowerChars = new char[length], 0, i);
        }
        lowerChars[i] = lc;
      }
    }
    return lowerChars == null ? chars : lowerChars;
  }

  /**
   * Answers the result of a char[] conversion to uppercase. Answers null if the given chars array
   * is null. <br>
   * NOTE: If no conversion was necessary, then answers back the argument one. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    chars = { 'A' , 'B' }
   *    result => { 'A' , 'B' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'a', 'B' }
   *    result => { 'A' , 'B' }
   * </pre>
   * </ol>
   *
   * @param chars the chars to convert
   * @return the result of a char[] conversion to uppercase
   * @since 3.5
   */
  public static final char[] toUpperCase(char[] chars) {
    if (chars == null) return null;
    int length = chars.length;
    char[] upperChars = null;
    for (int i = 0; i < length; i++) {
      char c = chars[i];
      char lc = ScannerHelper.toUpperCase(c);
      if ((c != lc) || (upperChars != null)) {
        if (upperChars == null) {
          System.arraycopy(chars, 0, upperChars = new char[length], 0, i);
        }
        upperChars[i] = lc;
      }
    }
    return upperChars == null ? chars : upperChars;
  }

  /**
   * Answers a new array removing leading and trailing spaces (' '). Answers the given array if
   * there is no space characters to remove. <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    chars = { ' ', 'a' , 'b', ' ',  ' ' }
   *    result => { 'a' , 'b' }
   * </pre>
   *   <li>
   *       <pre>
   *    array = { 'A', 'b' }
   *    result => { 'A' , 'b' }
   * </pre>
   * </ol>
   *
   * @param chars the given array
   * @return a new array removing leading and trailing spaces (' ')
   */
  public static final char[] trim(char[] chars) {

    if (chars == null) return null;

    int start = 0, length = chars.length, end = length - 1;
    while (start < length && chars[start] == ' ') {
      start++;
    }
    while (end > start && chars[end] == ' ') {
      end--;
    }
    if (start != 0 || end != length - 1) {
      return subarray(chars, start, end + 1);
    }
    return chars;
  }

  /**
   * Answers a string which is the concatenation of the given array using the '.' as a separator.
   * <br>
   * <br>
   * For example:
   *
   * <ol>
   *   <li>
   *       <pre>
   *    array = { { 'a' } , { 'b' } }
   *    result => "a.b"
   * </pre>
   *   <li>
   *       <pre>
   *    array = { { ' ',  'a' } , { 'b' } }
   *    result => " a.b"
   * </pre>
   * </ol>
   *
   * @param array the given array
   * @return a string which is the concatenation of the given array using the '.' as a separator
   */
  public static final String toString(char[][] array) {
    char[] result = concatWith(array, '.');
    return new String(result);
  }

  /**
   * Answers an array of strings from the given array of char array.
   *
   * @param array the given array
   * @return an array of strings
   * @since 3.0
   */
  public static final String[] toStrings(char[][] array) {
    if (array == null) return NO_STRINGS;
    int length = array.length;
    if (length == 0) return NO_STRINGS;
    String[] result = new String[length];
    for (int i = 0; i < length; i++) result[i] = new String(array[i]);
    return result;
  }
}
