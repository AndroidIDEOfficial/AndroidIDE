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
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.itsaky.androidide.R
import com.itsaky.androidide.activities.MainActivity
import com.itsaky.androidide.adapters.ProjectListAdapter
import com.itsaky.androidide.databinding.FragmentProjectListBinding
import com.itsaky.androidide.models.ProjectItem
import com.itsaky.androidide.viewmodel.MainViewModel
import java.io.File


class ProjectListFragment :
  FragmentWithBinding<FragmentProjectListBinding>(R.layout.fragment_project_list,
    FragmentProjectListBinding::bind) {

  private var adapter: ProjectListAdapter? = null
  private var layoutManager: FlexboxLayoutManager? = null

  private lateinit var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

  private val viewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
    layoutManager!!.justifyContent = JustifyContent.SPACE_EVENLY
    binding.list.layoutManager = layoutManager


    reloadRecentProjects()

    binding.exitButton.setOnClickListener {
      viewModel.setScreen(MainViewModel.SCREEN_MAIN)
    }

  }

  fun reloadRecentProjects() {
    adapter = ProjectListAdapter(ProjectItem.getProjects(context = requireContext())) { project, _ ->
      openProject(project.path.toFile())
    }
    binding.list.adapter = adapter
  }

  fun openProject(root: File) {
    (requireActivity() as MainActivity).openProject(root)
  }
}