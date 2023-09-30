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

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.Callback
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Filterable [RecyclerView.Adapter].
 *
 * @author Akash Yadav
 */
abstract class FilterableRecyclerViewAdapter<V : RecyclerView.ViewHolder, D>(val items: List<D>) :
  RecyclerView.Adapter<V>() {

  protected var filtered: List<D> = mutableListOf<D>().apply { addAll(items) }
  private var filterJob: Job? = null

  /**
   * Filter the list with the given query.
   *
   * @param query The query.
   */
  @SuppressLint("NotifyDataSetChanged")
  fun filter(query: String?) {
    filterJob?.cancel(CancellationException("A new query has been submitted for filtering"))

    val items = this.items
    filterJob = CoroutineScope(Dispatchers.Default).launch {
      val (filtered, result) = doFilter(query?.trim(), items)

      withContext(Dispatchers.Main) {
        val adapter = this@FilterableRecyclerViewAdapter
        if (result == null) {
          adapter.filtered = adapter.items
          notifyDataSetChanged()
          return@withContext
        }

        adapter.filtered = filtered
        result.dispatchUpdatesTo(adapter)
      }
    }
  }

  private fun doFilter(
    query: String?,
    items: List<D>,
  ): Pair<List<D>, DiffResult?> {
    if (query.isNullOrBlank()) {
      return items to null
    }

    val filtered = items.filter {
      onFilter(it, query)
    }

    val result =
      DiffUtil.calculateDiff(
        object : Callback() {
          override fun getOldListSize(): Int {
            return items.size
          }

          override fun getNewListSize(): Int {
            return filtered.size
          }

          override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return items[oldItemPosition] == filtered[newItemPosition]
          }

          override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return items[oldItemPosition] == filtered[newItemPosition]
          }
        }
      )

    return filtered to result
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
}
