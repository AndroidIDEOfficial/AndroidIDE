/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.inflater.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.drawable.DrawableParserFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Contains methods which are commonly used by layout inflater, attribute adapters and drawable
 * parsers.
 *
 * @author Akash Yadav
 */
public class CommonParseUtils {

  private static final Pattern HEX_COLOR = Pattern.compile("#[a-fA-F0-9]{6,8}");

  protected IResourceTable resourceFinder;
  protected final DisplayMetrics displayMetrics;

  private static final ILogger LOG = ILogger.newInstance("CommonParseUtils");

  public CommonParseUtils(@NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
    this.resourceFinder = resourceFinder;
    this.displayMetrics = displayMetrics;
  }

  protected String[] parseArray(final String value) {
    if (value.startsWith("@android:array/")) {
      final var name = value.substring("@android:array/".length());
      return frameworkArrayValue(name);
    } else if (value.startsWith("@array/")) {
      final var name = value.substring("@array/".length());
      return resourceFinder.findArray(name);
    } else {
      return new String[] {value};
    }
  }

  private String[] frameworkArrayValue(String name) {
    try {
      final var id = findFrameworkResourceId("array", name);
      if (id == -1) {
        return new String[] {name};
      }

      return BaseApplication.getBaseInstance().getResources().getStringArray(id);
    } catch (Throwable th) {
      return new String[0];
    }
  }

  protected String parseString(@NonNull String value) {
    if (value.startsWith("@string/")) {
      return parseString(resourceFinder.findString(value.substring("@string/".length())));
    } else if (value.startsWith("@android:string/")) {
      return frameworkStringValue(value.substring("@android:string/".length()));
    } else {
      return value;
    }
  }

  private String frameworkStringValue(String name) {
    try {
      final var id = findFrameworkResourceId("string", name);
      if (id == -1) {
        return "";
      }

      return BaseApplication.getBaseInstance().getString(id);
    } catch (Throwable th) {
      return "";
    }
  }

  protected boolean parseBoolean(@NonNull String value) {
    if (!value.startsWith("@")) {
      if ("true".equals(value)) {
        return true;
      } else if ("false".equals(value)) {
        return false;
      }
    } else if (value.startsWith("@bool/")) {
      return parseBoolean(resourceFinder.findBoolean(value.substring("@bool/".length())));
    } else if (value.startsWith("@android:bool/")) {
      final var name = value.substring("@android:bool/".length());
      return frameworkBooleanValue(name);
    }
    return false;
  }

  private boolean frameworkBooleanValue(String name) {
    try {
      final var id = findFrameworkResourceId("boolean", name);
      if (id == -1) {
        return false;
      }

      return BaseApplication.getBaseInstance().getResources().getBoolean(id);
    } catch (Throwable th) {
      return false;
    }
  }

  protected int parseInteger(@NonNull String value, int defaultVal) {
    if (value.startsWith("@integer/")) {
      return parseInteger(
          resourceFinder.findInteger(value.substring("@integer/".length())), defaultVal);
    } else if (value.startsWith("@android:integer/")) {
      final var name = value.substring("@android:integer/".length());
      return frameworkIntegerResource(name, defaultVal);
    } else {
      try {
        return Integer.parseInt(value);
      } catch (Throwable th) {
        return defaultVal;
      }
    }
  }

  private int frameworkIntegerResource(String name, int defaultVal) {
    try {
      final var id = findFrameworkResourceId("integer", name);
      if (id == -1) {
        return defaultVal;
      }

      return BaseApplication.getBaseInstance().getResources().getInteger(id);
    } catch (Throwable th) {
      return defaultVal;
    }
  }

  protected int parseDimension(final String value, int defaultValue) {
    if (value == null || value.isEmpty()) {
      return defaultValue;
    }

    char c = value.charAt(0);
    if (Character.isDigit(c)) {
      // A dimension value which starts with a digit. E.g.: 1dp, 12sp, 123px, etc.
      StringBuilder dimensionVal = new StringBuilder();
      int index = 0;
      while (Character.isDigit(c = value.charAt(index))) {
        dimensionVal.append(c);
        index++;
      }

      final int dimen = Integer.parseInt(dimensionVal.toString());
      final String dimensionType = value.substring(index);
      return (int)
          TypedValue.applyDimension(getUnitForDimensionType(dimensionType), dimen, displayMetrics);
    } else if (c == '@') {
      if (value.startsWith("@android:dimen/")) {
        final var name = value.substring("@android:dimen/".length());
        return frameworkDimensionValue(name, defaultValue);
      } else if (value.startsWith("@dimen/")) {
        String name = value.substring("@dimen/".length());
        return parseDimension(resourceFinder.findDimension(name), defaultValue);
      }
    } else if (Character.isLetter(c)) {
      // This could be one of the following :
      // 1. match_parent
      // 2. wrap_content
      // 3. fill_parent
      switch (value) {
        case "match_parent":
        case "fill_parent":
          return ViewGroup.LayoutParams.MATCH_PARENT;
        case "wrap_content":
        default:
          return ViewGroup.LayoutParams.WRAP_CONTENT;
      }
    }

    return defaultValue;
  }

  private int frameworkDimensionValue(String name, int defValue) {
    try {
      final var id = findFrameworkResourceId("dimen", name);
      if (id == -1) {
        return defValue;
      }

      return (int) BaseApplication.getBaseInstance().getResources().getDimension(id);
    } catch (Throwable th) {
      return defValue;
    }
  }

