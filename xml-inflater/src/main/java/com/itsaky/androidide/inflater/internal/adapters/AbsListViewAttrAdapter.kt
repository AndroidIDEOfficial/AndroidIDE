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
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.IView

/**
 * Attribute adapter for [AbsListView].
 *
 * @author Akash Yadav
 */
abstract class AbsListViewAttrAdapter : AdapterViewAttrAdapter() {

  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<AbsListView>(view, attribute) { _, context, _, _, name, value ->
      var applied = true
      when (name) {
        "cacheColorHint" -> cacheColorHint = parseColor(context, value)
        "choiceMode" -> choiceMode = parseChoiceMode(value)
        "drawSelectorOnTop" -> isDrawSelectorOnTop = parseBoolean(value)
        "fastScrollEnabled" -> isFastScrollEnabled = parseBoolean(value)
        "listSelector" -> selector = parseDrawable(context, value)
        "smoothScrollbar" -> isSmoothScrollbarEnabled = parseBoolean(value)
        "stackFromBottom" -> isStackFromBottom = parseBoolean(value)
        "textFilterEnabled" -> isTextFilterEnabled = parseBoolean(value)
        "transcriptMode" -> transcriptMode = parseTranscriptMode(value)
        else -> applied = false
      }

      if (!applied) {
        applied = super.apply(view, attribute)
      }

      return@doApply applied
    }
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
