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
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.itsaky.androidide.R
import com.itsaky.androidide.activities.MainActivity
import com.itsaky.androidide.adapters.ProjectListAdapter
import com.itsaky.androidide.databinding.FragmentProjectListBinding
import com.itsaky.androidide.models.ProjectInfoDetails
import com.itsaky.androidide.models.toProjectInfoDetails
import com.itsaky.androidide.provider.IDEViewModelProvider
import com.itsaky.androidide.utils.ProjectInfoDetailsUtils
import com.itsaky.androidide.utils.appendClickableSpan
import com.itsaky.androidide.viewmodel.MainViewModel
import com.itsaky.androidide.viewmodel.ProjectInfoViewModel
import java.io.File


class ProjectListFragment :
  FragmentWithBinding<FragmentProjectListBinding>(R.layout.fragment_project_list,
    FragmentProjectListBinding::bind) {

  private var adapter: ProjectListAdapter? = null

  private val viewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })
  private val projectInfoViewModel: ProjectInfoViewModel by viewModels { IDEViewModelProvider.Factory }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupRecyclerView()
    observeProjectInfoList()

    projectInfoViewModel.getAllProjectInfo()

    binding.exitButton.setOnClickListener {
      viewModel.setScreen(MainViewModel.SCREEN_MAIN)
    }

  }

  private fun setupRecyclerView() {
    val layoutManager = LinearLayoutManager(requireContext())
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    binding.list.layoutManager = layoutManager

  }

  private fun observeProjectInfoList() {
    projectInfoViewModel.projectInfoList.observe(viewLifecycleOwner) {
      val projectList = it.map { projectInfo ->
        projectInfo.toProjectInfoDetails()
      }

      onFileListChanged(projectList)
    }

  }

  private fun onFileListChanged(projectList: List<ProjectInfoDetails>) {
    val sortOptions = ProjectInfoDetailsUtils.ProjectSortOptions(
      ProjectInfoDetailsUtils.SortBy.OPEN_LAST, ProjectInfoDetailsUtils.Order.DESCENDING)
      .createComparator()
    val sortedProjectList = projectList.sortedWith(sortOptions)

    adapter = ProjectListAdapter(sortedProjectList) { project, _ ->
      openProject(project.file)
    }

    binding.list.adapter = adapter
    setupNoProjectList(sortedProjectList.isEmpty())
  }

  private fun setupNoProjectList(isEmpty: Boolean) {
    binding.list.visibility = if (isEmpty) View.GONE else View.VISIBLE
    binding.noOpenProjectsSummary.visibility = if (isEmpty) View.VISIBLE else View.GONE

    binding.noOpenProjectsSummary.movementMethod = LinkMovementMethod()
    val openExistingProjectSpan: ClickableSpan = object : ClickableSpan() {
      override fun onClick(widget: View) {
        pickDirectory()
      }
    }

    val sb = SpannableStringBuilder()
    sb.appendClickableSpan(
      R.string.msg_empty_recent_projects,
      openExistingProjectSpan,
      requireContext())
    binding.noOpenProjectsSummary.text = sb
  }

  private fun openProject(root: File) {
    (requireActivity() as MainActivity).openProject(root)
  }

  private fun pickDirectory() {
    pickDirectory(this::openProject)
  }
}