  protected int getUnitForDimensionType(@NonNull String dimensionType) {

    switch (dimensionType) {
      case "dp":
        return TypedValue.COMPLEX_UNIT_DIP;
      case "sp":
        return TypedValue.COMPLEX_UNIT_SP;
      case "px":
        return TypedValue.COMPLEX_UNIT_PX;
      case "pt":
        return TypedValue.COMPLEX_UNIT_PT;
      case "in":
        return TypedValue.COMPLEX_UNIT_IN;
      case "mm":
        return TypedValue.COMPLEX_UNIT_MM;
    }

    return TypedValue.COMPLEX_UNIT_DIP;
  }

  protected int parseColor(String color, final Context ctx) {
    if (HEX_COLOR.matcher(color).matches()) {
      try {
        return Color.parseColor(color);
      } catch (Throwable th) {
        // Ignored
      }
    } else if (color.startsWith("@color/")) {
      return parseColor(resourceFinder.findColor(color.substring("@color/".length())), ctx);
    } else if (color.startsWith("@android:color/")) {
      final int id = findFrameworkResourceId("color", color.substring("@android:color/".length()));
      return ContextCompat.getColor(ctx, id);
    }

    return Color.parseColor("#00ffffff");
  }

  protected ColorStateList parseColorStateList(@NonNull String value, Context context) {
    if (HEX_COLOR.matcher(value).matches()) {
      return ColorStateList.valueOf(Color.parseColor(value));
    } else if (value.startsWith("@color/")) {
      final var name = value.substring("@color/".length());
      final var res = resourceFinder.findColor(name);
      final var color = parseColor(res, context);
      return ColorStateList.valueOf(color);
    } else if (value.startsWith("@android:color/")) {
      final var name = value.substring("@android:color/".length());
      final var id = findFrameworkResourceId("color", name);
      if (id == -1) {
        return ColorStateList.valueOf(Color.TRANSPARENT);
      }
      try {
        return ContextCompat.getColorStateList(context, id);
      } catch (Throwable th) {
        try {
          return ColorStateList.valueOf(ContextCompat.getColor(context, id));
        } catch (Throwable th2) {
          LOG.error("Unable to create color state list for framework resource", name);
          return ColorStateList.valueOf(Color.TRANSPARENT);
        }
      }
    }

    return ColorStateList.valueOf(Color.TRANSPARENT);
  }

  protected Drawable parseDrawable(String value, final Context ctx) {
    if (HEX_COLOR.matcher(value).matches()) {
      return drawableForColor(value);
    } else if (value.startsWith("@")) {

      // First check if this is a reference to an android resource
      if (value.startsWith("@android:")) {
        final String typeAndValue = value.substring("@android:".length());
        final String[] split =
            typeAndValue.split(
                Pattern.quote("/")); // For @android:color/white, it will be ["color",
        // "white"]
        final String type = split[0];
        final String typeVal = split[1];
        final int id = findFrameworkResourceId(type, typeVal);

        if (id != -1) {
          switch (type) {
            case "color":
              return drawableForColor(ContextCompat.getColor(ctx, id));
            case "drawable":
              return ContextCompat.getDrawable(ctx, id);
            default:
              return newTransparentDrawable();
          }
        }

      } else {
        // We found a reference to another resource
        if (value.startsWith("@drawable/")) {
          final File drawable = resourceFinder.findDrawable(value.substring("@drawable/".length()));

          if (drawable == null) {
            return null;
          }

          try {
            final var parser = DrawableParserFactory.newParser(ctx, drawable, resourceFinder);
            if (parser == null) {
              return null;
            }
            return parser.parse();
          } catch (Exception e) {
            LOG.error("Error parsing drawable", e);
            return null;
          }

        } else if (value.startsWith("@color/")) {
          final String color = resourceFinder.findColor(value.substring("@color/".length()));
          return new ColorDrawable(parseColor(color, ctx));
        }
      }
    }
    return newTransparentDrawable();
  }

  private int findFrameworkResourceId(String type, String name) {
    try {
      final Class<?> typeClass =
          Objects.requireNonNull(
                  android.R.class.getClassLoader(),
                  "Unable to get class loader for loading system resources")
              .loadClass("android.R$" + type);
      final Field typeField = typeClass.getDeclaredField(name);
      typeField.setAccessible(true);
      return typeField.getInt(null);
    } catch (Throwable th) {
      LOG.error("Unable to find framework resource.", "type=" + type, "name=" + name);
      return -1;
    }
  }

  protected Drawable drawableForColor(String color) {
    try {
      return drawableForColor(Color.parseColor(color));
    } catch (Throwable th) {
      return newTransparentDrawable();
    }
  }

  protected Drawable drawableForColor(int color) {
    return new ColorDrawable(color);
  }

  protected Drawable newTransparentDrawable() {
    return new ColorDrawable(Color.TRANSPARENT);
  }

  protected int parseGravity(@NonNull String value) {
    final String[] splits = value.split(Pattern.quote("|"));
    int result = -1;
    for (String split : splits) {
      final int gravity = gravityFor(split);
      if (result == -1) {
        result = gravity;
      } else {
        result |= gravity;
      }
    }
    return result;
  }

  protected int gravityFor(@NonNull String gravity) {
    switch (gravity) {
      case "center":
        return Gravity.CENTER;
      case "center_vertical":
        return Gravity.CENTER_VERTICAL;
      case "center_horizontal":
        return Gravity.CENTER_HORIZONTAL;
      case "left":
        return Gravity.LEFT;
      case "right":
        return Gravity.RIGHT;
      case "top":
        return Gravity.TOP;
      case "bottom":
        return Gravity.BOTTOM;
      case "start":
        return GravityCompat.START;
      case "end":
        return GravityCompat.END;
      default:
        return Gravity.TOP | Gravity.START;
    }
  }
}
