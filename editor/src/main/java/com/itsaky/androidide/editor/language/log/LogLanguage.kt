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

import android.os.Bundle
import com.itsaky.androidide.editor.language.IDELanguage
import io.github.rosemoe.sora.lang.completion.CompletionPublisher
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.ContentReference
import io.github.rosemoe.sora.widget.SymbolPairMatch.DefaultSymbolPairs

/** @author Akash Yadav */
class LogLanguage @JvmOverloads constructor(val analyzer: LogLineAnalyzer = LogLineAnalyzer()) :
  IDELanguage() {

  override fun getAnalyzeManager() = analyzer

  override fun requireAutoComplete(
    content: ContentReference,
    position: CharPosition,
    publisher: CompletionPublisher,
    extraArguments: Bundle
  ) {}

  override fun destroy() {}

  override fun getInterruptionLevel() = INTERRUPTION_LEVEL_NONE
  override fun getIndentAdvance(content: ContentReference, line: Int, column: Int) = 0
  override fun getSymbolPairs() = DefaultSymbolPairs()
  override fun getNewlineHandlers() = emptyArray<NewlineHandler>()
}
