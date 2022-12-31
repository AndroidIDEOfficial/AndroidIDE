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
package com.itsaky.androidide.inflater.drawable

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.ChecksSdkIntAtLeast
import com.itsaky.androidide.inflater.AbstractParser
import org.xmlpull.v1.XmlPullParser

/**
 * Base class for drawable parsers.
 *
 * @author Akash Yadav
 */
abstract class IDrawableParser
protected constructor(protected open val parser: XmlPullParser?, open var minDepth: Int) : AbstractParser() {
  /**
   * Parse the drawable using the already provided parser and data.
   *
   * @return The parsed [Drawable] or `null` if the parse was unsuccessful.
   * @throws Exception If any fatal error occurred while parsing the drawable.
   */
  @Throws(Exception::class)
  open fun parse(context: Context): Drawable? {
    var index = attrIndex("visible")
    var visible = true
    if (index != -1) {
      visible = parseBoolean(value(index))
    }
    var autoMirrored = false
    index = attrIndex("autoMirrored")
    if (index != -1) {
      autoMirrored = parseBoolean(value(index))
    }
    var level = 0
    index = attrIndex("level")
    if (index != -1) {
      level = parseInteger(value(index), 0)
    }
    val drawable = parseDrawable(context)
    if (drawable != null) {
      drawable.setVisible(visible, false)
      drawable.isAutoMirrored = autoMirrored
      drawable.level = level
    }
    return drawable
  }
  
  /**
   * Actual implementation of the parse logic.
   *
   * @return The parsed drawable. Maybe `null`.
   * @throws Exception If any fatal error occurs while parsing the drawable.
   */
  @Throws(Exception::class) protected abstract fun parseDrawable(context: Context): Drawable?

  /**
   * Find the index of the attribute with the given name.
   *
   * @param name The name of the attribute to look for.
   * @return The index of the attribute or `-1`.
   */
  protected fun attrIndex(name: String): Int {
    for (i in 0 until parser!!.attributeCount) {
      if (parser!!.getAttributeName(i) == name) {
        return i
      }
    }
    return -1
  }

  /**
   * Get the value of the attribute at the given index.
   *
   * @param index The index of the attribute.
   * @return The value of the attribute.
   */
  protected fun value(index: Int): String {
    return parser!!.getAttributeValue(index)
  }

  /**
   * Checks if the drawable parser is allowed to parse at the current depth. This must be checked if
   * the drawable parser keeps looking for [XmlPullParser.START_TAG] or [ ][XmlPullParser.END_TAG].
   *
   * The [LayerListParser] can contain tags which are expected to be parsed by other parsers. So, if
   * the nesting parser does not check if this method if `true`, it might consume all the events and
   * the [LayerListParser] will have invalid data to parse. In some cases, the nested parser might
   * consume all the events until [XmlPullParser.END_DOCUMENT].
   *
   * @return `true` if the parser can keep parsing, `false` otherwise.
   */
  protected fun canParse(): Boolean {
    return parser!!.depth >= minDepth
  }

  @get:ChecksSdkIntAtLeast(api = VERSION_CODES.Q)
  protected val isApi29: Boolean
    get() = VERSION.SDK_INT >= 29

  companion object {
    const val ANY_DEPTH = -1
  }
}
