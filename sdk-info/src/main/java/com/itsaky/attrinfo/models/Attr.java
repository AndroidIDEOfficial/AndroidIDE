/*
 * This file is part of AndroidIDE.
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
 */
package com.itsaky.attrinfo.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.xml.INamespace;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Represents an xml attribute which is read from {@code attrs.xml}.
 *
 * @author Akash Yadav
 * @see com.itsaky.attrinfo.AttrInfo
 */
public class Attr implements Parcelable {

  public static final int REFERENCE = 1;
  public static final int COLOR = 1 << 1;
  public static final int BOOLEAN = 1 << 2;
  public static final int DIMENSION = 1 << 3;
  public static final int FLOAT = 1 << 4;
  public static final int INTEGER = 1 << 5;
  public static final int FRACTION = 1 << 6;
  public static final int STRING = 1 << 7;
  public static final int ENUM = 1 << 8;
  public static final int FLAG = 1 << 9;
  public static final int UNKNOWN = 1 << 10;

  public static final String FORMAT_REFERENCE = "reference";
  public static final String FORMAT_COLOR = "color";
  public static final String FORMAT_BOOLEAN = "boolean";
  public static final String FORMAT_DIMENSION = "dimension";
  public static final String FORMAT_FLOAT = "float";
  public static final String FORMAT_INTEGER = "integer";
  public static final String FORMAT_FRACTION = "fraction";
  public static final String FORMAT_STRING = "string";
  public static final String FORMAT_ENUM = "enum";
  public static final String FORMAT_FLAG = "flag";
  public static final Creator<Attr> CREATOR =
      new Creator<>() {
        @Override
        public Attr createFromParcel(Parcel in) {
          return new Attr(in);
        }

        @Override
        public Attr[] newArray(int size) {
          return new Attr[size];
        }
      };
  private static final ILogger LOG = ILogger.newInstance("AttrInfo::Attr");
  public INamespace namespace;
  public String name;
  public Set<String> possibleValues;
  public int format;

  public Attr(String name) {
    this(name, INamespace.ANDROID);
  }

  public Attr(String name, INamespace namespace) {
    this.name = name;
    this.namespace = namespace;
    this.possibleValues = new TreeSet<>();
  }

  protected Attr(@NonNull Parcel in) {
    this.namespace = in.readParcelable(INamespace.class.getClassLoader());
    this.name = in.readString();
    this.format = in.readInt();

    final var list = new ArrayList<String>();
    in.readStringList(list);
    this.possibleValues = new TreeSet<>(list);
  }

  public static int formatForName(@NonNull String names) {
    var result = 0;
    if (names.contains("|")) {
      for (var name : names.split(Pattern.quote("|"))) {
        result |= formatForSingleName(name);
      }
    } else {
      result = formatForSingleName(names);
    }
    return result;
  }

  @Contract(pure = true)
  public static int formatForSingleName(@NonNull String name) {
    switch (name) {
      case FORMAT_REFERENCE:
        return REFERENCE;
      case FORMAT_COLOR:
        return COLOR;
      case FORMAT_BOOLEAN:
        return BOOLEAN;
      case FORMAT_DIMENSION:
        return DIMENSION;
      case FORMAT_FLOAT:
        return FLOAT;
      case FORMAT_INTEGER:
        return INTEGER;
      case FORMAT_FRACTION:
        return FRACTION;
      case FORMAT_STRING:
        return STRING;
      case FORMAT_ENUM:
        return ENUM;
      case FORMAT_FLAG:
        return FLAG;
    }

    return UNKNOWN;
  }

  public static String createFormatText(int format) {
    final var formats = new ArrayList<String>();
    if ((format & REFERENCE) != 0) {
      formats.add(FORMAT_REFERENCE);
    }

    if ((format & COLOR) != 0) {
      formats.add(FORMAT_COLOR);
    }

    if ((format & BOOLEAN) != 0) {
      formats.add(FORMAT_BOOLEAN);
    }

    if ((format & DIMENSION) != 0) {
      formats.add(FORMAT_DIMENSION);
    }

    if ((format & FLOAT) != 0) {
      formats.add(FORMAT_FLOAT);
    }

    if ((format & INTEGER) != 0) {
      formats.add(FORMAT_INTEGER);
    }

    if ((format & FRACTION) != 0) {
      formats.add(FORMAT_FRACTION);
    }

    if ((format & STRING) != 0) {
      formats.add(FORMAT_STRING);
    }

    if ((format & ENUM) != 0) {
      formats.add(FORMAT_ENUM);
    }

    if ((format & FLAG) != 0) {
      formats.add(FORMAT_FLAG);
    }

    if (formats.isEmpty()) {
      formats.add("unknown");
    }

    return TextUtils.join("|", formats);
  }

  public boolean hasPossibleValues() {
    return possibleValues != null && possibleValues.size() > 0;
  }

  public boolean hasFormat(int format) {
    return (this.format & format) != 0;
  }

  @NonNull
  @Override
  public String toString() {
    return "Attr ["
        + "  name: "
        + name
        + "\n"
        + "  namespace: "
        + namespace
        + "\n"
        + "  values: "
        + TextUtils.join(", ", this.possibleValues)
        + "\n"
        + "  format: "
        + format
        + "\n"
        + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Attr attr = (Attr) o;
    return format == attr.format
        && Objects.equals(namespace, attr.namespace)
        && Objects.equals(name, attr.name)
        && Objects.equals(possibleValues, attr.possibleValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(namespace, name, possibleValues, format);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(@NonNull Parcel dest, int flags) {
    dest.writeParcelable(namespace, flags);
    dest.writeString(name);
    dest.writeInt(format);
    dest.writeStringList(new ArrayList<>(this.possibleValues));
  }
}
