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

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.COMPLEX_UNIT_IN
import android.util.TypedValue.COMPLEX_UNIT_MM
import android.util.TypedValue.COMPLEX_UNIT_PT
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.util.TypedValue.applyDimension
import android.view.ViewGroup.LayoutParams
import com.android.aaptcompiler.AaptResourceType
import com.android.aaptcompiler.AaptResourceType.BOOL
import com.android.aaptcompiler.AaptResourceType.DIMEN
import com.android.aaptcompiler.BinaryPrimitive
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.Reference
import com.android.aaptcompiler.ResourceEntry
import com.android.aaptcompiler.ResourceName
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.ResourceTablePackage
import com.android.aaptcompiler.Value
import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.xml.utils.attrValue_qualifiedRef
import com.itsaky.androidide.xml.utils.attrValue_unqualifiedRef
import java.util.regex.Pattern

private var currentModule: AndroidModule? = null
private val log = ILogger.newInstance("ParseUtilsKt")
private val HEX_COLOR: Pattern = Pattern.compile("#[a-fA-F\\d]{6,8}")

val module: AndroidModule
  get() =
    currentModule ?: throw IllegalStateException("You must call startParse(AndroidModule) first")

fun startParse(m: AndroidModule) {
  currentModule = m
}

fun endParse() {
  currentModule = null
}

@JvmOverloads
fun parseBoolean(value: String, def: Boolean = false): Boolean {
  when (value) {
    "true" -> return true
    "false" -> return false
  }

  if (value[0] == '@') {
    val (pck, type, name) = parseResourceReference(value)
    if (type != "bool") {
      throw IllegalArgumentException("Value must be a reference to a boolean resource. '$value'")
    }
    val booleanResolver: (Value?) -> Boolean? =
      fun(resValue): Boolean {
        return if (resValue is BinaryPrimitive) {
          when (resValue.resValue.data) {
            -1 -> true
            0 -> false
            else -> def
          }
        } else def
      }
    return if (pck == null) {
      resolveUnqualifiedResourceReference(
        type = BOOL,
        name = name,
        value = value,
        def = def,
        resolver = booleanResolver
      )
    } else {
      resolveQualifiedResourceReference(
        pck = pck,
        type = BOOL,
        name = name,
        def = def,
        resolver = booleanResolver
      )
    }
  }

  return def
}

@JvmOverloads
fun parseDrawable(context: Context, value: String, def: Drawable = unknownDrawable()): Drawable {
  if (HEX_COLOR.matcher(value).matches()) {
    return parseColorDrawable(value)
  }

  //    if (value[0] == '@') {
  //      val (pck, type, name) = parseResourceReference(value)
  //      return if(pck == null) {
  //        parseDrawableResRef(type, name, value)
  //      } else {
  //        parseQualifiedDrawableResRef()
  //      }
  //    }
  return def
}

fun parseColorDrawable(value: String, def: Int = Color.TRANSPARENT): Drawable {
  val color =
    try {
      Color.parseColor(value)
    } catch (err: Throwable) {
      log.warn("Unable to parse color code", value)
      def
    }
  return newColorDrawable(color)
}

fun unknownDrawable(): Drawable {
  return newColorDrawable(Color.TRANSPARENT)
}

fun newColorDrawable(color: Int): Drawable {
  return ColorDrawable(color)
}

fun parseDimension(
  context: Context,
  value: String?,
  def: Int = LayoutParams.WRAP_CONTENT,
): Int {
  if (value.isNullOrBlank()) {
    return def
  }
  val c = value[0]
  if (c.isDigit()) {
    val i = value.length - 2
    val dimension = parseFloat(value.substring(0, i), def.toFloat())
    val unit = parseDimensionUnit(value.substring(i))
    return applyDimension(unit, dimension, context.resources.displayMetrics).toInt()
  } else if (c == '@') {
    val (pck, type, name) = parseResourceReference(value)
    if (type != "dimen") {
      throw IllegalArgumentException("Value must be a dimension resource reference '$value'")
    }
    val resolver: (Value?) -> Int? = {
      if (it is BinaryPrimitive) {
        it.resValue.data
        // TODO handle other resource types
      } else null
    }
    return if (pck == null) {
      resolveUnqualifiedResourceReference(
        type = DIMEN,
        name = name,
        value = value,
        def = def,
        resolver = resolver
      )
    } else {
      resolveQualifiedResourceReference(
        pck = pck,
        type = DIMEN,
        name = name,
        def = def,
        resolver = resolver
      )
    }
  } else {
    return when (value) {
      "wrap_content" -> LayoutParams.WRAP_CONTENT
      "fill_parent",
      "match_parent", -> LayoutParams.MATCH_PARENT
      else -> {
        log.warn("Cannot infer type of dimension resource: '$value'")
        def
      }
    }
  }
}

fun parseFloat(value: String, defValue: Float): Float {
  return try {
    value.toFloat()
  } catch (err: Throwable) {
    defValue
  }
}

fun parseDimensionUnit(unitStr: String): Int {
  return when (unitStr) {
    "dp" -> COMPLEX_UNIT_DIP
    "sp" -> COMPLEX_UNIT_SP
    "pt" -> COMPLEX_UNIT_PT
    "px" -> COMPLEX_UNIT_PX
    "in" -> COMPLEX_UNIT_IN
    "mm" -> COMPLEX_UNIT_MM
    else -> COMPLEX_UNIT_DIP
  }
}

fun <T> resolveUnqualifiedResourceReference(
  type: AaptResourceType,
  name: String,
  value: String?,
  def: T,
  resolver: (Value?) -> T?
): T {
  val (table, _, pack, entry) =
    lookupUnqualifedResource(type, name, value) ?: return def
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
  table: ResourceTable,
  type: AaptResourceType,
  pck: String,
  name: String,
  def: T,
  resolver: (Value?) -> T?
): T {
  val result =
    table.findResource(ResourceName(pck = pck, type = type, entry = name))
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
  table: ResourceTable,
  pck: ResourceTablePackage,
  entry: ResourceEntry,
  type: AaptResourceType,
  name: String,
  def: T,
  resolver: (Value?) -> T?
): T {
  val dimenValue = entry.findValue(ConfigDescription())!!.value
  if (dimenValue is Reference) {
    return resolveResourceReference(
      table = table,
      type = type,
      pck = pck.name,
      name = dimenValue.name.entry!!,
      def = def,
      resolver = resolver
    )
  }

  return resolver(dimenValue)
    ?: run {
      log.warn("Unable to resolve dimension reference '$name'")
      def
    }
}

private fun parseResourceReference(value: String): Triple<String?, String, String> {
  // A qualified resource reference
  // For example: '@com.itsaky.androidide.resources:dimen/my_dimen' or '@android:dimen/my_dimen'
  //                           ^pck                 ^type   ^name
  var matcher = attrValue_qualifiedRef.matcher(value)
  if (matcher.matches()) {
    return Triple(matcher.group(1)!!, matcher.group(3)!!, matcher.group(4)!!)
  }

  matcher = attrValue_unqualifiedRef.matcher(value)
  if (matcher.matches()) {
    return Triple<String?, String, String>(null, matcher.group(1)!!, matcher.group(2)!!)
  }

  return Triple<String?, String, String>(null, "", "")
}
