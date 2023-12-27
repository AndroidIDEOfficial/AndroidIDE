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
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding

/**
 * Handles inflation and release of [ViewHolder][androidx.viewbinding.ViewBinding] for the fragment.
 *
 * @author Akash Yadav
 */
abstract class FragmentWithBinding<T : ViewBinding> : BaseFragment {

  @Suppress("PropertyName")
  protected var _binding: T? = null

  protected val binding: T
    get() = checkNotNull(
      _binding) { "Cannot access ViewHolder. Fragment may have been destroyed." }

  private var bind: ((View) -> T)? = null

  private var inflate: ((LayoutInflater, ViewGroup?, Boolean) -> T)? = null

  constructor(@LayoutRes layout: Int, bind: (View) -> T) : super(layout) {
    this.bind = bind
  }

  constructor(inflate: (LayoutInflater, ViewGroup?, Boolean) -> T) {
    this.inflate = inflate
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    this.bind?.let { bind ->
      return super.onCreateView(inflater, container, savedInstanceState)!!
        .also {
          _binding = bind(it)
        }
    }

    return inflate!!.invoke(inflater, container, false).let {
      _binding = it
      binding.root
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}