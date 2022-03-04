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
package com.itsaky.inflater;

/**
 * Value type of {@link IAttribute}
 *
 * @author Akash Yadav
 */
public enum AttrType {

  /** Refers to another resource */
  REFERENCE("reference"),

  /** Color value */
  COLOR("color"),

  /** Either {@code true} or {@code false} */
  BOOLEAN("boolean"),

  /** A dimension like 10dp, 14sp etc. */
  DIMENSION("dimension"),

  /** A floating point number. E.g.: 1.9, 123.45 */
  FLOAT("float"),

  /** An integer value */
  INTEGER("integer"),

  /** A string value */
  STRING("string"),

  /** A fraction */
  FRACTION("fraction"),

  /** A enum value. Unlike {@link #FLAG}, an attribute can have only one of this. */
  ENUM("enum"),

  /** A flag value. May contain on or more values */
  FLAG("flag"),

  /** An unknown value. The layout inflater does not consider this */
  UNKNOWN("");

  private final String format;

  public static final String FORMAT_REF = REFERENCE.getFormat();
  public static final String FORMAT_COLOR = COLOR.getFormat();
  public static final String FORMAT_BOOL = BOOLEAN.getFormat();
  public static final String FORMAT_DIMEN = DIMENSION.getFormat();
  public static final String FORMAT_FLOAT = FLOAT.getFormat();
  public static final String FORMAT_INT = INTEGER.getFormat();
  public static final String FORMAT_STRING = STRING.getFormat();
  public static final String FORMAT_FRACTION = FRACTION.getFormat();
  public static final String FORMAT_ENUM = ENUM.getFormat();
  public static final String FORMAT_FLAG = FLAG.getFormat();

  private AttrType(String format) {
    this.format = format;
  }

  public String getFormat() {
    return this.format;
  }

  public boolean hasFixedValues() {
    return this == AttrType.ENUM || this == AttrType.FLAG;
  }

  /**
   * Get the attribute type from format
   *
   * @param format Format, as declared in styleables.
   * @return The Attribute type
   */
  public static AttrType fromFormat(String format) {
    if (FORMAT_REF.equals(format)) {
      return AttrType.REFERENCE;
    } else if (FORMAT_COLOR.equals(format)) {
      return AttrType.COLOR;
    } else if (FORMAT_BOOL.equals(format)) {
      return AttrType.BOOLEAN;
    } else if (FORMAT_DIMEN.equals(format)) {
      return AttrType.DIMENSION;
    } else if (FORMAT_FLOAT.equals(format)) {
      return AttrType.FLOAT;
    } else if (FORMAT_INT.equals(format)) {
      return AttrType.INTEGER;
    } else if (FORMAT_STRING.equals(format)) {
      return AttrType.STRING;
    } else if (FORMAT_FRACTION.equals(format)) {
      return AttrType.FRACTION;
    } else if (FORMAT_ENUM.equals(format)) {
      return AttrType.ENUM;
    } else if (FORMAT_FLAG.equals(format)) {
      return AttrType.FLAG;
    }

    return AttrType.UNKNOWN;
  }
}
