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
 * Copyright (c) 2005, 2020 IBM Corporation and others.
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
 *     David Foerster - patch for toUpperCase as described in https://bugs.eclipse.org/bugs/show_bug.cgi?id=153125
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.parser;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ScannerHelper {

  public static final long[] Bits = {
    ASTNode.Bit1, ASTNode.Bit2, ASTNode.Bit3, ASTNode.Bit4, ASTNode.Bit5, ASTNode.Bit6,
    ASTNode.Bit7, ASTNode.Bit8, ASTNode.Bit9, ASTNode.Bit10, ASTNode.Bit11, ASTNode.Bit12,
    ASTNode.Bit13, ASTNode.Bit14, ASTNode.Bit15, ASTNode.Bit16, ASTNode.Bit17, ASTNode.Bit18,
    ASTNode.Bit19, ASTNode.Bit20, ASTNode.Bit21, ASTNode.Bit22, ASTNode.Bit23, ASTNode.Bit24,
    ASTNode.Bit25, ASTNode.Bit26, ASTNode.Bit27, ASTNode.Bit28, ASTNode.Bit29, ASTNode.Bit30,
    ASTNode.Bit31, ASTNode.Bit32L, ASTNode.Bit33L, ASTNode.Bit34L, ASTNode.Bit35L, ASTNode.Bit36L,
    ASTNode.Bit37L, ASTNode.Bit38L, ASTNode.Bit39L, ASTNode.Bit40L, ASTNode.Bit41L, ASTNode.Bit42L,
    ASTNode.Bit43L, ASTNode.Bit44L, ASTNode.Bit45L, ASTNode.Bit46L, ASTNode.Bit47L, ASTNode.Bit48L,
    ASTNode.Bit49L, ASTNode.Bit50L, ASTNode.Bit51L, ASTNode.Bit52L, ASTNode.Bit53L, ASTNode.Bit54L,
    ASTNode.Bit55L, ASTNode.Bit56L, ASTNode.Bit57L, ASTNode.Bit58L, ASTNode.Bit59L, ASTNode.Bit60L,
    ASTNode.Bit61L, ASTNode.Bit62L, ASTNode.Bit63L, ASTNode.Bit64L,
  };
  public static final int MAX_OBVIOUS = 128;
  public static final int[] OBVIOUS_IDENT_CHAR_NATURES = new int[MAX_OBVIOUS];
  public static final int C_JLS_SPACE = ASTNode.Bit9;
  public static final int C_SPECIAL = ASTNode.Bit8;
  public static final int C_IDENT_START = ASTNode.Bit7;
  public static final int C_UPPER_LETTER = ASTNode.Bit6;
  public static final int C_LOWER_LETTER = ASTNode.Bit5;
  public static final int C_IDENT_PART = ASTNode.Bit4;
  public static final int C_DIGIT = ASTNode.Bit3;
  public static final int C_SEPARATOR = ASTNode.Bit2;
  public static final int C_SPACE = ASTNode.Bit1;
  private static final int START_INDEX = 0;
  private static final int PART_INDEX = 1;
  private static long[][][] Tables;
  private static long[][][] Tables7;
  private static long[][][] Tables8;
  private static long[][][] Tables9;
  private static long[][][] Tables11;
  private static long[][][] Tables12;
  private static long[][][] Tables13;
  private static long[][][] Tables15;

  static {
    OBVIOUS_IDENT_CHAR_NATURES[0] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[1] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[2] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[3] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[4] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[5] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[6] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[7] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[8] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[14] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[15] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[16] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[17] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[18] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[19] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[20] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[21] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[22] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[23] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[24] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[25] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[26] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[27] = C_IDENT_PART;
    OBVIOUS_IDENT_CHAR_NATURES[127] = C_IDENT_PART;

    for (int i = '0'; i <= '9'; i++) OBVIOUS_IDENT_CHAR_NATURES[i] = C_DIGIT | C_IDENT_PART;

    for (int i = 'a'; i <= 'z'; i++)
      OBVIOUS_IDENT_CHAR_NATURES[i] = C_LOWER_LETTER | C_IDENT_PART | C_IDENT_START;
    for (int i = 'A'; i <= 'Z'; i++)
      OBVIOUS_IDENT_CHAR_NATURES[i] = C_UPPER_LETTER | C_IDENT_PART | C_IDENT_START;

    OBVIOUS_IDENT_CHAR_NATURES['_'] = C_SPECIAL | C_IDENT_PART | C_IDENT_START;
    OBVIOUS_IDENT_CHAR_NATURES['$'] = C_SPECIAL | C_IDENT_PART | C_IDENT_START;

    OBVIOUS_IDENT_CHAR_NATURES[9] = C_SPACE | C_JLS_SPACE; // \ u0009: HORIZONTAL TABULATION
    OBVIOUS_IDENT_CHAR_NATURES[10] = C_SPACE | C_JLS_SPACE; // \ u000a: LINE FEED
    OBVIOUS_IDENT_CHAR_NATURES[11] = C_SPACE;
    OBVIOUS_IDENT_CHAR_NATURES[12] = C_SPACE | C_JLS_SPACE; // \ u000c: FORM FEED
    OBVIOUS_IDENT_CHAR_NATURES[13] = C_SPACE | C_JLS_SPACE; //  \ u000d: CARRIAGE RETURN
    OBVIOUS_IDENT_CHAR_NATURES[28] = C_SPACE;
    OBVIOUS_IDENT_CHAR_NATURES[29] = C_SPACE;
    OBVIOUS_IDENT_CHAR_NATURES[30] = C_SPACE;
    OBVIOUS_IDENT_CHAR_NATURES[31] = C_SPACE;
    OBVIOUS_IDENT_CHAR_NATURES[32] = C_SPACE | C_JLS_SPACE; //  \ u0020: SPACE

    OBVIOUS_IDENT_CHAR_NATURES['.'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES[':'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES[';'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES[','] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['['] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES[']'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['('] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES[')'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['{'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['}'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['+'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['-'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['*'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['/'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['='] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['&'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['|'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['?'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['<'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['>'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['!'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['%'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['^'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['~'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['"'] = C_SEPARATOR;
    OBVIOUS_IDENT_CHAR_NATURES['\''] = C_SEPARATOR;
  }

  public static boolean isJavaIdentifierPart(char c) {
    if (c < MAX_OBVIOUS) {
      return (ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_IDENT_PART) != 0;
    }
    return Character.isJavaIdentifierPart(c);
  }

  public static boolean isJavaIdentifierPart(long complianceLevel, char c) {
    if (c < MAX_OBVIOUS) {
      return (ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_IDENT_PART) != 0;
    }
    return isJavaIdentifierPart(complianceLevel, (int) c);
  }

  public static boolean isJavaIdentifierPart(long complianceLevel, int codePoint) {
    if (complianceLevel <= ClassFileConstants.JDK1_6) {
      if (Tables == null) {
        initializeTable();
      }
      return isJavaIdentifierPart0(codePoint, Tables);
    } else if (complianceLevel <= ClassFileConstants.JDK1_7) {
      // java 7 supports Unicode 6
      if (Tables7 == null) {
        initializeTable17();
      }
      return isJavaIdentifierPart0(codePoint, Tables7);
    } else if (complianceLevel <= ClassFileConstants.JDK1_8) {
      // java 8 supports Unicode 6.2
      if (Tables8 == null) {
        initializeTable18();
      }
      return isJavaIdentifierPart0(codePoint, Tables8);
    } else if (complianceLevel <= ClassFileConstants.JDK10) {
      // java 9/10 supports Unicode 8
      if (Tables9 == null) {
        initializeTable19();
      }
      return isJavaIdentifierPart0(codePoint, Tables9);
    } else if (complianceLevel <= ClassFileConstants.JDK11) {
      // java 11 supports Unicode 10
      if (Tables11 == null) {
        initializeTableJava11();
      }
      return isJavaIdentifierPart0(codePoint, Tables11);
    } else if (complianceLevel <= ClassFileConstants.JDK12) {
      // java 12 supports Unicode 11
      if (Tables12 == null) {
        initializeTableJava12();
      }
      return isJavaIdentifierPart0(codePoint, Tables12);
    } else if (complianceLevel <= ClassFileConstants.JDK14) {
      // java 13 and 14 support Unicode 12.1
      if (Tables13 == null) {
        initializeTableJava13();
      }
      return isJavaIdentifierPart0(codePoint, Tables13);

    } else {
      // java 15 supports Unicode 13
      if (Tables15 == null) {
        initializeTableJava15();
      }
      return isJavaIdentifierPart0(codePoint, Tables15, true);
    }
  }

  public static boolean isJavaIdentifierStart(char c) {
    if (c < MAX_OBVIOUS) {
      return (ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_IDENT_START) != 0;
    }
    return Character.isJavaIdentifierStart(c);
  }

  public static boolean isJavaIdentifierStart(long complianceLevel, char c) {
    if (c < MAX_OBVIOUS) {
      return (ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_IDENT_START) != 0;
    }
    return ScannerHelper.isJavaIdentifierStart(complianceLevel, (int) c);
  }

  public static boolean isJavaIdentifierStart(long complianceLevel, int codePoint) {
    if (complianceLevel <= ClassFileConstants.JDK1_6) {
      if (Tables == null) {
        initializeTable();
      }
      return isJavaIdentifierStart0(codePoint, Tables);
    } else if (complianceLevel <= ClassFileConstants.JDK1_7) {
      // java 7 supports Unicode 6
      if (Tables7 == null) {
        initializeTable17();
      }
      return isJavaIdentifierStart0(codePoint, Tables7);
    } else if (complianceLevel <= ClassFileConstants.JDK1_8) {
      // java 8 supports Unicode 6.2
      if (Tables8 == null) {
        initializeTable18();
      }
      return isJavaIdentifierStart0(codePoint, Tables8);
    } else if (complianceLevel <= ClassFileConstants.JDK10) {
      // java 9/10 supports Unicode 8
      if (Tables9 == null) {
        initializeTable19();
      }
      return isJavaIdentifierStart0(codePoint, Tables9);
    } else if (complianceLevel <= ClassFileConstants.JDK11) {
      // java 11 supports Unicode 10
      if (Tables11 == null) {
        initializeTableJava11();
      }
      return isJavaIdentifierStart0(codePoint, Tables11);
    } else if (complianceLevel <= ClassFileConstants.JDK12) {
      // java 12 supports Unicode 11
      if (Tables12 == null) {
        initializeTableJava12();
      }
      return isJavaIdentifierStart0(codePoint, Tables12);
    } else if (complianceLevel <= ClassFileConstants.JDK14) {
      // java 13 and 14 support Unicode 12.1
      if (Tables13 == null) {
        initializeTableJava13();
      }
      return isJavaIdentifierStart0(codePoint, Tables13);
    } else {
      // java 15 supports Unicode 13
      if (Tables15 == null) {
        initializeTableJava15();
      }
      return isJavaIdentifierStart0(codePoint, Tables15, true);
    }
  }

  public static boolean isDigit(char c) throws InvalidInputException {
    if (c < ScannerHelper.MAX_OBVIOUS) {
      return (ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_DIGIT) != 0;
    }
    return Character.isDigit(c);
  }

  public static int digit(char c, int radix) {
    if (c < ScannerHelper.MAX_OBVIOUS) {
      switch (radix) {
        case 8:
          if (c >= 48 && c <= 55) {
            return c - 48;
          }
          return -1;
        case 10:
          if (c >= 48 && c <= 57) {
            return c - 48;
          }
          return -1;
        case 16:
          if (c >= 48 && c <= 57) {
            return c - 48;
          }
          if (c >= 65 && c <= 70) {
            return c - 65 + 10;
          }
          if (c >= 97 && c <= 102) {
            return c - 97 + 10;
          }
          return -1;
      }
    }
    return Character.digit(c, radix);
  }

  public static int getNumericValue(char c) {
    if (c < ScannerHelper.MAX_OBVIOUS) {
      switch (ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c]) {
        case C_DIGIT:
          return c - '0';
        case C_LOWER_LETTER:
          return 10 + c - 'a';
        case C_UPPER_LETTER:
          return 10 + c - 'A';
      }
    }
    return Character.getNumericValue(c);
  }

  public static int getHexadecimalValue(char c) {
    switch (c) {
      case '0':
        return 0;
      case '1':
        return 1;
      case '2':
        return 2;
      case '3':
        return 3;
      case '4':
        return 4;
      case '5':
        return 5;
      case '6':
        return 6;
      case '7':
        return 7;
      case '8':
        return 8;
      case '9':
        return 9;
      case 'A':
      case 'a':
        return 10;
      case 'B':
      case 'b':
        return 11;
      case 'C':
      case 'c':
        return 12;
      case 'D':
      case 'd':
        return 13;
      case 'E':
      case 'e':
        return 14;
      case 'F':
      case 'f':
        return 15;
      default:
        return -1;
    }
  }

  public static char toUpperCase(char c) {
    if (c < MAX_OBVIOUS) {
      if ((ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_UPPER_LETTER) != 0) {
        return c;
      } else if ((ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_LOWER_LETTER)
          != 0) {
        return (char) (c - 32);
      }
    }
    return Character.toUpperCase(c);
  }

  public static char toLowerCase(char c) {
    if (c < MAX_OBVIOUS) {
      if ((ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_LOWER_LETTER) != 0) {
        return c;
      } else if ((ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_UPPER_LETTER)
          != 0) {
        return (char) (32 + c);
      }
    }
    return Character.toLowerCase(c);
  }

  public static boolean isLowerCase(char c) {
    if (c < MAX_OBVIOUS) {
      return (ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_LOWER_LETTER) != 0;
    }
    return Character.isLowerCase(c);
  }

  public static boolean isUpperCase(char c) {
    if (c < MAX_OBVIOUS) {
      return (ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_UPPER_LETTER) != 0;
    }
    return Character.isUpperCase(c);
  }

  /**
   * Include also non JLS whitespaces.
   *
   * <p>return true if Character.isWhitespace(c) would return true
   */
  public static boolean isWhitespace(char c) {
    if (c < MAX_OBVIOUS) {
      return (ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c] & ScannerHelper.C_SPACE) != 0;
    }
    return Character.isWhitespace(c);
  }

  public static boolean isLetter(char c) {
    if (c < MAX_OBVIOUS) {
      return (ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c]
              & (ScannerHelper.C_UPPER_LETTER | ScannerHelper.C_LOWER_LETTER))
          != 0;
    }
    return Character.isLetter(c);
  }

  public static boolean isLetterOrDigit(char c) {
    if (c < MAX_OBVIOUS) {
      return (ScannerHelper.OBVIOUS_IDENT_CHAR_NATURES[c]
              & (ScannerHelper.C_UPPER_LETTER
                  | ScannerHelper.C_LOWER_LETTER
                  | ScannerHelper.C_DIGIT))
          != 0;
    }
    return Character.isLetterOrDigit(c);
  }

  private static final boolean isBitSet(long[] values, int i) {
    try {
      return (values[i / 64] & Bits[i % 64]) != 0;
    } catch (NullPointerException e) {
      return false;
    }
  }

  private static boolean isJavaIdentifierPart0(int codePoint, long[][][] tables) {
    return isJavaIdentifierPart0(codePoint, tables, false);
  }

  private static boolean isJavaIdentifierPart0(
      int codePoint, long[][][] tables, boolean isJava15orAbove) {
    switch ((codePoint & 0x1F0000) >> 16) {
      case 0:
        return isBitSet(tables[PART_INDEX][0], codePoint & 0xFFFF);
      case 1:
        return isBitSet(tables[PART_INDEX][1], codePoint & 0xFFFF);
      case 2:
        return isBitSet(tables[PART_INDEX][2], codePoint & 0xFFFF);
      case 3:
        if (isJava15orAbove) {
          return isBitSet(tables[PART_INDEX][3], codePoint & 0xFFFF);
        }
        return false;
      case 14:
        if (isJava15orAbove) {
          return isBitSet(tables[PART_INDEX][4], codePoint & 0xFFFF);
        }
        return isBitSet(tables[PART_INDEX][3], codePoint & 0xFFFF);
    }
    return false;
  }

  private static boolean isJavaIdentifierStart0(int codePoint, long[][][] tables) {
    return isJavaIdentifierStart0(codePoint, tables, false);
  }

  private static boolean isJavaIdentifierStart0(
      int codePoint, long[][][] tables, boolean isJava15orAbove) {
    switch ((codePoint & 0x1F0000) >> 16) {
      case 0:
        return isBitSet(tables[START_INDEX][0], codePoint & 0xFFFF);
      case 1:
        return isBitSet(tables[START_INDEX][1], codePoint & 0xFFFF);
      case 2:
        return isBitSet(tables[START_INDEX][2], codePoint & 0xFFFF);
      case 3:
        if (isJava15orAbove) return isBitSet(tables[START_INDEX][3], codePoint & 0xFFFF);
        return false;
    }
    return false;
  }

  static void initializeTable() {
    Tables = initializeTables("unicode"); // $NON-NLS-1$
  }

  static void initializeTable17() {
    Tables7 = initializeTables("unicode6"); // $NON-NLS-1$
  }

  static void initializeTable18() {
    Tables8 = initializeTables("unicode6_2"); // $NON-NLS-1$
  }

  static void initializeTable19() {
    Tables9 = initializeTables("unicode8"); // $NON-NLS-1$
  }

  static void initializeTableJava11() {
    Tables11 = initializeTables("unicode10"); // $NON-NLS-1$
  }

  static void initializeTableJava12() {
    Tables12 = initializeTables("unicode11"); // $NON-NLS-1$
  }

  static void initializeTableJava13() {
    Tables13 = initializeTables("unicode12_1"); // $NON-NLS-1$
  }

  static void initializeTableJava15() {
    Tables15 = initializeTables13andPlus("unicode13"); // $NON-NLS-1$
  }

  static long[][][] initializeTables(String unicode_path) {
    long[][][] tempTable = new long[2][][];
    tempTable[START_INDEX] = new long[3][];
    tempTable[PART_INDEX] = new long[4][];
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/start0.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[START_INDEX][0] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/start1.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[START_INDEX][1] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/start2.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[START_INDEX][2] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/part0.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[PART_INDEX][0] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/part1.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[PART_INDEX][1] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/part2.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[PART_INDEX][2] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/part14.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[PART_INDEX][3] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tempTable;
  }

  static long[][][] initializeTables13andPlus(String unicode_path) {
    long[][][] tempTable = new long[2][][];
    tempTable[START_INDEX] = new long[4][];
    tempTable[PART_INDEX] = new long[5][];
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/start0.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[START_INDEX][0] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/start1.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[START_INDEX][1] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/start2.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[START_INDEX][2] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/start3.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[START_INDEX][3] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/part0.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[PART_INDEX][0] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/part1.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[PART_INDEX][1] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/part2.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[PART_INDEX][2] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/part3.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[PART_INDEX][3] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (DataInputStream inputStream =
        new DataInputStream(
            new BufferedInputStream(
                ScannerHelper.class.getResourceAsStream(
                    unicode_path + "/part14.rsc")))) { // $NON-NLS-1$
      long[] readValues = new long[1024];
      for (int i = 0; i < 1024; i++) {
        readValues[i] = inputStream.readLong();
      }
      tempTable[PART_INDEX][4] = readValues;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tempTable;
  }

  public static class InvalidInputException extends RuntimeException {}
}
