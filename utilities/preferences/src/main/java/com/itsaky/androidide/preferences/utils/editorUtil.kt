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

package com.itsaky.androidide.preferences.utils

import com.itsaky.androidide.preferences.internal.EditorPreferences

/** @author Akash Yadav */

/**
 * The indentation character to use. If [useSoftTab] is enabled, the character is a space, otherwise
 * '\t'.
 */
val indentationChar: Char
  get() = if (EditorPreferences.useSoftTab) ' ' else '\t'

/** Get the string which should be used as indentation while generating code. */
val indentationString: String
  get() = if (EditorPreferences.useSoftTab) " ".repeat(EditorPreferences.tabSize) else "\t"

/**
 * Creates the indentation string for the given number of spaces. The result is simply
 * [indentationChar] repeated [spaceCount] times if [useSoftTab] is enabled, otherwise `spaceCount /
 * tabSize` times.
 *
 * @param spaceCount The number of spaces to indent.
 * @return The indentation string.
 */
fun indentationString(spaceCount: Int): String {
  val count = if (EditorPreferences.useSoftTab) spaceCount else spaceCount / EditorPreferences.tabSize
  return indentationChar.toString().repeat(count)
}
