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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.itsaky.androidide.databinding.FragmentEmptyStateBinding
import com.itsaky.androidide.viewmodel.EmptyStateFragmentViewModel

/**
 * A fragment that shows a message when there is no data to show in the subclass fragment.
 *
 * @author Akash Yadav
 */
abstract class EmptyStateFragment<T : ViewBinding> : FragmentWithBinding<T> {

  constructor(layout: Int, bind: (View) -> T) : super(layout, bind)
  constructor(inflate: (LayoutInflater, ViewGroup?, Boolean) -> T) : super(inflate)

  protected var emptyStateBinding: FragmentEmptyStateBinding? = null
    private set

  protected val emptyStateViewModel by viewModels<EmptyStateFragmentViewModel>()

  internal var isEmpty: Boolean
    get() = emptyStateViewModel.isEmpty.value ?: false
    set(value) {
      emptyStateViewModel.isEmpty.value = value
    }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View {

    return FragmentEmptyStateBinding.inflate(inflater, container, false).also { emptyStateBinding ->
      this.emptyStateBinding = emptyStateBinding

      // add the main fragment view
      emptyStateBinding.root.addView(
        super.onCreateView(inflater, emptyStateBinding.root, savedInstanceState)
      )
    }.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    emptyStateViewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
      emptyStateBinding?.apply {
        root.displayedChild = if (isEmpty) 0 else 1
      }
    }

    emptyStateViewModel.emptyMessage.observe(viewLifecycleOwner) { message ->
      emptyStateBinding?.emptyView?.message = message
    }
  }

  override fun onDestroyView() {
    this.emptyStateBinding = null
    super.onDestroyView()
  }
}