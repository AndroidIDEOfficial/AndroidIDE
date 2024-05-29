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

package com.itsaky.androidide.inflater

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.itsaky.androidide.inflater.internal.utils.defaultGravity
import com.itsaky.androidide.inflater.internal.utils.unknownDrawable

/**
 * Abstract class which provides access to the internal parsing utility methods to its subclasses.
 *
 * @author Akash Yadav
 */
abstract class AbstractParser {

  /**
   * Parses the given string value representing an ID resource.
   *
   * @param value The string value. Usually value from attributes.
   * @param def The default value.
   */
  @JvmOverloads
  protected open fun parseId(resName: String, value: String, def: Int = 0): Int {
    return com.itsaky.androidide.inflater.internal.utils.parseId(resName, value, def)
  }

  /**
   * Parses the given string value representing a float to its actual value.
   *
   * @param value The string value. Usually value from attributes.
   * @param def The default value.
   */
  @JvmOverloads
  protected open fun parseFloat(value: String, def: Float = 0f): Float {
    return com.itsaky.androidide.inflater.internal.utils.parseFloat(value = value, def = def)
  }

  /**
   * Parses the given string value representing a long to its actual value.
   *
   * @param value The string value. Usually value from attributes.
   * @param def The default value.
   */
  @JvmOverloads
  protected open fun parseLong(value: String, def: Long = 0L): Long {
    return com.itsaky.androidide.inflater.internal.utils.parseLong(value = value, def = def)
  }

  /**
   * Parses the given string value representing an integer or reference to an integer resource to
   * its actual value.
   *
   * @param value The string value. Usually value from attributes.
   * @param def The default value.
   */
  @JvmOverloads
  protected open fun parseInteger(value: String, def: Int = 0): Int {
    return com.itsaky.androidide.inflater.internal.utils.parseInteger(value = value, def = def)
  }

  /**
   * Parses the given string value representing a reference to an integer array resource to its
   * actual value. Returns an empty array if the resource reference cannot be resolved.
   *
   * @param value The string value. Usually value from attributes.
   */
  protected open fun parseIntegerArray(value: String): IntArray {
    return com.itsaky.androidide.inflater.internal.utils.parseIntegerArray(value) ?: intArrayOf()
  }

  /**
   * Parses the given string value representing a boolean or reference to an boolean resource to its
   * actual value.
   *
   * @param value The string value. Usually value from attributes.
   * @param def The default value.
   */
  @JvmOverloads
  protected open fun parseBoolean(value: String, def: Boolean = false): Boolean {
    return com.itsaky.androidide.inflater.internal.utils.parseBoolean(value = value, def = def)
  }

  /**
   * Parses the given string value representing a string or reference to a string resource to its
   * actual value. Returns [value] itself if it cannot be parsed.
   *
   * @param value The string value. Usually value from attributes.
   */
  protected open fun parseString(value: String): String {
    return com.itsaky.androidide.inflater.internal.utils.parseString(value)
  }

  /**
   * Parses the given string value representing a reference to a string array resource to its actual
   * value. Returns an empty array if the resource reference cannot be resolved.
   *
   * @param value The string value. Usually value from attributes.
   */
  protected open fun parseStringArray(value: String): Array<String> {
    return com.itsaky.androidide.inflater.internal.utils.parseStringArray(value) ?: emptyArray()
  }

  /**
   * Parses the given string value representing a color or reference to a drawable resource to an
   * actual drawable which can be rendered.
   *
   * @param value The string value. Usually value from attributes.
   * @param def The default value.
   */
  @JvmOverloads
  protected open fun parseDrawable(
    context: Context,
    value: String,
    def: Drawable = unknownDrawable()
  ): Drawable {
    return com.itsaky.androidide.inflater.internal.utils.parseDrawable(
      context = context,
      value = value,
      def = def
    )
  }

  /**
   * Parses the gravity flags which can be single flag value like `center` or a multiple combined
   * flag values like `start|top`.
   *
   * @param value The gravity string.
   * @param def The default gravity flag value.
   */
  @JvmOverloads
  protected open fun parseGravity(value: String, def: Int = defaultGravity()): Int {
    return com.itsaky.androidide.inflater.internal.utils.parseGravity(value = value, def = def)
  }

  /**
   * Parses the given string value representing a dimension value or reference to a dimension
   * resource to its actual value.
   *
   * @param value The string value. Usually value from attributes.
   * @param def The default value.
   */
  @JvmOverloads
  protected open fun parseDimension(context: Context, value: String, def: Int = 0): Int {
    return parseDimensionF(context = context, value = value, def = def.toFloat()).toInt()
  }

  /**
   * Parses the given string value representing a dimension value or reference to a dimension
   * resource to its actual value as a float point number.
   *
   * @param value The string value. Usually value from attributes.
   * @param def The default value.
   */
  @JvmOverloads
  protected open fun parseDimensionF(context: Context, value: String, def: Float = 0f): Float {
    return com.itsaky.androidide.inflater.internal.utils.parseDimension(
      context = context,
      value = value,
      def = def
    )
  }

  /**
   * Parses the given string value representing a color code or reference to a color resource to its
   * actual value.
   *
   * @param value The string value. Usually value from attributes.
   * @param def The default value.
   */
  @JvmOverloads
  protected open fun parseColor(
    context: Context,
    value: String,
    def: Int = Color.TRANSPARENT
  ): Int {
    return com.itsaky.androidide.inflater.internal.utils.parseColor(
      context = context,
      value = value,
      def = def
    )
  }

  /**
   * Parses the given string value representing a color code or reference to a color state list
   * resource to its actual value.
   *
   * @param value The string value. Usually value from attributes.
   * @param def The default value.
   */
  @JvmOverloads
  protected open fun parseColorStateList(
    context: Context,
    value: String,
    def: ColorStateList = ColorStateList.valueOf(Color.TRANSPARENT)
  ): ColorStateList {
    return com.itsaky.androidide.inflater.internal.utils.parseColorStateList(
      context = context,
      value = value,
      def = def
    )
  }

  /**
   * Parses the given string value representing a date to miliseconds.
   *
   * @param value The string value. Usually value from attributes.
   * @param format The date format for [java.text.SimpleDateFormat].
   * @param def The default value.
   */
  @JvmOverloads
  fun parseDate(value: String, format: String = "MM/dd/yyyy", def: Long = 0L): Long {
    return com.itsaky.androidide.inflater.internal.utils.parseDate(
      value = value,
      format = format,
      def = def
    )
  }
}
