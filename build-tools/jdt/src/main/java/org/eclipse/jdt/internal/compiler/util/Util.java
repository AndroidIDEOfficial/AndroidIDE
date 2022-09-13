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
package org.eclipse.jdt.internal.compiler.util;

import org.eclipse.jdt.core.compiler.CharOperation;

public class Util {

  /**
   * Character constant indicating the primitive type boolean in a signature. Value is <code>'Z'
   * </code>.
   */
  public static final char C_BOOLEAN = 'Z';

  /**
   * Character constant indicating the primitive type byte in a signature. Value is <code>'B'
   * </code> .
   */
  public static final char C_BYTE = 'B';

  /**
   * Character constant indicating the primitive type char in a signature. Value is <code>'C'
   * </code> .
   */
  public static final char C_CHAR = 'C';

  /**
   * Character constant indicating the primitive type double in a signature. Value is <code>'D'
   * </code>.
   */
  public static final char C_DOUBLE = 'D';

  /**
   * Character constant indicating the primitive type float in a signature. Value is <code>'F'
   * </code>.
   */
  public static final char C_FLOAT = 'F';

  /**
   * Character constant indicating the primitive type int in a signature. Value is <code>'I'
   * </code>.
   */
  public static final char C_INT = 'I';

  /** Character constant indicating the semicolon in a signature. Value is <code>';'</code>. */
  public static final char C_SEMICOLON = ';';

  /**
   * Character constant indicating the colon in a signature. Value is <code>':'</code>.
   *
   * @since 3.0
   */
  public static final char C_COLON = ':';

  /**
   * Character constant indicating the primitive type long in a signature. Value is <code>'J'
   * </code> .
   */
  public static final char C_LONG = 'J';

  /**
   * Character constant indicating the primitive type short in a signature. Value is <code>'S'
   * </code>.
   */
  public static final char C_SHORT = 'S';

  /** Character constant indicating result type void in a signature. Value is <code>'V'</code>. */
  public static final char C_VOID = 'V';

  /**
   * Character constant indicating the start of a resolved type variable in a signature. Value is
   * <code>'T'</code>.
   *
   * @since 3.0
   */
  public static final char C_TYPE_VARIABLE = 'T';

  /**
   * Character constant indicating an unbound wildcard type argument in a signature. Value is <code>
   * '*'</code>.
   *
   * @since 3.0
   */
  public static final char C_STAR = '*';

  /**
   * Character constant indicating an exception in a signature. Value is <code>'^'</code>.
   *
   * @since 3.1
   */
  public static final char C_EXCEPTION_START = '^';

  /**
   * Character constant indicating a bound wildcard type argument in a signature with extends
   * clause. Value is <code>'+'</code>.
   *
   * @since 3.1
   */
  public static final char C_EXTENDS = '+';

  /**
   * Character constant indicating a bound wildcard type argument in a signature with super clause.
   * Value is <code>'-'</code>.
   *
   * @since 3.1
   */
  public static final char C_SUPER = '-';

  /** Character constant indicating the dot in a signature. Value is <code>'.'</code>. */
  public static final char C_DOT = '.';

  /** Character constant indicating the dollar in a signature. Value is <code>'$'</code>. */
  public static final char C_DOLLAR = '$';

  /** Character constant indicating an array type in a signature. Value is <code>'['</code>. */
  public static final char C_ARRAY = '[';

  /**
   * Character constant indicating the start of a resolved, named type in a signature. Value is
   * <code>'L'</code>.
   */
  public static final char C_RESOLVED = 'L';

  /**
   * Character constant indicating the start of an unresolved, named type in a signature. Value is
   * <code>'Q'</code>.
   */
  public static final char C_UNRESOLVED = 'Q';

  /**
   * Character constant indicating the end of a named type in a signature. Value is <code>';'
   * </code> .
   */
  public static final char C_NAME_END = ';';

