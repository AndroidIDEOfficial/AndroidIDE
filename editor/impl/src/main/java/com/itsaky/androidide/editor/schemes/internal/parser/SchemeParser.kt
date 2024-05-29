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
import com.itsaky.androidide.editor.schemes.IDEColorScheme
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme
import java.io.File

/**
 * Parses editor's color scheme.
 *
 * @author Akash Yadav
 */
class SchemeParser(private val resolveFileRef: (String) -> File) {

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
    
    CURSOR("text.cursor", EditorColorScheme.SELECTION_INSERT),
    SELECTION_HANDLE("text.selection.handle", EditorColorScheme.SELECTION_HANDLE),
    UNDERLINE("text.underline", EditorColorScheme.UNDERLINE),
    
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
    COMPLETION_WND_TEXT_LABEL(
      "completion.window.text.label",
      SchemeAndroidIDE.COMPLETION_WND_TEXT_LABEL
    ),
    COMPLETION_WND_TEXT_DETAIL(
      "completion.window.text.detail",
      SchemeAndroidIDE.COMPLETION_WND_TEXT_DETAIL
    ),
    COMPLETION_WND_TEXT_API("completion.window.text.api", SchemeAndroidIDE.COMPLETION_WND_TEXT_API),
    COMPLETION_WND_TEXT_TYPE(
      "completion.window.text.type",
      SchemeAndroidIDE.COMPLETION_WND_TEXT_TYPE
    ),
    COMPLETION_WND_BG_CURRENT_ITEM(
      "completion.window.item.current",
      SchemeAndroidIDE.COMPLETION_WND_BG_CURRENT_ITEM
    ),
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
    const val KEY_DEFINITIONS = "definitions"
    const val KEY_EDITOR = "editor"
    const val KEY_LANGUAGES = "languages"
  }

  fun parse(file: File, name: String, isDark: Boolean): IDEColorScheme {
    require(file.exists() && file.isFile) { "File does not exist or is not a file" }
    val scheme = IDEColorScheme(file, name)
    scheme.name = name
    scheme.isDarkScheme = isDark
    load(scheme)
    return scheme
  }
  
  internal fun load(scheme: IDEColorScheme) {
    JsonReader(scheme.file.reader()).use { reader ->
      reader.beginObject()
      while (reader.hasNext()) {
        when (reader.nextName()) {
          KEY_DEFINITIONS -> scheme.definitions = scheme.parseDefinitions(reader)
          KEY_EDITOR -> scheme.parseEditorScheme(reader, resolveFileRef)
          KEY_LANGUAGES -> scheme.parseLanguages(reader, resolveFileRef)
        }
      }
      reader.endObject()
  
      if (scheme.name.isBlank()) {
        throw ParseException("A color scheme must a valid name. Current name is '${scheme.name}'")
      }
    }
  }
}
