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

package com.itsaky.androidide.editor.ui

import android.os.Handler
import io.github.rosemoe.sora.lang.completion.CompletionItem
import io.github.rosemoe.sora.lang.completion.CompletionPublisher

/**
 * [CompletionPublisher] implementation for AndroidIDE.
 *
 * @author Akash Yadav
 */
class IDECompletionPublisher(
  handler: Handler,
  callback: Runnable,
  languageInterruptionLevel: Int
) : CompletionPublisher(handler, callback, languageInterruptionLevel) {

  init {
    setUpdateThreshold(1)
  }

  /**
   * Adds the given [completion items][items] to the completion list.
   */
  fun <CompletionItemT : CompletionItem> addLSPItems(items: Collection<CompletionItemT>) {
    super.addItems(items)
  }
}