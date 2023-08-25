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

package com.itsaky.androidide.fragments.sidebar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.itsaky.androidide.adapters.BuildVariantsAdapter
import com.itsaky.androidide.fragments.RecyclerViewFragment
import com.itsaky.androidide.viewmodel.EditorViewModel

/**
 * A fragment to show the list of Android modules and its build variants.
 *
 * @author Akash Yadav
 */
class BuildVariantsFragment : RecyclerViewFragment<BuildVariantsAdapter>() {

  private val viewModel by viewModels<EditorViewModel>(ownerProducer = { requireActivity() })

  override fun onCreateAdapter(): RecyclerView.Adapter<*> {
    return BuildVariantsAdapter(viewModel.buildVariants)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel._buildVariants.observe(viewLifecycleOwner) {

      // Setup the recyclerview each time build variants list is updated
      onSetupRecyclerView()
    }
  }
}