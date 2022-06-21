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

package com.itsaky.androidide.language.xml

import android.graphics.Color
import com.itsaky.androidide.app.StudioApp
import com.itsaky.androidide.language.incremental.BaseIncrementalAnalyzeManager
import com.itsaky.androidide.language.incremental.IncrementalToken
import com.itsaky.androidide.language.incremental.LineState
import com.itsaky.androidide.lexers.xml.XMLLexer
import com.itsaky.androidide.lexers.xml.XMLLexer.CLOSE
import com.itsaky.androidide.lexers.xml.XMLLexer.COLON
import com.itsaky.androidide.lexers.xml.XMLLexer.DASH
import com.itsaky.androidide.lexers.xml.XMLLexer.EQUALS
import com.itsaky.androidide.lexers.xml.XMLLexer.NOT
import com.itsaky.androidide.lexers.xml.XMLLexer.Name
import com.itsaky.androidide.lexers.xml.XMLLexer.OPEN
import com.itsaky.androidide.lexers.xml.XMLLexer.OPEN_SLASH
import com.itsaky.androidide.lexers.xml.XMLLexer.SLASH
import com.itsaky.androidide.lexers.xml.XMLLexer.SLASH_CLOSE
import com.itsaky.androidide.lexers.xml.XMLLexer.SPECIAL_CLOSE
import com.itsaky.androidide.lexers.xml.XMLLexer.STRING
import com.itsaky.androidide.lexers.xml.XMLLexer.TEXT
import com.itsaky.androidide.lexers.xml.XMLLexer.XMLDeclOpen
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.FIELD
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LITERAL
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.OPERATOR
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TEXT_NORMAL
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.XML_TAG
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forComment
import com.itsaky.inflater.util.CommonParseUtils
import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult
import io.github.rosemoe.sora.lang.styling.CodeBlock
import io.github.rosemoe.sora.lang.styling.Span
import io.github.rosemoe.sora.lang.styling.TextStyle.makeStyle
import io.github.rosemoe.sora.text.Content

/**
 * Syntax analyzer for XML.
 *
 * @author Akash Yadav
 */
class XMLAnalyzer : BaseIncrementalAnalyzeManager(XMLLexer::class.java) {

  override fun computeBlocks(
    text: Content?,
    delegate: CodeBlockAnalyzeDelegate?,
  ): MutableList<CodeBlock> {
    return mutableListOf()
  }

  override fun getMultilineTokenStartEndTypes(): Array<IntArray> {
    val start = intArrayOf(OPEN, NOT, DASH, DASH)
    val end = intArrayOf(DASH, DASH, OPEN)
    return arrayOf(start, end)
  }

  override fun generateSpans(
    tokens: LineTokenizeResult<LineState, IncrementalToken>,
  ): MutableList<Span> {
    val app = StudioApp.getInstance()
    val parser = CommonParseUtils(app.resourceTable, app.resources.displayMetrics)
    val spans = mutableListOf<Span>()
    var previous = XMLLexer.SEA_WS
  
    spans.add(Span.obtain(0, makeStyle(TEXT_NORMAL)))
    
    for (token in tokens.tokens) {
      val type = token.type
      val offset = token.startIndex

      when (type) {
        XMLLexer.COMMENT -> spans.add(Span.obtain(offset, forComment()))
        OPEN,
        DASH,
        NOT,
        OPEN_SLASH,
        SLASH_CLOSE,
        CLOSE,
        SLASH,
        SPECIAL_CLOSE,
        EQUALS,
        COLON,
        XMLDeclOpen, -> spans.add(Span.obtain(offset, makeStyle(OPERATOR)))
        STRING -> // highlight hex color line
        try {
            val text: String = token.text
            val textVar = text.replace("\"", "")
            if (isColorValue(textVar)) {
              val color: Int =
                parser.parseColor(textVar, StudioApp.getInstance().applicationContext)
              val span = Span.obtain(offset + 1, makeStyle(LITERAL))
              span.setUnderlineColor(color)
              spans.add(span)
              val middle = Span.obtain(offset + text.length - 1, makeStyle(LITERAL))
              middle.setUnderlineColor(Color.TRANSPARENT)
              spans.add(middle)
              val end = Span.obtain(offset + text.length, makeStyle(TEXT_NORMAL))
              end.setUnderlineColor(Color.TRANSPARENT)
              spans.add(end)
            } else {
              spans.add(Span.obtain(offset, makeStyle(LITERAL)))
            }
          } catch (ex: Exception) {
            // This color string is not valid
            spans.add(Span.obtain(offset, makeStyle(LITERAL)))
          }
        Name -> {
          var colorId = TEXT_NORMAL
          if (previous == OPEN) {
            colorId = XML_TAG
          } else if (previous == OPEN_SLASH) {
            colorId = XML_TAG
          }
          val attribute: String = token.text
          if (attribute.contains(":")) {
            spans.add(Span.obtain(offset, makeStyle(FIELD)))
            spans.add(Span.obtain(offset + attribute.indexOf(":"), makeStyle(TEXT_NORMAL)))
          } else {
            spans.add(Span.obtain(offset, makeStyle(colorId)))
          }
        }
        TEXT -> // highlight hex color line
        try {
            val textVar: String = token.text
            if (isColorValue(textVar)) {
              val color: Int =
                parser.parseColor(textVar, StudioApp.getInstance().applicationContext)
              val span = Span.obtain(offset, makeStyle(TEXT_NORMAL))
              span.setUnderlineColor(color)
              spans.add(span)
            } else {
              spans.add(Span.obtain(offset, makeStyle(TEXT_NORMAL)))
            }
          } catch (ex: Exception) {
            // This color string is not valid
            spans.add(Span.obtain(offset, makeStyle(TEXT_NORMAL)))
          }
        else -> spans.add(Span.obtain(offset, makeStyle(TEXT_NORMAL)))
      }
      if (token.getType() != XMLLexer.SEA_WS) {
        previous = token.getType()
      }
    }
    return spans
  }

  override fun handleIncompleteToken(token: IncrementalToken) {
    token.type = XMLLexer.COMMENT
  }

  private fun isColorValue(value: String): Boolean {
    return (value.startsWith("#") ||
      value.startsWith("@android:color") ||
      value.startsWith("@color/"))
  }
}