  /**
   * Character constant indicating the start of a parameter type list in a signature. Value is
   * <code>'('</code>.
   */
  public static final char C_PARAM_START = '(';

  /**
   * Character constant indicating the end of a parameter type list in a signature. Value is <code>
   * ')'</code>.
   */
  public static final char C_PARAM_END = ')';

  /**
   * Character constant indicating the start of a formal type parameter (or type argument) list in a
   * signature. Value is <code>'&lt;'</code>.
   *
   * @since 3.0
   */
  public static final char C_GENERIC_START = '<';

  /**
   * Character constant indicating the end of a generic type list in a signature. Value is <code>
   * '&gt;'</code>.
   *
   * @since 3.0
   */
  public static final char C_GENERIC_END = '>';

  /**
   * Character constant indicating a capture of a wildcard type in a signature. Value is <code>'!'
   * </code>.
   *
   * @since 3.1
   */
  public static final char C_CAPTURE = '!';

  public static final String EMPTY_STRING = new String(CharOperation.NO_CHAR);

  public static int getParameterCount(char[] methodSignature) {
    try {
      int count = 0;
      int i = CharOperation.indexOf(C_PARAM_START, methodSignature);
      if (i < 0) {
        throw new IllegalArgumentException(String.valueOf(methodSignature));
      } else {
        i++;
      }
      for (; ; ) {
        if (methodSignature[i] == C_PARAM_END) {
          return count;
        }
        int e = Util.scanTypeSignature(methodSignature, i);
        if (e < 0) {
          throw new IllegalArgumentException(String.valueOf(methodSignature));
        } else {
          i = e + 1;
        }
        count++;
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalArgumentException(String.valueOf(methodSignature), e);
    }
  }

  /**
   * Scans the given string for a type signature starting at the given index and returns the index
   * of the last character.
   *
   * <pre>
   * TypeSignature:
   *  |  BaseTypeSignature
   *  |  ArrayTypeSignature
   *  |  ClassTypeSignature
   *  |  TypeVariableSignature
   * </pre>
   *
   * @param string the signature string
   * @param start the 0-based character index of the first character
   * @return the 0-based character index of the last character
   * @exception IllegalArgumentException if this is not a type signature
   */
  public static int scanTypeSignature(char[] string, int start) {
    // need a minimum 1 char
    if (start >= string.length) {
      throw newIllegalArgumentException(string, start);
    }
    char c = string[start];
    switch (c) {
      case C_ARRAY:
        return scanArrayTypeSignature(string, start);
      case C_RESOLVED:
      case C_UNRESOLVED:
        return scanClassTypeSignature(string, start);
      case C_TYPE_VARIABLE:
        return scanTypeVariableSignature(string, start);
      case C_BOOLEAN:
      case C_BYTE:
      case C_CHAR:
      case C_DOUBLE:
      case C_FLOAT:
      case C_INT:
      case C_LONG:
      case C_SHORT:
      case C_VOID:
        return scanBaseTypeSignature(string, start);
      case C_CAPTURE:
        return scanCaptureTypeSignature(string, start);
      case C_EXTENDS:
      case C_SUPER:
      case C_STAR:
        return scanTypeBoundSignature(string, start);
      default:
        throw newIllegalArgumentException(string, start);
    }
  }

  /**
   * Scans the given string for a base type signature starting at the given index and returns the
   * index of the last character.
   *
   * <pre>
   * BaseTypeSignature:
   *     <b>B</b> | <b>C</b> | <b>D</b> | <b>F</b> | <b>I</b>
   *   | <b>J</b> | <b>S</b> | <b>V</b> | <b>Z</b>
   * </pre>
   *
   * Note that although the base type "V" is only allowed in method return types, there is no
   * syntactic ambiguity. This method will accept them anywhere without complaint.
   *
   * @param string the signature string
   * @param start the 0-based character index of the first character
   * @return the 0-based character index of the last character
   * @exception IllegalArgumentException if this is not a base type signature
   */
  public static int scanBaseTypeSignature(char[] string, int start) {
    // need a minimum 1 char
    if (start >= string.length) {
      throw newIllegalArgumentException(string, start);
    }
    char c = string[start];
    if ("BCDFIJSVZ".indexOf(c) >= 0) { // $NON-NLS-1$
      return start;
    } else {
      throw newIllegalArgumentException(string, start);
    }
  }

  /**
   * Scans the given string for an array type signature starting at the given index and returns the
   * index of the last character.
   *
   * <pre>
   * ArrayTypeSignature:
   *     <b>[</b> TypeSignature
   * </pre>
   *
   * @param string the signature string
   * @param start the 0-based character index of the first character
   * @return the 0-based character index of the last character
   * @exception IllegalArgumentException if this is not an array type signature
   */
  public static int scanArrayTypeSignature(char[] string, int start) {
    int length = string.length;
    // need a minimum 2 char
    if (start >= length - 1) {
      throw newIllegalArgumentException(string, start);
    }
    char c = string[start];
    if (c != C_ARRAY) {
      throw newIllegalArgumentException(string, start);
    }

    c = string[++start];
    while (c == C_ARRAY) {
      // need a minimum 2 char
      if (start >= length - 1) {
        throw newIllegalArgumentException(string, start);
      }
      c = string[++start];
    }
    return scanTypeSignature(string, start);
  }

  /**
   * Scans the given string for a capture of a wildcard type signature starting at the given index
   * and returns the index of the last character.
   *
   * <pre>
   * CaptureTypeSignature:
   *     <b>!</b> TypeBoundSignature
   * </pre>
   *
   * @param string the signature string
   * @param start the 0-based character index of the first character
   * @return the 0-based character index of the last character
   * @exception IllegalArgumentException if this is not a capture type signature
   */
  public static int scanCaptureTypeSignature(char[] string, int start) {
    // need a minimum 2 char
    if (start >= string.length - 1) {
      throw newIllegalArgumentException(string, start);
    }
    char c = string[start];
    if (c != C_CAPTURE) {
      throw newIllegalArgumentException(string, start);
    }
    return scanTypeBoundSignature(string, start + 1);
  }

  /**
   * Scans the given string for a type variable signature starting at the given index and returns
   * the index of the last character.
   *
   * <pre>
   * TypeVariableSignature:
   *     <b>T</b> Identifier <b>;</b>
   * </pre>
   *
   * @param string the signature string
   * @param start the 0-based character index of the first character
   * @return the 0-based character index of the last character
   * @exception IllegalArgumentException if this is not a type variable signature
   */
  public static int scanTypeVariableSignature(char[] string, int start) {
    // need a minimum 3 chars "Tx;"
    if (start >= string.length - 2) {
      throw newIllegalArgumentException(string, start);
    }
    // must start in "T"
    char c = string[start];
    if (c != C_TYPE_VARIABLE) {
      throw newIllegalArgumentException(string, start);
    }
    int id = scanIdentifier(string, start + 1);
    c = string[id + 1];
    if (c == C_SEMICOLON) {
      return id + 1;
    } else {
      throw newIllegalArgumentException(string, start);
    }
  }

  /**
   * Scans the given string for an identifier starting at the given index and returns the index of
   * the last character. Stop characters are: ";", ":", "&lt;", "&gt;", "/", ".".
   *
   * @param string the signature string
   * @param start the 0-based character index of the first character
   * @return the 0-based character index of the last character
   * @exception IllegalArgumentException if this is not an identifier
   */
  public static int scanIdentifier(char[] string, int start) {
    // need a minimum 1 char
    if (start >= string.length) {
      throw newIllegalArgumentException(string, start);
    }
    int p = start;
    while (true) {
      char c = string[p];
      if (c == '<' || c == '>' || c == ':' || c == ';' || c == '.' || c == '/') {
        return p - 1;
      }
      p++;
      if (p == string.length) {
        return p - 1;
      }
    }
  }

  /**
   * Scans the given string for a class type signature starting at the given index and returns the
   * index of the last character.
   *
   * <pre>
   * ClassTypeSignature:
   *     { <b>L</b> | <b>Q</b> } Identifier
   *           { { <b>/</b> | <b>.</b> Identifier [ <b>&lt;</b> TypeArgumentSignature* <b>&gt;</b> ] }
   *           <b>;</b>
   * </pre>
   *
   * Note that although all "/"-identifiers most come before "."-identifiers, there is no syntactic
   * ambiguity. This method will accept them without complaint.
   *
   * @param string the signature string
   * @param start the 0-based character index of the first character
   * @return the 0-based character index of the last character
   * @exception IllegalArgumentException if this is not a class type signature
   */
  public static int scanClassTypeSignature(char[] string, int start) {
    // need a minimum 3 chars "Lx;"
    if (start >= string.length - 2) {
      throw newIllegalArgumentException(string, start);
    }
    // must start in "L" or "Q"
    char c = string[start];
    if (c != C_RESOLVED && c != C_UNRESOLVED) {
      return -1;
    }
    int p = start + 1;
    while (true) {
      if (p >= string.length) {
        throw newIllegalArgumentException(string, start);
      }
      c = string[p];
      if (c == C_SEMICOLON) {
        // all done
        return p;
      } else if (c == C_GENERIC_START) {
        int e = scanTypeArgumentSignatures(string, p);
        p = e;
      } else if (c == C_DOT || c == '/') {
        int id = scanIdentifier(string, p + 1);
        p = id;
      }
      p++;
    }
  }

  /**
   * Scans the given string for a type bound signature starting at the given index and returns the
   * index of the last character.
   *
   * <pre>
   * TypeBoundSignature:
   *     <b>[-+]</b> TypeSignature <b>;</b>
   *     <b>*</b></b>
   * </pre>
   *
   * @param string the signature string
   * @param start the 0-based character index of the first character
   * @return the 0-based character index of the last character
   * @exception IllegalArgumentException if this is not a type variable signature
   */
  public static int scanTypeBoundSignature(char[] string, int start) {
    // need a minimum 1 char for wildcard
    if (start >= string.length) {
      throw newIllegalArgumentException(string, start);
    }
    char c = string[start];
    switch (c) {
      case C_STAR:
        return start;
      case C_SUPER:
      case C_EXTENDS:
        break;
      default:
        // must start in "+/-"
        throw newIllegalArgumentException(string, start);
    }
    c = string[++start];
    if (c != C_STAR
        && start >= string.length - 1) { // unless "-*" we need at least one more char, e.g. after
      // "+[", other
      // variants are even longer
      throw new IllegalArgumentException();
    }
    switch (c) {
      case C_CAPTURE:
        return scanCaptureTypeSignature(string, start);
      case C_SUPER:
      case C_EXTENDS:
        return scanTypeBoundSignature(string, start);
      case C_RESOLVED:
      case C_UNRESOLVED:
        return scanClassTypeSignature(string, start);
      case C_TYPE_VARIABLE:
        return scanTypeVariableSignature(string, start);
      case C_ARRAY:
        return scanArrayTypeSignature(string, start);
      case C_STAR:
        return start;
      default:
        throw newIllegalArgumentException(string, start);
    }
  }

  /**
   * Scans the given string for a list of type argument signatures starting at the given index and
   * returns the index of the last character.
   *
   * <pre>
   * TypeArgumentSignatures:
   *     <b>&lt;</b> TypeArgumentSignature* <b>&gt;</b>
   * </pre>
   *
   * Note that although there is supposed to be at least one type argument, there is no syntactic
   * ambiguity if there are none. This method will accept zero type argument signatures without
   * complaint.
   *
   * @param string the signature string
   * @param start the 0-based character index of the first character
   * @return the 0-based character index of the last character
   * @exception IllegalArgumentException if this is not a list of type arguments signatures
   */
  public static int scanTypeArgumentSignatures(char[] string, int start) {
    // need a minimum 2 char "<>"
    if (start >= string.length - 1) {
      throw newIllegalArgumentException(string, start);
    }
    char c = string[start];
    if (c != C_GENERIC_START) {
      throw newIllegalArgumentException(string, start);
    }
    int p = start + 1;
    while (true) {
      if (p >= string.length) {
        throw newIllegalArgumentException(string, start);
      }
      c = string[p];
      if (c == C_GENERIC_END) {
        return p;
      }
      int e = scanTypeArgumentSignature(string, p);
      p = e + 1;
    }
  }

  /**
   * Scans the given string for a type argument signature starting at the given index and returns
   * the index of the last character.
   *
   * <pre>
   * TypeArgumentSignature:
   *     <b>&#42;</b>
   *  |  <b>+</b> TypeSignature
   *  |  <b>-</b> TypeSignature
   *  |  TypeSignature
   * </pre>
   *
   * Note that although base types are not allowed in type arguments, there is no syntactic
   * ambiguity. This method will accept them without complaint.
   *
   * @param string the signature string
   * @param start the 0-based character index of the first character
   * @return the 0-based character index of the last character
   * @exception IllegalArgumentException if this is not a type argument signature
   */
  public static int scanTypeArgumentSignature(char[] string, int start) {
    // need a minimum 1 char
    if (start >= string.length) {
      throw newIllegalArgumentException(string, start);
    }
    char c = string[start];
    switch (c) {
      case C_STAR:
        return start;
      case C_EXTENDS:
      case C_SUPER:
        return scanTypeBoundSignature(string, start);
      default:
        return scanTypeSignature(string, start);
    }
  }

  public static boolean effectivelyEqual(Object[] one, Object[] two) {
    if (one == two) return true;
    int oneLength = one == null ? 0 : one.length;
    int twoLength = two == null ? 0 : two.length;
    if (oneLength != twoLength) return false;
    if (oneLength == 0) return true;
    for (int i = 0; i < one.length; i++) {
      if (one[i] != two[i]) return false;
    }
    return true;
  }

  public static void appendEscapedChar(StringBuffer buffer, char c, boolean stringLiteral) {
    switch (c) {
      case '\b':
        buffer.append("\\b"); // $NON-NLS-1$
        break;
      case '\t':
        buffer.append("\\t"); // $NON-NLS-1$
        break;
      case '\n':
        buffer.append("\\n"); // $NON-NLS-1$
        break;
      case '\f':
        buffer.append("\\f"); // $NON-NLS-1$
        break;
      case '\r':
        buffer.append("\\r"); // $NON-NLS-1$
        break;
      case '\"':
        if (stringLiteral) {
          buffer.append("\\\""); // $NON-NLS-1$
        } else {
          buffer.append(c);
        }
        break;
      case '\'':
        if (stringLiteral) {
          buffer.append(c);
        } else {
          buffer.append("\\\'"); // $NON-NLS-1$
        }
        break;
      case '\\':
        buffer.append("\\\\"); // $NON-NLS-1$
        break;
      default:
        if (c >= 0x20) {
          buffer.append(c);
        } else if (c >= 0x10) {
          buffer.append("\\u00").append(Integer.toHexString(c)); // $NON-NLS-1$
        } else if (c >= 0) {
          buffer.append("\\u000").append(Integer.toHexString(c)); // $NON-NLS-1$
        } else {
          buffer.append(c);
        }
    }
  }

  private static IllegalArgumentException newIllegalArgumentException(char[] string, int start) {
    return new IllegalArgumentException(
        "\"" + String.valueOf(string) + "\" at " + start); // $NON-NLS-1$ //$NON-NLS-2$
  }
}
