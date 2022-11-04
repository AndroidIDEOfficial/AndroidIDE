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
import com.android.aaptcompiler.AaptResourceType.DIMEN
import com.android.aaptcompiler.BinaryPrimitive
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.Reference
import com.android.aaptcompiler.ResourceEntry
import com.android.aaptcompiler.ResourceGroup
import com.android.aaptcompiler.ResourceName
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.ResourceTablePackage
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

fun parseDrawable(context: Context, value: String): Drawable {
  if (HEX_COLOR.matcher(value).matches()) {
    return parseColorDrawable(value)
  }

//  if (value[0] == '@') {
//    val (pck, type, name) = parseResourceReference(value)
//    return if(pck == null) {
//      parseDrawableResRef()
//    } else {
//      parseQualifiedDrawableResRef()
//    }
//  }
  return newColorDrawable(Color.TRANSPARENT)
}

fun parseColorDrawable(value: String): Drawable {
  val color =
    try {
      Color.parseColor(value)
    } catch (err: Throwable) {
      log.warn("Unable to parse color code", value)
      Color.TRANSPARENT
    }
  return newColorDrawable(color)
}

fun newColorDrawable(color: Int): Drawable {
  return ColorDrawable(color)
}

fun parseDimension(
  context: Context,
  value: String?,
  defValue: Int = LayoutParams.WRAP_CONTENT,
): Int {
  if (value.isNullOrBlank()) {
    return defValue
  }
  val c = value[0]
  if (c.isDigit()) {
    val i = value.length - 2
    val dimension = parseFloat(value.substring(0, i), defValue.toFloat())
    val unit = parseDimensionUnit(value.substring(i))
    return applyDimension(unit, dimension, context.resources.displayMetrics).toInt()
  } else if (c == '@') {
    val (pck, type, name) = parseResourceReference(value)
    return if (pck == null) {
      parseDimensionResRef(type, name, value)
    } else {
      parseQualifiedDimensionResRef(type, value, pck, name)
    }
  } else {
    return when (value) {
      "wrap_content" -> LayoutParams.WRAP_CONTENT
      "fill_parent",
      "match_parent", -> LayoutParams.MATCH_PARENT
      else -> {
        log.warn("Cannot infer type of dimension resource: '$value'")
        LayoutParams.WRAP_CONTENT
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

private fun parseDimensionResRef(type: String, name: String, value: String?): Int {
  if (type.isBlank() || name.isBlank()) {
    throw IllegalArgumentException("Cannot parse resource reference: '$value'")
  }
  // Must be a dimension type
  if (type != "dimen") {
    throw IllegalArgumentException("A dimension value is expect. Current value is $value")
  }
  
  var resTable: ResourceTable? = null
  var resGrp: ResourceGroup? = null
  var resPck: ResourceTablePackage? = null
  var resEntry: ResourceEntry? = null
  for (t in module.getAllResourceTables()) {
    val result = t.findResource(ResourceName("", DIMEN, name)) ?: continue
    resTable = t
    resGrp = result.group
    resPck = result.tablePackage
    resEntry = result.entry
    break
  }
  
  return resolveDimensionReference(
    table = resTable!!,
    group = resGrp!!,
    entry = resEntry!!,
    pck = resPck!!.name,
    name = name
  )
}

private fun parseQualifiedDimensionResRef(
  type: String,
  value: String?,
  pck: String,
  name: String
): Int {
  // Must be a dimension type
  if (type != "dimen") {
    throw IllegalArgumentException("A dimension value is expect. Current value is $value")
  }
  
  val table =
    module.findResourceTableForPackage(pck, DIMEN)
      ?: throw IllegalArgumentException("Resource table for package '$pck' not found.")
  
  return resolveDimensionReference(table = table, group = null, pck = pck, name = name)
}

private fun resolveDimensionReference(
  table: ResourceTable,
  group: ResourceGroup? = null,
  entry: ResourceEntry? = null,
  pck: String,
  name: String,
): Int {
  val grp = group ?: table.findPackage(pck)!!.findGroup(DIMEN)!!
  val e =
    entry
      ?: grp.findEntry(name)
        ?: run {
        log.warn("Dimension resource with name '$name' not found")
        return LayoutParams.WRAP_CONTENT
      }
  val dimenValue = e.findValue(ConfigDescription())!!.value
  if (dimenValue is Reference) {
    return resolveDimensionReference(
      table = table,
      group = grp,
      entry = null,
      pck = pck,
      name = dimenValue.name.entry!!
    )
  } else if (dimenValue is BinaryPrimitive) {
    return dimenValue.resValue.data
  }

  log.warn("Unable to resolve dimension reference '$name'")
  return LayoutParams.WRAP_CONTENT
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
