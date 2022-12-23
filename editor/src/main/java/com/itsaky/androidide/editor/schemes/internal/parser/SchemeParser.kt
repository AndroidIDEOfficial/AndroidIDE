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

import android.graphics.Color
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken.BEGIN_OBJECT
import com.google.gson.stream.JsonToken.STRING
import com.itsaky.androidide.editor.schemes.IDEColorScheme
import com.itsaky.androidide.editor.schemes.LanguageScheme
import com.itsaky.androidide.editor.schemes.StyleDef
import com.itsaky.androidide.editor.schemes.internal.parser.SchemeParser.EditorColors
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme
import java.io.File
import java.io.StringReader

/**
 * Parses editor's color scheme.
 *
 * @author Akash Yadav
 */
class SchemeParser {

  enum class EditorColors(val key: String, val id: Int) {
    BG("bg", EditorColorScheme.WHOLE_BACKGROUND),
    LINE_CURRENT("line.current", EditorColorScheme.CURRENT_LINE),
    LINE_DIVIDER("line.divider", EditorColorScheme.LINE_DIVIDER),
    LINE_NUMBER("line.num", EditorColorScheme.LINE_NUMBER),
    LINE_NUMBER_CURRENT("line.num.current", EditorColorScheme.LINE_NUMBER_CURRENT),
    LINE_NUMBER_BACKGROUND("line.num.bg", EditorColorScheme.LINE_NUMBER_BACKGROUND),
    LINE_NUMBER_PANEL("line.num.panel", EditorColorScheme.LINE_NUMBER_PANEL),
    LINE_NUMBER_PANEL_TEXT("line.num.panel.text", EditorColorScheme.LINE_NUMBER_PANEL_TEXT),
    TEXT_NORMAL("text.normal", EditorColorScheme.TEXT_NORMAL),
    TEXT_SELECTED("text.selected", EditorColorScheme.TEXT_SELECTED),
    MATCHED_TEXT_BACKGROUND("text.matched.bg", EditorColorScheme.MATCHED_TEXT_BACKGROUND),
    SELECTED_TEXT_BACKGROUND("text.selected.bg", EditorColorScheme.SELECTED_TEXT_BACKGROUND),
    SNIPPET_BG_EDITING("snippet.bg.editing", EditorColorScheme.SNIPPET_BACKGROUND_EDITING),
    SNIPPET_BG_INACTIVE("snippet.bg.inactive", EditorColorScheme.SNIPPET_BACKGROUND_INACTIVE),
    SNIPPET_BG_RELATED("snippet.bg.related", EditorColorScheme.SNIPPET_BACKGROUND_RELATED),
    SCROLL_BAR_THUMB("scrollbar.thumb", EditorColorScheme.SCROLL_BAR_THUMB),
    SCROLL_BAR_THUMB_PRESSED("scrollbar.thumb.pressed", EditorColorScheme.SCROLL_BAR_THUMB_PRESSED),
    SCROLL_BAR_TRACK("scrollbar.track", EditorColorScheme.SCROLL_BAR_TRACK),
    CODE_BLOCK_LINE("code.block.line", EditorColorScheme.BLOCK_LINE),
    CODE_BLOCK_LINE_CURRENT("code.block.line.current", EditorColorScheme.BLOCK_LINE_CURRENT),
    CODE_BLOCK_LINE_SIDE("code.block.line.side", EditorColorScheme.SIDE_BLOCK_LINE),
    COMPLETION_WINDOW_BG("completion.window.bg", EditorColorScheme.COMPLETION_WND_BACKGROUND),
    COMPLETION_WINDOW_OUTLINE("completion.window.outline", EditorColorScheme.COMPLETION_WND_CORNER),
    NON_PRINTABLE_CHAR("non_printable_char", EditorColorScheme.NON_PRINTABLE_CHAR),
    HIGHLIGHTED_DELIMITERS_UNDERLINE(
      "highlighted.delimiters.underline",
      EditorColorScheme.HIGHLIGHTED_DELIMITERS_UNDERLINE
    ),
    HIGHLIGHTED_DELIMITERS_BACKGROUND(
      "highlighted.delimiters.bg",
      EditorColorScheme.HIGHLIGHTED_DELIMITERS_BACKGROUND
    ),
    HIGHLIGHTED_DELIMITERS_FOREGROUND(
      "highlighted.delimiters.fg",
      EditorColorScheme.HIGHLIGHTED_DELIMITERS_FOREGROUND
    );

