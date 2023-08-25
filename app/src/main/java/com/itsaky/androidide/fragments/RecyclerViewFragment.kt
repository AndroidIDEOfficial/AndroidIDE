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

package com.itsaky.androidide.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.itsaky.androidide.databinding.FragmentRecyclerviewBinding

/**
 * A fragment which shows a [RecyclerView].
 *
 * @author Akash Yadav
 */
abstract class RecyclerViewFragment<A : RecyclerView.Adapter<*>> :
  EmptyStateFragment<FragmentRecyclerviewBinding>(FragmentRecyclerviewBinding::inflate) {

  private var unsavedAdapter: A? = null

  /**
   * Creates the adapter for the [RecyclerView].
   */
  protected abstract fun onCreateAdapter(): RecyclerView.Adapter<*>

  /**
   * Creates the layout manager for the [RecyclerView].
   */
  protected open fun onCreateLayoutManager(): LayoutManager {
    return LinearLayoutManager(requireContext())
  }

  /**
   * Sets up the recycler view in the fragment.
   */
  protected open fun onSetupRecyclerView() {
    binding.root.apply {
      layoutManager = onCreateLayoutManager()
      adapter = unsavedAdapter ?: onCreateAdapter()
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    onSetupRecyclerView()

    unsavedAdapter = null

    checkIsEmpty()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    unsavedAdapter = null
  }

  /**
   * Set the adapter for the [RecyclerView].
   */
  fun setAdapter(adapter: A) {
    _binding?.root?.let { list -> list.adapter = adapter } ?: run { unsavedAdapter = adapter }
    checkIsEmpty()
  }

  private fun checkIsEmpty() {
    emptyStateViewModel.isEmpty.value = _binding?.root?.adapter?.itemCount == 0
  }
}