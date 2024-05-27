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

package com.itsaky.androidide.build.config

import java.io.File

private const val generatedWarning = "DO NOT EDIT - Automatically generated file"

/**
 * Replace the placeholders in the content of this file with the provided values.
 *
 * @param dest The destination file.
 * @param comment The comment characters. This is used to add the generated file header to the file.
 *   The default value if '//'.
 * @param candidates The candidates (placeholders) to replace. The first element in the pair is the
 *   placeholder text that will be replaced by the second element in the pair. For example, if a
 *   pair is `key -> value`, then `@@key@@` will be replaced with `value`.
 */
fun File.replaceContents(
  dest: File,
  comment: String = "//",
  vararg candidates: Pair<String, String>
) {
  val contents =
    StringBuilder()
      .append(comment)
      .append(" ")
      .append(generatedWarning)
      .append(System.getProperty("line.separator").repeat(2))

  bufferedReader().use { reader ->
    reader.readText().also { text ->
      var t = text
      for ((old, new) in candidates) {
        t = t.replace("@@${old}@@", new)
      }
      contents.append(t)
    }
  }

  if (dest.exists()) {
    dest.delete()
  }

  dest.parentFile.let {
    if (!it.exists()) {
      it.mkdirs()
    }
  }
  
  dest.bufferedWriter().use { writer ->
    writer.write(contents.toString())
    writer.flush()
  }
}
