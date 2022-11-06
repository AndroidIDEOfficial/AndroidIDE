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
import android.widget.ArrayAdapter

/**
 * Attribute adapter for [AdapterView][android.widget.AdapterView]
 *
 * @author Akash Yadav
 */
abstract class AdapterViewAttrAdapter : ViewAttrAdapter() {
  
  protected open fun newSimpleAdapter(ctx: Context): ArrayAdapter<String> {
    return newSimpleAdapter(ctx, newAdapterItems(4))
  }
  
  protected open fun newSimpleAdapter(ctx: Context, items: Array<String>): ArrayAdapter<String> {
    return ArrayAdapter<String>(ctx, layout.simple_list_item_1, items)
  }
  
  protected open fun newAdapterItems(size: Int): Array<String> {
    return Array(size) { "Item $it" }
  }
}