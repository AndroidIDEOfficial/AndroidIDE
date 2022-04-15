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

package com.itsaky.inflater.adapters.android.widget

import android.util.DisplayMetrics
import android.view.View
import android.widget.AbsListView
import com.itsaky.inflater.IAttribute
import com.itsaky.inflater.IResourceTable

/**
 * Attribute adapter for the [AbsListView] widget.
 *
 * @author Akash Yadav
 */
open class AbsListViewAttrAdapter(resourceTable: IResourceTable, displayMetrics: DisplayMetrics) :
    AdapterViewAttrAdapter(resourceTable, displayMetrics) {

    override fun isApplicableTo(view: View?): Boolean {
        return view is AbsListView
    }

    override fun apply(attribute: IAttribute, view: View): Boolean {
        val list = view as AbsListView
        val context = list.context
        val value = attribute.value

        if (!canHandleNamespace(attribute)) {
            return false
        }

        var handled = true
        when (attribute.attributeName) {
            "cacheColorHint" -> list.cacheColorHint = parseColor(value, context)
            "choiceMode" -> list.choiceMode = parseChoiceMode(value)
            "drawSelectorOnTop" -> list.isDrawSelectorOnTop = parseBoolean(value)
            "fastScrollEnabled" -> list.isFastScrollEnabled = parseBoolean(value)
            "listSelector" -> list.selector = parseDrawable(value, context)
            "smoothScrollbar" -> list.isSmoothScrollbarEnabled = parseBoolean(value)
            "stackFromBottom" -> list.isStackFromBottom = parseBoolean(value)
            "textFilterEnabled" -> list.isTextFilterEnabled = parseBoolean(value)
            "transcriptMode" -> list.transcriptMode = parseTranscriptMode(value)
            else -> handled = false
        }

        if (!handled) {
            handled = super.apply(attribute, view)
        }

        return handled
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
