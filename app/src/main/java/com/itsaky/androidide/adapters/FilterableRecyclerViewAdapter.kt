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

package com.itsaky.androidide.adapters

import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.progress.ProgressManager
import com.itsaky.androidide.utils.StopWatch

/**
 * Filterable [RecyclerView.Adapter].
 *
 * @author Akash Yadav
 */
abstract class FilterableRecyclerViewAdapter<V : RecyclerView.ViewHolder, D>(val items: List<D>) :
  RecyclerView.Adapter<V>() {

  protected var filtered: List<D> = mutableListOf<D>().apply { addAll(items) }
  private var filterThread: FilterThread<D>? = null

  /**
   * Filter the list with the given query.
   *
   * @param query The query.
   */
  fun filter(query: String?) {
    filterThread?.let { ProgressManager.instance.cancel(it) }
    filterThread =
      FilterThread(this.items, query?.trim(), this::onFilter).apply {
        start()
        join()
      }

    filterThread!!.let {
      ThreadUtils.runOnUiThread {
        this.filtered = it.filtered
        notifyDataSetChanged()
      }
    }
  }

  /** Get the list item at given index. */
  fun getItem(index: Int): D {
    return filtered[index]
  }

  override fun getItemCount(): Int {
    return filtered.size
  }

  /** Get the query candidate for the given list item. */
  abstract fun getQueryCandidate(item: D): String

  /** Called on every item when filtering the data. */
  protected open fun onFilter(item: D, query: String): Boolean {
    return getQueryCandidate(item).contains(query, ignoreCase = true)
  }

  private class FilterThread<E>(
    val items: List<E>,
    val query: String?,
    val onFilter: (E, String) -> Boolean
  ) : Thread("TaskFilterThread") {

    lateinit var filtered: List<E>

    override fun run() {
      this.filtered =
        if (query.isNullOrBlank()) {
          items
        } else
          items.filter {
            ProgressManager.abortIfCancelled()
            onFilter(it, query)
          }
    }
  }
}
