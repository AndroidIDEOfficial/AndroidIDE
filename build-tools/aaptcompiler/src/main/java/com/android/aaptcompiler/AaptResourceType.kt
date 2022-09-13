package com.android.aaptcompiler

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
  when (tag) {
    ANIM_TAG -> AaptResourceType.ANIM
    ANIMATOR_TAG -> AaptResourceType.ANIMATOR
    ARRAY_TAG -> AaptResourceType.ARRAY
    ATTR_TAG -> AaptResourceType.ATTR
    ATTR_PRIVATE_TAG -> AaptResourceType.ATTR_PRIVATE
    BOOL_TAG -> AaptResourceType.BOOL
    COLOR_TAG -> AaptResourceType.COLOR
    CONFIG_VARYING_TAG -> AaptResourceType.CONFIG_VARYING
    DIMEN_TAG -> AaptResourceType.DIMEN
    DRAWABLE_TAG -> AaptResourceType.DRAWABLE
    FONT_TAG -> AaptResourceType.FONT
    FRACTION_TAG -> AaptResourceType.FRACTION
    ID_TAG -> AaptResourceType.ID
    INTEGER_TAG -> AaptResourceType.INTEGER
    INTERPOLATOR_TAG -> AaptResourceType.INTERPOLATOR
    LAYOUT_TAG -> AaptResourceType.LAYOUT
    MACRO_TAG -> AaptResourceType.MACRO
    MENU_TAG -> AaptResourceType.MENU
    MIPMAP_TAG -> AaptResourceType.MIPMAP
    NAVIGATION_TAG -> AaptResourceType.NAVIGATION
    PLURALS_TAG -> AaptResourceType.PLURALS
    RAW_TAG -> AaptResourceType.RAW
    STRING_TAG -> AaptResourceType.STRING
    STYLE_TAG -> AaptResourceType.STYLE
    STYLEABLE_TAG -> AaptResourceType.STYLEABLE
    TRANSITION_TAG -> AaptResourceType.TRANSITION
    XML_TAG -> AaptResourceType.XML
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
