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

package com.itsaky.androidide.lsp.xml

import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.progress.ICancelChecker

/** @author Akash Yadav */
class CompletionHelperImpl : CompletionHelper {
  override fun complete(transform: (CompletionItem) -> CharSequence): Pair<Boolean, List<CharSequence>> {
    return XMLLSPTest.run {
      val createCompletionParams = createCompletionParams()
      val result = server.complete(createCompletionParams)
      result.isIncomplete to
        result.items
          .filter { it.ideLabel.isNotBlank() }
          .map { transform(it) }
          .filter { it.isNotBlank() }
          .toList()
    }
  }

  private fun createCompletionParams(): CompletionParams {
    return XMLLSPTest.run {
      val cursor = cursorPosition(true)
      val completionParams = CompletionParams(cursor, file!!, ICancelChecker.NOOP)
      completionParams.position.index = this.cursor
      completionParams.content = contents
      completionParams
    }
  }
}
