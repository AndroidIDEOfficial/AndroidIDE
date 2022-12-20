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

package com.itsaky.androidide.editor.language.log

import com.itsaky.androidide.editor.language.log.LogLineAnalyzer.LogLineState
import com.itsaky.androidide.lexers.log.LogLineTokenizer
import com.itsaky.androidide.lexers.log.LogToken
import com.itsaky.androidide.lexers.log.LogToken.PRIORITY
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_PRIORITY_BG_DEBUG
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_PRIORITY_BG_ERROR
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_PRIORITY_BG_INFO
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_PRIORITY_BG_VERBOSE
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_PRIORITY_BG_WARNING
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_PRIORITY_FG_DEBUG
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_PRIORITY_FG_ERROR
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_PRIORITY_FG_INFO
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_PRIORITY_FG_VERBOSE
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_PRIORITY_FG_WARNING
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_TEXT_DEBUG
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_TEXT_ERROR
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_TEXT_INFO
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_TEXT_VERBOSE
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LOG_TEXT_WARNING
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.get
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.ILogger.Priority.DEBUG
import com.itsaky.androidide.utils.ILogger.Priority.ERROR
import com.itsaky.androidide.utils.ILogger.Priority.INFO
import com.itsaky.androidide.utils.ILogger.Priority.VERBOSE
import com.itsaky.androidide.utils.ILogger.Priority.WARNING
import io.github.rosemoe.sora.lang.analysis.AsyncIncrementalAnalyzeManager
import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult
import io.github.rosemoe.sora.lang.styling.CodeBlock
import io.github.rosemoe.sora.lang.styling.Span
import io.github.rosemoe.sora.lang.styling.TextStyle
import io.github.rosemoe.sora.text.Content

/**
 * Analyzer for log lines.
 *
 * @author Akash Yadav
 */
class LogLineAnalyzer : AsyncIncrementalAnalyzeManager<LogLineState, LogToken>() {

  class LogLineState

  override fun getInitialState() = LogLineState()
  override fun stateEquals(state: LogLineState?, another: LogLineState?) =
    state?.equals(another) ?: false
  override fun computeBlocks(text: Content?, delegate: CodeBlockAnalyzeDelegate?) =
    mutableListOf<CodeBlock>()

  override fun tokenizeLine(
    lineText: CharSequence,
    state: LogLineState,
    line: Int
  ): LineTokenizeResult<LogLineState, LogToken> {
    val input = lineText.toArray()
    val tokenizer = LogLineTokenizer(input)
    val tokens = tokenizer.allTokens()
    return LineTokenizeResult(LogLineState(), tokens)
  }

  override fun generateSpansForLine(
    result: LineTokenizeResult<LogLineState, LogToken>
  ): MutableList<Span> {
    val tokens = result.tokens
    val spans = mutableListOf<Span>()
    for (token in tokens) {
      when (token.type) {
        PRIORITY -> {
          val priority = ILogger.priority(token.text[0].uppercaseChar())!!
          val text: Int
          val foreground: Int
          val background: Int
          when (priority) {
            DEBUG -> {
              text = LOG_TEXT_DEBUG
              foreground = LOG_PRIORITY_FG_DEBUG
              background = LOG_PRIORITY_BG_DEBUG
            }
            INFO -> {
              text = LOG_TEXT_INFO
              foreground = LOG_PRIORITY_FG_INFO
              background = LOG_PRIORITY_BG_INFO
            }
            ERROR -> {
              text = LOG_TEXT_ERROR
              foreground = LOG_PRIORITY_FG_ERROR
              background = LOG_PRIORITY_BG_ERROR
            }
            WARNING -> {
              text = LOG_TEXT_WARNING
              foreground = LOG_PRIORITY_FG_WARNING
              background = LOG_PRIORITY_BG_WARNING
            }
            VERBOSE -> {
              text = LOG_TEXT_VERBOSE
              foreground = LOG_PRIORITY_FG_VERBOSE
              background = LOG_PRIORITY_BG_VERBOSE
            }
          }

          val span = Span.obtain(0, get(text))
          spans.add(span)

          val pSpan =
            Span.obtain(
              token.startIndex - 1,
              TextStyle.makeStyle(
                /* foregroundColorId = */ foreground,
                /* backgroundColorId = */ background,
                /* bold = */ false,
                /* italic = */ false,
                /* strikeThrough = */ false,
                /* noCompletion = */ true
              )
            )
          spans.add(pSpan)

          // Add the normal color span after the priority tag
          spans.add(span.copy().apply { column = token.endIndex + 2 })
        }
      }
    }

    if (spans.isEmpty()) {
      spans.add(Span.obtain(0, get(LOG_TEXT_DEBUG)))
    }

    return spans
  }

  private fun CharSequence.toArray(): CharArray {
    return CharArray(this.length) { this[it] }
  }
}
