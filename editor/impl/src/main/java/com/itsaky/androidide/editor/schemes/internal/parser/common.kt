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

package com.itsaky.androidide.editor.schemes.internal.parser

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken.STRING
import com.itsaky.androidide.editor.schemes.IDEColorScheme
import com.itsaky.androidide.editor.schemes.LanguageScheme
import com.itsaky.androidide.editor.schemes.StyleDef
import com.itsaky.androidide.utils.parseHexColor
import java.io.File

/** @author Akash Yadav */
fun IDEColorScheme.parseEditorScheme(reader: JsonReader, resolveFileRef: (String) -> File) {
  val newReader = if (reader.peek() == STRING) {
    readerForFileRef(reader, "editor", resolveFileRef)
  } else reader

  try {
    EditorSchemeParser(newReader).parse(this)
  } finally {
    if (reader !== newReader) {
      newReader.close()
    }
  }
}

/**
 * Parses the color and returns the color ID. If [colorId] is `false`, returns the int color value
 * instead.
 */
fun IDEColorScheme.parseColorValue(value: String?, colorId: Boolean = true): Int {
  require(!value.isNullOrBlank()) { "Color value is not expected to be null or blank" }
  if (value[0] == '@') {
    val refName = value.substring(1)
    val refValue =
      definitions[refName] ?: throw ParseException("Referenced color '$value' not found")
    return if (colorId) refValue else colorIds.getOrDefault(refValue, 0)
  }

  if (value[0] == '#') {
    val color =
      try {
        parseHexColor(value).toInt()
      } catch (err: Throwable) {
        throw ParseException("Invalid hex color code: '$value'", err)
      }
    return if (colorId) putColor(color) else color
  }

  throw ParseException("Unsupported color value '$value'")
}

fun IDEColorScheme.parseDefinitions(reader: JsonReader): Map<String, Int> {
  val result = mutableMapOf<String, Int>()
  reader.beginObject()
  while (reader.hasNext()) {
    val name = reader.nextName()
    val value = reader.nextString()
    result[name] = parseColorValue(value)
  }
  reader.endObject()
  return result
}

fun IDEColorScheme.parseLanguages(reader: JsonReader, resolveFileRef: (String) -> File) {
  reader.beginArray()
  while (reader.hasNext()) {
    val lang = parseLanguage(reader, resolveFileRef)
    lang.files.forEach { languages[it] = lang }
  }
  reader.endArray()
}

fun IDEColorScheme.parseLanguage(
  reader: JsonReader,
  resolveFileRef: (String) -> File
): LanguageScheme {
  val newReader =
    if (reader.peek() == STRING) {
      readerForFileRef(reader, "language", resolveFileRef)
    } else reader

  return try {
    LanguageParser(newReader).parseLang(this)
  } finally {
    if (reader !== newReader) {
      newReader.close()
    }
  }
}

private fun readerForFileRef(reader: JsonReader, scheme: String,
  resolveFileRef: (String) -> File): JsonReader {
  val value = reader.nextString()
  if (value.length <= 1 || value[0] != '@') {
    throw ParseException("Expected a $scheme scheme file reference but was '$value'")
  }

  val langFile = resolveFileRef(value.substring(1))
  if (!langFile.exists() || !langFile.isFile) {
    throw ParseException("Referenced file does not exist or is not a file '$langFile'")
  }
  return JsonReader(langFile.reader())
}

fun IDEColorScheme.parseStyleDef(reader: JsonReader): StyleDef {
  reader.beginObject()
  val def = StyleDef(fg = 0)
  while (reader.hasNext()) {
    when (reader.nextName()) {
      "fg" -> def.fg = parseColorValue(reader.nextString())
      "bg" -> def.bg = parseColorValue(reader.nextString())
      "bold" -> def.bold = reader.nextBoolean()
      "italic" -> def.italic = reader.nextBoolean()
      "strikethrough" -> def.strikeThrough = reader.nextBoolean()
      "completion" -> def.completion = reader.nextBoolean()
      "maybeHexColor" -> def.maybeHexColor = reader.nextBoolean()
    }
  }
  if (def.fg == 0) {
    throw ParseException("A style definition must specify a valid foreground color")
  }
  reader.endObject()
  return def
}
