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

package com.itsaky.androidide.language.log

import com.itsaky.androidide.language.log.LogLineAnalyzer.LogLineState
import com.itsaky.androidide.lexers.log.LogToken
import io.github.rosemoe.sora.lang.analysis.AsyncIncrementalAnalyzeManager
import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult
import io.github.rosemoe.sora.lang.styling.CodeBlock
import io.github.rosemoe.sora.lang.styling.Span
import io.github.rosemoe.sora.text.Content

/**
 * Analyzer for log lines.
 *
 * @author Akash Yadav
 */
class LogLineAnalyzer : AsyncIncrementalAnalyzeManager<LogLineState, LogToken>() {
  
  data class LogLineState(val line: Int)
  
  override fun getInitialState() = LogLineState(-1)
  override fun stateEquals(state: LogLineState?, another: LogLineState?) = state?.equals(another) ?: false
  override fun computeBlocks(text: Content?, delegate: CodeBlockAnalyzeDelegate?) = mutableListOf<CodeBlock>()
  
  override fun tokenizeLine(line: CharSequence?, state: LogLineState?): LineTokenizeResult<LogLineState, LogToken> {
    TODO("Not yet implemented")
  }
  
  override fun generateSpansForLine(tokens: LineTokenizeResult<LogLineState, LogToken>?): MutableList<Span> {
    TODO("Not yet implemented")
  }
}