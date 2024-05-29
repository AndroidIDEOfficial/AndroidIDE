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

package com.itsaky.androidide.utils

import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import kotlin.math.ceil

/**
 * @author Akash Yadav
 */
object FlexboxUtils {

  @JvmStatic
  inline fun <T: RecyclerView.Adapter<*>> createGlobalLayoutListenerToDistributeFlexboxItemsEvenly(
    crossinline adapterProvider: () -> T?,
    crossinline layoutManagerProvider: () -> FlexboxLayoutManager?,
    crossinline fillDiff: (T, diff: Int) -> Unit
  ): ViewTreeObserver.OnGlobalLayoutListener {

    return object : ViewTreeObserver.OnGlobalLayoutListener {
      override fun onGlobalLayout() {

        val adapter = adapterProvider() ?: return
        val layoutManager = layoutManagerProvider() ?: return

        val columns = layoutManager.flexLinesInternal.firstOrNull()?.itemCount ?: 0
        if (columns == 0) {
          return
        }

        val itemCount = adapter.itemCount
        val rows = ceil(itemCount.toFloat() / columns.toFloat()).toInt()
        if (itemCount % columns == 0) {
          return
        }

        val diff = rows * columns - itemCount
        if (diff <= 0) {
          return
        }

        fillDiff(adapter, diff)
      }
    }
  }
}