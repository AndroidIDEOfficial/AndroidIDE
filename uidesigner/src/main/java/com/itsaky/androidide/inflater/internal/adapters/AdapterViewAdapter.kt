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

import android.R.layout
import android.content.Context
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.ViewGroupImpl

/**
 * Attribute adapter for [AdapterView][android.widget.AdapterView]
 *
 * @author Akash Yadav
 */
abstract class AdapterViewAdapter<T : AdapterView<*>> : ViewGroupAdapter<T>() {

  companion object {
    const val ADAPTER_DEFAULT_ITEM_COUNT = 3
  }

  override fun applyBasic(view: IView) {
    super.applyBasic(view)
    (view.view as AdapterView<*>).adapter = newSimpleAdapter(view.view.context)
    if (view is ViewGroupImpl) {
      view.childrenModifiable = false
    }
  }

  protected open fun newSimpleAdapter(ctx: Context): ArrayAdapter<String> {
    return newSimpleAdapter(ctx, newAdapterItems(ADAPTER_DEFAULT_ITEM_COUNT))
  }

  protected open fun newSimpleAdapter(ctx: Context, items: Array<String>): ArrayAdapter<String> {
    return ArrayAdapter<String>(ctx, layout.simple_list_item_1, items)
  }

  protected open fun newAdapterItems(size: Int): Array<String> {
    return Array(size) { "Item $it" }
  }
}
