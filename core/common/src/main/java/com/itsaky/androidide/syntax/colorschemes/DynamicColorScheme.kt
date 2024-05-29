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

package com.itsaky.androidide.syntax.colorschemes

import android.content.Context
import com.itsaky.androidide.common.R.attr
import com.itsaky.androidide.utils.isSystemInDarkMode
import com.itsaky.androidide.utils.resolveAttr

/**
 * [SchemeAndroidIDE] implementation that uses dynamic colors.
 *
 * @author Akash Yadav
 */
open class DynamicColorScheme : SchemeAndroidIDE() {

  private var isInDarkMode: Boolean = true

  fun apply(context: Context) {
    this.isInDarkMode = context.isSystemInDarkMode()
    val secondaryVariant = context.resolveAttr(attr.colorSecondaryVariant)
    val surface = context.resolveAttr(attr.colorSurface)
    val surfaceVariant = context.resolveAttr(attr.colorSurfaceVariant)
    val onSurface = context.resolveAttr(attr.colorOnSurface)
    val onSurfaceVariant = context.resolveAttr(attr.colorOnSurfaceVariant)
    val outline = context.resolveAttr(attr.colorOutline)
    setColor(WHOLE_BACKGROUND, surface)
    setColor(LINE_NUMBER_BACKGROUND, surface)
    setColor(LINE_NUMBER, onSurface)
    setColor(LINE_NUMBER_PANEL, surfaceVariant)
    setColor(LINE_NUMBER_PANEL_TEXT, onSurfaceVariant)
    setColor(CURRENT_LINE, surfaceVariant)
    setColor(TEXT_NORMAL, onSurface)
    setColor(BLOCK_LINE, surfaceVariant)
    setColor(BLOCK_LINE_CURRENT, outline)
    setColor(UNDERLINE, outline)

    setColor(COMPLETION_WND_BACKGROUND, surface)
    setColor(COMPLETION_WND_BG_CURRENT_ITEM, surfaceVariant)
    setColor(COMPLETION_WND_CORNER, outline)
    setColor(COMPLETION_WND_TEXT_LABEL, onSurface)
    setColor(COMPLETION_WND_TEXT_DETAIL, secondaryVariant)
    setColor(COMPLETION_WND_TEXT_API, secondaryVariant)
    setColor(COMPLETION_WND_TEXT_TYPE, secondaryVariant)

    if (!isInDarkMode) {
      applyLightColors()
    }
  }

  override fun isDark(): Boolean {
    return this.isInDarkMode
  }

  private fun applyLightColors() {
    setColor(KEYWORD, 0xffd32f2f)
    setColor(OPERATOR, 0xff1976d2)
    setColor(LITERAL, 0xff558b2f)
    setColor(TYPE_NAME, 0xff1976d2)
    setColor(ANNOTATION, 0xff1976d2)
    setColor(FIELD, 0xffff6f00)

    setColor(XML_TAG, 0xffe64a19)

    setColor(LOG_TEXT_ERROR, 0xfff44336)
    setColor(LOG_TEXT_WARNING, 0xffffab00)
    setColor(LOG_TEXT_INFO, 0xff4CAF50)
    setColor(LOG_TEXT_DEBUG, 0xff212121)
    setColor(LOG_TEXT_VERBOSE, 0xff0288d1)

    setColor(TODO_COMMENT, 0xffff6d00)
    setColor(FIXME_COMMENT, 0xffff6d00)
    setColor(COMMENT, 0xff9e9e9e)
  }

  private fun setColor(key: Int, value: Long) {
    setColor(key, value.toInt())
  }
}
