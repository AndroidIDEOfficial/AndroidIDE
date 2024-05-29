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

package com.itsaky.androidide.inflater.internal.adapters

import android.widget.AbsListView
import com.itsaky.androidide.inflater.AttributeHandlerScope

/**
 * Attribute adapter for [AbsListView].
 *
 * @author Akash Yadav
 */
abstract class AbsListViewAdapter<T : AbsListView> : AdapterViewAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("cacheColorHint") { view.cacheColorHint = parseColor(context, value) }
    create("choiceMode") { view.choiceMode = parseChoiceMode(value) }
    create("drawSelectorOnTop") { view.isDrawSelectorOnTop = parseBoolean(value) }
    create("fastScrollEnabled") { view.isFastScrollEnabled = parseBoolean(value) }
    create("listSelector") { view.selector = parseDrawable(context, value) }
    create("smoothScrollbar") { view.isSmoothScrollbarEnabled = parseBoolean(value) }
    create("stackFromBottom") { view.isStackFromBottom = parseBoolean(value) }
    create("textFilterEnabled") { view.isTextFilterEnabled = parseBoolean(value) }
    create("transcriptMode") { view.transcriptMode = parseTranscriptMode(value) }
  }

  protected open fun parseTranscriptMode(value: String): Int {
    return when (value) {
      "normal" -> AbsListView.TRANSCRIPT_MODE_NORMAL
      "alwaysScroll" -> AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL
      else -> AbsListView.TRANSCRIPT_MODE_DISABLED
    }
  }

  protected open fun parseChoiceMode(value: String): Int {
    return when (value) {
      "multipleChoice" -> AbsListView.CHOICE_MODE_MULTIPLE
      "multipleChoiceModal" -> AbsListView.CHOICE_MODE_MULTIPLE_MODAL
      "singleChoice" -> AbsListView.CHOICE_MODE_SINGLE
      else -> AbsListView.CHOICE_MODE_NONE
    }
  }
}
