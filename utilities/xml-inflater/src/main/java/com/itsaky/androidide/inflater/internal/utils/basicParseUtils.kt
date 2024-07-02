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

package com.itsaky.androidide.inflater.internal.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue.complexToDimension
import android.view.Gravity
import android.view.InflateException
import android.view.ViewGroup.LayoutParams
import androidx.core.text.isDigitsOnly
import com.android.SdkConstants.EXT_XML
import com.android.aaptcompiler.AaptResourceType
import com.android.aaptcompiler.AaptResourceType.ARRAY
import com.android.aaptcompiler.AaptResourceType.ATTR
import com.android.aaptcompiler.AaptResourceType.BOOL
import com.android.aaptcompiler.AaptResourceType.COLOR
import com.android.aaptcompiler.AaptResourceType.DIMEN
import com.android.aaptcompiler.AaptResourceType.INTEGER
import com.android.aaptcompiler.AaptResourceType.LAYOUT
import com.android.aaptcompiler.AaptResourceType.STRING
import com.android.aaptcompiler.ArrayResource
import com.android.aaptcompiler.AttributeResource
import com.android.aaptcompiler.BasicString
import com.android.aaptcompiler.BinaryPrimitive
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.FileReference
import com.android.aaptcompiler.RawString
import com.android.aaptcompiler.Reference
import com.android.aaptcompiler.ResourceEntry
import com.android.aaptcompiler.ResourceName
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.ResourceTablePackage
import com.android.aaptcompiler.StyledString
import com.android.aaptcompiler.Value
import com.android.aaptcompiler.android.ResValue.DataType.DIMENSION
import com.android.aaptcompiler.android.stringToFloat
import com.android.aaptcompiler.tryParseBool
import com.android.aaptcompiler.tryParseFlagSymbol
import com.android.aaptcompiler.tryParseInt
import com.android.aaptcompiler.tryParseReference
import com.itsaky.androidide.inflater.drawable.DrawableParserFactory
import com.itsaky.androidide.inflater.utils.module
import com.itsaky.androidide.xml.res.IResourceEntry
import com.itsaky.androidide.xml.res.IResourceTable
import com.itsaky.androidide.xml.res.IResourceTablePackage
import org.slf4j.LoggerFactory
import java.io.File
import java.text.SimpleDateFormat

private val log = LoggerFactory.getLogger("ParseUtilsKt")

// TODO : We need a more descriptive string here
private const val DEFAULT_STRING_VALUE = "AndroidIDE"

private val stringResolver =
  fun(it: Value?): String? {
    return when (it) {
      is com.android.aaptcompiler.BasicString -> it.ref.value()
      is com.android.aaptcompiler.RawString -> it.value.value()
      is com.android.aaptcompiler.StyledString -> it.ref.value()
      else -> null
    }
  }

private val intResolver =
  fun(it: Value?): Int? {
    return if (it is com.android.aaptcompiler.BinaryPrimitive) {
      it.resValue.data
    } else null
  }

val colorResolver: (Value?) -> Int? =
  fun(it): Int? {
    // TODO(itsaky) : Implement color state list parser
    if (it is com.android.aaptcompiler.BinaryPrimitive) {
      return it.resValue.data
    }
    return null
  }

inline fun <reified T> ((Value?) -> T?).arrayResolver(value: Value?): Array<T>? {
  return if (value is com.android.aaptcompiler.ArrayResource) {
    Array(value.elements.size) { invoke(value.elements[it]) ?: return null }
  } else emptyArray()
}

fun parseString(value: String): String {
  if (value.isEmpty()) {
    return DEFAULT_STRING_VALUE
  }
  if (value[0] == '@') {
    return parseReference(
      value = value,
      expectedType = STRING,
      def = value,
      resolver = stringResolver
    )
  }
  return value
}

fun parseStringArray(value: String, def: Array<String>? = emptyArray()): Array<String>? {
  return parseArray(value = value, def = def, resolver = stringResolver)
}

fun parseIntegerArray(value: String, def: IntArray? = intArrayOf()): IntArray? {
  return parseArray(value = value, def = def?.toTypedArray(), resolver = intResolver)?.toIntArray()
}

@JvmOverloads
inline fun <reified T> parseArray(
  value: String,
  def: Array<T>? = emptyArray(),
  noinline resolver: (Value?) -> T?
): Array<T>? {
  if (value.isEmpty()) {
    return emptyArray()
  }

  if (value[0] == '@') {
    return parseReference(
      value = value,
      expectedType = ARRAY,
      def = emptyArray(),
      resolver = resolver::arrayResolver
    )
  }
  return def
}

