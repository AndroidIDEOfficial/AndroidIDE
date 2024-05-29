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

package com.itsaky.androidide.ui

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.itsaky.androidide.adapters.ContributorsGridAdapter
import com.itsaky.androidide.utils.FlexboxUtils

class ContributorsRecyclerView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

  private var globalLayoutListener = FlexboxUtils.createGlobalLayoutListenerToDistributeFlexboxItemsEvenly(
    { adapter }, { layoutManager as FlexboxLayoutManager }) { adapter, extras ->
    (adapter as ContributorsGridAdapter).fillDiff(extras)
  }

  init {
    layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW).apply {
      justifyContent = JustifyContent.SPACE_EVENLY
    }
    viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
  }
}