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
import android.widget.ArrayAdapter
import android.widget.ListView
import com.blankj.utilcode.util.SizeUtils
import com.itsaky.inflater.IAttribute
import com.itsaky.inflater.IResourceTable

/** @author Akash Yadav */
class ListViewAttrAdapter(resourceTable: IResourceTable, displayMetrics: DisplayMetrics) :
    AbsListViewAttrAdapter(resourceTable, displayMetrics) {

    override fun isApplicableTo(view: View?): Boolean {
        return view is ListView
    }

    override fun apply(attribute: IAttribute, view: View): Boolean {
        val list = view as ListView
        val value = attribute.value
        val context = list.context

        if (!canHandleNamespace(attribute)) {
            return false
        }

        var handled = true

        when (attribute.attributeName) {
            "divider" -> list.divider = parseDrawable(value, context)
            "dividerHeight" ->
                list.dividerHeight = parseDimension(value, SizeUtils.dp2px(1f), displayMetrics)
            "entries" -> {
                val entries = parseArray(value)
                list.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, entries)
            }
            "footerDividersEnabled" -> list.setFooterDividersEnabled(parseBoolean(value))
            "headerDividersEnabled" -> list.setHeaderDividersEnabled(parseBoolean(value))
        }

        if (!handled) {
            handled = super.apply(attribute, view)
        }

        return handled
    }
}
