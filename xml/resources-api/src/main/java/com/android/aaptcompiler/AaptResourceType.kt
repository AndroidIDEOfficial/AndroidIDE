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

package com.android.aaptcompiler

import com.android.aaptcompiler.AaptResourceType.ANIM
import com.android.aaptcompiler.AaptResourceType.ANIMATOR
import com.android.aaptcompiler.AaptResourceType.ARRAY
import com.android.aaptcompiler.AaptResourceType.ATTR
import com.android.aaptcompiler.AaptResourceType.ATTR_PRIVATE
import com.android.aaptcompiler.AaptResourceType.BOOL
import com.android.aaptcompiler.AaptResourceType.COLOR
import com.android.aaptcompiler.AaptResourceType.CONFIG_VARYING
import com.android.aaptcompiler.AaptResourceType.DIMEN
import com.android.aaptcompiler.AaptResourceType.DRAWABLE
import com.android.aaptcompiler.AaptResourceType.FONT
import com.android.aaptcompiler.AaptResourceType.FRACTION
import com.android.aaptcompiler.AaptResourceType.ID
import com.android.aaptcompiler.AaptResourceType.INTEGER
import com.android.aaptcompiler.AaptResourceType.INTERPOLATOR
import com.android.aaptcompiler.AaptResourceType.LAYOUT
import com.android.aaptcompiler.AaptResourceType.MACRO
import com.android.aaptcompiler.AaptResourceType.MENU
import com.android.aaptcompiler.AaptResourceType.MIPMAP
import com.android.aaptcompiler.AaptResourceType.NAVIGATION
import com.android.aaptcompiler.AaptResourceType.PLURALS
import com.android.aaptcompiler.AaptResourceType.RAW
import com.android.aaptcompiler.AaptResourceType.STRING
import com.android.aaptcompiler.AaptResourceType.STYLE
import com.android.aaptcompiler.AaptResourceType.STYLEABLE
import com.android.aaptcompiler.AaptResourceType.TRANSITION
import com.android.aaptcompiler.AaptResourceType.XML

private const val ANIM_TAG = "anim"
private const val ANIMATOR_TAG = "animator"
private const val ARRAY_TAG = "array"
private const val ATTR_TAG = "attr"
private const val ATTR_PRIVATE_TAG = "^attr-private"
private const val BOOL_TAG = "bool"
private const val COLOR_TAG = "color"
private const val CONFIG_VARYING_TAG = "configVarying"
private const val DIMEN_TAG = "dimen"
private const val DRAWABLE_TAG = "drawable"
private const val FONT_TAG = "font"
private const val FRACTION_TAG = "fraction"
private const val ID_TAG = "id"
private const val INTEGER_TAG = "integer"
private const val INTERPOLATOR_TAG = "interpolator"
private const val LAYOUT_TAG = "layout"
private const val MACRO_TAG = "macro"
private const val MENU_TAG = "menu"
private const val MIPMAP_TAG = "mipmap"
private const val NAVIGATION_TAG = "navigation"
private const val PLURALS_TAG = "plurals"
private const val RAW_TAG = "raw"
private const val STRING_TAG = "string"
private const val STYLE_TAG = "style"
private const val STYLEABLE_TAG = "styleable"
private const val TRANSITION_TAG = "transition"
private const val XML_TAG = "xml"

fun resourceTypeFromTag(tag: String) =
  when(tag) {
    ANIM_TAG -> ANIM
    ANIMATOR_TAG -> ANIMATOR
    ARRAY_TAG -> ARRAY
    ATTR_TAG -> ATTR
    ATTR_PRIVATE_TAG -> ATTR_PRIVATE
    BOOL_TAG -> BOOL
    COLOR_TAG -> COLOR
    CONFIG_VARYING_TAG -> CONFIG_VARYING
    DIMEN_TAG -> DIMEN
    DRAWABLE_TAG -> DRAWABLE
    FONT_TAG -> FONT
    FRACTION_TAG -> FRACTION
    ID_TAG -> ID
    INTEGER_TAG -> INTEGER
    INTERPOLATOR_TAG -> INTERPOLATOR
    LAYOUT_TAG -> LAYOUT
    MACRO_TAG -> MACRO
    MENU_TAG -> MENU
    MIPMAP_TAG -> MIPMAP
    NAVIGATION_TAG -> NAVIGATION
    PLURALS_TAG -> PLURALS
    RAW_TAG -> RAW
    STRING_TAG -> STRING
    STYLE_TAG -> STYLE
    STYLEABLE_TAG -> STYLEABLE
    TRANSITION_TAG -> TRANSITION
    XML_TAG -> XML
    else -> null
  }

enum class AaptResourceType(val tagName: String) {
  ANIM(ANIM_TAG),
  ANIMATOR(ANIMATOR_TAG),
  ARRAY(ARRAY_TAG),
  ATTR(ATTR_TAG),
  ATTR_PRIVATE(ATTR_PRIVATE_TAG),
  BOOL(BOOL_TAG),
  COLOR(COLOR_TAG),
  CONFIG_VARYING(CONFIG_VARYING_TAG),
  DIMEN(DIMEN_TAG),
  DRAWABLE(DRAWABLE_TAG),
  FONT(FONT_TAG),
  FRACTION(FRACTION_TAG),
  ID(ID_TAG),
  INTEGER(INTEGER_TAG),
  INTERPOLATOR(INTERPOLATOR_TAG),
  LAYOUT(LAYOUT_TAG),
  MACRO(MACRO_TAG),
  MENU(MENU_TAG),
  MIPMAP(MIPMAP_TAG),
  NAVIGATION(NAVIGATION_TAG),
  PLURALS(PLURALS_TAG),
  RAW(RAW_TAG),
  STRING(STRING_TAG),
  STYLE(STYLE_TAG),
  STYLEABLE(STYLEABLE_TAG),
  TRANSITION(TRANSITION_TAG),
  UNKNOWN(""),
  XML(XML_TAG)
}