    companion object {
      @JvmStatic
      fun forKey(key: String): EditorColors {
        return values().find { it.key == key }
          ?: throw IllegalArgumentException("No editor color scheme available for key: '$key'")
      }
    }
  }

  companion object {
    const val KEY_IS_DARK = "scheme.isDark"
    const val KEY_DEFINITIONS = "definitions"
    const val KEY_EDITOR = "editor"
    const val KEY_LANGUAGES = "languages"
  }

  fun parse(file: File): IDEColorScheme {
    require(file.exists() && file.isFile) { "File does not exist or is not a file" }
    return parse(file.readText())
  }

  fun parse(content: String): IDEColorScheme {
    require(content.isNotBlank()) { "Cannot parse blank color scheme content" }
    val reader = JsonReader(StringReader(content))
    return parse(reader)
  }

  private fun parse(reader: JsonReader): IDEColorScheme {
    reader.beginObject()
    val scheme = IDEColorScheme()
    while (reader.hasNext()) {
      when (reader.nextName()) {
        KEY_IS_DARK -> scheme.isDarkScheme = reader.nextBoolean()
        KEY_DEFINITIONS -> scheme.definitions = scheme.parseDefinitions(reader)
        KEY_EDITOR -> scheme.parseEditorScheme(reader)
        KEY_LANGUAGES -> scheme.parseLanguages(reader)
      }
    }
    reader.endObject()
    return scheme
  }
}

private fun IDEColorScheme.parseEditorScheme(reader: JsonReader) {
  reader.beginObject()
  while (reader.hasNext()) {
    val color = EditorColors.forKey(reader.nextName())
    val value = reader.nextString()
    editorScheme[color.id] = parseColorValue(value)
  }
  reader.endObject()
}

private fun IDEColorScheme.parseColorValue(value: String?): Int {
  require(!value.isNullOrBlank()) { "Color value is not expected to be null or blank" }
  return when (value[0]) {
    '@' -> {
      val refName = value.substring(1)
      definitions[refName] ?: throw ParseException("Referenced color '$value' not found")
    }
    '#' -> {
      try {
        Color.parseColor(value)
      } catch (err: Throwable) {
        throw ParseException("Invalid hex color code: '$value'")
      }
    }
    else -> throw ParseException("Unsupported color value '$value'")
  }
}

private fun IDEColorScheme.parseDefinitions(reader: JsonReader): Map<String, Int> {
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

private fun IDEColorScheme.parseLanguages(reader: JsonReader) {
  reader.beginArray()
  while (reader.hasNext()) {
    val lang = parseLanguage(reader)
    lang.files.forEach { languages[it] = lang }
  }
  reader.endArray()
}

private fun IDEColorScheme.parseLanguage(reader: JsonReader): LanguageScheme {
  reader.beginObject()
  val fileTypes = mutableListOf<String>()
  val styles = mutableMapOf<String, StyleDef>()
  while (reader.hasNext()) {
    var name = reader.nextName()
    when (name) {
      "types" -> {
        reader.beginArray()
        while (reader.hasNext()) {
          fileTypes.add(reader.nextString())
        }
        reader.endArray()
      }
      "styles" -> {
        reader.beginObject()
        while (reader.hasNext()) {
          name = reader.nextName()
          if (reader.peek() == BEGIN_OBJECT) {
            styles[name] = parseStyleDef(reader)
          } else if (reader.peek() == STRING) {
            val color = parseColorValue(reader.nextString())
            styles[name] = StyleDef(fg = color)
          } else throw ParseException("A style definition must an object or a string value")
        }
        reader.endObject()
      }
      else -> throw ParseException("Unexpected key '$name' in language object")
    }
  }
  reader.endObject()

  if (fileTypes.isEmpty()) {
    throw ParseException("A language must specify the file types")
  }

  return LanguageScheme(files = fileTypes, styles = styles)
}

private fun IDEColorScheme.parseStyleDef(reader: JsonReader): StyleDef {
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
    }
  }
  if (def.fg == 0) {
    throw ParseException("A style definition must specify a valid foreground color")
  }
  reader.endObject()
  return def
}