@JvmOverloads
fun parseInteger(value: String, def: Int = 0): Int {
  if (value.isEmpty()) {
    return def
  }
  if (value.isDigitsOnly()) {
    com.android.aaptcompiler.tryParseInt(value)?.resValue?.apply {
      return data
    }
  }

  if (value[0] == '@') {
    return parseReference(value, INTEGER, def, intResolver)
  }

  return def
}

@JvmOverloads
fun parseBoolean(value: String, def: Boolean = false): Boolean {
  if (value.isEmpty()) {
    return def
  }

  com.android.aaptcompiler.tryParseBool(value)?.resValue?.apply {
    return data == -1
  }

  if (value[0] == '@') {
    val resolver: (Value?) -> Boolean? =
      fun(resValue): Boolean {
        return if (resValue is com.android.aaptcompiler.BinaryPrimitive) {
          resValue.resValue.data == -1
        } else def
      }
    return parseReference(value, BOOL, def, resolver)
  }

  return def
}

@JvmOverloads
fun parseDrawable(context: Context, value: String, def: Drawable = unknownDrawable()): Drawable {
  val drawableResolver: (Value?) -> Drawable? =
    fun(it): Drawable? {
      if (it is com.android.aaptcompiler.FileReference) {
        val file = File(it.path.value())
        if (!file.exists() || file.extension != EXT_XML) {
          return null
        }
        val parser = DrawableParserFactory.newParser(context, file) ?: return null
        return parser.parse(context)
      }
      // TODO(itsaky) : Drawable of any type other than a file?

      // If this is a color int, return a color drawable
      return colorResolver.invoke(it)?.let { newColorDrawable(it) }
    }

  if (value.isEmpty()) {
    return def
  }

  if (value[0] == '#') {
    return parseColorDrawable(context, value)
  } else if (value[0] == '@') {
    val type = parseResourceReference(value)?.second ?: return def
    return parseReference(
      value = value,
      expectedType = type,
      def = def,
      resolver = drawableResolver
    )
  }
  return def
}

fun parseLayoutReference(value: String): File? {
  if (value.isEmpty() || value[0] != '@') {
    throw InflateException("Value must be a reference to a layout file")
  }

  val layoutResolver: (Value?) -> File? =
    fun(it): File? {
      return if (it is com.android.aaptcompiler.FileReference) {
        File(it.source.path)
      } else null
    }
  val type = parseResourceReference(value)?.second ?: return null
  if (type != LAYOUT) {
    log.warn("Layout file reference is expected but '{}' was found for value '{}'", type, value)
    return null
  }
  return parseReference(value, type, null, layoutResolver)
}

@JvmOverloads
fun parseColorDrawable(context: Context, value: String, def: Int = Color.TRANSPARENT): Drawable {
  return newColorDrawable(parseColor(context, value, def))
}

@JvmOverloads
fun parseColor(@Suppress("UNUSED_PARAMETER") context: Context, value: String,
  def: Int = Color.TRANSPARENT): Int {
  if (value.isEmpty()) {
    return def
  }
  when (value[0]) {
    '#' -> return parseHexColor(value, def)
    '@' ->
      return parseReference(
        value = value,
        expectedType = COLOR,
        def = def,
        resolver = colorResolver
      )
  }
  return def
}

fun parseHexColor(value: String, def: Int = Color.TRANSPARENT): Int {
  return try {
    Color.parseColor(value)
  } catch (err: Throwable) {
    log.warn("Unable to parse color code: {}", value)
    def
  }
}

fun unknownDrawable(): Drawable {
  return newColorDrawable(Color.TRANSPARENT)
}

fun newColorDrawable(color: Int): Drawable {
  return ColorDrawable(color)
}

fun parseColorStateList(
  context: Context,
  value: String,
  def: ColorStateList = ColorStateList.valueOf(Color.TRANSPARENT)
): ColorStateList {
  return ColorStateList.valueOf(parseColor(context, value, def = def.defaultColor))
}

@JvmOverloads
fun parseDimension(
  context: Context,
  value: String?,
  def: Float = LayoutParams.WRAP_CONTENT.toFloat(),
): Float {
  if (value.isNullOrBlank()) {
    return def
  }
  val displayMetrics = context.resources.displayMetrics
  val c = value[0]
  if (c.isDigit()) {
    val (dataType, data, _) = stringToFloat(value) ?: return def
    if (dataType != DIMENSION) {
      return def
    }

    return complexToDimension(data, displayMetrics)
  } else if (c == '@') {
    val resolver: (Value?) -> Float? = {
      if (it is com.android.aaptcompiler.BinaryPrimitive) {
        complexToDimension(it.resValue.data, displayMetrics)
      } else null
    }
    return parseReference(value, DIMEN, def, resolver)
  } else {
    return when (value) {
      "wrap_content" -> LayoutParams.WRAP_CONTENT.toFloat()
      "fill_parent",
      "match_parent",
      -> LayoutParams.MATCH_PARENT.toFloat()

      else -> {
        log.warn("Cannot infer type of dimension resource: '{}'", value)
        def
      }
    }
  }
}

