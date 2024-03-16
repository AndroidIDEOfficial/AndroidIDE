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

package com.itsaky.androidide.lsp.util

import com.itsaky.androidide.lsp.api.ICompletionProvider

/** @author Akash Yadav */
class NoCompletionsProvider : ICompletionProvider {
  override fun complete(params: com.itsaky.androidide.lsp.models.CompletionParams?): com.itsaky.androidide.lsp.models.CompletionResult = com.itsaky.androidide.lsp.models.CompletionResult.EMPTY
}
