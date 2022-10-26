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
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.progress.ProcessCancelledException
import com.itsaky.androidide.progress.ProgressManager

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
  @SuppressLint("NotifyDataSetChanged")
  fun filter(query: String?) {
    filterThread?.let { ProgressManager.instance.cancel(it) }
    filterThread =
      FilterThread(this.items, query?.trim(), this::onFilter) { filtered, result ->
        if (result == null) {
          this.filtered = this.items
          notifyDataSetChanged()
          return@FilterThread
        }
        this.filtered = filtered
        result.dispatchUpdatesTo(this)
      }
    filterThread?.start()
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
    val onFilter: (E, String) -> Boolean,
    val onFiltered: (List<E>, DiffResult?) -> Unit
  ) : Thread("TaskFilterThread") {

    override fun run() {
      try {
        doFilter()
      } catch (e: ProcessCancelledException) {
        // ignored
      }
    }
  
    private fun doFilter() {
      if (query.isNullOrBlank()) {
        postFilter(items, null)
        return
      }
  
      val filtered = items.filter {
        ProgressManager.abortIfCancelled()
        onFilter(it, query)
      }
  
      val result =
        DiffUtil.calculateDiff(
          object : Callback() {
            override fun getOldListSize(): Int {
              ProgressManager.abortIfCancelled()
              return items.size
            }
        
            override fun getNewListSize(): Int {
              ProgressManager.abortIfCancelled()
              return filtered.size
            }
        
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
              ProgressManager.abortIfCancelled()
              return items[oldItemPosition] == filtered[newItemPosition]
            }
        
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
              ProgressManager.abortIfCancelled()
              return items[oldItemPosition] == filtered[newItemPosition]
            }
          }
        )
  
      postFilter(filtered, result)
    }
  
    private fun postFilter(filtered: List<E>, result: DiffResult?) {
      ThreadUtils.runOnUiThread {
        onFiltered(filtered, result)
      }
    }
  }
}