fun parseFloat(value: String, def: Float): Float {
  return try {
    value.toFloat()
  } catch (err: Throwable) {
    def
  }
}

@JvmOverloads
fun parseLong(value: String, def: Long = 0L): Long {
  return try {
    value.toLong()
  } catch (err: Throwable) {
    def
  }
}

@JvmOverloads
fun parseGravity(value: String, def: Int = defaultGravity()): Int {
  if (value.isEmpty()) {
    return def
  }

  val attr = findAttributeResource("android", ATTR, "gravity") ?: return defaultGravity()
  return parseFlag(attr = attr, value = value, def = def)
}

fun parseFlag(attr: com.android.aaptcompiler.AttributeResource, value: String, def: Int = -1): Int {
  return com.android.aaptcompiler.tryParseFlagSymbol(attr, value)?.resValue?.data ?: def
}

fun defaultGravity(): Int {
  return Gravity.START or Gravity.TOP
}

fun <T> parseReference(
  value: String,
  expectedType: AaptResourceType,
  def: T,
  resolver: (Value?) -> T?
): T {
  val (pck, type, name) = parseResourceReference(value) ?: return def
  if (type != expectedType) {
    log.warn(
      "Reference of type '{}' is expected but '{}' was found for value '{}'",
      expectedType,
      type,
      value
    )
    return def
  }
  return if (pck.isNullOrBlank()) {
    resolveUnqualifiedResourceReference(
      type = type,
      name = name,
      value = value,
      def = def,
      resolver = resolver
    )
  } else {
    resolveQualifiedResourceReference(
      pck = pck,
      type = type,
      name = name,
      def = def,
      resolver = resolver
    )
  }
}

fun <T> resolveUnqualifiedResourceReference(
  type: AaptResourceType,
  name: String,
  value: String?,
  def: T,
  resolver: (Value?) -> T?
): T {
  val (table, _, pack, entry) = lookupUnqualifedResource(type, name, value) ?: return def
  return resolveResourceReference(
    table = table,
    pck = pack,
    entry = entry,
    type = type,
    name = name,
    def = def,
    resolver = resolver
  )
}

fun <T> resolveQualifiedResourceReference(
  pck: String,
  type: AaptResourceType,
  name: String,
  def: T,
  resolver: (Value?) -> T?
): T {
  val table =
    module.findResourceTableForPackage(pck, type)
      ?: throw IllegalArgumentException("Resource table for package '$pck' not found.")

  return resolveResourceReference(
    table = table,
    type = type,
    pck = pck,
    name = name,
    def = def,
    resolver = resolver
  )
}

fun <T> resolveResourceReference(
  table: IResourceTable,
  type: AaptResourceType,
  pck: String,
  name: String,
  def: T,
  resolver: (Value?) -> T?
): T {
  val result =
    table.findResource(com.android.aaptcompiler.ResourceName(pck = pck, type = type, entry = name))
      ?: run { throw IllegalArgumentException("$type resource '$name' not found") }
  return resolveResourceReference(
    table,
    result.tablePackage,
    result.entry,
    type,
    name,
    def,
    resolver
  )
}

fun <T> resolveResourceReference(
  table: IResourceTable,
  pck: IResourceTablePackage,
  entry: IResourceEntry,
  type: AaptResourceType,
  name: String,
  def: T,
  resolver: (Value?) -> T?
): T {
  val value = entry.findValue(ConfigDescription())!!.value
  if (value is Reference) {
    return resolveResourceReference(
      table = table,
      type = type,
      pck = pck.name,
      name = value.name.entry!!,
      def = def,
      resolver = resolver
    )
  }

  return resolver(value)
    ?: run {
      log.warn("Unable to resolve dimension reference '$name'")
      def
    }
}

internal fun parseResourceReference(value: String): Triple<String?, AaptResourceType, String>? {
  return tryParseReference(value)?.let {
    val pck = it.reference.name.pck
    val type = it.reference.name.type
    val name = it.reference.name.entry!!

    if (type == ATTR) {
      // this an attribute reference
      // TODO(itsaky): When the UI designer allows the user to choose a theme for the UI designer,
      //   This should resolve the referred attribute and then return its pck, type and name
      return null
    }
    return Triple(pck, type, name)
  }
}

@SuppressLint("SimpleDateFormat")
@JvmOverloads
fun parseDate(value: String, format: String = "MM/dd/yyyy", def: Long = 0L): Long {
  if (value.isDigitsOnly()) {
    return value.toLong()
  }
  val formatter = SimpleDateFormat(format)
  return try {
    formatter.parse(value)!!.time
  } catch (err: Throwable) {
    log.warn("Unable to parse date (format='$format')", value)
    def
  }
}
