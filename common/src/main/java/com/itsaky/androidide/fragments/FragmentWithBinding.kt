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
 * Handles inflation and release of [ViewBinding][androidx.viewbinding.ViewBinding] for the fragment.
 *
 * @author Akash Yadav
 */
abstract class FragmentWithBinding<T : ViewBinding>(@LayoutRes layout: Int,
  private val bind: (View) -> T
) : BaseFragment(layout) {

  @Suppress("PropertyName")
  protected var _binding: T? = null

  protected val binding: T
    get() = checkNotNull(
      _binding) { "Cannot access ViewBinding. Fragment may have been destroyed." }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return super.onCreateView(inflater, container, savedInstanceState)!!
      .also(this::doBind)
  }

  protected fun doBind(it: View) {
    _binding = bind(it)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}