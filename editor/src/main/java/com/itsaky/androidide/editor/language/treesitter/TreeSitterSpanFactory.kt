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

package com.itsaky.androidide.editor.language.treesitter

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.itsaky.androidide.editor.schemes.LanguageScheme
import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryCapture
import com.itsaky.androidide.utils.ILogger
import io.github.rosemoe.sora.editor.ts.spans.DefaultSpanFactory
import io.github.rosemoe.sora.editor.ts.spans.TsSpanFactory
import io.github.rosemoe.sora.lang.styling.Span
import io.github.rosemoe.sora.lang.styling.StaticColorSpan
import io.github.rosemoe.sora.lang.styling.Styles
import io.github.rosemoe.sora.text.ContentReference

/**
 * [TsSpanFactory] for tree sitter languages.
 *
 * @author Akash Yadav
 */
class TreeSitterSpanFactory(
  private var content: ContentReference?,
  private var query: TSQuery?,
  private var styles: Styles?,
  private var langScheme: LanguageScheme?
) : DefaultSpanFactory() {

  companion object {

    private val log = ILogger.newInstance("TreeSitterSpanFactory")

    @JvmStatic
    private val HEX_REGEX = "#\\b([0-9a-fA-F]{3}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})\\b".toRegex()
  }

  override fun close() {
    content = null
    query = null
    styles = null
    langScheme = null
  }

  override fun createSpan(capture: TSQueryCapture, column: Int, spanStyle: Long): Span {
    val content = this.content?.reference ?: return super.createSpan(capture, column, spanStyle)
    val query = this.query ?: return super.createSpan(capture, column, spanStyle)
    val langScheme = this.langScheme ?: return super.createSpan(capture, column, spanStyle)

    val captureName = query.getCaptureNameForId(capture.index)
    val styleDef = langScheme.getStyles()[captureName]
    if (styleDef?.maybeHexColor != true) {
      return super.createSpan(capture, column, spanStyle)
    }

    val (start, end) = content.indexer.run {
      getCharPosition(capture.node.startByte / 2) to getCharPosition(capture.node.endByte / 2)
    }

    if (start.line != end.line || start.column != column) {
      // A HEX color can only defined on a single line
      return super.createSpan(capture, column, spanStyle)
    }

    var text: CharSequence = content.subContent(start.line, start.column + 1, end.line, end.column)

    // TODO(itsaky): update the API in sora-editor such that we can add multiple spans for this capture
    //     This method should probably return a list of spans
    val result = HEX_REGEX.find(text) ?: run {
      // Does not contain a HEX color string
      return super.createSpan(capture, column, spanStyle)
    }

    val color = try {
      text = result.groupValues[1]
      if (text.length == 3){
        // HEX color is in the form of #FFF
        // convert it to #FFFFFF format (6 character long)
        val r = text[0]
        val g = text[1]
        val b = text[2]
        text = "$r$r$g$g$b$b"
      }

      if (text.length == 6) {
        // Prepend alpha value
        text = "FF${text}"
      }

      java.lang.Long.parseLong(text, 16)
    } catch (e: Exception) {
      log.error("Failed to parse hex color. color=$text", e)
      return super.createSpan(capture, column, spanStyle)
    }.toInt()

    val textColor = if (ColorUtils.calculateLuminance(color) > 0.5f) {
      Color.BLACK
    } else {
      Color.WHITE
    }

    return StaticColorSpan.obtain(color, textColor, column, styleDef.makeStaticStyle())
  }
}